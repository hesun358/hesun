package com.hoppen.uvcctest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.hoppen.facedetection.SkinDetect
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_layout.*
import kotlin.random.Random
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class TestActivity : AppCompatActivity(){

    var s : String = ""
    var filePopup : FilePopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_layout)
        photograph.setOnClickListener {
            getImg()
        }
        file_selector.setOnClickListener {
            filePopup = FilePopupWindow(this)
            filePopup!!.showAtLocation(window.decorView,Gravity.CENTER,0,0)
        }

        bt_qd.setOnClickListener {
            val index = Random.nextInt(4)
            Log.e("TAG","index:"+index)
            uvc_camera_view.switchLight(index)
        }

    }

    private fun getImg(){
        Observable.create(ObservableOnSubscribe<Bitmap> {
            it.onNext(uvc_camera_view.bitmap)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Bitmap>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Bitmap) {
                        val dst_bitmap = Bitmap.createBitmap(t.getWidth(), t.getHeight(), Bitmap.Config.ARGB_8888)
                        // 初始化一个bitmap,存放返回的位图
                        val date = DetectInfo(1,"asia","yellow",0.36f,0.42f,1,3,1,0,"1.0",0,0,1)
                        val i = SkinDetect().faceDetect(t,date , dst_bitmap,s)
                        Log.e("TAG","i:"+i+"mbitmap:"+dst_bitmap+"分数："+date.score)
                        iv_img.setImageBitmap(dst_bitmap)
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    override fun onStart() {
        super.onStart()
        uvc_camera_view.uvcCameraStar()
    }

    override fun onStop() {
        super.onStop()
        uvc_camera_view.uvcCameraStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        uvc_camera_view.uvcCameraDestroy()
    }

}

