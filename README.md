# PAP22L-Z03
Authors:
Emilia Wojtiuk
Jakub Sadowski
Jakub Cimochowski
Jakub Nitkiewicz

Rodzaj aplikacji: desktop    
Nazwa aplikacji: planTask   
Tematyka aplikacji: planer zadań    

Wymagania funkconalne:

Priorytetowe 1 poziomu:
- planer zajęć 
- kalendarz (np święta/ ważne dni)
- wydarzenia bez godzin (np zakładka “cały dzień”)

Priorytetowe 2 poziomu:
- przypominanie o wydarzeniach
- tło wydarzeń ciemne/jasne w zależności od pory dnia
- wydarzenia na “kiedyś”
- wybór osoby używającej aplikacji (jak na Netflixie)

Priorytetowe 3 poziomu:
- pogoda/godziny wschodu i zachodu słońca
- czas na komputerze 

Extra:
- widget
- ścieżki do folderów

Wymagania niefunkcjonalne:

Biblioteka graficzna: javafx    
Biblioteka do połączenia z bazą danych: JDBC    
Rodzaj bazy danych: relacyjna   
Baza danych: Microsoft Azure SQL Database   

PAP22L-Z03
Autorzy: Emilia Wojtiuk, Jakub Sadowski, Jakub Cimochowski, Jakub Nitkiewicz

**Prezentacja projektu na etap I**

Diagram z głównymi warstwami aplikacji

![Diagram warstw aplikacji](diagram.png)

Projekt na chwilę obecną
Na moment obecny nasz projekt składa się z czterech pakietów app, ICSFiles, loginapp, WeatherInfo. Pakiet app zawiera klasy: App, AppPanel, Calendar, Planner, Settings, Statistics. Wykorzystywane są przy tworzeniu aplikacji, która ma ukazać się użytkownikowi po zalogowaniu. Pakiet ICSFiles zawiera klasy ICSFilesReader oraz ToICSFileWritter. Klasy te służą do czytania oraz zapisywania plików z rozszerzeniem ics. Pakiet loginapp posiada klasy: Database, LoginApplication, LoginController, LoginPanelControler, RegisterController, User. Używane są do tworzenia panelu logowania oraz rejestracji, który to łączy się z bazą danych. Pakiet WeatherInfo posiada klasy JsonHandling oraz WeatherInfo. Korzystamy z nich w celu uzyskania aktualnych danych meteorologicznych poprzez api korzystające z kodu pocztowego. 


Wymagania środowiskowe i instrukcja zbudowania i uruchomienia aplikacji z kodu źródłowego

Nasz zespół korzysta ze zintegrowanego środowiska programistycznego InteliJ IDEA dlatego też najwygodniej przy uruchomieniu aplikacji z kodu źródłowego jest go użyć. Wszystkie zewnętrzne biblioteki dodane są do Mavena. Program do działania potrzebuje SDK Oracle Openjdk version 17.0.2. W samej aplikacji znajdują się dwie klasy z funkcjami main App oraz LogginApplication. Pierwsza wyświetla to co ma zobaczyć użytkownik po zalogowaniu natomiast druga uruchamia logownie\
Diagram z Mavena:

 ![Diagram z maven](mavenDiagram.png)


# Etap 3
 
## Projekt Aplikacji

### Główne klasy:
**•	Database**\
Służy do komunikowania się z bazą danych poprzez metody pozwalające na dodawanie, zmienianie, usuwanie i sprawdzanie wierszy z odpowiednich tabel w bazie danych.\
**•	User**\
Model użytkownika będący singletonem. Przechowuje dane użytkownika i umożliwia ich modyfikację.\
**•	WeatherInfo**\
Umożliwia uzyskanie informacji pogodowych na podstawie kodu pocztowego.\
**•	ISCFilesReader**\
Pozwala na czytanie plików ics i zapisywanie wyniku jako listę json obiektów, umożliwia dodawanie wydarzeń z pliku ics do aplikacji.\
**•	ToICSFileWritte**\
Pozwala na pisanie do plików ics mając listę json obiektów, umożliwia zapisywanie wydarzeń z kalendarza do pliku ics.\
**•	Weather**\
Kontroler obsługujący wyświetlanie pogody.\
**•	Event**\
Implementuje wydarzenia. Pośredniczy ona pomiędzy wydarzeniami w aplikacji a wydarzeniami w bazie danych.\
**•	Calendar**\
Kontroler obsługujący wyświetlanie kalendarza.\
**•	AppPanel**\
Główny kontroler - obsługuje wydarzenia w głównej zakładce aplikacji.\
**•	Settings**\
Kontroler obsługujący zakładkę ustawień


### Wykorzystywane narzędzia (środowiska programistyczne), biblioteki, zależności 
Środowiskiem programistycznym wykożystywanym przez naszą grupę było, stwożone przez firmę JetBrains, Intelij IDEA. Projekt całkowicie stwożony został w języku programowania Java. W celu automatyzacji budowania oprogramowania użyty został Maven. Biblioteką użytą do twożenia graficznego interfejsu użytkownika jest JavaFx. Natomiast wykożystaną przez nas bazą danych jest Microsoft Azure SQL Database.
#### Najważniejszymi bibliotekami przez nas wykożystywanymi są: 
- Json służąca do obsługi formatu tekstowego JSON, 
- JUnit użytą do stworzenia testów jednostkowych,
- JavaFx  wykorzystaną do tworzenia graficznego interfejsu,
- CalendarFx użytą w celu dodawania i prezentowania wydarzeń,
- mssql-jdbc potrzebną do komunikacji z bazą danych,
- scenic-view która w naszym przypadku służy do dostania się do plików css wykorzystywanego przez nas calendarfx,
- iCal4j którą posługujemy się w celu odczytu oraz zapisu plików z rozszerzeniem ics.
- java.net oraz java.io, do obsługi API pogodowego oraz zapisu informacji w formacie .json\
\
  **Diagram połączeń z Mavena:**
  ![](module-info.png)
### Odniesienie do zastosowanego wzorca projektowego
#### Diagram zastosowanego wzorca MVC
![](pap-3.png)
### Wymagania środowiskowe
Maszyna wirtualna java, internet.
### Instrukcja budowania kodu i uruchomienia aplikacji
Należy sklonować repozytorium z projektem do IntelliJ. Następnie wykonać build projektu. Aplikacja uruchamiana jest poprzez uruchomienie metody main w klasie LoginApplication. Hierarchia: pap22l-z03\src\main\java\com\app\loginapp\LoginApplication.java.


