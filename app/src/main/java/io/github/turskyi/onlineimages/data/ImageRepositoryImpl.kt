package io.github.turskyi.onlineimages.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.turskyi.onlineimages.data.network.datasource.PhotoNetSource
import io.github.turskyi.onlineimages.data.network.service.OnlineImagesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(private val onlineImagesApi: OnlineImagesApi) {
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoNetSource(onlineImagesApi, query) }
        ).liveData
}