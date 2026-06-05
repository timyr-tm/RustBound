package com.timyr_tm.rust_bound.client.renderer

import net.minecraft.client.renderer.MaterialMapper
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.resources.model.AtlasManager
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent
import com.timyr_tm.rust_bound.RustBound.Companion.MOD_ID

@EventBusSubscriber(Dist.CLIENT, modid = MOD_ID)
object Sheets {
	private val ATLASES: MutableSet<AtlasManager.AtlasConfig> = mutableSetOf()

	private fun register(location: String, prefix: String, createMipmaps: Boolean = false): MaterialMapper {
		val texture: Identifier = Identifier.fromNamespaceAndPath(MOD_ID, "textures/atlas/${location}.png")
		val layer: Identifier = Identifier.fromNamespaceAndPath(MOD_ID, location)
		ATLASES.add(AtlasManager.AtlasConfig(texture, layer, createMipmaps))
		return MaterialMapper(texture, prefix)
	}

	val WIRE_SEGMENT_MAPPER: MaterialMapper = register("wire_segments",  "entity/wire_segment")

	val WIRE_SEGMENT_SHEET_TYPE: RenderType = RenderTypes.entitySolid(WIRE_SEGMENT_MAPPER.sheet)

	@SubscribeEvent
	private fun onRegisterTextureAtlasesEvent(event: RegisterTextureAtlasesEvent) = ATLASES.forEach(event::register)
}