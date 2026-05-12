package com.timyr_tm.rust_bound.world.block.entity

import com.timyr_tm.rust_bound.world.block.InsulatorBlock
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.BiConsumer

class InsulatorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): ConnectableBlockEntity(type, pos, state) {
	constructor(pos: BlockPos, state: BlockState): this(BlockEntityTypes.INSULATOR_BLOCK_ENTITY_TYPE.get(), pos, state)

	override fun createConnections(connections: BiConsumer<String, ConnectionPointInfo>) {
		val facing: Direction = blockState.getValue(InsulatorBlock.FACING)
		val shape: VoxelShape = when (facing) {
			Direction.DOWN -> InsulatorBlock.DOWN_CONNECTION_SHAPE
			Direction.UP -> InsulatorBlock.UP_CONNECTION_SHAPE
			Direction.NORTH -> InsulatorBlock.NORTH_CONNECTION_SHAPE
			Direction.SOUTH -> InsulatorBlock.SOUTH_CONNECTION_SHAPE
			Direction.WEST -> InsulatorBlock.WEST_CONNECTION_SHAPE
			Direction.EAST -> InsulatorBlock.EAST_CONNECTION_SHAPE
		}
		connections.accept("main", ConnectionPointInfo(Vec3(0.5, 0.5, 0.5), shape, this.blockPos))
	}
}