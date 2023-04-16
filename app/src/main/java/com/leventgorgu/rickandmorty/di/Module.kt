package com.leventgorgu.rickandmorty.di

import com.leventgorgu.rickandmorty.api.RickAndMortyAPI
import com.leventgorgu.rickandmorty.repo.RickAndMortyRepository
import com.leventgorgu.rickandmorty.repo.RickAndMortyRepositoryInterface
import com.leventgorgu.rickandmorty.utils.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun injectRetrofit(gsonConverterFactory: GsonConverterFactory) : Retrofit{
        return  Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): RickAndMortyAPI {
        return retrofit.create(RickAndMortyAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo(api: RickAndMortyAPI)=RickAndMortyRepository(api) as RickAndMortyRepositoryInterface

}