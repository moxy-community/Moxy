package moxy.sample.dailypicture.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DailyPictureInteractor(
    private val httpClient: HttpClient
) {

    suspend fun getPictureForToday(): PictureOfTheDayApiModel {
        return httpClient.get(NasaApi.APOD_URL) {
            parameter("api_key", NasaApi.KEY)
        }
    }
}
