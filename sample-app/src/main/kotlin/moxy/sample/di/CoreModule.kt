package moxy.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import moxy.sample.util.AndroidLogger
import moxy.sample.util.Logger

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {

    @Provides
    fun provideLogger(): Logger {
        return AndroidLogger()
    }
}
