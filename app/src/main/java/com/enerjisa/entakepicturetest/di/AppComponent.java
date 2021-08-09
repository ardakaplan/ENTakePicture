package com.enerjisa.entakepicturetest.di;

import android.app.Application;

import com.enerjisa.enframework.di.ENBaseModule;
import com.enerjisa.entakepicture.ENTakePictureDaggerModule;
import com.enerjisa.entakepicturetest.TestApplication;
import com.enerjisa.entakepicturetest.di.binders.ActivityBinder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class,
        AppModule.class,
        ENBaseModule.class,
        ActivityBinder.class,
        ENTakePictureDaggerModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(TestApplication testApplication);
}

