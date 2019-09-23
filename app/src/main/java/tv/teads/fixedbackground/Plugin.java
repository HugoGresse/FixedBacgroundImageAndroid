package tv.teads.fixedbackground;

import android.view.ViewGroup;


public interface Plugin {

    /**
     * Send the current player view to the plugin in order to him to modify it if needed
     */
    void setPlayerView(ViewGroup viewGroup);

    /**
     * Release the resources used in the plugin
     */
    void release();

    interface Listener {
        void onPluginLoaded();
    }

}
