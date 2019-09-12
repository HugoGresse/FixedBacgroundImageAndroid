package tv.teads.fixedbackground


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper

class ImageDownloader {

    private var callbackHandler: Handler? = null

    fun getBitmap(context: Context, callback: ImageDownloaderCallback) {
        callbackHandler = Handler(Looper.myLooper()!!)

        val inputStream = context.assets.open("mankeo.jpg")

        val bitmap: Bitmap?
        try {
            val imgBytes = inputStream.readBytes()
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
        }
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
