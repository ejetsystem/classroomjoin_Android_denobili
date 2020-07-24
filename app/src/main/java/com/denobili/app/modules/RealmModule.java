package com.denobili.app.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RealmModule {

    @Provides
    @Singleton
    Realm realm(){
       return Realm.getDefaultInstance();
    }
}
