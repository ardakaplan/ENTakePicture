package com.enerjisa.entakepicturetest.ui.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.ardakaplan.rdalogger.RDALogger;
import com.enerjisa.enframework.helpers.ENIntentHelpers;
import com.enerjisa.entakepicture.data.TakePictureListener;
import com.enerjisa.entakepicture.data.TakePictureResult;
import com.enerjisa.entakepicture.managers.TakePictureManager;
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

    @OnClick(R.id.mainActicity_button_takePhoto)
    void clickedTakePhoto() {

        if (checkPermissions()) {

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "test_en_take_picture_folder");

            if (!mediaStorageDir.exists()) {

                if (!mediaStorageDir.mkdirs()) {

                    RDALogger.error("failed to create directory test_en_take_picture_folder");
                }
            }

            String photoFilePath = mediaStorageDir.getPath() + File.separator + "TEST_PHOTO_" + System.currentTimeMillis() + ".jpg";

            new TakePictureManager(photoFilePath).setPhotoQuality(50).addListener(new TakePictureListener() {

                @Override
                public void onResult(TakePictureResult takePictureResult, String description, String photoFilePath, Exception exception) {

                    RDALogger.info("CAMERA RESULT " + takePictureResult);
                }
            }).setUp().takePicture(this);
        }
    }

    /**
     * @return izinleri kontrol eder, izin sayfasını açar
     */
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

    @Override
    public int getLayoutId() {
        return R.layout.main_activity;
    }
}
