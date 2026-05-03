# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# CLAUDE.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines, and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multistep tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.

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

| Screen                        | ViewModel             | Notes                                                  |
|-------------------------------|-----------------------|--------------------------------------------------------|
| `MainScreen`                  | `MainViewModel`       | Central hub: widget list, edit sheet, color management |
| `pomodoro/PomodoroTimer`      | `PomodoroViewModel`   | Core timer logic; handles portrait/landscape           |
| `onboarding/OnboardingScreen` | `OnboardingViewModel` | First-launch flow, gated by DataStore flag             |

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

## Development Process

### TDD (Test-Driven Development)

> **CRITICAL**: 새 기능 구현 시 반드시 테스트를 먼저 작성하고, 테스트가 통과하는 구현을 작성할 것.

1. **Red** — 실패하는 테스트 작성 (`./gradlew testDebugUnitTest` 로 실패 확인)
2. **Green** — 테스트를 통과하는 최소한의 구현 작성
3. **Refactor** — 중복 제거 및 코드 정리 (테스트는 계속 통과해야 함)

레이어별 테스트 위치 및 패턴은 `docs/harness/testing.md` 참고.

### Commit Message Convention (Conventional Commits)

```
<type>: <subject>
```

| type       | 사용 시점          |
|------------|----------------|
| `feat`     | 새 기능 추가        |
| `fix`      | 버그 수정          |
| `docs`     | 문서만 변경         |
| `refactor` | 기능 변경 없는 코드 개선 |
| `test`     | 테스트 추가/수정      |
| `chore`    | 빌드 설정, 의존성 등   |

예시:
- `feat: 목표 설정 기능 추가`
- `fix: 타이머 일시정지 후 재개 시 시간 오차 수정`
- `test: PomodoroViewModel 타이머 틱 테스트 추가`
