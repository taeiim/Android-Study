# 효율적인 큰 비트맵 로딩

## 1. 잘 될 거라고 생각했어요...

요번에 폰에 있는 사진들을 모두 가져와 사용자에게 리스트로 보여줘야하는 기능을 만들게 되었습니다.  

저는 '퍼미션에 저장소 접근 권한을 주고 미디어 저장소의 모든 사진들의 PATH URI 를 가져와 
ArrayList 에 모두 추가하여 RecyclerView 로 보여주면 되겠다.' 라고 생각했습니다.

저는 바로 작업에 들어갔습니다.

먼저 AndroidManifest 에 
```
<uses-permissionandroid:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```
저장소 접근 권한을 주었습니다.  
그리고 모든 사진들의 PATH 를 뽑는 함수를 만들었습니다.

``` Kotlin
@SuppressLint("Recycle")
private fun getAllImages(activity: Activity){
    val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor: Cursor?
    val columnIndexData: Int
    val albumList = ArrayList<String>()

    val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

    cursor = activity.contentResolver.query(uri, projection, null, null, null)

    columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
    while (cursor!!.moveToNext()) {
        albumList.add(cursor!!.getString(columnIndexData))
    }
    albumList.reverse()
    albumAdapter = AlbumAdapter(context!!, albumList)
    albumAdapter.notifyDataSetChanged()
}

```

마지막으로 사진을 보여줄 RecyclerView 에 쓰일 item view 와 Adapter 을 만들었습니다.

``` Kotlin
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coloring.com.ccb.R
import coloring.com.ccb.ui.palette.AddPaletteColorActivity
import kotlinx.android.synthetic.main.item_photo.view.*

class AlbumAdapter(private val context: Context,
                   private val albumList: ArrayList<String>) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        if (img != null) 
            holder.itemView.photo.imageURI = Uri.parse(albumList[position])
        else 
            holder.itemView.photo.setImageResource(R.drawable.ic_launcher_background)

        holder.itemView.photo.setOnClickListener {
            val intent = Intent(context, AddPaletteColorActivity::class.java)
            if(img != null) intent.putExtra("path", albumList[position])
            else intent.putExtra("path", "noPath")
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = albumList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder = AlbumViewHolder(parent)

    class AlbumViewHolder(parentView : View) : RecyclerView.ViewHolder(
            LayoutInflater.from(parentView.context).inflate(R.layout.item_photo, null, false))
}
```

저는 정말로 잘 작동될 것이라고 생각했습니다.  
그러나 저의 기대를 져버리지 않고 정말 놀랍게도 치명적인 문제가 생기고 맙니다.

앱이 완전히 켜지기 까지 10 초 이상 걸리고,  
사진 리스트들을 보여줄 때, 엄청 많이 폰의 메모리를 먹으면서 폰의 구동이 멈추면서 움직이지 않는 것이었습니다...

휴대폰도 감당못하는 치명적인 저의 미스는 무엇이었을까요?

## 2. 구동을 위한 문제 원인 찾기(삽질 하기)

저는 RecylclerView 의 구조에 문제가 있었던게 아닐까 생각했습니다.  
RecyclerView 관련 문서들을 해석하면서 까지 읽어보았지만 별 소득이 없었습니다.

다시 생각해보니, RecyclerView 에 item 으로 사진들이 잘 보인다는 것은 RecyclerView 의 잘못이 아닌 것을 알게되었습니다.

저는 item 에 문제가 있는게 아닐까 추측했습니다.  

``` xml
<?xml version="1.0" encoding="utf-8"?>
<ImageView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/photo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:scaleType="fitCenter"
    android:adjustViewBounds="true" />
```

위에는 사진을 보여주는 item의 xml 파일 입니다.  
정말로 별거 없이 ImageView 만으로 이루어져 있습니다.

``` Kotlin
if (img != null) 
    holder.itemView.photo.imageURI = Uri.parse(albumList[position])
else 
    holder.itemView.photo.setImageResource(R.drawable.ic_launcher_background)
```

위에 코드는 item 에 이미지를 설정하는 코드입니다.  
여기서 img 는 이미지의 PATH 입니다. 만약 img 가 null 이 아니면
uri 를 통해 item 이미지를 설정합니다.

else 일 때는 임의 이미지 리소스를 설정해줍니다.

저는 이 코드를
``` Kotlin
 holder.itemView.photo.setImageResource(R.drawable.ic_launcher_background)
```
로 바꾸고 테스트를 해보았습니다.  
코드상으로 보면 그냥 임의 사진들로 이루어진 RecyclerView 리스트가 만들어질 것입니다.

결과는 앱이 5초안에 켜지고 리스트를 볼 때도 앱이 잘 구동 되는 것이었습니다!

``` Kotlin
holder.itemView.photo.imageURI = Uri.parse(albumList[position])
```

문제의 코드는 위에의 코드였습니다.
uri 로만 이미지를 설정해서는 안되고  
무엇인가의 처리가 필요하다는 것을 알게 되었습니다.

저는 ImageView 가 이미지를 보여줄 때, Bitmap 을 통해서 보여준다는 것을 떠올렸습니다.  
저는 제가 가지고 있는 사진들의 파일 용량을 보았습니다.  
화질이 좋아서 그런지 70 MB 이상 이었습니다.

저는 사진들의 용량을 보고 확신 했습니다.  
'이 문제는 ImageView 가 사진들을 보여줄 때, 사진들의 Bitmap 크기가 너무 커서 메모리를 많이 먹는 것이구나.'

저는 사진들의 Bitmap 크기를 최적화 시킬 방법을 찾기로 했습니다.

## 3. 비트맵 최적화 하기

저에게 방법은 2가지로 나뉘었습니다.

- Glide 나 Picaso 같은 라이브러리로 비트맵 크기 줄이기
- Android 문서 보면서 직접 비트맵 크기 줄이기

저는 비트맵을 줄이기 위해 라이브러리를 쓰는 것은 비효울적이라고 생각하여 직접 비트맵 크기를 줄이기로 하였습니다.

``` Kotlin
    private fun decodeSampledBitmapFromURI(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)

            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(path, this)
        }
    }
```
저는 위에 코드 처럼 사진의 PATH URI 를 BitmapFactory 를 통해 Bitmap 으로 변환과 동시에 Bitmap 의 크기를 최적화 시키는 함수를 만들었습니다.

이 코드에서 최적화를 담당한 함수는 calculateInSampleSize 입니다.  
코드는 아래와 같습니다.

``` Kotlin
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
```

아래 코드는 적용 시킨 것입니다.  

``` Kotlin
val img = decodeSampledBitmapFromURI(albumList[position], 200, 200)

if (img != null) holder.itemView.photo.setImageBitmap(img)
else holder.itemView.photo.setImageResource(R.drawable.ic_launcher_background)
```
적용하니 정말 잘 돌아갑니다!
