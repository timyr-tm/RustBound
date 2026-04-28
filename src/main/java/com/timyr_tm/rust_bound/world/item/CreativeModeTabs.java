package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.RustBound;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.Items.LIGHT;

public final class CreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RustBound.MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RUST_BOUND = CREATIVE_MODE_TABS.register(
        "main",
        () -> CreativeModeTab.builder()
            .title(Component.literal(getModNamespace()))
            .icon(() -> new ItemStack(LIGHT))
            .displayItems(CreativeModeTabs::getMainTabItems)
            .build()
    );

    private static String getModNamespace() {
        return ModList.get().getModContainerById(RustBound.MOD_ID).orElseThrow().getNamespace();
    }

    private static void getMainTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(Items.TEST_CONNECTOR);
        output.accept(Items.TEST_COIL);
    }
}
