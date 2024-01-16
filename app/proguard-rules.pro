# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.slf4j.impl.StaticLoggerBinder

-keep class com.google.android.gms.dynamic.IObjectWrapper
-keep class com.google.android.gms.internal.zzuq

-keep class com.google.android.gms.ads.mediation.MediationAdRequest
-keep class com.google.android.gms.ads.mediation.MediationBannerListener
-keep class com.google.android.gms.ads.AdSize
-keep class com.google.android.gms.ads.mediation.MediationInterstitialListener
-keep class com.google.android.gms.ads.mediation.MediationNativeListener
-keep class com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener
-keep class com.google.android.gms.ads.InterstitialAd
-keep class com.google.android.gms.ads.AdListener
-keep class com.google.android.gms.ads.Correlator
-keep class com.google.android.gms.ads.formats.NativeAd
-keep class com.google.android.gms.ads.mediation.NativeMediationAdRequest
-keep class com.google.android.gms.ads.formats.MediaView
-keep class com.google.android.gms.ads.formats.AdChoicesView
-keep class com.google.android.gms.ads.mediation.NativeMediationAdRequest
-keep class com.google.android.gms.ads.VideoOptions
-keep class com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener
-keep class com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener
-keep class com.google.android.gms.ads.doubleclick.AppEventListener
-keep class com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener
-keep class com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener
-keep class com.google.android.gms.ads.mediation.customevent.CustomEventExtras

-keep class com.google.ads.mediation.MediationServerParameters
-keep class com.google.ads.mediation.NetworkExtras
-keep class com.google.ads.mediation.MediationInterstitialListener
-keep class com.google.ads.mediation.customevent.CustomEventServerParameters
-keep class com.google.ads.mediation.MediationBannerListener
-keep class com.google.ads.AdSize
-keep class com.google.ads.mediation.MediationAdRequest
-keep class com.google.ads.mediation.customevent.CustomEventBannerListener
-keep class com.google.ads.mediation.customevent.CustomEventInterstitialListener

-dontnote com.google.protobuf.zzc
-dontnote com.google.protobuf.zzd
-dontnote com.google.protobuf.zze

# Realm
-dontnote io.realm.internal.SyncObjectServerFacade

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *

-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }

-keep class io.realm.internal.KeepMember
-keep @io.realm.internal.KeepMember class * { @io.realm.internal.KeepMember *; }

-dontwarn javax.**
-dontwarn io.realm.**
-keep class io.realm.RealmCollection
-keep class io.realm.OrderedRealmCollection
-keepclasseswithmembernames class io.realm.** {
    * ;
}

-dontnote rx.Observable

-dontnote android.security.KeyStore
-dontwarn okio.Okio
-dontwarn okio.DeflaterSink

-dontnote com.android.org.conscrypt.SSLParametersImpl
-dontnote org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontnote sun.security.ssl.SSLContextImpl

# Don't note a bunch of dynamically referenced classes
-dontnote com.google.**
-dontnote com.facebook.**
-dontnote com.squareup.okhttp.**
-dontnote okhttp3.internal.**

# Recommended flags for Firebase Auth
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit config
-dontnote retrofit2.Platform
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keepattributes Exceptions

-dontwarn com.google.gson.Gson$6

-keep class com.google.android.apps.authenticator.** {*;}

-keepclassmembers class * {
    public protected static final % *;
}

-dontnote ** # Suppress warnings -- we know we're being overbroad

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-dontwarn com.google.android.gms.**

# This allows proguard to strip isLoggable() blocks containing only <=INFO log
# code from release builds.
-assumenosideeffects class android.util.Log {
  static *** i(...);
  static *** d(...);
  static *** v(...);
  static *** isLoggable(...);
}

# Allows proguard to make private and protected methods and fields public as
# part of optimization. This lets proguard inline trivial getter/setter methods.
-allowaccessmodification

# needlessly repeats com.google.android.apps.etc.
-repackageclasses ""

# release > source file
# The source file attribute must be present in order to print stack traces, but
# we rename it in order to avoid leaking the pre-obfuscation class name.
-renamesourcefileattribute PG

# The presence of both of these attributes causes dalvik and other jvms to print
# stack traces on uncaught exceptions, which is necessary to get useful crash
# reports.
-keepattributes SourceFile,LineNumberTable

# Preverification was introduced in Java 6 to enable faster classloading, but
# dex doesn't use the java .class format, so it has no benefit and can cause
# problems.
-dontpreverify

# Skipping analysis of some classes may make proguard strip something that's
# needed.
-dontskipnonpubliclibraryclasses

# Case-insensitive filesystems can't handle when a.class and A.class exist in
# the same directory.
-dontusemixedcaseclassnames

# This prevents the names of native methods from being obfuscated and prevents
# UnsatisfiedLinkErrors.
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# hackbod discourages the use of enums on android, but if you use them, they
# should work. Allow instantiation via reflection by keeping the values method.
-keepclassmembers enum * {
    public static **[] values();
}

# Parcel reflectively accesses this field.
-keepclassmembers class * implements android.os.Parcelable {
  public static *** CREATOR;
}

# These methods are needed to ensure that serialization behaves as expected when
# classes are obfuscated, shrunk, and/or optimized.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Don't warn about Guava. Any Guava-using app will fail the proguard stage without this dontwarn,
# and since Guava is so widely used, we include it here in the base.
-dontwarn com.google.common.**

# Don't warn about Error Prone annotations (e.g. @CompileTimeConstant)
-dontwarn com.google.errorprone.annotations.**

# android.app.Notification.setLatestEventInfo() was removed in MNC, but is still
# referenced (safely) by the NotificationCompat code.
-dontwarn android.app.Notification

# Keep all Javascript API methods
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Silence notes about dynamically referenced classes from AOSP support libraries.
-dontnote android.graphics.Insets

# AOSP support library:  ICU references to gender and plurals messages.
-dontnote libcore.icu.ICU
-keep class libcore.icu.ICU { *** get(...);}

# AOSP support library:  Handle classes that use reflection.
-dontnote android.support.v4.app.NotificationCompatJellybean

# Annotations are implemented as attributes, so we have to explicitly keep them.
# Catch all which encompasses attributes like RuntimeVisibleParameterAnnotations
# and RuntimeVisibleTypeAnnotations
-keepattributes RuntimeVisible*Annotation*

# Keep the annotations that proguard needs to process.
-keep class com.google.android.apps.common.proguard.UsedBy*

# Just because native code accesses members of a class, does not mean that the
# class itself needs to be annotated - only annotate classes that are
# referenced themselves in native code.
-keep @com.google.android.apps.common.proguard.UsedBy* class * {
  <init>();
}
-keepclassmembers class * {
    @com.google.android.apps.common.proguard.UsedBy* *;
}

# For design widgets from Android Support Library
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }

-keep class com.tienbx.diary.data.** {*;}
-keep interface com.tienbx.diary.data.** {*;}

-keep class com.stevdzasan.onetap.** {*;}
-keep interface com.stevdzasan.onetap.** {*;}

