package com.timyr_tm.rust_bound.world.block.entity.render

import net.minecraft.resources.Identifier
import org.joml.Vector3f

data class WireRenderInfo(
	val start: Vector3f,
	val end: Vector3f,
	val texture: Identifier
)
