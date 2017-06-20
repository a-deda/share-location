# dag 1 - 6 juni
Navigation drawer, fragment voor kaart (eerste pagina) en fragment voor instellingen aangemaakt.
 
# dag 2 - 7 juni
Google Maps geïmplementeerd en fragment voor contacten aangemaakt.
 
# dag 3 - 8 juni
ListAdapter aangemaakt voor weergave van contacten, nieuwe activity gemaakt voor het toevoegen van contacten, testcontacten in lijst en op kaart weergegeven (met fake data) en locatie van de gebruiker wordt weergegeven op de kaart.

# dag 4 - 9 juni
Pagina contact toevoegen omgevormd tot groepen toevoegen, nieuw fragment ‘groepen’ gemaakt, proposal bijgewerkt.
 
# dag 5 - 12 juni
Login en registreer activity aangemaakt en gekoppeld aan Firebase.

# dag 6 - 13 juni
Voor- en achternaam toegevoegd aan registratie. Gebruiker blijft ingelogd totdat deze zelf uitlogt. Coördinaten van gebruiker worden opgehaald en opgeslagen in Firebase onder referentie van de UserID, maar door een bug worden de gegevens onder een andere UserID eveneens overschreven.

# dag 7 - 14 juni
App laadt nu informatie voor ingelogde gebruiker en begonnen met de implementatie van groepen.

# dag 8 - 15 juni
Data wat betreft groepen kan worden opgehaald en geüpdate worden in Firebase. Elke gebruiker heeft een node 'groups' waaronder de keys van de groepen staan waarin de gebruiker zich bevindt. Onder de node 'groupData' staan alle groepen met daarin de keys van de gebruikers die de groep bevat. Idee van de app lichtelijk aangepast: app is gebaseerd op tijdelijke groepen: bij het aanmaken van de groep kan een gebruiker aangeven tot wanneer de groep moet blijven bestaan. Vervolgens verwijdert deze groep zich vanzelf.

# dag 9 - 16 juni
Gekeken naar hoe de data in de onDataChange van Firebase verkregen kan worden in de class die de Firebase-functie aanroept, aangezien al deze functies in een aparte class staan. Lichte aanpassingen gemaakt aan de LoginActivity en het ophalen van de namen van groepsleden uit de database voltooid.

# dag 10 - 19 juni
Gestart met het implementeren van interfaces om het probleem van vrijdag (dag 9) op te lossen. ExpendableListAdapter in de GroupsFragment wordt nu correct gevuld; alle groepen waar de gebruiker in zit met alle andere groepsleden worden weergegeven. Hierbij ontbreken nu nog de groepsfoto en de vervaltijd van de groep (en de profielfoto's van alle individuele contacten). Hiervoor moet de PhotoFixer class (reeds aangemaakt) aangevuld worden en is een optie nodig voor het toevoegen van een profielfoto, waarschijnlijk komt deze in het zijmenu.