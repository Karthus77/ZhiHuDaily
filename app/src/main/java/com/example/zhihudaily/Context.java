package com.example.zhihudaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import java.util.HashMap;
import java.util.Map;

public class Context extends AppCompatActivity {
    private TextView detail;
    private TextView number;
    private ImageView bigImage;
    private ImageView back;
    private ImageView next;
    String image;
    String commentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_context);

        next=findViewById(R.id.comment);
        number=findViewById(R.id.commentNumber);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent it2=getIntent();
        final Bundle bd=it2.getExtras();
        String url=bd.getString("url");
        final String id=bd.getString("id");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Context.this,comment.class);
                Bundle bd1=new Bundle();
                bd1.putString("id",id);
                intent1.putExtras(bd1);
                startActivity(intent1);

            }
        });

        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                final StringBuilder response1 = new StringBuilder();
                String line;
                String line1;
                try {
                    URL url=new URL("https://news-at.zhihu.com/api/3/story/"+id);
                    HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();//双方建立连接
                    urlConnection.setRequestMethod("GET");//给服务器发送请求
                    InputStream inputStream=urlConnection.getInputStream(); //字节流
                    Reader reader=new InputStreamReader(inputStream); //把字节流转化成字符流
                    BufferedReader bufferedReader=new BufferedReader(reader);//字符流 转成 缓冲流，一次可以读一行
                    URL Url=new URL("https://news-at.zhihu.com/api/3/story-extra/"+id);//获取服务器哦地址
                    HttpURLConnection UrlConnection = (HttpURLConnection) Url.openConnection();//双方建立连接
                    UrlConnection.setRequestMethod("GET");
                    InputStream inputStream1=UrlConnection.getInputStream();
                    Reader reader1=new InputStreamReader(inputStream1);
                    BufferedReader bufferedReader1=new BufferedReader(reader1);

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
                                image =jsonObject.getString("image");
                                JSONObject jsonObject1=new JSONObject(String.valueOf(response1));
                                commentNumber=jsonObject1.getString("comments");
                                number.setText(commentNumber);

                            } catch (JSONException e) {
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
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}