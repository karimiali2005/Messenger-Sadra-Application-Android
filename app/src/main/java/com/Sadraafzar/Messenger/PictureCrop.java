package com.Sadraafzar.Messenger;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.Classes.app;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PictureCrop extends AppCompatActivity {
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    String mediaPath;
    ImageView imgCrop;

    //region FontOverride
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_picture_crop);
            mCropImageView = (CropImageView)  findViewById(R.id.CropImageView);

            imgCrop=(ImageView)findViewById(R.id.imgCrop);
            imgCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap cropped =  mCropImageView.getCroppedImage(500, 500);
                    if (cropped != null) {
                        mCropImageView.setImageBitmap(cropped);
                        Intent returnIntent = new Intent();
                        app.File.cropPicture=cropped;
                        // returnIntent.putExtra("pic",cropped);
                        returnIntent.putExtra("mediaPath",mediaPath);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                }
            });

            startActivityForResult(getPickImageChooserIntent(), 200);
        }catch (Exception ex )
        {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view) {
        // startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropImageClick(View view) {

    }

    @Override
    protected void onActivityResult(int  requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = getPickImageResultUri(data);

                // For API >= 23 we need to check specifically that we have permissions to read external storage,
                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
                boolean requirePermissions = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        isUriRequiresPermissions(imageUri)) {

                    // request permissions and handle the result in onRequestPermissionsResult()
                    requirePermissions = true;
                    mCropImageUri = imageUri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

                if (!requirePermissions) {
                    mCropImageView.setImageUriAsync(imageUri);


                    //My Code


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
        }catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager =  getPackageManager();

// collect all camera intents
        Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new  Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}