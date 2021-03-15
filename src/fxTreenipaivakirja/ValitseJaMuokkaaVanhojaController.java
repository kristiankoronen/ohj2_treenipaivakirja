package fxTreenipaivakirja;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * @author Kristian Koronen
 * @version 15.2.2018
 * Luokka ValitseJaMuokkaaVanhoja-ikkunan tapahtumien hoitamiseksi
 */
public class ValitseJaMuokkaaVanhojaController implements ModalControllerInterface<String> {
    /**
     * @param oletus TODO ymm‰rr‰
     */
    @Override
    public void setDefault(String oletus) {
        //
    }
    
    
    @Override
    public void handleShown() {
        //
    }

    
    @Override
    public String getResult() {
        return "";
    }
    

    @FXML void handlePoista() { poista(); }
    @FXML void handleMuokkaa() { muokkaa(); }
    
    
    //===========================================================================================================================
    
    
    private void poista() {
        Dialogs.showQuestionDialog("Poisto?",
                "Haluatko varmasti poistaa valitsemasi tiedot?", "Kyll‰", "En");
    }
    
    
    private void muokkaa() {
        ModalController.showModal(TreenipaivakirjaController.class.getResource("VanhatMuokkausView.fxml"),
                "Muokkaa merkint‰‰", null, "");
    }
}

