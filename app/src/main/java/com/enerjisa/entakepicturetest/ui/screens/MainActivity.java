package com.enerjisa.entakepicturetest.ui.screens;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Parcel;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ardakaplan.rdalogger.RDALogger;
import com.enerjisa.enframework.helpers.ENIntentHelpers;
import com.enerjisa.entakepicture.ENTakePictureActivity;
import com.enerjisa.entakepicture.TakePictureListener;
import com.enerjisa.entakepicture.TakePictureManager;
import com.enerjisa.entakepicture.TakePictureResult;
import com.enerjisa.entakepicturetest.R;
import com.enerjisa.entakepicturetest.ui.base.BaseActivity;

import java.io.File;

import butterknife.OnClick;

/**
 * Created by Arda Kaplan at 5.08.2021 - 15:02
 * <p>
 * ardakaplan101@gmail.com
 */
public class MainActivity extends BaseActivity {

    //REQUEST CODES
    private final int REQUEST_CODE_OPEN_TAKE_PHOTO_ACTIVITY = 1596;

    @Override
    public int getLayoutId() {
        return R.layout.main_activity;
    }

    @OnClick(R.id.mainActicity_button_takePhoto)
    void clickedTakePhoto() {

        if (checkPermissions()) {

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "test_en_take_picture_folder");

            if (!mediaStorageDir.exists()) {

                if (!mediaStorageDir.mkdirs()) {

                    RDALogger.error("failed to create directory Enerjisa");
                }
            }

            String photoFilePath = mediaStorageDir.getPath() + File.separator + "TEST_PHOTO_" + System.currentTimeMillis() + ".jpg";

            new TakePictureManager(photoFilePath).setPhotoQuality(50).addListener(new TakePictureListener() {

                @Override
                public void onResult(TakePictureResult takePictureResult) {

                    RDALogger.info("CAMERA RESULT " + takePictureResult);

                }
            }).setUp().takePicture(this);

//            ENTakePictureActivity.open(this, photoFilePath, 50, REQUEST_CODE_OPEN_TAKE_PHOTO_ACTIVITY);
        }
    }

    private boolean checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ENIntentHelpers.openApplicationSettingsPage(this);

            return false;

        } else {

            return true;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_TAKE_PHOTO_ACTIVITY && resultCode == Activity.RESULT_OK) {

            TakePictureResult takePictureResult = (TakePictureResult) data.getExtras().getSerializable(ENTakePictureActivity.RESULT);

            RDALogger.info("CAMERA RESULT " + takePictureResult);

            //dönen cevaplar bu şekilde elde edilebilir
            switch (takePictureResult) {

                case SUCCESS:

                    break;

                case ERROR:

                    break;

                case NO_CAMERA:

                    break;

                case USER_CANCELED:

                    break;
            }
        }
    }
}
