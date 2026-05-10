package com.timyr_tm.rust_bound.world.block.entity.render

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import org.joml.Vector3f

class ConnectableBlockEntityRenderState: BlockEntityRenderState() {
	var points: Set<WireRenderInfo> = setOf()
}