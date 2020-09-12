package com.adeasy.advertise.callback;

import android.app.TaskInfo;

import com.google.android.gms.tasks.Task;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface OrderCallback {
    public void onCompleteInsertOrder(Task<Void> task);
}
