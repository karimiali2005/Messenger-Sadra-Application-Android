package com.Sadraafzar.Messenger.Classes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.Sadraafzar.Messenger.R;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 3/26/2018.
 */

public class app {
    public static  class AesEncryptionAlgorithm
    {
        public static String EncryptServiceUserSend(String plainTex)
        {
            String key = "%8567& 7hr67p*)@+ 923*";
            try {
                return encrypt(plainTex,key).toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return "";
        }
        public static String DecryptServiceUserReceive(String plainTex)
        {
            if(plainTex==null)
            {
                plainTex="null";
            }
            String key = "%8567& 7hr67p*)@+ 923*";

            try {
                return decrypt(plainTex,key).toString();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        //......................................................................
        private static final String SecretKeyDatabase = "$12&@34%&Key%@*";
        public static String EncryptDatabase(String plainText)
        {
            try {
                return encrypt(plainText,SecretKeyDatabase).toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return "";
        }

        public static String DecryptDatabase(String plainText)
        {

            try {
                return decrypt(plainText,SecretKeyDatabase).toString();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        private static final String SecretServicePublicKeyReceive = "@rt$% % &7U*)#%y) y&*";
        public static String DecryptServicePublicKeyReceive(String plainText)
        {

            try {
                return decrypt(plainText,SecretServicePublicKeyReceive).toString();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        private static final String SecretServicePrivateKeySend = "!@#@$)+_)_5674CDfwer23*vc 3";
        public static String EncryptServicePrivateKeySend(String plainText)
        {
            try {
                return encrypt(plainText,SecretServicePrivateKeySend).toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return "";
        }


        private static final String SecretServicePrivateKeyReceive = "&*FDGDFGD4ssde&#@# 0(4%";
        public static String DecryptServicePrivateKeyReceive(String plainText)
        {

            try {
                return decrypt(plainText,SecretServicePrivateKeyReceive).toString();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }


        private static final String SecretServicePublicKeySend = "!67 er &#p )+=23C swe )d$";
        public static String EncryptServicePublicKeySend(String plainText)
        {
            try {
                return encrypt(plainText,SecretServicePublicKeySend).toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return "";
        }



        private static final String characterEncoding = "UTF-8";
        private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
        private static final String aesEncryptionAlgorithm = "AES";

        private static String encrypt(String plainText, String key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
            byte[] plainTextbytes = plainText.getBytes(characterEncoding);
            byte[] keyBytes = getKeyBytes(key);
            return Base64.encodeToString(encrypt(plainTextbytes,keyBytes, keyBytes), Base64.DEFAULT);
        }

        private static String decrypt(String encryptedText, String key) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
            byte[] cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            byte[] keyBytes = getKeyBytes(key);
            return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
        }

        private static  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
        {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
            cipherText = cipher.doFinal(cipherText);
            return cipherText;
        }

        private static byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
        {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            plainText = cipher.doFinal(plainText);
            return plainText;
        }

        private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
            byte[] keyBytes= new byte[16];
            byte[] parameterKeyBytes= key.getBytes(characterEncoding);
            System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
            return keyBytes;
        }
    }

        //reternpublic key


    public  static class File
    {
        public static final String name = "fileShare";
        public static final String login = "login";
        public static Bitmap cropPicture;
    }
    public static class Info
    {
        public static int UserID =0;
        public static String UserIDtbScUSers;
        public static String UserName;
        public static String FullName;
        public static boolean FirstLogin=false;
        public static String FirstName;
        public static String LastName;
        public static int UserType;
        public static String Address;
        public static int    ActiveCode;
        public static Boolean NewUser=false;
        public static String PublicKey="";
        public static String PrivateKey="";
        public static String newenteruser="";
        public static String PrivateKeyRefresh="";
        public static String PrivateKeyExpire="";
        public static String DcPersonId="";
        public static int FkfCompanyId=-1;
        public static boolean topactivity=false;
        public static boolean ischeang=true;
        public static Context mContext2;
        public static String IP4;
        public static  int  VersionCode=-1;
        public static int storeFkfCompanyId=0;
        public static int notifiidcompaney=0;

        public static void SaveInt(String key,int value){
            SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2.getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
        public static int LoadInt(String key){
            SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2.getApplicationContext());
            return sharedPreferences.getInt(key, -1);
        }
        public static void SaveString(String key,String value){
            SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2.getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
        public static String LoadString(String key){
            SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext2.getApplicationContext());
            return sharedPreferences.getString(key, "empyty");
        }

    }

    public static class UserVertification
    {
        public static boolean Success =false;
        public static String LogicCode="";
        public static String MobileNew="";


    }

    public static class ChangePassword
    {
        public static String dcIdPerson="";
        public static String UserIDtbScUSers="";
        public static String vcMobileNo="";
        public static String ActiveCode="";
    }

    public static void StartApp(String url)
    {

        Communication.baseUrlService = url;
    }

    public  static  class Month
    {
        public static String GetMonth(int month)
        {
            String result="";
            switch (month)
            {
                case 1:
                    result="فروردین";
                    break;
                case 2:
                    result="اردیبهشت";
                    break;
                case 3:
                    result="خرداد";
                    break;
                case 4:
                    result="تیر";
                    break;
                case 5:
                    result="مرداد";
                    break;
                case 6:
                    result="شهریور";
                    break;
                case 7:
                    result="مهر";
                    break;
                case 8:
                    result="آبان";
                    break;
                case 9:
                    result="آذر";
                    break;
                case 10:
                    result="دی";
                    break;
                case 11:
                    result="بهمن";
                    break;
                case 12:
                    result="اسفند";
                    break;

            }
            return result;
        }
    }

    public static class  Communication
    {


        static public String baseUrlService="http://192.168.1.4:45455/";
      //  static public String baseUrlSignalR="http://91.98.102.101:9889/message";
        static public String baseUrlSignalR="http://91.98.102.101:9889/message";

        static public String VersioningApiVersion="api1/";
        static public String RegisterApiVersion="api1/";
        static public String MessageApiVersion="api1/";
        static public String apiGetUserPicName="api1/"+"GetUserPicName";
        public static String apiRegisterUserInfo="api1/";
        public static String apiGetUserPic="api1/"+"GetUserPic";


        static public boolean isNetworkConnected(Context mContext) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    mContext.getSystemService(Context.CONNECTIVITY_SERVICE); // 1
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
            return networkInfo != null && networkInfo.isConnected(); // 3
        }



        static public String MessageError(Context mContext,int code)
        {
            String Message="";
            switch (code)
            {
                case 0:
                    Message=mContext.getResources().getString(R.string.ErrorServer) ;
                    break;
                case 502:
                    Message=mContext.getResources().getString(R.string.ErrorBadGateway);
                    break;
                case 400:
                    Message=mContext.getResources().getString(R.string.ErrorNotFound);
                    break;
                case 409:
                    Message=mContext.getResources().getString(R.string.ErrorNoSendSMS);
                    break;
                default:
                    Message= "Error Code:"+code;
            }
            return Message;
        }

        public static boolean CheckCommunication(Context mContext)
        {
            if (Communication.isNetworkConnected(mContext)) {
                return true;
            }
            else
            {

                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.NoInternetConnectionTitle))//"No Internet Connection")
                        //.setMessage("It looks like your internet connection is off. Please turn it " +
                        //"on and try again")
                        .setMessage(mContext.getString(R.string.NoInternetConnectionBody))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
            return false;
        }

        public static class  Dialog
        {
            private static ProgressDialog mProgressDialog;

            public static android.app.Dialog onCreateDialog(Context mContext) {
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage(mContext.getString(R.string.wait));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;

            }
            public static void tryDismiss() {

                try {
                    mProgressDialog.dismiss();
                } catch(IllegalArgumentException ex) {

                }
            }


        }

    }
    public static class Database
    {

        public static final String dbName = "PriceInquirySystemDB";
    }

    public static  class Filter
    {
        /*public static int Year=-1;
        public static int Month=-1;*/

        public static boolean IsLeaveHour=true;
        public static boolean IsLeave=true;
        public static boolean IsMissionHour=true;
        public static boolean IsMission=true;
        public static String dFromRequestDate="";
        public static String dEndRequestDate="";
        public static String dFromSaveDate="";
        public static String dEndSaveDate="";
    }

    public static  class FilterDifference
    {
        public static String dstartdate="";
        public static String denddate="";
        public static String dcIdCitys="";
        public static int dcIdMarkazPoroje=-1;
        public static int dcIdMarkazHazineh=-1;
        public static int dcIdMantaghe=-1;
        public static Boolean IsFilter=false;
        public static String Title="";
    }

    public static class DateTime
    {

       /* static public Date ConvertToDate(String dateShamsi, String time)
        {
            if ((dateShamsi == null || dateShamsi.isEmpty() || dateShamsi.equalsIgnoreCase("null")))
                return null;

            if ((time == null || time.isEmpty() || time.equalsIgnoreCase("null")))
            {
                time="11:00";
            }


            ir.priceinquirysystem.Classes.CalendarTool calendarTool = new ir.priceinquirysystem.Classes.CalendarTool();
            String[] separated = dateShamsi.split("/");
            calendarTool.setIranianDate(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]));

            String dateTimeString= calendarTool.getGregorianDate()+"T"+time;

            Date date= CovertStringToDate(dateTimeString);
            return date;


        }
*/
       /* static public String CovertToPersianDate(Date date)
        {
            if(null == date) {
                return "";
            }

            PersianCalendar persianCalendar = new PersianCalendar();
            persianCalendar.setTime(date);

            String dateTime="";

            dateTime+=persianCalendar.get(Calendar.YEAR)+"/";
            if((persianCalendar.get(Calendar.MONTH) + 1)<10)
            {
                dateTime+="0"+(persianCalendar.get(Calendar.MONTH) + 1)+"/";
            }
            else
            {
                dateTime+=(persianCalendar.get(Calendar.MONTH) + 1)+"/";
            }

            if(persianCalendar.get(Calendar.DAY_OF_MONTH)<10)
            {
                dateTime+="0"+persianCalendar.get(Calendar.DAY_OF_MONTH)+" ";
            }
            else
            {
                dateTime+=persianCalendar.get(Calendar.DAY_OF_MONTH)+" ";
            }





            return dateTime;
        }

        static public String CovertToPersianDateWithoutHundred(Date date)
        {
            if(null == date) {
                return "";
            }

            PersianCalendar persianCalendar = new PersianCalendar();
            persianCalendar.setTime(date);

            String dateTime="";

            dateTime+=persianCalendar.get(Calendar.YEAR)+"/";
            dateTime=dateTime.substring(2,dateTime.length());
            if((persianCalendar.get(Calendar.MONTH) + 1)<10)
            {
                dateTime+="0"+(persianCalendar.get(Calendar.MONTH) + 1)+"/";
            }
            else
            {
                dateTime+=(persianCalendar.get(Calendar.MONTH) + 1)+"/";
            }

            if(persianCalendar.get(Calendar.DAY_OF_MONTH)<10)
            {
                dateTime+="0"+persianCalendar.get(Calendar.DAY_OF_MONTH)+" ";
            }
            else
            {
                dateTime+=persianCalendar.get(Calendar.DAY_OF_MONTH)+" ";
            }





            return dateTime;
        }

        static public String CovertToPersianTime(Date date)
        {
            if(null == date) {
                return "";
            }
            PersianCalendar persianCalendar = new PersianCalendar();
            persianCalendar.setTime(date);

            String dateTime="";
            if(persianCalendar.get(Calendar.HOUR_OF_DAY)<10)
            {
                dateTime+="0"+persianCalendar.get(Calendar.HOUR_OF_DAY)+":";
            }
            else
            {
                dateTime+=persianCalendar.get(Calendar.HOUR_OF_DAY)+":";
            }
            if(persianCalendar.get(Calendar.MINUTE)<10)
            {
                dateTime+="0"+persianCalendar.get(Calendar.MINUTE);
            }
            else
            {
                dateTime+=persianCalendar.get(Calendar.MINUTE);
            }





            return dateTime;
        }

        static public Date CovertStringToDate(String dateMiladi)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String dateInString = dateMiladi;

            try {

                Date date = formatter.parse(dateInString);
                return date;

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        static public String CovertDateToString(Date dateMiladi)
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String reportDate = df.format(dateMiladi);

            return  reportDate;
        }*/
    }

    public static class ErrorHandling
    {

        public static enum ErrorType {
             ManagedError(1),
            UnManagedError(2);
            private final int _value;

            ErrorType(int value)
            {
                _value = value;
            }
            public int getValue() {
                return _value;
            }
        }
        public static boolean AddError(Context mContext,ErrorType ErrorType,String FunctionName,String MessageError)
        {
            dbConnector  db = new dbConnector(mContext, Database.dbName , null ,1);
            ContentValues values = new ContentValues();

            values.put("ErrorType",ErrorType.getValue());
            values.put("FormName", mContext.getClass().getSimpleName());
            values.put("FunctionName", FunctionName);
            values.put("MessageError", MessageError);
            Boolean status = db.insert("ErrorHandling", values);
            return status;
        }
        public static void SendError()
        {

        }
        public static String getMethodName()
        {
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

            //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
            // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
            return ste[3].getMethodName(); //Thank you Tom Tresansky
        }

        public static String GetErrorOnFailure(Throwable t)
        {
            String errorType="";
            String errorDesc="";
            if (t instanceof IOException) {
                errorType = "Timeout";
                errorDesc = String.valueOf(t.getCause());
            }
            else if (t instanceof IllegalStateException) {
                errorType = "ConversionError";
                errorDesc = String.valueOf(t.getCause());
            } else {
                errorType = "Other Error";
                errorDesc = String.valueOf(t.getLocalizedMessage());
            }
            return  errorType+"-"+errorDesc;
        }
        public static String ShowError(Exception exceptionError) {
            String errorMessage = "Message: " + exceptionError.getMessage()
                   +"-" + "StackTrace: " + Arrays.toString(exceptionError.getStackTrace())+"-"+exceptionError.toString();
            String title = exceptionError.getClass().getName();
           return errorMessage+"-" +title;
        }


        public static void ErrorManager(final Context mContext, ErrorType ErrorType, String FunctionName, String MessageError) {

            ErrorHandling.AddError(mContext,ErrorType,FunctionName,MessageError);

            AlertDialog.Builder alertDialog;
            alertDialog = new AlertDialog.Builder(mContext);

            alertDialog.setMessage(mContext.getString(R.string.MessageErrorSend));
            alertDialog.setPositiveButton(mContext.getString(R.string.ErrorSend), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ErrorHandling.SendError();
                    dialog.dismiss();
                    exit(mContext);


                }

            });
            alertDialog.setNegativeButton(mContext.getString(R.string.ErrorNoSend), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exit(mContext);

                }

            });

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface dialog) {

                    dialog.dismiss();
                    exit(mContext);
                }
            });
            alertDialog.show();


        }
        static void exit(Context mContext)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }


    }

