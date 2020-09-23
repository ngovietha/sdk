/*
 * Created by NQC on 5/24/20 12:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 7/20/16 2:37 PM
 *
 */

package com.nqc.idoctor.common.creative.recvdata;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.creative.SpotCheck.ISpotCheckCallBack;
import com.creative.SpotCheck.SpotCheck;
import com.creative.base.BaseDate.ECGData;
import com.creative.base.BaseDate.Wave;
import com.creative.base.Ireader;
import com.creative.base.Isender;
import com.nqc.idoctor.common.utils.LogUtils;

import org.json.JSONArray;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class StaticReceive {

    // /**
    // * PC-300SNT系列协议解析
    // */
    // private static SpotCheck spotCheck;

    /**
     * PC-60NW系列协议解析
     */
    private static SpotCheck spotCheck;

    // /**
    // * PC80B系列协议解析
    // */
    // private static ECG ecg;

    /**
     * 通知上层各种数据消息
     */
    private static Handler mHandler;

    protected static Context mContext;

    /**
     * 开始接收数据
     *
     * @param bluName
     */
    public static void startReceive(Context context, String bluName, Ireader iReader, Isender iSender,
                                    Handler _handler) {
        if (bluName != null && !bluName.equals("")) {
            start = true;
            mHandler = _handler;
            mContext = context;
            if (bluName.equals("PC_300SNT") || bluName.equals("PC-200") || bluName.equals("PC-100")) {
                spotCheck = new SpotCheck(iReader, iSender, new SpotCallBack());
                spotCheck.Start();
                spotCheck.QueryDeviceVer();
            }
            // else if (bluName.equals("PC80B")) {
            // ecg = new ECG(iReader, iSender, new ECGCallBack());
            // ecg.Start();
            // ecg.QueryDeviceVer();
            // } else
        }
    }

    /**
     * 停止接收数据
     */
    public static void StopReceive() {
        start = false;
        if (spotCheck != null) {
            spotCheck.Stop();
            spotCheck = null;
        }
        HWMajor = HWMinor = SWMajor = SWMinor = ALMajor = ALMinor = 0;
    }

    /**
     * 暂停数据接收
     */
    public static void Pause() {
        pause = true;
        if (spotCheck != null) {
            spotCheck.Pause();
        }
        // if (ecg != null) {
        // ecg.Pause();
        // }
    }

    /**
     * 恢复数据接收
     */
    public static void Continue() {
        pause = false;
        if (spotCheck != null) {
            spotCheck.Continue();
        }
        // if (ecg != null) {
        // ecg.Continue();
        // }
    }

    public static boolean pause = false;

    public static boolean start = false;

    public static void setmHandler(Handler mHandler) {
        StaticReceive.mHandler = mHandler;
    }

    /**
     * 数据类型key——心电文件
     */
    public static final String DATATYPEKEY_ECG_FILE = "ecgFile";
    /**
     * 数据类型key——心电波形
     */
    public static final String DATATYPEKEY_ECG_WAVE = "ecgwave";

    /**
     * 设备数据消息——设备ID
     */
    public static final int MSG_DATA_DEVICE_ID = 0x201;

    /**
     * 设备数据消息——设备版本信息
     */
    public static final int MSG_DATA_DEVICE_VS = 0x202;

    /**
     * 设备数据消息——血氧参数数据
     */
    public static final int MSG_DATA_SPO2_PARA = 0x203;

    /**
     * 设备数据消息——血氧波形数据
     */
    public static final int MSG_DATA_SPO2_WAVE = 0x204;

    /**
     * 设备数据消息——体温数据
     */
    public static final int MSG_DATA_TEMP = 0x205;

    /**
     * 设备数据消息——血糖数据
     */
    public static final int MSG_DATA_GLU = 0x206;

    /**
     * 设备数据消息——设备关机
     */
    public static final int MSG_DATA_DEVICE_SHUT = 0x207;

    /**
     * 设备数据消息——连接断开
     */
    public static final int MSG_DATA_DISCON = 0x208;

    /**
     * 设备数据消息——心电测量状态改变
     */
    public static final int MSG_DATA_ECG_STATUS_CH = 0x209;

    /**
     * 设备数据消息——血压测量状态改变
     */
    public static final int MSG_DATA_NIBP_STATUS_CH = 0x20a;

    /**
     * 设备数据消息——血压测量实时袖袋压
     */
    public static final int MSG_DATA_NIBP_REALTIME = 0x20b;

    /**
     * 设备数据消息——血压测量结果
     */
    public static final int MSG_DATA_NIBP_END = 0x20c;

    /**
     * 设备数据消息——心电波形数据
     */
    public static final int MSG_DATA_ECG_WAVE = 0x20d;

    /**
     * 设备数据消息——电池电量
     */
    public static final int MSG_DATA_BATTERY = 0x20e;

    /**
     * 设备数据消息——搏动标记
     */
    public static final int MSG_DATA_PULSE = 0x20f;

    /**
     * 设备数据消息——ecg心电增益
     */
    public static final int MSG_DATA_ECG_GAIN = 0x210;

    /**
     * 设备数据消息——ecg心电增益
     */
    public static final int MSG_ECG_ARRAY = 0x211;

    /**
     * 设备版本信息
     */
    public static int HWMajor, HWMinor, SWMajor, SWMinor, ALMajor, ALMinor;

    /**
     * 保存波形数据
     */
    public static List<Wave> DRAWDATA = new ArrayList<Wave>();

    /**
     * 保存血氧波形数据 用于绘制血氧柱状图
     */
    public static List<Wave> SPOWAVE = new ArrayList<Wave>();

    /**
     * 是否是心电波形数据
     */
    public static boolean isECGData = false;

    /**
     * 心电波形范围是否是255
     */
    public static boolean is128 = false;


    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static class SpotCallBack implements ISpotCheckCallBack {
        public JSONArray ecgData;

        private SpotCallBack() {
            this.ecgData = new JSONArray();
        }

        @Override
        public void OnConnectLose() {
        }

        @Override
        public void OnGetDeviceID(String sDeviceID) {
            mHandler.obtainMessage(MSG_DATA_DEVICE_ID, sDeviceID).sendToTarget();
        }

        @Override
        public void OnGetDeviceVer(int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor, int nPower, int nBattery) {
            HWMajor = nHWMajor;
            HWMinor = nHWMinor;
            SWMajor = nSWMajor;
            SWMinor = nSWMinor;
            mHandler.obtainMessage(MSG_DATA_BATTERY, nPower, nBattery).sendToTarget();
        }

//		@Override
//		public void OnGetECGAction(boolean bStart) {
//			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, bStart ? 0 : 1, 0).sendToTarget();
//		}

        @Override
        public void OnGetECGAction(int status) {
            mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, status, 0).sendToTarget();
        }

        @Override
        public void OnGetECGRealTime(ECGData waveData, int nHR, boolean bLeadoff, int nMax) {
            isECGData = true;
            if (nMax == 255)
                is128 = true;
            else // nMax = 4095
                is128 = false;
            DRAWDATA.addAll(waveData.data);
            Message msg = mHandler.obtainMessage(MSG_DATA_ECG_WAVE, waveData);
            Bundle data = new Bundle();
            data.putBoolean("bLeadoff", bLeadoff);
            data.putInt("nHR", nHR);

            msg.setData(data);
            mHandler.sendMessage(msg);

			for (int i = 0; i < waveData.data.size(); i++) {
				this.ecgData.put(String.format("%.4f", new Object[]{Float.valueOf((Float.valueOf((float) ((Wave) waveData.data.get(i)).data).floatValue() - 128.0f) / 114.3f)}));
			}
        }

        @Override
        public void onGetECGGain(int arg0, int arg1) {
            mHandler.obtainMessage(MSG_DATA_ECG_GAIN, arg0, arg1).sendToTarget();
        }

        @Override
        public void OnGetECGResult(int nResult, int nHR) {

            LogUtils.d("ECG Data: " + ecgData.toString());

            mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, 2, nResult, nHR).sendToTarget();

            Bundle data = new Bundle();
            data.putString("MSG_ECG_ARRAY", ecgData.toString());

            Message msg = mHandler.obtainMessage(MSG_ECG_ARRAY);
            msg.setData(data);
            mHandler.sendMessage(msg);
        }

        @Override
        public void OnGetECGVer(int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
        }

        @Override
        public void OnGetGlu(float nGlu, int nGluStatus, int unit) {
            System.out.println("血糖值：" + nGlu + " " + nGluStatus + "  " + unit);
            mHandler.obtainMessage(MSG_DATA_GLU, nGluStatus, unit, nGlu).sendToTarget();
        }

        @Override
        public void OnGetGluStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {

        }

        @Override
        public void OnGetNIBPAction(int bStart) {
            mHandler.obtainMessage(MSG_DATA_NIBP_STATUS_CH, bStart, 0).sendToTarget();
        }

        @Override
        public void OnGetNIBPRealTime(boolean bHeartbeat, int nBldPrs) {
            if (bHeartbeat) {
                mHandler.obtainMessage(MSG_DATA_NIBP_REALTIME, 0, nBldPrs).sendToTarget();
            } else {
                mHandler.obtainMessage(MSG_DATA_NIBP_REALTIME, 1, nBldPrs).sendToTarget();
            }
        }

        @Override
        public void OnGetNIBPResult(boolean bHR, int nPulse, int nMAP, int nSYS, int nDIA, int nGrade, int nBPErr) {
            Message msg = mHandler.obtainMessage(MSG_DATA_NIBP_END);
            Bundle data = new Bundle();
            data.putBoolean("bHR", bHR);
            data.putInt("nPulse", nPulse);
            data.putInt("nSYS", nSYS);
            data.putInt("nDIA", nDIA);
            data.putInt("nGrade", nGrade);
            data.putInt("nBPErr", nBPErr);
            msg.setData(data);
            mHandler.sendMessage(msg);
        }

        @Override
        public void OnGetNIBPStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
        }

        @Override
        public void OnGetPowerOff() {
        }

        @Override
        public void OnGetSpO2Param(int nSpO2, int nPR, float nPI, boolean bProbe, int nMode) {
            Message msg = mHandler.obtainMessage(MSG_DATA_SPO2_PARA);
            Bundle data = new Bundle();
            data.putInt("nSpO2", nSpO2);
            data.putInt("nPR", nPR);
            data.putFloat("nPI", nPI);
            data.putBoolean("bProbe", bProbe);
            data.putInt("nMode", nMode);
            msg.setData(data);
            mHandler.sendMessage(msg);
        }

        @Override
        public void OnGetSpO2Status(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
        }

        @Override
        public void OnGetSpO2Wave(List<Wave> waveData) {
            DRAWDATA.addAll(waveData);
            SPOWAVE.addAll(waveData);
            isECGData = false;
        }

        @Override
        public void OnGetTmp(boolean bManualStart, boolean bProbeOff, float nTmp, int nTmpStatus, int nResultStatus) {
            Message msg = mHandler.obtainMessage(MSG_DATA_TEMP);
            Bundle data = new Bundle();
            data.putBoolean("bManualStart", bManualStart);
            data.putBoolean("bProbeOff", bProbeOff);
            data.putFloat("nTmp", nTmp);
            data.putInt("nTmpStatus", nTmpStatus);
            data.putInt("nResultStatus", nResultStatus);
            msg.setData(data);
            mHandler.sendMessage(msg);
        }

        @Override
        public void OnGetTmpStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {

        }

        @Override
        public void OnGetNIBPMode(int arg0) {

        }

        @Override
        public void OnGetSpO2Action(int action) {
        }

        @Override
        public void NIBP_StartStaticAdjusting() {
        }

        @Override
        public void OnGetGLUAction(int status) {
        }

        @Override
        public void OnGetTMPAction(int status) {
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
        return Float.valueOf(formater.format(v));
    }
}
