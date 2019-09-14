# Moxy
[ ![Bintray](https://api.bintray.com/packages/moxy-community/maven/moxy/images/download.svg) ](https://bintray.com/moxy-community/maven/moxy/_latestVersion)
[ ![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy)

Moxy 2 is out! Check out the [migration guide](https://github.com/moxy-community/Moxy/wiki/Migration-Guide-(1.x.x-to-2.x.x)) and give it a try!

Moxy is a library that helps to use MVP pattern when you do the Android Application. Without problems of lifecycle and boilerplate code!

The main idea of using Moxy:
![schematic_using](https://habrastorage.org/files/a2e/b51/8b4/a2eb518b465a4df9b47e68794519270d.gif)

## Capabilities

Moxy has a few killer features in other ways:
- _Presenter_ stay alive when _Activity_ recreated(it simplifies work with multithreading)
- Automatically restore everything user sees when _Activity_ recreated(including cases, when dynamic content is added)
- Capability to change many _Views_ from one _Presenter_

## Sample

View interface
```kotlin
interface MainView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
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
implementation "com.github.moxy-community:moxy:$moxyVersion"
```
#### Java project
```groovy
annotationProcessor "com.github.moxy-community:moxy-compiler:$moxyVersion"
```
#### Kotlin
```groovy
apply plugin: 'kotlin-kapt'
```
```groovy
kapt "com.github.moxy-community:moxy-compiler:$moxyVersion"
```
### Default android module integration
For additional base view classes `MvpActivity` and `MvpFragment` add this:
```groovy
implementation "com.github.moxy-community:moxy-android:$moxyVersion"
```
### AppCompat module integration
If you use AppCompat, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation "com.github.moxy-community:moxy-app-compat:$moxyVersion"
```
### AndroidX module integration
If you use AndroidX, use `MvpAppCompatActivity` and `MvpAppCompatFragment` add this:
```groovy
implementation "com.github.moxy-community:moxy-androidx:$moxyVersion"
```
### AndroidX(Google material) module integration
If you use google material, use `MvpBottomSheetDialogFragment` add this:
```groovy
implementation "com.github.moxy-community:moxy-material:$moxyVersion"
```
### Kotlin extensions integration
If you use kotlin, you can declare presenters in your views using property delegate:
```kotlin
class MyFragment: MvpFragment() {
    ...
    private val presenter by moxyPresenter { presenterProvider.get() }
    ...
}
```
To use `MvpDelegateHolder.moxyPresenter`, add this:
```groovy
implementation "com.github.moxy-community:moxy-ktx:$moxyVersion"
```
## New Features and Compiler option for Migration from old version

By default, each `MvpView` method must have an annotation `@StateStrategyType`.
In the old version of Moxy, it was allowed to omit strategy for methods. In this case, the default strategy was applied.

You can fallback to the old behavior. To do this, set the disableEmptyStrategyCheck parameter to true.
```kotlin
disableEmptyStrategyCheck : ‘true’
```

In this case, the default strategy will be `AddToEndSingleStrategy`. In old version default strategy was `AddToEndStrategy`.

To change default strategy, provide `defaultMoxyStrategy` parameter with the full qualified class name of new default strategy.

```kotlin
defaultMoxyStrategy : 'moxy.viewstate.strategy.OneExecutionStateStrategy'
```

If compiler finds `MvpView` method without annotation `@StateStrategyType`, compilation will fail with clarifying error message. To ease migration from older versions there is additional mechanism you can optionally enable.
It collects all the errors associated with an empty strategy in one generated class called `EmptyStrategyHelper`. Using this class, you can easily navigate to all methods with a missing strategy.

To turn on generation of this class, enable this compiler option:
```kotlin
enableEmptyStrategyHelper : 'true'
```

To see, how to correctly use compilation flags, check out [sample-app build.gradle file](https://github.com/moxy-community/Moxy/blob/develop/sample-app/build.gradle)

## ProGuard\R8
If you using any of `moxy-android`, `moxy-appcompat`, `moxy-androidx` or `moxy-material` artifacts, no additional configuration required.
If you use only `moxy`, you need to manually include rules from [this file](https://github.com/moxy-community/Moxy/blob/develop/proguard-rules.pro).


## Road Map
* [✓] ~~Provide a migration tool from com.arello-mobile.moxy and its default strategy~~
* [✓] ~~Kotlin incremental compilation support~~
* [✓] ~~Remove reflectors~~
* [✓] ~~Add delivery module support~~
* [х] ~~Add separate Annotation Processor for migration~~
* [ ] Provide Runtime Implementation
* [ ] Research possibility of removing @InjectViewState annotation

## Moxy Community
Brave people, who created this library

[@senneco](https://github.com/senneco)
[@jordan1997](https://github.com/jordan1997)
[@xanderblinov](https://github.com/xanderblinov)
[@asitnkova](https://github.com/asitnkova)
[@alaershov](https://github.com/alaershov)
[@bejibx](https://github.com/bejibx)
[@ekursakov](https://github.com/ekursakov)
[@SavinMike](https://github.com/SavinMike)
[@sychyow](https://github.com/sychyow)
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

## Contributing
Install code style to your IntelliJ or Android Studio. `MoxyAndroid.xml`
For import file use or Mac OS:
Preferences -> Editor -> Code style -> Scheme -> Import Scheme -> IntelliJ IDEA code style XML -> choose the `MoxyAndroid.xml` file in finder   

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
