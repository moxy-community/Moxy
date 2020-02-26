package android.os

class Bundle {
    fun getString(key: String) = ""
    fun getBundle(key: String) = Bundle()
    fun containsKey(key: String) = false
    fun putBundle(key: String, bundle: Bundle) = Unit
    fun putAll(bundle: Bundle) = Unit
    fun putString(key: String, string: String) = Unit
}
