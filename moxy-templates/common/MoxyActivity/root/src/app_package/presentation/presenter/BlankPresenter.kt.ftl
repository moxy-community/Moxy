package ${packageName}.presentation.presenter${dotSubpackage}

import moxy.InjectViewState
import moxy.MvpPresenter
import ${packageName}.presentation.view${dotSubpackage}.${viewName}

@InjectViewState
class ${presenterName} : MvpPresenter<${viewName}>() {

}
