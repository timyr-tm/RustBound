package com.timyr_tm.rust_bound.core.registries

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.electricity.WireType
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

object Registries {
	@JvmField
    val WIRE_TYPE: ResourceKey<Registry<WireType>> = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "wire_type"))
}
