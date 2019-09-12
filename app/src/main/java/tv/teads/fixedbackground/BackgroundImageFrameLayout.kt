package tv.teads.fixedbackground

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Display an image in a frame layout, this view resize its background to optimise the width / height ratio.
 */
class BackgroundImageFrameLayout(context: Context) : FrameLayout(context) {

    private var displayImage: Bitmap? = null

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        resizeBackground(MeasureSpec.getSize(widthMeasureSpec))
    }

    fun setBackground(bitmap: Bitmap) {
        displayImage = bitmap
    }

    /**
     * Resize the back ground width a given width to match perfectly this view.
     * If the displayImage is not big enough, it will be repeated
     *
     * @param width the with to resize
     */
    private fun resizeBackground(width: Int) {
        if (displayImage == null) {
            return
        }

        if (width <= 0) {
            return
        }
        try {
            val imageResizedHeight =
                width.toFloat() * (displayImage!!.height.toFloat() / displayImage!!.width.toFloat())
            val resizedBitmap =
                Bitmap.createScaledBitmap(displayImage!!, width, imageResizedHeight.toInt(), false)
            val displayImg = BitmapDrawable(resources, resizedBitmap)
            displayImg.tileModeY = Shader.TileMode.REPEAT
            background = displayImg
        } catch (ignored: Exception) {
        }
    }

    /**
     * Clean the background bitmap if it exist
     */
    fun cleanDisplayImage() {
        if (background != null && (background as BitmapDrawable).bitmap != null) {
            (background as BitmapDrawable).bitmap.recycle()
            displayImage = null
        }
    }
}
