package com.classroomjoin.app.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {

    @Provides
    @Singleton
    SharedPreferences sharedPreferences(Application application){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
