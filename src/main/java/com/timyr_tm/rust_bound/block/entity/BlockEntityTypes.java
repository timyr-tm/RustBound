package com.timyr_tm.rust_bound.block.entity;

import com.timyr_tm.rust_bound.RustBound;
import com.timyr_tm.rust_bound.block.Blocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RustBound.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConnectorBlockEntity>> CONNECTOR_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register(
        "connector",
        () -> new BlockEntityType<>(
            ConnectorBlockEntity::new,
            Blocks.TEST_CONNECTOR.get()
        )
    );
}
