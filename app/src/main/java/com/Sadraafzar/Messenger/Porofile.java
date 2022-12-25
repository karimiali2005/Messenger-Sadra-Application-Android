package com.Sadraafzar.Messenger;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.Image.ImageLoader;
import com.Sadraafzar.Messenger.Image.ServerResponseUpload;
import com.Sadraafzar.Messenger.Model.UserViewModel;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Porofile extends AppCompatActivity {
    Button btnRegisterInfo;
    EditText txtFirstName,txtLastName;
    dbConnector db;
    String mediaPath;
    ImageView imgProfile;
    ImageLoader imgLoader;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    Bitmap bitmap;
    Context mContext;
    UserViewModel _user=new UserViewModel();


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
            mContext=this;
            setContentView(R.layout.activity_porofile);

            db = new dbConnector(this, app.Database.dbName , null ,1);

            txtFirstName=(EditText)findViewById(R.id.edt_profile_name);
            txtLastName=(EditText)findViewById(R.id.edt_profile_lastname);
            resive();
            btnRegisterInfo=(Button)findViewById(R.id.btn_profile_enter);
            btnRegisterInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateFirstName()&&validateLastName())
                    {
                        Send();
                    }

                    Toast.makeText(Porofile.this, "send Info", Toast.LENGTH_SHORT).show();
                }
            });

            imgProfile=(ImageView)findViewById(R.id.img_profile);
            imgLoader = new ImageLoader(this);

            String url = app.Communication.baseUrlService + app.Communication.apiGetUserPic+"?userId="+app.Info.UserID;

            imgLoader.DisplayPicture(app.Info.UserID,url, imgProfile);
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), PictureCrop.class);
                    startActivityForResult(i, 1);
                    // GetImageFromGallery();
                }
            });
        }catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {


        }

        /*if (requestCode == 0 && resultCode == RESULT_OK) {


            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();


                // کد خودم
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

            }
        }
        else*/
        if (requestCode == 1) {

            if (data != null) {





               /* Bundle bundle = data.getExtras();

               bitmap = bundle.getParcelable("pic");*/
                bitmap = app.File.cropPicture;
                mediaPath = data.getStringExtra("mediaPath");

                imgProfile.setImageBitmap(bitmap);

                uploadFile();

            }
        }
    }

    /*  public void ImageCropFunction() {

          // Image Crop Code
          try {
              CropIntent = new Intent("com.android.camera.action.CROP");

              CropIntent.setDataAndType(uri, "image");

              CropIntent.putExtra("crop", "true");
              CropIntent.putExtra("outputX", 500);
              CropIntent.putExtra("outputY", 500);
              CropIntent.putExtra("aspectX", 4);
              CropIntent.putExtra("aspectY", 4);
              CropIntent.putExtra("scaleUpIfNeeded", true);
              CropIntent.putExtra("return-data", true);

              startActivityForResult(CropIntent, 1);

          } catch (ActivityNotFoundException e) {

          }
      }
  */
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Porofile.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Porofile.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }



    // Uploading Image/Video
    private void uploadFile() {
      //  showDialog(1);

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        // Parsing any Media type file
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), byteArray);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",  app.Info.UserID+"_"+file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),  app.Info.UserID+"_"+file.getName());



        APIInterface getResponse = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
        Call<ServerResponseUpload> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponseUpload>() {
            @Override
            public void onResponse(Call<ServerResponseUpload> call, Response<ServerResponseUpload> response) {
                ServerResponseUpload serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            //    dismissDialog(1);
            }

            @Override
            public void onFailure(Call<ServerResponseUpload> call, Throwable t) {

            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateFirstName() {
        TextInputLayout floatingTaskTitleLabel = (TextInputLayout) findViewById(R.id.txtinput_profile_name);
        if (txtFirstName.getText().toString().trim().isEmpty()) {
            floatingTaskTitleLabel.setError(getString(R.string.ErrorFirstName));
            requestFocus(txtFirstName);
            return false;
        } else {
            floatingTaskTitleLabel.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLastName() {
        TextInputLayout floatingTaskTitleLabel = (TextInputLayout) findViewById(R.id.txtinput_porofile_lastname);
        if (txtLastName.getText().toString().trim().isEmpty()) {
            floatingTaskTitleLabel.setError(getString(R.string.ErrorLastName));
            requestFocus(txtLastName);
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

            _user.setUserID(app.Info.UserID);
            _user.setUserName(app.Info.UserName);
            _user.setFirstName(txtFirstName.getText().toString());
            _user.setLastName(txtLastName.getText().toString());




            Call<ResponseBody> call = apiInterface.RegisterUserInfo(_user);
            app.Communication.Dialog.onCreateDialog(mContext);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {

                            ContentValues values = new ContentValues();

                            values.put("UserName", app.Info.UserName);
                            values.put("FirstName", _user.FirstName);
                            values.put("LastName", _user.LastName);
                            values.put("UserType", _user.UserType);
                            values.put("Address", _user.Address);
                            app.Info.FirstName = _user.FirstName;
                            app.Info.LastName = _user.LastName;
                            app.Info.Address = _user.Address;
                            app.Info.UserType = _user.UserType;

                            String strFilter = "UserID=" + app.Info.UserID;
                            Boolean status = db.update("User", values, strFilter);

                            if (status) {
                                Intent intent = new Intent(getApplicationContext(), Logine.class);
                                intent.putExtra("FirstRunKartabl", true);
                                startActivity(intent);
                                finish();
                            }


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
    //region Resive
    private void resive()
    {
        try {
            if(app.Communication.CheckCommunication(mContext))
            {
                APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);

                Call<UserViewModel> call = apiInterface.GetUserInfo(app.Info.UserID);
                app.Communication.Dialog.onCreateDialog(mContext);
                call.enqueue(new Callback<UserViewModel>() {
                    @Override
                    public void onResponse(Call<UserViewModel> call, Response<UserViewModel> response) {
                        if(response.isSuccessful())
                        {
                            txtFirstName.setText(response.body().FirstName);
                            txtLastName.setText(response.body().LastName);
                        }
                        app.Communication.Dialog.tryDismiss();
                    }

                    @Override
                    public void onFailure(Call<UserViewModel> call, Throwable t) {
                        app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t));
                        Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, 0), Toast.LENGTH_LONG).show();
                        app.Communication.Dialog.tryDismiss();
                    }
                });
            }

        }catch (Exception ex)
        {
            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex));


        }

    }
    //endregion

//old
   /* private void Send() {
        if (isNetworkConnected()) {

            if (!validateFirstName()) {
                return;
            }

            if (!validateLastName()) {
                return;
            }

            UserViewModel user=new UserViewModel(app.Info.UserID,app.Info.UserName,txtFirstName.getText().toString(),txtLastName.getText().toString());
            final Final final1 = new Final(user);
            final1.execute();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    if ( final1.getStatus() == AsyncTask.Status.RUNNING ) {
                        final1.cancel(true);
                        dismissDialog(1);
                        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.PleaseRepeate), Toast.LENGTH_SHORT).show();
                    }

                }


            }, app.Communication.TimeOut );
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getBaseContext().getString(R.string.NoInternetConnectionTitle))//"No Internet Connection")
                    //.setMessage("It looks like your internet connection is off. Please turn it " +
                    //"on and try again")
                    .setMessage(getBaseContext().getString(R.string.NoInternetConnectionBody))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }*/



}