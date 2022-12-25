package com.Sadraafzar.Messenger.Classes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.Sadraafzar.Messenger.WebService.APIClient;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.HashMap;
import java.util.Map;

public class SignalR {
    private static final String TAG = "";
  public static   HubConnection hubConnection;
    Context context;

    public SignalR(Context context) {
        this.context = context;
    }
    public void onReceive(String longtype) {
        try {
            if(hubConnection==null||hubConnection.getConnectionState()== HubConnectionState.DISCONNECTED)
            {

                Log.e(TAG, "Load signal R4" );
                Map<String, String> map = new HashMap<String, String>();
                // String username=app.AesEncryptionAlgorithm.EncryptServiceUserSend("User"+String.valueOf(app.Info.UserID)+":::"+longtype+":::"+String.valueOf(app.Info.VersionCode));
                //  String username="User"+String.valueOf(app.Info.UserID)+":::"+longtype+":::"+String.valueOf(app.Info.VersionCode);


                map.put("userId","User"+String.valueOf(app.Info.UserID));
                map.put("loginType",longtype);
                map.put("appVersion",String.valueOf(app.Info.VersionCode));
                map.put("ip",String.valueOf(app.Info.IP4));
                map.put("modelmobile",getDeviceName());
                map.put("skdmobile",getAndroidVersion());

                hubConnection= HubConnectionBuilder.create(app.Communication.baseUrlSignalR).withHeaders(map).build();

                hubConnection.start();
                boolean a=true;
                while (a==true)
                {
                    if(hubConnection.getConnectionState()== HubConnectionState.CONNECTED)
                    {
                        a=false;
                        Log.e(TAG, "Load signal R3" );
                        app.Info.mContext2=context;
                        int userid=app.Info.LoadInt("UserID");
                        Log.e(TAG, "Load signal "+"User"+String.valueOf(userid) );
                        hubConnection.on("User"+String.valueOf(userid),(name, message)->{

                            Log.e(TAG, "Load signal R"+message );


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Intent in = new Intent("com.an.sms.example2");
                                Bundle extras = new Bundle();
                                extras.putInt("refresh", 0);
                                in.putExtras(extras);
                                context.sendBroadcast(in);

                            }else
                            {
                                Intent in = new Intent("com.an.sms.example2");
                                Bundle extras = new Bundle();
                                extras.putInt("refresh",0);
                                in.putExtras(extras);
                                context.sendBroadcast(in);
                            }
                        },String.class,String.class);
                    }
                }



                Log.e(TAG, hubConnection.getConnectionState().toString());
            }

        }catch (Exception ex)
        {
            Log.e(TAG, "SignalrRisive: "+ex.getMessage() );
        }
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
