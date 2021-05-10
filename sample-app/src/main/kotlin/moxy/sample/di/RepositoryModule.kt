package moxy.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import moxy.sample.dailypicture.data.KtorDailyPictureRepository
import moxy.sample.dailypicture.domain.DailyPictureRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDailyPictureRepository(
        implementation: KtorDailyPictureRepository,
    ): DailyPictureRepository
}
