package moxy.sample.dailypicture.domain

import java.time.LocalDate

data class PictureOfTheDay(
    val date: LocalDate,
    val url: String,
    val title: String,
    val explanation: String,
    val copyright: String,
    val mediaType: MediaType
) {

    enum class MediaType {
        IMAGE,
        VIDEO,
        UNKNOWN
    }
}
