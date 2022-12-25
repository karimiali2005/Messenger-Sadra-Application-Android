package com.Sadraafzar.Messenger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.Sadraafzar.Messenger.Classes.ChengeTypeSendToServer;
import com.Sadraafzar.Messenger.Classes.app;

public class TheService extends Service {
    private static final String TAG ="" ;
    Context mContext;
    boolean isfirst=true;
    boolean issecond=false;
    Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

           return null;
    }
    @Override
    public void onCreate() {
        try {
            mContext=this;


        }catch (Exception ex)
        {
            Log.e(TAG, "onCreate: "+ex.getMessage() );
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            Log.e(TAG, "onStartCommand" );
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    looper();
                    onStartCommand(intent, flags, startId);
                }
            }, 10000);

        }catch (Exception ex)
        {
            Log.e(TAG, "onStartCommand: "+ex.getMessage() );
        }

        return START_NOT_STICKY;
    }



    private void looper()
{
    try
    {
        if(isOnline())
        {
            Log.e(TAG, "timer" );
            if(isfirst&&!issecond)
            {
                isfirst=false;

            }else if(isfirst&&issecond)
            {


                isfirst=false;

                Log.e(TAG, "In home");
                Intent in = new Intent("com.an.sms.example2");
                Bundle extras = new Bundle();
                extras.putInt("refresh", -1);
                in.putExtras(extras);
                mContext.sendBroadcast(in);


            }
            if (app.Info.ischeang)
            {
                try
                {
                    app.Info.ischeang=false;
                    ChengeTypeSendToServer c=new ChengeTypeSendToServer();
                   c.Send(mContext);
                   Log.e(TAG, "ChengeTypeSendToServer");
                }catch (Exception ex)
                {
                    Log.e(TAG, "ChengeTypeSendToServer Eror:"+ex.getMessage() );
                }



            }


        }
        else
        {
            if(!isfirst)
            {
                isfirst=true;
                issecond=true;
            }
        }
    }catch (Exception e)
    {
        Log.e(TAG, "Received an exception " + e.getMessage() );
    }
}




    //endregion










    //endregion



    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                return false;
            }
        }catch (Exception ex)
        {

            Log.e(TAG, "isOnline: "+ex.getMessage() );
            return false;
        }

    }

}
