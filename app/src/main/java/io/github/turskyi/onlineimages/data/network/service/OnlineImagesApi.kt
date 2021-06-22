package io.github.turskyi.onlineimages.data.network.service

import io.github.turskyi.onlineimages.BuildConfig.UNSPLASH_ACCESS_KEY
import io.github.turskyi.onlineimages.data.entities.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OnlineImagesApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = UNSPLASH_ACCESS_KEY
        const val SEARCH_PHOTOS_ENDPOINT = "search/photos"
    }

    /* suspend function can be run without blocking the main thread,
    * it can be called only from another suspend function or coroutine */
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET(SEARCH_PHOTOS_ENDPOINT)
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}