package com.ashlikun.easypopup.simple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/8 0008　17:18
 * 邮箱　　：496546144@qq.co
 * <p>
 * 功能介绍：
 */

public class AidlService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMyAidlInterface.Stub() {

            @Override
            public int qiuhe(int a, int b) throws RemoteException {
                return a + b + b;
            }
        };
    }
}
