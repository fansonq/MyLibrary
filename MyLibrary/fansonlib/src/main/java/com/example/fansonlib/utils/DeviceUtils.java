package com.example.fansonlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.fansonlib.base.AppUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author Created by：Fanson
 * Created Time: 2018/5/15 9:32
 * Describe：获取设备信息的工具类
 */

public class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设备是否root过
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {

        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备系统版本号（例：6.0）
     *
     * @return 设备系统版本号
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备系统版本码（例：23）
     *
     * @return 设备系统版本码
     */
    public static int getSDKVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备 AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        String id = Settings.Secure.getString(
                AppUtils.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        return id == null ? "" : id;
    }

    /**
     * 获取设备 MAC 地址
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return MAC 地址
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "please open wifi";
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getMacAddressByWifiInfo() {
        try {
            Context context = AppUtils.getAppContext().getApplicationContext();
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    return info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) {
                            return inetAddress;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备厂商
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取设备 ABIs
     *
     * @return 设备 ABIs
     */
    public static String[] getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS;
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return new String[]{Build.CPU_ABI};
        }
    }

    /**
     * 获取IMEI
     *
     * @param context 上下文
     * @return IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String strImei = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT > 23) {
            strImei = telephonyManager.getDeviceId(0);
        } else {
            strImei = telephonyManager.getDeviceId();
        }
        return strImei;
    }

    /**
     * 通过反射获取电信卡的IMEI
     *
     * @param context 上下文
     * @return
     */
    public static String getMachineImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (manager == null) {
            return "";
        }
        Class clazz = manager.getClass();
        String imei = "";
        try {
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
            getImei.setAccessible(true);
            imei = (String) getImei.invoke(manager);
        } catch (Exception e) {
        }
        return imei;
    }

    /**
     * 关机
     * <p>需要Root权限
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     */
    public static void shutdown() {
        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        AppUtils.getAppContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 重启设备
     * <p>需要Root权限
     * or hold {@code android:sharedUserId="android.uid.system"} in manifest.</p>
     */
    public static void reboot() {
        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        AppUtils.getAppContext().sendBroadcast(intent);
    }

    /**
     * Reboot the device.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to
     *               request special boot modes, or null.
     */
    public static void reboot(final String reason) {
        PowerManager mPowerManager =
                (PowerManager) AppUtils.getAppContext().getSystemService(Context.POWER_SERVICE);
        try {
            if (mPowerManager == null) {
                return;
            }
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取电量信息
     *
     * @param context 上下文
     * @return 电量(int类型)
     */
    public static int getBattery(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryInfoIntent == null ? 0 : batteryInfoIntent.getIntExtra("level", 0);
    }

    /**
     * 获取温度
     *
     * @param context 上下文
     * @return 温度(int类型)
     */
    public static int getTemperature(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryInfoIntent == null ? 0 : batteryInfoIntent.getIntExtra("temperature", 0);
    }

    /**
     * 获取电压
     *
     * @param context 上下文
     * @return 电压(int类型)
     */
    public static int getVoltage(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryInfoIntent == null ? 0 : batteryInfoIntent.getIntExtra("voltage", 0);
    }

    /**
     * 获取电池健康状态
     *
     * @param context 上下文
     * @return 电池健康状态
     */
    public static String getBatteryHealth(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext()
                .registerReceiver(null,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        String batteryTemp = "1";
        if (batteryInfoIntent == null) {
            return batteryTemp;
        }
        int health = batteryInfoIntent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                // 未知错误
                batteryTemp = "1";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                // 状态良好
                batteryTemp = "2";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                // 电池过热
                batteryTemp = "3";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                // 电池没有电
                batteryTemp = "4";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                // 电池电压过高
                batteryTemp = "5";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                // 电池健康未指明的失败
                batteryTemp = "6";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                // 电池冷
                batteryTemp = "7";
                break;
            default:
                break;
        }
        return batteryTemp;
    }

    /**
     * 获取电池状态
     *
     * @param context 上下文
     * @return 电池状态
     */
    public static String getBatteryStatus(Context context) {
        String batteryStatus = "1";
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryInfoIntent == null) {
            return batteryStatus;
        }
        int status = batteryInfoIntent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
        switch (status) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                //  未知道状态
                batteryStatus = "1";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                // 充电状态
                batteryStatus = "2";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // 放电状态
                batteryStatus = "3";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                // 未充电
                batteryStatus = "4";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // 充满电
                batteryStatus = "5";
                break;
            default:
                break;
        }
        return batteryStatus;
    }

    /**
     * 获取Cpu最高频
     *
     * @return Cpu最高频
     */
    public static String getCpuMaxFreq() {
        StringBuilder result = new StringBuilder();
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result.append(new String(re));
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = new StringBuilder("N/A");
        }
        return result.toString().trim();
    }

    /**
     * 获取Cpu名称
     *
     * @return Cpu名称
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Cpu数量
     *
     * @return Cpu数量
     */
    public static int getCpuNum() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    /**
     * 是否单Sim卡
     *
     * @param context 上下文
     * @return 是否单Sim卡
     */
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telMgr == null){
            return false;
        }
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
            default:
                break;
        }
        return result ;
    }

    /**
     * 是否是模拟器
     * @param context 上下文
     * @return true/false
     */
    public static boolean isEmulator(Context context) {
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        // 是否可以处理跳转到拨号的 Intent
        intent.setAction(Intent.ACTION_DIAL);
        boolean canResolveIntent = intent.resolveActivity(context.getPackageManager()) != null;
        boolean result = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || !canResolveIntent;
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        if ( telephonyManager != null){
            result = result|| ("android").equals(telephonyManager.getNetworkOperatorName().toLowerCase());
        }
        return result;
    }

    /**
     * 获取Cpu信息
     * @return Cpu信息
     */
    public static String getCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuilder strBuffer = new StringBuilder();
            String readLine ;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                strBuffer.append(readLine);
            }
            responseReader.close();
            result = strBuffer.toString().toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

}




