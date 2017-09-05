package mdl.sinlov.android.demo.device_mark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mdl.sinlov.andorid.device_mark.DeviceBaseInfo;
import mdl.sinlov.andorid.device_mark.DeviceIDFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    @BindView(R.id.tv_main_device_id)
    TextView tvMainDeviceId;
    @BindView(R.id.tv_main_uuid)
    TextView tvMainUuid;
    @BindView(R.id.tv_main_device_android_id)
    TextView tvMainDeviceAndroidId;
    @BindView(R.id.tv_main_device_hard_info_list)
    TextView tvMainDeviceHardInfoList;
    @BindView(R.id.tv_main_device_ip)
    TextView tvMainDeviceIp;
    @BindView(R.id.tv_main_device_mac)
    TextView tvMainDeviceMac;
    @BindView(R.id.tv_main_device_mac_safe)
    TextView tvMainDeviceMacSafe;
    @BindView(R.id.tv_main_device_sn)
    TextView tvMainDeviceSn;
    @BindView(R.id.tv_main_device_imei)
    TextView tvMainDeviceImei;
    @BindView(R.id.tv_main_device_cpu_info)
    TextView tvMainDeviceCpuInfo;
    @BindView(R.id.btn_main_get_info)
    Button btnMainGetInfo;
    @BindView(R.id.activity_main)
    ScrollView activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void initDeviceInfo() {
        Context aCtx = this.getApplicationContext();
        String uuid = "uuid:\n" + DeviceIDFactory.getUUID(aCtx);
        String imei = "imei:\n" + DeviceIDFactory.getDeviceIDByIMEI(aCtx);
        String sn = "sn:\n" + DeviceIDFactory.getDeviceIDBySN(aCtx);
        String ip = "local ip:\n" + DeviceIDFactory.getLocalIpAddress();
        String macSafe = "macSafe:\n" + DeviceIDFactory.getDeviceIDByMac(aCtx, true);
        String mac = "mac:\n" + DeviceIDFactory.getDeviceIDByMac();
        String deviceId = "deviceID:\n" + DeviceIDFactory.getDeviceId(aCtx, "");
        String androidID = "androidID:\n" + DeviceBaseInfo.getAndroidID(aCtx);
        String hardwareInfoList = "hardwareInfo:\n" + DeviceBaseInfo.getDeviceHardwareInfoList();
        String cpuInfo = "cpu info:\n" + DeviceBaseInfo.getProcCPUInfo(aCtx);
        tvMainDeviceAndroidId.setText(androidID);
        tvMainDeviceHardInfoList.setText(hardwareInfoList);
        tvMainUuid.setText(uuid);
        tvMainDeviceId.setText(deviceId);
        tvMainDeviceIp.setText(ip);
        tvMainDeviceMacSafe.setText(macSafe);
        tvMainDeviceMac.setText(mac);
        tvMainDeviceSn.setText(sn);
        tvMainDeviceImei.setText(imei);
        tvMainDeviceCpuInfo.setText(cpuInfo);
    }

    @OnClick(R.id.btn_main_get_info)
    public void onClick() {
        initDeviceInfo();
    }
}
