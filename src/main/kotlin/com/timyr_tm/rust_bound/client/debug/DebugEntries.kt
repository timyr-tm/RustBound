package com.timyr_tm.rust_bound.client.debug;

import com.timyr_tm.rust_bound.RustBound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDebugEntriesEvent
import net.neoforged.neoforge.client.event.RegisterDebugRenderersEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RustBound.MOD_ID)
object DebugEntries {
	private val ENTRY_MAP: MutableMap<Identifier, DebugScreenEntry> = mutableMapOf();

	val CONNECTION_ENDPOINTS: Identifier = register(
		Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "connection_endpoints"),
		DebugEntryConnectionsEndpoint()
	);

	private fun register(identifier: Identifier, entry: DebugScreenEntry): Identifier {
        ENTRY_MAP[identifier] = entry;
		return identifier;
	}

	@SubscribeEvent
	fun onRegisterDebugEntries(event: RegisterDebugEntriesEvent) {
		for (entry in ENTRY_MAP)
			event.register(entry.key, entry.value)
	}

	@SubscribeEvent
	fun onRegisterDebugRenderers(event: RegisterDebugRenderersEvent) {
		if (Minecraft.getInstance().debugEntries.isCurrentlyEnabled(CONNECTION_ENDPOINTS))
			event.register(DebugEntryConnectionsRender(Minecraft.getInstance()));
	}
}
