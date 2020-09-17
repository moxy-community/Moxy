# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.2.0] - 2020-09-18
### Added
- Allow MvpView to extend arbitrary interfaces ([#94](https://github.com/moxy-community/Moxy/issues/94))
- Added sources jar for android modules ([#44](https://github.com/moxy-community/Moxy/issues/44))

### Changes
- BuildConfig classes no longer included in android modules ([#118](https://github.com/moxy-community/Moxy/pull/118))

### Fixes
- Do not generate ViewState for abstract presenter classes ([#101](https://github.com/moxy-community/Moxy/issues/101))
- Fix ViewStateProvider generation for nested presenters ([#108](https://github.com/moxy-community/Moxy/pull/108))
- Throw exception if generated ViewStateProvider was not found ([#107](https://github.com/moxy-community/Moxy/pull/107))


## [2.1.2] - 2020-03-09
- Fixed R8 obfuscating names of presenters without explicit `@InjectViewState` ([#87](https://github.com/moxy-community/Moxy/issues/87))
- Added proguard rules to `com.github.moxy-community:moxy` artifact


## [2.1.1] - 2020-01-31
- Fixed compiler crash when type variable used in presenter class ([#81](https://github.com/moxy-community/Moxy/issues/81))


## [2.1.0] - 2020-01-30
### Fixes
- Fixed compilation error if View interface method's parameter name clash with generated code ([#53](https://github.com/moxy-community/Moxy/issues/53))
- Fixed restoration of childDelegate delegateTag in fragments ([#80](https://github.com/moxy-community/Moxy/pull/80))

### Added
- Added `MvpPresenter.presenterScope` extension for kotlin coroutines ([#59](https://github.com/moxy-community/Moxy/issues/59))
- Added ability to define alias annotations for `@StateStrategyType` annotation, and added some default aliases to the library ([#72](https://github.com/moxy-community/Moxy/issues/72))
- Added `@Inherited` annotation to `@InjectViewState`. It means `@InjectViewState` is implicitly applied to all presenters, `ViewState` will be generated even without explicit annotation on `MvpPresenter` subclass ([#76](https://github.com/moxy-community/Moxy/pull/76))

### Behaviour change
- Changed strategy resolution algorithm. Child view interface strategies will not propagate to superinterface views ([#70](https://github.com/moxy-community/Moxy/issues/70))


## [2.0.2] - 2019-09-20
- Guava conflicts fixes ([#56](https://github.com/moxy-community/Moxy/issues/56))


## [2.0.1] - 2019-09-19
- No public changes since 2.0-beta01. Release to maven central


## [2.0.0] - 2019-09-19
- No public changes since 2.0-beta01


## [2.0-beta01] - 2019-09-19
- No public changes since 2.0-alpha06


## [2.0-alpha06] - 2019-09-07
### Added
- Support for gradle incremental annotation processing.
- `moxy-ktx` artifact, containing `moxyPresenter` property delegate for kotlin users.
- Layout id constructors for `MvpFragment` and `MvpAppCompatActivity`.
- `enableEmptyStrategyHelper`, `defaultMoxyStrategy` and `disableEmptyStrategyCheck` compiler options.

### Changed
- Make `moxy-android`, `moxy-andoridx`, `moxy-appcompat` and `moxy-material` artifacts to be .aar instead of .jar
to automatically provide consumer proguard files.
- All view methods are required to have `@StateStrategy` annotation.

### Removed
- `@RegisterMoxyReflectorPackages` annotation, MoxyReflector code generation, and `moxyReflectorPackage` compiler option.

### Fixed
- Broken code generation when view interface is inside other class.
- Inherited method being written twice inside generated ViewState if modifiers changed or parameters renamed.


## Differences between [Arello-Mobile Moxy](https://github.com/Arello-Mobile/Moxy) and [Moxy-community v1.0.14](https://github.com/moxy-community/Moxy/tree/1.0.14)
### Added
- `moxy-androidx` and `moxy-material` artifacts.

### Changed
- Package from `com.arellomobile.mvp` to `moxy`.

### Removed
- Presenter types `WEAK` and `GLOBAL`. All presenters are now `LOCAL`.