package com.julianovincecampos.dogedex.di

import com.julianovincecampos.dogedex.auth.AuthRepository
import com.julianovincecampos.dogedex.auth.AuthTask
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryTaskModule {

    @Binds
    abstract fun provideAuthTask(
        authRepository: AuthRepository
    ): AuthTask

}