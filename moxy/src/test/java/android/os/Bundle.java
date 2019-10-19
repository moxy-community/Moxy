package android.os;

public class Bundle {

    public String getString(String key) {
        return "";
    }

    public Bundle getBundle(String key) {
        return new Bundle();
    }

    public boolean containsKey(String key) {
        return false;
    }

    public void putBundle(String key, Bundle bundle) {
    }

    public void putAll(Bundle bundle) {
    }

    public void putString(String key, String string) {
    }
}
