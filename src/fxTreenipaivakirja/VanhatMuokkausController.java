package fxTreenipaivakirja;

import java.util.List;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import treenipaivakirja.Laji;
import treenipaivakirja.Treeni;
import treenipaivakirja.Treenipaikka;
import treenipaivakirja.Treenipaivakirja;
/**
 * Luokka VanhatMuokkausView-ikkunan tapahtumien hoitamiseksi. Hakee tarvittavat tiedot
 * moukattavaksi valitusta treenimerkinnästä käyttäjälle ja muut tiedot muokkausta varten,
 * kuten kaikki lajivaihtoehdot ja treenipaikat listaan. Muokkausten tallennus.
 * 
 * @author Kristian Koronen
 * @version 15.2.2018
 * 
 */
public class VanhatMuokkausController implements ModalControllerInterface<Treeni> {
    @FXML private TextField textPvm;
    @FXML private TextField textKesto;
    @FXML private ComboBoxChooser<Laji> chooserLaji;
    @FXML private ComboBoxChooser<Treenipaikka> chooserPaikka;
    @FXML private TextArea textMuistiinpanot;
    
    @Override
    public void setDefault(Treeni oletus) {
        valittu = oletus;
    }
    
    
    @Override
    public void handleShown() {
        //
    }

    
    @Override
    public Treeni getResult() {
        return valittu;
    }
    

    @FXML void handlePeruuta() { peruuta(); }
    @FXML void handleTallenna() { tallenna(); }
    
    
    //===========================================================================================================================
    private Treeni valittu;
    private Treenipaivakirja paivakirja;
    private boolean onkoVirheitaPvm;
    private boolean onkoVirheitaK;
    private boolean onkoVirheitaM;
    
    /**
     * Täyttää ikkunan tiedot muokkausta varten.
     * @param treenipaivakirja josta kerätään tiedot muokkausta varten
     */
    public void setVaihtoehdot(Treenipaivakirja treenipaivakirja) {
        paivakirja = treenipaivakirja;
        
        textPvm.setText(valittu.anna(1));
        textKesto.setText(valittu.anna(3));
        textMuistiinpanot.setText(valittu.anna(5));
        
        chooserLaji.clear();
        for (Laji laji : paivakirja.annaLajit()) chooserLaji.add(laji.anna(1), laji);
        chooserLaji.setSelectedIndex(haeIndeksiL(paivakirja.annaLajit(), valittu.anna(2)));
        
        chooserPaikka.clear();
        for (Treenipaikka treenipaikka : paivakirja.annaTreenipaikat()) chooserPaikka.add(treenipaikka.anna(1), treenipaikka);
        chooserPaikka.setSelectedIndex(haeIndeksiT(paivakirja.annaTreenipaikat(), valittu.anna(4)));
    }
    
    
    /**
     * Hakee annetusta listasta alkiota vastaavan olion indeksin.
     * @param lista Mistä listasta haetaan alkion indeksiä
     * @param alkio Mitä alkiota haetaan listasta
     * @return Palauttaa löydetyn alkion indeksin tai -1, jos ei löydy
     * @example
     * <pre name="test">
     *   
     * </pre>
     */
    public static int haeIndeksiL(List<Laji> lista, String alkio) {  // TODO: TESTIT!
        int indeksi = 0;
        for (Laji laji : lista) {
            if (alkio.equals(laji.anna(0))) return indeksi;
            indeksi++;
        }        
        return -1;
    }
    
    
    /**
     * Hakee annetusta listasta alkiota vastaavan olion indeksin.
     * @param lista Mistä listasta haetaan alkion indeksiä
     * @param alkio Mitä alkiota haetaan listasta
     * @return Palauttaa löydetyn alkion indeksin tai -1, jos ei löydy
     */
    public static int haeIndeksiT(List<Treenipaikka> lista, String alkio) {
        int indeksi = 0;
        for (Treenipaikka treenipaikka : lista) {
            if (alkio.equals(treenipaikka.anna(0))) return indeksi;
            indeksi++;
        }        
        return -1;
    }
    
    
    /**
     * Tarkistaa päivämäärän oikeellisuuden.
     */
    @FXML void handleTarkistaPvm() {
        if (!textPvm.getText().matches("((0?[1-9])|([1-2][0-9])|(3[0-1]))[.]((1[0-2])|(0?[1-9]))[.]([0-9]{1,4})")) {
            textPvm.setStyle("-fx-background-color: red");
            onkoVirheitaPvm = true;
        }
        else {
            textPvm.setStyle("-fx-background-color: white");
            onkoVirheitaPvm = false;
        }
    }
    
    
    /**
     * Tarkistaa treenin keston oikeellisuuden.
     */
    @FXML void handleTarkistaKesto() {
        if (!textKesto.getText().matches("[0-9]{1,4}")) {
            textKesto.setStyle("-fx-background-color: red");
            onkoVirheitaK = true;
        }
        else {
            textKesto.setStyle("-fx-background-color: white");
            onkoVirheitaK = false;
        }        
    }


    /**
     * Varmistaa, ettei muistiinpanoihin laiteta puolipisteitä, jotta tiedostosta lukeminen toimisi oikein.
     */
    @FXML void handleTarkistaMuistiinpanot() {
        if (textMuistiinpanot.getText().matches(".*[;].*")) {
            textMuistiinpanot.setStyle("-fx-background-color: red");
            onkoVirheitaM = true;
        } 
        else {
            textMuistiinpanot.setStyle("-fx-background-color: white");
            onkoVirheitaM = false;
        }
    }
    
    
    /**
     * Sulkee ikkunan.
     */
    private void peruuta() {
        ModalController.closeStage(chooserLaji);
    }
    
    
    /**
     * Tallenna tietoa.
     */
    private void tallenna() {
        if (onkoVirheitaPvm || onkoVirheitaK || onkoVirheitaM) Dialogs.showMessageDialog("Virheitä syöttötiedoissa. Korjaa virheet ennen tallentamista.");
        else {
            valittu.setPvm(textPvm.getText());
            valittu.setKestoMin(Mjonot.erotaInt(textKesto.getText(), 0));
            valittu.setLaji(Mjonot.erotaInt(paivakirja.etsiLaji(chooserLaji.getSelectedText(), 1, 0), 0));
            valittu.setTreenipaikka(Mjonot.erotaInt(paivakirja.etsiPaikka(chooserPaikka.getSelectedText(), 1, 0), 0));
            valittu.setMuistiinpanoja(textMuistiinpanot.getText());
            paivakirja.muutettu();
            ModalController.closeStage(chooserLaji);            
        }
    }    
    
    
    /**
     * Avaa uuden modaalisen ikkunan treenimerkinnän muokkausta varten.
     * @param modalityStage Mille ollaan modaalisia
     * @param valittu listasta valittu treeni
     * @param treenipaivakirja paivakirja, jonka itetoja käytetäään
     * @return Palauttaa muokatun treenin tai null, jos ei muokattu
     */
    public static Treeni muokkaaTreenia(Stage modalityStage, Treeni valittu, Treenipaivakirja treenipaivakirja) {
        return ModalController.<Treeni, VanhatMuokkausController>showModal(
                VanhatMuokkausController.class.getResource("VanhatMuokkausView.fxml"),
                "Muokkaa treenimerkintää",
                modalityStage, valittu, ctrl -> ctrl.setVaihtoehdot(treenipaivakirja));
    }
    
}

