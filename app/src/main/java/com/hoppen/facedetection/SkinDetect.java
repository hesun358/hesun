package com.hoppen.facedetection;

import android.graphics.Bitmap;

import com.hoppen.uvcctest.DetectInfo;

public class SkinDetect {

    // 添加人脸皮肤检测的so库
    static {
        System.loadLibrary("face");
    }

    public native int faceDetect(Bitmap src_bitmap, DetectInfo detInfo, Bitmap dst_bitmap, String errMsg);

}
