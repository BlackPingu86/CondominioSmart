# 🏢 CondominioSmart

App Android per la gestione condominiale con funzionalità moderne e design elegante.

## ✨ Funzionalità

- 🔐 **Login e Registrazione** - Autenticazione sicura
- 💰 **Gestione Pagamenti** - Fatture, bollette, ricevute con filtri avanzati
- 📄 **Gestione Documenti** - Upload e visualizzazione documenti
- 📢 **Gestione Avvisi** - Comunicazioni condominiali
- 🎨 **Tema Scuro/Chiaro** - Cambio tema dinamico
- ✨ **Animazioni Zoom** - Transizioni fluide tra schermate
- 📱 **Design Moderno** - UI/UX ottimizzata per mobile

## 🚀 Test dell'App Senza Android Studio

### **Opzione 1: GitHub Actions (Raccomandato)**

1. **Crea repository GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/tuousername/CondominioSmart.git
   git push -u origin main
   ```

2. **Build automatico**
   - Il workflow si attiva automaticamente
   - Vai su GitHub → Actions
   - Scarica l'APK da "Artifacts"

3. **Installa sul telefono**
   - Abilita "Fonti sconosciute"
   - Installa l'APK scaricato

### **Opzione 2: Gitpod (Più Semplice)**

1. **Apri Gitpod**
   - Vai su https://gitpod.io
   - Inserisci: `https://github.com/tuousername/CondominioSmart`
   - Clicca "Continue"

2. **Build nel browser**
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```

3. **Download APK**
   - Clicca destro su `app/build/outputs/apk/debug/app-debug.apk`
   - Seleziona "Download"

## 📱 Test delle Funzionalità

### **1. Login**
- Inserisci email e password
- Verifica animazioni zoom in entrata

### **2. Navigazione**
- Tocca le card per navigare
- Testa animazioni zoom out/in
- Verifica transizioni fluide

### **3. Tema**
- Menu → Cambia Tema
- Verifica switch chiaro/scuro

### **4. Pagamenti**
- Testa filtri di ricerca
- Verifica anteprime file
- Testa creazione nuovo pagamento

### **5. Documenti**
- Verifica upload file
- Testa visualizzazione

## 🎨 Caratteristiche UI/UX

### **Animazioni**
- **Zoom In/Out** per transizioni
- **Bounce** per feedback click
- **Fade In/Out** per elementi
- **Scale** per interazioni

### **Design System**
- **Colori**: Arancione primario (#F97C2F)
- **Tipografia**: Gerarchia chiara
- **Spacing**: 8dp, 16dp, 24dp
- **Corner Radius**: 8dp per card

### **Temi**
- **Chiaro**: Sfondo bianco, testo scuro
- **Scuro**: Sfondo scuro (#121212), testo bianco

## 🔧 Tecnologie

- **Kotlin** - Linguaggio principale
- **Android Jetpack** - Componenti moderni
- **Material Design** - Design system
- **Firebase** - Backend e autenticazione
- **Glide** - Caricamento immagini

## 📋 Requisiti

- **Android**: API 21+ (Android 5.0+)
- **RAM**: 2GB minimo
- **Storage**: 50MB liberi

## 🚨 Risoluzione Problemi

### **APK non si installa**
- Verifica "Fonti sconosciute" abilitato
- Controlla versione Android (5.0+)
- Verifica spazio storage

### **App non si avvia**
- Verifica permessi concessi
- Controlla connessione internet
- Riavvia l'app

### **Animazioni lente**
- Verifica performance device
- Chiudi app in background
- Riavvia il telefono

## 📞 Supporto

Per problemi o suggerimenti:
- Apri una Issue su GitHub
- Controlla la documentazione
- Verifica i log di errore

## 🎯 Roadmap

- [ ] **Chat condominiale**
- [ ] **Calendario eventi**
- [ ] **Notifiche push**
- [ ] **Offline mode**
- [ ] **Backup automatico**

---

## 🎉 Risultato

L'app CondominioSmart offre:
- ✅ **Esperienza moderna** con animazioni fluide
- ✅ **Funzionalità complete** per gestione condominiale
- ✅ **Design professionale** e accessibile
- ✅ **Performance ottimizzate** su tutti i device

**Pronta per test reali!** 🚀 