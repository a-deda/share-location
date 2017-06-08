# Design document
Een goed functionerende app in de eerder beschreven context dient te bestaan uit een aantal onderdelen, die onderling op een correcte manier data uitwisselen. Het onderstaande diagram laat zien welke onderdelen dit zullen zijn.

![alt link](http://imgur.com/tEEYzbt)

De app zal starten bij de LoginActivity, waar aan de gebruiker gevraagd wordt om in te loggen, of - als de gebruiker nog geen account heeft - een account aan te maken. Een nieuw account wordt met een FirebasePusher class in de Firebase-database gezet, aan de hand van de gebruikersnaam. Voordat de verdere informatie in een Contact class wordt ondergebracht bij deze gebruikersnaam, wordt de profielfoto die de gebruiker geüpload heeft, verwerkt tot een standaard formaat. Dit zal gebeuren met een aparte PhotoFixer class, die de foto bijsnijdt tot een bepaalde ratio, en daarna verkleint naar een gegeven grootte.
	Op het moment dat er data weergegeven moet worden, wordt de FirebaseGetter class aangeroepen, om de benodigde gegevens uit Firebase te halen. Elk fragment dat een verzoek doet, krijgt de Contact class uit Firebase binnen. Hieruit kan de benodigde informatie worden geëxtraheerd.
	Het kaartje in de KaartFragment class maakt gebruik van de Google Maps API. Voor het meten van afstanden tussen de verschillende markers (die de coördinaten uit de Contact class overnemen) kan een functie uit de Google Maps API Utility Library worden gebruikt. 
 
De organisatie van de database in Firebase is erg belangrijk bij deze applicatie. De verschillende gebruikers zullen, om op een makkelijke manier teruggevonden te kunnen worden, geïndexeerd worden op basis van hun gebruikersnaam. De rest van de informatie die bij het account hoort zal dan in een Contact class, onder deze gebruikersnaam staan. Op dezelfde plek zullen de contacten van de gebruiker terug te vinden zijn, waarbij ook aangegeven staat of het connectieverzoek tussen de twee gebruikers reeds geaccepteerd is. Een schematische weergave hiervan ziet er als volgt uit:

![alt link](http://imgur.com/FWOxqaw)
