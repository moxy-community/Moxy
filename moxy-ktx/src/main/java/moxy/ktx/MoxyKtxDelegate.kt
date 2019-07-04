package moxy.ktx

import moxy.MvpDelegate
import moxy.MvpFacade
import moxy.MvpPresenter
import moxy.presenter.PresenterField
import kotlin.reflect.KProperty

class MoxyKtxDelegate<T : MvpPresenter<*>>(
    private val delegate: MvpDelegate<*>,
    private val factory: () -> T
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val field = object : PresenterField<Any?>(property.name, null, null) {
            override fun providePresenter(delegated: Any?): MvpPresenter<*> = factory()
            override fun bind(container: Any?, presenter: MvpPresenter<*>) = Unit
        }

        val presenter = MvpFacade.getInstance().mvpProcessor.injectPresenter(thisRef, field, delegate.delegateTag)

        delegate.addPresenter(presenter)

        return presenter as T
    }
}
