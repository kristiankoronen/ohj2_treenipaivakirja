package fxTreenipaivakirja;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import treenipaivakirja.Laji;
import treenipaivakirja.SailoException;
import treenipaivakirja.Treeni;
import treenipaivakirja.Treenipaikka;
import treenipaivakirja.Treenipaivakirja;

/**
 * Luokka käsittelee ohjelman pääikkunan toiminnot:
 *     - treenimerkinnän lisäys, muokkaus ja poisto
 *     - lajien lisäys, muokkaus ja poisto
 *     - treenipaikkojen. lisäys, muokkaus ja poisto
 *     - treenimerkintöjen lajittelun aina päivämäärän mukaan uusimmasta vanhimpaan
 *     - treenimerkintöjen haku joko lajin tai treenipaikan mukaan
 * 
 * Vielä tekemättä:
 *     - tavoitteiden lisäys, muokkaus, poisto ja näyttö stringgridissä
 *     - olosuhteiden lisäys, muokkaus ja poisto
 *     - käyttäjän omien tietojen käsittely
 *     
 * @author Kristian Koronen
 * @version 15.2.2018
 * 
 */
public class TreenipaivakirjaController implements Initializable {
    @FXML private ListChooser<Treeni> chooserTreenit;
    @FXML private TextArea areaTreeni;
    @FXML private Label textPvm;
    @FXML private Label textKesto;
    @FXML private Label textLaji;
    @FXML private Label textPaikka;
    @FXML private StringGrid<Laji> stringGridTavoitteet;
    @FXML private TextField syotaPvm;
    @FXML private TextField syotaKestoMin;
    @FXML private ComboBoxChooser<Treenipaikka> valitsePaikka;
    @FXML private ComboBoxChooser<Laji> valitseLaji;
    @FXML private ComboBoxChooser<String> chooserEhto;
    @FXML private TextArea syotaMuistiinpanot;
    @FXML private TextField textHaku;

