package com.denobili.app.components;

import com.denobili.app.helper_utils.Interactor;
import com.denobili.app.modules.ApiInterfaceModule;
import com.denobili.app.modules.NetModule;
import com.denobili.app.modules.RealmModule;
import com.denobili.app.modules.SharedPreferenceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={NetModule.class,
                        ApiInterfaceModule.class,
                            RealmModule.class,
                                SharedPreferenceModule.class
                                }

        ,dependencies = {ApplicationComponent.class})

public interface ApiInterfaceComponent {
    void inject(Interactor interactor);

}
