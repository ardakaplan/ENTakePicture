package com.enerjisa.entakepicture;

import android.app.Activity;

/**
 * Created by Arda Kaplan at 9.08.2021 - 12:44
 * <p>
 * ardakaplan101@gmail.com
 */
public final class TakePictureManager {

    private String photoFilePath;//fotoğraf dosya yolu
    private int quality = 30;//default value
    private TakePictureListener takePictureListener;//çekim sonucu için listener

    /**
     * @param photoFilePath fotoğraf dosyası için dosya yolu,
     *                      eğer klasör(ler) içerisinde ise bu klasör(lerin) önceden yaratılması gerekir
     */
    public TakePictureManager(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public TakePictureManager setPhotoQuality(int quality) {

        this.quality = quality;

        return this;
    }

    public TakePictureManager addListener(TakePictureListener takePictureListener) {

        this.takePictureListener = takePictureListener;

        return this;
    }

    public Setup setUp() {

        return new Setup();
    }

    public class Setup {

        private Setup() {

        }

        public void takePicture(Activity activity) {

            ENTakePictureActivity.open(activity, photoFilePath, quality, takePictureListener);
        }
    }
}
