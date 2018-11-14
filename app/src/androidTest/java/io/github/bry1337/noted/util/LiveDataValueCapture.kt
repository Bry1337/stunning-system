package io.github.bry1337.noted.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import com.google.common.truth.Truth

/**
 * Created by Edward Bryan Abergas on 14/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * Represents a list of capture values from a LiveData.
 *
 * This class is not threadsafe and must be used from the main thread.
 */
class LiveDataValueCapture<T> {

    private val _values = mutableListOf<T?>()
    val values: List<T?>
        get() = _values

    val channel = Channel<T?>(Channel.UNLIMITED)

    fun addValue(value: T?) {
        _values += value
        channel.offer(value)
    }

    suspend fun assertSendsValues(timeout: Long, vararg expected: T?) {
        val expectedList = expected.asList()
        if (values == expectedList) {
            return
        }
        try {
            withTimeout(timeout) {
                for (value in channel) {
                    if (values == expectedList) {
                        return@withTimeout
                    }
                }
            }
        } catch (ex: TimeoutCancellationException) {
            Truth.assertThat(values).isEqualTo(expectedList)
        }
    }
}

/**
 * Extension function to capture all values that are emitted to a LiveData<T> during the execution of
 * `captureBlock`.
 *
 * @param captureBlock a lambda that will
 */
inline fun <T> LiveData<T>.captureValues(block: LiveDataValueCapture<T>.() -> Unit) {
    val capture = LiveDataValueCapture<T>()
    val observer = Observer<T> {
        capture.addValue(it)
    }
    observeForever(observer)
    capture.block()
    removeObserver(observer)
}

/**
 * Get the current value from a LiveData without needing to register an observer.
 */
fun <T> LiveData<T>.getValueForTest(): T? {
    var value: T? = null
    var observer = Observer<T> {
        value = it
    }
    observeForever(observer)
    removeObserver(observer)
    return value
}