    @FXML void handleHaku() { 
        if ("laji".equals(chooserEhto.getSelectedObject())) hakuLajeilla();
        if ("treenipaikka".equals(chooserEhto.getSelectedObject())) hakuTreenipaikoilla();
    }
    @FXML void handleTallenna() { tallenna(); }
    @FXML void handleLisaaTavoite() { lisaaTavoite(); }    
    @FXML void handleAlusta() { alusta(); }  
    @FXML void handleMuokkaaOmiaTietoja() { muokkaaOmiaTietoja(); }   
    @FXML void handleMuokkaaTavoitteita() { muokkaaTavoitteita(); }
    @FXML void handleMuokkaaVanhoja() { muokkaaVanhoja(); } 
    @FXML void HandleKayttoohjeet() { kayttoohjeet(); }
    @FXML void handlePoista() { poista(); }
    @FXML void handleLisaaUusiLaji() { lisaaUusiLaji(); }
    @FXML void handleLisaaUusiPaikka() { lisaaUusiPaikka(); }
    @FXML void handleUusiTreeni() { uusiTreeni(); } 
    @FXML void handleMuokkaaTaiPoistaLaPaOl() { muokkaaTaiPoistaLaPaOl(); }
    @FXML void handleSulje() {
        tallenna();
        Platform.exit();
    }

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) { alustaTreenipaivakirja(); }
    
    


    //======================================================================================================================================
    private Treenipaivakirja treenipaivakirja;
    private boolean onkoVirheitaPvm = true;
    private boolean onkoVirheitaK = true;
    private boolean onkoVirheitaM = true;

    
    
    /**
     * @param paivakirja Olio, jonka lista lajeista halutaan.
     * @return Palauttaa listan lajeista.
     */
    public static List<Laji> annaLajit(Treenipaivakirja paivakirja) {
        return paivakirja.annaLajit();
    }
    
    
    /**
     * @return Palauttaa listan lajeista.
     */
    public List<Laji> annaLajit() {
        return annaLajit(treenipaivakirja);
    }
    
    
    /**
     * @return Palauttaa listan treenipaikoista.
     */
    public List<Treenipaikka> annaPaikat() {
        return treenipaivakirja.annaTreenipaikat();
    }
    
    
    /**
     * Näyttää väliaikaisesti treenimerkinnän tiedot muistiinpanoille varatussa tekstikentässä.
     */
    private void naytaTreeni() {
        Treeni treeniKohdalla = chooserTreenit.getSelectedObject();
        if (treeniKohdalla == null) return;
        
        areaTreeni.setText("");
        textPvm.setText(treeniKohdalla.anna(1));
        textLaji.setText(treenipaivakirja.etsiLaji(treeniKohdalla.anna(2), 0, 1));
        textKesto.setText(String.format("%3s %3s", treeniKohdalla.anna(3), "min"));
        textPaikka.setText(treenipaivakirja.etsiPaikka(treeniKohdalla.anna(4), 0, 1));
        try (PrintStream osMuistiinpanot = TextAreaOutputStream.getTextPrintStream(areaTreeni)) {
            treeniKohdalla.tulosta(osMuistiinpanot);
        }
    }
    
    
    /**
     * Tyhjentää listan vanhoista merkinnöistä ja tyhjentää muistiinpanoja varten varatun tekstikentän.
     * Muokataan, kun edetään harjoitustyössä. Nämä toiminnot lähinnä vaihetta 5 varten.
     */
    private void alustaTreenipaivakirja() {
        areaTreeni.setText("");
        textPvm.setText("");
        textLaji.setText("");
        textKesto.setText("");
        textPaikka.setText("");
        areaTreeni.setFont(new Font("Courier new", 12));
        chooserTreenit.clear();
        chooserTreenit.addSelectionListener(e -> naytaTreeni());
    }
    
    
    /**
     * Lajittelee treenit päivämäärän mukaan.
     * @param treenit Lajiteltava taulukko
     */
    public static void lajittele(Treeni[] treenit) {
        if (treenit != null) Arrays.sort(treenit);
    }
    
    
    /**
     * Hakee listaan tietyn lajin treenimerkintöjä.
     */
    private void hakuLajeilla() {
        chooserTreenit.clear();
        Treeni[] treenit = treenipaivakirja.annaTreenit();
        lajittele(treenit);
        int index = 0;
        
        for (int i = 0; i < treenipaivakirja.getLkm(); i++) {
            if (treenipaivakirja.etsiLaji(treenit[i].anna(2), 0, 1).startsWith(textHaku.getText())) 
                chooserTreenit.add(treenit[i].getPvm(), treenit[i]);
            index = i;
        }
        chooserTreenit.getSelectionModel().select(index);
    }
    
    
    /**
     * Hakee listaan tietyn treenipaikan treenimerkintöjä.
     */
    private void hakuTreenipaikoilla() {
        chooserTreenit.clear();
        Treeni[] treenit = treenipaivakirja.annaTreenit();
        lajittele(treenit);
        int index = 0;
        
        for (int i = 0; i < treenipaivakirja.getLkm(); i++) {
            if (treenipaivakirja.etsiPaikka(treenit[i].anna(4), 0, 1).startsWith(textHaku.getText())) 
                chooserTreenit.add(treenit[i].getPvm(), treenit[i]);
            index = i;
        }
        chooserTreenit.getSelectionModel().select(index);
    }
    
    
    /**
     * Hakee treenipäiväkirjan kaikki treenit listaan.
     */
    private void hae() {
        chooserTreenit.clear();
        Treeni[] treenit = treenipaivakirja.annaTreenit();
        lajittele(treenit);
        int index = 0;
        
        for (int i = 0; i < treenipaivakirja.getLkm(); i++) {            
            chooserTreenit.add(treenit[i].getPvm(), treenit[i]);
            index = i;
        }
        
        chooserTreenit.getSelectionModel().select(index);
        
        valitseLaji.clear();
        for (Laji laji : treenipaivakirja.annaLajit())
            valitseLaji.add(laji.anna(1), laji);
        
        valitsePaikka.clear();
        for (Treenipaikka treenipaikka : treenipaivakirja.annaTreenipaikat())
            valitsePaikka.add(treenipaikka.anna(1), treenipaikka);
        
        chooserEhto.clear();
        chooserEhto.add("laji");
        chooserEhto.add("treenipaikka");
        
    }
    
    
    /**
     * Lisää uuden treenimerkinnän päiväkirjaan ja tallentaa merkinnän tiedostoon.     
     */
    private void uusiTreeni() {
        if (onkoVirheitaPvm || onkoVirheitaK || onkoVirheitaM) Dialogs.showMessageDialog("Virheitä syöttötiedoissa. Korjaa virheet ennen tallentamista.");
        else {
            Treeni treeni = new Treeni();
            treeni.rekisteroi();
            treeni.setPvm(syotaPvm.getText());
            treeni.setKestoMin(Mjonot.erotaInt(syotaKestoMin.getText(), 0));
            treeni.setLaji(Mjonot.erotaInt(treenipaivakirja.etsiLaji(valitseLaji.getSelectedText(), 1, 0), 0));
            treeni.setTreenipaikka(Mjonot.erotaInt(treenipaivakirja.etsiPaikka(valitsePaikka.getSelectedText(), 1, 0), 0));
            treeni.setMuistiinpanoja(syotaMuistiinpanot.getText());
            treenipaivakirja.lisaa(treeni);
            tallenna();
            nollaa();
        }
    }
    
    
    /**
     * Tyhjentää kentät.
     */
    private void nollaa() {
        syotaPvm.setText("");
        syotaKestoMin.setText("");
        syotaMuistiinpanot.setText("");
        valitseLaji.setSelectedIndex(-1);
        valitsePaikka.setSelectedIndex(-1);
    }
    
    
    /**
     * Asettaa Treenipäiväkirja-olion, jota käyttöliittymä käyttää.
     * @param paivakirja Päiväkirja, jonka kanssa käyttäliittymä keskustelee.
     */
    public void setTreenipaivakirja(Treenipaivakirja paivakirja) {
        treenipaivakirja = paivakirja;
        hae();
    }
    
    
    /**
     * Avataan tiedosto, jos ei ole luodaan uusi.
     * @return Palauttaa true, jos tiedoston avaaminen onnistui, false, jos taas ei.
     */
    public boolean avaa() {
        // TODO : lueTiedosto();
        return true;
    }
    
    
    /**
     * Tarkistaa voiko ohjelman sulkea.
     * @return Palauttaa true tai false sen mukaan saako ohjelman sulkea vai ei.
     */
    public boolean voikoSulkea() { // TODO: Tee tarkistukset, joiden mukaan palautetaan true tai false.
        tallenna();
        return true;
    }
    
    
    /**
     * Avaa ikkunan, jossa käyttäjä voi muokata tai poistaa lajeja, treenipaikkoja tai olosuhteita.
     */
    private void muokkaaTaiPoistaLaPaOl() {
        Treenipaivakirja muokattu = MuokkaaTaiPoistaLaPaOlController.muokkaaLaPaOl(null, treenipaivakirja);
        if (muokattu != null) {
            treenipaivakirja = muokattu;
        }
        tallenna();
        hae();
    }    
    
    
    /**
     * Avaa dialogin uuden lajin lisäämiseksi.
     */
    private void lisaaUusiLaji() {
        String uudenLajinNimi = Dialogs.showInputDialog("Anna uuden lajin nimi", "");
        if (!uudenLajinNimi.equals("")) {
            Laji uusiLaji = new Laji();
            uusiLaji.rekisteroi();
            uusiLaji.setNimi(uudenLajinNimi);
            treenipaivakirja.lisaa(uusiLaji);
            tallenna();
            hae();
        }
    }
    
    
    /**
     * Avaa dialogin uuden treenipaikan lisäämiseksi.
     */
    private void lisaaUusiPaikka() {
        String uudenPaikanNimi = Dialogs.showInputDialog("Anna uuden treenipaikan nimi", "");
        if (!uudenPaikanNimi.equals("")) {
            Treenipaikka uusi = new Treenipaikka();
            uusi.rekisteroi();
            uusi.setNimi(uudenPaikanNimi);
            treenipaivakirja.lisaa(uusi);
            tallenna();
            hae();
        }
    }
    
    
    /**
     * Avaa uuden ikkunan, jossa käyttäjä voi lisätä uuden tavoitteen itselleen.
     */
    private void lisaaTavoite() { // TODO: Tee jos jaksat tulevaisuudessa.
        //ModalController.showModal(TreenipaivakirjaController.class.getResource("TavoitteidenMuokkausView.fxml"),
        //        "Lisää Tavoite", null, "");
        Laji tavoite = new Laji();
        tavoite.rekisteroi();
        tavoite.taytaLajinTiedot();
        treenipaivakirja.lisaa(tavoite);
        haeTavoitteet();
    }
    
    
    /**
     * Hakee tavoitteet StringGridiin käyttäjälle.
     */
    private void haeTavoitteet() {   // TODO: Lajien tavoitteet stringGridiin.
        stringGridTavoitteet.clear();
        
        stringGridTavoitteet.initTable("Laji","saavutettu h/vko","tavoite h/vko","saavutettu h/kk","tavoite h/kk");
        // stringGridTavoitteet.setOnCellString( (g, Laji, defValue, r, c) -> treenipaivakirja.anna(r) );
        List<Laji> lajit = treenipaivakirja.annaLajit();
        for (Laji laj : lajit) {
            String[] rivi = new String[5];
            for (int i = 0, k = 1; i < 3; i++,k++) rivi[i] = laj.anna(k);
            stringGridTavoitteet.add(laj,rivi);
        }  
    }

    
    /**
     * Avaa ikkunan, jossa käyttäjä voi muokata haluamaansa tavoitetta.
     */
    private void muokkaaTavoitteita() { // TODO: Tavoitteiden muokkaus.
        ModalController.showModal(TreenipaivakirjaController.class.getResource("TavoitteidenMuokkausView.fxml"),
                "Muokkaa tavoitetta", null, "");
    }
    
    
    /**
     * Pyyhkii käyttäjän tekemät merkinnät Uusi merkintä-ikkunasta.
     */
    private void alusta() {
        boolean alustako = Dialogs.showQuestionDialog("Alusta?",
                           "Haluatko varmasti poistaa tämänhetkiset merkintäsi ja aloittaa alusta?", "Kyllä", "En");
        if (alustako) nollaa();
    }
    
    
    /**
     * Avaa ikkunan, jossa käyttäjä voi muokata omia tietojaan.
     */
    private void muokkaaOmiaTietoja() { // TODO: Tee jos jaksat joskus.
        ModalController.showModal(TreenipaivakirjaController.class.getResource("OmienTietojenMuokkausView.fxml"),
                "Muokkaa tietojasi", null, "");
    }
    
    
    
    
    /**
     * Avaa ikkunan, jossa käyttäjä voi muokata valitsemaansa treenimerkintää
     */
    private void muokkaaVanhoja() {
        Treeni treeniKohdalla = chooserTreenit.getSelectedObject();
        if (!(treeniKohdalla == null)) VanhatMuokkausController.muokkaaTreenia(null, treeniKohdalla, treenipaivakirja);
        tallenna();
        hae();

    }
    
    
    /**
     * Vahvistaa valitun tiedon poiston
     */
    private void poista() {
        boolean poistetaanko = Dialogs.showQuestionDialog("Poisto?",
                               "Haluatko varmasti poistaa valitsemasi tiedot?", "Kyllä", "En");
        if (poistetaanko) treenipaivakirja.poista(chooserTreenit.getSelectedObject());
        tallenna();
        hae();
    }
    
    
    /**
     * Tallentaa tietoa.
     */
    private void tallenna() {
        try {
            treenipaivakirja.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia tallennuksessa!\n" + e.getMessage());
        }
        hae();
    }
    
    
    /**
     * Tarkistaa päivämäärän oikeellisuuden.
     */
    @FXML void handleTarkistaPvm() {
        if (!syotaPvm.getText().matches("((0?[1-9])|([1-2][0-9])|(3[0-1]))[.]((1[0-2])|(0?[1-9]))[.]([0-9]{1,4})")) {
            syotaPvm.setStyle("-fx-background-color: red");
            onkoVirheitaPvm = true;
        }
        else {
            syotaPvm.setStyle("-fx-background-color: white");
            onkoVirheitaPvm = false;
        }
    }
    
    
    /**
     * Tarkistaa treenin keston oikeellisuuden.
     */
    @FXML void handleTarkistaKesto() {
        if (!syotaKestoMin.getText().matches("[0-9]{1,4}")) {
            syotaKestoMin.setStyle("-fx-background-color: red");
            onkoVirheitaK = true;
        }
        else {
            syotaKestoMin.setStyle("-fx-background-color: white");
            onkoVirheitaK = false;
        }        
    }


    /**
     * Varmistaa, ettei muistiinpanoihin laiteta puolipisteitä, jotta tiedostosta lukeminen toimisi oikein.
     */
    @FXML void handleTarkistaMuistiinpanot() {
        if (syotaMuistiinpanot.getText().matches(".*[;].*")) {
            syotaMuistiinpanot.setStyle("-fx-background-color: red");
            onkoVirheitaM = true;
        } 
        else {
            syotaMuistiinpanot.setStyle("-fx-background-color: white");
            onkoVirheitaM = false;
        }
    }
    
    
    /**
     * Avaa käyttäjälle käyttöohjeet ohjelman käyttöön selaimessa
     */
    private void kayttoohjeet() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2018k/ht/krkorone");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            Dialogs.showMessageDialog("Jotain vikaa!\n" + e.getMessage());
        } catch (IOException e) {
            Dialogs.showMessageDialog("Jotain vikaa!\n" + e.getMessage());
        }
    }
             
}