package com.timyr_tm.rust_bound.world.item

import com.timyr_tm.rust_bound.world.Level.getBlockEntity
import com.timyr_tm.rust_bound.world.WireType
import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.block.entity.connectable.PointerInfo
import net.minecraft.core.Holder
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

import com.timyr_tm.rust_bound.core.component.DataComponents.SPOOL_POINTER as POINTER

class SpoolItem(val wire: Holder<WireType>, properties: Properties): Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val firstPointer = context.itemInHand.remove(POINTER)

        val blockEntity = context.level.getBlockEntity(context.clickedPos) as? ConnectableBlockEntity
            ?: return InteractionResult.PASS

        val (point, _) = blockEntity.connections
            .firstOrNull {
                (point, _) -> point.region
                    .move(context.clickedPos)
                    .inflate(0.001)
                    .contains(context.clickLocation)
            }
            ?: return InteractionResult.PASS

        val pointer = PointerInfo(point.name, context.clickedPos)

        if (firstPointer == null) {
            context.itemInHand[POINTER] = pointer
            return InteractionResult.SUCCESS
        }

        val firstBlockEntity = context.level.getBlockEntity(firstPointer)
            ?: return InteractionResult.FAIL

        blockEntity.connections[point]!![firstPointer] = this.wire
        blockEntity.setChanged()

        firstBlockEntity.connections[firstPointer.name]!![pointer] = this.wire
        firstBlockEntity.setChanged()

        return InteractionResult.SUCCESS
    }
}
