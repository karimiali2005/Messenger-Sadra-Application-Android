package com.Sadraafzar.Messenger.Classes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.Sadraafzar.Messenger.Model.ReceiveAllViewModel;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesStatusMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesTypeMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.Sadraafzar.Messenger.subMesssage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.Sadraafzar.Messenger.FontOverride.NOTIFICATION_CHANNEL_ID;

public class Risave_Data {
    private static final String TAG ="" ;

Context context;
int idcompany=0;
    public Risave_Data(Context c,int idc) {
        context=c;
        idcompany=idc;
    }


    public void onReceive() {

        Log.e(TAG, "Risave_Data" );
        app.Info.mContext2=context;
        int userid=app.Info.LoadInt("UserID");
        String mobile=app.Info.LoadString("UserName");
        try {
            if(app.Communication.isNetworkConnected(context))
            {
                Log.e(TAG, "resive: "+String.valueOf(userid)+ mobile);
                app.Info.UserID=app.Info.LoadInt("UserID");
                app.Info.PrivateKey=app.Info.LoadString("PrivateKey");
                APIInterface apiInterface = APIClient.getClient(app.Communication.MessageApiVersion).create(APIInterface.class);
                String userid_e=app.AesEncryptionAlgorithm.EncryptServiceUserSend(String.valueOf(userid));
                String number_e=app.AesEncryptionAlgorithm.EncryptServiceUserSend(mobile);
                String date=getdate(context);
                Call<ReceiveAllViewModel> call = apiInterface.GetReceiveAll(userid_e,number_e,date);

                call.enqueue(new Callback<ReceiveAllViewModel>() {
                    @Override
                    public void onResponse(Call<ReceiveAllViewModel> call, Response<ReceiveAllViewModel> response) {
                        if(response.isSuccessful())
                        {
                            if(response.body()!=null)
                            {
                                Log.e(TAG, "get data: "+String.valueOf(userid)+ mobile);
                                ReceiveAllViewModel rav=response.body();

                                SaveSqllite(rav,context);
                            }


                        }else
                        {
                            Log.e(TAG, "resive: "+response.message());
                        }

                    }


                    @Override
                    public void onFailure(Call<ReceiveAllViewModel> call, Throwable t) {
                        app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t));
                        Log.e(TAG, "resive: "+t.getMessage() );

                    }
                });

            }

        }catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
            Log.e(TAG, "resive: "+ex.getMessage() );

        }

    }


    //region getdate
    private String getdate(Context context)
    {
         try {
        dbConnector db=new dbConnector(context, app.Database.dbName, null, 1);
        String query = "SELECT * FROM ChangeDate WHERE id=" + 1;
        Cursor c = null;
        c = db.select(query);
        ContentValues values = new ContentValues();
        if (c.getCount() > 0) {
            if(c.moveToNext()) {

                Log.e(TAG, "getdate now "+c.getString(c.getColumnIndex("ChangeDate")));
                return c.getString(c.getColumnIndex("ChangeDate"));
            }

        }
db.close();
    }
    catch (Exception ex)
    {
        Log.e(TAG, "getdate: "+ex.getMessage() );
    }

        Log.e(TAG, "getdate now "+"2019-02-02");
        return "2019-02-02";
    }
    //endregion

    //region Save Sqllite
    private void SaveSqllite(ReceiveAllViewModel rav,Context context) {
        //ذخیره در دیتابیس
        try {

            dbConnector db=new dbConnector(context, app.Database.dbName, null, 1);
            db_query dg=new db_query();
            //region MesCompaney
            if (rav.ListCompany.size() > 0) {
                for (mesCompanyChange_ResultDTO m : rav.ListCompany) {

                    String query = "SELECT * FROM mesCompany WHERE pkfCompany=" + m.pkfCompany;
                    Cursor c = null;
                    c = db.select(query);
                    if (c!=null&&c.getCount() > 0) {
                       //update

                        dg.update_table_mesCompany(db,m);

                    }
                    else {
                        //insert

                        dg.insert_table_mesCompany(db,m);
                    }
                }
            }
            //endregion

            //region mesStatus
            if (rav.ListStatusMessage.size() > 0) {
                for (mesStatusMessageChange_ResultDTO m : rav.ListStatusMessage) {

                    String query = "SELECT * FROM mesStatus WHERE pkfStatusMessage=" + m.pkfStatusMessage;
                    Cursor c = null;
                    c = db.select(query);

                    if (c.getCount() > 0) {
                      //update
                        dg.update_table_mesStatus(db,m);

                    } else {
                        //insert
                        dg.insert_table_mesStatus(db,m);
                    }
                }
            }
            //endregion

            //region mesType
            if (rav.ListTypeMessage.size() > 0) {
                for (mesTypeMessageChange_ResultDTO m : rav.ListTypeMessage) {

                    String query = "SELECT * FROM mesType WHERE pkfTypeMessage=" + m.pkfTypeMessage ;
                    Cursor c = null;
                    c = db.select(query);
                    ContentValues values = new ContentValues();
                    if (c.getCount() > 0) {
                        //update
                      dg.update_table_mesType( db,m);

                    } else {
                        //insert
                        dg.insert_table_mesType(db,m);
                    }
                }
            }
            //endregion

            //region mesMessage
            db_query dq=new db_query();
            int ids_daryaft=dq.getstutosmessage(db,3);
            int ids_read=dq.getstutosmessage(db,4);
            if (rav.ListMessage.size() > 0) {
                for (mesMessageChange_ResultDTO m : rav.ListMessage) {

                    String query = "SELECT * FROM mesMessage WHERE pkfMessage=" + m.pkfMessage;
                    Cursor c = null;
                    c = db.select(query);

                    if (c!=null&&c.getCount() > 0) {

                        if(m.isDelete)
                        {
                            dg.del_table_mesMessage(db,m);
                        }else
                        {
//                            if(m.FkfStatusMessage!=ids_read)
//                            {
//                                if(m.FkfStatusMessage!=ids_daryaft)
//                                {
//                                    m.FkfStatusMessage=ids_daryaft;
//                                    dq.setchengeviewtype(db,m);
//                                }
//                            }
                            //update

                            dg.update_table_mesMessage(db,m);
                        }

                    }
                    else {
                        //insert
                        if(m.isDelete)
                        {
                            dg.del_table_mesMessage(db,m);
                         }
                        else
                        {
                            if(m.FkfStatusMessage!=ids_read)
                            {
                             if(m.FkfStatusMessage!=ids_daryaft)
                             {
                                 m.FkfStatusMessage=ids_daryaft;
                                 dq.setchengeviewtype(db,m);
                             }
                            }
                            dg.insert_table_mesMessage(db,m);
                            SendNotification(m,context,ids_read);

                        }

                    }

                }

            }
            //endregion

            //region ChangeDate
            if (rav.ChangeDate!=null&&!rav.ChangeDate.trim().equals("")) {

                String query = "SELECT * FROM ChangeDate WHERE id=" + 1;
                Cursor c = null;
                c = db.select(query);
                ContentValues values = new ContentValues();
                if (c.getCount() > 0) {
                    //update
                   dg.update_table_ChangeDate(db,rav.ChangeDate);

                }
                else {
                    //insert
                    dg.insert_table_ChangeDate(db,rav.ChangeDate);

                }

            }
            //endregion



            db.close();
            Log.e(TAG, "SaveSqllite: ");
        }catch (Exception ex)
        {
            Log.e(TAG, "SaveSqllite: "+ex.getMessage() );
        }



    }
    //endregion

    //region Send notification
    private void SendNotification(mesMessageChange_ResultDTO m,Context context ,int ids_read)
    {
        try
        {

            if(idcompany!=0&&idcompany==m.FkfCompanyId)
            {
                app.Info.notifiidcompaney=0;
                if(app.Info.FkfCompanyId==0)
                {
                    Log.e(TAG, "In home");
                    Intent in = new Intent("com.an.sms.example2");
                    Bundle extras = new Bundle();
                    extras.putInt("refresh", m.FkfCompanyId);
                    in.putExtras(extras);
                    context.sendBroadcast(in);
                }



                dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
                db_query gq=new db_query();

                mesCompanyChange_ResultDTO tc=gq.select_table_mesCompany(db, m.FkfCompanyId);


              /*  Intent notificationIntent = new Intent(app.Info.mContext2, subMesssage.class);
                notificationIntent.putExtra("idcompaney", m.FkfCompanyId);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ((MainActivity)context).startActivity(notificationIntent);
*/
                Intent intent = new Intent(context,subMesssage.class);
                intent.putExtra("idcompaney", m.FkfCompanyId);
                app.Info.FkfCompanyId=m.FkfCompanyId;
                context.startActivity(intent);


                Log.e(TAG, "send from firebase id companey " );
            }
            else
            {


                Log.e(TAG, "Dont send from firebase id companey " );
                app.Info.FkfCompanyId=app.Info.LoadInt("FkfCompanyId");
                Log.e(TAG, "enter notification"+" id companey :" +String.valueOf( app.Info.FkfCompanyId));
                if(app.Info.FkfCompanyId!=m.FkfCompanyId&&m.FkfStatusMessage!=ids_read&&m.IsFireBase!=true)
                {

                    dbConnector db = new dbConnector(context, app.Database.dbName, null, 1);
                    db_query gq=new db_query();

                mesCompanyChange_ResultDTO tc=gq.select_table_mesCompany(db, m.FkfCompanyId);
                    int idnot=m.FkfCompanyId;
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    // app.Info.FkfCompanyId=m.FkfCompanyId;
                    Intent notificationIntent = new Intent(app.Info.mContext2, subMesssage.class);
                    notificationIntent.putExtra("idcompaney", m.FkfCompanyId);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(context,idnot,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
                    notificationBuilder
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.arrow_bg1)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("پیام جدید")
                            .setContentText(m.message)
                            .setContentInfo(tc.companyName).setSound(uri)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                            .setAutoCancel(true)

                    ;
                    //   long[] v = {500,1000};
                    // Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //   notificationBuilder.setVibrate(v);
                    //   notificationBuilder.setSound(uri);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(idnot, notificationBuilder.build());
                    Log.e(TAG, "send notifi" );
                }
                else if(app.Info.FkfCompanyId==m.FkfCompanyId)
                {
                    Intent in = new Intent("com.an.sms.example");
                    Bundle extras = new Bundle();
                    extras.putInt("refresh", m.FkfCompanyId);
                    in.putExtras(extras);
                    context.sendBroadcast(in);
                }

                if(app.Info.FkfCompanyId==0)
                {

                    Log.e(TAG, "In home");
                    Intent in = new Intent("com.an.sms.example2");
                    Bundle extras = new Bundle();
                    extras.putInt("refresh", m.FkfCompanyId);
                    in.putExtras(extras);
                    context.sendBroadcast(in);
                }


            }

        }catch (Exception ex)
        {
            Log.e(TAG, "SendNotification: "+ex.getMessage() );
        }
    }
    //endregion
}
