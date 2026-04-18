package com.timyr_tm.rust_bound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ConnectorBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public static final VoxelShape DOWN_SHAPE = Block.box(5, 0, 5, 11, 6, 11);
    public static final VoxelShape UP_SHAPE = Block.box(5, 10, 5, 11, 16, 11);
    public static final VoxelShape NORTH_SHAPE = Block.box(5, 5, 0, 11, 11, 6);
    public static final VoxelShape SOUTH_SHAPE = Block.box(5, 5, 10, 11, 11, 16);
    public static final VoxelShape WEST_SHAPE = Block.box(0, 5, 5, 6, 11, 11);
    public static final VoxelShape EAST_SHAPE = Block.box(10, 5, 5, 16, 11, 11);

    public ConnectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    protected void neighborChanged(
        @NonNull BlockState state, @NonNull Level level,
        @NonNull BlockPos pos, @NonNull Block neighborBlock,
        @Nullable Orientation orientation, boolean movedByPiston
    ) {
        if (!canSurvive(state, level, pos))
            level.destroyBlock(pos, true);
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, @NonNull BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos neighborPos = pos.relative(facing);
        return level.getBlockState(neighborPos).isFaceSturdy(level, neighborPos, facing);
    }

    @Override
    protected @NonNull VoxelShape getShape(
        @NonNull BlockState state, @NonNull BlockGetter level,
        @NonNull BlockPos pos, @NonNull CollisionContext context
    ) {
        return switch (state.getValue(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case UP -> UP_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
        };
    }
}
