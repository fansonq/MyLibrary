package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.fansonlib.image.ImageLoaderUtils;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_pic = (ImageView)findViewById(R.id.iv_pic);
        String picUrl = "http://202.175.82.224:22024/TaxiGo-Server/images/public/ads_2x.jpg";
        ImageLoaderUtils.loadImage(this,iv_pic,picUrl);
    }
}
