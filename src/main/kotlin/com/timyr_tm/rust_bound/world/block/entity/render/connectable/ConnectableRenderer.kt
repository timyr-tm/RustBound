package com.timyr_tm.rust_bound.world.block.entity.render.connectable

import com.mojang.blaze3d.vertex.PoseStack
import com.timyr_tm.rust_bound.client.model.geom.ModelLayers
import com.timyr_tm.rust_bound.client.model.`object`.wire.WireSegmentModel
import com.timyr_tm.rust_bound.client.renderer.Sheets
import com.timyr_tm.rust_bound.core.math.round
import com.timyr_tm.rust_bound.core.math.toVec3
import com.timyr_tm.rust_bound.world.Level.getBlockEntity
import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.resources.model.sprite.SpriteGetter
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3fc
import kotlin.collections.iterator

class ConnectableRenderer(modelSet: EntityModelSet, private val materials: SpriteGetter): BlockEntityRenderer<ConnectableBlockEntity, ConnectableRenderState> {
	private val model: WireSegmentModel =
        WireSegmentModel(modelSet.bakeLayer(ModelLayers.SMALL_WIRE_SEGMENT), RenderTypes::entitySolid)

	override fun createRenderState() = ConnectableRenderState()

	override fun extractRenderState(entity: ConnectableBlockEntity, state: ConnectableRenderState, tick: Float, camera: Vec3, progress: ModelFeatureRenderer.CrumblingOverlay?) {
		super.extractRenderState(entity, state, tick, camera, progress)

		state.renderers = buildSet {
			for ((point, connections) in entity.connections) {
				val first: Vector3fc = point.pos
					.toVector3f()
					.round(3)
				for ((pointer, wire) in connections) {
					val blockEntity: BlockEntity = entity.level!!.getBlockEntity(pointer)
						?: continue
					val second: Vector3fc = blockEntity.blockPos
						.toVec3()
						.add(point.pos)
						.subtract(entity.blockPos.toVec3())
						.toVector3f()
						.round(3)
					this.add(
						ConnectableRenderState.WireRenderInfo(
							wire.key!!.identifier(),
							WireRenderCache[Pair(first, second)]
						)
					)
				}
			}
		}
	}

	override fun submit(state: ConnectableRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
		for (render in state.renderers) {
			for (i in (0 ..< render.points.size - 1)) {
				val matrix = Matrix4f()
					.translate(render.points[i])
					.scale(render.points[i].distance(render.points[i + 1]))
					.rotateTowards(render.points[i].sub(render.points[i + 1], Vector3f()).normalize(),
                        Vector3f(0f, 1f, 0f)
                    )
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
					materials.get(Sheets.WIRE_SEGMENT_MAPPER.apply(render.wire)),
					0,
					state.breakProgress
				)
				poseStack.popPose()
			}
		}
	}
}