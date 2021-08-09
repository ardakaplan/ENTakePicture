package com.enerjisa.entakepicture.di;


import com.enerjisa.entakepicture.ui.ENTakePictureActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@SuppressWarnings("unused")
@Module
public abstract class ENTakePictureDaggerModule {

    @ContributesAndroidInjector
    abstract ENTakePictureActivity bindENTakePictureActivity();
}
