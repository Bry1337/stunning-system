package io.github.bry1337.noted.application

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType.UNMETERED
import androidx.work.PeriodicWorkRequest

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * Override application setup background work via [WorkManager]
 */
class BaseApplication : Application() {

	override fun onCreate() {
		super.onCreate()
	}

	private fun setupWorkManagerJob() {
		// Use the constraints to require the work to run only when the device is charging
		// and the network is unmetered
		val constraints = Constraints.Builder()
				.setRequiresCharging(true)
				.setRequiredNetworkType(UNMETERED)
				.build()

		// Specify that the work should run everyday

	}
}