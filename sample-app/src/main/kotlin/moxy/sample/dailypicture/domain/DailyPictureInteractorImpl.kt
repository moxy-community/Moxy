package moxy.sample.dailypicture.domain

import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextLong

class DailyPictureInteractorImpl
@Inject
constructor(
    private val repository: DailyPictureRepository
) : DailyPictureInteractor {

    /**
     * Get Astronomy Picture of the Day for specific [date].
     * `null` [date] loads the picture for today.
     *
     * Note that this function is `suspend`-ing. It means that it's asynchronous.
     * In our case the DailyPictureRepository executes the network request
     * in background. The function suspends for that time, and resumes execution after
     * the request is complete. The Main thread is not blocked, and everyone is happy.
     */
    override suspend fun getPicture(date: LocalDate?): PictureOfTheDay {
        return repository.getPicture(date)
    }

    /**
     * Return random date within acceptable interval.
     */
    override fun getRandomDate(): LocalDate {
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
