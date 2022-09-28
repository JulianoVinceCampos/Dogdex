package com.julianovincecampos.dogedex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

//    @IoDispatcher
    @Provides
    fun providesDispatcherIO() = Dispatchers.IO

//    @MainDispatcher
//    @Provides
//    fun providesDispatcherMain() = Dispatchers.Main
//
//    @DefaultDispatcher
//    @Provides
//    fun providesDispatcherDefault() = Dispatchers.Default
//
//    annotation class DefaultDispatcher
//
//    annotation class MainDispatcher
//
//    annotation class IoDispatcher

}