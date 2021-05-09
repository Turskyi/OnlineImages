package io.github.turskyi.onlineimages.data

import androidx.paging.PagingSource
import io.github.turskyi.onlineimages.data.api.PhotoResponse
import io.github.turskyi.onlineimages.data.api.UnsplashApi
import io.github.turskyi.onlineimages.data.api.UnsplashResponse
import retrofit2.HttpException
import java.io.IOException

/* we do not set this constant to the companion object of ApiPagingSource
 because it does not belong to it */
private const val UNSPLASH_STARTING_PAGE_INDEX = 1

// query variable is available only at runtime so we cannot inject it with dagger
class ApiPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, PhotoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response: UnsplashResponse =
                unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos: List<PhotoResponse> = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            // example: no internet connection
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // example: server error
            LoadResult.Error(exception)
        }
    }
}