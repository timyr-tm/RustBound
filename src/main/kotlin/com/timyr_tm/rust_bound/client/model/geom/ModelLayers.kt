package com.timyr_tm.rust_bound.client.model.geom

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.client.model.`object`.WireSegmentModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.Identifier
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber
object ModelLayers {
	val WIRE_SEGMENT: ModelLayerLocation = ModelLayerLocation(Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "wire_segment"), "main")

	@SubscribeEvent
	fun onRegisterLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
		event.registerLayerDefinition(WIRE_SEGMENT, WireSegmentModel::createBodyLayer)
	}
}