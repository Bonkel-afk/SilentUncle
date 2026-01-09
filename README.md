# SilentUncle

SilentUncle ist ein Alarmierungssystem für Netzwerke. Es ermöglicht das Senden und Empfangen von Alarmen (Feuer, Tier, Kunde) zwischen verschiedenen Instanzen im Netzwerk.

## Funktionen
- Senden von Alarmen über die Benutzeroberfläche.
- Empfangen von Alarmen über eine REST-API (`/api/alarms/receive`).
- Anzeige von Alarmen über System-Tray-Benachrichtigungen.
- Konfigurierbare Ziel-IPs für Alarme.

## Konfiguration
In der `src/main/resources/application.properties` können die Ziel-Instanzen konfiguriert werden:
```properties
alarm.destinations=http://192.168.1.10:8080,http://192.168.1.11:8080
```

## Build & Ausführung
### Als JAR bauen
```bash
./mvnw clean package
```
Das JAR befindet sich dann unter `target/silentuncle-0.0.1-SNAPSHOT.jar`.

### Als EXE (Windows)
Um eine .exe zu erstellen, kann `jpackage` (ab Java 14) verwendet werden. Nach dem `mvn package`:
```bash
jpackage --input target/ --name SilentUncle --main-jar silentuncle-0.0.1-SNAPSHOT.jar --main-class org.btc.com.silentuncle.SpringApplication --type exe --win-shortcut --win-menu
```
*Hinweis: Dies erfordert eine installierte WiX Toolset Umgebung auf Windows.*