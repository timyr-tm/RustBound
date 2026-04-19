package com.timyr_tm.rust_bound.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ConnectorBlockEntity extends BlockEntity {
    public ConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ConnectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.CONNECTOR_BLOCK_ENTITY_TYPE.get(), pos, state);
    }
}
