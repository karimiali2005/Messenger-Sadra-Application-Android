package com.Sadraafzar.Messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Model.LoginLimitViewModel;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Spelash extends AppCompatActivity {
    private static final String TAG =" sepelash " ;
    Context mContext;
    private String Error = "";
    private String VersionName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_spelash);
            mContext=this;
            //=========================================================
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {

                } else {

                    if(extras.getInt("idc")!=0&&extras.getInt("idc")!=-1)
                    {
                        app.Info.notifiidcompaney= extras.getInt("idc");
                    }
                }
            }
            else {
                if((int) savedInstanceState.getSerializable("idc")!=0&&(int) savedInstanceState.getSerializable("idc")!=-1)
                {
                    app.Info.notifiidcompaney= (int) savedInstanceState.getSerializable("idc");

                }

            }
            //=========================================================
            String url = PreferenceManager.getDefaultSharedPreferences(this).getString("ServiceUrl",APIClient.BASE_URL);
            app.StartApp(url);


            getIp();
            getversion();
            LoginLimitViewModel loginLimitViewModel=getVesion_fromserver();
            onPostExecute(loginLimitViewModel);
            Intent intent = new Intent(this, Logine.class);
            startActivity(intent);
            finish();

        }catch (Exception ex)
        {
            if(isActivityRunning(Spelash.class)) {
                app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.UnManagedError, app.ErrorHandling.getMethodName(), app.ErrorHandling.ShowError(ex));
            }
        }



    }

    private void getversion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            app.Info.VersionCode=packageInfo.versionCode;;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            app.Info.VersionCode=1;
        }
    }

    private void getIp() {
        try
        {
            @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            if (!ipAddress.toString().trim().equals(""))
                app.Info.IP4 = ipAddress;
            else
                app.Info.IP4 = "1.1.1.1";
        }catch (Exception ex)
        {

        }



    }

    //get Version
    private LoginLimitViewModel getVesion_fromserver()
    {
        final LoginLimitViewModel[] resualt = {null};
        try {

            APIInterface apiInterface = APIClient.getClient(app.Communication.VersioningApiVersion).create(APIInterface.class);

            Call<LoginLimitViewModel> call = apiInterface.GetLoadVersion();
          //  app.Communication.Dialog.onCreateDialog(this);
            int versionCode = app.Info.VersionCode;
            final int finalVersionCode = versionCode;
            call.enqueue(new Callback<LoginLimitViewModel>() {
                @Override
                public void onResponse(Call<LoginLimitViewModel> call, Response<LoginLimitViewModel> response) {
                    try {


                        if(response.isSuccessful())
                        {
                            if(response.body().VersionCodeForceInstalling> finalVersionCode)
                            {
                                Error = "NewVersionForce";
                                VersionName=response.body().VersionNameForceInstalling;
                               resualt[0] =null;
                            }
                            if(response.body().VersionCodeNoForceInstalling> finalVersionCode)
                            {
                                Error = "NewVersionNoForce";
                                VersionName=response.body().VersionNameNoForceInstalling;
                                resualt[0] =response.body();
                            }



                        }

                    } catch (Exception ex)
                    {
                        if(isActivityRunning(Spelash.class)) {
                            app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.UnManagedError, app.ErrorHandling.getMethodName(), app.ErrorHandling.ShowError(ex));

                        }
                    }
                    if(isActivityRunning(Spelash.class)) {
//                        app.Communication.Dialog.onCreateDialog(mContext);
                    }
                }

                @Override
                public void onFailure(Call<LoginLimitViewModel> call, Throwable t) {
                    if(isActivityRunning(Spelash.class)) {

                        app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.ManagedError, app.ErrorHandling.getMethodName(), app.ErrorHandling.GetErrorOnFailure(t));
                        Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, 0), Toast.LENGTH_LONG).show();
                     //   app.Communication.Dialog.tryDismiss();
                    }
                }
            });

          //  app.Communication.Dialog.onCreateDialog(mContext);
            return resualt[0];
        }catch (Exception ex)
        {
            if(isActivityRunning(Spelash.class)) {
                app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.UnManagedError, app.ErrorHandling.getMethodName(), app.ErrorHandling.ShowError(ex));
            }
        }
        return resualt[0];

    }

    protected void onPostExecute(LoginLimitViewModel results) {
        try{
            switch (Error)
            {
                case "ConnectToServer":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorServer) , Toast.LENGTH_LONG).show();
                    exit();
                    break;
                case "ErrorNetwork":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorNetwork), Toast.LENGTH_LONG).show();
                    exit();
                    break;
                case "ParseData":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorParseJson), Toast.LENGTH_LONG).show();
                    exit();
                    break;
                case "NewVersionForce":
                    View v= app.Dialog_.dialog_creat(mContext,R.layout.dialog_authentication_code);
                    TextView txtmessage=v.findViewById(R.id.txt_message);
                    Button btn_dialog_enter=v.findViewById(R.id.btn_dialog_enter);
                    txtmessage.setText(R.string.NewVersionForce);
                    final AlertDialog alert=app.Dialog_.show_dialog(mContext,v);
                    btn_dialog_enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            app.Dialog_.dimos_dialog(alert);
                        }
                    });
                    break;
                case "NewVersionNoForce":
                    // Toast.makeText(getBaseContext(), getResources().getString(R.string.NewVersionNoForce), Toast.LENGTH_LONG).show();
                    //  AlertDialogVersion(getResources().getString(R.string.NewVersionNoForce),false);
                    View v1= app.Dialog_.dialog_creat(mContext,R.layout.dialog_authentication_code);
                    TextView txtmessage1=v1.findViewById(R.id.txt_message);
                    Button btn_dialog_enter1=v1.findViewById(R.id.btn_dialog_enter);
                    txtmessage1.setText(R.string.NewVersionNoForce);
                    final AlertDialog alert1=app.Dialog_.show_dialog(mContext,v1);
                    btn_dialog_enter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            app.Dialog_.dimos_dialog(alert1);
                        }
                    });

                    break;
            }
            if(Error.toString().equals("")) {
                if (results!=null) {


                    Intent i = new Intent(mContext, Logine.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(mContext,"خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();
                    exit();
                }
            }

        }catch (Exception ex)
        {

        }


        // dismissDialog(1);

    }

    void exit()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    protected Boolean isActivityRunning(Class activityClass)
    {
      /*  ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }*/

        return false;
    }
}
