package com.Sadraafzar.Messenger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.appIsRun;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.Classes.db_query;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.Sadraafzar.Messenger.FontOverride.NOTIFICATION_CHANNEL_ID;

public class TheFireBase2 extends FirebaseMessagingService {
    private static final String TAG ="" ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            int idc= Integer.parseInt(remoteMessage.getData().get("idc"));
            String body=remoteMessage.getData().get("body");
            if(appIsRun.isAppRunning(this,getPackageName()))
            {
                Log.e(TAG, "In home");
                Intent in = new Intent("com.an.sms.example2");
                    Bundle extras = new Bundle();
               extras.putInt("refresh", -1);
                in.putExtras(extras);
               this.sendBroadcast(in);
                notifi_when_app_destroy(idc,body);
            }
            else
            {
                Log.e(TAG, "In home");
                Intent in = new Intent("com.an.sms.example2");
                Bundle extras = new Bundle();
                extras.putInt("refresh", -1);
                in.putExtras(extras);
                this.sendBroadcast(in);
                notifi_when_app_isrun(idc,body);
            }

        }

    }
private void notifi_when_app_destroy(int idc,String titel)
{


    int idnot=idc;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    // app.Info.FkfCompanyId=m.FkfCompanyId;
    Intent notificationIntent = new Intent(this, Spelash.class);
    notificationIntent.putExtra("idc", idc);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(this,idnot,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
    notificationBuilder
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.arrow_bg1)
            .setContentIntent(pendingIntent)
            .setContentTitle("پیام جدید")
            .setContentText(titel)
            .setContentInfo("پیام جدید").setSound(uri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setAutoCancel(true)

    ;
    //   long[] v = {500,1000};
    // Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    //   notificationBuilder.setVibrate(v);
    //   notificationBuilder.setSound(uri);
    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(idnot, notificationBuilder.build());
    Log.e(TAG, "send notifi" );
}
private void notifi_when_app_isrun(int idc,String titel)
{
    int idnot=idc;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    // app.Info.FkfCompanyId=m.FkfCompanyId;
    Intent notificationIntent = new Intent(this, subMesssage.class);
    notificationIntent.putExtra("idcompaney",idc);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(this,idnot,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
    notificationBuilder
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.arrow_bg1)
            .setContentIntent(pendingIntent)
            .setContentTitle("پیام جدید")
            .setContentText(titel)
            .setContentInfo("").setSound(uri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setAutoCancel(true);

    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(idnot, notificationBuilder.build());
    Log.e(TAG, "send notifi" );
}

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);


     //   sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        try
        {
            APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
            int userid=app.Info.LoadInt("UserID");
            String userid_e=app.AesEncryptionAlgorithm.EncryptServiceUserSend(String.valueOf(userid));
            Call<ResponseBody> call = apiInterface.ChangeTokenFireBase(userid_e,token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful())
                    {
                        Log.e(TAG, "Refreshed token succes : " + token);
                    }else
                    {
                        Log.e(TAG, "Refreshed token Send to server Eror: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Refreshed token Send to server filed: " +t.getMessage());
                }
            });

        }catch (Exception e)
        {

        }
    }
}
