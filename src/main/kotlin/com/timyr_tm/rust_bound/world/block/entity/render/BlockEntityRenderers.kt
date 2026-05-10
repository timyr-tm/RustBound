@file:EventBusSubscriber

package com.timyr_tm.rust_bound.world.block.entity.render

import com.timyr_tm.rust_bound.world.block.entity.BlockEntityTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@SubscribeEvent
fun onRegisterBlockEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
	event.registerBlockEntityRenderer(BlockEntityTypes.CONNECTOR_BLOCK_ENTITY_TYPE.get()) {
		_ -> ConnectableBlockEntityRenderer()
	}
}