# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/linyun/Library/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn freemarker.**
-dontwarn com.sun.tools.javac.**
-dontwarn javax.**

-keep class me.skyun.broadcastex.api.Action
-keep class me.skyun.broadcastex.api.ReceiverRegistrar
-keep class me.skyun.broadcastex.api.BroadcastExReceiver
-keep @me.skyun.broadcastex.api.Action class *
-keep class * extends me.skyun.broadcastex.api.ReceiverRegistrar
-keepclassmembers class * {
    @me.skyun.broadcastex.api.BroadcastExReceiver <methods>;
}
