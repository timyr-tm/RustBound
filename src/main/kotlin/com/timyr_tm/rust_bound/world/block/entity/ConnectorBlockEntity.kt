package com.timyr_tm.rust_bound.world.block.entity;

import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

class ConnectorBlockEntity: ConnectableBlockEntity {
	constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState): super(type, pos, state)

	constructor(pos: BlockPos, state: BlockState): super(BlockEntityTypes.CONNECTOR_BLOCK_ENTITY_TYPE.get(), pos, state)

	override fun createConnectionPoints(connections: BiConsumer<String, ConnectionPointInfo>) {
		if (level == null) return
		connections.accept("main", ConnectionPointInfo(Vec3(0.5, 0.5, 0.5), blockState.getShape(level!!, blockPos), blockPos, true))
	}
}
