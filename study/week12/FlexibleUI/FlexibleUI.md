# flexible UI

> 발표자 : 송시은
>
> 발표주제 : 레이아웃 분기
>
> 발표일자 : 2018-09-07



[TOC]



## 0. 참고문서

- 될 때까지 안드로이드(오준석 지음)
- https://developer.android.com/training/basics/fragments/fragment-ui?hl=ko
- https://developer.android.com/training/basics/fragments/communicating?hl=ko



## 1. 리소스 분기

안드로이드 프로젝트에서 아이콘이 있는 mipmap 디렉터리는 기기의 밀도(density)에 따라 분기하도록 되어있다. 앱이 실행될 때 기기의 밀도에 따라 해당 폴더의 아이콘이 선택되는 것이다.

![](https://github.com/taeiim/Android-Study/blob/master/study/week12/FlexibleUI/mipmap.PNG)



레이아웃도 이런식으로 분기하도록 처리할 수 있다.



## 2. 레이아웃 분기

### 2.1. 실습

1. 레이아웃 분기를 위해 res 디렉터리에서 마우스 오른쪽을 누르고 [New -> Android resource directory]를 클릭하여 리소스 디렉터리를 추가한다
2. New Resource Directory 창에서 Resource type:은 'layout'을 선택하고, 왼쪽 하단에 Available qualifiers 상자에서 'Orientation'을 선택한 다음, 가운데에 있는 >> 버튼을 누른다.
3. Screen orientation: 항목에서 'Landscape'를 선택한다. 그러면 Directory name:이 layout-land로 설정되며, 이 디렉터리에 있는 레이아웃 XML은 가로 모드에 우선으로 적용되는 레이아웃이 된다.
4. layout/activity_main.xml 파일을 layout-land 디렉터리에 복사해서 붙여넣는다.
5. 가로 모드와 세로 모드의 레이아웃을 설정한다.

레이아웃 분기를 프래그 먼트와 조합하면 다양한 화면 구성을 보다 쉽게 구성할 수 있다.



## 3. 유연한 사용자 인터페이스 구축 

프래그먼트를 사용하면 다양한 화면 크기를 지원하는 앱을 만들 때, 화면 크기나 방향에 따라 프래그먼트를 재활용하여 레이아웃을 구성할 수 있다. 이는 태플릿과 핸드폰을 동시에 지원하는 앱을 개발할 때 유용하다.

예시)