public static class chek_edt
{
    public static boolean mobil(String mobile)
    {
        String pattern = "^09[0-9]{9}";

        Boolean result = Pattern.matches(pattern,mobile);
        return result;


    }

}


//نمایش دیالوگ
    public static class Dialog_
    {
//ایجاد دیالوگ
        public static View dialog_creat(final Context context, int R_id) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View promptView = layoutInflater.inflate(R_id, null);


            return promptView;
        }
        //نمایش دیالوگ
        public static AlertDialog show_dialog(final Context context, final View _view)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(_view);
            final AlertDialog alert = alertDialogBuilder.create();
alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alert.show();
            ImageView img_close=_view.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dimos_dialog(alert);
                }
            });
            return alert;
        }
        public static void dimos_dialog(AlertDialog alert)
        {

            alert.dismiss();
        }

    }

        public static String ConvertFAToEN(String faNumbers) {
            String[][] mChars = new String[][]{
                    {"0", "۰"},
                    {"1", "۱"},
                    {"2", "۲"},
                    {"3", "۳"},
                    {"4", "۴"},
                    {"5", "۵"},
                    {"6", "۶"},
                    {"7", "۷"},
                    {"8", "۸"},
                    {"9", "۹"}
            };

            for (String[] num : mChars) {

                faNumbers = faNumbers.replace( num[1],num[0]);

            }

            return faNumbers;
        }


}
