package com.Sadraafzar.Messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Adapter.messageCompaneyApaptor;
import com.Sadraafzar.Messenger.Classes.Risave_Data;
import com.Sadraafzar.Messenger.Classes.SignalR;
import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.Classes.db_query;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.Model.mesMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="" ;
    RelativeLayout rel_acrion;
    LinearLayout lin_search;
    ImageView img_main_search;
    ImageView img_clear,img_back;
    EditText edt_search;
    Context mContext;
    public static   Context mContext_main;
RecyclerView recyclerView;
ImageView img_hamberger;
ImageView img_user;
TextView txt_user;
messageCompaneyApaptor messageaompaneyapaptor;
    dbConnector db;
    private BroadcastReceiver broadcastReceiver;
    int idcompaney=0;
    public static String Content_KEY = "idc"; //alterative to message
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

            setContentView(R.layout.activity_main);
            Log.e(TAG,"token: "+FirebaseInstanceId.getInstance().getToken());
            mContext=MainActivity.this;
            mContext_main=mContext;
            app.Info.mContext2=mContext;
            app.Info.FkfCompanyId=0;
            app.Info.SaveInt("FkfCompanyId",app.Info.FkfCompanyId);
            db = new dbConnector(mContext, app.Database.dbName, null, 1);
           idcompaney=app.Info.notifiidcompaney;


           // sendRegistrationToServer("1wqsqasadaff");
            //===============================================================
            final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
                img_hamberger=findViewById(R.id.img_hambergerbar);
                img_hamberger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                });
//=================================Navigation=============================
                NavigationView navigationView=findViewById(R.id.navigation_view);
            View headerView = navigationView.getHeaderView(0);
img_user=headerView.findViewById(R.id.img_user);

       //  Bitmap bitmap = app.File.cropPicture;

         //   img_user.setImageBitmap(bitmap);
