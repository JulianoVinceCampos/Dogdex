package com.julianovincecampos.dogedex.di

import com.julianovincecampos.dogedex.BASE_URL
import com.julianovincecampos.dogedex.api.ApiService
import com.julianovincecampos.dogedex.api.ApiServiceInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().addInterceptor(ApiServiceInterceptor).build()

}
