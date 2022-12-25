package com.Sadraafzar.Messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Adapter.submessageCompaneyApaptor;
import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.Classes.db_query;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesMessageChange_ResultDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class subMesssage extends AppCompatActivity {
Context mContext;
ImageView imgback;
   RecyclerView recyclerView;
    dbConnector db;
    TextView txt_titel;
   submessageCompaneyApaptor submessageaompaneyapaptor;
    private BroadcastReceiver broadcastReceiver;
    //region FontOverride
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_sub_messsage);
            mContext=this;
            app.Info.mContext2=mContext;
            app.Info.SaveInt("FkfCompanyId2",0);
            app.Info.SaveInt("FkfCompanyId",app.Info.FkfCompanyId);
            registerReceiver();
            //..............put Exstra..................

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {

                } else {

                    if(extras.getInt("idcompaney")!=0&&extras.getInt("idcompaney")!=-1)
                    {
                        app.Info.FkfCompanyId= extras.getInt("idcompaney");
                        app.Info.SaveInt("FkfCompanyId",app.Info.FkfCompanyId);
                    }
                }
            }
            else {
                if((int) savedInstanceState.getSerializable("idcompaney")!=0&&(int) savedInstanceState.getSerializable("idcompaney")!=-1)
                {
                    app.Info.FkfCompanyId= (int) savedInstanceState.getSerializable("idcompaney");
                    app.Info.SaveInt("FkfCompanyId",app.Info.FkfCompanyId);
                }

            }
            //.............................................................

            //......................................................................
            db = new dbConnector(mContext, app.Database.dbName, null, 1);
            //.........................................................................
            txt_titel=findViewById(R.id.txt_submesage_titel);
            db_query dq=new db_query();
            mesCompanyChange_ResultDTO mcr=dq.select_table_mesCompany(db,app.Info.FkfCompanyId);
            txt_titel.setText(mcr.companyName);
            //..................................................
            imgback=findViewById(R.id.img_back);
            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
           // select();

        }catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex) );

        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        app.Info.topactivity=true;

        if(app.Info.FkfCompanyId==-1)
        {
            int i=app.Info.LoadInt("FkfCompanyId2");
            app.Info.FkfCompanyId=i;
        }
        app.Info.SaveInt("FkfCompanyId",app.Info.FkfCompanyId);

        if (Context.NOTIFICATION_SERVICE!=null) {

            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(app.Info.FkfCompanyId);
        }
        app.Info.mContext2=mContext;
        select();
    }
    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int otpCode = intent.getIntExtra("refresh",0);
                app.Info.FkfCompanyId=otpCode;
                if (Context.NOTIFICATION_SERVICE!=null) {

                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
                    nMgr.cancel(app.Info.FkfCompanyId);
                }
                select();
                Log.e("x", "otpCode :"+String.valueOf(otpCode) );

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("com.an.sms.example"));
    }
    @Override
    protected void onDestroy() {
        app.Info.SaveInt("FkfCompanyId",0);
        app.Info.topactivity=false;
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
        app.Info.SaveInt("FkfCompanyId",-1);
        app.Info.SaveInt("FkfCompanyId2",app.Info.FkfCompanyId);
              Log.e("x", "Stop");

        if(broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }

    }
    public void select()
    {
        db_query dq=new db_query();
        int idstatus=dq.getstutosmessage(db,4);



        String query = "SELECT * FROM mesMessage WHERE isDelete="+0+ " and FkfCompanyId= "+app.Info.FkfCompanyId;
        Cursor c = null;

        c = db.select(query);
        List<mesMessageChange_ResultDTO> mr=new ArrayList<>();
        int i=0;
        dq.setchengecompany(db,app.Info.FkfCompanyId);
        if(c!=null&&c.moveToFirst())
        {

            boolean isenter=false;

            do {

                String date2 = new Date(0).toString();
                mesMessageChange_ResultDTO m=new mesMessageChange_ResultDTO();
               // m.companyName= c.getString(c.getColumnIndex("companyName"));
                m.changeDateEN=new Date((!c.getString(c.getColumnIndex("changeDateEN")).equals(""))?c.getString(c.getColumnIndex("changeDateEN")):date2);
                m.FkfCompanyId=c.getInt(c.getColumnIndex("FkfCompanyId"));
                m.FkfDocument=c.getInt(c.getColumnIndex("FkfDocument"));
                m.FkfSource=c.getInt(c.getColumnIndex("FkfSource"));
                m.FkfStatusMessage=c.getInt(c.getColumnIndex("FkfStatusMessage"));
                m.FkfTypeMessage=c.getInt(c.getColumnIndex("FkfTypeMessage"));
                m.FkfUser=c.getInt(c.getColumnIndex("FkfUser"));
                m.isDelete= (c.getInt(c.getColumnIndex("isDelete"))) == 1;
                m.message=c.getString(c.getColumnIndex("message"));
                m.pkfMessage=c.getInt(c.getColumnIndex("pkfMessage"));
                m.ansswerDate= c.getString(c.getColumnIndex("ansswerDate"));
                m.ansswerDateEN=new Date((!c.getString(c.getColumnIndex("ansswerDateEN")).equals(""))?c.getString(c.getColumnIndex("ansswerDateEN")):date2);
                m.ansswerTime= c.getString(c.getColumnIndex("ansswerTime"));
                m.reciveDate= c.getString(c.getColumnIndex("reciveDate"));
                m.reciveDateEN=new Date((!c.getString(c.getColumnIndex("reciveDateEN")).equals(""))?c.getString(c.getColumnIndex("reciveDateEN")):date2);
                m.reciveTime= c.getString(c.getColumnIndex("reciveTime"));
                m.replay=c.getInt(c.getColumnIndex("replay"));
                m.replayDiscript= c.getString(c.getColumnIndex("replayDiscript"));
                m.sendDate= c.getString(c.getColumnIndex("sendDate"));
                m.sendDateEN=new Date((!c.getString(c.getColumnIndex("sendDateEN")).equals(""))?c.getString(c.getColumnIndex("sendDateEN")):date2);
                m.sendTime= c.getString(c.getColumnIndex("sendTime"));
                m.showDate= c.getString(c.getColumnIndex("showDate"));
                m.showDateEN=new Date((!c.getString(c.getColumnIndex("showDateEN")).equals(""))?c.getString(c.getColumnIndex("showDateEN")):date2);
                m.showTime= c.getString(c.getColumnIndex("showTime"));




                if(!isenter)
                {
                    i++;
                }

            //    m.statusMessage=c.getString(c.getColumnIndex("statusMessage"));
            //    m.typeMessage=c.getString(c.getColumnIndex("typeMessage"));
                 if(m.isDelete)
                   {

                       dq.del_table_mesMessage(db,m);
                   }else
                   {

                       if(m.FkfStatusMessage!=idstatus)
                       {
                           m.FkfStatusMessage=idstatus;
                           isenter=true;

                         dq.setchengeviewtype(db,m);
                       }

                     mr.add(m);
                   }

            }while (c.moveToNext());

        }
        recycler(mr,i);
    }
    //region recycle
    private void recycler(List<mesMessageChange_ResultDTO> listcompaney,int i)
    {
        if(listcompaney.size()>0) {
            try {
                recyclerView = (RecyclerView) findViewById(R.id.rec_submessage);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.removeAllViews();
                        recyclerView.setHasFixedSize(true);

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                        layoutManager.scrollToPosition(i-1);
                        //    DividerItemDecoration decoration = new DividerItemDecoration(mContext, ((LinearLayoutManager) layoutManager).getOrientation());
                        recyclerView.setLayoutManager(layoutManager);
                        //    recyclerView.addItemDecoration(decoration);

                        submessageaompaneyapaptor= new submessageCompaneyApaptor(listcompaney,mContext);
                        recyclerView.setAdapter(submessageaompaneyapaptor);

                    }
                });
            } catch (Exception ex) {
            }
        }
    }
    //endregion


}
