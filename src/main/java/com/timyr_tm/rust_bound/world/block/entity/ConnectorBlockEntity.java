package com.timyr_tm.rust_bound.world.block.entity;

import com.timyr_tm.rust_bound.world.electricity.ConnectionPointInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public class ConnectorBlockEntity extends ConnectableBlockEntity {
	public ConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ConnectorBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityTypes.CONNECTOR_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	@Override
	public void createConnectionPoints(BiConsumer<String, ConnectionPointInfo> connections) {
		if (this.level == null)
			return;
		connections.accept(
			"main",
			new ConnectionPointInfo(
				new Vec3(.5, .5, .5),
				this.getBlockState().getShape(this.level, this.getBlockPos()),
				this.getBlockPos(),
				true
			)
		);
	}
}
