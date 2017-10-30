package mustafaozhan.github.com.simpleweather.extension

import android.widget.ImageView
import com.squareup.picasso.Picasso
import mustafaozhan.github.com.simpleweather.common.Common

/**
 * Created by Mustafa Ozhan on 10/30/17 at 8:18 PM on Arch Linux.
 */
fun ImageView.setByUrl(icon: ImageView, icon1: String) {
    Picasso.with(context)
            .load(Common.getImage(icon1))
            .into(icon)
}