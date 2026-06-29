package com.timyr_tm.rust_bound.world.item

import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.entity.BlockEntity

class WireCutterItem(properties: Properties): Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity: BlockEntity? = context.level.getBlockEntity(context.clickedPos)
        if (blockEntity is ConnectableBlockEntity) {

            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }
}