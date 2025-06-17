# 📱 Setup Appetize.io - Test Immediato

## 🚀 **Passo 1: Crea Repository GitHub**

### **1. Vai su GitHub.com**
- Accedi al tuo account GitHub
- Clicca "New repository"

### **2. Configura Repository**
- **Nome**: `CondominioSmart`
- **Descrizione**: "App Android per gestione condominiale"
- **Pubblica** (non privato)
- **Non inizializzare** con README
- Clicca "Create repository"

### **3. Pusha il Codice**
```bash
git remote add origin https://github.com/TUOUSERNAME/CondominioSmart.git
git push -u origin master
```

## 📱 **Passo 2: Setup Appetize.io**

### **1. Vai su Appetize.io**
- Apri https://appetize.io
- Clicca "Sign Up" o "Get Started"

### **2. Connetti GitHub**
- Clicca "Connect GitHub"
- Autorizza Appetize ad accedere ai tuoi repository
- Seleziona il repository `CondominioSmart`

### **3. Configura Build**
- **Platform**: Android
- **Branch**: master
- **Build Command**: `./gradlew assembleDebug`
- **APK Path**: `app/build/outputs/apk/debug/app-debug.apk`

## 🎮 **Passo 3: Test dell'App**

### **1. Build Automatico**
- Appetize builda automaticamente l'APK
- Aspetta il messaggio "Build completed"

### **2. Emulatore Online**
- Clicca "Launch" per aprire l'emulatore
- L'app si avvia nel browser
- Testa tutte le funzionalità

### **3. Condivisione**
- Clicca "Share" per ottenere un link
- Condividi il link con altri per testare

## 🧪 **Test delle Funzionalità**

### **1. Login**
- Inserisci email e password
- Verifica animazioni zoom in entrata
- Testa transizione alla home

### **2. Navigazione**
- Tocca le card per navigare
- Verifica animazioni zoom out/in
- Testa transizioni fluide

### **3. Tema**
- Menu → Cambia Tema
- Verifica switch chiaro/scuro
- Testa persistenza del tema

### **4. Pagamenti**
- Tocca "Pagamenti"
- Testa filtri di ricerca
- Verifica anteprime file
- Testa creazione nuovo pagamento

### **5. Documenti**
- Tocca "Documenti"
- Verifica upload file
- Testa visualizzazione

## 🎨 **Verifica Animazioni**

### **Zoom In/Out**
- ✅ Entrata schermate con zoom in
- ✅ Uscita schermate con zoom out
- ✅ Transizioni fluide tra schermate

### **Feedback Visivo**
- ✅ Bounce per click
- ✅ Scale per hover
- ✅ Fade in/out per elementi

### **Tema Dinamico**
- ✅ Switch chiaro/scuro
- ✅ Persistenza del tema
- ✅ Colori corretti

## 📱 **Vantaggi di Appetize**

### **Test Immediato**
- ✅ Nessun download richiesto
- ✅ Test nel browser
- ✅ Emulatore realistico

### **Condivisione Facile**
- ✅ Link diretto per testare
- ✅ Nessuna installazione
- ✅ Accesso da qualsiasi dispositivo

### **Debug e Feedback**
- ✅ Log in tempo reale
- ✅ Screenshot automatici
- ✅ Video delle sessioni

## 🚨 **Risoluzione Problemi**

### **Build Fallisce**
1. Verifica repository pubblico
2. Controlla branch master
3. Verifica build command
4. Controlla APK path

### **App Non Si Avvia**
1. Verifica build completato
2. Controlla log errori
3. Verifica configurazione
4. Riprova build

### **Funzionalità Non Funzionano**
1. Verifica connessione internet
2. Controlla permessi app
3. Testa su emulatore diverso
4. Verifica log errori

## 🎯 **Prossimi Passi**

### **Dopo il Test**
1. **Feedback** - Annota problemi trovati
2. **Correzioni** - Risolvi bug identificati
3. **Push** - Aggiorna repository
4. **Rebuild** - Appetize rebuilda automaticamente

### **Distribuzione**
1. **APK Download** - Se necessario
2. **Google Play** - Per distribuzione ufficiale
3. **Firebase** - Per distribuzione beta

---

## 🎉 **Risultato**

Con Appetize avrai:
- ✅ **Test immediato** nel browser
- ✅ **Nessun download** richiesto
- ✅ **Condivisione facile** con link
- ✅ **Debug completo** dell'app
- ✅ **Feedback rapido** per miglioramenti

**Pronto per testare l'app CondominioSmart!** 🚀📱 