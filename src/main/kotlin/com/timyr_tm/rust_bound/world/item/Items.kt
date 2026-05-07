package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.block.Blocks
import com.timyr_tm.rust_bound.world.electricity.WireTypes
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object Items {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(RustBound.MOD_ID);

    val TEST_CONNECTOR: DeferredItem<BlockItem> = ITEMS.registerSimpleBlockItem(Blocks.TEST_CONNECTOR);
    val TEST_COIL: DeferredItem<SpoolItem> = ITEMS.registerItem("test_spool") {
        properties -> SpoolItem(WireTypes.TEST_WIRE_TYPE.getKey(), properties)
    }
}
