# Android-Project

Tasks:  
1. Stellen Sie Ihre App in den Sprachen deutsch und englisch bereit.  

ACTIVITY 1:  
2. Ihr App soll beim Schütteln des Geräts einen GET-Request an http://app-imtm.iaw.ruhr-unibochum.de:3000/posts/random
senden und die empfange Nachricht anzeigen.  
3. Wenn der Näherungssensor verdeckt wird, soll die aktuell angezeigte Nachricht lokal auf dem Gerät  
archiviert werden. Speichern Sie zusätzlich zum Inhalt der Nachricht einen Zeitstempel beim Archivieren. Benutzen Sie zum Speichern die integrierte SQL-Lite Datenbank.  
4. Beim Aufrufen der App soll stets die aktuellste Nachricht aus dem Archiv angezeigt werden.  
5. Beim Pausieren der App sollen jedes Mal Text- und Hintergrundfarbe neu gesetzt werden.  

ACTIVITY 2:  
6. Erstellen Sie eine Activity in der alle gespeicherten Nachrichten mit Zeitstempel in einer ListView
angezeigt werden.  
7. Diese Liste soll vom User alphabetisch (Nachrichtentext), nach _id oder nach Empfangsdatum sortiert
werden können.  
8. Stellen Sie eine Möglichkeit bereit einzelne Nachrichten via SMS, E-Mail o.ä. zu teilen.  

ACTIVITY 3:  
9. Stellen Sie eine Möglichkeit bereit eigene Nachrichten an den Server zu senden (POST an http://appimtm.iaw.ruhr-uni-bochum.de:3000/posts). Übergeben Sie dem Server ein JSON-Objekt mit dem Key
‘msg‘ und Ihrer Nachricht als zugehöriger Value.  
