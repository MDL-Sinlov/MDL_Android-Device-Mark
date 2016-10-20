package mdl.sinlov.andorid.device_mark;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

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

    /**
     * 获取设备IP地址
     *
     * @return
     */
    public static String getMachineIpAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        String ipAddress = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface iF = null;
            while (interfaces.hasMoreElements()) {
                iF = interfaces.nextElement();
                Enumeration<InetAddress> addressEnumeration = iF.getInetAddresses();
                InetAddress address = addressEnumeration.nextElement();
                ipAddress = bytesToString(address.getAddress());
                if (ipAddress == null) continue;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
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
     * need open WIFI add at uses-permission android:name="android.permission.ACCESS_WIFI_STATE"
     *
     * @return {@link String}
     */
    public static String getDeviceIDByMac() {
        if (!TextUtils.isEmpty(wifiMac)) {
            return wifiMac;
        }

        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (true) {
                String str = input.readLine();
                if (str != null) {
                    wifiMac = str.trim();
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        if (TextUtils.isEmpty(wifiMac)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toLowerCase().substring(0, 17);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return wifiMac;
    }

    private static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
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
