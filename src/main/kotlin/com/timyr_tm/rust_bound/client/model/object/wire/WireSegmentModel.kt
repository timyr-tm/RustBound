package com.timyr_tm.rust_bound.client.model.`object`.wire

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

class WireSegmentModel(root: ModelPart, render: Function<Identifier, RenderType>) : Model<Unit>(root, render) {
	companion object {
		fun createBodyLayer(width: Float = 0.5f): LayerDefinition {
			val mesh = MeshDefinition()
			mesh.root.addOrReplaceChild(
				"segment",
				CubeListBuilder.create().addBox(
					0f, -width, -width,
					16f, width * 2, width * 2,
					setOf(Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN)
				),
				PartPose.rotation(0f, Math.toRadians(90f), 0f)
			)
			return LayerDefinition.create(mesh, (width * 2 + 32).toInt(), (width * 4).toInt())
		}
	}
}