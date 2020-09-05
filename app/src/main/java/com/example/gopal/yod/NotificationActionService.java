package com.example.gopal.yod;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class NotificationActionService extends IntentService {
    public static final String ACTION_REMEMBERED = "ACTION_REMEMBERED";
    public static final String ACTION_FORGOT = "ACTION_FORGOT";

    public NotificationActionService() {
        super("Argument");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getAction() == ACTION_REMEMBERED){
            NotificationUtils.clearAllNotifications(NotificationActionService.this);
        }else
            NotificationUtils.clearAllNotifications(NotificationActionService.this);

    }
}
