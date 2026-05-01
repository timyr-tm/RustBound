package com.timyr_tm.rust_bound.world.electricity;

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object WireTypes {
	val WIRE_TYPES: DeferredRegister<WireType> = DeferredRegister.create(Registries.WIRE_TYPE, RustBound.MOD_ID);
	val TEST_WIRE_TYPE: DeferredHolder<WireType, WireType> = WIRE_TYPES.register("test", fun() = WireType(15f))
}
