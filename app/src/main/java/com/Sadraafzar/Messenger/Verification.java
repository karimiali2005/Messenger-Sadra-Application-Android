package com.Sadraafzar.Messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Verification extends AppCompatActivity {

    Button btn_enter;
    ImageView img_close;
    RelativeLayout rel_refresh;
    EditText edt_code;
Context mContext;
    dbConnector db;
    TextView txt_mobile;

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
            setContentView(R.layout.activity_verification);
            mContext=this;
            db = new dbConnector(this, app.Database.dbName, null, 1);
            edt_code=findViewById(R.id.edt_dialog_code);
            btn_enter=findViewById(R.id.btn_dialog_enter);
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(app.Communication.CheckCommunication(mContext))
                    {
                        if(validateProductRequest_Comment())
                        {
                            Send();
                        }
                    }


                }
            });
rel_refresh=findViewById(R.id.rel_verification_refresh);
rel_refresh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       if(app.Communication.CheckCommunication(mContext))
       {
           SendRefreshVerificationCode();
       }

        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SendSMSDelay), Toast.LENGTH_LONG).show();
    }
});
         img_close=findViewById(R.id.img_close);
         img_close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 app.Info.UserID = 0;
                 app.Info.UserName = "";
                 db.delete_Tables("User");
                 Intent intent = new Intent(getApplicationContext(), Logine.class);
                 startActivity(intent);

             }
         });
           txt_mobile=findViewById(R.id.txt_verification_mobile);
            txt_mobile.setText("شماره ثبت شده " + app.Info.UserName + " می باشد ");
        }catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));

        }


    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateProductRequest_Comment() {
        TextInputLayout floatingTaskTitleLabel = (TextInputLayout) findViewById(R.id.txtinput_login_mobile);
        if (edt_code.getText().toString().trim().length()<4) {
            floatingTaskTitleLabel.setError(getString(R.string.ErrorCode));
            requestFocus(floatingTaskTitleLabel);
            return false;
        } else {
            floatingTaskTitleLabel.setErrorEnabled(false);
        }

        return true;
    }


    //region Send
    void Send()
    {
        try {

            APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
            String tokenid= FirebaseInstanceId.getInstance().getToken();
            Call<ResponseBody> call = apiInterface.ValidVerificationCode(app.Info.UserID,Integer.parseInt(edt_code.getText().toString()),app.AesEncryptionAlgorithm.EncryptServicePublicKeySend(app.Info.PublicKey),tokenid);
            app.Communication.Dialog.onCreateDialog(mContext);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (response.isSuccessful()) {

                            StringBuilder result = new StringBuilder();
                            BufferedReader reader =
                                    new BufferedReader(new InputStreamReader(response.body().byteStream()), 65728);
                            String line = null;

                            try {
                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String[] separated = result.toString().split(":::");
                            if (separated[1].equals("0")) {
                                Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NoValidPublicKey), Toast.LENGTH_SHORT).show();
                                app.Info.UserID = 0;
                                app.Info.UserName = "";
                                //     db.delete_Tables("User");
                                Intent intent = new Intent(getApplicationContext(), Porofile.class);
                                startActivity(intent);
                            } else {
                                if (separated[0].equals("True")) {
                                    // Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SendDataSuccess), Toast.LENGTH_SHORT).show();


                                    ContentValues values = new ContentValues();
                                    values.put("ActiveCode", 1);
                                    app.Info.ActiveCode = 1;

                                    app.Info.PrivateKey = app.AesEncryptionAlgorithm.DecryptServicePrivateKeyReceive(separated[2]);
                                    values.put("PrivateKey", app.AesEncryptionAlgorithm.EncryptDatabase(app.Info.PrivateKey));

                                    app.Info.PrivateKeyRefresh = app.AesEncryptionAlgorithm.DecryptServicePrivateKeyReceive(separated[3]);
                                    values.put("PrivateKeyRefresh", app.AesEncryptionAlgorithm.EncryptDatabase(app.Info.PrivateKeyRefresh));

                                    app.Info.PrivateKeyExpire = app.AesEncryptionAlgorithm.DecryptServicePrivateKeyReceive(separated[4]);
                                    values.put("PrivateKeyExpire", app.AesEncryptionAlgorithm.EncryptDatabase(app.Info.PrivateKeyExpire));


                                    String strFilter = "UserID=" + app.Info.UserID;
                                    Boolean status = db.update("User", values, strFilter);
                                    if (status) {
                                        Intent intent = new Intent(getApplicationContext(), Porofile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NoValidVerificationCode), Toast.LENGTH_SHORT).show();
                                    edt_code.clearFocus();
                                }
                            }


                        } else {
                            int code = response.code();
                            Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, code), Toast.LENGTH_LONG).show();
                            app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.ManagedError, app.ErrorHandling.getMethodName(), "ResponseCode:" + code + "-" + response.errorBody().toString());
                        }
                    }
                    catch (Exception ex)
                    {
                        app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
                    }

                    app.Communication.Dialog.tryDismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t));
                    Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, 0), Toast.LENGTH_LONG).show();
                    app.Communication.Dialog.tryDismiss();
                }
            });
        }
        catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
        }
    }
    //endregion
    //region SendRefreshVerificationCode
    void SendRefreshVerificationCode()
    {
        try {

            APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
            Call<ResponseBody> call = apiInterface.RegisterUser(app.Info.UserName,1,app.Info.IP4,app.Info.VersionCode);
            app.Communication.Dialog.onCreateDialog(mContext);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (response.isSuccessful()) {


                        } else {
                            int code = response.code();
                            Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, code), Toast.LENGTH_LONG).show();
                            app.ErrorHandling.ErrorManager(mContext, app.ErrorHandling.ErrorType.ManagedError, app.ErrorHandling.getMethodName(), "ResponseCode:" + code + "-" + response.errorBody().toString());
                        }
                    }
                    catch (Exception ex)
                    {
                        app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
                    }
                    app.Communication.Dialog.tryDismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t));
                    Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, 0), Toast.LENGTH_LONG).show();
                    app.Communication.Dialog.tryDismiss();
                }
            });
        }
        catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
        }
    }
    //endregion
}
