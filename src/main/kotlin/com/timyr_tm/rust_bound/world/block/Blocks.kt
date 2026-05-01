package com.timyr_tm.rust_bound.world.block;

import com.timyr_tm.rust_bound.RustBound
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object Blocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(RustBound.MOD_ID)

    val TEST_CONNECTOR: DeferredBlock<ConnectorBlock> = BLOCKS.registerBlock("test_connector", ::ConnectorBlock)
}
