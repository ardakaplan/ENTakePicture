package com.enerjisa.entakepicture.data;

/**
 * Created by Arda Kaplan at 9.08.2021 - 12:58
 * <p>
 * ardakaplan101@gmail.com
 */
public interface TakePictureListener {

    /**
     * @param takePictureResult fotoğraf çekme sonucu
     * @param description       fotoğraf çekilme sırasında bazen hangi fotoğrafın çekildiğini bilmemiz gerekiyor,
     *                          bu yüzden böyle bir flag yazıldı, set edilen değer buradan aynı şekilde geri dönüyor
     * @param photoFilePath     fotoğraf dosyası yolu
     * @param exception         sadece {@link TakePictureResult#ERROR} durumunda dolu gelecek hata
     */
    void onResult(TakePictureResult takePictureResult, String description, String photoFilePath, Exception exception);
}
