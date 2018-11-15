package io.github.bry1337.noted


import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.bry1337.noted.activities.main.MainViewModel
import io.github.bry1337.noted.util.captureValues
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Edward Bryan Abergas on 14/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */
@RunWith(JUnit4::class)
class MainViewModelTest {


    /**
     * In this test, LiveData will immediately post values without switching threads.
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: MainViewModel

    /**
     * Before the test run initialize object
     */
    @Before
    fun setup() {
        subject = MainViewModel()
    }

    @Test
    fun whenMainViewModelClicked_showSnackBar() {
        runBlocking {
            subject.snackbar.captureValues {
                subject.onMainViewClicked(0)
                assertSendsValues(2_000, "Hello, from threads")
            }
        }
    }

}