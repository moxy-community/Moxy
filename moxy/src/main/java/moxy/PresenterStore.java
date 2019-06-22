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
     * @param tag Tag of presenter. Local presenters contains also delegate's tag as prefix
     * @param instance Instance of MvpPresenter implementation to store
     * @param <T> Type of presenter
     */
    public <T extends MvpPresenter> void add(String tag, T instance) {
        presenters.put(tag, instance);
    }

    /**
     * Get presenter on existing params
     *
     * @param tag Tag of presenter. Local presenters contains also delegate's tag as prefix
     * @return Presenter if it's exists. Null otherwise (if it's no exists)
     */
    public MvpPresenter get(String tag) {
        return presenters.get(tag);
    }

    /**
     * Remove presenter from store.
     *
     * @param tag Tag of presenter. Local presenters contains also delegate's tag as prefix
     * @return Presenter which was removed
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
