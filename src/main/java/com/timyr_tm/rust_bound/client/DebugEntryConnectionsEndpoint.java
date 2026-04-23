package com.timyr_tm.rust_bound.client;

import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DebugEntryConnectionsEndpoint implements DebugScreenEntry {
	@Override
	public void display(
		@NonNull DebugScreenDisplayer displayer, @Nullable Level level,
		@Nullable LevelChunk levelChunk, @Nullable LevelChunk levelChunk1
	) {}

	@Override
	public @NonNull DebugEntryCategory category() {
		return DebugEntryCategory.RENDERER;
	}
}
