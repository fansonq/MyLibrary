package com.example.fansonlib.utils.log;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/22 14:58
 * Describe：Logan日志的解析方法
 * <p> 用法实例 new LoganParser(Key16.getBytes(),Iv16.getBytes()).parse(“加密日志文件的输入流”, “输出流就是解密明文”);</>
 */
public class LoganParser {

    private static final String TAG = LoganParser.class.getSimpleName();
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_TYPE = "AES/CBC/NoPadding";
    private Cipher mDecryptCipher;
    /**
     * 128位ase加密Key
     */
    private byte[] mEncryptKey16;
    /**
     * 128位aes加密IV
     */
    private byte[] mEncryptIv16;

    public LoganParser(byte[] encryptKey16, byte[] encryptIv16) {
        mEncryptKey16 = encryptKey16;
        mEncryptIv16 = encryptIv16;
        initEncrypt();
    }

    private void initEncrypt() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(mEncryptKey16, ALGORITHM);
        try {
            mDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            mDecryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(mEncryptIv16));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     *  解析日志
     * @param is 加密日志文件的输入流
     * @param os 输出流就是解密明文
     */
    public void parse(InputStream is, OutputStream os) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int t = 0;
        try {
            while ((t = is.read(buffer)) >= 0) {
                bos.write(buffer, 0, t);
                bos.flush();
            }
            byte[] content = bos.toByteArray();
            for (int i = 0; i < content.length; i++) {
                byte start = content[i];
                if (start == '\1') {
                    i++;
                    int length = (content[i] & 0xFF) << 24 |
                            (content[i + 1] & 0xFF) << 16 |
                            (content[i + 2] & 0xFF) << 8 |
                            (content[i + 3] & 0xFF);
                    i += 3;
                    int type;
                    if (length > 0) {
                        int temp = i + length + 1;
                        //异常
                        if (content.length - i - 1 == length) {
                            type = 0;
                        } else if (content.length - i - 1 > length && '\0' == content[temp]) {
                            type = 1;
                        } else if (content.length - i - 1 > length && '\1' == content[temp]) {
                            //异常
                            type = 2;
                        } else {
                            i -= 4;
                            continue;
                        }
                        byte[] dest = new byte[length];
                        System.arraycopy(content, i + 1, dest, 0, length);
                        ByteArrayOutputStream uncompressBytesArray = new ByteArrayOutputStream();
                        InflaterInputStream inflaterOs = null;
                        byte[] uncompressByte;
                        try {
                            uncompressBytesArray.reset();
                            inflaterOs = new GZIPInputStream(new CipherInputStream(new ByteArrayInputStream(dest), mDecryptCipher));
                            int e = 0;
                            while ((e = inflaterOs.read(buffer)) >= 0) {
                                uncompressBytesArray.write(buffer, 0, e);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        uncompressByte = uncompressBytesArray.toByteArray();
                        uncompressBytesArray.reset();
                        os.write(uncompressByte);

                        if (inflaterOs != null) {
                            inflaterOs.close();
                        }
                        i += length;
                        if (type == 1) {
                            i++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG,os.toString());
    }
}
