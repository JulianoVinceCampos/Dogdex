package com.julianovincecampos.dogedex.di

import com.julianovincecampos.dogedex.machinelearning.ClassifierRepository
import com.julianovincecampos.dogedex.machinelearning.ClassifierTask
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierRepositoryModule {

    @Binds
    abstract fun provideClassifier(
        classifierRepository: ClassifierRepository
    ): ClassifierTask

}
