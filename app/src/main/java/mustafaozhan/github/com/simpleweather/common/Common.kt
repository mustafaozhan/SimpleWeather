package mustafaozhan.github.com.simpleweather.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mustafa Ozhan on 9/30/17 at 11:11 AM on Arch Linux.
 */
object Common {
    private val API_KEY = "365891e5ccaa5c76adac3f14ce4f2a26"
    private val API_LINK = "http://api.openweathermap.org/data/2.5/weather"
    private val FUTURE_LINK="http://api.openweathermap.org/data/2.5/forecast/daily"


    fun apiRequest(lat: String, lng: String): String {
        val sb = StringBuilder(API_LINK)
        sb.append("?lat=$lat&lon=$lng&APPID=$API_KEY&units=metric")
        return sb.toString()
    }
    fun futureApiRequest(lat: String, lng: String): String {
        val sb = StringBuilder(FUTURE_LINK)
        sb.append("?lat=$lat&lon=$lng&APPID=$API_KEY&units=metric&cnt=8")
        return sb.toString()
    }

    fun unixTimeStampToDateTime(unixTimeStamp: Double): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        date.time = unixTimeStamp.toLong() * 1000
        return dateFormat.format(date)
    }

    fun getCurrentDateAndTime(): String {
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
        val date = Date()
        return dateFormat.format(date)
    }
    fun getCurrentDate(i:Int): String {
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val date = Date()
        return dateFormat.format(date.time + (i*1000 * 60 * 60 * 24))
    }

    fun getImage(icon: String) = "http://openweathermap.org/img/w/$icon.png"
}