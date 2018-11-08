package io.github.bry1337.noted.activities.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.bry1337.noted.util.BACKGROUND

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way.
 * This allows data to survive configuration changes such as screen rotation.
 * In addition, background work such as fetching network results can continue through
 * configuration changes and deliver results after the new Fragment or Activity is available.
 */
class MainViewModel : ViewModel() {

	/**
	 * Request a snackbar to display a string
	 *
	 * This variable is private because we don't want to expose the MutableLiveData.
	 *
	 * MutableLiveData allows anyone to set a value, and MainViewModel is the only class
	 * that should be setting values.
	 */
	private val _snackBar = MutableLiveData<String>()

	/**
	 * Request a snackbar to display a string
	 *
	 * Use Transformations.map to wrap each string sent to _snackbar in a non-null value.
	 */
	val snackbar: LiveData<String>
		get() = _snackBar

	// TODO: Add viewModelJob and uiScope here

	// TODO: Add onCleared() here to cancel viewModelJob

	/**
	 * Wait one second to display snackbar.
	 */
	fun onMainViewClicked() {
		// TODO: replace with coroutine implementation
		BACKGROUND.submit {
			Thread.sleep(1_000)
			// use postValue since we're in a background thread
			_snackBar.postValue("Hello, from threads")
		}
	}

	/**
	 * Called immediately after the UI shows the snackbar.
	 */
	fun onSnackbarShown() {
		_snackBar.value = null
	}

}