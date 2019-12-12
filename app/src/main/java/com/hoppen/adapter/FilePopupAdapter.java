package com.hoppen.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoppen.uvcctest.R;

import java.util.ArrayList;

public class FilePopupAdapter extends RecyclerView.Adapter<FilePopupAdapter.ViewHolder> {

    private ArrayList<String> list;
    private FileSelectItmInterface anInterface;

    public void setSelectItm(FileSelectItmInterface anInterface) {
        this.anInterface = anInterface;
    }

    public FilePopupAdapter(ArrayList<String> list) {
        this.list = list;
    }

    public void updateData(ArrayList<String> list){
        if (list!=null){
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itm_file_popup,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String name = list.get(position);
        holder.tv_file_name.setText(name);
        holder.layout_file_itm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anInterface!=null){
                    anInterface.OnFileItmCallback(position);
                }
            }
        });
        holder.bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","点击选择路径:"+name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layout_file_itm;
        private TextView tv_file_name;
        private Button bt_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_file_itm = itemView.findViewById(R.id.layout_file_itm);
            tv_file_name = itemView.findViewById(R.id.tv_file_name);
            bt_select = itemView.findViewById(R.id.bt_select);
        }
    }

    public interface FileSelectItmInterface{
        void OnFileItmCallback(int position);
    }

}
