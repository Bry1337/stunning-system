package io.github.bry1337.noted.repositories

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

}