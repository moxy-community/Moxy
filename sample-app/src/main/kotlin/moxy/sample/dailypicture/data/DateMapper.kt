package moxy.sample.dailypicture.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateMapper {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun parse(date: String): LocalDate {
        return LocalDate.parse(date, formatter)
    }

    fun serialize(date: LocalDate): String {
        return date.format(formatter)
    }
}
