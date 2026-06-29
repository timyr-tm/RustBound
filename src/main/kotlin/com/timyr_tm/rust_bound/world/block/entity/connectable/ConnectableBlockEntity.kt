package com.timyr_tm.rust_bound.world.block.entity.connectable

import com.timyr_tm.rust_bound.world.Level.getBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.function.Consumer
import kotlin.jvm.optionals.getOrNull

abstract class ConnectableBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): BlockEntity(type, pos, state) {
    val connections: PointsMap = PointsMap(blockPos, buildSet { createPoints(this::add) })

    protected open fun createPoints(consumer: Consumer<PointInfo>) { }

    override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        output.store("connections", PointsMap.CODEC, connections.save())
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        connections.load(input.read("connections", PointsMap.CODEC).getOrNull() ?: emptyMap())
    }

    override fun preRemoveSideEffects(pos: BlockPos, state: BlockState) {
        super.preRemoveSideEffects(pos, state)

        for ((point, connections) in this.connections) {
            for ((pointer, _) in connections) {
                val blockEntity = this.level!!.getBlockEntity(pointer) ?: continue
                blockEntity.connections[pointer.name]?.remove(PointerInfo(point.name, blockPos))
                blockEntity.setChanged()
                level!!.sendBlockUpdated(blockEntity.blockPos, blockEntity.blockState, blockEntity.blockState, Block.UPDATE_CLIENTS)
            }
        }
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = this.saveWithoutMetadata(registries)

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? = ClientboundBlockEntityDataPacket.create(this)
}