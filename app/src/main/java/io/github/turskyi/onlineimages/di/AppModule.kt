package io.github.turskyi.onlineimages.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.turskyi.onlineimages.data.network.service.OnlineImagesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/* The methods in this Module are never called manually,
* these are instructions for dagger how to call them.
* InstallIn is an annotation from Hilt to identify scope of this module,
* and [SingletonComponent] is an autogenerated class by hilt,
* which identify that all provided classes should live as long as "component". */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OnlineImagesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Since we already declared how to get retrofit we now can pass it as a parameter.
    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): OnlineImagesApi {
        return retrofit.create(OnlineImagesApi::class.java)
    }
}