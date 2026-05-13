package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.RustBound
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items.LIGHT
import net.neoforged.fml.ModList
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object CreativeModeTabs {
    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RustBound.MOD_ID);

    val RUST_BOUND: DeferredHolder<CreativeModeTab, CreativeModeTab> = CREATIVE_MODE_TABS.register(
        "main",
        fun () = CreativeModeTab.builder()
            .title(Component.literal(getModNamespace()))
            .icon(fun () = ItemStack(LIGHT))
            .displayItems(::getMainTabItems)
            .build()
    )

    private fun getModNamespace(): String = ModList.get().getModContainerById(RustBound.MOD_ID).orElseThrow().getNamespace()

    private fun getMainTabItems(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        output.accept(Items.TEST_COIL)
        output.accept(Items.COPPER_INSULATOR)
    }
}
