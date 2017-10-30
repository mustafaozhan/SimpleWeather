package mustafaozhan.github.com.simpleweather.extension

import android.widget.ImageView
import com.squareup.picasso.Picasso
import mustafaozhan.github.com.simpleweather.R
import mustafaozhan.github.com.simpleweather.common.Common

/**
 * Created by Mustafa Ozhan on 10/30/17 at 8:18 PM on Arch Linux.
 */
fun ImageView.setFromDrawableByName(name: String) {
    when(name){
        "01d"->this.setImageResource(R.drawable._01d)
        "01n"->this.setImageResource(R.drawable._01n)
        "02d"->this.setImageResource(R.drawable._02d)
        "02n"->this.setImageResource(R.drawable._02n)
        "03d"->this.setImageResource(R.drawable._03d)
        "03n"->this.setImageResource(R.drawable._03n)
        "04d"->this.setImageResource(R.drawable._04d)
        "04n"->this.setImageResource(R.drawable._04n)
        "09d"->this.setImageResource(R.drawable._09d)
        "09n"->this.setImageResource(R.drawable._09n)
        "10d"->this.setImageResource(R.drawable._10d)
        "10n"->this.setImageResource(R.drawable._10n)
        "11d"->this.setImageResource(R.drawable._11d)
        "11n"->this.setImageResource(R.drawable._11n)
        "13d"->this.setImageResource(R.drawable._13d)
        "13n"->this.setImageResource(R.drawable._13n)
        "50d"->this.setImageResource(R.drawable._50d)
        "50n"->this.setImageResource(R.drawable._50n)
    }
}