![](https://github.com/taeiim/Android-Study/blob/master/study/week12/FlexibleUI/fragments-screen-mock.png)



**FragmentManager 클래스** 런타임에 동적으로 프래그먼트를 추가나 삭제, 또는 교체할 수 있는 메서드를 제공



### 3.1. 실습

작성할 예제의 동작을 그림으로 표현하면 다음과 같다.

![](https://github.com/taeiim/Android-Study/blob/master/study/week12/FlexibleUI/example_process_pic.PNG)

1. 액티비티가 시작하면 HeadlinesFragment를 동적 추가한다.
2. HeadlinesFragment에서 아이템이 선택되면 onHeadlineSelected 콜백이 호출된다.
3. 액티비티에서 ArticleFragment로 프래그먼트를 교체한다.
4. 프래그먼트가 교체되기 전에 백스택에 저장함으로써 뒤로 가기로 HeadlinesFragment로 돌아갈 수 있다.

------

제목을 누르면 내용을 표시하는 간단한 앱의 샘플을 작성할 것이다.

프래그먼트 A에 제목 리스트가 표시되게 하고, 제목을 클릭하면 프래그먼트 B에 내용이 보이게 할 것이다.

이를 위해 Article 클래스를 새로 만들고 다음과 같은 코드를 작성한다.

```java
public class Articles {
    // 제목
    static String[] Headlines = {
            "제목 1", "제목 2"
    };
    // 내용
    static String[] Articles = {
            "이것은 제목1의 내용입니다.",
            "이것은 제목2의 내용입니다."
    };
}
```



#### 3.1.1. 화면이 작은 기기에서의 동작

화면이 작은 기기와 큰 기기(7인치 이상 태블릿)에서 레이아웃과 앱의 동작이 다르게 하려고 한다.

핸드폰처럼 화면 크기가 7인치 미만인 기기에서는 프래그먼트 A의 리스트를 클릭하면 프래그먼트 B의 내용이 표시되게 하고, 태블릿처럼 큰 기기에서는 한 화면에 프래그먼트 A와 B가 결합되어 리스트와 내용이 모두 표시되게 하려고 한다. 



1. 프래그먼트 A에 해당하는 HeadlinesFragment를 작성한다 - 제목을 표시하는 리스트를 표시

   ```java
   /**
    * 제목을 표시할 리스트 프래그먼트
    */
   public class HeadlinesFragment extends ListFragment {
       // 이 프래그먼트를 포함하는 액티비티는 반드시 이 인터페이스를 구현해야 함
       interface OnHeadlineSelectedListener {
           // 제목이 선택되었을 때 호출됨
           void onHeadlineSelected(int position);
       }
       private OnHeadlineSelectedListener mListener;
       @Override
       public void onCreate(@Nullable Bundle savedInstanceState){
           super.onCreate(savedInstanceState);
           // Articles 의 Headlines 배열을 사용하여 리스트 뷰를 위한 ArrayAdapter 를 생성
           setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Articles.Headlines));
       }
       @Override
       public void onAttach(Context context) {
           super.onAttach(context);
           // 이 프래그먼트를 포함하는 Context 는 반드시 OnHeadlineSelectedListener 를 구현해야 한다.
           // 그렇지 않으면 ClassCastException 이 발생하고 앱을 종료한다.
           try {
               mListener = (OnHeadlineSelectedListener) context;
           }catch (ClassCastException e){
               throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
           }
       }
       @Override
       public void onListItemClick(ListView l, View v, int position, long id){
           super.onListItemClick(l, v, position, id);
           // 선택된 위치를 액티비티에 알려 줌
           if (mListener != null) {
               mListener.onHeadlineSelected(position);
           }
       }
   }
   ```

   이 프래그먼트는 ListFragment를 상속하며 제목의 리스트를 표시하고 제목을 클릭했을 때 이 프래그먼트를 포함하고 있는 액티비티에 콜백을 호출하도록 되어있다. 이 프래그먼트를 포함할 액티비티는 반드시 OnHeadlineSelectedListener 인터페이스를 구현하도록 onAttach() 메서드에서 강제로 액티비티와 연결하고 있다.



2. 프래그먼트의 레이아웃 파일을 준비 `fragment_article`

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   android:orientation="vertical">
   
   <TextView
       android:id="@+id/article_text"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content" />
   </LinearLayout>
   ```

   

3. 프래그먼트 B에 해당하는 ArticleFragment 프래그먼트를 작성한다 - 내용을 텍스트뷰에 표시

   ```java
   /**
    * HeadlinesFragment 를 클릭했을 때 제목를 표시할 프래그먼트
    */
   
   public class ArticleFragment extends Fragment {
       public static final String ARG_POSITION = "position";
       private int mCurrentPosition = -1;
   
       @Nullable
       @Override
       public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           // 화면이 회전되면 이전에 선택된 위치를 복원
           if (savedInstanceState != null) {
               mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
           }
           // 화면 레이아웃은 TextView 하나만 있는 레이아웃을 사용
           return inflater.inflate(R.layout.fragment_articles, container, false);
       }
   
       @Override
       public void onActivityCreated(@Nullable Bundle savedInstanceState) {
           super.onActivityCreated(savedInstanceState);
           Bundle args = getArguments();
           if (args != null) {
               // 프래그먼트가 생성되었을 경우
               updateArticleView(args.getInt(ARG_POSITION));
           } else if (mCurrentPosition != -1) {
               // 화면 회전 등의 경우
               updateArticleView(mCurrentPosition);
           }
       }
   
       // 선택된 제목의 내용을 표시
       public void updateArticleView(int position) {
           TextView article = (TextView) getView().findViewById(R.id.article_text);
           article.setText(Articles.Articles[position]);
           mCurrentPosition = position;
       }
   
       @Override
       public void onSaveInstanceState(Bundle outState) {
           super.onSaveInstanceState(outState);
           // 화면이 회전될 때, 선택된 위치를 저장
           outState.putInt(ARG_POSITION, mCurrentPosition);
       }
   }
   ```

   이 프래그먼트는 몇 번째 제목이 선택되었는지에 따라서 해당 제목의 내용을 Articles.Articles 배열에서 가져와서 텍스트뷰에 표시한다. 

   프래그먼트는 액티비티 생명주기와 연관된 복잡한 사정으로 생성자를 통한 파라미터 전달이 금지되어 있다고 한다. 파라미터를 가지는 생성자를 만들면 안드로이드 스튜디오가 빨간 줄로 표시할 것이다. 대신 Bundle 객체를 Argument로 전달할 수 있다. 프래그먼트를 처음 생성했을 때는 Argument로 제목 번호를 전달받아서 제목을 표시하고, 화면이 회전하면 마지막에 선택된 기사 번호를 onSavedInstanceState() 메서드에서 저장하고 onCreateView() 메서드에서 복원한다.

   

4. 두 프래그먼트를 포함할 액티비티인 MainActivity인 레이아웃 파일을 다음과 같이 작성한다.

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   tools:context=".MainActivity">
   
   <FrameLayout
       android:id="@+id/fragment_container"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />
   </RelativeLayout>
   ```

   레이아웃의 내부에는 프래그먼트를 표시할 영역인 FrameLayout을 배치하였고 id를 부여했다.

   이 영역에 최초에는 프래그먼트 A를 표시하고, 제목을 선택하면 프래그먼트 B로 교체할 것이다.

   

5. MainActivity 작성

   ```java
   public class MainActivity extends AppCompatActivity implements HeadlinesFragment.OnHeadlineSelectedListener {
   
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
   
           // 화면 회전 시 HeadlinesFragment 가 재생성되는 것 방지
           if (savedInstanceState == null) {
               HeadlinesFragment headlinesFragment = new HeadlinesFragment();
               // headlinesFragment 를 R.id.fragment_container 영역에 추가
               getSupportFragmentManager().beginTransaction()
                   .add(R.id.fragment_container, headlinesFragment)
                   .commit();
           }
       }
   
       // HeadlinesFragment 의 제목이 선택되었을 때 호출
       @Override
       public void onHeadlineSelected(int position) {
           // ArticleFragment 프래그먼트 생성
           ArticleFragment newArticleFragment = new ArticleFragment();
           // Argument 로 제목 번호 전달
           Bundle args = new Bundle();
           args.putInt(ArticleFragment.ARG_POSITION, position);
           newArticleFragment.setArguments(args);
           // R.id.fragment_container 아이디를 가진 영역의 
           //프래그먼트를 articleFragment 로 교체하고
           // 프래그먼트 매니저의 BackStack 에 쌓는다
           getSupportFragmentManager().beginTransaction()
               .replace(R.id.fragment_container, newArticleFragment)
               .addToBackStack(null)
               .commit();
       }
   }
   ```

   MainActivity를 실행 시 HeadlinesFragment를 표시하고 제목을 클릭했을 때 onHeadlineSelected() 콜백이 호출되며, ArticleFragment로 교체된다.

   주의해서 볼 부분은 onCreate() 메서드에서 savedInstanceState == null 일 때만 HeadlinesFragment를 추가하고 있다. 이 조건이 없다면 화면이 회전될 때마다 기존 HeadlinesFragment가 유지되며 새로운 HeadlinsFragment가 생성되기 때문에 반드시 savedInstanceState가 null인지 검사하는 로직을 잊으면 안된다. 이유는 액티비티를 회전하여 강제로 재생성해도 프래그먼트는 프래그먼트 매니저가 별도로 관리하며 재생성되지 않기 때문이다.

   onHeadlineSelected() 콜백 메서드에서 기사의 내용을 표시하는 ArticleFragment로 교체할 때 addToBackStack(null) 메서드를 통해서 프래그먼트의 백스택에 추가한다. 백스택(BackStack)이란, 프래그먼트 매니저가 내부적으로 관리하고 있는 프래그먼트를 관리하는 스택이다.



#### 3.1.2. 화면이 큰 기기를 위한 구현 추가

안드로이드의 레이아웃은 화면 크기에 따라 분기하게 할 수 있다. 예를 들어, 기본 레이아웃의 경로는 layout/activity_main.xml이지만, layout-large.activity_main.xml을 추가하면 일반적으로는 layout 디렉터리의 XML 파일을 참조하고, 화면이 7인치 이상으로 큰 기기에서는 layout_large 디렉터리의 XML 파일을 참조할 수 있다.

1. 7인치 이상의 레이아웃을 위한 디렉터리를 추가한다.

2. layout-large 에 activity_main.xml을 추가한다.

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:tools="http://schemas.android.com/tools"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
   
       <fragment
           android:id="@+id/headlines_fragment"
           android:name="com.example.user.dynamicuiexam.HeadlinesFragment"
           android:layout_width="0dp"
           android:layout_height="match_parent"
           android:layout_weight="1"
           tools:layout="@android:layout/simple_list_item_1" />
   
       <fragment
           android:id="@+id/article_fragment"
           android:name="com.example.user.dynamicuiexam.ArticleFragment"
           android:layout_width="0dp"
           android:layout_height="match_parent"
           android:layout_weight="2"
           tools:layout="@layout/fragment_articles" />
   </LinearLayout>
   ```

3. MainActivity 코드 수정

   ```java
   @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
   
           // layout-large 의 레이아웃에는 fragment_container 가 없음
           if (findViewById(R.id.fragment_container) != null) {
               // 화면 회전 시 HeadlinesFragment 가 재생성되는 것 방지
               if (savedInstanceState == null) {
                   HeadlinesFragment headlinesFragment = new HeadlinesFragment();
                   // headlinesFragment 를 R.id.fragment_container 영역에 추가
                   getSupportFragmentManager().beginTransaction()
                           .add(R.id.fragment_container, headlinesFragment)
                           .commit();
               }
           }
       }
   ```

   onCreate() 에서 layout-large인지 아닌지에 따라 다르게 처리

   layout-large의 레이아웃은 미리 두 개의 프래그먼트가 위치 해 있으므로 아무것도 하지 않아도 된다.

   

   ```java
   @Override
       public void onHeadlineSelected(int position) {
           ArticleFragment articleFragment = (ArticleFragment) getSupportFragmentManager().findFragmentById(R.id.article_fragment);
           // layout-large 의 경우 null 이 아님
           if (articleFragment == null) {
               // ArticleFragment 프래그먼트 생성
               ArticleFragment newArticleFragment = new ArticleFragment();
               // Argument 로 기사 번호 전달
               Bundle args = new Bundle();
               args.putInt(ArticleFragment.ARG_POSITION, position);
               newArticleFragment.setArguments(args);
               // R.id.fragment_container 아이디를 가진 영역의 프래그먼트를 articleFragment 로 교체하고
               // 프래그먼트 매니저의 BackStack 에 쌓는다
               getSupportFragmentManager().beginTransaction()
                       .replace(R.id.fragment_container, newArticleFragment)
                       .addToBackStack(null)
                       .commit();
           } else {
               articleFragment.updateArticleView(position);
           }
       }
   }
   ```

   기존의 articleFragment라는 변수명을 newArticleFragment로 수정하였고, layout-large의 경우에는 article_fragment 아이디로 이미 ArticleFragment를 포함하고 있으므로, 이것이 null인지 아닌지에 따라서 아른 처리를 하도록 수정했다. 만약 layout-large와 같이 ArticleFragment가 이미 있다면 그 프래그먼트의 기사만 updateArticleView()를 통해 갱신하면 된다.