package com.example.test1.di


import com.example.test1.photoDB.QueryInterface
import com.example.test1.photoDB.QueryClass
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoModule {
    @Binds
    @Singleton
    abstract fun provideAPI(injectObject: QueryClass) : QueryInterface
}