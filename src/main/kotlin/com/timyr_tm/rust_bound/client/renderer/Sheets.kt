package com.timyr_tm.rust_bound.client.renderer

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.client.model.geom.ModelLayerDefinitions
import net.minecraft.client.renderer.MaterialMapper
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.resources.model.AtlasManager
import net.minecraft.resources.Identifier
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent

@EventBusSubscriber
object Sheets {
	private val ATLASES: MutableSet<AtlasManager.AtlasConfig> = mutableSetOf()

	private fun register(texture: Identifier, layer: Identifier, prefix: String, createMipmaps: Boolean = false): MaterialMapper {
		ATLASES.add(AtlasManager.AtlasConfig(texture, layer, createMipmaps))
		return MaterialMapper(texture, prefix)
	}

	val WIRE_SEGMENT_SHEET: Identifier = Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "textures/atlas/wire_segments.png")

	val WIRE_SEGMENT_SHEET_TYPE: RenderType = RenderTypes.entitySolid(WIRE_SEGMENT_SHEET)

	val WIRE_SEGMENT_MAPPER: MaterialMapper = register(WIRE_SEGMENT_SHEET, ModelLayerDefinitions.WIRE_SEGMENT.model, "entity/wire_segment")

	@SubscribeEvent
	private fun onRegisterTextureAtlasesEvent(event: RegisterTextureAtlasesEvent) {
		for (config in ATLASES)
			event.register(config)
	}
}