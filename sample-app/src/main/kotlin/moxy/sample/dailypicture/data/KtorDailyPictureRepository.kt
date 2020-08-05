package moxy.sample.dailypicture.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import moxy.sample.dailypicture.domain.DailyPictureRepository
import moxy.sample.dailypicture.domain.PictureOfTheDay
import java.time.LocalDate

class KtorDailyPictureRepository(
    private val httpClient: HttpClient
) : DailyPictureRepository {

    override suspend fun getPicture(date: LocalDate?): PictureOfTheDay {
        return httpClient.get<PictureOfTheDayApiModel>(NasaApi.APOD_URL) {
            parameter("api_key", NasaApi.KEY)
            date?.let { parameter("date", DateMapper.serialize(it)) }
        }.toDomain()
    }
}
