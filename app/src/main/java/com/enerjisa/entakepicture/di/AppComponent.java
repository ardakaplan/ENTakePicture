package com.enerjisa.entakepicture.di;

import android.app.Application;

import com.enerjisa.enframework.di.ENBaseModule;
import com.enerjisa.entakepicture.TestApplication;
import com.enerjisa.entakepicture.di.binders.ActivityBinder;

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

