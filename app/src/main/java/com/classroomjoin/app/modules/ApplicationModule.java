package com.classroomjoin.app.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application sApplication;

    public ApplicationModule(Application application) {
        sApplication = application;
    }

    @Provides
    Application providesApplication(){
        return sApplication;
    }

}
