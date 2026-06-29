package com.timyr_tm.rust_bound.world.block.entity.render.connectable

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.resources.Identifier
import org.joml.Vector3fc

class ConnectableRenderState: BlockEntityRenderState(), Iterable<ConnectableRenderState.WireRenderInfo> {
	var renderers: Set<WireRenderInfo> = emptySet()

	override fun iterator(): Iterator<WireRenderInfo> = renderers.iterator()

	data class WireRenderInfo(
        val wire: Identifier,
        val points: List<Vector3fc>
	)
}