package moxy.sample.dailypicture.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class PictureOfTheDayApiModel(
    @SerialName("date")
    val date: String,
    @SerialName("url")
    val url: String,
    @SerialName("title")
    val title: String,
    @SerialName("explanation")
    val explanation: String,
    @SerialName("copyright")
    val copyright: String?,
    @SerialName("media_type")
    val mediaType: String
)

fun PictureOfTheDayApiModel.toDomain(): PictureOfTheDay {
    return PictureOfTheDay(
        date = parseDate(this.date),
        url = this.url,
        title = this.title,
        explanation = this.explanation,
        copyright = this.copyright.orEmpty(),
        mediaType = parseMediaType(this.mediaType)
    )
}

private fun parseDate(date: String): LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
}

private fun parseMediaType(mediaType: String): PictureOfTheDay.MediaType {
    return when (mediaType) {
        "image" -> PictureOfTheDay.MediaType.IMAGE
        "video" -> PictureOfTheDay.MediaType.VIDEO
        else -> PictureOfTheDay.MediaType.UNKNOWN
    }
}
