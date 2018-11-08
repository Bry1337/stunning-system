package io.github.bry1337.noted.activities.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.github.bry1337.noted.R

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

/**
 * Main Activity of the application. This activity uses [MainViewModel] to implement MVVM.
 */
class MainActivity : AppCompatActivity() {

	/**
	 * Inflate layout and setup click listeners and LiveData observers.
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val rootLayout = findViewById<ConstraintLayout>(R.id.rootLayout)

		val viewModel = ViewModelProviders.of(this)
				.get(MainViewModel::class.java)

		// When rootLayout is clicked call on onMainViewClicked in ViewModel
		rootLayout.setOnClickListener { viewModel.onMainViewClicked() }

		// Show a snackbar whenever the [ViewModel.snackbar] is updated with a non-null value
		viewModel.snackbar.observe(this, Observer { text ->
			text?.let {
				Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT)
						.show()
				viewModel.onSnackbarShown()
			}
		})
	}
}
