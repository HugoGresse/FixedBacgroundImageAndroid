package tv.teads.fixedbackground

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Runnable {

    private lateinit var fixedBackgroundImagePlugin: FixedBackgroundImagePlugin
    private val handler = Handler()
    private var locationOnScreen: IntArray = IntArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fixedBackgroundImagePlugin = FixedBackgroundImagePlugin(
            Plugin.Listener { Log.d(TAG, "onPluginLoaded") },
            this,
            "https://hugo.gresse.io/teads/mankeo.jpg",
            ImageDownloader()
        )

        fixedBackgroundImagePlugin.setPlayerView(fixedBackgroundImageLayout)

        handler.postDelayed(this, RUN_MS)
    }

    override fun run() {
        scrollViewContainer.getLocationOnScreen(locationOnScreen)
        fixedBackgroundImagePlugin.update(locationOnScreen)

        handler.postDelayed(this, RUN_MS)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val RUN_MS = 10L
    }
}

