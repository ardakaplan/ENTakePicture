package com.enerjisa.entakepicture;

import com.ardakaplan.rdalogger.RDALogFullData;
import com.enerjisa.enframework.ENApplication;
import com.enerjisa.entakepicture.di.AppComponent;
import com.enerjisa.entakepicture.di.DaggerAppComponent;

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
        return null;
    }

    @Override
    protected boolean doesRDALoggerWork() {
        return false;
    }

    @Override
    public void onLogReceived(RDALogFullData RDALogFullData) {
        // NOTHING TO DO
    }
}
