package com.timyr_tm.rust_bound;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.timyr_tm.rust_bound.world.item.Items.ITEMS;
import com.timyr_tm.rust_bound.world.item.CreativeModeTabs.CREATIVE_MODE_TABS;
import com.timyr_tm.rust_bound.world.block.Blocks.BLOCKS;
import com.timyr_tm.rust_bound.world.block.entity.BlockEntityTypes.BLOCK_ENTITY_TYPES;
import com.timyr_tm.rust_bound.core.component.DataComponents.DATA_COMPONENTS;
import com.timyr_tm.rust_bound.world.electricity.WireTypes.WIRE_TYPES;

@Mod(RustBound.MOD_ID)
class RustBound {
    companion object {
        const val MOD_ID: String = "rust_bound"
    }

    constructor(bus: IEventBus, container: ModContainer) {
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        DATA_COMPONENTS.register(bus);
        WIRE_TYPES.register(bus);
    }
}
