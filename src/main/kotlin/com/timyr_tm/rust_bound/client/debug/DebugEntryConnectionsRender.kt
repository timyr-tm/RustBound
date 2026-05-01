package com.timyr_tm.rust_bound.client.debug;

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.debug.DebugRenderer
import net.minecraft.core.BlockPos
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.gizmos.TextGizmo
import net.minecraft.util.ARGB
import net.minecraft.util.debug.DebugValueAccess
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class DebugEntryConnectionsRender(private val minecraft: Minecraft): DebugRenderer.SimpleDebugRenderer {

	override fun emitGizmos(
		camX: Double, camY: Double, camZ: Double,
		access: DebugValueAccess,
		frustum: Frustum, ticks: Float
	) {
		if (minecraft.player == null || minecraft.level == null)
			return
		val blocks: Iterable<BlockPos> = BlockPos.betweenClosed(
			minecraft.player!!.blockPosition().offset(-10, -10, -10),
			minecraft.player!!.blockPosition().offset(10, 10, 10)
		)
		val color: Int = ARGB.colorFromFloat(.25f, 0f, .5f, 0f)
		for (pos in blocks) {
			val blockEntity: BlockEntity? = minecraft.level!!.getBlockEntity(pos)
			if (blockEntity is ConnectableBlockEntity) {
				for(point in blockEntity.getConnectionPoints()) {
					point.value.shape.forAllBoxes (
						fun(minX, minY, minZ, maxX, maxY, maxZ) {
							Gizmos.cuboid(
								AABB(
									Vec3(point.value.pos).add(minX, minY, minZ),
									Vec3(point.value.pos).add(maxX, maxY, maxZ)
								),
								GizmoStyle.fill(color)
							)
						}
					)

					val pointPos: Vec3 = Vec3(point.value.pos).add(point.value.point)
					Gizmos.point(pointPos, color, 8f)

					val connections: Set<ConnectionInfo> = blockEntity.getConnections(point.key) ?: setOf()
					Gizmos.billboardText(
						"${point.key} (${connections.size})",
						pointPos.add(.0, .25, .0),
						TextGizmo.Style.forColor(ARGB.colorFromFloat(1f, 1f, 1f, 1f))
					)
					for(connection in connections) {
                        val lastPoint: ConnectionPointInfo = connection.getConnectionPoint(minecraft.level!!) ?: continue
                        Gizmos.line(
							pointPos,
							pointPos.lerp(Vec3(lastPoint.pos).add(lastPoint.point), 0.5),
							color,
							8f
						)
					}
				}
			}
		}
	}
}
