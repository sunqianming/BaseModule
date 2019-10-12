package com.base.module.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.base.module.MainActivity;
import com.base.module.R;

/**
 * @author SQM
 */
public class NotifyUtil {

    /**
     * 创建通知渠道
     */
    public static void createNotificationChannel(Context context) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mOrderChannel = new NotificationChannel("自定义id1", "订单消息", NotificationManager.IMPORTANCE_HIGH);
            mOrderChannel.enableLights(true);
            mOrderChannel.setLightColor(Color.BLUE);//设置闪灯
            mOrderChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/raw/sound"), null);//设置通知铃声
            mOrderChannel.enableVibration(true);
            mOrderChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});//设置震动
            notifyManager.createNotificationChannel(mOrderChannel);

            NotificationChannel mNormalChannel = new NotificationChannel("自定义id2", "其他消息", NotificationManager.IMPORTANCE_HIGH);
            mNormalChannel.enableLights(true);
            mNormalChannel.setLightColor(Color.BLUE);
            mNormalChannel.enableVibration(true);
            mNormalChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notifyManager.createNotificationChannel(mNormalChannel);
        }
    }

    /**
     * 发送通知
     *
     * @param context 上下文
     * @param title   消息标题
     * @param content 消息内容
     * @param extras  扩展参数
     */
    public static void sendNotification(Context context, String title, String content, String extras) {
        String msgType = "", code = "", channelId = "自定义id2";
        int pkOrder = 0;
        try {
//            JSONObject object = new JSONObject(extras);
//            code = object.getString(Appconfig.CODE);
//            msgType = object.getString(Appconfig.MEG_TYPE);
//            pkOrder = Integer.parseInt(object.getString(Appconfig.PKORDER));
//            if (TextUtils.equals(Appconfig.ORDER, object.getString(Appconfig.MSGINFO_TYPE))) {
//                channelId = Appconfig.ORDER_MESSAGE_ID;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        PendingIntent pendingIntent;
        if (TextUtils.equals("自定义id1", channelId)) {  //订单
            //builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound)); //自定义的铃声sound放在res的raw目录下
            Intent intent = new Intent(context, MainActivity.class);
//            if (TextUtils.equals(msgType, Appconfig.SERVICE_DELIVERY)) {  //待派工
//                intent = new Intent(context, DispatchDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(Appconfig.CODE, code);
//                bundle.putInt(Appconfig.PKORDER, pkOrder);
//                intent.putExtras(bundle);
//            } else if (TextUtils.equals(msgType, Appconfig.SERVICE_EXECUTED)) {  //待执行
//                intent = new Intent(context, OrderDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(Appconfig.CODE, code);
//                bundle.putInt(Appconfig.PKORDER, pkOrder);
//                intent.putExtras(bundle);
//            }
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            Intent intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_SHOW_LIGHTS;//注意：只有设置Notification的标志位为FLAG_SHOW_LIGHTS，才能支持三色灯提醒
        notify.ledARGB = Color.GREEN;
        notify.ledOnMS = 300;//led亮的时间
        notify.ledOffMS = 300;//led灭的时间
        notifyManager.notify(pkOrder, notify);
    }

}
