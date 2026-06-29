package com.timyr_tm.rust_bound.world.block.entity.render.connectable

import com.mojang.logging.LogUtils
import com.timyr_tm.rust_bound.RustBound
import com.timyr_tm.rust_bound.core.math.round
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist.CLIENT
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.event.level.LevelEvent
import org.joml.Vector3f
import org.joml.Vector3fc
import org.slf4j.Logger
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@EventBusSubscriber(value = [CLIENT], modid = RustBound.MOD_ID)
object WireRenderCache {
    const val UPDATE_TIME: ULong = 5u
    const val DEATH_TIME: ULong = 50u

    private val logger: Logger = LogUtils.getLogger()

    private val cache: MutableMap<Pair<Vector3fc, Vector3fc>, CachedWirePointsList> = HashMap()

    init {
        logger.debug("Init")
    }

    operator fun get(range: Pair<Vector3fc, Vector3fc>): List<Vector3fc> {
        val time: Long = Minecraft.getInstance().level?.gameTime
            ?: return emptyList()

        if (range in cache)
            return cache[range]!!.apply {
                if ((time - this.useTime).toULong() >= UPDATE_TIME)
                    this.useTime = time
            }

        val center: Vector3fc = range.first.lerp(range.second, .5f, Vector3f())
            .round(3)

        val distance: Int = range.first.distance(center)
            .roundToInt()

        if (distance <= 0)
            return emptyList()

        this.cache[range] = CachedWirePointsList(
            time, (0 .. distance).map {
                i -> range.first.lerp(center, i.toFloat() / distance, Vector3f()).apply {
                    this.y -= sin((PI.toFloat() / 2) * (i.toFloat() / distance)) * (.25f * distance)
                }
            }
        )
        logger.debug("Create cache: {}", range)
        return this.cache[range]!!
    }

    @SubscribeEvent
    private fun onClientTick(event: ClientTickEvent.Post) {
        this.cache.entries.removeIf(::isRemoveCache)
    }

    private fun isRemoveCache(entry: MutableMap.MutableEntry<Pair<Vector3fc, Vector3fc>, CachedWirePointsList>): Boolean {
        val time: Long? = Minecraft.getInstance().level?.gameTime
        if (time != null && (time - entry.value.useTime).toULong() >= DEATH_TIME) {
            logger.debug("Remove cache: {}", entry.key)
            return true
        }
        return false
    }

    @SubscribeEvent
    private fun onUnloadLevel(event: LevelEvent.Unload) {
        this.cache.clear()
        logger.debug("Clean cache")
    }

    private class CachedWirePointsList(var useTime: Long, vectors: List<Vector3fc>): List<Vector3fc> by vectors
}

