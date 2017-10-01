package mustafaozhan.github.com.simpleweather.common.extension

import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import mustafaozhan.github.com.simpleweather.common.Common

/**
 * Created by Mustafa Ozhan on 10/1/17 at 10:48 AM on Arch Linux.
 */
fun ImageView.setImageByUrl(icon: String) {
    Picasso.with(imageView.context)
            .load(Common.getImage(icon))
            .into(this)

}