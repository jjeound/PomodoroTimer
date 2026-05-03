# Claude Code Harness

Claude Code의 hooks, 권한, 자동화 동작 설정을 관리합니다.

## 설정 파일 위치

| 파일 | 범위 | 용도 |
|------|------|------|
| `.claude/settings.json` | 프로젝트 | 프로젝트 공유 설정 (git 추적) |
| `.claude/settings.local.json` | 로컬 전용 | 개인 설정 (git 제외) |

## Hooks

### Pre-tool-use

```json
{
  "hooks": {
    "PreToolUse": []
  }
}
```

> 예시: Edit 전 ktlint 포맷 검증

### Post-tool-use

```json
{
  "hooks": {
    "PostToolUse": []
  }
}
```

> 예시: Write/Edit 후 자동 포맷

### Stop

```json
{
  "hooks": {
    "Stop": []
  }
}
```

> 예시: 작업 완료 시 알림

## 권한 Allowlist

```json
{
  "permissions": {
    "allow": []
  }
}
```

> 자주 쓰는 read-only Bash 명령어를 등록해 프롬프트 줄이기

## 자동화 동작 목록

| 트리거 | 동작 | 상태 |
|--------|------|------|
| - | - | 미정 |
