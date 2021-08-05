package com.enerjisa.entakepicture.di.binders;



import com.enerjisa.entakepicture.ui.screens.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@SuppressWarnings("unused")
@Module
public abstract class ActivityBinder {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();


}
