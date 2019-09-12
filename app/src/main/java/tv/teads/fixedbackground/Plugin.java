package tv.teads.fixedbackground;

import android.view.ViewGroup;


public interface Plugin {

    /**
     * Send the current player view to the plugin in order to him to modify it if needed
     */
    void setPlayerView(ViewGroup viewGroup);

    /**
     * Update the plugin (it would maybe change some UI or do what it need to do)
     */
    void update(int[] locationOnScreen);

    /**
     * Release the resources used in the plugin
     */
    void release();

    interface Listener {
        void onPluginLoaded();
    }

}
