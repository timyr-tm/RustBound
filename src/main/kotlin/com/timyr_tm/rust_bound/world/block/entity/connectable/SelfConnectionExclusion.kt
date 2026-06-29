package com.timyr_tm.rust_bound.world.block.entity.connectable

@Suppress("unused")
class SelfConnectionExclusion: RuntimeException {
    constructor(): super()
    constructor(message: String): super(message)
    constructor(cause: Throwable): super(cause)
    constructor(message: String, cause: Throwable): super(message, cause)
}