package com.timyr_tm.rust_bound.world.block.entity

import com.mojang.logging.LogUtils
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.slf4j.Logger
import java.util.function.BiConsumer

abstract class ConnectableBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): BlockEntity(type, pos, state) {
    private val logger: Logger = LogUtils.getLogger()

    var connections: Map<String, ConnectionPointInfo> = mapOf()
        private set

    open fun createConnections(connections: BiConsumer<String, ConnectionPointInfo>) {}

    fun disconnectAll() {
        for ((name, point) in this.connections)
            for (connection in point) {
                val blockEntity: ConnectableBlockEntity = connection.getBlockEntity(this.level!!) ?: continue
                blockEntity.connections[connection.name] ?: continue -= ConnectionPointerInfo(name, point.pos, connection.wireType)
            }
    }

    fun updateConnections() {
        val connections: MutableMap<String, ConnectionPointInfo> = mutableMapOf()
        createConnections(connections::put)

        for ((key, value) in connections.entries) {
            if (key in connections)
                connections[key]!! += value
            else for (connection in value) {
                val point: ConnectionPointInfo = connection.getBlockEntity(this.level!!)?.connections[connection.name] ?: continue
                point -= ConnectionPointerInfo(key, value.pos, connection.wireType)
            }
        }

        this.connections = connections.toMap()
    }

    override fun onLoad() {
        super.onLoad()
        updateConnections()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        updateConnections()
    }

    override fun setRemoved() {
        super.setRemoved()
        disconnectAll()
    }
}