@file:JvmName("Util")

package com.liminghuang.vrouter

import okhttp3.OkHttpClient
import java.util.*

/** Returns an immutable copy of this. */
fun <T> List<T>.toImmutableList(): List<T> {
    return Collections.unmodifiableList(toMutableList())
}

/** Returns an immutable list containing [elements]. */
@SafeVarargs
fun <T> immutableListOf(vararg elements: T): List<T> {
    return Collections.unmodifiableList(Arrays.asList(*elements.clone()))
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun Any.assertThreadDoesntHoldLock() {
    if (assertionsEnabled && Thread.holdsLock(this)) {
        throw AssertionError("Thread ${Thread.currentThread().name} MUST NOT hold lock on $this")
    }
}

@JvmField
internal val assertionsEnabled = VRouter::class.java.desiredAssertionStatus()

inline fun threadName(name: String, block: () -> Unit) {
    val currentThread = Thread.currentThread()
    val oldName = currentThread.name
    currentThread.name = name
    try {
        block()
    } finally {
        currentThread.name = oldName
    }
}