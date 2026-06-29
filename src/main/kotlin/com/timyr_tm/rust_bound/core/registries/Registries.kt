package com.timyr_tm.rust_bound.core.registries

import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.world.WireType
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber
object Registries {
    private val WIRE_TYPES: MutableSet<Registry<*>> = mutableSetOf()

    private fun <T: Any> register(key: Identifier, builder: (RegistryBuilder<T>) -> RegistryBuilder<T>): Registry<T> {
        val registry = builder(RegistryBuilder(ResourceKey.createRegistryKey(key)))
            .create()
        WIRE_TYPES.add(registry)
        return registry
    }

    val WIRE_TYPE: Registry<WireType> = register(Identifier.fromNamespaceAndPath(RustBound.MOD_ID, "wire_type")) {
        builder -> builder
            .sync(true)
    }

    @SubscribeEvent
    private fun onNewRegistryEvent(event: NewRegistryEvent) = WIRE_TYPES.forEach {
        registry -> event.register(registry)
    }
}
