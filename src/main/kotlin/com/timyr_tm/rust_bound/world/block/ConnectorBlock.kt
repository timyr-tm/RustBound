package com.timyr_tm.rust_bound.world.block;

import com.timyr_tm.rust_bound.world.block.entity.ConnectorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class ConnectorBlock(properties: Properties): Block(properties), EntityBlock {
    companion object {
        val FACING: EnumProperty<Direction> = BlockStateProperties.FACING

        val DOWN_SHAPE: VoxelShape = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        val UP_SHAPE: VoxelShape = Block.box(5.0, 10.0, 5.0, 11.0, 16.0, 11.0)
        val NORTH_SHAPE: VoxelShape = Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 6.0)
        val SOUTH_SHAPE: VoxelShape = Block.box(5.0, 5.0, 10.0, 11.0, 11.0, 16.0)
        val WEST_SHAPE: VoxelShape = Block.box(0.0, 5.0, 5.0, 6.0, 11.0, 11.0)
        val EAST_SHAPE: VoxelShape = Block.box(10.0, 5.0, 5.0, 16.0, 11.0, 11.0)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
       builder.add(FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState = defaultBlockState()
        .setValue(FACING, context.clickedFace.opposite)

    override fun neighborChanged(
        state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block,
        orientation: Orientation?, movedByPiston: Boolean
    ) {
        if (!canSurvive(state, level, pos))
            level.destroyBlock(pos, true)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val facing: Direction = state.getValue(FACING)
        val neighborPos: BlockPos = pos.relative(facing)
        return level.getBlockState(neighborPos).isFaceSturdy(level, neighborPos, facing)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = when (state.getValue(FACING)) {
        Direction.DOWN -> DOWN_SHAPE
        Direction.UP -> UP_SHAPE
        Direction.NORTH -> NORTH_SHAPE
        Direction.SOUTH -> SOUTH_SHAPE
        Direction.WEST -> WEST_SHAPE
        Direction.EAST -> EAST_SHAPE
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ConnectorBlockEntity(pos, state)
}