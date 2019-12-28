package com.cfox.camera.camera.session.impl;

import android.content.Context;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.log.EsLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraSessionManager implements ISessionManager {

    private static ISessionManager mSessionManager;
    private List<ICameraSession> mCameraSessionList = new ArrayList<>();
    private Context mContext;
    private AtomicInteger mSessionIndex = new AtomicInteger(0);


    private CameraSessionManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static ISessionManager getInstance(Context context) {
        if (mSessionManager == null) {
            synchronized (CameraSessionManager.class) {
                if (mSessionManager == null) {
                    mSessionManager = new CameraSessionManager(context);
                }
            }
        }
        return mSessionManager;
    }

    @Override
    public void getCameraSession(int count) {
        EsLog.d("getCameraSession: count:" + count  + "  session size:" + mCameraSessionList.size());
        mSessionIndex.set(0);
        for (int i = 0 ; i < count; i ++) {
            if (i < mCameraSessionList.size()) {
                EsLog.d("getCameraSession: get i:" + i);
                mCameraSessionList.get(i).onClose();
            } else {
                EsLog.d( "getCameraSession: create i:" + i);
                mCameraSessionList.add(createSession());
            }
        }

        for (int i = count;  i < mCameraSessionList.size() ; i ++) {
            EsLog.d("getCameraSession: remove i：" + i);
            mCameraSessionList.remove(i).onClose();
        }

        EsLog.d("getCameraSession: end session size:" + mCameraSessionList.size());
    }

    @Override
    public boolean hasSession() {
        return mSessionIndex.get() < mCameraSessionList.size();
    }

    @Override
    public ICameraSession getCameraSession() {
        if (!hasSession()) {
            throw new RuntimeException("don`t have camera session , create size :" + mCameraSessionList.size()  + "  current session count :" + (mSessionIndex.get() + 1) );
        }
        return mCameraSessionList.get(mSessionIndex.get());
    }

    private ICameraSession createSession() {
        return new CameraSession(mContext);
    }


}
