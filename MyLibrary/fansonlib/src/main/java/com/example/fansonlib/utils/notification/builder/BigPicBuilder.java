package com.example.fansonlib.utils.notification.builder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.example.fansonlib.utils.notification.MyNotificationUtils;

/**
 * 带图片的建造
 */

public class BigPicBuilder extends BaseBuilder {
   private Bitmap bitmap;
    @DrawableRes
    int bigPic;


    public BigPicBuilder setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        return this;
    }


    public BigPicBuilder setPicRes(@DrawableRes int drawable){
        this.bigPic = drawable;
        return this;
    }

    @Override
    public void build() {
        super.build();
        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
        if(bitmap==null || bitmap.isRecycled()){
            if(bigPic >0){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = true;
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeResource(MyNotificationUtils.mContext.getResources(),
                        bigPic, options);
            }
        }
        picStyle.bigPicture(bitmap);
        //picStyle.bigLargeIcon(bitmap);
        picStyle.setBigContentTitle(contentTitle);
        picStyle.setSummaryText(summaryText);
        cBuilder.setStyle(picStyle);
    }
}
