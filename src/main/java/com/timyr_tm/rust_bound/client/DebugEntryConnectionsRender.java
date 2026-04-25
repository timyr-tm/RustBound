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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
		if (minecraft.player == null || minecraft.level == null)
			return;
		Iterable<BlockPos> blocks = BlockPos.betweenClosed(
			minecraft.player.blockPosition().offset(-10, -10, -10),
			minecraft.player.blockPosition().offset(10, 10, 10)
		);
		final int color = ARGB.colorFromFloat(.25f, 0, .5f, 0);
		for (BlockPos pos : blocks) {
			if (minecraft.level.getBlockEntity(pos) instanceof ConnectableBlockEntity blockEntity) {
				for (ConnectionPointInfo point : blockEntity.getConnectionPoints()) {
					point.shape().forAllBoxes(
						(minX, minY, minZ, maxX, maxY, maxZ) -> Gizmos.cuboid(
							new AABB(
								new Vec3(point.pos()).add(minX, minY, minZ),
								new Vec3(point.pos()).add(maxX, maxY, maxZ)
							),
							GizmoStyle.fill(color)
						)
					);
				}
			}
		}
	}
}
