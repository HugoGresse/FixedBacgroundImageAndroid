package tv.teads.fixedbackground

import android.view.View
import android.widget.ListView
import android.widget.ScrollView

import androidx.core.view.ScrollingView

/**
 * Return the first scrollable parent from the given view
 */
fun getFirstScrollableParent(view: View?): View? {
    if (view == null || view.parent == null) {
        return null
    }
    return if (view is ScrollView || view is ScrollingView || view is ListView
        || view === view.rootView
    ) {
        view
    } else getFirstScrollableParent(view.parent as View)
}
