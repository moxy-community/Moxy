package moxy.sample.ui

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * Lifecycle-aware holder for ViewBinding instance.
 * Releases a reference to ViewBinding after the View is destroyed
 * so you don't need to do it manually.
 */
class ViewBindingHolder<T : ViewBinding> {

    private var _binding: T? = null

    val binding: T
        get() = _binding!!

    fun createView(lifecycleOwner: LifecycleOwner, inflater: () -> T): View {
        val newBinding = inflater.invoke()
        _binding = newBinding
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _binding = null
            }
        })
        return newBinding.root
    }
}
