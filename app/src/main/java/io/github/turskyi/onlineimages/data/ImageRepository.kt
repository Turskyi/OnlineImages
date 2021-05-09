package io.github.turskyi.onlineimages.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.turskyi.onlineimages.data.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ApiPagingSource(unsplashApi, query) }
        ).liveData
}