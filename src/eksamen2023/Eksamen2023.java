package eksamen2023;

import eksamen2023.kontroll.Kontroll;
import eksamen2023.model.ContainerSkip;
import eksamen2023.model.CruiseSkip;
import eksamen2023.model.Land;
import eksamen2023.model.Skip;
import eksamen2023.model.Status;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Hovedprogrammet for eksamenprosjektet
 *
 * @author 7040
 */
public class Eksamen2023 extends Application {

    private Scene scene, sceneHoved;
    private Stage stage = new Stage();

    private Button btnCruise, btnContainer;
    private Button btnHovedMeny = new Button("Tilbake til hovedmenyen");
    public Menu meny;
    private TextField txtRegNr, txtAntall, txtLandkode, txtStatus,
            txtAntLugarer, txtAntallContainere, txtLengde,
            txtNyLandkode, txtNylandsnavn, txtHovedstad;
    private Label lblRegNr, lblAntall, lblLandkode,
            lblStatus, lblAntLugarer, lblAntallContainere, lblLengde,
            lblLandskode, lblLandsnavn, lblHovedstad, lblSkipdata;
    private ComboBox cbxStatus, cbxLandkode;
    private MenuBar menylinje = new MenuBar();
    private ObservableList<String[]> landlist = FXCollections.observableArrayList();
    private Kontroll kontroll = new Kontroll();
    private ObservableList<String[]> data = FXCollections.observableArrayList();
    private ObservableList<Skip> alleskip = FXCollections.observableArrayList();
    private HashMap<String, Skip> skipene;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starter stage
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        btnHovedMeny.setOnAction(e -> hovedScene());

