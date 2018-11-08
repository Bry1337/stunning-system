package io.github.bry1337.noted.networks

import io.github.bry1337.noted.util.FakeNetworkCall

/**
 * Created by Edward Bryan Abergas on 08/11/2018.
 *
 * @author edwardbryan.abergas@gmail.com
 */

interface MainNetwork{
	fun fetchNewWelcome(): FakeNetworkCall<String>
}