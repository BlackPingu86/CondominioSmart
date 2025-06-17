# 🧪 Test Checklist - CondominioSmart

## ✅ Configurazione Progetto
- [x] AndroidManifest.xml aggiornato con tutte le attività
- [x] build.gradle con dipendenze Glide
- [x] google-services.json configurato
- [x] Layout XML validi
- [x] File Kotlin senza errori di sintassi

## 🎯 Funzionalità da Testare

### 1. **Anteprime File** (PagamentiAdapter.kt)
- [ ] **Test JPG/PNG**: Carica immagine e mostra miniatura 60x60dp
- [ ] **Test PDF**: Mostra icona PDF rossa
- [ ] **Test file non riconosciuto**: Mostra icona upload grigia
- [ ] **Test senza file**: Non mostra nulla

### 2. **Ricerca e Filtri** (PagamentiActivity.kt)
- [ ] **Campo di ricerca**: Funziona in tempo reale
- [ ] **Filtro "Tutti"**: Mostra tutti i pagamenti
- [ ] **Filtro "Da pagare"**: Mostra solo pagamenti non pagati
- [ ] **Filtro "Pagati"**: Mostra solo pagamenti pagati
- [ ] **Filtro "Fattura"**: Mostra solo fatture
- [ ] **Filtro "Bolletta"**: Mostra solo bollette
- [ ] **Filtro "Ricevuta"**: Mostra solo ricevute
- [ ] **Filtro "Altro"**: Mostra solo altri tipi
- [ ] **Combinazione filtri**: Ricerca + stato + tipo

### 3. **Conferma Eliminazione** (DettaglioPagamentoActivity.kt)
- [ ] **Dialog personalizzato**: Mostra dettagli del pagamento
- [ ] **Animazione pulsante**: Vibrazione al click
- [ ] **Eliminazione file**: Rimuove da Firebase Storage
- [ ] **Eliminazione record**: Rimuove da Firestore
- [ ] **Feedback successo**: Snackbar verde con chiusura automatica

### 4. **Visual Feedback** (Tutte le Activity)
- [ ] **Snackbar caricamento**: Progress indicator animato
- [ ] **Snackbar successo**: Icona verde con messaggio
- [ ] **Animazioni FAB**: Fade in/out durante operazioni
- [ ] **Feedback selezione file**: Animazione + Snackbar
- [ ] **Feedback filtri**: Conteggio risultati

### 5. **Flusso Completo**
- [ ] **Login** → HomeActivity
- [ ] **Home** → PagamentiActivity
- [ ] **Lista** → DettaglioPagamentoActivity
- [ ] **Nuovo** → NuovoPagamentoActivity
- [ ] **Salvataggio** → Ritorno alla lista
- [ ] **Eliminazione** → Ritorno alla lista

## 📱 Test su Smartphone

### Prerequisiti
- [ ] Smartphone Android (API 21+)
- [ ] Connessione internet
- [ ] Account Firebase configurato
- [ ] APK installato

### Test Manuali
1. **Installazione APK**
2. **Primo avvio e login**
3. **Navigazione tra schermate**
4. **Test upload file (JPG/PDF)**
5. **Test ricerca e filtri**
6. **Test eliminazione (admin)**
7. **Test feedback visivi**

## 🐛 Possibili Problemi
- [ ] Permessi storage Android 13+
- [ ] Connessione Firebase
- [ ] Dimensioni file troppo grandi
- [ ] Formati file non supportati
- [ ] Permessi admin per eliminazione

## 📊 Risultati Attesi
- ✅ Anteprime file funzionanti
- ✅ Ricerca e filtri responsive
- ✅ Conferma eliminazione sicura
- ✅ Feedback visivo professionale
- ✅ UX fluida e intuitiva 