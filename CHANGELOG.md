# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixes
- Compilation error if View interface method's parameter name clash with generated code

### Added
- `MvpPresenter.presenterScope` extension for kotlin coroutines

### Behaviour change
- Added ability to define alias annotations for `@StateStrategyType` annotation. More info [in this issue](https://github.com/moxy-community/Moxy/issues/72)


## [2.0.2] - 2019-09-20
- Guava conflicts fixes

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