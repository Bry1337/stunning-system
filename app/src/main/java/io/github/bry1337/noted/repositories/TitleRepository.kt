package io.github.bry1337.noted.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import io.github.bry1337.noted.databases.TitleDao
import io.github.bry1337.noted.networks.MainNetwork

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * TitleRepository provides an interface to fetch a title or
 * request a new one be generated.
 *
 * Repository modules handle data operations. They provide a clean API so that
 * the rest of the app can retrieve this data easily. They know where to get the data from
 * and what API calls to make when data is updated. You can consider repositories
 * to be mediators between different data sources, in our case it mediates between
 * a network API and an offline database cache.
 */
class TitleRepository(private val network: MainNetwork,
                      private val titleDao: TitleDao) {

    /**
     * [LiveData] to load title.
     *
     * This is the main interface for loading the title. The title will be loaded
     * from the offlien cache.
     *
     * Observing this will not cause the title to be refreshed. use [TitleRepository.refreshTitle]
     * to refresh the title.
     *
     * Because this is defined as `by lazy` it won't be instantiated until the property id used
     * for the first time.
     */
    val title: LiveData<String> by lazy<LiveData<String>>(LazyThreadSafetyMode.NONE) {
        Transformations.map(titleDao.loadTitle()) {
            it.title
        }
    }

    fun refreshTitle(onStateChanged: TitleStateListener) {

    }

    /**
     * Class that represents the state of a refresh request.
     *
     * Sealed class can only be extended from inside this file.
     */
    // TODO: Remove this class after rewriting refreshTitle
    sealed class RefreshState {

        /**
         * The request is currently loading.
         *
         * An object is a singleton that cannot have more than one instance.
         */
        object Loading : RefreshState()

        /**
         * The request has completed successfully.
         *
         * An object is a singleton that cannot have more than one instance.
         */
        object Success : RefreshState()

        /**
         * The request has completed with an error.
         *
         * @param throwable error message ready to be displayed.
         */
        class Error(val throwable: Throwable) : RefreshState()
    }

}

/**
 * Listener for [RefreshState] changes.
 *
 * A typealias introduces a shorthand way to say a complex type.
 * It does not create a new type.
 */
// TODO: Remove this typealias after rewriting refreshTitle
typealias TitleStateListener = (TitleRepository.RefreshState) -> Unit

/**
 * Thrown when there was an error fetching a new title.
 *
 * @property message user ready error message
 * @property cause the original cause of this exception
 */
class TitleRefreshError(cause: Throwable) : Throwable(cause.message, cause)

/**
 * Suspend function to use callback-based [FakeNetworkCall] in coroutines.
 *
 * @return network result after completion.
 * @throws throwable original exception from the library if network request fails.
 */
// TODO: Implement FakeNetworkCall<T>.wait() here