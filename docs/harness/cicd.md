# CI/CD Harness (Fastlane)

## 환경 설정

| 항목 | 위치 | 비고 |
|------|------|------|
| Google Play 서비스 계정 키 | `fastlane/pomodoro-timer-481808-...json` | git 제외 필요 |
| 키스토어 | `local.properties` 로 경로 지정 | git 제외 |
| 패키지명 | `com.jje.timer` | `Appfile` 에 설정됨 |

## Fastlane Lanes

```bash
bundle exec fastlane android test     # 유닛 테스트 실행
bundle exec fastlane android beta     # 릴리즈 빌드 후 베타 배포
bundle exec fastlane android deploy   # Google Play 프로덕션 배포
```

## Lane 구성 현황

### `test`
- `./gradlew test` 실행

### `beta`
- `./gradlew clean assembleRelease`
- Crashlytics 업로드 (현재 미설정)

### `deploy`
- `./gradlew clean assembleRelease`
- `upload_to_play_store` (서비스 계정 키 필요)

## 개선 예정

- [ ] `beta` lane: Firebase App Distribution 또는 내부 트랙으로 전환
- [ ] 버전 코드 자동 증가 (`increment_version_code`)
- [ ] 릴리즈 노트 자동화
- [ ] GitHub Actions 연동 (PR merge 시 자동 배포)

## GitHub Actions (예정)

```yaml
# .github/workflows/deploy.yml
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: ruby/setup-ruby@v1
      - run: bundle exec fastlane android deploy
```

## 시크릿 관리

로컬 개발 시 `local.properties`:
```
STORE_PASSWORD=...
KEY_ALIAS=...
KEY_PASSWORD=...
```

CI 환경에서는 GitHub Secrets / 환경 변수로 주입.
