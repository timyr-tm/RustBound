package com.timyr_tm.rust_bound.client.debug

import net.minecraft.client.gui.components.debug.DebugEntryCategory
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk

class DebugConnections: DebugScreenEntry {
	override fun display(
		p0: DebugScreenDisplayer,
		p1: Level?,
		p2: LevelChunk?,
		p3: LevelChunk?
	) {

	}

	override fun category(): DebugEntryCategory = DebugEntryCategory.RENDERER
}
