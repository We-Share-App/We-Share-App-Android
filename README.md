# 🐦 Kachi Android

**가치 있는 소비와 나눔을 함께 실천하는 지역 기반 커뮤니티 플랫폼, 까치(Kachi)의 안드로이드 앱입니다.**

## 🧭 프로젝트 소개

까치는 공동구매와 물품 교환을 통해 지역 사회 내 절약과 순환 소비를 실현하는 커뮤니티 플랫폼입니다.  
본 레포지토리는 안드로이드 클라이언트를 개발하는 저장소입니다.

---

## 🛠 기술 스택

- Kotlin
- Jetpack Compose
- Android SDK 35
- Gradle Kotlin DSL
- MVVM (계층화 기반 폴더 구조 적용 예정)

---

## 📁 폴더 구조 (예정)
app/
└── src/
└── main/
└── java/site/weshare/android/
├── data/
├── di/
├── domain/
├── presentation/
└── ui/theme/

---

## 🧃 브랜치 전략

- `main`: 배포용 브랜치 (리드미, 깃허브 설정 등만 유지)
- `develop`: 실제 개발이 이루어지는 브랜치
- `feature/*`: 각 기능 개발을 위한 브랜치

> 모든 기능은 `feature/*` → `develop` → `main` 순으로 머지합니다.

---

## 📝 커밋 컨벤션

| 태그 | 설명 |
|------|------|
| `feat` | 새로운 기능 |
| `fix` | 버그 수정 |
| `docs` | 문서 추가/수정 |
| `style` | 코드 스타일 수정 |
| `refactor` | 리팩토링 |
| `chore` | 빌드 설정, 패키지 매니저 등 |

---

## 📌 초기 세팅 항목

- `.gitignore` 구성 완료
- 브랜치 전략 및 PR/Issue 템플릿 설정 완료
- Jetpack Compose 기반 템플릿 적용
- 기본 구조화 완료 (계층 폴더 포함)

---

## 🤝 기여 방식

1. `develop` 브랜치에서 `feature/기능명` 브랜치 생성
2. 기능 개발 후 Pull Request 생성
3. 코드 리뷰 및 승인 → develop에 병합
4. 일정 주기 또는 마일스톤 단위로 `main`에 병합

---

## 🐛 이슈 관리

- GitHub Issue 탭에서 기능 단위로 이슈 작성
- 이슈 템플릿 활용 권장
- PR은 반드시 관련 이슈를 연결해주세요
