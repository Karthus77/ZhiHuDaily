package com.example.zhihudaily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class comment extends AppCompatActivity {
    private TextView CN;
    private RecyclerView Re1;
    private RecyclerView Re2;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_comment);
        back=findViewById(R.id.Back);
        CN=findViewById(R.id.totalNumber);
        Re1=findViewById(R.id.Comment);
        Re1.setLayoutManager(new LinearLayoutManager(comment.this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent it1=getIntent();
        Bundle bd1=it1.getExtras();
        final String id=bd1.getString("id");
        final List <Map<String,Object>> list = new ArrayList<>();
        final List <Map<String,Object>> list1 = new ArrayList<>();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                final StringBuilder response1 = new StringBuilder();
                final StringBuilder response2 = new StringBuilder();
                String line;
                String line1;
                String line2;
                try {
                    URL url=new URL("https://news-at.zhihu.com/api/3/story-extra/"+id);
                    HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();//双方建立连接
                    urlConnection.setRequestMethod("GET");//给服务器发送请求
                    InputStream inputStream=urlConnection.getInputStream(); //字节流
                    Reader reader=new InputStreamReader(inputStream); //把字节流转化成字符流
                    BufferedReader bufferedReader=new BufferedReader(reader);//字符流 转成 缓冲流，一次可以读一行
                    URL Url=new URL("https://news-at.zhihu.com/api/4/story/"+id+"/long-comments");//获取服务器哦地址
                    HttpURLConnection UrlConnection = (HttpURLConnection) Url.openConnection();//双方建立连接
                    UrlConnection.setRequestMethod("GET");
                    InputStream inputStream1=UrlConnection.getInputStream();
                    Reader reader1=new InputStreamReader(inputStream1);
                    BufferedReader bufferedReader1=new BufferedReader(reader1);
                    URL url1=new URL("https://news-at.zhihu.com/api/4/story/"+id+"/short-comments");
                    HttpURLConnection urlConnection1 =(HttpURLConnection) url1.openConnection();
                    urlConnection1.setRequestMethod("GET");
                    InputStream inputStream2=urlConnection1.getInputStream();
                    final Reader reader2=new InputStreamReader(inputStream2);
                    BufferedReader bufferedReader2=new BufferedReader(reader2);
                    while ((line2=bufferedReader2.readLine())!=null){
                        response2.append(line2);
                    }

                    while ((line1=bufferedReader1.readLine())!=null){//当temp读到的数据为空就结束
                        response1.append(line1);

                    }

                    while ((line=bufferedReader.readLine())!=null){//当temp读到的数据为空就结束
                        response.append(line);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
                                String comments=jsonObject.getString("comments");
                                CN.setText(comments+"条评论");
                                String shorts=jsonObject.getString("short_comments");
                                String longs=jsonObject.getString("long_comments");
                                if (longs.equals("0"))
                                {
                                }
                                else
                                {
                                    JSONObject jsonObject1=new JSONObject(String.valueOf(response1));
                                    JSONArray jsonArray1=jsonObject1.getJSONArray("comments");
                                    Map<String,Object> map1=new HashMap<>();
                                    map1.put("number",longs);
                                    list.add(map1);
                                    int t=Integer.parseInt(longs);

                                    for(int i=0;i<t;i++)
                                    {
                                        Map<String,Object> map=new HashMap<>();
                                        JSONObject jsonObject3=jsonArray1.getJSONObject(i);
                                        String time=jsonObject3.getString("time");
                                        String author=jsonObject3.getString("author");
                                        String content=jsonObject3.getString("content");
                                        String imageUrl =jsonObject3.getString("avatar");
                                        map.put("time",time);
                                        map.put("author",author);
                                        map.put("content",content);
                                        map.put("image",imageUrl);
                                        list.add(map);
                                    }


                                }

                                if (shorts.equals("0"))
                                {
                                }
                                else {
                                JSONObject jsonObject2=new JSONObject(String.valueOf(response2));
                                JSONArray jsonArray2=jsonObject2.getJSONArray("comments");
                                Map<String,Object> map1=new HashMap<>();
                                map1.put("number",shorts);
                                map1.put("key",shorts);
                                list.add(map1);
                                int k=Integer.parseInt(shorts);
                                if(k>20)
                                {
                                    k=20;
                                }
                                for(int j=0;j<k;j++)
                                {
                                    Map<String,Object> map=new HashMap<>();
                                    JSONObject jsonObject4=jsonArray2.getJSONObject(j);
                                    String time=jsonObject4.getString("time");
                                    String author=jsonObject4.getString("author");
                                    String content=jsonObject4.getString("content");
                                    String imageUrl =jsonObject4.getString("avatar");
                                    map.put("time",time);
                                    map.put("author",author);
                                    map.put("content",content);
                                    map.put("image",imageUrl);
                                    list.add(map);

                                }}
                                Re1.setAdapter(new LongAdapter(comment.this,list));



                            }

                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();//启动线程

    }
}