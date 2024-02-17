package io.github.auag0.disableaudiofocus

import android.media.AudioManager
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    companion object {
        private const val TAG = "DisableAudioFocus"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val className = when (lpparam.packageName) {
            "android" -> "com.android.server.audio.MediaFocusControl"
            else -> "android.media.AudioManager"
        }
        val clazz = XposedHelpers.findClassIfExists(className, lpparam.classLoader)
        clazz?.let { safeClass ->
            XposedBridge.hookAllMethods(
                safeClass,
                "requestAudioFocus",
                XC_MethodReplacement.returnConstant(AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            )
        } ?: Log.e(TAG, "not found class: $className")
    }
}