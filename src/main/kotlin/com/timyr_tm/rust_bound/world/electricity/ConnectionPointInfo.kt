package com.timyr_tm.rust_bound.world.electricity

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape

class ConnectionPointInfo: Iterable<Map.Entry<ConnectionPointerInfo, ResourceKey<WireType>>> {
    private val connections: MutableMap<ConnectionPointerInfo, ResourceKey<WireType>>

    lateinit var pos: BlockPos
    val point: Vec3
    val area: VoxelShape

    private constructor(point: Vec3, area: VoxelShape, connections: Map<ConnectionPointerInfo, ResourceKey<WireType>>) {
        this.point = point
        this.area = area
        this.connections = connections.toMutableMap()
    }

    constructor(point: Vec3, area: VoxelShape): this(point, area, emptyMap())

    constructor(point: ConnectionPointInfo): this(point.point, point.area, point.connections)

    fun clear() = connections.clear()

    override fun iterator(): Iterator<Map.Entry<ConnectionPointerInfo, ResourceKey<WireType>>> = this.connections.entries.iterator()

    operator fun set(pointer: ConnectionPointerInfo, wireType: ResourceKey<WireType>): ResourceKey<WireType>? = connections.put(pointer, wireType)

    operator fun get(pointer: ConnectionPointerInfo): ResourceKey<WireType>? = connections[pointer]

    operator fun minusAssign(pointer: ConnectionPointerInfo) {
        connections -= pointer
    }

    operator fun contains(pointer: ConnectionPointerInfo) = pointer in this.connections
}