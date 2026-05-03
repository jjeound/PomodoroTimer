# Test Harness

## 테스트 실행 명령어

```bash
# 유닛 테스트 (JVM)
./gradlew testDebugUnitTest

# 특정 테스트 클래스
./gradlew testDebugUnitTest --tests "com.pomodoro.timer.PomodoroViewModelTest"

# 계측 테스트 (기기/에뮬레이터 필요)
./gradlew connectedAndroidTest
```

## 테스트 구조

```
app/src/
├── test/java/com/pomodoro/timer/
│   ├── PomodoroViewModelTest.kt      # 타이머 상태 머신
│   ├── MainViewModelTest.kt          # 위젯/색상 CRUD
│   ├── ColorTypeConverterTest.kt     # Color ↔ String round-trip
│   ├── CustomWidgetMapperTest.kt     # 도메인 ↔ Entity 매핑
│   └── FakeMainRepository.kt        # 유닛 테스트용 Fake
│
└── androidTest/java/com/pomodoro/timer/
    ├── HiltTestRunner.kt             # Hilt 계측 테스트 러너
    └── CustomWidgetDaoTest.kt        # Room in-memory DAO 검증
```

## 레이어별 테스트 전략

### ViewModel (유닛 테스트)
- `Dispatchers.setMain(StandardTestDispatcher())` 으로 `viewModelScope` 제어
- `advanceUntilIdle()` / `advanceTimeBy()` 로 코루틴 실행 진행
- `PomodoroViewModel` — 의존성 없음, 직접 인스턴스화
- `MainViewModel` — `FakeMainRepository` 주입

### Flow 테스트
- `app.cash.turbine:turbine` 사용 (`eventFlow.test { }`)

### Room DAO (계측 테스트)
- `Room.inMemoryDatabaseBuilder` + `addTypeConverter()` 로 실제 SQLite 검증
- `@ProvidedTypeConverter` 라 각 converter를 수동으로 제공해야 함

### Hilt 계측 테스트
- `testInstrumentationRunner = "com.pomodoro.timer.HiltTestRunner"`
- `@HiltAndroidTest` + `HiltAndroidRule` 사용

## 의존성

```toml
# gradle/libs.versions.toml
coroutinesTest = "1.9.0"
turbine = "1.2.0"
```

```kotlin
// app/build.gradle.kts
testImplementation(libs.kotlinx.coroutines.test)
testImplementation(libs.turbine)
androidTestImplementation(libs.hilt.android.testing)
androidTestImplementation(libs.androidx.room.testing)
kspAndroidTest(libs.hilt.compiler)
```

## 현재 커버리지 현황

| 레이어 | 테스트 파일 | 케이스 수 |
|--------|-------------|-----------|
| PomodoroViewModel | PomodoroViewModelTest | 8 |
| MainViewModel | MainViewModelTest | 7 |
| ColorTypeConverter | ColorTypeConverterTest | 5 |
| CustomWidget Mapper | CustomWidgetMapperTest | 6 |
| CustomWidgetDao (Room) | CustomWidgetDaoTest | 5 |
| ColorDao | 미작성 | - |
| OnboardingViewModel | 미작성 | - |
