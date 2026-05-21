package com.timyr_tm.rust_bound.world.item

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.entity.BlockEntity

class WireCutterItem(properties: Properties): Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity: BlockEntity? = context.level.getBlockEntity(context.clickedPos)
        if (blockEntity is ConnectableBlockEntity) {
            for ((key, value) in blockEntity.connections) {
                for ((connection, _) in value)
                    connection.getBlockEntity(context.level)?.connections[connection.name]?.minusAssign(ConnectionPointerInfo(key, value.pos))
                value.clear()
            }
            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }
}