package moxy.sample.dailypicture.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import moxy.sample.dailypicture.data.DateMapper
import moxy.sample.dailypicture.data.NasaApi
import moxy.sample.dailypicture.data.PictureOfTheDayApiModel
import moxy.sample.dailypicture.data.toDomain
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextLong

class DailyPictureInteractor(
    private val httpClient: HttpClient
) {

    /**
     * Get Astronomy Picture of the Day for specific [date].
     * `null` [date] loads the picture for today.
     *
     * Note that this function is `suspend`-ing. It
     */
    suspend fun getPicture(date: LocalDate? = null): PictureOfTheDay {
        return httpClient.get<PictureOfTheDayApiModel>(NasaApi.APOD_URL) {
            parameter("api_key", NasaApi.KEY)
            date?.let { parameter("date", DateMapper.serialize(it)) }
        }.toDomain()
    }

    /**
     * Return random date within acceptable interval.
     */
    fun getRandomDate(): LocalDate {
        val minEpochDay = minDate.toEpochDay()
        val maxEpochDay = LocalDate.now().toEpochDay()
        val randomEpochDay = Random.Default.nextLong(minEpochDay..maxEpochDay)
        return LocalDate.ofEpochDay(randomEpochDay)
    }

    companion object {

        /**
         * According to NASA API specification, the minimum date is Jun 16, 1995.
         */
        private val minDate = LocalDate.of(1995, 6, 16)
    }
}
