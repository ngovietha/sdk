/*
 * Created by NQC on 4/26/20 3:15 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.measure.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.SpotCheck.SpotSendCMDThread;
import com.creative.SpotCheck.StatusMsg;
import com.nqc.idoctor.AppConfig;
import com.nqc.idoctor.BuildConfig;
import com.nqc.idoctor.R;
import com.nqc.idoctor.common.creative.bluetooth.ConnectActivity;
import com.nqc.idoctor.common.creative.bluetooth.MyBluetooth;
import com.nqc.idoctor.common.creative.recvdata.ReceiveService;
import com.nqc.idoctor.common.creative.recvdata.StaticReceive;
import com.nqc.idoctor.common.utils.LogUtils;
import com.nqc.idoctor.common.utils.ToastUtils;
import com.nqc.idoctor.common.utils.Utils;
import com.nqc.idoctor.common.view.activity.MainActivity;
import com.nqc.idoctor.common.view.base.BaseFragment;
import com.nqc.idoctor.databinding.FragmentMeasureBinding;
import com.nqc.idoctor.measure.view.navigator.MeasureNavigator;
import com.nqc.idoctor.measure.viewmodel.MeasureViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * LoginFragment screen
 */
public class MeasureFragment extends BaseFragment<MeasureViewModel, FragmentMeasureBinding>
        implements MeasureNavigator, View.OnClickListener {

    public static final String TAG = MeasureFragment.class.getSimpleName();


    /**
     * 绘制300柱状图线程
     */
    private Thread drawPC300SPO2RectThread;

    /**
     * 绘图线程
     */
    private Thread drawThread;

    /**
     * 电量等级
     */
    private int batteryRes[] = {R.drawable.battery_0, R.drawable.battery_1, R.drawable.battery_2,
            R.drawable.battery_3};

    /**
     * 电池充电状态
     */
    private int batteryRes_img[] = {R.drawable.battery_0_ing, R.drawable.battery_1_ing, R.drawable.battery_2_ing,
            R.drawable.battery_3_ing};

    /**
     * 血压测量错误结果
     */
    private String[] nibpERR;

    /**
     * 心电测量错误结果
     */
    private String[] ecgERR;

    private Resources myResources;

    private boolean bECGIng = false;
    private boolean bNIBPIng = false;
    private boolean bSpo2Ing = false;

    /**
     * 血糖单位: true->mmol/L ,false->mg/dl
     */
    private boolean bGluUnitMmol = true;

    private float mGLU_Mgdl = 0;
    private float mGLU_Mmol = 0;

    @SuppressLint("HandlerLeak")
    public MeasureFragment() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

//                LogUtils.d("msg.what: "+msg.what+", "+"msg.arg1: "+msg.arg1+", "+"msg.arg2: "+msg.arg2);
                switch (msg.what) {
                    case StaticReceive.MSG_DATA_DEVICE_SHUT:
                    case StaticReceive.MSG_DATA_DISCON: {
                        Toast.makeText(getContext(), R.string.connect_connect_off, Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case StaticReceive.MSG_DATA_DEVICE_ID: {
                    }
                    break;
                    case StaticReceive.MSG_DATA_BATTERY: {
                        setBattery(msg.arg1, msg.arg2);
                    }
                    break;
                    case StaticReceive.MSG_DATA_ECG_GAIN:
                        LogUtils.d("MSG_DATA_ECG_GAIN: " + msg.arg1 + ", " + "msg.arg2: " + msg.arg2);
                        dataBinding.realpalyPc300ViewDraw.setGain(msg.arg2);
                        break;
                    case StaticReceive.MSG_DATA_ECG_STATUS_CH: {

                        LogUtils.d("MSG_DATA_ECG_STATUS_CH: " + msg.arg1 + ", " + "msg.arg2: " + msg.arg2);
                        if (msg.arg1 == 0) {// 待机
                            bECGIng = true;
                            checkECGStatus(true);
                            setTVtext(dataBinding.realplayPc300TvMode, "ECG");
                            if (!bSpo2Ing) {
                                setPR("");
                            }
                        } else if (msg.arg1 == 1) {// Start
                            bECGIng = false;
                            checkECGStatus(false);
                            dataBinding.realpalyPc300ViewDraw.cleanWaveData();
                        } else if (msg.arg1 == 2) {// Stop
                            bECGIng = false;
                            checkECGStatus(false);
                            Toast.makeText(getContext(), ecgERR[msg.arg2], Toast.LENGTH_SHORT).show();
                            dataBinding.realpalyPc300ViewDraw.cleanWaveData();
                            if (!bSpo2Ing) {
                                setPR(String.valueOf(msg.obj));
                            }
                        }
                    }
                    break;
                    case StaticReceive.MSG_ECG_ARRAY: {
                        LogUtils.d("MSG_ECG_ARRAY: " + msg.arg1);
                        String dataECD = msg.getData().getString("MSG_ECG_ARRAY");
                        viewModel.postDataECG(dataECD);
                    }
                    break;
                    case StaticReceive.MSG_DATA_ECG_WAVE: {
                        LogUtils.d("MSG_DATA_ECG_WAVE: " + msg.arg1 + ", " + "msg.arg2: " + msg.arg2);
                        Bundle d = msg.getData();
                        if (!d.getBoolean("bLeadoff")) {
                            setTVtext(dataBinding.realplayPc300TvMsg, myResources.getString(R.string.measure_lead_off));
                        } else {
                            dataBinding.realplayPc300TvMsg.setText("");
                        }
                    }
                    break;
                    case StaticReceive.MSG_DATA_GLU: {
                        if (msg.arg2 == 0) { // 0：血糖单位 mmol/L
                            bGluUnitMmol = true;
                            if (msg.arg1 == 1) { //过低
                                setGLU("L");
                            } else if (msg.arg1 == 2) { //过高
                                setGLU("H");
                            } else if (msg.arg1 == 0) { //血糖值正常
                                String temp = String.valueOf((Float) msg.obj);
                                setGLU(temp);
                                //（mmol/L)转换为 ->（mg/dl）
                                mGLU_Mgdl = switchGLUUnit(false, (Float) msg.obj);
                            }
                            dataBinding.tvGluUnit.setText("mmol/L");

                        } else { // 1:mg/dL
                            bGluUnitMmol = false;
                            if (msg.arg1 == 1) {
                                setGLU("L");
                            } else if (msg.arg1 == 2) {
                                setGLU("H");
                            } else if (msg.arg1 == 0) {
                                String temp = String.valueOf((Float) msg.obj);
                                setGLU(temp);
                                // （mg/dl）转换为-> （mmol/L)
                                mGLU_Mmol = switchGLUUnit(true, (Float) msg.obj);

                            }
                            dataBinding.tvGluUnit.setText("mg/dl");
                        }

                    }
                    break;
                    case StaticReceive.MSG_DATA_NIBP_STATUS_CH: {
                        if (msg.arg1 == 1) {
                            bNIBPIng = true;
                            checkNIBPStatus(true);
                            setSYS("0");
                            setDIA("0");
                            if (!bSpo2Ing)
                                setPR("0");
                        } else if (msg.arg1 == 2) {
                            bNIBPIng = false;
                            checkNIBPStatus(false);
                            setSYS("0");
                            setDIA("0");
                        }
                    }
                    break;
                    case StaticReceive.MSG_DATA_NIBP_REALTIME: {
                        setSYS(msg.arg2 + "");
                        if (msg.arg1 == 0) {
                            showPulse(true);
                        }
                        dataBinding.realpalyPc300DrawNibpRect.setNIBP(msg.arg2, false);
                    }
                    break;
                    case StaticReceive.MSG_DATA_NIBP_END: {
                        bNIBPIng = false;
                        checkNIBPStatus(false);
                        Bundle data = msg.getData();
                        data.getBoolean("bHR");
                        int pr = data.getInt("nPulse");
                        int sys = data.getInt("nSYS");
                        int dia = data.getInt("nDIA");
                        int grade = data.getInt("nGrade");
                        int err = data.getInt("nBPErr");
                        if (err == StatusMsg.NIBP_ERROR_NO_ERROR) {
                            setSYS(sys + "");
                            setDIA(dia + "");
                            if (!bSpo2Ing && !bECGIng) {
                                setPR(pr + "");
                            }
                            dataBinding.realpalyPc300DrawNibpRect.setNIBP(grade, true);
                        } else {
                            setSYS("0");
                            setDIA("0");
                            if (!bSpo2Ing)
                                setPR("0");
                            if (err == 15)
                                err = nibpERR.length - 1;
                            dataBinding.realpalyPc300DrawNibpRect.setNIBP(0, false);
                        }
                    }
                    break;
                    case StaticReceive.MSG_DATA_SPO2_PARA: {
                        Bundle d = msg.getData();
                        if (!bECGIng) {
                            setTVtext(dataBinding.realplayPc300TvMode, "Pleth");
                        }
                        if (!d.getBoolean("bProbe")) {
                            bSpo2Ing = false;
                            dataBinding.realpalyPc300ViewDraw.cleanWaveData();
                            setSPO2("");
                            setPR("");
                        } else {
                            bSpo2Ing = true;
                            setSPO2(d.getInt("nSpO2") + "");
                            if (!bECGIng) {
                                setPR(d.getInt("nPR") + "");
                            }
                        }
                    }
                    break;
                    case StaticReceive.MSG_DATA_TEMP: {
                        Bundle d = msg.getData();
                        if (d.getInt("nResultStatus") == 0) {
                            setTMP(msg.getData().getFloat("nTmp") + "");
                        } else if (d.getInt("nResultStatus") == 1) {
                            setTMP("L");
                        } else if (d.getInt("nResultStatus") == 2) {
                            setTMP("H");
                        }
                    }
                    break;
                    case RECEIVEMSG_PULSE_OFF: {
                        showPulse(false);
                    }
                    break;
                    case StaticReceive.MSG_DATA_PULSE: {
                        showPulse(true);
                    }
                    break;
                }
            }

        };
    }

    public static MeasureFragment newInstance() {
        Bundle args = new Bundle();
        MeasureFragment fragment = new MeasureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler;

    @Override
    protected Class<MeasureViewModel> getViewModel() {
        return MeasureViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_measure;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel(viewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
        if (!dataBinding.realpalyPc300ViewDraw.isStop()) {
            dataBinding.realpalyPc300ViewDraw.Stop();
        }
        drawThread = null;
        if (!dataBinding.realpalyPc300DrawSpoRect.isStop()) {
            dataBinding.realpalyPc300DrawSpoRect.Stop();
        }
        drawPC300SPO2RectThread = null;
        getContext().stopService(new Intent(getContext(), ReceiveService.class));
    }

    private void observeViewModel(MeasureViewModel viewModel) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 改变心电按钮状态
     *
     * @param isStart 测量状态 true 正在测量 false 没有测量
     */
    private void checkECGStatus(boolean isStart) {
        if (isStart) {
            dataBinding.realplayPc300BtEcg.setText(R.string.measure_stop);
        } else {
            dataBinding.realplayPc300BtEcg.setText(R.string.measure_ecg);
        }
    }

    /**
     * 改变血压按钮状态
     *
     * @param isStart 测量状态 true 正在测量 false 没有测量
     */
    private void checkNIBPStatus(boolean isStart) {
        if (isStart) {
            dataBinding.realplayPc300BtNibp.setText(R.string.measure_stop);
        } else {
            dataBinding.realplayPc300BtNibp.setText(R.string.measure_nibp);
        }
    }

    /**
     * 取消搏动标记
     */
    public static final int RECEIVEMSG_PULSE_OFF = 0x115;

    /**
     * 设置搏动标记
     */
    private void showPulse(boolean isShow) {
        if (isShow) {
            dataBinding.realplayPc300ImgPulse.setVisibility(View.VISIBLE);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(RECEIVEMSG_PULSE_OFF);
                }
            }.start();
        } else {
            dataBinding.realplayPc300ImgPulse.setVisibility(View.INVISIBLE);
        }
    }

    private int batteryCnt = 0;

    private long batteryTime = 0L;

    private void setBattery(int battery, int cell) {
        if (cell == 0) {
            batteryCnt = 0;
            dataBinding.realplayPc300ImgBattery.setImageResource(batteryRes[battery]);
            if (battery == 0) {
                long time = System.currentTimeMillis();
                if (time - batteryTime < 500) {
                    return;
                }
                batteryTime = time;
                int visible = dataBinding.realplayPc300ImgBattery.isShown() ? View.INVISIBLE : View.VISIBLE;
                dataBinding.realplayPc300ImgBattery.setVisibility(visible);
            } else {
                dataBinding.realplayPc300ImgBattery.setVisibility(View.VISIBLE);
            }
        } else if (cell == 1) {
            setImgResource(dataBinding.realplayPc300ImgBattery, batteryRes_img[batteryCnt]);
            batteryCnt = (batteryCnt + 1) % batteryRes_img.length;
        } else if (cell == 2) {
            batteryCnt = 0;
            setImgResource(dataBinding.realplayPc300ImgBattery, batteryRes_img[2]);
        }
    }

    /**
     * 设置电量图片
     *
     * @param img
     * @param res
     */
    private void setImgResource(ImageView img, int res) {
        if (!img.isShown()) {
            img.setVisibility(View.VISIBLE);
        }
        img.setImageResource(res);
    }

    private void setSPO2(String data) {
        setTVtext(dataBinding.realplayPc300TvSpo, data);
    }

    private void setPR(String data) {
        setTVtext(dataBinding.realplayPc300TvPr, data);
    }

    private void setSYS(String data) {
        setTVtext(dataBinding.realplayPc300TvSys, data);
    }

    private void setDIA(String data) {
        setTVtext(dataBinding.realplayPc300TvDia, data);
    }

    private void setTMP(String itemp) {
        setTVtext(dataBinding.realplayPc300TvTemp, itemp + "");
    }

    private void setGLU(String data) {
        setTVtext(dataBinding.realplayPc300TvGlu, data);
    }


    /**
     * android6.0 蓝牙检测
     */
    private static final int REQUEST_FINE_LOCATION = 0;

    private void android6_RequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(getContext(), R.string.android6_request_location, Toast.LENGTH_LONG).show();

                //请求权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);
                return;
            }
        }
    }

    private void setGLUUnit(boolean unit) {
        bGluUnitMmol = unit;
        if (bGluUnitMmol) { //切换
            dataBinding.tvGluUnit.setText("(mmol/L)");
            setGLU(String.valueOf(mGLU_Mmol));
        } else {
            dataBinding.tvGluUnit.setText("(mg/dl)");
            setGLU(String.valueOf(mGLU_Mgdl));
        }
    }

    /**
     * 血糖单位转换
     *
     * @param isL  <br>
     *             true 将data转换为  （mmol/L)毫摩尔/升  <br>
     *             false 将data转换为  （mg/dl）毫克/分升 <br><br>
     * @param data :GLU Value
     * @return
     */
    public float switchGLUUnit(boolean isL, float data) {
        if (isL) {// mmol/L
            return getFloatHALF_UP(data / 18f);// data / 18f;//
        } else {
            return getFloatHALF_UP(data * 18);// data * 18;//
        }
    }


    /**
     * 将float数据转换为精度为0.1的float
     */
    public static float getFloatHALF_UP(float v) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(1);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(formater.format(v));
    }

    private void setTVtext(TextView tv, String msg) {
        if (tv != null) {
            if (msg != null) {
                if (msg.equals("0") || msg.equals("") || msg.equals("0.0")) {
                    tv.setText(myResources.getString(R.string.const_data_nodata));
                } else {
                    tv.setText(msg);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (drawThread != null && !dataBinding.realpalyPc300ViewDraw.isPause()) {
            dataBinding.realpalyPc300ViewDraw.Pause();
        }
        if (drawPC300SPO2RectThread != null && !dataBinding.realpalyPc300DrawSpoRect.isPause()) {
            dataBinding.realpalyPc300DrawSpoRect.Pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Subsystem.SetGpioData(117, 1);
        // Subsystem.SetGpioData(118, 0);
        if (drawThread == null) {
            drawThread = new Thread(dataBinding.realpalyPc300ViewDraw, "DrawPC300Thread");
            drawThread.start();
        } else if (dataBinding.realpalyPc300ViewDraw.isPause()) {
            dataBinding.realpalyPc300ViewDraw.Continue();
        }
        if (drawPC300SPO2RectThread == null) {
            drawPC300SPO2RectThread = new Thread(dataBinding.realpalyPc300DrawSpoRect, "DrawPC300RectThread");
            drawPC300SPO2RectThread.start();
        } else if (dataBinding.realpalyPc300DrawSpoRect.isPause()) {
            dataBinding.realpalyPc300DrawSpoRect.Continue();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        myResources = getResources();
        nibpERR = myResources.getStringArray(R.array.bp_err_new);
        ecgERR = myResources.getStringArray(R.array.ecg_measureres);

        dataBinding.realpalyPc300ViewDraw.setmHandler(mHandler);
        drawThread = new Thread(dataBinding.realpalyPc300ViewDraw, "DrawPC300SNT");
        drawThread.start();

        drawPC300SPO2RectThread = new Thread(dataBinding.realpalyPc300DrawSpoRect, "DrawPC300RectThread");
        drawPC300SPO2RectThread.start();

        dataBinding.realplayPc300BtNibp.setOnClickListener(this);
        dataBinding.realplayPc300BtEcg.setOnClickListener(this);
        dataBinding.realplayPc300TvGlu.setOnClickListener(this);
        dataBinding.tvGluUnit.setOnClickListener(this);

        android6_RequestLocation();

        // connectDevice();
        Objects.requireNonNull(getContext()).startService(new Intent(getContext(), ReceiveService.class));

        return dataBinding.getRoot();
    }

    @Override
    public void initView() {
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .setupActionBar(getString(R.string.select_measure), true);
        setHasOptionsMenu(true);

        dataBinding.setCallback(viewModel);
        dataBinding.setViewModel(viewModel);
        String baseURL = Utils.getBaseURL(appPrefs).equals(AppConfig.BASE_URL)
                ? ""
                : Utils.getBaseURL(appPrefs);

        dataBinding.edtBaseUrl.setText(baseURL);
        viewModel.setNavigator(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0x100: {
                if (resultCode == 1) {
                    StaticReceive.setmHandler(mHandler);
                }
            }
            break;
        }
    }

    @Override
    public void onClickEditBaseUrl() {
        if (dataBinding.edtBaseUrl.isEnabled()) {
            String baseUrl = dataBinding.edtBaseUrl.getText().toString().trim();
            if (Utils.isUrl(baseUrl)) {
                appPrefs.setBaseUrl(baseUrl);
                dataBinding.btnEdit.setText(getString(R.string.edit));
                dataBinding.edtBaseUrl.setEnabled(false);
            } else {
                ToastUtils.showToast(getContext(), getString(R.string.url_error));
            }
        } else {
            dataBinding.btnEdit.setText(getString(R.string.save));
            dataBinding.edtBaseUrl.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.realplay_pc300_bt_ecg: {
                if (!MyBluetooth.isConnected) {
                    // connectDevice();
                    Intent i = new Intent(getContext(), ConnectActivity.class);
                    i.putExtra("device", 0);
                    startActivityForResult(i, 0x100);
                    break;
                }
                if (StatusMsg.ECG_DEVICE_STATUS == StatusMsg.ECG_LEAD_ON) {
                    if (bECGIng) {
                        SpotSendCMDThread.SendStopECG();
                    } else {
                        SpotSendCMDThread.SendStartECG();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.measure_ecg_connect_dev, Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.realplay_pc300_bt_nibp: {
                if (!MyBluetooth.isConnected) {
                    // connectDevice();
                    Intent i = new Intent(getContext(), ConnectActivity.class);
                    i.putExtra("device", 0);
                    startActivityForResult(i, 0x100);
                    break;
                }
                if (bNIBPIng) {
                    SpotSendCMDThread.SendStopMeasure();
                } else {
                    SpotSendCMDThread.SendStartMeasure();
                }
            }
            break;

            case R.id.realplay_pc300_tv_glu: {
                setGLUUnit(!bGluUnitMmol);
            }
            break;
            case R.id.tv_glu_unit: {
                setGLUUnit(!bGluUnitMmol);
            }
            break;
            default:
                break;
        }
    }
}
