package com.timyr_tm.rust_bound.client;

import com.timyr_tm.rust_bound.world.block.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.util.debug.DebugValueAccess;
import org.jspecify.annotations.NonNull;

public class DebugEntryConnectionsRender implements DebugRenderer.SimpleDebugRenderer {
	private final Minecraft minecraft;

	public DebugEntryConnectionsRender(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	@Override
	public void emitGizmos(
		double camX, double camY, double camZ,
		@NonNull DebugValueAccess access,
		@NonNull Frustum frustum, float ticks
	) {
		if (minecraft.player == null)
			return;
		BlockPos blockPos = minecraft.player.blockPosition();
		for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-10, -10, -10), blockPos.offset(10, 10, 10)))
			if (minecraft.player.level().getBlockEntity(pos) instanceof ConnectableBlockEntity)
				Gizmos.cuboid(pos, GizmoStyle.fill(ARGB.colorFromFloat(.5f, 0, .5f, 0)));
	}
}
