package io.github.bry1337.noted.activities.main

import android.content.Context
import android.support.annotation.WorkerThread
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * Worker job to refresh titles from the network while the app is in the background.
 *
 * WorkManager is a library used to enqueue work that is guaranteed to execute after its
 * constraints are met. It can work even when the app is in the background, or not running.
 */
class RefreshMainDataWork(context: Context,
	params: WorkerParameters) : Worker(context, params) {

	/**
	 * Do our actual processing for the worker.
	 *
	 * WorkManager will call this method from a background thread. It may be even after
	 * the app has been terminated by the operating system, in which case [WorkManager] will start
	 * just enough to run this [Worker]
	 */
	override fun doWork(): Result {
		return refreshTitle()
	}

	/**
	 * Refresh the title from the network using [TItleRepository]
	 */
	// TODO: Implement refreshTitle using coroutines and runBlocking
	@WorkerThread
	private fun refreshTitle(): Result = Result.SUCCESS

}