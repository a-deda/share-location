# Introductie

Apps waarmee men zijn locatie kan delen met anderen zijn al tijden beschikbaar voor smartphones,
op verschilllende besturingssystemen. Veel van deze apps baseren zich op het idee dat men zijn
locatie non-stop wil delen met diens contacten. In de praktijk blijkt dit echter anders; hoewel
een app met een dergelijke functie voor velen van grote waarde kan zijn, worden deze apps door
het genoemde karakter maar weinig gebruikt. Hieruit ontstond het idee om een app te bouwen die
dit vermijdt. De gebruiker kan in deze app een groep aanmaken, waarin de locatie gedeeld wordt.
Per groep kan dan een eindtijd ingesteld worden, waarop deze zich vanzelf weer verwijdert. De
bedoeling is dan ook dat deze ook maar tijdelijk gebruikt wordt.

![Screenshot](http://i.imgur.com/JZ6yt6H.jpg)

# Technisch design

<b>Algemeen overzicht</b>

De app opent zich, wanneer de gebruiker niet ingelogd is, op het login-scherm. Vanzelfsprekend
kan de gebruiker hier inloggen met een e-mailadres en wachtwoord. Als de gebruiker
nog niet over een account beschikt, kan er, middels een knop onderaan het login-formulier, een
account aangemaakt worden op het registratie-scherm.

Nadat de gebruiker is ingelogd (of zojuist een account heeft aangemaakt), komt deze uit op het
groepen-overzicht. Hier worden alle groepen waarin de gebruiker zit, weergegeven. De groepen
kunnen uitgevouwen worden, waardoor alle andere gebruikers die in de groep zitten worden getoond.
 Ook kan men hier uit de groep stappen, waardoor alle locatie-updates naar deze groep worden
gestaakt. Als de gebruiker de knop met het kompas-icoon indrukt, wordt een kaart geopend waarop
de locaties van alle gebruikers in de groep worden aangegeven. Onder deze kaart bevindt zich
tevens een lijst met daarin de namen van alle groepsleden, een foto (mits de gebruiker deze heeft
 toegevoegd) en een afstand tot de huidige gebruiker.

In het groepen-overzicht kan vanaf de linkerzijkant een <i>drawer</i> worden geopend, waarin de
profielfoto van de ingelogde gebruiker kan worden aangepast. Ook is hier een optie 'instellingen'
 te vinden, die leidt naar een scherm waarop uitgelogd kan worden.

In de <i>Action Bar</i> aan de bovenkant van het groepen-overzicht, maar ook wanneer men zich in
de kaartweergave bevindt, kan een nieuwe groep aangemaakt worden middels een knop. Op het scherm
dat dan getoond wordt, kan een groepsnaam ingevuld worden en kunnen gebruikers aan de groep
worden toegevoegd op basis van het e-mailadres. Met behulp van de <i>FloatingActionButton</i>
onderaan kan een datum en tijd geselecteerd worden, waarop de groep komt te vervallen.

<b>Technische weergave</b>

<i>LoginActivity</i>

Bij het openen van de app wordt, zoals genoemd, een login-scherm getoond. Dit is de LoginActivity.
 De gebruiker vult hier zijn gegevens in, die dan worden geverifieerd in Firebase. Wanneer de
 gebruiker geen account heeft, kan deze worden aangemaakt in de RegisterActivity, welke met een
 knop onderaan de LoginActivity kan worden bereikt. De gegevens die hierin worden opgegeven,
 worden bij registratie opgeslagen in Firebase onder een node die de naam heeft van de
 gebruiker-ID. In deze node wordt later meer informatie opgeslagen, waardoor deze voor de
 gebruiker van groot belang is.

<i>MainActivity</i>

Na het inloggen wordt de MainActivity gestart; het centrale onderdeel van de app. Hierin wordt
vervolgens een GroupsFragment geladen, die bij initialisatie start met het ophalen van de groepen
 van de gebruiker uit Firebase. Voor het overzicht is voor het uitwisselen van data tussen
 Firebase en de applicatie een <i>FirebaseHelper</i> klasse aangemaakt. Voor iedere bewerking in
 Firebase, op alle plekken in de app, wordt deze klasse aangeroepen. Hier wordt eveneens gestart
 met het vastleggen van de locatie van de gebruiker in Firebase. Deze locatie wordt opgehaald met
  een GPSHelper klasse, welke vervolgens wordt doorgegeven aan de FirebaseHelper klasse.

Om het ophalen van groepsinformatie mogelijk te maken, is er in de Firebase-database sprake van
wederzijdse referentie tussen groepen en gebruikers: de groepen bevatten de gebruiker-ID's van de
 gebruikers in de groep, de gebruikers bevatten de groep-ID's van de groepen waar ze toe behoren.
Om de groepen vervolgens op te halen, wordt gestart bij de gebruiker. Hieruit worden de
groep-ID's opgehaald, welke weer worden gebruikt als input voor het ophalen van de
groepsinformatie. Deze groepsinformatie wordt op zijn plaats ook gebruikt voor het ophalen van de
 namen van de andere gebruikers in de groep. Deze informatie wordt dan weergegeven in een
 ExpandableListView, voor een duidelijke weergave.

Wanneer er op het kompas-icoon gedrukt wordt, wordt vanuit de GroupsFragment in de FirebaseHelper
 klasse een methode aangeroepen die de locaties van de groepsleden aan de hand van hun
 gebruiker-ID ophaalt uit Firebase. Wanneer deze gegevens opgehaald zijn, ontvangt de
 GroupsFragment een Callback met daarin <i>User</i>-objecten, waarin de naam, locatie en de ID
 van alle gebruikers in de groep worden opgeslagen. Hiermee wordt de MapFragment gestart.
 Hierin worden, wanneer de Google Map geladen is, de <i>markers</i> voor de kaart
  geïnitialiseerd, wat inhoudt dat de FirebaseHelper opnieuw wordt aangeroepen; deze keer voor
  het ophalen van de foto van de gebruiker zelf en de gebruikers in de groep, deze worden dan
  toegewezen aan de markers die geplaatst worden. Allerlaatst wordt hier een timer geïnitieerd,
  die ervoor zorgt dat er elke 2,5 seconde een update van de gebruikerslocaties plaatsvindt. Na
  de initialisatie van de markers blijven deze bewaard, waardoor er alleen nieuwe coördinaten
  hoeven worden toegewezen aan de markers, wat de souplesse van de app bevordert.
Onder de kaart wordt een ListView weergegeven, met daarin de namen van elke gebruiker in de groep
 (behalve de ingelogde gebruiker), de foto's van deze gebruikers (mits deze zijn geüpload) en de
 afstand van elk contact tot de ingelogde gebruiker. Deze afstand wordt met elke update van de
 coordinaten van de gebruikers bijgewerkt. In dit stadium is het object voor de gebruiker,
 <i>User</i>, compleet gevuld.

Naast het kompas-icoon staat, aan de linkerkant, een prullenbak-icoon weergegeven. Hiermee wordt
een AlertDialog geopend, waarop de gebruiker de keuze wordt gegeven om de groep daadwerkelijk te
verlaten. Wanneer dit gedaan wordt, wordt met de FirebaseHelper de gebruiker-ID verwijderd uit de
 groep-node in Firebase. Vervolgens wordt in de gebruiker-node de groep-ID verwijderd, zodat er
 geen referenties meer bestaan. Als de gebruiker de enige is in de groep, zal met deze actie de
 hele groep verwijderd worden.

Zoals genoemd, is het voor de gebruiker mogelijk om een foto toe te voegen aan het account. Dit
kan gedaan worden door in de NavigationDrawer die aan de linkerkant van het scherm kan worden
geopend het lege veld boven de naam van de ingelogde gebruiker aan te tikken. Een standaard
keuzemenu van Android zal zich dan openen, met de optie om een foto te maken met de camera of een
 foto te kiezen uit de galerij. Na het maken of kiezen van een foto wordt deze in twee formaten
 opgeslagen in de Firebase Storage: eenmaal in het originele formaat en eenmaal in het
 standaardformaat dat voor de kaart nodig is, waarvoor de PhotoFixer klasse gebruikt wordt. Op deze
 manier wordt bij het laden van de foto's voor de kaart niet teveel data verbruikt en verloopt
 het downloaden sneller.

Tenslotte bevindt zich in de MainActivity de SettingsFragment, welke alleen een knop bevat die de
 gebruiker uitlogt en deze vervolgens opnieuw de LoginActivity toont.

<i>AddGroupActivity</i>
Door in de ActionBar bovenin de GroupsFragment of de MapFragment op 'Nieuwe groep' te drukken,
wordt de AddGroupActivity geopend. De gebruiker kiest hierin een groepsnaam, de groepsleden en
een tijd en datum waarop de groep verloopt.

De groepsleden worden, voordat ze in de ListView onder de invoervelden verschijnen, met de
FirebaseHelper geverifieerd. Dit gebeurt wanneer de gebruiker op de knop drukt met het
plus-icoon, naast het invoerveld voor het e-mailadres van de toe te voegen gebruiker. Eerst wordt
 bekeken of dit e-mailadres geregistreerd staat, door over alle gebruiker-nodes in de Firebase
 database te itereren. Als het e-mailadres gevonden wordt, wordt de gebruiker-id van de
 betreffende gebruiker opgeslagen in een lijst, welke wordt opgehaald wanneer de gebruiker op de
 maak-groep-knop drukt. De naam en het e-mailadres van de gebruiker worden dan ook teruggegeven
 aan de AddGroupActivity, welke dit in de ListView weergeeft.
 De lijst van gebruiker-ID's wordt eveneens gebruikt om te verifiëren dat de gebruiker niet
  tweemaal aan dezelfde groep wordt toegevoegd. Gebeurt dit wel, dan krijgt de gebruiker dit te
  zien.

De datum en tijd waarop de groep verloopt kunnen worden gekozen met de FloatingActionButton
onderin het scherm. Wanneer hierop gedrukt wordt, wordt eerst een DatePicker getoond. Na het
kiezen van de datum wordt meteen een TimePicker geïnitieerd. De gegevens hieruit worden
opgeslagen in een <i>DateTime</i> klasse, zodat deze makkelijk kunnen worden doorgegeven. Het
verstrijken van de datum wordt gecheckt bij het maken van de GroupsFragment. Groepen die op dat
moment bestaan na de datum en tijd die in de node in Firebase staan beschreven, worden dan
verwijderd.

# Proces

Tijdens het bouwen van de app traden geregeld complicaties op, welke voornamelijk te maken hadden
 met de bewerkingen in Firebase. Alle bewerkingen in Firebase kennen een asynchroon patroon, wat
 betekent dat de code in de app doorloopt nadat een bewerking is gestart in Firebase. Dit leverde
  enkele problemen op, aangezien de data die uit Firebase moest worden opgehaald, bedoeld was
  voor de klasse die de Firebase-methode (in de FirebaseHelper) aanriep. Omdat deze gegevens,
  door het karakter van Firebase, niet met behulp van een <i>return</i> teruggegeven konden
  worden aan de methode die de FirebaseHelper aanriep, moest er gebruikgemaakt worden van
  Interfaces, welke dan ook in bijna elke methode in de FirebaseHelper te vinden is.

In het laatste stadium van het bouwen van de app moesten de foto's voor de markers op de kaart
nog op een correcte manier worden opgehaald uit de Firebase Storage. Hiervoor werd een lijst van
<i>User</i>-objecten als input gegeven, welke vervolgens moesten worden aangevuld met de foto die
 op de kaart moest verschijnen. Opnieuw was het asynchrone patroon van Firebase een probleem. Dit
  patroon is hierbij getracht te omzeilen met behulp van een Future-object, welke wacht op het
  vullen van een variabele, maar dit is helaas niet gelukt.

In eerste instantie werd geprobeerd om alle gegevens van de gebruikers, inclusief de foto's, elke
 keer op te halen wanneer er een locatie-update was voor tenminste één van de gebruikers in de
 groep. Omdat dit leidde tot het aanmaken van meer threads dan Firebase accepteerde, is er
 gekozen voor een interval van 2,5 seconde. Bovendien wordt de code voor het ophalen van de
 foto's voor de markers maar éénmaal geladen bij het initiëren van deze markers.

Het verwijderen van groepen was een functie waarvan de implementatie tot het laatste moment is
uitgebleven. Er is in een eerder stadium getracht om hiervoor een SlideView te gebruiken op de
ListItems die de groepsinformatie bevatten (in GroupsFragment), wat leidde tot disfunctionaliteit
 van de ExpandableListView. Ook de SlideView was onbruikbaar, dus is besloten om deze functie te
 vertalen naar een simpele knop naast de knop voor de MapFragment.

Een contactoverzicht, zoals in het eerder gepubliceerde design document beschreven staat, is er
in de laatste versie van de app niet gekomen. Vrij snel werd besloten dat de app zich moet
centraliseren rondom groepen, in plaats van losse gebruikers. Het contact-overzicht werd daarom
vervangen door het groepen-overzicht. Om dezelfde reden is gekozen voor het openen van de app op
het groepen-overzicht in plaats van op een kaart waarop gebruikers weergegeven zijn.

Het idee van een aparte FirebasePusher en FirebaseGetter klasse blijkt achteraf toch vrij
bruikbaar. Van dit idee is afgeweken, omdat bij het schrijven van de eerste methoden de omvang
van één methode voor alle Firebase bewerkingen mee leek te vallen. Uiteindelijk is de
FirebaseHelper klasse vrij groot geworden, wat door het blijven bij de initiële beslissing
voorkomen had kunnen worden.

# Conclusie

De uiteindelijke applicatie is in veel opzichten anders dan de app die aan het begin van het
ontwikkelingstraject is voorgesteld. Het maken van groepen die zich na verloop van tijd vanzelf
verwijderen blijkt een goede keuze geweest te zijn. Echter, hierdoor is eveneens afgeweken van
het accepteren van een uitnodiging door de gebruiker, waardoor iedere kwaadwillende die het
e-mailadres van een gebruiker van deze app bezit, de locatie kan zien van deze persoon. De
gebruiker heeft wél de mogelijkheid om een dergelijke groep te verlaten, maar deze kan hier te
laat achter komen. Daarnaast is de app esthetisch gezien niet perfect, wat het voor gebruikers
minder aantrekkelijk maakt om van de app gebruik te maken. Om deze app daadwerkelijk bruikbaar
te maken voor het grote publiek, zal de beveiling en de layout in ieder geval verbeterd moeten
worden.


