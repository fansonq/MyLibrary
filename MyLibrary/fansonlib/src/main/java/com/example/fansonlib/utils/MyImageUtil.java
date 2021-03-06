package com.example.fansonlib.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.fansonlib.utils.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by：fanson
 * Created on：2017/5/5 13:29
 * Describe：图片工具类
 */

public class MyImageUtil {

    private static String saveDir ; // 图片保存地址

    private volatile static MyImageUtil mImageUtil;

    private Context mContext;

    public MyImageUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    public static MyImageUtil getImageUtil(Context context) {
        if (mImageUtil == null) {
            synchronized (MyImageUtil.class) {
                if (mImageUtil == null) {
                    mImageUtil = new MyImageUtil(context);
                }
            }
        }
        return mImageUtil;
    }

    /**
     * 文件转化为而二进制流byte[]
     * @param filePath
     * @return
     * @throws Exception
     */
    public byte[] filePath2Bytes(String filePath) throws Exception {
        FileInputStream fs = new FileInputStream(filePath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        fs.close();
        return outputStream.toByteArray();
    }

    /**
     * Drawable转换成byte[]
     */
    public byte[] drawable2Bytes(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.bitmap2Bytes(bitmap);
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */

    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换成byte[]
     *
     * @param bm
     * @return
     */
    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转换成Drawable
     *
     * @param b
     * @return
     */
    public Drawable bytes2Drawable(byte[] b) {
        Bitmap bitmap = this.bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }

    /**
     * byte[]转换成Bitmap
     *
     * @param b
     * @return
     */
    public Bitmap bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * Bitmap转换成Drawable
     *
     * @param bitmap
     * @return
     */
    public Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(mContext.getResources(), bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    /**
     * 将byte[]转换成InputStream
     *
     * @param b
     * @return
     */
    public InputStream byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * 将InputStream转换成byte[]
     *
     * @param is
     * @return
     */
    public byte[] inputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param bm
     * @return
     */
    public InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 将InputStream转换成Bitmap
     *
     * @param is
     * @return
     */
    public Bitmap inputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    /**
     * Drawable转换成InputStream
     *
     * @param d
     * @return
     */
    public InputStream drawable2InputStream(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.bitmap2InputStream(bitmap);
    }

    /**
     * InputStream转换成Drawable
     *
     * @param is
     * @return
     */
    public Drawable inputStream2Drawable(InputStream is) {
        Bitmap bitmap = this.inputStream2Bitmap(is);
        return this.bitmap2Drawable(bitmap);
    }

    /**
<<<<<<< HEAD
     * 设置图片保存的位置
     * @param dir 路径
     */
    public static void setSaveDir(String dir){
        saveDir = Environment.getExternalStorageDirectory() + dir;
    }

    /**
     * 保存图片并插入图库
     * @param context
     * @param bmp
     * @return
     */
    public static boolean saveImageToGalley(Context context, Bitmap bmp){
        String fileName =System.currentTimeMillis()+".jpg";
        File file = new File(saveDir,fileName);
        if (!file.exists()){
            FileUtils.createFile(file);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return insertImage(context, file.getAbsolutePath(), fileName, null);
    }

    /**
     * 图片文件插入系统图库
     *
     * @param context
     * @param imagePath
     * @param name
     * @param description
     * @return
     */
    public static boolean insertImage(Context context, String imagePath, String name, String description) {
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, name, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath)));
        return true;
    }

    /**
     * 通过Uri获取图片的path路径
     * @param context 上下文
     * @param path 路径
     * @return 图片的path路径
     */
    public Uri getImageContentUri(Context context,String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

    /**
     * 获取资源文件中图片的Uri
     * @param context 上下文
     * @param resourceId 资源ID
     * @return 资源文件中图片的Uri
     */
    public String getResourceUri(Context context,int resourceId){
        Resources resources = context.getResources();
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(resourceId) + "/" +
                resources.getResourceTypeName(resourceId) + "/" +
                resources.getResourceEntryName(resourceId);
    }

    /**
     * 保存图片资源Bitmap到相册
     * @param context 上下文
     * @param bmp 图片资源Bitmap
     * @param saveFileDir 图片存放路径名
     * @return 是否保存成功，true/false
     */
    public  boolean saveImageToGallery(Context context, Bitmap bmp,String saveFileDir) {
        boolean result = true;
        //注意小米手机必须这样获得public绝对路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File appDir = new File(file ,saveFileDir);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    currentFile.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
        return result;
    }

}
