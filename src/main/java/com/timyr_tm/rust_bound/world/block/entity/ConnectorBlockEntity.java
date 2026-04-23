package com.timyr_tm.rust_bound.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ConnectorBlockEntity extends ConnectableBlockEntity {
	public ConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ConnectorBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityTypes.CONNECTOR_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
}
