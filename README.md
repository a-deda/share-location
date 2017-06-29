# Proposal
<b>Probleemstelling</b>

We leven in een tijdperk waarin technologie alomtegenwoordig is: er is geen ontkomen aan. De smartphone is één van deze technologieën. Wereldwijd dragen ruim twee miljard mensen dagelijks een apparaat bij zich dat in staat is om enorme hoeveelheden data over de gebruiker te verzamelen. Met name data betreffende de locatie van de gebruiker is erg waardevol voor meerdere partijen. Eén van deze partijen ligt erg dicht bij de gebruiker; de vrienden van de gebruiker. 

Een dergelijke applicatie kan erg handig zijn voor vriendengroepen. Op het moment dat vrienden een afspraak maken en elkaar bij aanvang van de afspraak niet kunnen vinden, weten beide partijen van elkaar waar ze zich bevinden, zonder dat ze op een andere manier contact hoeven te leggen (telefonisch, via berichten-apps, etc.). Bovendien kunnen instructies voor het vinden van de juiste locatie met deze informatie veel accurater zijn, wat tijd en frustratie bespaart. 
 
<b>Functies & onderdelen</b>

De applicatie zal worden gebouwd rondom groepen, waarbinnen men diens locatie kan delen met elkaar. Van tevoren wordt aangegeven wanneer een groep dient te vervallen, zodat het een erg bruikbare app wordt voor vriendengroepen. Er wordt een kaart weergegeven, met daarop de locaties van de andere gebruikers in de groep. Daaronder staat een lijst met de exacte afstand tot elk contact. Gebruikers kunnen in het groepenoverzicht een nieuwe groep aanmaken. Hier kunnen gebruikers aan worden toegevoegd aan de hand van hun e-mail adres.

Voordat een gebruiker de applicatie kan benutten, is het nodig dat er een account aangemaakt wordt. Wanneer iemand reeds een account aangemaakt heeft, kan er ingelogd worden. Na het registreren kan de gebruiker ervoor kiezen om ofwel een nieuwe groep aan te maken, of niets te doen. 
	
<b>Data</b>

De GPS-coördinaten zijn voor de app het belangrijkste onderdeel. Deze coördinaten worden voor elke gebruiker in een Firebase-database gezet. Deze data wordt vervolgens gebruikt om de nieuwe locaties van de contacten van de gebruiker - en de locatie van de gebruiker zelf - weer te geven.
 
<b>Externe componenten</b>

Zoals reeds genoemd, zal de applicatie gebruik maken van Firebase voor het opslaan van gebruikersgegevens (gebruikersnaam, locatie, etc.). Daarnaast zal, voor het weergeven van een kaart waarop de verschillende gebruikers staan aangegeven, de Google Maps API worden gebruikt.
 
<b>Potentiële technische problemen</b>

Het idee is om in de applicatie de locatie standaard elke minuut bij te werken. De afbakening van deze tijdseenheid kan op verschillende manieren geschieden. Beide manieren brengen hun eigen problemen met zich mee:
- Wanneer een minuut aan de hand van een klok-eenheid bepaald wordt, plaatsen alle gebruikers - die de standaard tijdseenheid geselecteerd hebben - tegelijkertijd een request op de database, wat bij een groot aantal gebruikers problematisch kan zijn.
- Het meten van de tijd op het apparaat van de gebruiker voorkomt dit probleem, maar is daarbij ook lastiger te implementeren.
 
<b>Gelijksoortige applicaties</b>

Er zijn een aantal apps in de Google Play Store die hetzelfde doen voor de gebruiker:
- Life360: focust zich op families; er kunnen groepen aangemaakt worden waarbinnen een chatfunctie is toegevoegd.
- Glympse: heeft geen duidelijke doelgroep. Verschilt fundamenteel met de voorgestelde applicatie door de gebruiker voor een van te voren bepaalde periode real-time te volgen. 
- Find My Friends: lijkt erg veel op de voorgestelde app, maar kent een aantal extra functies. Gebruikers kunnen ook hier groepen aanmaken, waarmee gechat kan worden en gebruikers kunnen hun contacten een alert sturen.
 
<b>Minimaal werkbare product (MVP)</b>

Voor deze applicatie zijn een aantal onderdelen essentieel. Allereerst moet de gebruiker een account aan kunnen maken bij het eerste gebruik, of in kunnen loggen wanneer deze uitgelogd is. Vervolgens moet een groepenoverzicht getoond worden, waarin alle groepen waarin de gebruiker zit zichtbaar moeten zijn.

Een functie die niet nodig is, maar het product iets completer maakt is ten eerste een <i>I’M HERE</i>-functie. Wanneer een gebruiker in de buurt is van een contact, kan het betreffende contact aangeklikt worden. Als dit gedaan wordt, deze functie ingeschakeld. De functie kan gebruikt worden wanneer men zich bevindt op drukke plekken - bijvoorbeeld op festivals - waardoor elkaar vinden erg lastig wordt. Wanneer deze functie geactiveerd wordt, kleurt het scherm van de gebruiker rood en wordt de helderheid tot het maximale niveau verhoogd. Tevens verschijnt de tekst “I’m here” op het scherm. De gebruiker kan zijn toestel boven zich uit steken en zo de kans op het vinden van de ander vergroten. 

Daarnaast is het wenselijk om voor het uitschakelen van de locatie-tracking om een wachtwoord te vragen. Kwaadwillenden, die de telefoon van een ander in handen zouden kunnen hebben, kunnen zo worden opgespoord.

<b>Optionele functies</b>
- Sturen van foto's van de omgeving naar groepen.
- Gebruikers in staat stellen een verzoek tot het maken van een foto te sturen.

# Schetsen
![alt text](http://i.imgur.com/fbQIqff.png)
![alt text](http://i.imgur.com/blnoChg.png)
![alt text](http://i.imgur.com/kB0be9W.png)
![alt text](http://i.imgur.com/OQ9FYOS.png)
![alt text](http://i.imgur.com/76Yy44B.png)
