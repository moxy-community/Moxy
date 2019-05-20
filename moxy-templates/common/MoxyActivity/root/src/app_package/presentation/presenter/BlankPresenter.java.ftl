package ${packageName}.presentation.presenter${dotSubpackage};

<#if applicationPackage??>import ${applicationPackage}.R;</#if>
import ${packageName}.presentation.view${dotSubpackage}.${viewName};
import io.moxy.InjectViewState;
import io.moxy.MvpPresenter;

@InjectViewState
public class ${presenterName} extends MvpPresenter<${viewName}>  {

}
