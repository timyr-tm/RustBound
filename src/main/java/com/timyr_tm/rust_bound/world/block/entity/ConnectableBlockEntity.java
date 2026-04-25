package com.timyr_tm.rust_bound.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public abstract class ConnectableBlockEntity extends BlockEntity {
	private final Map<String, Set<ConnectionInfo>> connections = new HashMap<>();
	private final Set<ConnectionPointInfo> connectionPoints = new HashSet<>();

	public ConnectableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		updateConnectionPoints();
	}

	private void updateConnectionPoints() {
		assert level != null;

		connectionPoints.clear();
		createConnectionPoints(connectionPoints::add);

		for (String name: connections.keySet()) {
			if (connectionPoints.stream().allMatch(item -> item.name().equals(name)))
				continue;
			clearConnection(name);
		}
	}

	public void createConnectionPoints(Consumer<ConnectionPointInfo> connections) { }

	public Set<ConnectionPointInfo> getConnectionPoints() {
		return connectionPoints;
	}

	public boolean addConnection(String name, ConnectionInfo connection) {
		if (connectionPoints.stream().noneMatch(point -> point.name().equals(name)))
			return false;
		if (!connections.containsKey(name))
			connections.put(name, new HashSet<>());
		return connections.get(name).add(connection);
	}

	@SuppressWarnings("UnusedReturnValue")
    public boolean removeConnection(String name, ConnectionInfo connection) {
		if (!connections.containsKey(name))
			return false;
		if (connections.get(name).size() == 1)
			return connections.remove(name) != null;
		return connections.get(name).remove(connection);
	}

	public void clearConnection(String name) {
		if (level == null || !connections.containsKey(name))
			return;

		for (ConnectionInfo connection : connections.get(name)) {
			ConnectableBlockEntity blockEntity = connection.getBlockEntity(level);
			if (blockEntity == null)
				continue;
			blockEntity.removeConnection(
				connection.name(),
				new ConnectionInfo(
					name,
					getBlockPos()
				)
			);
		}
		connections.remove(name);
	}

	public @Nullable Set<ConnectionInfo> getConnections(String name) {
		return connections.get(name);
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

    @Override
	@SuppressWarnings("deprecation")
	public void setBlockState(@NonNull BlockState state) {
		super.setBlockState(state);
		this.updateConnectionPoints();
	}
}
