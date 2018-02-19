package me.paixao.tmdbexplorer.ui.movielist

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.widget.TextView
import me.paixao.tmdbexplorer.R
import me.paixao.tmdbexplorer.data.Movie
import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*


class MovieListItemViewModel(val context: Context,
                             val movie: Movie) {

    fun getDisplayDate(): String {
        val year = getMovieYear()
        if (year != 0)
            return "$year"
        return ""
    }

    private fun getMovieYear(): Int {
        if (movie.release_date != "")
            return dateParse(movie.release_date).year + 1900
        return 0
    }

    fun getDisplayDateColor(): Int {
        if (isThisYear())
            return context.resources.getColor(R.color.colorPrimary)
        return context.resources.getColor(android.R.color.white)
    }

    fun getDateBoldStyle(): String {
        if (isThisYear())
            return "bold"
        return "normal"
    }

    fun isThisYear(): Boolean = getMovieYear().equals(java.util.Calendar.getInstance().get(Calendar.YEAR))

    fun dateParse(s: String): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(s, ParsePosition(0))

    object DateHelper {
        const val DF_SIMPLE_STRING = "yyyy-MM-dd"
        @JvmField
        val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
            override fun initialValue(): DateFormat {
                return SimpleDateFormat(DF_SIMPLE_STRING, Locale.US)
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("android:typeface")
        fun setTypeface(v: TextView, style: String) {
            when (style) {
                "bold" -> v.setTypeface(null, Typeface.BOLD)
                else -> v.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

}