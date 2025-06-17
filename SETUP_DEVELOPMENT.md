# 🛠️ Setup Ambiente di Sviluppo

## 📋 **Prerequisiti**

### **1. Java Development Kit (JDK)**
```bash
# Scarica JDK 11 o superiore da:
# https://adoptium.net/ o https://www.oracle.com/java/technologies/
```

### **2. Android Studio**
```bash
# Scarica Android Studio da:
# https://developer.android.com/studio
```

### **3. Android SDK**
```bash
# Installato automaticamente con Android Studio
# API Level 21+ (Android 5.0+)
```

## 🔧 **Configurazione Java**

### **Windows**
```bash
# 1. Installa JDK
# 2. Imposta JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-11.0.x"
setx PATH "%PATH%;%JAVA_HOME%\bin"

# 3. Verifica installazione
java -version
javac -version
```

### **macOS/Linux**
```bash
# 1. Installa JDK
sudo apt install openjdk-11-jdk  # Ubuntu/Debian
brew install openjdk@11          # macOS

# 2. Imposta JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$PATH:$JAVA_HOME/bin

# 3. Verifica installazione
java -version
javac -version
```

## 📱 **Configurazione Android**

### **1. Android Studio Setup**
```bash
# 1. Installa Android Studio
# 2. Configura Android SDK
# 3. Installa emulatore Android
# 4. Configura device fisico (opzionale)
```

### **2. Variabili d'Ambiente**
```bash
# Windows
setx ANDROID_HOME "C:\Users\%USERNAME%\AppData\Local\Android\Sdk"
setx PATH "%PATH%;%ANDROID_HOME%\platform-tools"

# macOS/Linux
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

## 🚀 **Build e Test**

### **1. Verifica Setup**
```bash
# Verifica Java
java -version

# Verifica Android SDK
adb version

# Verifica Gradle
./gradlew --version
```

### **2. Build App**
```bash
# Clean build
./gradlew clean

# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease
```

### **3. Installazione**
```bash
# Installa su device/emulatore
adb install app/build/outputs/apk/debug/app-debug.apk

# Avvia app
adb shell am start -n com.condominiosmart.app/.MainActivity
```

## 🔍 **Test Sicurezza**

### **1. Test Crittografia**
```bash
# Verifica che l'app si avvii
# Testa registrazione con dati sensibili
# Verifica che i dati siano crittografati
```

### **2. Test Biometrica**
```bash
# Configura fingerprint/face nell'emulatore
# Testa autenticazione biometrica
# Verifica fallback a password
```

### **3. Test Network**
```bash
# Verifica che tutto il traffico sia HTTPS
# Testa certificate pinning
# Verifica network security config
```

## 🛠️ **Strumenti Utili**

### **Android Studio**
- **Layout Inspector** per debug UI
- **Profiler** per performance
- **Logcat** per log e debug
- **Device Manager** per emulatori

### **ADB (Android Debug Bridge)**
```bash
# Lista device connessi
adb devices

# Screenshot
adb shell screencap /sdcard/screenshot.png
adb pull /sdcard/screenshot.png

# Log dell'app
adb logcat | grep "CondominioSmart"

# Shell del device
adb shell
```

### **Gradle**
```bash
# Clean project
./gradlew clean

# Build specifico flavor
./gradlew assembleDebug
./gradlew assembleRelease

# Test
./gradlew test

# Lint check
./gradlew lint
```

## 🚨 **Risoluzione Problemi**

### **Problema: JAVA_HOME non trovato**
```bash
# Verifica installazione Java
java -version

# Imposta JAVA_HOME correttamente
echo $JAVA_HOME  # Linux/macOS
echo %JAVA_HOME% # Windows
```

### **Problema: Android SDK non trovato**
```bash
# Verifica ANDROID_HOME
echo $ANDROID_HOME  # Linux/macOS
echo %ANDROID_HOME% # Windows

# Verifica adb
adb version
```

### **Problema: Build fallisce**
```bash
# Clean e rebuild
./gradlew clean
./gradlew assembleDebug

# Verifica dipendenze
./gradlew dependencies
```

### **Problema: App non si avvia**
```bash
# Verifica log
adb logcat | grep "CondominioSmart"

# Verifica permessi
adb shell pm list permissions | grep "condominiosmart"
```

## 📊 **Checklist Setup**

### **Java** ✅
- [ ] JDK 11+ installato
- [ ] JAVA_HOME configurato
- [ ] java -version funziona
- [ ] javac -version funziona

### **Android** ✅
- [ ] Android Studio installato
- [ ] Android SDK configurato
- [ ] ANDROID_HOME configurato
- [ ] adb version funziona

### **Gradle** ✅
- [ ] ./gradlew --version funziona
- [ ] ./gradlew clean funziona
- [ ] ./gradlew assembleDebug funziona
- [ ] APK generato correttamente

### **Device/Emulatore** ✅
- [ ] Emulatore configurato
- [ ] Device fisico connesso (opzionale)
- [ ] adb devices mostra device
- [ ] App installabile

---

## 🎯 **Prossimo Passo**

Una volta completato il setup:
1. **Build dell'app** con sicurezza
2. **Test funzionali** completi
3. **Test di sicurezza** specifici
4. **Preparazione per distribuzione** 