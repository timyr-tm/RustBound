package com.timyr_tm.rust_bound.world.block.entity.render

import com.mojang.blaze3d.vertex.PoseStack
import com.timyr_tm.rust_bound.client.model.geom.ModelLayerDefinitions
import com.timyr_tm.rust_bound.client.model.`object`.WireSegmentModel
import com.timyr_tm.rust_bound.client.renderer.Sheets
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.MaterialSet
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.roundToInt
import kotlin.math.sin

class ConnectableBlockEntityRenderer(modelSet: EntityModelSet, private val materials: MaterialSet): BlockEntityRenderer<ConnectableBlockEntity, ConnectableBlockEntityRenderState> {
	private val model: WireSegmentModel = WireSegmentModel(modelSet.bakeLayer(ModelLayerDefinitions.WIRE_SEGMENT), RenderTypes::entitySolid)

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
					.filter { (pointer, _) -> pointer.getConnectionPoint(blockEntity.level!!) != null }
					.map {
						(pointer, wireType) -> run {
							val a = pointer.getConnectionPoint(blockEntity.level!!)!!
							return@run WireRenderInfo(
								value.point.toVector3f(),
								Vec3(a.pos)
									.subtract(Vec3(value.pos))
									.add(a.point)
									.lerp(value.point, 0.5)
									.toVector3f(),
								wireType.identifier()
							)
						}
					}
			}
			.toSet()
	}

	override fun submit(
		state: ConnectableBlockEntityRenderState, poseStack: PoseStack,
		collector: SubmitNodeCollector, cameraState: CameraRenderState
	) {
		for (point in state.points) {
			val length: Float = point.start.distance(point.end)
			val atlas: TextureAtlasSprite = materials.get(
				Sheets.WIRE_SEGMENT_MAPPER.apply(point.texture)
			)

			for (i in 0..<length.roundToInt()) {
				val firstPos: Vector3f = mathPos(point.start, point.end, i.toFloat() / length.roundToInt(), length)
				val lastPos: Vector3f = mathPos(point.start, point.end, (i + 1).toFloat() / length.roundToInt(), length)

                val matrix = Matrix4f()
					.translate(firstPos)
					.scale(firstPos.distance(lastPos))
					.rotateTowards(firstPos.sub(lastPos).normalize(), Vector3f(0f, 1f, 0f))

				poseStack.pushPose()
				poseStack.mulPose(matrix)
				collector.submitModel(
					model,
					Unit,
					poseStack,
					Sheets.WIRE_SEGMENT_SHEET_TYPE,
					state.lightCoords,
					OverlayTexture.NO_OVERLAY,
					-1,
					atlas,
					0,
					state.breakProgress
				)
				poseStack.popPose()
			}

		}
	}

	fun mathPos(start: Vector3f, end: Vector3f, segment: Float, length: Float): Vector3f = Vector3f(
		start.x + segment * (end.x - start.x),
		start.y + segment * (end.y - start.y) - (sin((Math.PI / 2).toFloat() * segment) * (.25f * length)),
		start.z + segment * (end.z - start.z)
	)
}