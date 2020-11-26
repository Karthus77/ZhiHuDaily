package com.example.zhihudaily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Paper extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView Date;
    TextView   month;
    MyAdapter myAdapter;

    int date;
    int yesterday;
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_paper);

        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        month=findViewById(R.id.month);
        Date=findViewById(R.id.date);
        time=findViewById(R.id.hello);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List <Map<String,Object>> list = new ArrayList<>();


        final Thread thread=new Thread(new Runnable() {


            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                final StringBuilder response1 = new StringBuilder();
                String line;
                String line1;
                Calendar c=Calendar.getInstance();
                int t = c.get(Calendar.HOUR_OF_DAY);
                if(t>=18)
                {
                    time.setText("晚上好!");
                }
                else if(t<=8&&t>=6)
                {
                    time.setText("早上好!");
                }
                else
                {
                    time.setText("知乎日报");
                }
                int d=c.get(Calendar.DAY_OF_MONTH);
                String days=String.format("%d",d);
                Date.setText(days);

                int months=c.get(Calendar.MONTH);
                switch (months+1)
                {
                    case 1:month.setText("一月");
                    break;
                    case 2:month.setText("二月");
                    break;
                    case 3:month.setText("三月");
                    break;
                    case 4:month.setText("四月");
                    break;
                    case 5:month.setText("五月");
                    break;
                    case 6:month.setText("六月");
                    break;
                    case 7:month.setText("七月");
                    break;
                    case 8:month.setText("八月");
                    break;
                    case 9:month.setText("九月");
                    break;
                    case 10:month.setText("十月");
                    break;
                    case 11:month.setText("十一月");
                    break;
                    default:month.setText("十二月");

                }
                try {
                    URL url=new URL("https://news-at.zhihu.com/api/3/stories/latest");//获取服务器哦地址
                    HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();//双方建立连接

                    urlConnection.setRequestMethod("GET");//给服务器发送请求
                    InputStream inputStream=urlConnection.getInputStream(); //字节流
                    Reader reader=new InputStreamReader(inputStream); //把字节流转化成字符流
                    BufferedReader bufferedReader=new BufferedReader(reader);//字符流 转成 缓冲流，一次可以读一行


                    while ((line=bufferedReader.readLine())!=null){//当temp读到的数据为空就结束
                        response.append(line);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                date=jsonObject.getInt("date");
                                int day=date%100;
                                String days=String.format("%d",day);
                                JSONArray jsonArray = jsonObject.getJSONArray("stories");
                                for (int i = 0; i < 6; i++) {
                                    Map<String,Object> map=new HashMap<>();
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                                    String title = jsonObject1.getString("title");
                                    String vicetitle = jsonObject1.getString("hint");
                                    String url=jsonObject1.getString("url");
                                    String id=jsonObject1.getString("id");
                                    JSONArray jsonArray1=jsonObject1.getJSONArray("images");
                                    String imageUrl=(String)jsonArray1.get(0);
                                    map.put("url",url);
                                    map.put("id",id);
                                    map.put("image",imageUrl);
                                    map.put("title", title);
                                    map.put("hint",vicetitle);
                                    list.add(map);
                                }

                                myAdapter = new MyAdapter(Paper.this,list);
                                recyclerView.setAdapter(myAdapter);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    date=jsonObject.getInt("date");
                    yesterday=date;
                     URL Url=new URL("https://news-at.zhihu.com/api/3/news/before/"+String.valueOf(yesterday));//获取服务器哦地址
                     HttpURLConnection UrlConnection = (HttpURLConnection) Url.openConnection();//双方建立连接
                    UrlConnection.setRequestMethod("GET");
                    InputStream inputStream1=UrlConnection.getInputStream();
                    Reader reader1=new InputStreamReader(inputStream1);
                    BufferedReader bufferedReader1=new BufferedReader(reader1);

                    while ((line1=bufferedReader1.readLine())!=null){//当temp读到的数据为空就结束
                        response1.append(line1);

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response1));
                                JSONArray jsonArray = jsonObject.getJSONArray("stories");
                                Map<String,Object> map1=new HashMap<>();
                                Integer day1=jsonObject.getInt("date");
                                map1.put("date",day1);
                                list.add(map1);
                                for (int i = 0; i < 6; i++) {
                                    Map<String,Object> map=new HashMap<>();
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                                    String title = jsonObject1.getString("title");
                                    String vicetitle = jsonObject1.getString("hint");
                                    String id=jsonObject1.getString("id");
                                    String url=jsonObject1.getString("url");
                                    JSONArray jsonArray1=jsonObject1.getJSONArray("images");
                                    String imageUrl=(String)jsonArray1.get(0);
                                    map.put("url",url);
                                    map.put("id",id);
                                    map.put("image",imageUrl);
                                    map.put("title", title);
                                    map.put("hint",vicetitle);
                                     list.add(map);


                                }


                                myAdapter = new MyAdapter(Paper.this,list);
                                recyclerView.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                Toast.makeText(Paper.this,"网络连接断开，请刷新重试",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                    });
                    inputStream.close();
                    reader.close();
                    bufferedReader.close();
                } catch (Exception e) {
                    list.clear();
                Map<String,Object> map=new HashMap<>();
                map.put("1",1);
                map.put("2",2);
                list.add(map);
                    myAdapter = new MyAdapter(Paper.this,list);
                    recyclerView.setAdapter(myAdapter);


                    e.printStackTrace();
//这里要有联网失败提示
                }



            }
        });
        thread.start();//启动线程
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            list.clear();
                final Thread thread=new Thread(new Runnable() {


                    @Override
                    public void run() {
                        final StringBuilder response = new StringBuilder();
                        final StringBuilder response1 = new StringBuilder();
                        String line;
                        String line1;
                        try {
                            URL url=new URL("https://news-at.zhihu.com/api/3/stories/latest");//获取服务器哦地址
                            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();//双方建立连接

                            urlConnection.setRequestMethod("GET");//给服务器发送请求
                            InputStream inputStream=urlConnection.getInputStream(); //字节流
                            Reader reader=new InputStreamReader(inputStream); //把字节流转化成字符流
                            BufferedReader bufferedReader=new BufferedReader(reader);//字符流 转成 缓冲流，一次可以读一行


                            while ((line=bufferedReader.readLine())!=null){//当temp读到的数据为空就结束
                                response.append(line);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                        date=jsonObject.getInt("date");
                                        int day=date%100;
                                        String days=String.format("%d",day);
                                        Date.setText(days);
                                        JSONArray jsonArray = jsonObject.getJSONArray("stories");
                                        for (int i = 0; i < 6; i++) {
                                            Map<String,Object> map=new HashMap<>();
                                            JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                                            String url=jsonObject1.getString("url");
                                            Integer day1=jsonObject.getInt("date");
                                            String title = jsonObject1.getString("title");
                                            String vicetitle = jsonObject1.getString("hint");
                                            String id=jsonObject1.getString("id");
                                            JSONArray jsonArray1=jsonObject1.getJSONArray("images");
                                            String imageUrl=(String)jsonArray1.get(0);
                                            map.put("day",0);
                                            map.put("url",url);
                                            map.put("id",id);
                                            map.put("image",imageUrl);
                                            map.put("title", title);
                                            map.put("hint",vicetitle);
                                            list.add(map);


                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            date=jsonObject.getInt("date");
                            yesterday=date;
                            URL Url=new URL("https://news-at.zhihu.com/api/3/news/before/"+String.valueOf(yesterday));//获取服务器哦地址
                            HttpURLConnection UrlConnection = (HttpURLConnection) Url.openConnection();//双方建立连接
                            UrlConnection.setRequestMethod("GET");
                            InputStream inputStream1=UrlConnection.getInputStream();
                            Reader reader1=new InputStreamReader(inputStream1);
                            BufferedReader bufferedReader1=new BufferedReader(reader1);

                            while ((line1=bufferedReader1.readLine())!=null){//当temp读到的数据为空就结束
                                response1.append(line1);

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(String.valueOf(response1));
                                        JSONArray jsonArray = jsonObject.getJSONArray("stories");
                                        Map<String,Object> map1=new HashMap<>();
                                        Integer day1=jsonObject.getInt("date");
                                        map1.put("date",day1);
                                        list.add(map1);
                                        for (int i = 0; i < 6; i++) {
                                            Map<String,Object> map=new HashMap<>();
                                            JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                                            String title = jsonObject1.getString("title");
                                            String vicetitle = jsonObject1.getString("hint");
                                            String id=jsonObject1.getString("id");
                                            JSONArray jsonArray1=jsonObject1.getJSONArray("images");
                                            String url=jsonObject1.getString("url");
                                            String imageUrl=(String)jsonArray1.get(0);
                                            map.put("url",url);
                                            map.put("id",id);
                                            map.put("image",imageUrl);
                                            map.put("title", title);
                                            map.put("hint",vicetitle);
                                            list.add(map);


                                        }
                                        myAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            inputStream.close();
                            reader.close();
                            bufferedReader.close();
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("1",1);
                                    map.put("2",2);
                                    list.add(map);
                                    myAdapter = new MyAdapter(Paper.this,list);
                                    recyclerView.setAdapter(myAdapter);
                                }
                            });
                            e.printStackTrace();
//这里要有联网失败提示
                        }



                    }
                });
                thread.start();//启动线程

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                final Thread thread=new Thread(new Runnable() {


                    @Override
                    public void run() {

                        final StringBuilder response1 = new StringBuilder();
                        String line1;
                        try {
                           yesterday=yesterday-1;

                            URL Url=new URL("https://news-at.zhihu.com/api/3/news/before/"+String.valueOf(yesterday));//获取服务器哦地址
                            HttpURLConnection UrlConnection = (HttpURLConnection) Url.openConnection();//双方建立连接
                            UrlConnection.setRequestMethod("GET");
                            InputStream inputStream1=UrlConnection.getInputStream();
                            Reader reader1=new InputStreamReader(inputStream1);
                            BufferedReader bufferedReader1=new BufferedReader(reader1);

                            while ((line1=bufferedReader1.readLine())!=null){//当temp读到的数据为空就结束
                                response1.append(line1);

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(String.valueOf(response1));
                                        JSONArray jsonArray = jsonObject.getJSONArray("stories");
                                        Map<String,Object> map1=new HashMap<>();
                                        Integer day1=jsonObject.getInt("date");
                                        map1.put("date",day1);
                                        list.add(map1);
                                        for (int i = 0; i < 6; i++) {
                                            Map<String,Object> map=new HashMap<>();
                                            JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                                            String title = jsonObject1.getString("title");
                                            String vicetitle = jsonObject1.getString("hint");
                                            String url=jsonObject1.getString("url");
                                            String id=jsonObject1.getString("id");
                                            JSONArray jsonArray1=jsonObject1.getJSONArray("images");
                                            String imageUrl=(String)jsonArray1.get(0);
                                            map.put("url",url);
                                            map.put("id",id);
                                            map.put("image",imageUrl);
                                            map.put("title", title);
                                            map.put("hint",vicetitle);
                                            list.add(map);
                                        }


                                        myAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            inputStream1.close();
                            reader1.close();
                            bufferedReader1.close();
                        } catch (Exception e) {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   list.clear();
                                   Map<String,Object> map=new HashMap<>();
                                   map.put("1",1);
                                   map.put("2",2);
                                   list.add(map);
                                   myAdapter = new MyAdapter(Paper.this,list);
                                   recyclerView.setAdapter(myAdapter);
                               }
                           });

                            e.printStackTrace();
//这里要有联网失败提示
                        }



                    }
                });
                thread.start();//启动线程

            }
        });


}}