# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
./gradlew assembleDebug          # Debug APK
./gradlew assembleRelease        # Release APK (requires signing config in local.properties)
./gradlew test                   # Unit tests
./gradlew connectedAndroidTest   # Instrumented tests (requires connected device/emulator)
./gradlew lint                   # Lint checks
./gradlew clean                  # Clean build outputs
```

Release signing requires `local.properties` with:
```
STORE_PASSWORD=...
KEY_ALIAS=...
KEY_PASSWORD=...
```
and `upload-keystore.jks` at the path configured in `app/build.gradle.kts`.

## Architecture

**Clean Architecture + MVVM** with Jetpack Compose UI.

### Layers

```
presentation/   — Composables, ViewModels, UI state (StateFlow)
data/           — Repository interfaces + implementations, DataStore keys, domain models
database/       — Room entities, DAOs, type converters, entity↔domain mappers
di/             — Hilt modules (DatabaseModule, DataStoreModule, DataModule)
ui/theme/       — Material 3 colors, typography, background
```

### Data Flow

```
Composable → ViewModel (StateFlow) → Repository → Room DB / DataStore Preferences
```

ViewModels expose `StateFlow` consumed by composables via `collectAsStateWithLifecycle()`. Repositories return `Flow` from Room DAOs or DataStore.

### Core Domain Model

- **CustomWidget** — a timer preset combining mode, appearance (colors, font, background), and behavior (sound mode, Lottie animation). Stored in Room.
- **Mode** — `POMODORO` (analog clock), `DIGITAL`, `DESK` (table format)
- **SoundMode** — `NO_SOUND`, `VIBRATE`, `SOUND`
- **BgMode** — `IDLE`, `SNOW` (Lottie animation)
- **ColorEntity** — saved color palette entries in Room.

### Key Screens / ViewModels

| Screen | ViewModel | Notes |
|--------|-----------|-------|
| `MainScreen` | `MainViewModel` | Central hub: widget list, edit sheet, color management |
| `pomodoro/PomodoroTimer` | `PomodoroViewModel` | Core timer logic; handles portrait/landscape |
| `onboarding/OnboardingScreen` | `OnboardingViewModel` | First-launch flow, gated by DataStore flag |

Navigation is handled with Navigation Compose + Kotlin Serialization for type-safe nav arguments.

## Key Technology Choices

- **DI**: Hilt (KSP-generated)
- **UI**: Jetpack Compose + Material 3; Lottie for snow animation; Coil for image/SVG loading
- **Persistence**: Room (v2.8.2, DB version 5) + DataStore Preferences
- **Async**: Coroutines + Flow throughout
- **Navigation**: Navigation Compose with serialized route objects
- **Compiler flags**: `-Xcontext-receivers` enabled; Compose compiler reports written to `build/compose_compiler/`

## Room Database Notes

- `PomodoroDatabase` version **5**; explicit migration `4→5` adds a `pattern` column to `CustomWidgetEntity`.
- `fallbackToDestructiveMigration` is enabled — adding new migrations is required to preserve user data.
- Type converters handle `Color` (Int), `FontFamily`, and enum types (`Mode`, `SoundMode`, `BgMode`).
