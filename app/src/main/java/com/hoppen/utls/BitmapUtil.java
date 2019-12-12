package com.hoppen.utls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    /**
     * 将Bitmap转成本地图片
     * @param path 保存为本地图片的地址
     * @param bitmap 要转化的Bitmap
     */
    public static void saveImage(String path, Bitmap bitmap){
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将本地图片转成Bitmap
     * @param path 已有图片的路径
     * @return
     */
    public static Bitmap openImage(String path){
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 资源图片转成Bitmap
     */
    public Bitmap getRescBitmap(Context context,int id){
        /*该方法直接传文件路径的字符串，即可将指定路径的图片读取到Bitmap对象。
        Bitmap bitmap = BitmapFactory.decodeFile(path);*/

        //该方法可从资源文件中读取图片信息。第一个参数一般传getResources(),第二个参数传drawable图片的资源id，如下
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

}
