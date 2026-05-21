package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.core.component.DataComponents
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import com.timyr_tm.rust_bound.world.electricity.WireType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.util.CommonColors
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer

class SpoolItem(val wireType: ResourceKey<WireType>, properties: Properties): Item(properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity: BlockEntity? = context.level.getBlockEntity(context.clickedPos)
        if (blockEntity is ConnectableBlockEntity) {
            val pos: Vec3 = context.clickLocation.subtract(Vec3(blockEntity.blockPos))
            val point: Map.Entry<String, ConnectionPointInfo>? = blockEntity.connections.entries.firstOrNull {
                info -> info.value.area.toAabbs().any {
                    aabb -> aabb.inflate(0.001).contains(pos)
                }
            }
            if (point == null)
                return InteractionResult.FAIL
            if (context.itemInHand.has(DataComponents.SPOOL_POINTER)) {
                val firstConnection: ConnectionPointerInfo = context.itemInHand.remove(DataComponents.SPOOL_POINTER)!!

                val lastBlockEntity: ConnectableBlockEntity = firstConnection.getBlockEntity(context.level)
                    ?: return InteractionResult.FAIL

                val lastConnection = ConnectionPointerInfo(point.key, point.value.pos)

                blockEntity.connections[point.key]!![firstConnection] = wireType
                blockEntity.setChanged()

                lastBlockEntity.connections[firstConnection.name]!![lastConnection] = wireType
                lastBlockEntity.setChanged()

                if (context.level.isClientSide && context.player != null)
                    context.player!!.displayClientMessage(
                        Component.literal(
                            "${firstConnection.name} (${firstConnection.pos}) - ${point.key} (${point.value.pos})"
                        ),
                        true
                    )
            }
            else {
                context.itemInHand.set(DataComponents.SPOOL_POINTER, ConnectionPointerInfo(point.key, point.value.pos))
                if (context.level.isClientSide && context.player != null)
                    context.player!!.displayClientMessage(Component.literal("+ ${point.key}"), true)
            }
            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun appendHoverText(stack: ItemStack, context: TooltipContext, display: TooltipDisplay, adder: Consumer<Component>, flag: TooltipFlag) {
        val pointer: ConnectionPointerInfo = stack.get(DataComponents.SPOOL_POINTER) ?: return
        val color: Int = if (context.level() != null && pointer.getBlockEntity(context.level()!!)?.connections?.containsKey(pointer.name) ?: false) CommonColors.GRAY else CommonColors.RED
        adder.accept(
            Component.translatable("spool.pointer", pointer.name, pointer.pos.toShortString())
                .withColor(color)
        )
    }
}
