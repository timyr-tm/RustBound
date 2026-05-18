package com.timyr_tm.rust_bound.client.model.`object`

import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import org.joml.Math
import java.util.function.Function

class WireSegmentModel(root: ModelPart, renderType: Function<Identifier, RenderType>) : Model<Unit>(root, renderType) {
	companion object {
		fun createBodyLayer(): LayerDefinition {
			val mesh = MeshDefinition()
			mesh.root.addOrReplaceChild(
				"segment",
				CubeListBuilder.create()
					.texOffs(0, 0)
					.addBox(
						0f, -.5f, -.5f,
						16f, 1f, 1f,
						setOf(Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN)
					),
				PartPose.rotation(0f, Math.toRadians(90f), 0f)
			)
			return LayerDefinition.create(mesh, 34, 2)
		}
	}
}