package moxy.sample.dailypicture.domain

import java.time.LocalDate

interface DailyPictureRepository {

    suspend fun getPicture(date: LocalDate?): PictureOfTheDay
}
