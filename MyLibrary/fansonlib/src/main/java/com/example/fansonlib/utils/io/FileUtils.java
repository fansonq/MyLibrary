package com.example.fansonlib.utils.io;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.fansonlib.base.AppUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by：fanson
 * Created on：2017/8/17 13:35
 * Describe：文件工具类
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath() {
        String cacheRootPath = "";
        if (StorageUtils.isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = AppUtils.getAppContext().getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = AppUtils.getAppContext().getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        // 这个sd卡中文件路径下的内容会随着，程序卸载或者设置中清除缓存后一起清空
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 重命名
     *
     * @param filePath
     * @return
     */
    public static boolean rename(String filePath, String newFilePath) {
        Log.d(TAG, "" + filePath);
        Log.d(TAG, "" + newFilePath);

        if (!TextUtils.isEmpty(filePath)) {
            final File file = new File(filePath);
            final File newFile = new File(newFilePath);
            if (file.exists()) {
                return file.renameTo(newFile);
            }
        }
        return false;
    }

    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                Log.i(TAG,"----- 创建文件"+file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                Log.i(TAG,"----- 创建文件"+file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                Log.i(TAG,"----- 创建文件夹"+file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                Log.i(TAG,"----- 创建文件夹"+file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        Log.d(TAG, "" + filePath);
        if (!TextUtils.isEmpty(filePath)) {
            final File file = new File(filePath);
            Log.d(TAG, "deleteFile path exists " + file.exists());
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 删除文件夹内所有文件
     *
     * @param delpath delpath path of file
     * @return boolean the result
     */
    public static boolean deleteAllFile(String delpath) {
        try {
            // create file
            final File file = new File(delpath);

            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {

                final String[] filelist = file.list();
                final int size = filelist.length;
                for (int i = 0; i < size; i++) {

                    // create new file
                    final File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        // digui
                        deleteFile(delpath + "/" + filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile     源文件File
     * @param destDir     目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    public static long copyFile(final File srcFile, final File destDir, String newFileName) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            Log.d(TAG, "源文件不存在");

            copySizes = -1;
        } else if (!destDir.exists()) {
            Log.d(TAG, "目标目录不存在");

            copySizes = -1;
        } else if (newFileName == null) {
            Log.d(TAG, "文件名为null");
            copySizes = -1;
        } else {
            FileChannel fcin = null;
            FileChannel fcout = null;
            try {
                fcin = new FileInputStream(srcFile).getChannel();
                fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                copySizes = size;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fcin != null) {
                        fcin.close();
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySizes;
    }

}
