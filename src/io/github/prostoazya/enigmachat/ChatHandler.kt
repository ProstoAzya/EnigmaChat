package io.github.prostoazya.enigmachat

import kotlin.properties.Delegates

object ChatHandler {
    var enabled by Delegates.notNull<Boolean>()
    lateinit var key: String
}