package demo.at.ram.presentation

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import timber.log.Timber

open class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setStrictModePolicy()

        setLogger()
    }

    private fun setLogger() {
        if (isDebuggable()) {
            Timber.plant(Timber.DebugTree())
            Timber.i("Timber is initialized.")
        }
    }

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            Timber.w("StrictModePolicy is enabled.")
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }

}