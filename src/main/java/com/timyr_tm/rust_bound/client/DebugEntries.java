package com.timyr_tm.rust_bound.client;

import com.timyr_tm.rust_bound.RustBound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDebugEntriesEvent;
import net.neoforged.neoforge.client.event.RegisterDebugRenderersEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT, modid = RustBound.MOD_ID)
public final class DebugEntries {
	private static final Map<Identifier, DebugScreenEntry> ENTRY_MAP = new HashMap<>();

	public static final Identifier CONNECTION_ENDPOINTS = register(
		Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "connection_endpoints"),
		new DebugEntryConnectionsEndpoint()
	);

	private static Identifier register(Identifier identifier, DebugScreenEntry entry) {
		ENTRY_MAP.put(identifier, entry);
		return identifier;
	}

	@SubscribeEvent
	private static void onRegisterDebugEntries(RegisterDebugEntriesEvent event) {
		for(Map.Entry<Identifier, DebugScreenEntry> entry : ENTRY_MAP.entrySet())
			event.register(entry.getKey(), entry.getValue());
	}

	@SubscribeEvent
	private static void onRegisterDebugRenderers(RegisterDebugRenderersEvent event) {
		if (Minecraft.getInstance().debugEntries.isCurrentlyEnabled(CONNECTION_ENDPOINTS))
			event.register(new DebugEntryConnectionsRender(Minecraft.getInstance()));
	}
}
