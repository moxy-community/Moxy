package moxy.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import moxy.sample.dailypicture.data.KtorDailyPictureRepository
import moxy.sample.dailypicture.domain.DailyPictureRepository

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDailyPictureRepository(
        implementation: KtorDailyPictureRepository
    ): DailyPictureRepository
}
