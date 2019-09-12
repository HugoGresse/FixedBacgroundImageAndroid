package tv.teads.fixedbackground


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import okhttp3.*
import java.io.IOException

class ImageDownloader {

    private var callbackHandler: Handler? = null
    private val client = OkHttpClient()

    fun getBitmap(URI: String, callback: ImageDownloaderCallback) {
        callbackHandler = Handler(Looper.myLooper()!!)

        // Create request
        val networkRequest = Request.Builder()
            .url(URI)
            .build()

        val currentCall = client.newCall(networkRequest)

        // Call API
        currentCall.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    reportError(callback, Exception("Server error"))
                    response.body?.close()
                    return
                }

                val bitmap: Bitmap?
                try {
                    val imgBytes = response.body!!.bytes()
                    var ratioToResize = imgBytes.size / 2000000 //We calcule the right ratio for the
                    // image weight < 2mo

                    val options = BitmapFactory.Options()
                    options.inSampleSize = ratioToResize
                    bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size, options)
                    if (bitmap == null) {
                        reportError(callback, Exception("Impossible to decode scroller image"))
                        return
                    }

                    ratioToResize =
                        bitmap.byteCount / 4000000 // Sometime "decodeByteArray" return bigger image
                    // depending of the image encoding, so we resize to manipulate only a light image
                    if (ratioToResize <= 1) {
                        reportDownload(callback, bitmap)
                    } else {
                        val resizedBitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            bitmap.width / ratioToResize,
                            bitmap.height / ratioToResize,
                            false
                        )
                        if (resizedBitmap == null) {
                            reportError(callback, Exception("Impossible to resize scroller image"))
                            return
                        }

                        reportDownload(callback, resizedBitmap)
                        bitmap.recycle()
                    }
                } catch (e: Exception) {
                    reportError(callback, e)
                } catch (e: OutOfMemoryError) {
                    reportError(callback, Exception(e.message))
                } finally {
                    response.body?.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(Exception("Server Error"))
            }
        })
    }

    private fun reportError(callback: ImageDownloaderCallback, e: Exception) {
        callbackHandler?.post { callback.onError(e) }
    }

    private fun reportDownload(callback: ImageDownloaderCallback, b: Bitmap?) {
        callbackHandler?.post { callback.imageDownloaded(b) }
    }

    interface ImageDownloaderCallback {

        fun imageDownloaded(bitmap: Bitmap?)
        fun onError(e: Exception)
    }

}
