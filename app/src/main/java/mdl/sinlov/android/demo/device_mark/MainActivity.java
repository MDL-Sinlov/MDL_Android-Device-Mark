package mdl.sinlov.android.demo.device_mark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mdl.sinlov.andorid.device_mark.DeviceBaseInfo;
import mdl.sinlov.andorid.device_mark.DeviceIDFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main_device_id)
    TextView tvMainDeviceId;
    @BindView(R.id.tv_main_uuid)
    TextView tvMainUuid;
    @BindView(R.id.tv_main_device_android_id)
    TextView tvMainDeviceAndroidId;
    @BindView(R.id.tv_main_device_hard_info_list)
    TextView tvMainDeviceHardInfoList;
    @BindView(R.id.tv_main_device_mac)
    TextView tvMainDeviceMac;
    @BindView(R.id.tv_main_device_sn)
    TextView tvMainDeviceSn;
    @BindView(R.id.tv_main_device_imei)
    TextView tvMainDeviceImei;
    @BindView(R.id.tv_main_device_cpu_info)
    TextView tvMainDeviceCpuInfo;
    @BindView(R.id.tv_main_device_ip_address)
    TextView tvMainDeviceIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDeviceInfo();
    }

    private void initDeviceInfo() {
        Context aCtx = this.getApplicationContext();
        String uuid = "uuid:\n" + DeviceIDFactory.getUUID(aCtx);
        String imei = "imei:\n" + DeviceIDFactory.getDeviceIDByIMEI(aCtx);
        String sn = "sn:\n" + DeviceIDFactory.getDeviceIDBySN(aCtx);
        String mac = "mac:\n" + DeviceIDFactory.getDeviceIDByMac();
        String deviceId = "deviceID:\n" + DeviceIDFactory.getDeviceId(aCtx, "");
        String androidID = "androidID:\n" + DeviceBaseInfo.getAndroidID(aCtx);
        String hardwareInfoList = "hardwareInfo:\n" + DeviceBaseInfo.getDeviceHardwareInfoList();
        String cpuInfo = "cpu info:\n" + DeviceBaseInfo.getProcCPUInfo(aCtx);
        String ipAddress = "IP address:\n" + DeviceIDFactory.getMachineIpAddress();
        tvMainDeviceAndroidId.setText(androidID);
        tvMainDeviceHardInfoList.setText(hardwareInfoList);
        tvMainUuid.setText(uuid);
        tvMainDeviceId.setText(deviceId);
        tvMainDeviceMac.setText(mac);
        tvMainDeviceSn.setText(sn);
        tvMainDeviceImei.setText(imei);
        tvMainDeviceCpuInfo.setText(cpuInfo);
        tvMainDeviceIpAddress.setText(ipAddress);
    }
}
