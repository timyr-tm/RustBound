package com.timyr_tm.rust_bound.client.model.geom

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.client.model.`object`.wire.WireSegmentModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import java.util.function.Supplier

@EventBusSubscriber(Dist.CLIENT, modid = RustBound.MOD_ID)
object ModelLayers {
	private val LAYERS: MutableMap<ModelLayerLocation, Supplier<LayerDefinition>> = mutableMapOf()

	private fun register(location: ModelLayerLocation, supplier: Supplier<LayerDefinition>): ModelLayerLocation {
		LAYERS[location] = supplier
		return location
	}

	val WIRE_SEGMENT_LOCATION: Identifier = Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "wire_segment")

	val SMALL_WIRE_SEGMENT: ModelLayerLocation = register(ModelLayerLocation(WIRE_SEGMENT_LOCATION, "small")) {
		WireSegmentModel.createBodyLayer(0.5f)
	}

	@SubscribeEvent
	private fun onRegisterLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
		for ((location, supplier) in LAYERS)
			event.registerLayerDefinition(location, supplier)
	}
}