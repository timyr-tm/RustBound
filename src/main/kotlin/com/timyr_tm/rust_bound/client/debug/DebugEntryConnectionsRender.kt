package com.timyr_tm.rust_bound.client.debug;

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import com.timyr_tm.rust_bound.world.electricity.WireType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.debug.DebugRenderer
import net.minecraft.core.BlockPos
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.gizmos.TextGizmo
import net.minecraft.resources.ResourceKey
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
		val color: Int = ARGB.colorFromFloat(.5f, 0f, .5f, 0f)
		for (pos in blocks) {
			val blockEntity: BlockEntity? = minecraft.level!!.getBlockEntity(pos)
			if (blockEntity is ConnectableBlockEntity) {
				for((key, value) in blockEntity.connections) {
					value.area.forAllBoxes (
						fun(minX, minY, minZ, maxX, maxY, maxZ) {
							Gizmos.cuboid(
								AABB(
									Vec3(value.pos).add(minX, minY, minZ),
									Vec3(value.pos).add(maxX, maxY, maxZ)
								),
								GizmoStyle.fill(color)
							)
						}
					)

					val pointPos: Vec3 = Vec3(value.pos).add(value.point)
					Gizmos.point(pointPos, color, 8f)

					val connections: Set<Map.Entry<ConnectionPointerInfo, ResourceKey<WireType>>> = blockEntity.connections[key]!!.toSet()

					Gizmos.billboardText(
						"$key (${connections.size})",
						pointPos.add(.0, .25, .0),
						TextGizmo.Style.forColorAndCentered(ARGB.colorFromFloat(1f, 1f, 1f, 1f))
					)

					for((pointer, wireType) in connections) {
                        val lastPoint: ConnectionPointInfo = pointer.getConnectionPoint(minecraft.level!!) ?: continue
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
