package moxy.sample.dailypicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.sample.databinding.FragmentDailyPictureBinding
import moxy.sample.ui.ViewBindingHolder

class DailyPictureFragment : MvpAppCompatFragment(),
    DailyPictureView {

    // moxyPresenter delegate is the recommended way to create an instance of Presenter in Kotlin.
    // This is a factory for creating presenter for this fragment. You can do it
    // any way you want: manually, or with DI framework of your choice.
    private val presenter: DailyPicturePresenter by moxyPresenter { DailyPicturePresenter() }

    private val bindingHolder = ViewBindingHolder<FragmentDailyPictureBinding>()
    private val binding get() = bindingHolder.binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingHolder.createView(viewLifecycleOwner) {
        FragmentDailyPictureBinding.inflate(inflater, container, false)
    }

    override fun showPicture(url: String) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
