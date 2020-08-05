package moxy.sample.dailypicture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.PictureOfTheDay
import moxy.sample.databinding.FragmentDailyPictureBinding
import moxy.sample.ui.ViewBindingHolder

class DailyPictureFragment : MvpAppCompatFragment(),
    DailyPictureView {

    // moxyPresenter delegate is the recommended way to create an instance of Presenter in Kotlin.
    // This is a factory for creating presenter for this fragment. You can do it
    // any way you want: manually, or with DI framework of your choice.
    private val presenter: DailyPicturePresenter by moxyPresenter {
        DailyPicturePresenter(
            DailyPictureInteractor(
                HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(
                            Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
                        )
                    }
                }
            )
        )
    }

    private val bindingHolder = ViewBindingHolder<FragmentDailyPictureBinding>()
    private val binding get() = bindingHolder.binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingHolder.createView(viewLifecycleOwner) {
        FragmentDailyPictureBinding.inflate(inflater, container, false)
    }

    override fun showPicture(picture: PictureOfTheDay) {
        binding.textPictureDescription.text = picture.explanation
        binding.imageDailyPicture.load(picture.url) {
            crossfade(true)
        }
    }

    override fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
