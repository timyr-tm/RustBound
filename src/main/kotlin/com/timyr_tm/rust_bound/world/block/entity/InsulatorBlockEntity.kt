package com.timyr_tm.rust_bound.world.block.entity

import com.timyr_tm.rust_bound.world.block.InsulatorBlock
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import java.util.function.BiConsumer

import net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING

class InsulatorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): ConnectableBlockEntity(type, pos, state) {
	constructor(pos: BlockPos, state: BlockState): this(BlockEntityTypes.INSULATOR_BLOCK_ENTITY_TYPE.get(), pos, state)

	override fun registerConnections(connections: BiConsumer<String, ConnectionPointInfo>) {
		val facing: Direction = blockState.getValue(FACING)
		connections.accept(
			"main",
			ConnectionPointInfo(
				Vec3(0.5, 0.5, 0.5).relative(facing, 0.25),
				InsulatorBlock.CONNECTIONS_SHAPES[facing]!!
			)
		)
	}
}