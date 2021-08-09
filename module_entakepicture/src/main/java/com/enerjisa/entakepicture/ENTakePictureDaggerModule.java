package com.enerjisa.entakepicture;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@SuppressWarnings("unused")
@Module
public abstract class ENTakePictureDaggerModule {

    @ContributesAndroidInjector
    abstract ENTakePictureActivity bindENTakePictureActivity();
}
