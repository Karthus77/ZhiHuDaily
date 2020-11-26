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

import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Paper context;
    private List<Map<String,Object>> list;
    private View inflater;
    private AdapterView.OnItemClickListener onItemClickListener;
    //构造方法，传入数据
    public MyAdapter(Paper context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    private static final int news = 0;
    private static final int date = 1;
    private static final int noNet= 2;
    @Override
    public int getItemViewType(int position) {
        int size=list.get(position).size();
        if (size==1)
        {
            return date;
        }
        else if(size==2)
        {
            return noNet;
        }
        else
        {
            return news;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        if(viewType==news)
        {
        inflater = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;}
        else if(viewType==noNet)
        {
            inflater=LayoutInflater.from(context).inflate(R.layout.item_nonet,parent,false);
            noHolder noHolder=new noHolder(inflater);
            return  noHolder;
        }
        else
        {
            inflater = LayoutInflater.from(context).inflate(R.layout.item_date,parent,false);
            dateHolder dateHolder = new dateHolder(inflater);
            return  dateHolder;
        }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //将数据和控件绑定
        int viewType=getItemViewType(position);
                if(viewType==0)
                {

           ViewHolder viewholder = (ViewHolder) holder;
           String number_title=list.get(position).get("title").toString();
           if(number_title.length()>27)
           {
               String title=number_title.substring(0,27);
               viewholder.titlePaper.setText(title+"...");
           }
           else{
               viewholder.titlePaper.setText(list.get(position).get("title").toString());
           }

        viewholder.titleVice.setText(list.get(position).get("hint").toString());
        Glide.with(context).load(list.get(position).get("image")).into(viewholder.titleImage);

        viewholder.paper_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Context.class);
                Bundle bd=new Bundle();
                bd.putString("id",list.get(position).get("id").toString());
                bd.putString("url",list.get(position).get("url").toString());
                intent.putExtras(bd);
                context.startActivity(intent);

            }
        });}
                else if(viewType==2)
                {
                    noHolder noHolder=(noHolder) holder;
                    noHolder.hint.setText("网络好像走丢了");
                }
        else {

            dateHolder viewHolder = (dateHolder) holder;
            String day=list.get(position).get("date").toString();
            int nowDay=(Integer.parseInt(day)%100);
            int nowMonth=((Integer.parseInt(day)%10000)/100);
            viewHolder.date.setText(nowMonth+" 月 "+nowDay+" 日");}






    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout paper_item;
        TextView titlePaper;
        TextView titleVice;
        ImageView titleImage;
        TextView everyday;
        public ViewHolder(View view) {
            super(view);
            everyday=view.findViewById(R.id.everyday);
            titlePaper= view.findViewById(R.id.titleArticle);
            titleVice= view.findViewById(R.id.viceTitle);
            titleImage = view.findViewById(R.id.imageTitle);
            paper_item=view.findViewById(R.id.paper_item);
        }
    }
    class dateHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView background;
        public dateHolder (View view){
            super(view);
            date=view.findViewById(R.id.newDate);
            background=view.findViewById(R.id.background);
        }
    }
    class noHolder extends RecyclerView.ViewHolder{
        TextView hint;
        public noHolder (View view){
            super(view);
            hint =view.findViewById(R.id.hint);
        }
    }
}
