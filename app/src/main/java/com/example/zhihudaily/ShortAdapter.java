package com.example.zhihudaily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Comment;

import java.util.List;
import java.util.Map;

public class ShortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private comment context;
    private List<Map<String,Object>> list;
    private View inflater;
    private AdapterView.OnItemClickListener onItemClickListener;
    //构造方法，传入数据
    public ShortAdapter(comment context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    private static final int short_number = 0;
    private static final int comment = 1;
    @Override
    public int getItemViewType(int position) {
        int size=list.get(position).size();
        if (size==1)
        {
            return short_number;
        }
        else
        {
            return comment;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        if(viewType==comment)
        {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_short,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;}
        else
        {
            inflater = LayoutInflater.from(context).inflate(R.layout.item_coms,parent,false);
            comHolder comHolder = new comHolder(inflater);
            return  comHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //将数据和控件绑定
        int viewType=getItemViewType(position);
        if(viewType==1) {
            ViewHolder viewholder=(ViewHolder)holder;
            viewholder.name.setText(list.get(position).get("author").toString());
            viewholder.comment.setText(list.get(position).get("content").toString());
            Glide.with(context).load(list.get(position).get("image")).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(viewholder.head);
        }
        else {
            comHolder comHolder=(comHolder)holder;
            comHolder.number.setText(list.get(position).get("number").toString()+"条短评");
        }



    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView comment;
        ImageView head;
        public ViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name_Short);
            comment=view.findViewById(R.id.comment_Short);
            head=view.findViewById(R.id.head_Short);
        }
    }
    class  comHolder extends RecyclerView.ViewHolder{
        TextView number;

        public comHolder(View view) {
            super(view);
            number=view.findViewById(R.id.number_short);
        }
    }
}
