package com.fanson.mylibrary.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.fanson.mylibrary.R;

/**
 * @author Created by：Fanson
 * Created Time: 2018/10/18 10:12
 * Describe：
 */
public class ExampleActivity extends AppCompatActivity{

    private ImageView mIvPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mIvPhoto = findViewById(R.id.iv_cover);

//        GlideUtils.loadImageView(this,"url",mIvPhoto);
    }
}
