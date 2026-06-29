package com.timyr_tm.rust_bound.world.block.entity.connectable

import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class PointInfo (val name: String, val pos: Vec3, val region: AABB) {
    constructor(point: PointInfo): this(point.name, point.pos, point.region)

    fun equals(point: PointInfo?): Boolean = this.name == point?.name

    fun equals(name: String?): Boolean = this.name == name

    override fun equals(other: Any?): Boolean = this.equals(other as? PointInfo) || this.equals(other as? String)

    override fun hashCode(): Int = this.name.hashCode()

    override fun toString(): String = this.name
}