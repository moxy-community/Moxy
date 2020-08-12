package moxy.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.DailyPictureInteractorImpl
import moxy.sample.dailypicture.domain.DailyPictureRepository
import moxy.sample.util.CoroutineDispatcherProvider

@Module
@InstallIn(ApplicationComponent::class)
object InteractorModule {

    @Provides
    fun provideDailyPictureInteractor(repository: DailyPictureRepository): DailyPictureInteractor {
        return DailyPictureInteractorImpl(repository)
    }

    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProvider()
    }
}
