package com.classroomjoin.app.components;

import com.classroomjoin.app.helper_utils.Interactor;
import com.classroomjoin.app.modules.ApiInterfaceModule;
import com.classroomjoin.app.modules.NetModule;
import com.classroomjoin.app.modules.RealmModule;
import com.classroomjoin.app.modules.SharedPreferenceModule;

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
