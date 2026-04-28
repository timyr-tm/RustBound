package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.RustBound;
import com.timyr_tm.rust_bound.world.block.Blocks;
import com.timyr_tm.rust_bound.world.electricity.WireTypes;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RustBound.MOD_ID);

    public static final DeferredItem<BlockItem> TEST_CONNECTOR = ITEMS.registerSimpleBlockItem(Blocks.TEST_CONNECTOR);
    public static final DeferredItem<WireCoilItem> TEST_COIL = ITEMS.registerItem("test_coil", properties -> new WireCoilItem(WireTypes.TEST_WIRE_TYPE.getKey(), properties));
}
