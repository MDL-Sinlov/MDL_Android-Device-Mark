package mdl.sinlov.andorid.device_mark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * for APP create device UUID
 * <pre>
 *     sinlov
 *
 *     /\__/\
 *    /`    '\
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!
 *    \  --  /
 *   /        \
 *  /          \
 * |            |
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * </pre>
 * Created by "sinlov" on 16/9/1.
 */
public class DeviceIDFactory {

    private static StringBuilder deviceId;
    private static String uuid32;
    private static String wifiMac;
    private static String imei;
    private static String sn;

    /**
     * this info will mark by &key#value by wifi imei sn uuid
     *
     * @param context    {@link Context}
     * @param channelTag {@link String}
     * @return {@link String}
     */
    public static String getDeviceId(Context context, String channelTag) {
        if (null == deviceId) {
            deviceId = new StringBuilder();
        } else {
            deviceId.setLength(0);
        }
        String wifiMac = getDeviceIDByMac();
        if (!TextUtils.isEmpty(wifiMac)) {
            deviceId.append("&wifi#");
            deviceId.append(wifiMac);
        }
        String imei = getDeviceIDByIMEI(context);
        if (!TextUtils.isEmpty(imei)) {
            deviceId.append("&imei#");
            deviceId.append(imei);
        }
        String sn = getDeviceIDBySN(context);
        if (!TextUtils.isEmpty(sn)) {
            deviceId.append("&sn#");
            deviceId.append(sn);
        }
        String uuid = getUUID(context);
        if (!TextUtils.isEmpty(uuid)) {
            deviceId.append("&uuid#");
            deviceId.append(uuid);
        }
        deviceId.append(channelTag);
        return deviceId.toString();
    }

    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * typ get by {@link #getDeviceIDByMac()} first
     * if not has use {@link #getDeviceIDByMac(Context)}
     * even return empty or 02:00:00:00:00:00 return {@link #getUUID(Context)}
     *
     * @param context
     * @return
     */
    public static String getDeviceIDByMac(Context context) {
        if (!TextUtils.isEmpty(wifiMac)) {
            return wifiMac;
        }
        wifiMac = getDeviceIDByMac();
        if (TextUtils.isEmpty(wifiMac)) {
            wifiMac = getMacAddressFromWifiManager(context);
        }
        if (TextUtils.isEmpty(wifiMac)) {
            wifiMac = getUUID(context);
        } else {
            if (wifiMac.equals("02:00:00:00:00:00")) {
                wifiMac = getUUID(context);
            }
        }
        return wifiMac;
    }

    /**
     * need open WIFI add at uses-permission android:name="android.permission.ACCESS_WIFI_STATE"
     * first read getMacAddressByIP
     * second read getDevicesHardwareAddress
     * finally read readMacAddressByLinuxClassNet
     *
     * @return {@link String} this will return null
     */
    public static String getDeviceIDByMac() {
        wifiMac = getMacAddressByIP();
        if (TextUtils.isEmpty(wifiMac)) {
            wifiMac = getDevicesHardwareAddress();
        }
        if (TextUtils.isEmpty(wifiMac)) {
            wifiMac = readMacAddressByLinuxClassNet();
        }
        return wifiMac;
    }

    /**
     * get mac address by IP
     *
     * @return this will return null
     */
    public static String getMacAddressByIP() {
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            Log.w("MacAddressByIP", "error", e);
        }
        return strMacAddr;
    }

    /**
     * 获取本地IP
     *
     * @return this will return null
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 移动设备本地IP,得到一个ip地址的列表
     *
     * @return {@link InetAddress} this will return null
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":"))
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取设备HardwareAddress地址,扫描各个网络接口
     *
     * @return this will return null
     */
    public static String getDevicesHardwareAddress() {
        String hardWareAddress = null;
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                NetworkInterface iF;
                while (interfaces.hasMoreElements()) {
                    iF = interfaces.nextElement();
                    hardWareAddress = bytes2String(iF.getHardwareAddress());
                    if (hardWareAddress != null)
                        break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hardWareAddress;
    }

    private static String bytes2String(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Android 6.0
     * cat /sys/class/net/wlan0/address is wlan0
     * /sys/class/net/eth0/address is eth0
     *
     * @return this will return null
     */
    private static String readMacAddressByLinuxClassNet() {
        String readMac = null;
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = null;
            while ((line = input.readLine()) != null) {
                readMac = line.trim();
                break;
            }
        } catch (Exception ex) {
            try {
                readMac = loadFileAsString("/sys/class/net/eth0/address")
                        .toLowerCase().substring(0, 17);
            } catch (IOException e) {
                Log.w("MacByLinuxClassNet", "error", e);
            }
        }
        return readMac;
    }

    private static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * android 6.0以下, 部分 4.4 设备或者模拟器也出问题，建议实在获取不到时使用
     * android  6.0之后 移除了通过 WiFi 和蓝牙 API 来在应用程序中可编程的访问本地硬件标示符
     * WifiInfo.getMacAddress() 和 BluetoothAdapter.getAddress() 方法都将返回 02:00:00:00:00:00
     *
     * @param context {@link Context}
     * @return 注意会取到 02:00:00:00:00:00
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressFromWifiManager(Context context) {
        String macAddress = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                @SuppressLint("WifiManagerPotentialLeak") WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo info = wifi.getConnectionInfo();
                    if (info != null) {
                        macAddress = info.getMacAddress();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macAddress;
    }

    /**
     * need open WIFI add at uses-permission android:name="android.permission.READ_PHONE_STATE"
     *
     * @param context {@link Context}
     * @return {@link String}
     */
    public static String getDeviceIDByIMEI(Context context) {
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /**
     * need open WIFI add at uses-permission android:name="android.permission.READ_PHONE_STATE"
     *
     * @param context {@link Context}
     * @return {@link String}
     */
    public static String getDeviceIDBySN(Context context) {
        if (!TextUtils.isEmpty(sn)) {
            return sn;
        }
        String or_serialon = DeviceBaseInfo.getSystemProperties(DeviceBaseInfo.OR_SERIALON);
        if (TextUtils.isEmpty(or_serialon)) {
            sn = DeviceBaseInfo.getSystemProperties(DeviceBaseInfo.OR_BOOT_SERIALON);
        }
        sn = or_serialon;
        return sn;
    }


    /**
     * get uuid size 32
     *
     * @param context {@link Context}
     * @return {@link String}
     */
    public static String getUUID(Context context) {
        return null == uuid32 ? uuid32 = new InstallUuidFactory(context).getInstallUuid().toString().replace("-", "") : uuid32;
    }

    private DeviceIDFactory() {
    }
}
