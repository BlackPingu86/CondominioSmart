# Regole ProGuard per CondominioSmart
# Sicurezza e ottimizzazione

# Mantieni le classi di sicurezza
-keep class com.condominiosmart.app.security.** { *; }
-keep class androidx.security.** { *; }
-keep class com.google.crypto.tink.** { *; }

# Mantieni le classi di Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Mantieni le classi di biometrica
-keep class androidx.biometric.** { *; }

# Mantieni le classi di JWT
-keep class com.auth0.android.jwt.** { *; }

# Mantieni le classi di networking sicuro
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Mantieni le classi di crittografia
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }

# Mantieni le classi di Android Keystore
-keep class android.security.** { *; }
-keep class android.security.keystore.** { *; }

# Mantieni le classi di Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Mantieni le classi di Lifecycle
-keep class androidx.lifecycle.** { *; }

# Mantieni le classi di Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Regole generali di sicurezza
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes Exceptions

# Rimuovi log in produzione
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Ottimizzazioni per sicurezza
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Mantieni le classi principali dell'app
-keep class com.condominiosmart.app.MainActivity { *; }
-keep class com.condominiosmart.app.HomeActivity { *; }
-keep class com.condominiosmart.app.**Activity { *; }

# Mantieni i modelli dati
-keep class com.condominiosmart.app.models.** { *; }

# Mantieni gli adapter
-keep class com.condominiosmart.app.adapters.** { *; }

# Mantieni i manager Firebase
-keep class com.condominiosmart.app.firebase.** { *; }

# Regole per serializzazione sicura
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Mantieni le interfacce di callback
-keep interface com.condominiosmart.app.** { *; }

# Regole per reflection sicura
-keepattributes InnerClasses
-keep class **.R$* {
    public static <fields>;
}

# Mantieni le risorse
-keep class **.R$* {
    public static <fields>;
}

# Regole per debugging sicuro
-keepattributes LocalVariableTable
-keepattributes LocalVariableTypeTable

# Mantieni le classi di test
-dontnote junit.framework.**
-dontnote junit.runner.**

# Regole per sicurezza aggiuntiva
-keep class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Mantieni le classi di networking sicuro
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Regole per Firebase Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Mantieni le classi di Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Regole per sicurezza finale
-keep class * {
    @androidx.annotation.Keep *;
} 