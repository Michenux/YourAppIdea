# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:/dev/android-sdk/tools/proguard/proguard-android.txt
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

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault,*Annotation*


# Dagger
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
  @javax.inject.* *;
  @dagger.* *;
  <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# Guava
-keep class com.google.common.**
-dontwarn com.google.common.collect.MinMaxPriorityQueue

# facebook
-keep class com.facebook.** { *; }
-keepattributes Signature