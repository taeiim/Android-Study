# 0.참고문서

https://developer.android.com/reference/android/support/v7/widget/RecyclerView

http://liveonthekeyboard.tistory.com/135

http://www.kmshack.kr/2014/10/android-recyclerview/

[루비페이퍼] 될 때까지 안드로이드 (오준석)



# 1.Recyclerview

### 1.1. 소개

- 기존의 ListView는 커스텀하기에는 구조적인 문제로 많은 제약이 따랐으며, 구조적인 문제로 인해 성능문제가 있었다. 그래서 이 문제들을 해결하기 위해 좀 더 다양한 형태로 개발자가 커스텀할 수 있도록 만들어진 것이 RecyclerView!
- 기존의 ListView보다 유연하고 성능이 향상된 고급 위젯
- 리스트를 표시하기 위한 어댑터뷰를 좀 더 개선한 컴포넌트

- 큰 틀은 유지한 채 데이터만 바뀜

1. 어댑터뷰와의 차이점
   1. RecyclerView는 레이아웃 매니저(LayoutManager)를 지정해 줘야 함
   2. 어댑터뷰에서는 선택사항이었던 뷰홀더 패던이 RecyclerView에서는 꼭 구현해야 함
   3. 어댑터뷰는 미리 제공된 어댑터가 있는 반면 RecyclerView의 어댑터는 아무것도 제공해 주지 않음
2. 장점
   1. ViewHolder패턴을 적용하여 뷰를 재활용
   2. Swipe를 이용하여 직관적인 Refresh UI구조 설계가능
   3. position에 따라서 한 리스트 내에서 다양한 뷰를 표현가능(MultiView)
3. 단점 
   1. 이벤트 리스너와 커서(Cursor)를 지원하지 않음



### 1.2. 주요 클래스

- ##### RecyclerView.Adapter  

  ##### 어댑터뷰의 어댑터와 같은 역할

- ##### RecyclerView.ViewHolder

  ##### 뷰홀더 클래스는 이것을 상속해야함

- ##### LayoutManager

  ##### 데이터를 배치하고, 뷰의 재사용 등을 결정하는 역할을 하여 기존의 어댑터뷰보다 성능이 개선됨

  - LinearLayoutManager : 데이터를 리스트뷰처럼 세로나 가로 한 줄로 표시

  - GridLayoutManager : 그리드뷰처럼 데이터를 그리드 형식으로 표시

  - StaggeredGridLayoutManager : 그리드뷰처럼 데이터를 격자 형식으로 표시하면서 아이템의 높이가 일정하지 않아도 되는 지그재그형 그리드 형식으로 표시

    ![](D:\GitHub_Study\Android-Study\study\week4\RecyclerView\그림01.png)

- ##### RecyclerView.ItemAnimation

  ##### 아이템이 추가, 삭제 또는 재정렬될 때 애니메이션 정의

- ##### RecyclerView.ItemDecoration

  ##### 아이템을 세부적으로 꾸밈



### 1.3. 예제

##### 1.3.1.RecyclerView + CardView 사용

1. 라이브러리 종속성 추가

   ```java
   dependencies{
       ...
   	implementation 'com.android.support:recyclerview-v7:26.1.0'
   	implementation 'com.android.support:cardview-v7:26.1.0'   
   }
   ```

2. CardView 레이아웃 작성

