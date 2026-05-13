package com.timyr_tm.rust_bound.world.block.entity;

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.block.Blocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object BlockEntityTypes {
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RustBound.MOD_ID)

    val INSULATOR_BLOCK_ENTITY_TYPE: DeferredHolder<BlockEntityType<*>, BlockEntityType<InsulatorBlockEntity>> = BLOCK_ENTITY_TYPES.register("insulator") {
        -> BlockEntityType(::InsulatorBlockEntity, Blocks.COPPER_INSULATOR.get())
    }
}
