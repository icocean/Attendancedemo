package com.example.attendancedemo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancedemo.R;
import com.example.attendancedemo.model.RequestqiandaoList;

import java.util.List;

public class qiandaoListAddCityAdapter extends RecyclerView.Adapter<qiandaoListAddCityAdapter.myHolder> {
    private Context context;
    private List<RequestqiandaoList.SignListBean> qiandaoList;

    public qiandaoListAddCityAdapter(Context context) {
        this.context = context;
    }

    public void setQiandaoList(List<RequestqiandaoList.SignListBean> qiandaoList) {
        this.qiandaoList = qiandaoList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        myHolder holder = new myHolder
                (LayoutInflater.from(context).inflate(R.layout.list_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  qiandaoListAddCityAdapter.myHolder holder, int position) {
        holder.list_item_name.setText(qiandaoList.get(position).getStuName());
        holder.list_item_data.setText(qiandaoList.get(position).getSignTime());
        int IsLate=qiandaoList.get(position).getIsLate();
        if (IsLate==1){
            holder.list_item_state.setText("迟到");
            holder.list_item_state.setTextColor(Color.parseColor("#EE2C2C"));
        }else {
            holder.list_item_state.setText("正常");
        }

    }

    @Override
    public int getItemCount() {
        return qiandaoList==null?0:qiandaoList.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        TextView list_item_data,list_item_state,list_item_name;

        public myHolder( View itemView) {
            super(itemView);
            list_item_data=itemView.findViewById(R.id.list_item_data);
            list_item_state=itemView.findViewById(R.id.list_item_state);
            list_item_name=itemView.findViewById(R.id.list_item_name);

        }}
}
