package com.enerjisa.entakepicturetest;

import android.util.Log;

import com.ardakaplan.rdalogger.RDALogFullData;
import com.enerjisa.enframework.ENApplication;
import com.enerjisa.entakepicturetest.di.AppComponent;
import com.enerjisa.entakepicturetest.di.DaggerAppComponent;

/**
 * Created by Arda Kaplan at 8.07.2021 - 11:41
 * <p>
 * ardakaplan101@gmail.com
 */
public class TestApplication extends ENApplication {


    @Override
    protected void initDagger() {

        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();

        appComponent.inject(this);
    }

    @Override
    protected String getRDALoggerTag() {
        return getString(R.string.app_name);
    }

    @Override
    protected boolean doesRDALoggerWork() {
        return true;
    }

    @Override
    public void onLogReceived(RDALogFullData RDALogFullData) {
        // NOTHING TO DO
    }
}
