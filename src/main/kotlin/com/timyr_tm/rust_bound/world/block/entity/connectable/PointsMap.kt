package com.timyr_tm.rust_bound.world.block.entity.connectable

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.timyr_tm.rust_bound.core.registries.Registries
import com.timyr_tm.rust_bound.world.WireType
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder

typealias ConnectionPair = Pair<PointerInfo, Holder<WireType>>

class PointsMap(private val blockPos: BlockPos,points: Set<PointInfo>): Iterable<Pair<PointInfo, PointsMap.ConnectionsMap>> {
    private val connections: MutableMap<PointInfo, ConnectionsMap> = points
        .associateWith { ConnectionsMap(blockPos, mutableMapOf()) }
        .toMutableMap()

    companion object {
        val PAIR_CODEC: Codec<ConnectionPair> = RecordCodecBuilder.create {
                instance -> instance
            .group(
                PointerInfo.CODEC.fieldOf("first").forGetter(ConnectionPair::first),
                Registries.WIRE_TYPE.holderByNameCodec().fieldOf("second").forGetter(ConnectionPair::second)
            )
            .apply(instance, ::ConnectionPair)
        }

        val CODEC: Codec<Map<String, List<ConnectionPair>>> = Codec.unboundedMap(Codec.STRING, Codec.list(PAIR_CODEC))
    }

    val points: Set<PointInfo> = this.connections.keys

    override fun iterator(): Iterator<Pair<PointInfo, ConnectionsMap>> = connections.entries
        .map { it.toPair() }
        .iterator()

    fun save(): Map<String, List<ConnectionPair>> = this.connections
        .map { (point, connections) -> point.name to connections.entries.map { it.toPair() } }
        .toMap()

    fun load(data: Map<String, List<ConnectionPair>>) = this.connections.keys.forEach {
        point -> this.connections[point] = ConnectionsMap(
            blockPos, mutableMapOf(
                *data[point.name]?.toTypedArray()
                    ?: return@forEach
            )
        )
    }

    operator fun get(point: PointInfo): ConnectionsMap? = this.connections[point]

    operator fun get(name: String): ConnectionsMap? = this.connections.entries
        .firstOrNull { it.key.name == name }
        ?.value

    operator fun set(point: PointInfo, connections: ConnectionsMap) {
        this.connections[point] = connections
    }

    operator fun set(name: String, connections: ConnectionsMap) = this.connections.entries
        .firstOrNull { it.key.name == name}
        ?.setValue(connections)

    operator fun contains(name: String): Boolean = this.connections.keys
        .any { it.name == name }

    override fun toString(): String = this.connections.toString()

    class ConnectionsMap(private val blockPos: BlockPos, private val self: MutableMap<PointerInfo, Holder<WireType>>): MutableMap<PointerInfo, Holder<WireType>> by self {
        override fun put(key: PointerInfo, value: Holder<WireType>): Holder<WireType>? {
            if (key.pos == this.blockPos)
                throw SelfConnectionExclusion()
            return self.put(key, value)
        }

        override fun putAll(from: Map<out PointerInfo, Holder<WireType>>) {
            if (from.any { it.key.pos == this.blockPos })
                throw SelfConnectionExclusion()
            self.putAll(from)
        }
    }
}