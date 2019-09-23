package tv.teads.fixedbackground

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import java.lang.ref.WeakReference

class FixedBackgroundImagePlugin(
    private val listener: Plugin.Listener?,
    context: Context,
    imageUrl: String,
    imageDownloader: ImageDownloader
) : Plugin, ImageDownloader.ImageDownloaderCallback {
    private var bitmap: Bitmap? = null
    private var playerViewGroup: WeakReference<ViewGroup>? = null
    private var backgroundImageFrameLayout: BackgroundImageFrameLayout? = null


    init {
        playerViewGroup = WeakReference<ViewGroup>(null)

        Log.d(TAG, "Download image: $imageUrl")
        imageDownloader.getBitmap(context, this)
    }

    override fun setPlayerView(viewGroup: ViewGroup) {
        playerViewGroup = WeakReference(viewGroup)
        displayImage()
    }

    override fun release() {
        if (backgroundImageFrameLayout != null) {
            backgroundImageFrameLayout!!.cleanDisplayImage()
            backgroundImageFrameLayout = null
        }
    }

    override fun imageDownloaded(bitmap: Bitmap?) {
        this.bitmap = bitmap

        displayImage()
        listener?.onPluginLoaded()
    }

    override fun onError(e: Exception) {
        e.printStackTrace()
    }

    private fun displayImage() {
        val playerGroupViewGroup = playerViewGroup!!.get()
        if (playerGroupViewGroup == null || bitmap == null) {
            return
        }
        try {
            backgroundImageFrameLayout = BackgroundImageFrameLayout(playerGroupViewGroup.context)
            backgroundImageFrameLayout?.setBackground(bitmap!!)
            playerGroupViewGroup.addView(backgroundImageFrameLayout, 0)
            playerGroupViewGroup.requestLayout()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val TAG = "FixedBackgroundImagePlugin"
    }

}