txt_user=headerView.findViewById(R.id.txt_user);
txt_user.setText(app.Info.FirstName);
//===============================endNavigation====================


            rel_acrion=findViewById(R.id.rel_main_actionbar);
            lin_search=findViewById(R.id.lin_main_search);
            img_main_search=findViewById(R.id.img_main_serach);
            img_clear=findViewById(R.id.img_clear);
            img_back=findViewById(R.id.img_back);
            edt_search=findViewById(R.id.edt_search);

            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(edt_search.getText().length()>0)
                    {
                        img_clear.setVisibility(View.VISIBLE);
                    }else
                    {
                        img_clear.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            img_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_search.setText("");
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rel_acrion.setVisibility(View.VISIBLE);lin_search.setVisibility(View.GONE);
                }
            });
            img_main_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rel_acrion.setVisibility(View.GONE);lin_search.setVisibility(View.VISIBLE);
                }
            });

            //================risave====================
            selectsql();
            //............................................


            registerReceiver();
            resive();
            if(idcompaney>0)
            {
                Intent intent = new Intent(mContext,subMesssage.class);
                intent.putExtra("idcompaney", idcompaney);
                app.Info.FkfCompanyId=idcompaney;
                mContext.startActivity(intent);
                Toast.makeText(mContext, String.valueOf(idcompaney), Toast.LENGTH_SHORT).show();
            }
            startService();


        }
        catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex) );

        }

    }
    private void resive()
    {
        if(app.Communication.isNetworkConnected(mContext))
        {
            Risave_Data rd=new Risave_Data(mContext,idcompaney);
            rd.onReceive();
            Log.e(TAG, "risave in service" );
            //================================================
            SignalrRisive();
        }



    }
    public void SignalrRisive()
    {

        if(app.Communication.isNetworkConnected(mContext)) {

            //باید دفعه اول که وارد میشیم newenter user رو بزاریم در بقیه وارد عدد
            SignalR sr = new SignalR(this.mContext);
            String enter = app.Info.LoadString("enterfirst");
            String newuser = app.AesEncryptionAlgorithm.DecryptDatabase(app.Info.newenteruser);
            Log.e(TAG, "signalr risave " + newuser);
            if (enter.equals("empyty")) {
                sr.onReceive(newuser);
                Log.e(TAG, "signalr type " + String.valueOf(app.Info.newenteruser));
                app.Info.SaveString("enterfirst", "full");
                //بار اول که وارد می شویم
            } else {
                sr.onReceive("3");
            }


        }

    }

    private void registerReceiver() {
        try
        {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int otpCode = intent.getIntExtra("refresh",0);
                    idcompaney=0;
                    if(otpCode==-1)
                    {



                       // SignalrRisive();
                        resive();

                    }
                    else
                    {
                      // idcompaney=otpCode;
                        resive();
                    }
                    selectsql();
                    Log.e("x", "otpCode :"+String.valueOf(otpCode) );

                }
            };
            registerReceiver(broadcastReceiver, new IntentFilter("com.an.sms.example2"));

        }catch (Exception ex)
        {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }


    @Override
    protected void onResume() {
        super.onResume();

        app.Info.SaveInt("FkfCompanyId",0);
        app.Info.mContext2=mContext;

        selectsql();


    }


    @Override
    protected void onStop() {
        super.onStop();
if(app.Info.topactivity==false)
{
    app.Info.SaveInt("FkfCompanyId",-1);
}
        if(broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        }
    }

    public void startService()
    {

       /* Intent ll24 = new Intent(mContext, RestartService.class);
        PendingIntent recurringLl24 = PendingIntent.getBroadcast(mContext, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long first_log = new Date().getTime();
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, first_log, AlarmManager.INTERVAL_DAY, recurringLl24); // Log repetition*/


        if(!isMyServiceRunning(TheService.class))
        {
            Intent intent=new Intent(this,TheService.class);
            startService(intent);
        }else
        {
            Intent intent=new Intent(this,TheService.class);
          //  stopService(intent);
            startService(intent);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        app.Info.SaveInt("FkfCompanyId",-1);
        if(SignalR.hubConnection!=null)
        {
            SignalR.hubConnection.stop();
        }


        super.onDestroy();
    }

    //region recycle
    private void recycler(List<mesCompanyChange_ResultDTO> listcompaney)
    {try {
        if(listcompaney.size()>0) {
            try {
                recyclerView = (RecyclerView) findViewById(R.id.rec_main);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                DividerItemDecoration decoration = new DividerItemDecoration(mContext, ((LinearLayoutManager) layoutManager).getOrientation());
                decoration.setDrawable(mContext.getResources().getDrawable(R.drawable.driver_line));
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(decoration);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.removeAllViews();
                        messageaompaneyapaptor= new messageCompaneyApaptor(listcompaney,mContext);
                        recyclerView.setAdapter(messageaompaneyapaptor);
                    }
                });
            } catch (Exception ex) {
            }
        }
    }catch (Exception ex)
    {
        Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

     }
    //endregion




    //region select sql

    public void selectsql() {

        try {
            //  String query = "SELECT * FROM mesCompany ORDER BY column_2 DESC";
            String query = "SELECT * FROM mesCompany";
            Cursor c = null;
            c = db.select(query);
            List<mesCompanyChange_ResultDTO> mr=new ArrayList<>();

            if(c.moveToNext())
            {

                do {
                    mesCompanyChange_ResultDTO m=new mesCompanyChange_ResultDTO();
                    m.companyName= c.getString(c.getColumnIndex("companyName"));
                    m.CountAll=c.getInt(c.getColumnIndex("CountAll"));
                    m.CountRecive=c.getInt(c.getColumnIndex("CountRecive"));
                    m.CountSend=c.getInt(c.getColumnIndex("CountSend"));
                    m.CountShow=c.getInt(c.getColumnIndex("CountShow"));
                    m.number=c.getString(c.getColumnIndex("number"));
                    m.pkfCompany=c.getInt(c.getColumnIndex("pkfCompany"));
                    db_query dq=new db_query();
                    mesMessageChange_ResultDTO mm=dq.select_lasted_message(mContext, m.pkfCompany);
                    m.CountShow=dq.select_cunt_dont_read(mContext, m.pkfCompany);
                    m.lastmessage=mm.message;
                    m.timesen=mm.sendTime;
                    mr.add(m);
                }while (c.moveToNext());

            }

            recycler(mr);
        }catch (Exception ex)
        {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        }

    //endregion


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
