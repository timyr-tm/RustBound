package com.timyr_tm.rust_bound.world.block.entity.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.logging.LogUtils
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import org.slf4j.Logger
import kotlin.math.roundToInt
import kotlin.math.sin

class ConnectableBlockEntityRenderer: BlockEntityRenderer<ConnectableBlockEntity, ConnectableBlockEntityRenderState> {
	private val logger: Logger = LogUtils.getLogger()

	override fun createRenderState() = ConnectableBlockEntityRenderState()

	override fun extractRenderState(
		blockEntity: ConnectableBlockEntity,
		renderState: ConnectableBlockEntityRenderState,
		partialTick: Float,
		cameraPosition: Vec3,
		breakProgress: ModelFeatureRenderer.CrumblingOverlay?
	) {
		super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress)
		renderState.points = blockEntity.connections.values
			.flatMap {
				value -> value
					.filter { con -> con.getConnectionPoint(blockEntity.level!!) != null }
					.map(
						fun (con): WireRenderInfo {
							val a = con.getConnectionPoint(blockEntity.level!!)!!
							return WireRenderInfo(
								value.point.toVector3f(),
								Vec3(a.pos)
									.subtract(Vec3(value.pos))
									.add(a.point)
									.lerp(value.point, 0.5)
									.toVector3f(),
								con.wireType.identifier()
							)
						}
					)
			}
			.toSet()
	}

	override fun submit(
		state: ConnectableBlockEntityRenderState, poseStack: PoseStack,
		collector: SubmitNodeCollector, cameraState: CameraRenderState
	) {
		for (point in state.points) collector.submitCustomGeometry(
			poseStack,
			RenderTypes.lines(),
			fun(pose: PoseStack.Pose, consumer: VertexConsumer) = renderWire(point.start, point.end, pose, consumer)
		)
	}

	fun renderWire(start: Vector3f, end: Vector3f, pose: PoseStack.Pose, consumer: VertexConsumer) {
		val length: Float = start.distance(end)

		for (i in 0..<length.roundToInt()) {
			consumer.addVertex(pose, mathPos(start, end, i.toFloat() / length.roundToInt(), length))
				.setColor(0, 0, 0, 255)
				.setLineWidth(8f)
				.setNormal(0f, 1f, 0f)
			consumer.addVertex(pose, mathPos(start, end, (i + 1).toFloat() / length.roundToInt(), length))
				.setColor(0, 0, 0, 255)
				.setLineWidth(8f)
				.setNormal(0f, 1f, 0f)
		}
	}

	fun mathPos(start: Vector3f, end: Vector3f, segment: Float, length: Float): Vector3f = Vector3f(
		start.x + segment * (end.x - start.x),
		(start.y + segment * (end.y - start.y)) - (sin((Math.PI / 2).toFloat() * segment) * (.25f * length)),
		start.z + segment * (end.z - start.z)
	)
}