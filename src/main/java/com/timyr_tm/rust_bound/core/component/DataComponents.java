package com.timyr_tm.rust_bound.core.component;

import com.timyr_tm.rust_bound.RustBound;
import com.timyr_tm.rust_bound.world.block.entity.ConnectionInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class DataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RustBound.MOD_ID);

    public static final Supplier<DataComponentType<ConnectionInfo>> WIRE_COIL_CONNECTION = DATA_COMPONENTS.registerComponentType(
        "wire_coil/connection",
        builder -> builder
            .persistent(ConnectionInfo.CODEC)
            .networkSynchronized(ConnectionInfo.STREAM_CODEC)
    );
}
