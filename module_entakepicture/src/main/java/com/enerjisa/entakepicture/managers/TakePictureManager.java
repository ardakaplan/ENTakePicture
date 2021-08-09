package com.enerjisa.entakepicture.managers;

import android.app.Activity;

import com.enerjisa.entakepicture.data.TakePictureListener;
import com.enerjisa.entakepicture.ui.ENTakePictureActivity;

/**
 * Created by Arda Kaplan at 9.08.2021 - 12:44
 * <p>
 * ardakaplan101@gmail.com
 */
public final class TakePictureManager {

    //varsayılan fotoğraf kalitesi
    public static final int DEFAULT_PHOTO_QUALITY = 30;

    //fotoğraf dosya yolu
    private final String photoFilePath;

    //default value
    private int quality = DEFAULT_PHOTO_QUALITY;

    //çekim sonucu için listener
    private TakePictureListener takePictureListener;

    //bazı durumlarda hangi resmi çektiğimiz bilgisinide almamız gerekebiliyor, bu yüzden ekstra bir alan eklendi,
    // eğer bu alan set edildiyse resim sonucundan aynen geri gönderilir
    private String description;

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

    public TakePictureManager addDescription(String description) {

        this.description = description;

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

            ENTakePictureActivity.open(activity, photoFilePath, quality, description, takePictureListener);
        }
    }
}
