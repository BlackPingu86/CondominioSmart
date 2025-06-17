# 📱 Test su Smartphone - CondominioSmart

## 🚀 Preparazione

### 1. **Prerequisiti Hardware**
- Smartphone Android (API 21+)
- Connessione internet stabile
- Spazio libero: almeno 100MB
- File di test: JPG, PDF

### 2. **Prerequisiti Software**
- APK dell'app installato
- Account Firebase configurato
- Permessi storage concessi

## 🧪 Test Step-by-Step

### **FASE 1: Installazione e Setup**
1. **Installare APK** sul dispositivo
2. **Concedere permessi** quando richiesto
3. **Avviare l'app** per la prima volta
4. **Effettuare login** con credenziali Firebase

### **FASE 2: Test Navigazione**
1. **HomeActivity** - Verificare che tutte le card siano visibili
2. **Pagamenti** - Toccare la card pagamenti
3. **PagamentiActivity** - Verificare caricamento lista

### **FASE 3: Test Anteprime File**
1. **Creare nuovo pagamento** con file JPG
2. **Verificare miniatura** nella lista (60x60dp)
3. **Creare pagamento** con file PDF
4. **Verificare icona PDF** rossa
5. **Creare pagamento** senza file
6. **Verificare** che non mostri nulla

### **FASE 4: Test Ricerca e Filtri**
1. **Campo di ricerca** - Digitare parte del titolo
2. **Verificare risultati** in tempo reale
3. **Filtro "Da pagare"** - Selezionare chip
4. **Verificare** solo pagamenti non pagati
5. **Filtro "Fattura"** - Selezionare chip
6. **Verificare** solo fatture
7. **Combinazione** - Ricerca + filtro stato + tipo

### **FASE 5: Test Visual Feedback**
1. **Caricamento** - Verificare Snackbar con progress
2. **Successo** - Verificare Snackbar verde
3. **Errori** - Verificare messaggi dettagliati
4. **Animazioni** - Verificare fade FAB
5. **Feedback filtri** - Verificare conteggio risultati

### **FASE 6: Test Eliminazione (Admin)**
1. **Aprire dettaglio** di un pagamento
2. **Verificare FAB elimina** (solo se admin)
3. **Toccare FAB** - Verificare dialog personalizzato
4. **Verificare dettagli** nel dialog
5. **Confermare eliminazione** - Verificare animazione
6. **Verificare successo** - Snackbar verde
7. **Verificare ritorno** alla lista

## 📊 Checklist Test

### ✅ **Anteprime File**
- [ ] Miniatura JPG visibile e corretta
- [ ] Icona PDF rossa per file PDF
- [ ] Icona generica per file non riconosciuti
- [ ] Nessuna anteprima per file mancanti

### ✅ **Ricerca e Filtri**
- [ ] Ricerca funziona in tempo reale
- [ ] Filtri stato funzionano correttamente
- [ ] Filtri tipo funzionano correttamente
- [ ] Combinazione filtri funziona
- [ ] Feedback risultati mostrato

### ✅ **Conferma Eliminazione**
- [ ] Dialog personalizzato con dettagli
- [ ] Animazione pulsante elimina
- [ ] Eliminazione file + record
- [ ] Snackbar successo verde
- [ ] Chiusura automatica activity

### ✅ **Visual Feedback**
- [ ] Snackbar caricamento con progress
- [ ] Snackbar successo con icona
- [ ] Animazioni FAB fluide
- [ ] Feedback selezione file
- [ ] Messaggi errore dettagliati

## 🐛 Risoluzione Problemi

### **Problema: App non si avvia**
- Verificare connessione internet
- Verificare configurazione Firebase
- Controllare log errori

### **Problema: Upload file fallisce**
- Verificare permessi storage
- Controllare dimensione file
- Verificare formato supportato

### **Problema: Filtri non funzionano**
- Verificare dati nel database
- Controllare log errori
- Verificare connessione Firestore

### **Problema: Eliminazione non disponibile**
- Verificare ruolo utente (deve essere ADMIN)
- Controllare permessi Firebase
- Verificare connessione

## 📈 Metriche di Successo

- ✅ **Tempo di caricamento** < 3 secondi
- ✅ **Responsività UI** - Nessun lag
- ✅ **Accuratezza filtri** - 100% risultati corretti
- ✅ **Stabilità** - Nessun crash
- ✅ **UX** - Flusso intuitivo e fluido

## 🎯 Risultati Attesi

Dopo il test su smartphone, l'app dovrebbe:
1. **Funzionare stabilmente** senza crash
2. **Mostrare anteprime** corrette per tutti i tipi di file
3. **Filtrare accuratamente** i pagamenti
4. **Confermare eliminazioni** in modo sicuro
5. **Fornire feedback** visivo professionale
6. **Offrire UX** fluida e intuitiva 