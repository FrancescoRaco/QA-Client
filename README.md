# QA-Client
Question Answering - Programma Client

Il programma client utilizza la porta TCP 8080 per dialogare con il server ed offre un’interfaccia grafica mediante la libreria JavaFX. Spiegazioni dettagliate sono presenti nei commenti del codice.

Istruzioni per importare il progetto client nell'ambiente di sviluppo:

- Scaricare il file .zip dalla Github repository ed importarlo in un nuovo progetto
- Assicurarsi che la cartella "src" sia riconosciuta "source folder" (da usare per i file sorgente)
- Assicurarsi che "client" e "gui" (all'interno di "src") siano importati come package e che sia presente il file module-info.java
- Assicurarsi che la cartella "data" sia presente nel class path del progetto (configurabile tramite il build path)
- Scaricare la libreria JavaFX 12.0.1 SDK specifica per il sistema operativo in uso al seguente URL: https://gluonhq.com/products/javafx/ 
- Estrarla ed aggiungere i file .jar (path: /lib) in una nuova libreria utente "JavaFX12" (senza modificare i percorsi delle sottocartelle sul disco fisso e/o spostare gli altri file di sistema)
- Includere tale libreria "JavaFX12" nel module path del progetto (per sicurezza effettuare un cleaning del progetto al termine delle precedenti operazioni)
- Configurare opportunamente il Build Path del progetto scegliendo una versione JDK 11+ per assicurarsi che il programma funzioni
- Eseguire la classe "Main", all'interno del package "gui" (percorso: gui.Main)

Istruzioni per eseguire il runnable .jar:

- Scaricare il file .jar eseguibile specifico per la piattaforma in uso:

- Linux: https://drive.google.com/open?id=1z5unqAlasqllzZTpgLCGKU_vAPiEib1e
- Mac: https://drive.google.com/open?id=1jTnC4XD6RN61uBOM8kVIFe6WouH8l1wq
- Windows: https://drive.google.com/open?id=1tsHD-XVtF9Xs7dWesmOwU6kXKLKwTDhP

- Estrarre la cartella "bin" dalla radice del file .jar appena scaricato e posizionarne tutti i file .so/.dylib/.dll all'interno della cartella "bin" relativa invece al percorso della JVM in uso (JDK 11+)
- Assicurarsi che il file .jar sia eseguito dall'applicazione javaw (all'interno della cartella "bin" relativa alla JVM in uso)
- Eseguire il programma VRP-Client_[SO-Name].jar mediante doppio click del mouse (oppure da linea di comando utilizzando la seguente sintassi: java -jar nomeFile.jar)



Si mostrano di seguito alcune schermate dell’esecuzione del programma selezionando la quick scan. Questa scelta è consigliata in quanto essa effettua la ricerca su un indice di dimensioni molto ridotte, che ha indicizzato solo le triple degli esempi spiegati in questo documento: ciò consente di ottenere una rapida risposta dal server per valutarne il funzionamento in tempi ragionevolmente bassi (l’indice del cinema completo, che è comunque una versione ridotta dell’intero database freebase, pesa infatti più di 3 GB su un server non molto prestante).