3. 어댑터 작성

   - RecyclerView.Adapter<VH>를 상속받아 구현해야 함

   - <VH> 부분에는 RecyclerView.ViewHolder 클래스를 상속한 뷰홀더 지정해야 함

     => ListView에서는 뷰홀더 패턴 사용이 권장사항이었으나, RecyclerView는 의무

   ```java
   import android.support.v7.widget.RecyclerView;
   import android.view.LayoutInflater;
   import android.view.View;
   import android.view.ViewGroup;
   import android.widget.TextView;
   import java.util.List;
   
   public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
       private final List<CardItem> mDataList;
       public MyRecyclerAdapter(List<CardItem> dataList){
           mDataList = dataList;
       }
       // 뷰 홀더를 생성하는 부분. 레이아웃을 만드는 부분
       @Override
       public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.item_card, parent, false);
           return new ViewHolder(view);
       }
       // 뷰 홀더에 데이터를 설정하는 부분
       @Override
       public void onBindViewHolder(ViewHolder holder, int position) {
           // CardItem는 아이템에 표시할 데이터를 가지는 모델 클래스
           CardItem item = mDataList.get(position);
           holder.title.setText(item.getTitle());
           holder.contents.setText(item.getContents());
       }
       //아이템의 수
       @Override
       public int getItemCount() {
           return mDataList.size();
       }
       //각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
       //반드시 RecyclerView.ViewHolder를 상속해야 함
       public static class ViewHolder extends RecyclerView.ViewHolder{
           TextView title;
           TextView contents;
   
           public ViewHolder(View itemview){
               super(itemview);
               title = itemview.findViewById(R.id.title_text);
               contents = itemview.findViewById(R.id.contents_text);
           }
       }
   }
   ```

4. 레이아웃 매니저와 어댑터 설정 - LinearLayoutManager

   ```java
   import android.support.v7.app.AppCompatActivity;
   import android.os.Bundle;
   import android.support.v7.widget.GridLayoutManager;
   import android.support.v7.widget.LinearLayoutManager;
   import android.support.v7.widget.RecyclerView;
   import android.support.v7.widget.StaggeredGridLayoutManager;
   
   import java.util.ArrayList;
   import java.util.List;
   
   public class MainActivity extends AppCompatActivity {
   
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
   
           RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
           //레이아웃 매니저로 LinearLayoutManager를 설정
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
           recyclerView.setLayoutManager(layoutManager);
           //표시할 임시 데이터
           List<CardItem> dataList = new ArrayList<>();
           // dataList.add(new CardItem(title, contents));
           dataList.add(new CardItem("이것은 첫번째 아이템", "안드로이드 보이라고 합니다"));
           dataList.add(new CardItem("이것은 두번째 아이템", "두 줄 입력\n두 줄 입니다"));
           dataList.add(new CardItem("이것은 세번째 아이템", "세줄\n두번째 줄\n세번째 줄"));
           dataList.add(new CardItem("이것은 네번째 아이템", "잘 되네요!"));
           //어댑터 설정
           MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
           recyclerView.setAdapter(adapter);
       }
   }
   ```

   ![1530258033138](C:\Users\dsm2016\AppData\Local\Temp\1530258033138.png)

   

   - 레이아웃 매니저 코드 정리

     - LinearLayoutManager

       ```java
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       recyclerView.setLayoutManager(linearLayoutManager);
       ```

     - GridLayoutManager

       ```java
       GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
       gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       recyclerView.setLayoutManager(gridLayoutManager);
       ```

     - StaggeredGridLayoutManager

       ```java
       StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
       recyclerView.setLayoutManager(gridLayoutManager);
       ```

##### 1.3.2.클릭 이벤트 처리

1. RecyclerView 아이템의 클릭 리스너를 제공하지 않는다

   =>모든 이벤트에 대한 처리를 개발자가 직접 구현해야함

```java
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private final List<CardItem> mDataList;
    private MyRecyclerViewClickListener mListener;
    ... 생략 ...
    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }
    public interface MyRecyclerViewClickListener {
        // 아이템 전체 부분의 클릭
        void onItemClicked(int position);
        // share 버튼 클릭
        void onShareButtonClicked(int position);
        // Learn More 버튼 클릭
        void onLearnMoreButtonClicked(int position);
    }
}
```

2. onBindViewHolder() 메서드에서 클릭이 발생하는 곳을 MyRecyclerViewClickListener와 연결

