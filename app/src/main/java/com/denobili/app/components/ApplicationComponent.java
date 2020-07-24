package com.denobili.app.components;

import android.app.Application;

import com.denobili.app.modules.ApplicationModule;

import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //Exposes Application to any component which depends on this
    Application getApplication();
}
