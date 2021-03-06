package com.cfox.rpaudio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cfox.rpaudio.recorder.RecorderCapture;
import com.cfox.rpaudio.recorder.RecorderPlayer;
import com.cfox.rpaudio.service.bind.CreateRecorderBind;
import com.cfox.rpaudio.service.bind.PlayRecorderBind;

public class RecorderService extends Service implements ServiceConstants {

    private RecorderCapture mRecorderCapture;
    private RecorderPlayer mRecorderPlayer;
    private CreateRecorderBind mCreateRecorderBind;
    private PlayRecorderBind mPlayRecorderBind;

    @Override
    public void onCreate() {
        super.onCreate();
        mRecorderCapture = new RecorderCapture();
        mRecorderPlayer = new RecorderPlayer();
        mCreateRecorderBind = new CreateRecorderBind(mRecorderCapture);
        mPlayRecorderBind = new PlayRecorderBind(mRecorderPlayer);
    }

    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ServiceConstants.BIND_TYPE_RECORDER)) {
            return mCreateRecorderBind;
        }

        if (action.equals(ServiceConstants.BIND_TYPE_PLAY)) {
            return mPlayRecorderBind;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecorderCapture = null;
        mRecorderPlayer = null;
        mCreateRecorderBind = null;
        mPlayRecorderBind = null;
    }
}
