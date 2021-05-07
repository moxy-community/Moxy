package moxy.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.DailyPictureInteractorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class InteractorModule {

    @Binds
    abstract fun provideDailyPictureInteractor(
        implementation: DailyPictureInteractorImpl,
    ): DailyPictureInteractor
}
