package com.timyr_tm.rust_bound.client.debug;

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity;
import com.timyr_tm.rust_bound.world.electricity.ConnectionInfo;
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.util.ARGB;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

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
				for (Map.Entry<String, ConnectionPointInfo> point : blockEntity.getConnectionPoints().entrySet()) {
					point.getValue().shape().forAllBoxes(
						(minX, minY, minZ, maxX, maxY, maxZ) -> Gizmos.cuboid(
							new AABB(
								new Vec3(point.getValue().pos()).add(minX, minY, minZ),
								new Vec3(point.getValue().pos()).add(maxX, maxY, maxZ)
							),
							GizmoStyle.fill(color)
						)
					);
					final Vec3 pointPos = new Vec3(point.getValue().pos()).add(point.getValue().point());
					Gizmos.point(pointPos, color, 8);
					Gizmos.billboardText(
						"%s (%s)".formatted(
							point.getKey(),
							blockEntity.getConnections(point.getKey()) != null
								? blockEntity.getConnections(point.getKey()).size()
								: "0"
						),
						pointPos.add(0, .25, 0),
						TextGizmo.Style.forColor(ARGB.colorFromFloat(1, 1, 1, 1))
					);
					for (ConnectionInfo connection : Optional.ofNullable(blockEntity.getConnections(point.getKey())).orElse(new HashSet<>())) {
						final ConnectionPointInfo lastPoint = connection.getConnectionPoint(minecraft.level);
						if (lastPoint == null)
							continue;
						final Vec3 lastPointPos = pointPos.lerp(new Vec3(lastPoint.pos()).add(lastPoint.point()), .5);
						Gizmos.line(pointPos, lastPointPos, color, 8);
					}
				}
			}
		}
	}
}
