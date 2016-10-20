package mdl.sinlov.andorid.device_mark;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Device Base info
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
 * Created by sinlov on 16/5/5.
 */
public class DeviceBaseInfo {

    /**
     * Device Model
     */
    public static final String BUILD_MODEL = "ro.product.model";

    /**
     * Brand of mobile phone manufacturers
     */
    public static final String BUILD_MANUFACTURER = "ro.product.manufacturer";

    /**
     * vm flag ro.hardware
     */
    public static final String OR_HARDWARE = "ro.hardware";

    /**
     * boot serial no
     */
    public static final String OR_BOOT_SERIALON = "ro.boot.serialno";

    /**
     * serial no
     */
    public static final String OR_SERIALON = "ro.serialno";

    /**
     * Emulator android google simuator use {@link #OR_HARDWARE} get
     */
    public static final String EMULATOR_GOLDFISH = "goldfish";

    /**
     * Emulator Genymotion use {@link #OR_HARDWARE} get
     */
    public static final String EMULATOR_GENYMOTION = "vbox86";
    /**
     * Emulator BlueStacks use {@link #OR_HARDWARE} get
     */
    public static final String EMULATOR_BLUESTACKS = "smdk4x12";

    /**
     * real phone qualcomm use {@link #OR_HARDWARE} get
     */
    public static final String REAL_Qualcomm = "qcom";
    /**
     * real phone MTK begin with 'mt', such as 'mt6795' use {@link #OR_HARDWARE} get
     */
    public static final String REAL_MTK = "mt";

    /**
     * get system properties by android.os.SystemProperties
     *
     * @param propertyName {@link String}
     * @return {@link String}
     */
    public static String getSystemProperties(String propertyName) {
        String value = "";
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method method = clazz.getMethod("get", paramTypes);

            Object object = clazz.newInstance();
            Object[] propertyNames = new Object[1];
            propertyNames[0] = propertyName;
            value = (String) method.invoke(object, propertyNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * get device cpu info
     *
     * @param context {@link Context}
     * @return true is emulator
     */
    public static String getProcCPUInfo(Context context) {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getDeviceHardwareInfoList() {
        StringBuffer sb = new StringBuffer();
        sb.append(BUILD_MODEL);
        sb.append(" - ");
        sb.append(getSystemProperties(BUILD_MODEL));
        sb.append("\n");
        sb.append(BUILD_MANUFACTURER);
        sb.append(" - ");
        sb.append(getSystemProperties(BUILD_MANUFACTURER));
        sb.append("\n");
        sb.append(OR_HARDWARE);
        sb.append(" - ");
        sb.append(getSystemProperties(OR_HARDWARE));
        sb.append("\n");
        return sb.toString();
    }

    public static String getAndroidID(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidID)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            androidID = tm.getDeviceId();
        }
        return androidID;
    }
}