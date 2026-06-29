package com.timyr_tm.rust_bound.world.block

import com.mojang.logging.LogUtils
import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.block.entity.InsulatorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.slf4j.Logger

class InsulatorBlock(properties: Properties): Block(properties), EntityBlock {
	companion object {
		val logger: Logger = LogUtils.getLogger()

		val SHAPES: Map<Direction, VoxelShape> = mapOf(
			Direction.DOWN 	to box(6.0, 0.0, 6.0, 10.0, 3.5, 10.0),
			Direction.UP 	to box(6.0, 12.5, 6.0, 10.0, 16.0, 10.0),
			Direction.NORTH to box(6.0, 6.0, 0.0, 10.0, 10.0, 3.5),
			Direction.SOUTH to box(6.0, 6.0, 12.5, 10.0, 10.0, 16.0),
			Direction.WEST 	to box(0.0, 6.0, 6.0, 3.5, 10.0, 10.0),
			Direction.EAST 	to box(12.5, 6.0, 6.0, 16.0, 10.0, 10.0)
		)

		val CONNECTIONS_SHAPES: Map<Direction, AABB> = mapOf(
			Direction.DOWN 	to AABB(0.4375, 0.21875, 0.4375, 0.5625, 0.25, 0.5625),
			Direction.UP 	to AABB(0.4375, 0.75, 0.4375, 0.5625, 0.78125, 0.5625),
			Direction.NORTH to AABB(0.4375, 0.4375, 0.21875, 0.5625, 0.5625, 0.25),
			Direction.SOUTH to AABB(0.4375, 0.4375, 0.75, 0.5625, 0.5625, 0.78125),
			Direction.WEST 	to AABB(0.21875, 0.4375, 0.4375, 0.25, 0.5625, 0.5625),
			Direction.EAST 	to AABB(0.75, 0.4375, 0.4375, 0.78125, 0.5625, 0.5625)
		)
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = Shapes.or(
		SHAPES[state.getValue(FACING)]!!,
		Shapes.create(CONNECTIONS_SHAPES[state.getValue(FACING)]!!)
	)

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

	override fun rotate(state: BlockState, rotation: Rotation): BlockState = state
		.setValue(FACING, rotation.rotate(state.getValue(FACING)))

	override fun mirror(state: BlockState, mirror: Mirror): BlockState = state
		.setValue(FACING, mirror.mirror(state.getValue(FACING)))

	override fun newBlockEntity(pos: BlockPos, state: BlockState): ConnectableBlockEntity = InsulatorBlockEntity(pos, state)

	override fun useItemOn(
		itemStack: ItemStack,
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is ConnectableBlockEntity) {
			logger.debug("connections:")
			blockEntity.connections.forEach {
				(point, connections) -> logger.debug("{}", point)
					connections.forEach {
						entry -> logger.debug("\t{}", entry)
					}
			}
		}

		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
	}
}