# Moxy
[ ![Bintray](https://api.bintray.com/packages/moxy-community/maven/moxy/images/download.svg) ](https://bintray.com/moxy-community/maven/moxy/_latestVersion)
[ ![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.moxy-community/moxy)

Moxy 2 is out! Check out the [migration guide](https://github.com/moxy-community/Moxy/wiki/Migration-Guide-(1.x.x-to-2.x.x)) and give it a try!

Moxy is a library that allows for hassle-free implementation of the MVP pattern in an Android Application. Without troubles of lifecycle and boilerplate code!

Illustration of how Moxy is working:
![schematic_using](https://habrastorage.org/files/a2e/b51/8b4/a2eb518b465a4df9b47e68794519270d.gif)

## Capabilities

Moxy has a few killer features:
- _Presenter_ stays alive when _Activity_ is being recreated (it simplifies multithread handling)
- Automatical restoration of all content in the recreated _Activity_ (including additional dynamic content)

## Example

View interface
```kotlin
interface MainView : MvpView {

    @AddToEndSingle
    fun displayUser(user: User)
}

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    override fun displayUser(user: User) {
        userLayout.showUser(user)
    }
}
```
Presenter
```kotlin
class MainPresenter : MvpPresenter<MainView>() {
    override fun onFirstViewAttach() {
        viewState.showUser(getCurrentUser())
    }
}
```

## Inject with Dagger2

### Kotlin

```kotlin
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

private val presenter by moxyPresenter { presenterProvider.get() }
```

### Java

```java
@InjectPresenter
MainPresenter presenter;

@Inject
Provider<MainPresenter> presenterProvider;

@ProvidePresenter
MainPresenter providePresenter() {
    return presenterProvider.get();
}
```

## Android studio and Intellij templates
**We will change this template in future**
In order to avoid tedious task of writing boilerplate code for binding activities, fragments and its presentation parts, we recommend use of Android Studio templates for Moxy.

## Links
**Telegram channels from original moxy community**

[Telegram channel (en)](https://telegram.me/moxy_mvp_library)<br />
[Telegram channel (ru)](https://telegram.me/moxy_ru)<br />

## Integration
Please replace `moxyVersion` with the latest version number: [![Bintray](https://api.bintray.com/packages/moxy-community/maven/moxy/images/download.svg)](https://bintray.com/moxy-community/maven/moxy/_latestVersion)

### Base library:
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
### Default android module
For additional base view classes `MvpActivity` and `MvpFragment` add this:
```groovy
implementation "com.github.moxy-community:moxy-android:$moxyVersion"
```
### AppCompat module
If you are using AppCompat, you'll need `MvpAppCompatActivity` and `MvpAppCompatFragment` classes. Add this:
```groovy
implementation "com.github.moxy-community:moxy-app-compat:$moxyVersion"
```
### AndroidX module
If you're using AndroidX, you'll need a different implementation for `MvpAppCompatActivity` and `MvpAppCompatFragment` classes. Use this one:
```groovy
implementation "com.github.moxy-community:moxy-androidx:$moxyVersion"
```
### AndroidX (Google material) module
If you're using Google material, use `MvpBottomSheetDialogFragment` and add this:
```groovy
implementation "com.github.moxy-community:moxy-material:$moxyVersion"
```
### Kotlin extensions
Declare presenters in your views using property delegate:
```kotlin
class MyFragment: MvpFragment() {
    ...
    private val presenter by moxyPresenter { presenterProvider.get() }
    ...
}
```
Launch coroutines in presenter scope:
```kotlin
class MyPresenter : MvpPresenter<MvpView>() {
    override fun onFirstViewAttach() {
        presenterScope.launch {
            // Coroutine that will be canceled when presenter is destroyed
        }
    }
}
```
To use `MvpDelegateHolder.moxyPresenter` and `MvpPresenter.presenterScope`, add this:
```groovy
implementation "com.github.moxy-community:moxy-ktx:$moxyVersion"
```
## New Features and Compiler option for Migration from old version

By default, each `MvpView` method must have an annotation `@StateStrategyType`.
In the version 1 of Moxy it was allowed to omit stategies for methods. In this case a default strategy was applied.

You can fallback to the old behavior. To do this, set the `disableEmptyStrategyCheck` parameter to true.
```kotlin
disableEmptyStrategyCheck : 'true'
```

In this case the default strategy will be `AddToEndSingleStrategy`. In the old version the default strategy was `AddToEndStrategy`.

To change default strategy provide for the `defaultMoxyStrategy` parameter a full class name of the new default strategy.

```kotlin
defaultMoxyStrategy : 'moxy.viewstate.strategy.OneExecutionStateStrategy'
```

If the compiler finds `MvpView` method without the `@StateStrategyType` annotation, it'd show an error via standard method for notifying about compilation problems. For ease of migration from older versions we have provided an additional mechanism: `EmptyStrategyHelper`.
It collects all the errors associated with an empty strategy in one place. Using it, you can easily navigate from the `EmptyStrategyHelper` directly to the method with a missing strategy.

To switch the error output method enable this option
```kotlin
enableEmptyStrategyHelper : 'true'
```

How to correctly use compilation flags check out the [sample-app build.gradle file](https://github.com/moxy-community/Moxy/blob/develop/sample-app/build.gradle)

## ProGuard\R8
`moxy-android`, `moxy-appcompat`, `moxy-androidx` and `moxy-material` artifacts already include ProGuard rule files, no additional configuration required.
While using standalone `moxy` you have to manually add rules from [this file](https://github.com/moxy-community/Moxy/blob/develop/proguard-rules.pro).


## Road Map
* [✓] ~~Provide a migration tool from com.arello-mobile.moxy and its default strategy~~
* [✓] ~~Kotlin incremental compilation support~~
* [✓] ~~Remove reflectors and common presenter store~~
* [✓] ~~Add delivery module support~~
* [х] ~~Add separate Annotation Processor for migration~~
* [✓] ~~Research possibility of removing @InjectViewState annotation~~
* [ ] Provide Runtime Implementation

## Moxy Community
Brave people who created the library:

[@senneco](https://github.com/senneco)  
[@jordan1997](https://github.com/jordan1997)  
[@xanderblinov](https://github.com/xanderblinov)  
[@asitnkova](https://github.com/asitnkova)  
[@alaershov](https://github.com/alaershov)  
[@bejibx](https://github.com/bejibx)  
[@katkoff](https://github.com/katkoff)  
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
[@NotPerfectBlue](https://github.com/NotPerfectBlue)  
[@IlyaGulya](https://github.com/IlyaGulya)  


You may also find them in [contributors page of the old project](https://github.com/Arello-Mobile/Moxy/graphs/contributors)

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
