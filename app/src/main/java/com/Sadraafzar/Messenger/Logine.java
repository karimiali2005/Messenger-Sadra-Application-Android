package com.Sadraafzar.Messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Logine extends AppCompatActivity {
Button btn_login;
EditText edt_mobile;
Context context;
    dbConnector db;

    //region FontOverride
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion
    @Override
    protected void onResume() {
app.Info.mContext2=this;
        try {

           /* db.delete_Tables("mesCompany");
            db.delete_Tables("mesMessage");
            db.delete_Tables("mesStatus");
            db.delete_Tables("mesType");
            db.delete_Tables("ChangeDate");
            db.delete_Tables("Changetype");*/




            Boolean status = getSharedPreferences(app.File.name, 0).getBoolean(app.File.login, false);

            if (status) {
                String query = "SELECT * FROM User ";
                Cursor c = null;
                c = db.select(query);
                if (c.moveToNext()) {


                    app.Info.UserName = c.getString(c.getColumnIndex("UserName")).replaceFirst("0","");
                    app.Info.SaveString("UserName",app.Info.UserName);
                    app.Info.FirstName = c.getString(c.getColumnIndex("FirstName"));
                    app.Info.LastName = c.getString(c.getColumnIndex("LastName"));
                    app.Info.UserID = c.getInt(c.getColumnIndex("UserID"));
                    app.Info.SaveInt("UserID",app.Info.UserID);
                    app.Info.ActiveCode = c.getInt(c.getColumnIndex("ActiveCode"));
                    app.Info.Address=c.getString(c.getColumnIndex("Address"));
                    app.Info.UserType=c.getInt(c.getColumnIndex("UserType"));
                    app.Info.newenteruser=c.getString(c.getColumnIndex("newUser"));
                    try {
                        app.Info.PublicKey = app.AesEncryptionAlgorithm.DecryptDatabase(c.getString(c.getColumnIndex("PublicKey")));
                    } catch (Exception ex) {
                        app.Info.PublicKey = "";
                    }
                    try {
                        app.Info.PrivateKey = app.AesEncryptionAlgorithm.DecryptDatabase(c.getString(c.getColumnIndex("PrivateKey")));
                        app.Info.SaveString("PrivateKey",app.Info.PrivateKey);
                        app.Info.PrivateKeyRefresh = app.AesEncryptionAlgorithm.DecryptDatabase(c.getString(c.getColumnIndex("PrivateKeyRefresh")));
                        app.Info.PrivateKeyExpire = app.AesEncryptionAlgorithm.DecryptDatabase(c.getString(c.getColumnIndex("PrivateKeyExpire")));
                    } catch (Exception ex) {

                    }
                }
            String str1=app.Info.PublicKey;
            if (!(str1 == null || str1.isEmpty() || str1.equalsIgnoreCase("null"))) {


                String str = app.Info.FirstName;
                if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")) && app.Info.ActiveCode == 1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("FirstRunKartabl", true);
                    startActivity(intent);

                    finish();
                }
                if (app.Info.UserID != 0 && app.Info.ActiveCode == 0) {
                    startActivity(new Intent(this, Verification.class));
                    finish();
                }
                if ((str == null || str.isEmpty() || str.equalsIgnoreCase("null")) && app.Info.ActiveCode == 1) {
                   // startActivity(new Intent(this, UserInfoActivity.class));
                    startActivity(new Intent(this, Porofile.class));
                    finish();
                }
            }
            else
            {
                app.Info.UserID=0;
                app.Info.UserName="";
                app.Info.FirstName ="";
                app.Info.LastName ="";
                app.Info.ActiveCode=0;
              //  db.drop_Tables("User");
                db.create_Tables();
            }

            }
        }
        catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
        }

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            db = new dbConnector(this, app.Database.dbName, null, 1);
            setContentView(R.layout.activity_logine);
            context=Logine.this;
            btn_login=findViewById(R.id.btn_logine_enter);
            edt_mobile=findViewById(R.id.edt_logine_mobile);
            //===================================================
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(validateProductRequest_Comment())
                    {
                       Send();
                    }
                }
            });

        }catch (Exception ex)
        {


        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateProductRequest_Comment() {
        TextInputLayout floatingTaskTitleLabel = (TextInputLayout) findViewById(R.id.txtinput_login_mobile);
        if (!app.chek_edt.mobil(edt_mobile.getText().toString().trim())) {
            floatingTaskTitleLabel.setError(getString(R.string.Errormobile));
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

            Call<ResponseBody> call = apiInterface.RegisterUser(edt_mobile.getText().toString(), 0, app.Info.IP4, app.Info.VersionCode);
            app.Communication.Dialog.onCreateDialog(context);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            String query = "SELECT * FROM User ";
                            Cursor c = null;
                            c = db.select(query);
                            if (c.moveToNext()) {


                                app.Info.UserID = c.getInt(c.getColumnIndex("UserID"));
                                app.Info.UserName = c.getString(c.getColumnIndex("UserName"));
                                app.Info.FirstName = c.getString(c.getColumnIndex("FirstName"));
                                app.Info.LastName = c.getString(c.getColumnIndex("LastName"));
                                app.Info.PublicKey = app.AesEncryptionAlgorithm.DecryptDatabase(c.getString(c.getColumnIndex("PrivateKey")));


                            }
                            c.close();

                            Boolean status = true;
                            String str = app.Info.UserName;


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

                            if ((str == null || str.isEmpty() || str.equalsIgnoreCase("null"))) {
                                ContentValues values = new ContentValues();

                                values.put("UserID", separated[0]);
                                String str2 = edt_mobile.getText().toString();
                                if (str2.startsWith("0"))
                                    str2 = str2.substring(1);
                                values.put("UserName", str2);
                                app.Info.UserID = Integer.parseInt(separated[0]);
                                app.Info.UserName = edt_mobile.getText().toString();

                                if (separated[1].toString().equals("1"))
                                    app.Info.NewUser = true;

                                app.Info.newenteruser=separated[1].toString();
                                values.put("newUser", app.AesEncryptionAlgorithm.EncryptDatabase(app.Info.newenteruser));

                                app.Info.PublicKey = app.AesEncryptionAlgorithm.DecryptServicePublicKeyReceive(separated[2]);
                                values.put("PublicKey", app.AesEncryptionAlgorithm.EncryptDatabase(app.Info.PublicKey));


                                status = db.insert("User", values);
                            }

                            getSharedPreferences(app.File.name, MODE_PRIVATE)
                                    .edit()
                                    .putBoolean(app.File.login, true)
                                    .commit();
                            if (status) {
                                Intent intent = new Intent(getApplicationContext(), Verification.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                        else {
                            int code = response.code();
                            Toast.makeText(getBaseContext(), app.Communication.MessageError(context, code), Toast.LENGTH_LONG).show();
                            app.ErrorHandling.ErrorManager(context, app.ErrorHandling.ErrorType.ManagedError, app.ErrorHandling.getMethodName(), "ResponseCode:" + code + "-" + response.errorBody().toString());
                        }
                    }
                    catch (Exception ex)
                    {
                        app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
                    }
                    app.Communication.Dialog.tryDismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t) );
                    Toast.makeText(getBaseContext(), app.Communication.MessageError(context, 0), Toast.LENGTH_LONG).show();
                    app.Communication.Dialog.tryDismiss();
                }
            });
        }
        catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(context,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));
        }
    }
    //endregion




}
