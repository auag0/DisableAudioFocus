package io.github.auag0.disableaudiofocus

import android.media.AudioAttributes
import android.media.AudioManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android") return

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "com.android.server.audio.MediaFocusControl",
                lpparam.classLoader
            ),
            "requestAudioFocus",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any {
                    val audioAttrs = param.args[0] as? AudioAttributes?
                    if (audioAttrs?.usage == AudioAttributes.USAGE_VOICE_COMMUNICATION) {
                        return XposedBridge.invokeOriginalMethod(
                            param.method,
                            param.thisObject,
                            param.args
                        )
                    }
                    return AudioManager.AUDIOFOCUS_REQUEST_GRANTED
                }
            }
        )
    }
}