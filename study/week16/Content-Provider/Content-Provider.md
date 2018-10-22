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

### 1.1. 정의

> **콘텐트 프로바이더** 
>
> 안드로이드 4대 컴포넌트 중 하나로 앱 내부의 데이터베이스나 파일을 외부로 공개하는 역할

![](https://github.com/taeiim/Android-Study/blob/master/study/week16/Content-Provider/Content-Provider.jpg)

프로바이더를 통해 공개된 데이터를 이용하는 방법만 알고 있다면, 연락처, 일정, 미디어(오디오, 비디오, 음악) 등의 정보를 쉽게 가져올 수 있다!



* 참고로 안드로이드 N 7.0 부터는 'file://'로 시작하는 파일 객체를 외부로 공유해야 할 때, 보안 상의 이유로 FileProvider를 사용하도록 변경되었다.



### 1.2. 사용 방법

프로바이더를 사용하려면 Context에서 제공하는 getContentResolver() 메서드로 ContentResolver 객체를 얻어야 한다. 이 객체가 제공하는 insert(), update(), query(), delete() 메서드를 이용하면 프로바이더가 제공하는 데이터를 조작할 수 있다~!



[ContentResolver를 통해 데이터를 얻는 예 ]

```java
Cursor curcor = getContentResolver().query(MemoProvider.CONTENT_URI,
                                          null,
                                          null,
                                          null,
                                          MemoContract.MemoEntry._ID + " DESC");
```

프로바이더를 이용하여 원하는 데이터를 얻으려면 각 정보를 가리키는 대표 URI를 알아야 하는데, 프로바이더마다 고유한 URI를 가지고 있어서 이것만 알면 원하는 정보에 접근할 수 있다.



### 1.3. 콘텐트 프로바이더 URI 목록

안드로이드에서 제공하는 대표 URI는 다음과 같다.

URI마다 특별한 권한이 필요한 경우 해당 권한을 매니페스트 파일에 추가해야 한다.

 **[권한 불필요]**

* 내부 사진: MediaStore.Images.Media.INTERNAL_CONTENT_URI
* 내부 동영상: MediaStore.Video.Media.INTERNAL_CONTENT_URI
* 내부 동영상 섬네일: MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI
* 내부 음악: MediaStore.Audio.Media.INTERNAL_CONTENT_URI
* 내부 음악앨범: MediaStore.Audio.Albums.INTERNAL_CONTENT_URI



**[android.permission.READ_EXTERNAL_STORAGE 권한 필요]**

- 외부 사진: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
- 외부 동영상: MediaStore.Video.Media.EXTERNAL_CONTENT_URI
- 외부 동영상 섬네일: MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
- 외부 음악: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
- 외부 음악앨범: MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI



**[android.permission.READ_CONTACTS 권한 필요]**

- 통화 이력: CallLog.Calls.CONTENT_URI
- 연락처: ContactsContract.Contract.CONTENT_URI
- 연락처의 주소: ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI
- 연락처의 메일: ContactsContract.CommonDataKinds.Email.CONTENT_URI
- 연락처의 그룹: ContactsContract.Groups.CONTENT_URI
- 단말 설정: Settings.System.CONTENT_URI
- 보안 설정: Settings.Secure.CONTENT_URI



**[android.permission.READ_USER_DICTIONARY 권한 필요]**

- 사용자 정보: UserDictionary.Words.CONTENT_URI



**[com.android.browser.permission.READ_HISTORY_BOOKMARKS 권한 또는 com.android.browser.permission.WRITE_HISTORY_BOOKMARKS 권한 필요]**

- 북마크: Browser.BOOKMARKS_URI
- 검색 문자열: Browser.SEARCHES_URI



대표 URI를 통해서 얻은 데이터는 Cursor라는 객체로 반환되며, Cursor는 곧 데이터의 집합이다.

이러한 Cursor를 어댑터뷰에 표시할 때는 CursorAdapter가 편리하다.

