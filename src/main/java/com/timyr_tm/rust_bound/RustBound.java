package com.timyr_tm.rust_bound;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import static com.timyr_tm.rust_bound.item.Items.ITEMS;
import static com.timyr_tm.rust_bound.item.CreativeModeTabs.CREATIVE_MODE_TABS;
import static com.timyr_tm.rust_bound.block.Blocks.BLOCKS;

@Mod(RustBound.MOD_ID)
public class RustBound {
    public static final String MOD_ID = "rust_bound";

    public RustBound(IEventBus bus, ModContainer container) {
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        BLOCKS.register(bus);
    }

    public static ModContainer getModContainer() {
        return ModList.get().getModContainerById(MOD_ID).orElseThrow();
    }
}
