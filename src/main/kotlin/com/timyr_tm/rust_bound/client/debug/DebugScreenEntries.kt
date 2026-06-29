package com.timyr_tm.rust_bound.client.debug

import com.timyr_tm.rust_bound.RustBound
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.client.renderer.debug.DebugRenderer
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterDebugEntriesEvent
import net.neoforged.neoforge.client.event.RegisterDebugRenderersEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RustBound.MOD_ID)
object DebugScreenEntries {
	private val DEBUG_ENTRIES: MutableMap<Identifier, Pair<DebugScreenEntry, DebugRenderer.SimpleDebugRenderer?>> = mutableMapOf()

    private fun register(identifier: Identifier, entry: DebugScreenEntry, renderer: DebugRenderer.SimpleDebugRenderer? = null): Identifier {
		DEBUG_ENTRIES[identifier] = Pair(entry, renderer)
		return identifier
    }

	val CONNECTION_ENDPOINTS: Identifier = register(
		Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "connection_endpoints"),
		DebugConnections(), DebugConnectionsRender()
	)

    @SubscribeEvent
	private fun onRegisterDebugEntries(event: RegisterDebugEntriesEvent) = DEBUG_ENTRIES.forEach {
		(key, value) -> event.register(key, value.first)
	}

	@SubscribeEvent
	private fun onRegisterDebugRenderers(event: RegisterDebugRenderersEvent) = DEBUG_ENTRIES.forEach {
		(key, value) -> if (value.second != null && Minecraft.getInstance().debugEntries.isCurrentlyEnabled(key))
			event.register(value.second!!)
	}
}
