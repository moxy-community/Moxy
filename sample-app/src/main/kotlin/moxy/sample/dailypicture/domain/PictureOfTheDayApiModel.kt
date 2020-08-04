package moxy.sample.dailypicture.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val copyright: String
)
