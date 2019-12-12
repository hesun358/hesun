package com.hoppen.uvcctest;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoppen.adapter.FilePopupAdapter;
import com.hoppen.utls.FileUtil;

import java.io.File;
import java.util.ArrayList;

public class FilePopupWindow extends PopupWindow implements FilePopupAdapter.FileSelectItmInterface{

    private Context context;
    private FilePopupAdapter adapter;
    private ArrayList<String> fileDir;
    private String mCurrentPathFile;

    public FilePopupWindow(Context context) {
        super(context);
        this.context = context;
        setFocusable(false);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.file_popup_window_layout,null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        String sdPath = FileUtil.getExternalStoragePath(context);
        mCurrentPathFile = sdPath;
        Log.e("TAG","sdPath:"+sdPath);
        RecyclerView file_popup_recycler_view = view.findViewById(R.id.file_popup_recycler_view);
        file_popup_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        fileDir = FileUtil.getFileDir(sdPath);
        adapter = new FilePopupAdapter(fileDir);
        adapter.setSelectItm(this);
        file_popup_recycler_view.setAdapter(adapter);

        Button bt_fh_fbj = view.findViewById(R.id.bt_fh_fbj);
        bt_fh_fbj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPathFile = getCurrentParentFile();
                fileDir = FileUtil.getFileDir(getCurrentParentFile());
                adapter.updateData(fileDir);
                mCurrentPathFile = currentPathFile;
            }
        });
    }

    @Override
    public void OnFileItmCallback(int position) {
        String filePath = fileDir.get(position);
        mCurrentPathFile = filePath;
        Log.e("TAG","position:"+filePath);
        fileDir = FileUtil.getFileDir(filePath);
        adapter.updateData(fileDir);
    }

    /**
     * 获取父目录
     */
    public String getCurrentParentFile(){
        File parentFile = new File(mCurrentPathFile).getParentFile();
        return parentFile.getAbsolutePath();
    }

}
