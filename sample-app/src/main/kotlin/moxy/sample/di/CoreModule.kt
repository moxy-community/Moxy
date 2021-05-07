package moxy.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moxy.sample.util.AndroidLogger
import moxy.sample.util.Logger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun provideLogger(
        implementation: AndroidLogger,
    ): Logger
}
