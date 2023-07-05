package eksamen2023.kontroll;

import eksamen2023.hjelpeklasser.Filbehandling;
import eksamen2023.model.ContainerSkip;
import eksamen2023.model.CruiseSkip;
import eksamen2023.model.Land;
import eksamen2023.model.Skip;
import eksamen2023.model.Status;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Kontrollerklasse for objektene
 *
 * @author 7040
 */
public class Kontroll {

    //Databasevariabler
    private String databasenavn = "jdbc:mysql://localhost:3306/hobbyhuset";
    private String databasedriver = "com.mysql.cj.jdbc.Driver";
    private Connection forbindelse;

    private ResultSet resultat;
    private Statement utsagn;

    private HashMap<String, Skip> skipene = new HashMap<>();
    public ArrayList<Land> landene = new ArrayList<>();

    /**
     * Henter skipene
     *
     * @return HashMap<String, Skip>
     */
    public HashMap<String, Skip> hentSkipene() {
        return skipene;
    }

    /**
     * Henter landene i et ObservableList<String>
     *
     * @return ObservableList<String>
     */
    public ObservableList<String> hentLandene() {
        ObservableList<String> alleLandene = FXCollections.observableArrayList();
        //for hvert land i landene-arrayen
        for (Land l : landene) {
            alleLandene.add(l.getLandkode());
        }
        return alleLandene;
    }

    /**
     * Lager forbindelse til hobbyhuset-databasen
     *
     * @throws Exception
     */
    public void lagForbindelse() throws Exception {
        try {
            forbindelse = DriverManager.getConnection(databasenavn, "root", "");
            System.out.println("Koblet til");
        } catch (Exception e) {
            throw new Exception("Kan ikke oppnå kontakt med databasen");
        }
    }

    /**
     * Lukker forbindelse til databasen om den eksisterer
     *
     * @throws Exception
     */
    public void lukk() throws Exception {
        try {
            if (forbindelse != null) {
                forbindelse.close();
                //resultat.close();
                //utsagn.close();
            }
        } catch (Exception e) {
            throw new Exception("Kan ikke lukke databaseforbindelse");
        }
    }

    /**
     * Legger inn et skip i skipene-hashmappet
     *
     * @param skip
     */
    public void leggTilSkip(Skip skip) {
        skipene.put(skip.getRegnr(), skip);
    }

    /**
     * Legger inn et land i landene-hashmappet
     *
     * @param land
     */
    public void leggTilLand(Land land) {
        landene.add(land);
    }

    /**
     * Skriver til filene når programmet avsluttes.
     */
    public void avslutt() {
        skrivSkip("skip.txt");
        skrivLand("land.txt");
    }

    /**
     * Finne et skip i skipene hashmappet
     *
     * @param regnr
     * @return
     */
    public Skip finnSkip(String regnr) {
        return skipene.get(regnr);
    }

    /**
     * Finne land ved å søke med landkoden
     *
     * @param landkode
     * @return Land
     */
    public Land finnLandnavn(String landkode) {
        for (int j = 0; j < landene.size(); j++) {
            Land land = landene.get(j);
            if (land.getLandkode().equals(landkode)) {
                return land;
            }
        }
        return null;
    }

    /**
     * Leser kundene fra kunde i hobbyhuset databasen
     *
     * @return ResultSet
     * @throws Exception
     */
    public ResultSet lesKunder() throws Exception {
        ResultSet resultat = null;
        String sql = "SELECT * FROM kunde WHERE 1";
        try {
            Statement utsagn = forbindelse.createStatement();
            resultat = utsagn.executeQuery(sql);
        } catch (Exception e) {
            throw new Exception("Kan ikke åpne databasetabell");
        }
        return resultat;

    }

    /**
     * Skriver skipene til "skip.txt"
     *
     * @param filnavn
     */
    public void skrivSkip(String filnavn) {
        try {
            PrintWriter utfil = Filbehandling.lagSkriveforbindelse(filnavn);
            Iterator<Skip> oppramser = getIterator();
            while (oppramser.hasNext()) {
                //Sjekke hva slags objekt det er her
                Skip skip = oppramser.next();
                utfil.println(skip.toFile());
            }
            utfil.close();
        } catch (Exception e) {
        }

    }

