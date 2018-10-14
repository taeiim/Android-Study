# Content Provider

> 발표자 : 송시은
>
> 발표일자 : 2018-10-??(?)
>
> 발표주제 : Content Provider



## 0. 참고문서

- [18장 콘텐트 프로바이더] 될 때까지 안드로이드 (오준석 지음)
- [콘텐트 프로바이더 이미지] http://bitsoul.tistory.com/155



## 1. 개요

> **콘텐트 프로바이더** 
>
> 안드로이드 4대 컴포넌트 중 하나로 앱 내부의 데이터베이스나 파일을 외부로 공개하는 역할

![](https://github.com/taeiim/Android-Study/blob/master/study/week16/Content-Provider/Content-Provider.jpg)

프로바이더를 통해 공개된 데이터를 이용하는 방법만 알고 있다면, 연락처, 일정, 미디어(오디오, 비디오, 음악) 등의 정보를 쉽게 가져올 수 있다!



* 참고로 안드로이드 N 7.0 부터는 'file://'로 시작하는 파일 객체를 외부로 공유해야 할 때, 보안 상의 이유로 FileProvider를 사용하도록 변경되었다.