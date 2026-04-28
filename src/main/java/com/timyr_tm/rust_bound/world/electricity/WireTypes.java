package com.timyr_tm.rust_bound.world.electricity;

import com.timyr_tm.rust_bound.RustBound;
import com.timyr_tm.rust_bound.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireTypes {
	public static final DeferredRegister<WireType> WIRE_TYPES = DeferredRegister.create(Registries.WIRE_TYPE, RustBound.MOD_ID);
	public static final DeferredHolder<WireType, WireType> TEST_WIRE_TYPE = WIRE_TYPES.register("test", WireType::new);
}
