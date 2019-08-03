package com.cfox.rpaudio.service.bind;

import android.os.Bundle;
import android.os.RemoteException;

import com.cfox.rpaudio.ICallBack;
import com.cfox.rpaudio.ICreateRecorderService;
import com.cfox.rpaudio.IWaveCallBack;
import com.cfox.rpaudio.recorder.RecorderCapture;
import com.cfox.rpaudio.service.ServiceConstants;

public class CreateRecorderBind extends ICreateRecorderService.Stub implements ServiceConstants {
    private static final String TAG = "CreateRecorderBind";
    private RecorderCapture mRecorderCapture;

    public CreateRecorderBind(RecorderCapture recorderCapture) {
        this.mRecorderCapture = recorderCapture;
    }


    @Override
    public void startRecorder(Bundle bundle, ICallBack callback) throws RemoteException {
        String filePath = null;
        String fileName = null;
        if (bundle != null) {
            filePath = bundle.getString(FILE_PATH, null);
            fileName = bundle.getString(FILE_NAME, null);
        }
        mRecorderCapture.setCallBack(callback).startRecorder(filePath, fileName);
    }

    @Override
    public void setWaveListener(IWaveCallBack callBack) {
        mRecorderCapture.setWaveCallBack(callBack);
    }

    @Override
    public void pauseRecorder(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderCapture.setCallBack(callback).pauseRecorder();
    }

    @Override
    public void resumeRecorder(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderCapture.setCallBack(callback).resumeRecorder();
    }

    @Override
    public void stopRecorder(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderCapture.setCallBack(callback).stopRecorder();
    }

    @Override
    public void getRecorderStatus(ICallBack callback) {
        mRecorderCapture.setCallBack(callback).getRecorderStatus();
    }
}
