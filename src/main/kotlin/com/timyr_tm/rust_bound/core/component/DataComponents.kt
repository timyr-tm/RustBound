package com.timyr_tm.rust_bound.core.component;

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.item.SpoolItemPointer
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object DataComponents {
    val DATA_COMPONENTS: DeferredRegister.DataComponents = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RustBound.MOD_ID);

    val SPOOL_POINTER: Supplier<DataComponentType<SpoolItemPointer>> = DATA_COMPONENTS.registerComponentType("spool/pointer") {
        builder -> builder
            .persistent(SpoolItemPointer.CODEC)
            .networkSynchronized(SpoolItemPointer.STREAM_CODEC)
    }
}