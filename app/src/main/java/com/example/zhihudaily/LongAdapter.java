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

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private comment context;
    private List<Map<String,Object>> list;
    private View inflater;
    private AdapterView.OnItemClickListener onItemClickListener;

    //构造方法，传入数据
    public LongAdapter(comment context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    private static final int long_number = 0;
    private static final int comment = 1;
    private static final int short_number=2;
    @Override
    public int getItemViewType(int position) {
        int size=list.get(position).size();
        if (size==1)
        {
            return long_number;
        }
        else if (size==2)
        {
            return  short_number;
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
            inflater = LayoutInflater.from(context).inflate(R.layout.item_long,parent,false);
        ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;}
        else if(viewType==short_number)
        {
            inflater=LayoutInflater.from(context).inflate(R.layout.item_coms,parent,false);
            shortHolder shortHolder = new shortHolder(inflater);
            return  shortHolder;
        }
        else
        {
            inflater = LayoutInflater.from(context).inflate(R.layout.item_coml,parent,false);
            comHolder comHolder = new comHolder(inflater);
            return  comHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //将数据和控件绑定
        int viewType=getItemViewType(position);
        if(viewType==1)
        {

            ViewHolder viewholder=(ViewHolder)holder;
            Calendar c=Calendar.getInstance();
            int d = c.get(Calendar.DAY_OF_MONTH);
            int m =c.get(Calendar.MONTH);
            String time=list.get(position).get("time").toString();
            int times=Integer.parseInt(time);
            int standard=1606172451;
            int standard_day=24;
            int standard_month=11;
            int between=times-standard;
            int between_day=between/86400;
            int now_day=standard_day+between_day;
            int between_hour=between/3600;
            int now_hour=(between_hour+7)%24;
            int between_min=between/60;
            int now_min=between_min%60;
            int now_month=11;
            if(now_hour<0&&now_min<0)
            {
                now_day=now_day-1;
                now_hour=24+now_hour-1;
                now_min=60+now_min;
            }
            if(now_hour>0&&now_min<0)
            {
                now_hour=now_hour-1;
                now_min=now_min+60;
            }
            if(now_hour==0&&now_min<0)
            {
                now_day=now_day-1;
                now_hour=23;
                now_min=now_min+60;
            }
            if(now_day>30)
            {
                now_day=now_day-30;
            }
            if(now_day==d)
            {
                if(now_hour<10&&now_min<10)
                    viewholder.nowTime.setText("今天 "+"0"+now_hour+":"+"0"+now_min);
                 else if(now_hour<10&&now_min>=10)
                     viewholder.nowTime.setText("今天 "+"0"+now_hour+":"+now_min);
                 else if(now_min<10)
                     viewholder.nowTime.setText("今天 "+now_hour+":"+"0"+now_min);
                 else
                     viewholder.nowTime.setText("今天 "+now_hour+":"+now_min);
            }
            else
            {
                if(now_hour<10&&now_min<10)
                    viewholder.nowTime.setText(standard_month+"-"+now_day+" "+"0"+now_hour+":"+"0"+now_min);
                else if(now_hour<10&&now_min>=10)
                    viewholder.nowTime.setText(standard_month+"-"+now_day+" "+"0"+now_hour+":"+now_min);
                else if(now_min<10)
                    viewholder.nowTime.setText(standard_month+"-"+now_day+" "+now_hour+":"+"0"+now_min);
                else
                    viewholder.nowTime.setText(standard_month+"-"+now_day+" "+now_hour+":"+now_min);
            }
            viewholder.name.setText(list.get(position).get("author").toString());
            viewholder.comment.setText(list.get(position).get("content").toString());
            Glide.with(context).load(list.get(position).get("image")).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(viewholder.head);
        }
        else if(viewType==2)
        {
            shortHolder shortHolder=(shortHolder)holder;
            shortHolder.number.setText(list.get(position).get("number").toString()+"条短评");
        }
        else{
            comHolder comHolder=(LongAdapter.comHolder)holder;
            comHolder.number.setText(list.get(position).get("number").toString()+"条长评");
        }




    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nowTime;
        TextView name;
        TextView comment;
        ImageView head;
        public ViewHolder(View view) {
            super(view);
            nowTime=view.findViewById(R.id.comment_time);
            name=view.findViewById(R.id.name_Long);
            comment=view.findViewById(R.id.comment_Long);
            head=view.findViewById(R.id.head_Long);
        }
    }
    class  comHolder extends RecyclerView.ViewHolder{
        TextView number;

        public comHolder(View view) {
            super(view);
            number=view.findViewById(R.id.number_long);
        }
    }
    class  shortHolder extends RecyclerView.ViewHolder{
        TextView number;

        public shortHolder(View view) {
            super(view);
            number=view.findViewById(R.id.number_short);
        }
    }
}
