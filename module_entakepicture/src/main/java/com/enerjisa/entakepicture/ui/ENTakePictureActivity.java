package com.enerjisa.entakepicture.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.ardakaplan.rdalogger.RDALogger;
import com.enerjisa.enframework.helpers.ENBitmapHelpers;
import com.enerjisa.enframework.helpers.ENResourceHelpers;
import com.enerjisa.enframework.ui.screens.ENActivity;
import com.enerjisa.entakepicture.data.TakePictureListener;
import com.enerjisa.entakepicture.data.TakePictureResult;
import com.enerjisa.entakepicture.managers.TakePictureManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public final class ENTakePictureActivity extends ENActivity {


    //*****************************************************//
    //**************INTENT REQUEST CODE********************//
    //*****************************************************//
    private static final int INTENT_REQUEST_CODE_TAKE_PICTURE = 1;

    //*****************************************************//
    //**************INTENT FIELD KEYS**********************//
    //*****************************************************//
    private static final String FILE_PATH = "FILE_PATH";
    private static final String PHOTO_QUALITY = "PHOTO_QUALITY";
    private static final String DESCRIPTION = "DESCRIPTION";


    //*****************************************************//
    //**************STATIC OLARAK SET EDILENLER************//
    //*****************************************************//
    //geri dönüş için listener
    public static TakePictureListener takePictureListener;

    //*****************************************************//
    //**************INTENT TEN GELEN DEĞERLER**************//
    //*****************************************************//
    //resmin kaydedileceği dosya
    private File photoFile;

    //fotoğraf kalitesi
    private int photoQuality;

    //aynen geri gönderilecek açıklama alanı
    private String description;

    //*****************************************************//
    //**************KONUM ICIN GEREKLI NESNELER************//
    //*****************************************************//

    //resme ait koordinatlar
    private double photoLatitude = 0L;
    private double photoLongitude = 0L;

    //konum için locationManager
    private LocationManager locationManager;

    //konum etiketi için gerekli obje
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(@NonNull Location location) {

            photoLatitude = location.getLatitude();
            photoLongitude = location.getLongitude();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLocationManager();

        photoFile = new File(getIntent().getExtras().getString(FILE_PATH));

        photoQuality = getIntent().getExtras().getInt(PHOTO_QUALITY, TakePictureManager.DEFAULT_PHOTO_QUALITY);

        description = getIntent().getExtras().getString(DESCRIPTION, null);

        checkNeedsAndGoOn();
    }

    private void checkNeedsAndGoOn() {

        //checking camera
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {

            //checking permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                takePhoto();

            } else {

                takePictureListener.onResult(TakePictureResult.NO_PERMISSION, description, photoFile.getPath(), null);
            }

        } else {

            takePictureListener.onResult(TakePictureResult.NO_CAMERA, description, photoFile.getPath(), null);
        }
    }

    private void takePhoto() {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri uri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, INTENT_REQUEST_CODE_TAKE_PICTURE);
    }


    private String saveImage(Bitmap bitmap) throws IOException {

        if (bitmap == null) {

            return null;
        }

        if (photoQuality == TakePictureManager.DEFAULT_PHOTO_QUALITY) {

            if (bitmap.getWidth() > 1200) {

                bitmap = Bitmap.createScaledBitmap(bitmap, 1200, (1200 * bitmap.getHeight()) / bitmap.getWidth(), false);
            }

            try {

                bitmap = ENBitmapHelpers.rotateImageIfRequired(bitmap, Uri.fromFile(photoFile));

            } catch (IOException e) {

                e.printStackTrace();
            }

        } else {

            bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() / 100) * photoQuality, (bitmap.getHeight() / 100) * photoQuality, false);
        }


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        RDALogger.info("TAKEN PHOTO\nQuality : " + photoQuality + "\nWidth : " + bitmap.getWidth() + "\nHeight : " + bitmap.getHeight());

        bitmap.compress(Bitmap.CompressFormat.JPEG, photoQuality, byteArrayOutputStream);

        byte[] bitmapdata = byteArrayOutputStream.toByteArray();

        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(photoFile);

            fileOutputStream.write(bitmapdata);

        } catch (IOException e) {

            throw e;

        } finally {

            if (fileOutputStream != null) {

                try {

                    fileOutputStream.flush();

                    fileOutputStream.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        //konum izinleri verilmediyse fotoğrafa enlem-boylam bilgileri eklenmez
        if (checkLocationPermission()) {

            setGeoTag(photoFile.getAbsolutePath());
        }

        if (photoFile.exists()) {

            return photoFile.getAbsolutePath();

        } else {

            return "";
        }
    }

    private void setGeoTag(String path) {

        try {

            ExifInterface exifInterface = new ExifInterface(path);

            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, dec2DMS(photoLatitude));

            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, dec2DMS(photoLongitude));

            if (photoLatitude > 0) {

                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");

            } else {

                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (photoLongitude > 0) {

                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");

            } else {

                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exifInterface.saveAttributes();

        } catch (IOException ignored) {

        }
    }

    private String dec2DMS(double coord) {

        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543

        String sOut = (int) coord + "/1,";   // 105/1,

        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258

        sOut = sOut + (int) coord + "/1,";   // 105/1,59/1,

        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555

        sOut = sOut + (int) coord + "/1000";   // 105/1,59/1,15555/1000

        return sOut;
    }

    /**
     * locationManager ı başlatır, fotoğrafa konum bilgisini eklemek için gerekli.
     * <p>
     * Eğer uygulama tarafından konum izni verilmediyse bu bilgiler eklenmez
     */
    @SuppressLint("MissingPermission")
    private void requestLocationManager() {

        //konum izni verilmediyse locationManager başlatılmaz
        if (checkLocationPermission()) {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager != null) {

                final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

                final long MIN_TIME_BW_UPDATES = 1000 * 60;

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {

                    photoLatitude = location.getLatitude();
                    photoLongitude = location.getLongitude();
                }
            }
        }
    }

    /**
     * @return konum izinleri verilmedi kontrol eder
     */
    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {

            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();

            bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(photoFile).getPath(), bitmapFactoryOptions);

            try {

                takePictureListener.onResult(TakePictureResult.SUCCESS, description, saveImage(bitmap), null);

            } catch (IOException e) {

                e.printStackTrace();

                takePictureListener.onResult(TakePictureResult.ERROR, description, photoFile.getPath(), e);
            }

        } else if (requestCode == INTENT_REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_CANCELED) {

            takePictureListener.onResult(TakePictureResult.USER_CANCELED, description, photoFile.getPath(), null);

        } else {

            takePictureListener.onResult(TakePictureResult.ERROR, description, photoFile.getPath(), new RuntimeException("Something went worng"));
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (checkLocationPermission()) {

            locationManager.removeUpdates(locationListener);

            locationManager = null;
        }
    }

    @Override
    public int getLayoutId() {
        return ENResourceHelpers.NO_LAYOUT;
    }

    public static void open(Activity activity, String filePath, @Nullable Integer photoQuality, @Nullable String description, TakePictureListener takePictureListener) {

        Intent intent = new Intent(activity, ENTakePictureActivity.class);

        //interface static olarak set edilir
        ENTakePictureActivity.takePictureListener = takePictureListener;

        intent.putExtra(FILE_PATH, filePath);
        intent.putExtra(PHOTO_QUALITY, photoQuality);
        intent.putExtra(DESCRIPTION, description);

        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_TAKE_PICTURE);
    }
}
