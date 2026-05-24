package com.timyr_tm.rust_bound.world.block

import com.timyr_tm.rust_bound.world.block.entity.BlockEntityTypes
import com.timyr_tm.rust_bound.world.block.entity.InsulatorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class InsulatorBlock(properties: Properties): Block(properties), EntityBlock {
	companion object {
		val SHAPES: Map<Direction, VoxelShape> = mapOf(
			Direction.DOWN 	to box(6.0, 0.0, 6.0, 10.0, 3.5, 10.0),
			Direction.UP 	to box(6.0, 12.5, 6.0, 10.0, 16.0, 10.0),
			Direction.NORTH to box(6.0, 6.0, 0.0, 10.0, 10.0, 3.5),
			Direction.SOUTH to box(6.0, 6.0, 12.5, 10.0, 10.0, 16.0),
			Direction.WEST 	to box(0.0, 6.0, 6.0, 3.5, 10.0, 10.0),
			Direction.EAST 	to box(12.5, 6.0, 6.0, 16.0, 10.0, 10.0)
		)

		val CONNECTIONS_SHAPES: Map<Direction, VoxelShape> = mapOf(
			Direction.DOWN 	to box(7.0, 3.5, 7.0, 9.0, 4.0, 9.0),
			Direction.UP 	to box(7.0, 12.0, 7.0, 9.0, 12.5, 9.0),
			Direction.NORTH to box(7.0, 7.0, 3.5, 9.0, 9.0, 4.0),
			Direction.SOUTH to box(7.0, 7.0, 12.0, 9.0, 9.0, 12.5),
			Direction.WEST 	to box(3.5, 7.0, 7.0, 4.0, 9.0, 9.0),
			Direction.EAST 	to box(12.0, 7.0, 7.0, 12.5, 9.0, 9.0)
		)
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		val facing: Direction = state.getValue(FACING)
		return Shapes.or(SHAPES[facing]!!, CONNECTIONS_SHAPES[facing]!!)
	}

	override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = SHAPES[state.getValue(FACING)]!!

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState = defaultBlockState()
		.setValue(FACING, context.clickedFace.opposite)

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		val facing: Direction = state.getValue(FACING)
		val neighborPos: BlockPos = pos.relative(facing)
		return level.getBlockState(neighborPos).isFaceSturdy(level, neighborPos, facing)
	}

	override fun rotate(state: BlockState, rotation: Rotation): BlockState = state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

	override fun mirror(state: BlockState, mirror: Mirror): BlockState = state.setValue(FACING, mirror.mirror(state.getValue(FACING)))

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = InsulatorBlockEntity(pos, state)
}