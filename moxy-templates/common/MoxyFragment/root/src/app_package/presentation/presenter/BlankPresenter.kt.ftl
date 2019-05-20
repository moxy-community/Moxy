package ${packageName}.presentation.presenter${dotSubpackage}

import io.moxy.InjectViewState
import io.moxy.MvpPresenter
import ${packageName}.presentation.view${dotSubpackage}.${viewName}

@InjectViewState
class ${presenterName} : MvpPresenter<${viewName}>() {

}
