package com.denobili.app.modules;

import com.denobili.app.helper_utils.ApiInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class ApiInterfaceModule {
    @Provides
    @Singleton
    public ApiInterface providesGitHubInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
}
