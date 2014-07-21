package org.freemp.android;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import org.freemp.android.player.ServicePlayer;

/**
 * Author: jorik
 * Date: 07.12.13
 */
public class NotificationUtils {


    public static Notification getNotification(Context context, PendingIntent pendingIntent, ClsTrack track, boolean isPlaying) {

        Notification notification = new Notification();
        if (track!=null) {
            notification.contentView = getNotificationViews(track, context, isPlaying, R.layout.notification);
        }
        else {
            notification.setLatestEventInfo(context, "", "", pendingIntent);
        }
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        notification.contentIntent = pendingIntent;
        notification.icon = R.drawable.icon;
        return notification;
    }



    private static RemoteViews getNotificationViews(final ClsTrack track, final Context context,  boolean isPlaying, int layoutId) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

        views.setTextViewText(R.id.notifTitle, track.getTitle());
        views.setTextViewText(R.id.notifArtist, track.getArtist());
        //views.setImageViewResource(R.id.notifAlbum,  R.drawable.artwork);
        Bitmap cover =  MediaUtils.getArtworkQuick(context, track, 180, 180);
        if (cover != null){
            views.setImageViewBitmap(R.id.notifAlbum, cover);
        }else {
            views.setImageViewResource(R.id.notifAlbum,  R.drawable.artwork);
        }
        if (Build.VERSION.SDK_INT<11) {
            views.setViewVisibility(R.id.action_prev, View.GONE);
            views.setViewVisibility(R.id.action_play, View.GONE);
            views.setViewVisibility(R.id.action_next, View.GONE);

        }

        views.setImageViewResource(R.id.action_play, isPlaying ? R.drawable.base_pause : R.drawable.base_play);

        ComponentName componentName = new ComponentName(context, ServicePlayer.class);

        Intent intentPlay = new Intent("play");
        intentPlay.setComponent(componentName);
        views.setOnClickPendingIntent(R.id.action_play, PendingIntent.getService(context, 0, intentPlay, 0));

        Intent intentNext = new Intent("next");
        intentNext.setComponent(componentName);
        views.setOnClickPendingIntent(R.id.action_next, PendingIntent.getService(context, 0, intentNext, 0));

        Intent intentPrevious = new Intent("prev");
        intentPrevious.setComponent(componentName);
        views.setOnClickPendingIntent(R.id.action_prev, PendingIntent.getService(context, 0, intentPrevious, 0));

        return views;
    }

    public static void alert(Context context,String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);

        bld.create().show();
    }
}
