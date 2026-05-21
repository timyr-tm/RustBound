package com.timyr_tm.rust_bound.world.block.entity

import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.function.BiConsumer

abstract class ConnectableBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): BlockEntity(type, pos, state) {
    lateinit var connections: Map<String, ConnectionPointInfo>
        private set

    protected open fun registerConnections(connections: BiConsumer<String, ConnectionPointInfo>) {}

    protected open fun updateConnections(connections: BiConsumer<String, ConnectionPointInfo>) {}

    override fun setLevel(level: Level) {
        super.setLevel(level)
        connections = buildMap {
            registerConnections {
                key, value -> this[key] = value.apply {
                    pos = blockPos
                }
            }
        }
    }

    override fun onLoad() {
        super.onLoad()
        connections += buildMap {
            updateConnections {
                key, value -> this[key.takeIf { it in this } ?: throw NullPointerException()] = value.apply {
                    pos = blockPos
                }
            }
        }
    }

    override fun setRemoved() {
        super.setRemoved()
        connections.forEach {
            (name, point) -> point.forEach {
                (pointer, _) -> pointer.getBlockEntity(level!!)?.connections[pointer.name]?.minusAssign(
                    ConnectionPointerInfo(name, point.pos)
                )
            }
        }
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
    }

    override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
    }
}