    /**
     * Skriver nye land til "land.txt"
     *
     * @param filnavn
     */
    public void skrivLand(String filnavn) {
        try {
            PrintWriter utfil = Filbehandling.lagSkriveforbindelse(filnavn);
            for (Land land : landene) {
                utfil.println(land.toFile());
            }
            utfil.close();
        } catch (Exception e) {
        }

    }

    /**
     * Leser land fra "land.txt" og oppretter dem i landene-arrayen
     *
     * @param filnavn
     * @throws Exception
     */
    public void lesLand(String filnavn) throws Exception {
        String innlinje;
        StringTokenizer inndata;
        try {
            BufferedReader innfil = Filbehandling.lagLeseforbindelse(filnavn);
            while (innfil.ready()) {
                innlinje = innfil.readLine();
                inndata = new StringTokenizer(innlinje, ",");
                //Lager et cruiseSkip objekt
                String landkode = inndata.nextToken();
                String landsnavn = inndata.nextToken();
                String hovedstad = inndata.nextToken();

                landene.add(new Land(landkode, landsnavn, hovedstad));
                // skipene.put(varenr, new Skip(varenr, varenavn, beholdning));
            }//l�kke
            innfil.close();
        } catch (Exception e) {
            throw new Exception("Kan ikke lese fra fil" + e.toString());
        }
    }

    /**
     * Leser skip fra "skip.txt" og skiller CruiseSkip og ContainerSkip på
     * første ordet i linje Deretter opprettes det riktige skipet i
     * skipene-arrayen
     *
     * @param filnavn
     * @throws Exception
     */
    public void lesSkip(String filnavn) throws Exception {
        String innlinje;
        StringTokenizer inndata;
        skipene.clear();
        try {
            BufferedReader innfil = Filbehandling.lagLeseforbindelse(filnavn);
            while (innfil.ready()) {
                innlinje = innfil.readLine();
                inndata = new StringTokenizer(innlinje, ",");
                String skipType = inndata.nextToken();
                //Hvis første ordet er cruiseship, så opprettes det 
                if (skipType.equals("CruiseSkip")) {
                    String regNr = inndata.nextToken();
                    int lengde = Integer.parseInt(inndata.nextToken());
                    int antallBruttoRegTonn = Integer.parseInt(inndata.nextToken());
                    String statustxt = inndata.nextToken();
                    String landkode = inndata.nextToken();
                    int antalLugarer = Integer.parseInt(inndata.nextToken());
                    Status status = Status.valueOf(statustxt);
                    leggTilSkip(new CruiseSkip(regNr, lengde, antallBruttoRegTonn, landkode, status, antalLugarer));
                } else {
                    //Lager et containerSkip objekt
                    String regNr = inndata.nextToken();
                    int lengde = Integer.parseInt(inndata.nextToken());
                    int antallBruttoRegTonn = Integer.parseInt(inndata.nextToken());
                    String statustxt = inndata.nextToken();
                    String landkode = inndata.nextToken();
                    int antallContainer = Integer.parseInt(inndata.nextToken());
                    Status status = Status.valueOf(statustxt);
                    leggTilSkip(new ContainerSkip(regNr, lengde, antallBruttoRegTonn, landkode, status, antallContainer));
                }

                // skipene.put(varenr, new Skip(varenr, varenavn, beholdning));
            }//l�kke
            innfil.close();
        } catch (Exception e) {
            throw new Exception("Kan ikke lese fra fil" + e.toString());
        }
    }

    /**
     * Henter en iterator
     *
     * @return
     */
    public Iterator<Skip> getIterator() {
        Collection<Skip> verdier = skipene.values();
        return verdier.iterator();
    }

    /**
     * Henter listen "landene" som inneholder alle landene.
     *
     * @return
     */
    public ArrayList<Land> hentLandliste() {
        return landene;
    }
}
