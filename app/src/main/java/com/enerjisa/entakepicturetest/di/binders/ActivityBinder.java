package com.enerjisa.entakepicturetest.di.binders;



import com.enerjisa.entakepicturetest.ui.screens.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@SuppressWarnings("unused")
@Module
public abstract class ActivityBinder {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();


}
