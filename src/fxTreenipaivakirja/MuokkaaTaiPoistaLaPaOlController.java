package fxTreenipaivakirja;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import treenipaivakirja.Laji;
import treenipaivakirja.Treenipaikka;
import treenipaivakirja.Treenipaivakirja;

/**
 * Luokka MuokkaaTaiPoistaLaPaOl-ikkunan tapahtumien hoitamiseksi. Hakee tarvittavat tiedot
 * ja n‰ytt‰‰ ne k‰ytt‰j‰lle. Lajeille ja treenipaikoille muokkaus ja poisto toteutettu.
 * 
 * Viel‰ tekem‰tt‰ olusuhteille toimminnot.
 * 
 * @author Kristian Koronen
 * @version 15.2.2018
 * 
 */
public class MuokkaaTaiPoistaLaPaOlController implements ModalControllerInterface<Treenipaivakirja>, Initializable {
    @FXML private ListChooser<Laji> chooserLajit;
    @FXML private ListChooser<Treenipaikka> chooserPaikat;
    
    @Override
    public void handleShown() {
        //
    }

    
    @Override
    public Treenipaivakirja getResult() {
        return null;
    }

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

    @FXML void handlePoista() { /* */ }
    @FXML void handlePoistaLaji() { poistaLaji(); }
    @FXML void handlePoistaPaikka() { poistaPaikka(); }
    @FXML void handleMuokkaaLajia() { muokkaaLajia(); }
    @FXML void handleMuokkaaPaikkaa() { muokkaaPaikkaa(); }    
    @FXML void handlePeruuta() { peruuta(); }
    
    
    //===========================================================================================================================
    private Treenipaivakirja muokattava;
    
    
    @Override
    public void setDefault(Treenipaivakirja oletus) {
        muokattava = oletus;
        hae(oletus);
    }
    
    
    /**
     * Hakee lajit ja treenipaikat listaan k‰ytt‰j‰n n‰kyville.
     * @param oletus paivakirja
     */
    public void hae(Treenipaivakirja oletus) {
        chooserLajit.clear();
        for (Laji laji : oletus.annaLajit()) chooserLajit.add(laji.anna(1), laji);
        
        chooserPaikat.clear();
        for (Treenipaikka treenipaikka : oletus.annaTreenipaikat()) chooserPaikat.add(treenipaikka.anna(1), treenipaikka);
    }
    
    
    /**
     * Sulkee ikkunan.
     */
    private void peruuta() {
        ModalController.closeStage(chooserLajit);
    }
    
    
    /**
     * Tallentaa tietoa.
     */
    private void tallenna() {
        try {
            muokattava.tallenna();
        } catch (Exception e) {
            Dialogs.showMessageDialog("Ongelmia tallennuksessa!\n" + e.getMessage());
        }
    }
    
    
    /**
     * Avaa ikkunan, jossa kysyt‰‰n vahvistusta tietojen poistoon ja poistetaan tiedot, jos 
     * valitaan Kyll‰ ja suljetaan ikkuna jos En.
     */
    private void poistaLaji() {
        boolean poistetaanko = Dialogs.showQuestionDialog("Poisto?",
                               "Haluatko varmasti poistaa valitsemasi tiedot?", "Kyll‰", "En");
        if (poistetaanko) 
            if (muokattava.poista(chooserLajit.getSelectedObject()) > 0) tallenna();
        hae(muokattava);
    }
    
    
    /**
     * Poistaa valitun treenipaikan.
     */
    private void poistaPaikka() {
        if (Dialogs.showQuestionDialog("Poisto?", "Haluatko varmasti poistaa valitsemasi tiedot?", "Kyll‰", "En"))
            if (muokattava.poista(chooserPaikat.getSelectedObject()) > 0) tallenna();
        hae(muokattava);
    }
    
    
    /**
     * Avaa ikkunan, jossa voi muokata lajeja, treenipaikkoja tai olosuhteita.
     */
    private void muokkaaLajia() {
        String uudenLajinNimi = Dialogs.showInputDialog("Muokkaa lajin nime‰", chooserLajit.getSelectedObject().anna(1));
        if (!uudenLajinNimi.equals(chooserLajit.getSelectedObject().anna(1))) {
            muokattava.muokkaa(chooserLajit.getSelectedObject(), uudenLajinNimi);
            tallenna();
            hae(muokattava);
        }
    }
    
    
    /**
     * Avaa ikkunan, jossa voi muokata lajeja, treenipaikkoja tai olosuhteita.
     */
    private void muokkaaPaikkaa() {
        String uudenPaikanNimi = Dialogs.showInputDialog("Muokkaa treenipaikan nime‰", chooserPaikat.getSelectedObject().anna(1));
        if (!uudenPaikanNimi.equals(chooserPaikat.getSelectedObject().anna(1))) {
            muokattava.muokkaa(chooserPaikat.getSelectedObject(), uudenPaikanNimi);           
            tallenna();
            hae(muokattava);
        }
    }


    /**
     * Luodaan muokkausdialogi ja palautetaan muokattu p‰iv‰kirja.
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit‰ p‰iv‰kirjaa n‰ytet‰‰n oletuksena
     * @return null jos painetaan Cancel, muuten muokattu p‰iv‰kirja
     */
    public static Treenipaivakirja muokkaaLaPaOl(Stage modalityStage, Treenipaivakirja oletus) {
        return ModalController.showModal(
                TreenipaivakirjaController.class.getResource("MuokkaaTaiPoistaLaPaOlView.fxml"),
                "Kerho",
                modalityStage, oletus);
    }
    
}

