package com.timyr_tm.rust_bound.world.block.entity

import com.timyr_tm.rust_bound.world.electricity.ConnectionInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

abstract class ConnectableBlockEntity: BlockEntity {
    private val connections: MutableMap<String, MutableSet<ConnectionInfo>> = mutableMapOf()
    private val connectionPoints: MutableMap<String, ConnectionPointInfo> = mutableMapOf()

    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    override fun onLoad() {
        super.onLoad()
        createConnectionPoints(connectionPoints::put)
        for (name in connections.keys) {
            if (connectionPoints.containsKey(name))
                continue
            clearConnection(name)
        }
    }

    private fun updateConnectionPoints() {
        assert(level != null)
        connectionPoints.clear()
        for (name in connectionPoints.keys) {
            if (connectionPoints.containsKey(name)) continue
            clearConnection(name)
        }
    }

    open fun createConnectionPoints(connections: BiConsumer<String, ConnectionPointInfo>) {}

    fun getConnectionPoints(): MutableMap<String, ConnectionPointInfo> = connectionPoints

    fun addConnection(name: String, connection: ConnectionInfo): Boolean {
        if (!connectionPoints.containsKey(name))
            return false
        if (!connections.containsKey(name))
            connections[name] = mutableSetOf()
        return connections[name]!!.add(connection)
    }

    fun removeConnection(name: String, connection: ConnectionInfo): Boolean {
        if (!connections.containsKey(name))
            return false;
        if (connections[name]!!.count() == 1)
            return connections.remove(name) != null;
        return connections[name]!!.remove(connection);
    }

    fun clearConnection(name: String) {
        if (level == null || !connections.containsKey(name))
            return

        for(connection in connections[name]!!) {
            val blockEntity: ConnectableBlockEntity = connection.getBlockEntity(level!!) ?: continue;
            blockEntity.removeConnection(connection.name, ConnectionInfo(name, blockPos, connection.wireType));
        }
    }

    fun getConnections(name: String): Set<ConnectionInfo>? = connections[name]

    override fun setRemoved() {
        if (level != null)
            for (connectionEntry in connections )
                for (connection in connectionEntry.value) {
                    val blockEntity: ConnectableBlockEntity = connection.getBlockEntity(level!!) ?: continue
                    blockEntity.removeConnection(connection.name, ConnectionInfo(connectionEntry.key, blockPos, connection.wireType))
                    blockEntity.setChanged()
                }
        super.setRemoved()
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? = ClientboundBlockEntityDataPacket.create(this)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        this.updateConnectionPoints()
    }
}