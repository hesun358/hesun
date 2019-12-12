package com.hoppen.uvcctest

import android.graphics.Bitmap
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
import android.graphics.BitmapFactory
import kotlin.random.Random

class TestActivity : AppCompatActivity(){

    var mbitmap : Bitmap? =null
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

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        mbitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.a,options)

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
                        val i = SkinDetect().faceDetect(t, DetectInfo(1,"asia","yellow",0.36f,0.42f,1,3,1
                        ,0,"1.0",0,0,1), mbitmap,s)
                        Log.e("TAG","i:"+i+"mbitmap:"+mbitmap)
                        iv_img.setImageBitmap(mbitmap)
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

