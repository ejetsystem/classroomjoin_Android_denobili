package com.denobili.app.helper_utils


import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.denobili.app.components.ApiInterfaceComponent
import com.denobili.app.components.DaggerApiInterfaceComponent
import com.denobili.app.components.DaggerApplicationComponent
import com.denobili.app.helper.ForceUpdateChecker
import com.denobili.app.modules.ApplicationModule
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.Shaky
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class Myapp : Application(), AnkoLogger {

    var apiInterface: ApiInterfaceComponent? = null

    private val USERTYPE_KEY = "com.classroom.setting.usertype"
    var localizationDelegate = LocalizationApplicationDelegate(this)


    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        initRealmConfiguration()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        val applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        apiInterface = DaggerApiInterfaceComponent.builder().applicationComponent(applicationComponent).build()
        Shaky.with(this, EmailShakeDelegate("support@classroomjoin.com"))
        //MobileAds.initialize(this, "ca-app-pub-5723471129009289~9340573445")
        warn { "this is the app resume" }

        /*val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build()
        Fabric.with(fabric)*/


        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSIONCODE, 42)
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false)
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=denobili.classroomjoin.app")
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        // fetch every minutes
        firebaseRemoteConfig.fetch(60)
                .addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful()) {
                        print("Data---> Myapp remote config  ###is fetched.")
                        firebaseRemoteConfig.activateFetched()
                    }
                }

        print("Data---> Myapp  Done")
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base));
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    private fun initRealmConfiguration() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .name("ClassroomJoin.realm")
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}
