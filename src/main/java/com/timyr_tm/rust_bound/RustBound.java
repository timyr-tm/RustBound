package com.timyr_tm.rust_bound;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import static com.timyr_tm.rust_bound.world.item.Items.ITEMS;
import static com.timyr_tm.rust_bound.world.item.CreativeModeTabs.CREATIVE_MODE_TABS;
import static com.timyr_tm.rust_bound.world.block.Blocks.BLOCKS;
import static com.timyr_tm.rust_bound.world.block.entity.BlockEntityTypes.BLOCK_ENTITY_TYPES;
import static com.timyr_tm.rust_bound.core.component.DataComponents.DATA_COMPONENTS;

@Mod(RustBound.MOD_ID)
public class RustBound {
    public static final String MOD_ID = "rust_bound";

    public RustBound(IEventBus bus, ModContainer container) {
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        DATA_COMPONENTS.register(bus);
    }

    public static ModContainer getModContainer() {
        return ModList.get().getModContainerById(MOD_ID).orElseThrow();
    }
}
