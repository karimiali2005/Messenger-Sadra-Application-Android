package com.Sadraafzar.Messenger.Image;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Classes.dbConnector;
import com.Sadraafzar.Messenger.Model.UserPicViewModel;
import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageLoader {

    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Context mContext;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    dbConnector db;

    String _url;
    ImageView _imageView;
    int _userProfileId;

    public ImageLoader(Context context){
        mContext=context;
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
        db = new dbConnector(mContext, app.Database.dbName , null ,1);
    }

   // final int stub_id= R.drawable.img_profilepic;
     int stub_id= R.drawable.ic_user;

    public void DisplayPicture(int userId ,String url, ImageView imageView)
    {
        _url=url;
        _imageView=imageView;
        _userProfileId=userId;
        ReceiveUserPicName();
    }

    public void DisplayImage(String url, ImageView imageView)
    {


        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);

//from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;

//from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();

            //MY addHeader

            if(!app.Info.PrivateKey.toString().trim().equals("")) {
                String str= app.AesEncryptionAlgorithm.EncryptServicePrivateKeySend(app.Info.PrivateKey);
                str=str.substring(0,str.length()-1);
                conn.setRequestProperty("Authorize", str);
                conn.setRequestProperty("User", String.valueOf(app.Info.UserID));
            }



            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
//decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

//Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=200;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

//decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    //region ReceiveUserPicName
    private void ReceiveUserPicName() {
        if (isNetworkConnected()) {
            Receive();
            /*final Receive receive = new Receive();
            receive.execute();*/
        }

    }

    //region Receive
    void Receive()
    {
        try {
            APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
            Call<UserPicViewModel> call = apiInterface.GetUserPicName(app.Info.UserID);
            // app.Communication.Dialog.onCreateDialog(mContext);

            call.enqueue(new Callback<UserPicViewModel>() {
                @Override
                public void onResponse(Call<UserPicViewModel> call, Response<UserPicViewModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            UserPicViewModel userPicViewModel=response.body();



                            String  query  = "SELECT * FROM PictureCache Where UserID="+userPicViewModel.UserID;
                            Cursor c = null;
                            c = db.select(query);
                            if (c.moveToNext()) {
                                if(c.getString(c.getColumnIndex("UserPicName"))!=null&&c.getString(c.getColumnIndex("UserPicName")).trim().equals(userPicViewModel.UserPicName.trim()))
                                {
                                    c.close();
                                    DisplayImage(_url,_imageView);
                                }
                                else
                                {
                                    c.close();
                                    ContentValues values=new ContentValues();
                                    values.put("UserPicName",userPicViewModel.UserPicName.trim());
                                    String strFilter="UserID="+userPicViewModel.UserID;
                                    db.update("PictureCache",values,strFilter);
                                    FileCache fileCache=new FileCache(mContext);
                                    fileCache.remove(_url);
                                    DisplayImage(_url,_imageView);

                                }
                            }
                            else
                            {
                                c.close();
                                ContentValues values=new ContentValues();
                                values.put("UserPicName",userPicViewModel.UserPicName.trim());
                                values.put("UserID",userPicViewModel.UserID);
                                db.insert("PictureCache",values);
                                DisplayImage(_url,_imageView);
                            }

                        } else {
                            int code = response.code();
                            //  Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, code), Toast.LENGTH_LONG).show();
                            app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.ManagedError, app.ErrorHandling.getMethodName(), "ResponseCode:" + code + "-" + response.errorBody().toString());
                        }
                    }
                    catch (Exception ex)
                    {
                        app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.UnManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.ShowError(ex) );
                    }

                    //app.Communication.Dialog.tryDismiss();
                }

                @Override
                public void onFailure(Call<UserPicViewModel> call, Throwable t) {
                    app.ErrorHandling.ErrorManager(mContext,app.ErrorHandling.ErrorType.ManagedError,app.ErrorHandling.getMethodName(),app.ErrorHandling.GetErrorOnFailure(t) );
                    //       Toast.makeText(getBaseContext(), app.Communication.MessageError(mContext, 0), Toast.LENGTH_LONG).show();
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




   /* public class Receive extends AsyncTask<String, Integer, UserPicViewModel> {

        private String Error = "";
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected UserPicViewModel doInBackground(String... params) {
            try {
                Error="";
                String url = app.Communication.baseUrl + app.Communication.apiGetUserPicName+"?userId=" + _userProfileId ;

                String results = Methods.httpGet(url);
                if(results.toLowerCase().contains("error"))
                {
                    Error = "ConnectToServer";
                    return null;
                }
                else {

                    Gson gson = new Gson();
                    UserPicViewModel userPicViewModel = gson.fromJson(results, UserPicViewModel.class);


                    return userPicViewModel;
                }


            } catch (NetworkOnMainThreadException ex)
            {
                Error = "ErrorNetwork";
                return null;
            }
            catch (JsonParseException e) {
                Error = "ParseData";
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //Not sure what to do here
        }

        protected void onPostExecute(UserPicViewModel results) {

            if(Error.toString().equals("")) {
                if (results != null) {
                    String  query  = "SELECT * FROM PictureCache Where UserID="+results.UserID;
                    Cursor c = null;
                    c = db.select(query);
                    if (c.moveToNext()) {
                        if(c.getString(c.getColumnIndex("UserPicName")).trim().equals(results.UserPicName.trim()))
                        {
                            c.close();
                            DisplayImage(_url,_imageView);
                        }
                        else
                        {
                            c.close();
                            ContentValues values=new ContentValues();
                            values.put("UserPicName",results.UserPicName.trim());
                            String strFilter="UserID="+results.UserID;
                            db.update("PictureCache",values,strFilter);
                            FileCache fileCache=new FileCache(mContext);
                            fileCache.remove(_url);
                            DisplayImage(_url,_imageView);

                        }
                    }
                    else
                    {
                        c.close();
                        ContentValues values=new ContentValues();
                        values.put("UserPicName",results.UserPicName.trim());
                        values.put("UserID",results.UserID);
                        db.insert("PictureCache",values);
                        DisplayImage(_url,_imageView);
                    }
                }
            }


        }
    }*/
    //endregion

}