package com.julianovincecampos.dogedex.di

import com.julianovincecampos.dogedex.doglist.DogRepositoryTask
import com.julianovincecampos.dogedex.doglist.DogRespository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DogRepositoryTaskModule {

    @Binds
    abstract fun bindDogTasks(
        dogRepository: DogRespository
    ): DogRepositoryTask

}