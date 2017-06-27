# dag 1 - 6 juni
Navigation drawer, fragment voor kaart (eerste pagina) en fragment voor instellingen aangemaakt.
 
# dag 2 - 7 juni
Google Maps geïmplementeerd en fragment voor contacten aangemaakt.
 
# dag 3 - 8 juni
ListAdapter aangemaakt voor weergave van contacten, nieuwe activity gemaakt voor het toevoegen
van contacten, testcontacten in lijst en op kaart weergegeven (met fake data) en locatie van de
gebruiker wordt weergegeven op de kaart.

# dag 4 - 9 juni
Pagina contact toevoegen omgevormd tot groepen toevoegen, nieuw fragment ‘groepen’ gemaakt,
proposal bijgewerkt.
 
# dag 5 - 12 juni
Login en registreer activity aangemaakt en gekoppeld aan Firebase.

# dag 6 - 13 juni
Voor- en achternaam toegevoegd aan registratie. Gebruiker blijft ingelogd totdat deze zelf
uitlogt. Coördinaten van gebruiker worden opgehaald en opgeslagen in Firebase onder referentie
van de UserID, maar door een bug worden de gegevens onder een andere UserID eveneens overschreven.

# dag 7 - 14 juni
App laadt nu informatie voor ingelogde gebruiker en begonnen met de implementatie van groepen.

# dag 8 - 15 juni
Data wat betreft groepen kan worden opgehaald en geüpdate worden in Firebase. Elke gebruiker
heeft een node 'groups' waaronder de keys van de groepen staan waarin de gebruiker zich bevindt.
Onder de node 'groupData' staan alle groepen met daarin de keys van de gebruikers die de groep
bevat. Idee van de app lichtelijk aangepast: app is gebaseerd op tijdelijke groepen: bij het
aanmaken van de groep kan een gebruiker aangeven tot wanneer de groep moet blijven bestaan.
Vervolgens verwijdert deze groep zich vanzelf.

# dag 9 - 16 juni
Gekeken naar hoe de data in de onDataChange van Firebase verkregen kan worden in de class die de
Firebase-functie aanroept, aangezien al deze functies in een aparte class staan. Lichte
aanpassingen gemaakt aan de LoginActivity en het ophalen van de namen van groepsleden uit de
database voltooid.

# dag 10 - 19 juni
Gestart met het implementeren van interfaces om het probleem van vrijdag (dag 9) op te lossen.
ExpendableListAdapter in de GroupsFragment wordt nu correct gevuld; alle groepen waar de
gebruiker in zit met alle andere groepsleden worden weergegeven. Hierbij ontbreken nu nog de
groepsfoto en de vervaltijd van de groep (en de profielfoto's van alle individuele contacten).
Hiervoor moet de PhotoFixer class (reeds aangemaakt) aangevuld worden en is een optie nodig voor
het toevoegen van een profielfoto, waarschijnlijk komt deze in het zijmenu.

# dag 11 - 20 juni
Mogelijkheden onderzocht om het verwijderen van groepen op een duidelijke manier te implementeren
. Hierbij ging de voorkeur uit naar het gebruik van een swipefunctie over de rijen van de
ExpandableListView, waaronder een prullenbak-icoon tevoorschijn komt. Een soortgelijke werkwijze
wordt in de Gmail-applicatie toegepast. Dit blijkt echter vrij lastig te zijn, dus wordt dit
 vooruitgeschoven; de functies omtrent het tonen van gebruikers op de kaart (met een vooraf
 ingestelde foto) is belangijker voor het MVP. Om een kaart te kunnen tonen voor elke groep zijn
 knoppen toegevoegd in de rijen van de ExpandableListView, welke nog geen functie hebben. De
 benodigde data (coördinaten & naam van gebruikers) worden al opgehaald uit Firebase, dit moet
 alleen nog op een kaart weergegeven worden.

# dag 12 - 21 juni
Bij het klikken op de knop in de lijst met groepen wordt een kaart geladen met de locaties van de
 leden in die groep. Enige complicaties verholpen betreffende het verversen van de locatiedata
 bij de verkeerde gebruikers. Er moet nog voor gezorgd worden dat er rekening gehouden wordt met
 de tijd waarop de groep vervalt. Waarschijnlijk gaat deze tijd met een groot tijdsinterval
 gecheckt worden, zodat het aantal requests bij Firebase beperkt blijft. Ook moet er nog steeds
 een PhotoFixer() class gevuld worden met een foto die de gebruiker zelf uploadt.

# dag 13 - 22 juni
Gebruikers kunnen nu een foto uploaden die met de camera gemaakt wordt, of uit de galerij wordt
opgehaald. Deze foto blijft behouden op het moment dat een gebruiker uitlogt, dus deze functie
werkt compleet. Daarnaast is nu de eindtijd van groepen (als deze is ingesteld) zichtbaar in het
groepenoverzicht. Groepen verwijderen zichzelf uit Firebase als deze tijd verstreken is.

# dag 14 - 23 juni
Nogmaals gekeken naar de implementatie van een SlideView voor de lijst. Vervolgens een begin
gemaakt aan het weergeven van foto's van gebruikers op de kaart.

# dag 15 - 26 juni
Het begin dat op dag 14 gemaakt is bleek vrij complexe code te behoeven. Door de vele
branch points was deze code lastig te volgen. Dit moest dus worden vergemakkelijkt,
door het maken van een aparte methode. Het laden van de foto van de gebruiker zelf lukt (maar ook
 beperkt), de overige gebruikers worden op de kaart niet meer weergegeven, ook niet met een
 normale marker. Ook de afstand tussen gebruikers is verdwenen, dus dit moet worden teruggehaald.
  Daarnaast is de User class Parcelable gemaakt (was Serializable). De oude implementatie leidde
  tot een crash wanneer de app afgesloten werd, maar in de achtergrond wel draaide.