# Moxy
[ ![Download](https://api.bintray.com/packages/moxy-io/maven/moxy/images/download.svg) ](https://bintray.com/moxy-io/maven/moxy/_latestVersion)

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
### Base modules integration: 
```groovy
implementation 'io.moxy:moxy:1.0'
```
#### Java project
```groovy
annotationProcessor 'io.moxy:moxy-compiler:1.0'
```
#### Kotlin
```groovy
apply plugin: 'kotlin-kapt'
```
```groovy
kapt 'io.moxy:moxy-compiler:1.0'
```
### Default android module integration
For additional base view classes `MvpActivity` and `MvpFragment` add this:
```groovy
implementation 'io.moxy:moxy-android:1.0'
```
### AppCompat module integration
If you use AppCompat, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'io.moxy:moxy-app-compat:1.0'
```
### AndroidX module integration
If you use AndroidX, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation 'io.moxy:moxy-androidx:1.0'
```
### AndroidX(Google material) module integration
If you use google material, use `MvpBottomSheetDialogFragment` add this:
```groovy
implementation 'io.moxy:moxy-material:1.0'
```

Temporary artifacts are in Bintray repository (until published to jcenter and maven).
```
allprojects {
    repositories {
        maven {
            url 'https://dl.bintray.com/moxy-io/maven'
        }
    }
```
## ProGuard
Moxy is completely without reflection! No special ProGuard rules required.

## Moxy Community
Brave people how created library

@senneco
@ekursakov
@jordan1997
@dependabot-bot
@xanderblinov
@VovaStelmashchuk
@SavinMike
@AlexeyKorshun
@dmdevgo
@rsajob
@terrakok
@mohaxspb
@CherryPerry
@fl1pflops
@phoenixxt
@Dosssik
@AcoustickSan
@seven332
@v-grishechko
@sbogolepov
@A-Zaiats
@lion4ik

You may also find them in [contributors page of original project](https://github.com/Arello-Mobile/Moxy/graphs/contributors)

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