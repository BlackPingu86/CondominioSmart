# 📱 Guida Rapida: APK Online

## 🚀 **Opzione 1: GitHub Actions (Raccomandato)**

### **Passo 1: Crea Repository GitHub**
1. Vai su https://github.com
2. Clicca "New repository"
3. Nome: `CondominioSmart`
4. Pubblica (non privato)
5. Clicca "Create repository"

### **Passo 2: Pusha il Codice**
```bash
git remote add origin https://github.com/TUOUSERNAME/CondominioSmart.git
git push -u origin master
```

### **Passo 3: Scarica APK**
1. Vai su GitHub → Actions
2. Clicca sul workflow completato
3. Scarica "CondominioSmart-APK"

## 🌐 **Opzione 2: Gitpod (Più Semplice)**

### **Passo 1: Apri Gitpod**
1. Vai su https://gitpod.io
2. Inserisci: `https://github.com/TUOUSERNAME/CondominioSmart`
3. Clicca "Continue"

### **Passo 2: Build Automatico**
- Gitpod builda automaticamente
- Aspetta il messaggio "✅ APK built successfully!"

### **Passo 3: Download**
- Clicca destro su `app-debug.apk`
- Seleziona "Download"

## 📱 **Opzione 3: Replit (Alternativa)**

### **Passo 1: Crea Repl**
1. Vai su https://replit.com
2. Clicca "Create Repl"
3. Scegli "Android"
4. Importa il codice

### **Passo 2: Build**
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### **Passo 3: Download**
- APK in `app/build/outputs/apk/debug/`

## 🔗 **Opzione 4: Firebase App Distribution**

### **Passo 1: Setup Firebase**
1. Crea progetto Firebase
2. Configura App Distribution
3. Upload APK

### **Passo 2: Inviti**
- Invita via email
- Link diretto per download

## 📋 **Checklist Pre-Build**

- ✅ AndroidManifest.xml configurato
- ✅ build.gradle con dipendenze
- ✅ google-services.json presente
- ✅ Tutti i layout completati
- ✅ Animazioni implementate

## 🚨 **Risoluzione Problemi**

### **Build Fallisce**
- Verifica dipendenze in build.gradle
- Controlla versioni SDK
- Verifica permessi AndroidManifest

### **APK Non Si Installa**
- Abilita "Fonti sconosciute"
- Verifica versione Android (5.0+)
- Controlla spazio storage

## 📱 **Test dell'APK**

### **Funzionalità da Testare**
- ✅ Login con animazioni zoom
- ✅ Navigazione tra schermate
- ✅ Cambio tema scuro/chiaro
- ✅ Filtri pagamenti
- ✅ Upload documenti

### **Animazioni da Verificare**
- ✅ Zoom in entrata
- ✅ Zoom out uscita
- ✅ Bounce click
- ✅ Transizioni fluide

---

## 🎯 **Raccomandazione**

**GitHub Actions** è la soluzione migliore perché:
- ✅ Build automatico ad ogni push
- ✅ APK sempre aggiornato
- ✅ Download facile
- ✅ Gratuito per repository pubblici

**Vuoi che procediamo con GitHub Actions?** 