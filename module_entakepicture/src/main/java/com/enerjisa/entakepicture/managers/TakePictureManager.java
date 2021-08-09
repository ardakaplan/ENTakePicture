package com.enerjisa.entakepicture.managers;

import android.app.Activity;

import com.ardakaplan.rdalogger.RDALoggerNonListened;
import com.enerjisa.entakepicture.data.TakePictureListener;
import com.enerjisa.entakepicture.ui.ENTakePictureActivity;

/**
 * Created by Arda Kaplan at 9.08.2021 - 12:44
 * <p>
 * ardakaplan101@gmail.com
 */
@SuppressWarnings("unused")
public final class TakePictureManager {

    //varsayılan fotoğraf kalitesi
    public static final int DEFAULT_PHOTO_QUALITY = 30;

    //fotoğraf dosya yolu
    private final String photoFilePath;

    //default value
    private int quality = DEFAULT_PHOTO_QUALITY;

    //crop ekranı açılacak mı
    private boolean openCropScreen = false;

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

    /**
     * @param description adding description for recognising result value
     * @return {@link TakePictureManager}
     */
    public TakePictureManager addDescription(String description) {

        this.description = description;

        return this;
    }

    /**
     * TODO henüz yapılmadı
     *
     * @param openCropScreen crop ekranı açılacak mı, varsayılan değer false
     * @return {@link TakePictureManager}
     */
    public TakePictureManager penCropScreen(boolean openCropScreen) {

        this.openCropScreen = openCropScreen;

        return this;
    }

    /**
     * @param takePictureListener fotoğraf sonucu öğrenmek istiyorsan bu listener ı set etmen gerekiyor
     * @return {@link TakePictureManager}
     */
    public TakePictureManager addListener(TakePictureListener takePictureListener) {

        this.takePictureListener = takePictureListener;

        return this;
    }

    /**
     * ayarlamalar bittikten sonra bu metodun çağrılması gerekiyor.
     * <p>
     * {@link Setup#takePicture(Activity)} metodu bu obje üzerinden çağrılır
     *
     * @return {@link Setup}
     */
    public Setup setUp() {

        return new Setup();
    }

    public class Setup {

        /**
         * dışarıya kapalı constructor
         */
        private Setup() {

        }

        /**
         * @param activity nihai olarak resim çekmeyi başlatacak olan metot
         *                 daha önce ayarlanan değerlere göre resim çekme ekranını {@link ENTakePictureActivity} başlatır
         */
        public void takePicture(Activity activity) {

            RDALoggerNonListened.info("\nCamera starts with ->" +
                    "\nphotoFilePath  : " + photoFilePath +
                    "\nquality        : " + quality +
                    "\ndescription    : " + description);

            ENTakePictureActivity.open(activity, photoFilePath, quality, description, takePictureListener);
        }
    }
}