```java
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
   ... 생략 ...
    // 뷰 홀더에 데이터를 설정하는 부분
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.contents.setText(item.getContents());
        // 클릭 이벤트
        if (mListener != null) {
            // 현재 위치
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListener.onShareButtonClicked(pos);
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onLearnMoreButtonClicked(pos);
                }
            });
        }
    }
    ... 생략 ...
    // 각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
    // 반드시 RecyclerView.ViewHolder를 상속해야 함
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView contents;
        Button share;
        Button more;

        public ViewHolder(View itemview) {
            super(itemview);
            title = itemview.findViewById(R.id.title_text);
            contents = itemview.findViewById(R.id.contents_text);
            share = (Button) itemview.findViewById(R.id.share_button);
            more = (Button) itemview.findViewById(R.id.more_button);
        }
    }
	... 생략 ...
}
```

3. 액티비티에서 리스너 구현, 메서드 재정의

```java
public class MainActivity extends AppCompatActivity implements MyRecyclerAdapter.MyRecyclerViewClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ... 생략 ...
        // 어댑터 설정
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClicked(int position) {
        Log.d(TAG, "onItemClicked: " + position);
    }
    @Override
    public void onShareButtonClicked(int position) {
        Log.d(TAG, "onShareButtonClicked: " + position);
    }
    @Override
    public void onLearnMoreButtonClicked(int position) {
        Log.d(TAG, "onLearnMoreButtonClicked: " + position);
    }
}
```

4. 콜백이 잘 동작하는지 확인

##### 1.3.3. 애니메이션

1. 어댑터뷰에서는 데이터가 변경되었을 때 이를 반영하기 위해 notifyDataSetChanged() 메서드를 호출하여 데이터 변경을 통지하고 뷰가 생신되도록 함 => 항상 전체 항목을 새로 로드

2. RecyclerView의 어댑터는 각 상황에 맞게 사용할 수 있는 통지 메서드 제공

   - notifyItemInserted(int position) : position 위치의 아이템이 삽입된 것을 통지
   - notifyItemRemoved(int position) : position 위치의 아이템이 삭제된 것을 통지

   ```java
   public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
       ... 생략 ...
       public void removeItem(int position){
           mDataList.remove(position);
           notifyItemRemoved(position);
           notifyItemRangeChanged(position, mDataList.size());
       }
       public void addItem(int position, CardView item){
           mDataList.add(position, item);
           notifyItemInserted(position);
           notifyItemRangeChanged(position, mDataList.size());
       }
   }
   
   ```

   - 위 메서드들은 아이템이 추가, 삭제되면서  기본적으로 애니메이션이 지원됨
   - notifyItemRangeChanged(position, mDataList.size()) 메서드를 호출해야 제대로 작동
     - 이 메서드는 첫 번째 인자부터 두 번째 인자 사이의 데이터가 변경되었음을 통지
     - 이에 맞게 제대로 동작하도록 함

3. ItemAnimator와 ItemDecoration

   1. RecyclerView는 애니메이션 모양을 꾸밀 수 있는 방법을 제공

      - ItemAnimator

        - 아이템이 추가, 삭제, 정렬될 때의 애니메이션을 정의할 수 있는 클래스
        - 이 클래스를 상속하면 여러 가지 추상 메서드를 구현하여 애니메이션 구성 가능

      - ItemDecoration

        - 아이템의 모양을 꾸밀 수 있는 클래스

          ```java
          DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
          recyclerView.addItemDecoration(decoration);
          ```

          

      - DefaultItemAnimator

        - RecyclerView는 특별한 설정이 없으면 미리정의된 DefaultItemAnimator가 적용됨

        - 이 클래스는 추가, 삭제, 정렬 애니메이션의 실행 속도를 조절할 수 있음

          ```java
          DefaultItemAnimator animator = new DefaultItemAnimator();
          animator.setAddDuration(1000);
          animator.setremoveDuration(1000);
          animator.setMoveDuration(1000);
          animator.setChangeDuration(1000);
          recyclerView.setItemAnimator(animator);
          ```

   

