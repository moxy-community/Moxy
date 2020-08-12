package moxy.sample.dailypicture.domain

import java.time.LocalDate

interface DailyPictureInteractor {

    suspend fun getPicture(date: LocalDate?): PictureOfTheDay

    fun getRandomDate(): LocalDate
}
