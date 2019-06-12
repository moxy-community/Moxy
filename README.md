# Moxy
[ ![Bintray](https://api.bintray.com/packages/moxy-community/maven/moxy/images/download.svg) ](https://bintray.com/moxy-community/maven/moxy/_latestVersion)
[ ![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy)

Moxy is a library that helps to use MVP pattern when you do the Android Application. Without problems of lifecycle and boilerplate code!

The main idea of using Moxy:
![schematic_using](https://habrastorage.org/files/a2e/b51/8b4/a2eb518b465a4df9b47e68794519270d.gif)

## Capabilities

Moxy has a few killer features in other ways:
- _Presenter_ stay alive when _Activity_ recreated(it simplify work with multithreading)
- Automatically restore all that user see when _Activity_ recreated(including dynamic content is added)
- Capability to changes of many _Views_ from one _Presenter_

## Sample

View interface
```kotlin
interface MainView : MvpView {
	fun printLog(msg: String)
}

class MainActivity : MvpAppCompatActivity(), MainView {
	
	@InjectPresenter
	internal lateinit var presenter: MainPresenter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
	
	override fun printLog(msg: String) {
		Log.e(TAG, "printLog : msg : $msg activity hash code : ${hashCode()}")
	}
	
	companion object {
		const val TAG = "MoxyDebug"
	}
}
```
Presenter
```kotlin
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
	override fun onFirstViewAttach() {
		super.onFirstViewAttach()
		Log.e(MainActivity.TAG, "presenter hash code : ${hashCode()}")
		viewState.printLog("TEST")
	}
}
```

## Inject with Dagger2
```kotlin
	@Inject
	lateinit var daggerPresenter: Lazy<MainPresenter>
	
	@InjectPresenter
	lateinit var presenter: MainPresenter
	
	@ProvidePresenter
	fun providePresenter(): MainPresenter = daggerPresenter.get()
```


## Android studio and Intellij templates 
**We will change this template in future**
In order to avoid boilerplate code creating for binding activity, fragments and its presentation part, we propose to use Android Studio templates for Moxy.

## Links
**Telegram channels from original moxy community**

[Telegram channel (en)](https://telegram.me/moxy_mvp_library)<br />
[Telegram channel (ru)](https://telegram.me/moxy_ru)<br />

## Integration
(Please replace moxyVersion with the latest version number:[ ![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy))

### Base modules integration:
```groovy
implementation 'com.github.moxy-community:moxy:moxyVersion'
```
#### Java project
```groovy
annotationProcessor 'com.github.moxy-community:moxy-compiler:moxyVersion'
```
#### Kotlin
```groovy
apply plugin: 'kotlin-kapt'
```
```groovy
kapt 'com.github.moxy-community:moxy-compiler:moxyVersion'
```
### Default android module integration
For additional base view classes `MvpActivity` and `MvpFragment` add this:
```groovy
implementation 'com.github.moxy-community:moxy-android:moxyVersion'
```
### AppCompat module integration
If you use AppCompat, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'com.github.moxy-community:moxy-app-compat:moxyVersion'
```
### AndroidX module integration
If you use AndroidX, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'com.github.moxy-community:moxy-androidx:moxyVersion'
```
### AndroidX(Google material) module integration
If you use google material, use `MvpBottomSheetDialogFragment` add this:
```groovy
implementation 'com.github.moxy-community:moxy-material:moxyVersion'
```
## New Features and Compiler option for Migration from old version

By default, each `MvpView` method must have an annotation `@StateStrategyType`.
In the old version of Moxy, it was allowed to miss strategy for methods. In this case, the default strategy was applied.

You can fallback to the old behavior. To do this, set the disableEmptyStrategyCheck parameter to true.
```kotlin
disableEmptyStrategyCheck : ‘true’
```

In this case, the default strategy will be `AddToEndSingleStrategy`. In old version default strategy was` AddToEndStrategy`.

To change default strategy, provide for `defaultMoxyStrategy` parameter the full class name of new default strategy.

```kotlin
defaultMoxyStrategy : 'moxy.viewstate.strategy.OneExecutionStateStrategy'
```

If compiler finds `MvpView` method without annotation `@StateStrategyType` it show this error with standard method for notifying about compilation problems.For ease of migration from older versions, we have provided an additional mechanism: `EmptyStrategyHelper`.
It collects all the errors associated with an empty strategy in one place. Using it, you can easily navigate from the `EmptyStrategyHelper` directly to the method with a missing strategy.

To switch the error output method, enable the option
```kotlin
enableEmptyStrategyHelper : 'true'
```

How to correctly use compilation flags see at [sample-app build.gradle file](https://github.com/moxy-community/Moxy/blob/develop/sample-app/build.gradle)

## ProGuard
Moxy is completely without reflection! No special ProGuard rules required.

## Road Map
* [✓] ~~Provide a migration tool from com.arello-mobile.moxy and its default strategy~~
* [ ]Kotlin incremental compilation support
* [ ]Remove reflectors and common presenter store
* [ ]Provide Runtime Implementation

## Moxy Community
Brave people how created library

[@senneco](https://github.com/senneco)
[@ekursakov](https://github.com/ekursakov)
[@jordan1997](https://github.com/jordan1997)
[@xanderblinov](https://github.com/xanderblinov)
[@SavinMike](https://github.com/SavinMike)
[@AlexeyKorshun](https://github.com/AlexeyKorshun)
[@dmdevgo](https://github.com/dmdevgo)
[@rsajob](https://github.com/rsajob)
[@terrakok](https://github.com/terrakok)
[@mohaxspb](https://github.com/mohaxspb)
[@CherryPerry](https://github.com/CherryPerry)
[@fl1pflops](https://github.com/fl1pflops)
[@phoenixxt](https://github.com/phoenixxt)
[@Dosssik](https://github.com/Dosssik)
[@AcoustickSan](https://github.com/AcoustickSan)
[@seven332](https://github.com/seven332)
[@v-grishechko](https://github.com/v-grishechko)
[@sbogolepov](https://github.com/sbogolepov)
[@A-Zaiats](https://github.com/A-Zaiats)
[@lion4ik](https://github.com/lion4ik)


You may also find them in [contributors page of old project](https://github.com/Arello-Mobile/Moxy/graphs/contributors)

## License
```
The MIT License (MIT)

Copyright (c) 2019 Moxy Community

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
