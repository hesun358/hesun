package com.hoppen.utls;

import android.content.Context;
import android.os.Environment;
import android.telephony.mbms.FileInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AiMr on 2017/10/11
 */
public class FileUtil {
    public static final String DIR_UPDATE = "MyVideo";
    public static final String FOLDER_NAME = "flower";
    public static final String DIR_IMG = "OfficeSpImage";

    /**
     * 获取sd卡目录
     */
    public static String getExternalStoragePath(Context context){
        String storageState = Environment.getExternalStorageState();
        String cacheDir = "";
        if(storageState.equals(Environment.MEDIA_MOUNTED)){
            cacheDir = Environment.getExternalStorageDirectory().getPath();
        }else{
            cacheDir = context.getCacheDir().getPath();
        }
        return cacheDir+ File.separator;
    }

    /**
     *
    创建视频文件
     */
    public static File createFileVideo(Context context, String filename){
        String path = FileUtil.getExternalStoragePath(context)+filename+".mp4";
        File file = new File(path);
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        try {
            if(file.createNewFile()){
                Log.d("TAG","文件创建成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前路劲所有文件夹
     */
    public static ArrayList<String> getFileDir(String filePath) {
        ArrayList<String> paths = new ArrayList<>();
        File f = new File(filePath);
        File[] files = f.listFiles();
        //判断当前下是否有文件夹
        if (files.length <= 0){
            Log.e("TAG","一个文件夹都没有");
            return null;
        }
        for (File file : files) {
            //过滤一遍,1.是否为文件夹,2.是否可访问
            if (file.isDirectory() && file.listFiles() != null) {
                paths.add(file.getPath());
            }
        }
        return paths;
    }

    /**
     * 获取当前路劲所有图片
     */
    public static ArrayList<String> getFileList(String path){
        ArrayList<String> fileList = new ArrayList<>();
        File file = new File(path);
        File[] listFiles = file.listFiles();
        if (listFiles.length <= 0){
            Log.e("TAG","当前目录没有文件");
            return null;
        }
        for (File f : listFiles) {
            String filePath = f.getAbsolutePath();
            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                fileList.add(path);
            }
        }
        return fileList;
    }

    /**
     * 文件过滤,将手机中隐藏的文件给过滤掉
     */
    public static File[] fileFilter(File file) {
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        return files;
    }

}
