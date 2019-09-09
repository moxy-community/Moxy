package moxy;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class PresenterStore {

    private Map<String, MvpPresenter> presenters = new HashMap<>();

    /**
     * Add presenter to storage
     *
     * @param tag Tag of a presenter. Local presenters also contain delegate's tag as their prefix
     * @param instance Instance of MvpPresenter implementation to store
     * @param <T> Type of a presenter
     */
    public <T extends MvpPresenter> void add(String tag, T instance) {
        presenters.put(tag, instance);
    }

    /**
     * Get a presenter by tag 
     *
     * @param tag Tag of a presenter. Local presenters also contain delegate's tag as their prefix
     * @return Presenter if exists. Null otherwise (if it doesn't exists)
     */
    public MvpPresenter get(String tag) {
        return presenters.get(tag);
    }

    /**
     * Remove a presenter from store.
     *
     * @param tag Tag of a presenter. Local presenters also contain delegate's tag as their prefix
     * @return Presenter that was removed
     */
    public MvpPresenter remove(String tag) {
        return presenters.remove(tag);
    }

    public void logPresenters() {
        for (Map.Entry<String, MvpPresenter> currentEntry : presenters.entrySet()) {
            Log.d("PresenterStore", currentEntry.getKey() + " -> " + currentEntry.getValue());
        }
    }
}
