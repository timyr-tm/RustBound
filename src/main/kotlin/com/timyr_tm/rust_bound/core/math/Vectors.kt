package com.timyr_tm.rust_bound.core.math

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import org.joml.Vector3fc
import kotlin.math.pow
import kotlin.math.round


fun Vector3fc.round(digits: Int): Vector3fc {
    val multiplier: Float = 10f.pow(digits)
    return Vector3f(
        round(this.x() * multiplier) / multiplier,
        round(this.y() * multiplier) / multiplier,
        round(this.z() * multiplier) / multiplier
    )
}

fun BlockPos.toVec3(): Vec3 = Vec3(this)