        hovedScene();
    }

    /**
     * Hovedmeny for valg av scenene'
     */
    public void hovedScene() {
        BorderPane bp = new BorderPane();
        meny = new Menu("Meny");
        MenuItem menyNyttSkip = new MenuItem("Registrer et skip");
        MenuItem menyRegSkip = new MenuItem("Registrer status for et skip");
        MenuItem menyFinnSkip = new MenuItem("Finn skip");
        MenuItem menyRegLand = new MenuItem("Registrer et land");
        MenuItem menySeSkip = new MenuItem("Se registrerte skip");
        MenuItem menySePassasjerer = new MenuItem("Se passasjerer skip");
        MenuItem menyLesFil = new MenuItem("Les fil");
        MenuItem menyLesPassasjer = new MenuItem("Les passasjerer fra hobbyhuset");
        MenuItem menyAvslutt = new MenuItem("Avslutt");

        //Nedenfor håndteres alle menytrykkene i hovedmenyen
        menyNyttSkip.setOnAction(e -> {
            regSkipScene();
        });
        menyRegSkip.setOnAction(e -> endreSkipStatusScene());
        menyFinnSkip.setOnAction(e -> finnSkipScene());
        menyRegLand.setOnAction(e -> regLandScene());
        menySeSkip.setOnAction(e -> {
            try {
                seSkipScene();
            } catch (Exception ex) {
                Logger.getLogger(Eksamen2023.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menySePassasjerer.setOnAction(e -> {
            try {

                passasjerListe();
            } catch (Exception ex) {
                Logger.getLogger(Eksamen2023.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menyLesFil.setOnAction(e -> {
            try {
                kontroll.lesLand("land.txt");
                kontroll.lesSkip("skip.txt");
                if (alleskip.isEmpty()) {
                    skipene = kontroll.hentSkipene();
                    for (Skip skip : skipene.values()) {
                        alleskip.add(skip);
                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(Eksamen2023.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menyLesPassasjer.setOnAction(e -> {
            try {
                kontroll.lagForbindelse();
                hentKunder();
            } catch (Exception ex) {
                Logger.getLogger(Eksamen2023.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menyAvslutt.setOnAction(e -> avslutt());

        meny.getItems().addAll(menyNyttSkip, menyRegSkip, menyFinnSkip,
                menyRegLand, menySeSkip, menySePassasjerer, menyLesFil, menyLesPassasjer, menyAvslutt);
        //Sjekker om menylinjen finnes fra før av, hvis ja så gjør systemet ingenting
        if (menylinje.getMenus().isEmpty()) {
            //Legger menyen til menylinjen:
            menylinje.getMenus().addAll(meny);
        }

        bp.setTop(menylinje);

        stage.setTitle("Hovedmeny");
        scene = new Scene(bp, 300, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Dette er scene som inneholder en tabell med alle passasjerene
     *
     * @throws Exception
     */
    public void passasjerListe() throws Exception {
        //Henter passasjerlista i kunder.hobbyhuset

        BorderPane bp = new BorderPane();

        TableView<String[]> tableView = new TableView<>();

        TableColumn<String[], String> knrColumn = new TableColumn<>("Knr");
        knrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> fornavnColumn = new TableColumn<>("Fornavn");
        fornavnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> etternavnColumn = new TableColumn<>("Etternavn");
        etternavnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> adresseColumn = new TableColumn<>("Adresse");
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> postnrColumn = new TableColumn<>("Postnr");
        postnrColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        TableColumn<String[], String> kjonnColumn = new TableColumn<>("Kjønn");
        kjonnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

        tableView.getColumns().addAll(knrColumn, fornavnColumn, etternavnColumn, adresseColumn, postnrColumn, kjonnColumn);
        tableView.setItems(data);

        bp.setCenter(tableView);
        bp.setBottom(btnHovedMeny);
        sceneHoved = new Scene(bp);
        stage.setScene(sceneHoved);
        stage.show();
        stage.setTitle("Kunden");
    }

    /**
     * Henter kundene i kunder.hobbyhuset med kontroll.leskunder og legger det i
     * en ResultSet, så legger systemet det i et String array
     *
     * @throws Exception
     */
    public void hentKunder() throws Exception {
        data.clear();
        try {
            ResultSet resultat = kontroll.lesKunder();
            while (resultat.next()) {
                String knr = resultat.getString("knr");
                String fornavn = resultat.getString("fornavn");
                String etternavn = resultat.getString("etternavn");
                String adresse = resultat.getString("adresse");
                String postnr = resultat.getString("postnr");
                String kjonn = resultat.getString("kjønn");

                String[] rowData = {knr, fornavn, etternavn, adresse, postnr, kjonn};
                data.add(rowData);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Registrere et skip(ContainterSkip / CruiseSkip) ComboBox for valg av
     * status blir fylt av enumklassen : Status ComboBox for valg av landkode
     * blir fylt av arrayen landene i kontroll.
     *
     */
    public void regSkipScene() {
        FlowPane flowpane = new FlowPane();
        flowpane.setHgap(40);
        flowpane.setOrientation(Orientation.VERTICAL);

        //KNAPPER
        btnCruise = new Button("Registrer ny cruiseskip!");
        btnContainer = new Button("Registrer ny containerskip!");
        //LABELS
        lblRegNr = new Label("Skriv inn registreringsnummer");
        lblLengde = new Label("Skriv inn lengde på båten");
        lblAntall = new Label("Skriv inn bruttoregistreringstonn");
        lblLandkode = new Label("Velg landkode på skipet: ");
        cbxLandkode = new ComboBox();

        cbxLandkode.setItems(kontroll.hentLandene());
        lblStatus = new Label("Velg status");
        lblAntLugarer = new Label("Skriv inn antall lugarer");
        lblAntallContainere = new Label("Skriv inn antall containere");
        //TEXTFIELDS
        txtRegNr = new TextField("");
        txtAntall = new TextField("");
        txtLengde = new TextField("");
        txtLandkode = new TextField("");
        cbxStatus = new ComboBox();
        cbxStatus.getItems().setAll(Status.values());
        txtAntLugarer = new TextField("");
        txtAntallContainere = new TextField("");

        flowpane.getChildren().addAll(lblRegNr, txtRegNr, lblAntall,
                txtAntall, lblLandkode, cbxLandkode, lblStatus,
                cbxStatus, lblAntLugarer, txtAntLugarer,
                lblLengde, txtLengde,
                lblAntallContainere, txtAntallContainere,
                btnCruise, btnContainer, btnHovedMeny);

        //Når det trykkes på knappen for å legge til et cruiseskip
        btnCruise.setOnAction(e -> {
            //Henter den som er valgt i comboboxene
            String valgt = cbxStatus.getSelectionModel().getSelectedItem().toString();
            String land = cbxLandkode.getSelectionModel().getSelectedItem().toString();

            CruiseSkip cruiseSkip = new CruiseSkip(
                    txtRegNr.getText(),
                    Integer.parseInt(txtLengde.getText()),
                    Integer.parseInt(txtAntall.getText()),
                    land,
                    Status.valueOf(valgt),
                    Integer.parseInt(txtAntLugarer.getText()));
            kontroll.leggTilSkip(cruiseSkip);
            tøm();
        });
        //Når det trykkes på knappen for å legge til et cruiseskip
        btnContainer.setOnAction(e -> {
            String valgt = cbxStatus.getSelectionModel().getSelectedItem().toString();
            ContainerSkip containerSkip = new ContainerSkip(
                    txtRegNr.getText(),
                    Integer.parseInt(txtLengde.getText()),
                    Integer.parseInt(txtAntall.getText()),
                    txtLandkode.getText(),
                    Status.valueOf(valgt),
                    Integer.parseInt(txtAntallContainere.getText()));
            kontroll.leggTilSkip(containerSkip);
            //Tømmer inputfeltene/textfieldene
            tøm();
        });
        stage.setTitle("Registrer skip");

        scene = new Scene(flowpane, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Registrering av et nytt land
     */
    public void regLandScene() {

        VBox vbox = new VBox();
        Button btnNyttLand = new Button("Legg til land");
        txtNyLandkode = new TextField("");
        txtNylandsnavn = new TextField("");
        txtHovedstad = new TextField("");

        lblLandskode = new Label("Skriv inn landkoden");
        lblLandsnavn = new Label("Skriv inn navnet på landet");
        lblHovedstad = new Label("Skriv inn hovedstaden for dette landet");

        HBox hbxLandkode = new HBox();
        hbxLandkode.getChildren().addAll(lblLandskode, txtNyLandkode);
        HBox hbxLandsnavn = new HBox();
        hbxLandsnavn.getChildren().addAll(lblLandsnavn, txtNylandsnavn);
        HBox hbxHovedstad = new HBox();
        hbxHovedstad.getChildren().addAll(lblHovedstad, txtHovedstad);
        vbox.getChildren().addAll(hbxLandkode, hbxLandsnavn, hbxHovedstad, btnNyttLand, btnHovedMeny);

        btnNyttLand.setOnAction(e -> {
            Land nyLand = new Land(txtNyLandkode.getText(),
                    txtNylandsnavn.getText(), txtHovedstad.getText());
            kontroll.leggTilLand(nyLand);
        });
        scene = new Scene(vbox, 400, 200);
        stage.setTitle("Registrer land");

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Finner skipet som er inputtet i txtRegNr, da søkes det i HashMappet som
     * inneholder alle skipene.
     *
     */
    public void finnSkipScene() {
        VBox vbox = new VBox();
        HBox finnskip = new HBox();
        HBox skipdata = new HBox();
        lblRegNr = new Label("Finn et skip: ");
        txtRegNr = new TextField("Registreringsnummer");
        lblSkipdata = new Label("");
        skipdata.getChildren().add(lblSkipdata);
        Button btnFinn = new Button("Finn skip");
        finnskip.getChildren().addAll(lblRegNr, txtRegNr, btnFinn, btnHovedMeny, skipdata);
        vbox.getChildren().addAll(finnskip, skipdata);
        scene = new Scene(vbox, 600, 200);
        btnFinn.setOnAction(e -> {
            Skip s = kontroll.finnSkip(txtRegNr.getText());
            lblSkipdata.setText(s.toString());
        });

        stage.setTitle("Finn skip");

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Scene som inneholder inneholder tabellen for å se alle skipene i et
     * tabell
     *
     * @throws Exception
     */
    public void seSkipScene() throws Exception {

        BorderPane bp = new BorderPane();

        TableView<Skip> skiptabell = new TableView<>();
        //Oppretting av kolonnene i tabellen
        TableColumn<Skip, String> regnKolonne = new TableColumn<>("Regnr");
        regnKolonne.setCellValueFactory(new PropertyValueFactory<>("regnr"));

        TableColumn<Skip, Integer> lengdeKolonne = new TableColumn<>("Lengde");
        lengdeKolonne.setCellValueFactory(new PropertyValueFactory<>("lengde"));

        TableColumn<Skip, Integer> bruttoRegTonnKolonne = new TableColumn<>("Antall brutto reg tonn");
        bruttoRegTonnKolonne.setCellValueFactory(new PropertyValueFactory<>("antallBruttoRegTonn"));

        TableColumn<Skip, String> landkodeKolonne = new TableColumn<>("Landkode");
        landkodeKolonne.setCellValueFactory(new PropertyValueFactory<>("landkode"));

        TableColumn<Skip, Status> statusKolonne = new TableColumn<>("Status");
        statusKolonne.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn antalLugarerKolonne = new TableColumn<Skip, Integer>("Antall lugarer");
        antalLugarerKolonne.setCellValueFactory(new PropertyValueFactory<>("antalLugarer"));

        TableColumn antallContainereKolonne = new TableColumn<Skip, Integer>("Antall containere");
        antallContainereKolonne.setCellValueFactory(new PropertyValueFactory<>("antallContainere"));

        //Legger inn alle kolonnene i tabellen
        skiptabell.getColumns().addAll(regnKolonne, lengdeKolonne, bruttoRegTonnKolonne, landkodeKolonne, statusKolonne, antalLugarerKolonne, antallContainereKolonne);
        //Deretter dataen tilhørende kolonnene
        skiptabell.setItems(alleskip);
        //Legger tablellen, og hovedmeny-knapp inn i BorderPane
        bp.setCenter(skiptabell);
        bp.setBottom(btnHovedMeny);

        scene = new Scene(bp, 700,400);
        stage.setTitle("Se skiptabellen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Endrer statusen på et skip lagret i skipene, det blir endret når
     * programmet avsluttes
     */
    public void endreSkipStatusScene() {
        HBox endreStatus = new HBox();

        lblRegNr = new Label("Endre status: ");
        txtRegNr = new TextField("Registreringsnummer");
        Button btnFinn = new Button("Finn skip");
        cbxStatus = new ComboBox();
        cbxStatus.getItems().setAll(Status.values());

        //Legges inn i hovedlayouten for denne scenen
        endreStatus.getChildren().addAll(lblRegNr, txtRegNr, cbxStatus, btnFinn, btnHovedMeny);

        scene = new Scene(endreStatus, 500, 200);
        //Når det trykkes på knappen for å endre på skipstatus
        btnFinn.setOnAction(e -> {
            String valgt = cbxStatus.getSelectionModel().getSelectedItem().toString();
            Skip skip = kontroll.finnSkip(txtRegNr.getText());
            Status s = Status.valueOf(valgt);
            skip.setStatus(s);
            kontroll.avslutt();
        });
        stage.setScene(scene);
        stage.setTitle("Endre skipstatus");

        stage.show();

    }

    /**
     * Tømmer tekstfeld i i Skip registrering scenen
     */
    public void tøm() {
        txtRegNr.setText("");
        txtAntall.setText("");
        txtLengde.setText("");
        txtLandkode.setText("");
        txtAntLugarer.setText("");
        txtAntallContainere.setText("");
    }

    /**
     * Når programmet blir avsluttet, så kjører vi avslutt funksjonen i kontroll
     * klassen
     */
    private void avslutt() {
        kontroll.avslutt();
        System.exit(0);
    }

}
