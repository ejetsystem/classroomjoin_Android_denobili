package com.classroomjoin.app.components;

import android.app.Application;

import com.classroomjoin.app.modules.ApplicationModule;

import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //Exposes Application to any component which depends on this
    Application getApplication();
}
