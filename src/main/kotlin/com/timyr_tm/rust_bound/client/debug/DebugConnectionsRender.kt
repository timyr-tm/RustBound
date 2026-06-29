package com.timyr_tm.rust_bound.client.debug

import com.timyr_tm.rust_bound.world.Level.getPoint
import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.debug.DebugRenderer
import net.minecraft.core.BlockPos
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.gizmos.TextGizmo
import net.minecraft.util.ARGB
import net.minecraft.util.CommonColors
import net.minecraft.util.debug.DebugValueAccess
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3
import kotlin.collections.component1
import kotlin.collections.component2

class DebugConnectionsRender: DebugRenderer.SimpleDebugRenderer {
	companion object {
		private val color: Int = ARGB.colorFromFloat(.5f, 0f, .5f, 0f)
	}

	override fun emitGizmos(
		camX: Double, camY: Double, camZ: Double,
		access: DebugValueAccess,
		frustum: Frustum, ticks: Float
	) {
		val minecraft = Minecraft.getInstance()
		if (minecraft.player == null || minecraft.level == null)
			return
		val blocks: Iterable<BlockPos> = BlockPos.betweenClosed(
			minecraft.player!!.blockPosition().offset(-10, -10, -10),
			minecraft.player!!.blockPosition().offset(10, 10, 10)
		)
		for (pos in blocks) {
			val blockEntity: BlockEntity? = minecraft.level!!.getBlockEntity(pos)
			if (blockEntity is ConnectableBlockEntity) {
				for ((point, connections) in blockEntity.connections) {
					Gizmos.cuboid(point.region.move(blockEntity.blockPos), GizmoStyle.fill(color))

					val pos = Vec3(blockEntity.blockPos).add(point.pos)

					Gizmos.point(pos, color, 8f)

					Gizmos.billboardText(
						"$point (${connections.size})",
						pos.add(.0, .25, .0),
						TextGizmo.Style.forColorAndCentered(CommonColors.WHITE)
					)

					for ((pointer, _) in connections) {
						val point = minecraft.level!!.getPoint(pointer) ?: continue
						Gizmos.line(pos, pos.lerp(Vec3(pointer.pos).add(point.pos), 0.5), color, 8f)
					}
				}
			}
		}
	}
}
