package net.harimurti.tv.extension

import android.graphics.BitmapFactory
import android.widget.ImageView
import net.harimurti.tv.R
import okhttp3.*
import java.io.IOException

fun ImageView.loadUrl(url: String?) {
    // Set a placeholder that indicates loading
    setImageResource(R.mipmap.ic_launcher)

    if (url.isNullOrBlank()) {
        // if url is blank, show a "no image" placeholder
        setImageResource(R.drawable.ic_clear)
        return
    }

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            // Set an "error" placeholder
            post { setImageResource(R.drawable.ic_clear) }
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                post { setImageResource(R.drawable.ic_clear) }
                return
            }
            
            response.body?.byteStream()?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                     post { setImageBitmap(bitmap) }
                } else {
                     post { setImageResource(R.drawable.ic_clear) }
                }
            }
        }
    })
}
