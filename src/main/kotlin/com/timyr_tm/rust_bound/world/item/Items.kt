package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.block.Blocks
import com.timyr_tm.rust_bound.world.electricity.WireTypes
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object Items {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(RustBound.MOD_ID);
    val TEST_SPOOL: DeferredItem<SpoolItem> = ITEMS.registerItem("test_spool") {
        properties -> SpoolItem(WireTypes.TEST_WIRE_TYPE.getKey(), properties)
    }
    val WIRE_SPOOL: DeferredItem<SpoolItem> = ITEMS.registerItem("copper_spool") {
        properties -> SpoolItem(WireTypes.COPPER_WIRE_TYPE.getKey(), properties)
    }

    val COPPER_INSULATOR: DeferredItem<BlockItem> = ITEMS.registerSimpleBlockItem(Blocks.COPPER_INSULATOR)

    val WIRE_CUTTER: DeferredItem<WireCutterItem> = ITEMS.registerItem("wire_cutter", ::WireCutterItem)
}
