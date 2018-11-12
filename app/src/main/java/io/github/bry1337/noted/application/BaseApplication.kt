package io.github.bry1337.noted.application

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType.UNMETERED
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.github.bry1337.noted.activities.main.RefreshMainDataWork
import java.util.concurrent.TimeUnit

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
        val work = PeriodicWorkRequest.Builder(RefreshMainDataWork::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // Enqueue it with WorkManager, keeping any previously scheduled jobs for the same work.
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(RefreshMainDataWork::class.java.name, ExistingPeriodicWorkPolicy.KEEP, work)
    }
}