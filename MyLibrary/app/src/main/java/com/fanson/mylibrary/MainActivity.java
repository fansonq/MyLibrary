package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.image.OnUniversalListener;
import com.example.fansonlib.image.OnUniversalProgress;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView iv_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        iv_pic = (ImageView)findViewById(R.id.iv_pic);
//        String picUrl = "http://img02.tooopen.com/images/20140504/sy_60294738471.jpg";
////        ImageLoaderUtils.loadCircleImage(this,iv_pic,picUrl);
//        ImageLoaderUtils.loadImageWithListener(this, iv_pic, picUrl, new OnUniversalListener() {
//            @Override
//            public void loadStart() {
//                Log.d(TAG, "loadStart");
//            }
//
//            @Override
//            public void loadSuccess() {
//                Log.d(TAG, "loadSuccess");
//            }
//
//            @Override
//            public void loadFailed() {
//                Log.d(TAG, "loadFailed");
//            }
//        }, new OnUniversalProgress() {
//            @Override
//            public void onProgressUpdate(int current, int total) {
//                Log.d(TAG,"onProgress :　"+current%total);
//            }
//        });
    }
}
