package moxy.sample.dailypicture.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moxy.sample.dailypicture.domain.PictureOfTheDay

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
    val copyright: String? = null,
    @SerialName("media_type")
    val mediaType: String
)

fun PictureOfTheDayApiModel.toDomain(): PictureOfTheDay {
    return PictureOfTheDay(
        date = DateMapper.parse(this.date),
        url = this.url,
        title = this.title,
        explanation = this.explanation,
        copyright = this.copyright.orEmpty(),
        mediaType = parseMediaType(this.mediaType)
    )
}

private fun parseMediaType(mediaType: String): PictureOfTheDay.MediaType {
    return when (mediaType) {
        "image" -> PictureOfTheDay.MediaType.IMAGE
        "video" -> PictureOfTheDay.MediaType.VIDEO
        else -> PictureOfTheDay.MediaType.UNKNOWN
    }
}
