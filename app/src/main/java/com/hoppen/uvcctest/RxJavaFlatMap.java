package com.hoppen.uvcctest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoppen.adapter.DetectContrastAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class RxJavaFlatMap extends AppCompatActivity {

    private Button bt_cs;
    private RecyclerView my_db_RecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map_layout);
        bt_cs = findViewById(R.id.bt_cs);
        my_db_RecyclerView = findViewById(R.id.my_db_RecyclerView);

        my_db_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        my_db_RecyclerView.setAdapter(new DetectContrastAdapter());

        bt_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheShi();
            }
        });
    }

    /*
     * 使用map对集合数据进行转换---数据类型转换
     * */
    /*private void RxJavaMapStream() {
        //创建集合
        List<Integer> list = Arrays.asList(1,2,3,4,7,8,0);
        Observable.from(list)
                //对集合数据进行转换,参数 1.代表了原数据类型(转换前)  2.代表了要转换最终结果的数据类型(转换后)
                //提示:可以把流转换成bitmap,文件,String...    把String字符串或数据库Cursor转换为Bean类等
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer.toString();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("++++","rxjava对集合数据类型转换操作"+s);
                    }
                });

    }*/

    private void cheShi(){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i<100;i++){
            list.add("H:"+i);
        }
        Observable.just(list)
                .map(new Function<List<String>, String>() {
                    @Override
                    public String apply(List<String> strings) throws Exception {
                        StringBuilder sbu = new StringBuilder();
                        for (String text:strings) {
                            sbu.append(text).append("卧槽");
                        }
                        Thread.sleep(10);
                        return sbu.toString();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        Log.e("TAG",value);
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

}
