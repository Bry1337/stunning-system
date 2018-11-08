package io.github.bry1337.noted.util

import android.os.Handler
import android.os.Looper
import java.util.Random
import java.util.concurrent.Executors

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * This file contains a completely fake networking library that returns a random value
 * after a delay.
 *
 * The API is intended to look similar to Retrofit or Volley without requiring a network.
 *
 * Callers will be a given a [FakeNetworkCall] object and call
 * [FakeNetworkCall.addOnResultListener] to get results from the "network".
 *
 * Results are represented by sealed [FakeNetworkResults] with [FakeNetworkSuccess] and
 * [FakeNetworkError] subclasses.
 *
 */
private const val ONE_SECOND = 1_000L

private const val ERROR_RATE = 0.3

private val executor = Executors.newCachedThreadPool()

private val uiHandler = Handler(Looper.getMainLooper())

/**
 * A completely fake network library that returns from a given
 * list of strings or an error.
 */
fun fakeNetworkLibrary(from: List<String>): FakeNetworkCall<String> {
	assert(from.isNotEmpty()) { "You must pass at least one result string" }
	val result = FakeNetworkCall<String>()

	// Launch the "network request" in a new thread to avoid blocking the calling thread
	executor.submit {
		Thread.sleep(ONE_SECOND) // pretend we actually made a network request by sleeping

		// pretend we got a result from the passed list, or random error
		if (DefaultErrorDecisionStrategy.shouldError()) {
			result.onError(FakeNetworkException("Error contacting the network"))
		} else {
			result.onSuccess(from[Random().nextInt(from.size)])
		}
	}
	return result
}

/**
 * Error decision strategy is used to decide if an error should be returned
 * by the fake request.
 */
interface ErrorDecisionStrategy {
	fun shouldError(): Boolean
}

/**
 * Default error decision strategy allows us to override the behavior of a decision
 * strategy in tests
 */
object DefaultErrorDecisionStrategy : ErrorDecisionStrategy {
	var delegate: ErrorDecisionStrategy = RandomErrorStrategy

	override fun shouldError() = delegate.shouldError()
}

/**
 * Random error decision strategy uses random to return error randomly
 */
object RandomErrorStrategy : ErrorDecisionStrategy {
	override fun shouldError() = Random().nextFloat() < ERROR_RATE
}

/**
 * Fake Call for our network library used to observe results
 */
class FakeNetworkCall<T> {

	var results: FakeNetworkResults<T>? = null

	val listeners = mutableListOf<FakeNetworkListener<T>>()

	/**
	 * Register a result listener to observe this callback.
	 *
	 * Errors will be passed to this callback as an instance of [FakeNetworkError] and successful
	 * calls will be passed to this callback as an instance of [FakeNetworkSuccess].
	 *
	 * @param listener the callback to call when this request completes.
	 */
	fun addOnResultListener(listener: (FakeNetworkResults<T>) -> Unit) {
		trySendResult(listener)
		listeners += listener
	}

	/**
	 * The library will call this when a result is available
	 */
	fun onSuccess(data: T) {
		results = FakeNetworkSuccess(data)
		sendResultToAllListeners()
	}

	/**
	 * The library will call this when an error happens
	 */
	fun onError(throwable: Throwable) {
		results = FakeNetworkError(throwable)
		sendResultToAllListeners()
	}

	/**
	 * Broadcast the current result (success or error) to all registered listeners.
	 */
	private fun sendResultToAllListeners() = listeners.map { trySendResult(it) }

	/**
	 * Send the current result to a specific listeners.
	 *
	 * If no result is set (null), this method will do nothing.
	 */
	private fun trySendResult(listener: FakeNetworkListener<T>) {
		val thisResult = results
		thisResult?.let {
			uiHandler.post {
				listener(thisResult)
			}
		}
	}

}

/**
 * Network result class that represents both success and errors
 */
sealed class FakeNetworkResults<T>

/**
 * Passed to listener when the network request was successful
 *
 * @param data the result
 */
class FakeNetworkSuccess<T>(val data: T) : FakeNetworkResults<T>()

/**
 * Passed to listener when the network failed
 *
 * @param error the exception that caused this error
 */
class FakeNetworkError<T>(val error: Throwable) : FakeNetworkResults<T>()


/**
 * Listener "type" for observing a [FakeNetworkCall]
 */
typealias FakeNetworkListener<T> = (FakeNetworkResults<T>) -> Unit

/**
 * Throwable to use in fake network errors.
 */
class FakeNetworkException(message: String) : Throwable(message)
