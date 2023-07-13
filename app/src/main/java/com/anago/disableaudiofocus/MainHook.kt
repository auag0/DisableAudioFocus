package com.anago.disableaudiofocus

import android.media.AudioManager
import android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.hookAllMethods(
            AudioManager::class.java,
            "requestAudioFocus",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Int {
                    XposedBridge.log("hooked requestAudioFocus")
                    return AUDIOFOCUS_REQUEST_GRANTED
                }
            })

        /* 意味なし
                XposedBridge.hookAllMethods(
                    AudioManager.OnAudioFocusChangeListener::class.java,
                    "onAudioFocusChange",
                    object : XC_MethodReplacement() {
                        override fun replaceHookedMethod(param: MethodHookParam): Any? {
                            param.args[0] = AUDIOFOCUS_GAIN
                            XposedBridge.log("hooked onAudioFocusChange")
                            return null
                        }
                    })*/
    }
}