# Basic Android Sample

앱 초기 설정 및 기본 설계에 초점

## 컨벤션

### Code Style
일반적으로 회사 혹은 오픈소스들에는 사용하는 스타일 가이드가 존재

- Preference > Editor > Code Style > Kotlin > set from > Kotlin style guide
- Preference > Editor > Code Style > XML > Android

[Square Android CodeStyles](https://github.com/square/java-code-styles)

[Kotlin StyleGuide](https://developer.android.com/kotlin/style-guide)


### Android Lint

Android Lint는 앱 소스 파일과 lint.xml를 통해서 앱의 문제점들을 해결해 줍니다.

[린트 검사로 코드 개선](https://developer.android.com/studio/write/lint)


### Ktlint / Detekt
- Android Lint가 안드로이드의 관련된 디펜던시나 리소스 관련 부분들에 대해 정적분석을 해준다면 ktlint와 detekt는 kotlin 소스에 대한 정적분석을 실행합니다.
- ktlint와 detekt는 Kotlin 상에서 실수할 수 있는 부분을 고쳐주며 다양한 rule들을 통해 코드를 분석하여 html이나 xml과 같은 형식으로 받아서 볼 수 있습니다.

[ktlint](https://ktlint.github.io/)
1. app/build.gradle 설정 추가
```
configurations {
    ktlint
}

dependencies {
    ktlint "com.pinterest:ktlint:0.41.0"
    // additional 3rd party ruleset(s) can be specified here
    // just add them to the classpath (ktlint 'groupId:artifactId:version') and 
    // ktlint will pick them up
}

task ktlint(type: JavaExec, group: "verification") {
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.ktlint
    args "src/**/*.kt"
    // to generate report in checkstyle format prepend following args:
    // "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
    // see https://github.com/pinterest/ktlint#usage for more
}
check.dependsOn ktlint

task ktlintFormat(type: JavaExec, group: "formatting") {
    description = "Fix Kotlin code style deviations."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.ktlint
    args "-F", "src/**/*.kt"
}
```

```
KotlinDSL

val ktlint by configurations.creating

tasks.register<JavaExec>("verification") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("--android", "src/**/*.kt")
}

tasks.named("check") {
    dependsOn(ktlint)
}

/** 스타일 수정 후 자동으로 수정해 줌 */
tasks.register<JavaExec>("ktlintFormat"){
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("-F", "src/**/*.kt")
}
```

- configurations 이 dependencies 보다 위에 선언 되어 있어야 ktlint 적용됨


2. .editorconfig 로 Rule 세팅
- rules 추가

[Standard rules](https://github.com/pinterest/ktlint#standard-rules)


[detekt](https://detekt.github.io/detekt/)
- Kotlin 프로젝트를위한 코드 분석
- Gradle 빌드를 통한 코드 분석을위한 Gradle 플러그인

1. detekt 플러그인 추가

```
plugins {
    ...
    id "io.gitlab.arturbosch.detekt" version "1.16.0"
}
```

```
KotlinDSL

plugins {
    ...
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
}
```


2. detekt.yml 생성

```
./gradlew detektGenerateConfig
```


3. detekt 설정
- app/build.gradle 에 detekt 추가 하고 detekt.yml 실제 경로로 수정

```
detekt {
    toolVersion = "1.16.0"
    config = files("$rootDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
```


[detekt 설정 참고](https://medium.com/livefront/android-static-code-checks-keep-your-codebase-tidy-with-detekt-408435665fc3)


### Git Hooks
- 적용된 린트들을 깃에서 commit 혹은 push를 될 때마다 시켜주기 위해서 깃훅을 사용했습니다.

```
KotlinDSL

/** GitHooks 복사 */
tasks.register<Copy>("copyGitHooks"){
    from("${rootDir}/codeConfig/git/git-hooks/") {
        include("**/*")
        rename("(.*)", "$1")
    }

    into("${rootDir}/.git/hooks")
}

/** GitHooks 설치 */
tasks.register<Exec>("installGitHooks"){
    group = "git hooks"
    workingDir(rootDir)
    commandLine("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")
}
```

## 버저닝
-  GitHub 의 공동창업자인 톰 프레스턴 베르나가 만든 [Semantic Versioning](https://semver.org/)을 기반으로 버전 관리

### Semantic Versioning 2.0.0
Given a version number MAJOR.MINOR.PATCH, increment the:
1. MAJOR version when you make incompatible API changes,
2. MINOR version when you add functionality in a backwards compatible manner, and
3. PATCH version when you make backwards compatible bug fixes.

- Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

---

## 🛠 Tech Sacks & Open Source Libraries
- 비동기 처리 [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- Jetpack
    - Compose: 기본 UI 구축을 위한 Android의 최신 툴킷입니다.
    - Lifecycle: 수명 주기 변경 사항을 관찰합니다.
    - ViewModel: UI 관련 데이터 홀더 및 수명 주기 인식.
- Hilt: 의존성 주입
- Glide: 이미지 로딩 라이브러리
- Retrofit2 & OkHttp3: REST API 네트워크 데이터를 구성합니다.
- Timber: 유틸리티를 제공하는 작고 확장 가능한 API가 있는 로거.
- Firebse
    - Crashlytics: App Crash Report
    - Analytics: APP 분석
    - Authentication: 인증
---

## 🏛️ Architecture

### 아키텍처의 레이어 및 경계
- User Interface Layer: 
    - UI 레이어의 책임은 화면에 애플리케이션 데이터를 렌더링하는 것입니다.
    - 사용자 상호 작용 또는 네트워크 및 데이터베이스와의 외부 통신으로 인해 애플리케이션 데이터가 변경될 때마다 UI 요소를 업데이트해야 합니다.

- Presentation Layer:
    - 프레젠테이션 계층의 책임은 UI 계층과 도메인 계층 간의 데이터 변경 사항을 상호 작용하고 알리는 것입니다.
    - 구성 변경 시 데이터를 보유하고 복원합니다.

- Domain Layer:
    - 도메인 계층은 복잡한 비즈니스 로직을 추상화하고 재사용성을 향상시키는 역할을 합니다.
    - 이 계층은 복잡한 애플리케이션 데이터를 프레젠테이션 계층에 적합한 유형으로 변환하고 유사한 비즈니스 로직을 단일 기능으로 그룹화합니다.

- Data Layer:
    - 데이터 계층의 책임은 CRUD 작업(생성, 검색, 업데이트, 삭제 – 모든 시스템 이벤트)과 같은 비즈니스 로직 실행 결과를 전달하는 것입니다.
    - 이 계층은 Repository나 DataSource와 같은 다양한 전략으로 실행 책임을 나누어 설계할 수 있습니다.

---

## 브랜치 전략
### GitLab flow 사용
- master 브랜치는 production 브랜치
- production브랜치는 master 이상 권한만 push 가능
- developer 권한 사용자는 master 브랜치에서 신규 브랜치를 추가
- 신규 브랜치에서 소스를 commit 하고 push
- merge request를 생성하여 master 브랜치로 merge 요청
- merge_request를 생성하기 전에는 master 브랜치를 rebase 해서 병합 커밋을 방지
- master 권한 사용자는 developer 사용자와 함께 리뷰 진행 후 master 브랜치로 merge
- 테스트가 필요하다면 master에서 procution 브랜치로 merge하기 전에 pre-production 브랜치에서 테스트

---

## Custom Template Plugin for Android Studio
- MVVM 에서 기본적으로 사용하는 View, ViewModel, layout 을 기본으로 생성해주는 Custom Template 추가
- Plugin Library Path: ./libs/android-mvvm-plugin-template-0.0.1.jar

### 플러그인 설치 방법 (Mac 기준)
- Android Studio > Preferences.. > Plugins > 오른쪽 상단 톱니바퀴 모양 클릭 > Install Plugin from Disk > jar 파일 선택 > 스튜디오 재시작

### 사용법
- 생성할 폴더에서 우클릭 > New > Other > Android Fragment MVVM Creator > 정보 입력

- EntityName 규칙
    - Mvvm Activity creator
        - EntityName: FeatureHome 결과
            - view/FeatureHomeActivity
            - viewmodel/FeatureHomeViewModel
            - layout/activity_feature_home
    - Mvvm Fragment creator
        - EntityName: FeatureHome 결과
            - view/FeatureHomeFragment
            - viewmodel/FeatureHomeViewModel
            - layout/fragment_feature_home

---

## Firebase Google Authentication

### Sign-in-method 구글 설정
- [디지털 지문 추가](https://developers.google.com/android/guides/client-auth?authuser=0&hl=ko)
    - 프로젝트 설정에서 SHA-1 인증서를 구성

### 참고
[Firebase Google Login 연동하기](https://jamie-dev.tistory.com/128)
[Firebase Android Codelab - 친근한 채팅 구축](https://firebase.google.com/codelabs/firebase-android?authuser=0&hl=ko#5)
[alexmamo/FirebaseSignInWithGoogle](https://github.com/alexmamo/FirebaseSignInWithGoogle)
[저장된 자격 증명으로 사용자 로그인](https://developers.google.com/identity/one-tap/android/get-saved-credentials)
    - 비밀번호 대용 Smart Lock을 Android 앱에 통합하면 사용자가 저장한 사용자 인증 정보를 사용하여 앱에 자동으로 로그인

---

## 참고
- [혼자서 Android App 개발하기](https://woowabros.github.io/experience/2020/12/31/developing-an-android-app-in-one-person.html)
- [Architecture Components](https://getstream.io/blog/android-developer-roadmap-part-3/#architecture-components)
- [Introduction to GitLab Flow ALL TIERS](https://docs.gitlab.com/ee/topics/gitlab_flow.html)
- [Git branch 전략(Git-Flow, Github-Flow, Gitlab-Flow)](https://velog.io/@kw2577/Git-branch-%EC%A0%84%EB%9E%B5)
- [intellij-platform-plugin-template](https://github.com/JetBrains/intellij-platform-plugin-template)
- [Custom Template Plugin for Android Studio](https://velog.io/@devoks/Custom-Template-Plugin-for-Android-Studio?fbclid=IwAR29yO98ZmX1EeHkTwBPHAT8kq68FiXNLsHKJ8Ayffs0eUpkXWd5IDpCg_Q&mibextid=epqs5l)