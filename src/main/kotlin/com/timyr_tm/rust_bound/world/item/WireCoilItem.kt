package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.core.component.DataComponents
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import com.timyr_tm.rust_bound.world.electricity.WireType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3

class WireCoilItem: Item {
    val wireType: ResourceKey<WireType>

    constructor(wireType: ResourceKey<WireType>, properties: Properties): super(properties) {
        this.wireType = wireType;
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity: BlockEntity? = context.level.getBlockEntity(context.clickedPos)
        if (blockEntity is ConnectableBlockEntity) {
            val pos: Vec3 = context.clickLocation.subtract(Vec3(blockEntity.blockPos))
            val point: Map.Entry<String, ConnectionPointInfo>? = blockEntity.getConnectionPoints().entries.firstOrNull {
                info -> info.value.shape.toAabbs().any {
                    aabb -> aabb.inflate(0.001).contains(pos)
                }
            }
            if (point == null)
                return InteractionResult.FAIL
            if (context.itemInHand.has(DataComponents.WIRE_COIL_CONNECTION)) {
                val firstConnection: ConnectionInfo = context.itemInHand.remove(DataComponents.WIRE_COIL_CONNECTION)!!
                val lastConnection: ConnectionInfo = ConnectionInfo(point.key, point.value.pos, wireType)

                val lastBlockEntity: ConnectableBlockEntity = firstConnection.getBlockEntity(context.level)
                    ?: return InteractionResult.FAIL

                blockEntity.addConnection(lastConnection.name, firstConnection)
                lastBlockEntity.addConnection(firstConnection.name, lastConnection)
                blockEntity.setChanged()
                lastBlockEntity.setChanged()

                if (context.level.isClientSide && context.player != null)
                    context.player!!.displayClientMessage(
                        Component.literal(
                            "${firstConnection.name} (${firstConnection.pos}) - ${lastConnection.name} (${lastConnection.pos})"
                        ),
                        true
                    )
            }
            else {
                context.itemInHand.set(DataComponents.WIRE_COIL_CONNECTION, ConnectionInfo(point.key, point.value.pos, wireType))
                if (context.level.isClientSide && context.player != null)
                    context.player!!.displayClientMessage(Component.literal("+ ${point.key}"), true)
            }
            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }
}
