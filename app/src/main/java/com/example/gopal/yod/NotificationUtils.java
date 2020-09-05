package com.example.gopal.yod;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 33;
    private static final int PENDING_INTENT_ID = 78;

    public static void notifyWordOfTheDay(Context context) {

        // Get the NotificationManager using context.getSystemService
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    android.app.NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        String contentText = "";
        String bigStyleText = "";
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Word> words = realm.where(Word.class).findAll();
        if (!words.isEmpty()) {
            int randomNumber = new Random().nextInt(words.size());
            Word randomWord = words.get(randomNumber);
            contentText = "Word: " + randomWord.getWordName();
            bigStyleText = "Word: " + randomWord.getWordName() + "\n" + "Meaning: " + randomWord.getWordMeaning();
        }else {
            contentText = "OOPS, you haven't saved any word!";
        }


        // - sets the style to NotificationCompat.BigTextStyle().bigText(text)
        // setDefaults()- sets the notification defaults to vibrate
        //setAutoCancel() - automatically cancels the notification when the notification is clicked
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigStyleText))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setColor(ContextCompat.getColor(context, R.color.royal_blue))
                .addAction(rememberedAction(context))
                .addAction(forgotAction(context))
                .setAutoCancel(true);

        //  If the build version is greater than JELLY_BEAN and lower than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    private static NotificationCompat.Action rememberedAction(Context context) {

        Intent rememberedIntent = new Intent(context, NotificationActionService.class);
//        // Set the action of the intent to designate you want to dismiss the notification
        rememberedIntent.setAction(NotificationActionService.ACTION_REMEMBERED);

        PendingIntent rememberedPendingIntent = PendingIntent.getService(
                context,
                PENDING_INTENT_ID,
                rememberedIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action rememberedAction = new NotificationCompat.Action(R.drawable.ic_launcher_background,
                "Remembered?",
                rememberedPendingIntent);
        return rememberedAction;
    }

    private static NotificationCompat.Action forgotAction(Context context) {
        Intent forgotIntent = new Intent(context, NotificationActionService.class);
        forgotIntent.setAction(NotificationActionService.ACTION_FORGOT);
        PendingIntent forgotPendingIntent = PendingIntent.getService(
                context,
                PENDING_INTENT_ID,
                forgotIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action forgotAction = new NotificationCompat.Action(R.drawable.ic_launcher_background,
                "Forgot?",
                forgotPendingIntent);
        return forgotAction;
    }

    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        // - Has the flag FLAG_UPDATE_CURRENT, so that if the intent is created again, keep the intent but update the data
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        //  Get a Resources object from the context.
        Resources res = context.getResources();
        // Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable.ic_local_drink_black_24px
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_launcher_foreground);
        return largeIcon;
    }


}
