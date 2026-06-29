package com.timyr_tm.rust_bound.client.renderer

import net.minecraft.client.renderer.SpriteMapper
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent
import com.timyr_tm.rust_bound.RustBound.Companion.MOD_ID
import net.minecraft.client.resources.model.sprite.AtlasManager

@EventBusSubscriber(Dist.CLIENT, modid = MOD_ID)
object Sheets {
	private val ATLASES: MutableSet<AtlasManager.AtlasConfig> = mutableSetOf()

	private fun register(location: String, prefix: String, createMipmaps: Boolean = false): SpriteMapper {
		val texture: Identifier = Identifier.fromNamespaceAndPath(MOD_ID, "textures/atlas/${location}.png")
		val layer: Identifier = Identifier.fromNamespaceAndPath(MOD_ID, location)
		ATLASES.add(AtlasManager.AtlasConfig(texture, layer, createMipmaps))
		return SpriteMapper(texture, prefix)
	}

	val WIRE_SEGMENT_MAPPER: SpriteMapper = register("wire_segments",  "entity/wire_segment")

	val WIRE_SEGMENT_SHEET_TYPE: RenderType = RenderTypes.entitySolid(WIRE_SEGMENT_MAPPER.sheet)

	@SubscribeEvent
	private fun onRegisterTextureAtlasesEvent(event: RegisterTextureAtlasesEvent) = ATLASES.forEach(event::register)
}