package com.example.mobilepaindiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/14 20:31
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("alarm".equals(intent.getAction())) {
            Log.e("AlarmBroadcastReceiver", "onReceive: ");

            Toast.makeText(context, "Need add record", Toast.LENGTH_LONG).show();

        }
    }
}
