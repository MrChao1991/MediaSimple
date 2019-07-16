package com.chao.lib_audio.service.connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.chao.lib_audio.ICallBack;
import com.chao.lib_audio.ICreateRecorderService;
import com.chao.lib_audio.IWaveCallBack;
import com.chao.lib_audio.log.FxLog;
import com.chao.lib_audio.service.ServiceConstants;


public class RecorderServiceConnection implements ServiceConnection , ServiceConstants {
    private static final String TAG = "RecorderServiceConnection";

    private IWaveCallBack mWaveCallBack;
    private ICreateRecorderService mRecorderService;

    public RecorderServiceConnection(IWaveCallBack waveCallBack) {
        this.mWaveCallBack = waveCallBack;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mRecorderService = ICreateRecorderService.Stub.asInterface(service);
        if (mWaveCallBack != null) {
            try {
                mRecorderService.setWaveListener(mWaveCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRecorderService = null;
    }


    public void starRecorder(String value, boolean isFileName, ICallBack.Stub callBack) {
        if (isFileName) {
            starRecorder(null, value, callBack);
        } else {
            starRecorder(value, null, callBack);
        }
    }

    public void starRecorder(String filePath, String fileName, ICallBack.Stub callBack) {
        Bundle bundle = null;
        if (!fileName.isEmpty() || !filePath.isEmpty()) {
            bundle = new Bundle();
        }

        if (!filePath.isEmpty()) {
            bundle.putString(FILE_PATH, filePath);
        }

        if (!fileName.isEmpty()) {
            bundle.putString(FILE_NAME, fileName);
        }

        starRecorder(bundle, callBack);

    }

    public void starRecorder(Bundle bundle, ICallBack.Stub callBack) {

        if (!checkConnected()) {
            return;
        }
        try {
            mRecorderService.startRecorder(bundle, callBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void pauseRecorder(Bundle bundle, ICallBack.Stub callBack) {
        if (!checkConnected()) {
            return;
        }
        try {
            mRecorderService.pauseRecorder(bundle, callBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    public void resumeRecorder(Bundle bundle, ICallBack.Stub callBack) {
        if (!checkConnected()) {
            return;
        }
        try {
            mRecorderService.resumeRecorder(bundle, callBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void stopRecorder(Bundle bundle, ICallBack.Stub callBack) {
        if (!checkConnected()) {
            return;
        }
        try {
            mRecorderService.stopRecorder(bundle, callBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean checkConnected() {
        if (mRecorderService == null) {
            FxLog.e(TAG,"player recorder service not connected!!!!!!!!!!!");
            return false;
        }
        return true;
    }
}
