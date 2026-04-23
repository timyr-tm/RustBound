package com.timyr_tm.rust_bound.world.block;

import com.timyr_tm.rust_bound.RustBound;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RustBound.MOD_ID);

    public static final DeferredBlock<ConnectorBlock> TEST_CONNECTOR = BLOCKS.registerBlock("test_connector", ConnectorBlock::new);
}
