package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryImpl
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.QueryMonth
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.getNextMonth
import com.al4apps.skillcinema.domain.model.getQueryMonthFromMonthNumber
import java.text.SimpleDateFormat
import javax.inject.Inject

class GetPremieresUseCase @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl
) {
    suspend fun execute(): List<MovieModel> {
        val currentTimeMillis = System.currentTimeMillis()
        val queries = getQueryParams(currentTimeMillis)

        val result = mutableListOf<MovieModel>()
        queries.forEach { query ->
            result.addAll(moviesRepository.getMoviePremieres(query))
        }
        return result.filterByDates(currentTimeMillis)
    }

    private fun List<MovieModel>.filterByDates(currentTimeMillis: Long): List<MovieModel> {
        val currentDate = SimpleDateFormat(ApiSettings.API_DATE_FORMAT).format(currentTimeMillis)
        val startTimeMillis = dateToTimeStamp(currentDate)
        val endTimeMillis = startTimeMillis + ApiSettings.MOVIE_PREMIERES_RANGE_MILLIS

        return filter { movie ->
            movie.premiereRu != null && dateToTimeStamp(movie.premiereRu) in startTimeMillis..endTimeMillis
        }
    }

    private fun dateToTimeStamp(
        date: String
    ): Long {
        val simpleDateTimeFormat = SimpleDateFormat(ApiSettings.API_DATE_FORMAT)
        return simpleDateTimeFormat.parse(date)!!.time
    }

    private fun getQueryParams(millis: Long): List<QueryPremieres> {

        val simpleDateFormatDay = SimpleDateFormat("dd")
        val simpleDateFormatMonth = SimpleDateFormat("MM")
        val simpleDateFormatYear = SimpleDateFormat("yyyy")
        val day = simpleDateFormatDay.format(millis).toInt()
        val monthNumber = simpleDateFormatMonth.format(millis).toInt()
        val year = simpleDateFormatYear.format(millis).toInt()

        val queryMonth = getQueryMonthFromMonthNumber(monthNumber)

        if (queryMonth == QueryMonth.FEBRUARY) {
            val isLeapYear = year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)
            queryMonth.days = if (isLeapYear) 29 else 28
        }
        val queries = if (queryMonth.days - day >= 14) {
            listOf(QueryPremieres(year, queryMonth))
        } else {
            val nextMonth = queryMonth.getNextMonth()
            val nextYear = if (nextMonth.number < monthNumber) year + 1
            else year
            listOf(QueryPremieres(year, queryMonth), QueryPremieres(nextYear, nextMonth))
        }
        return queries
    }

}