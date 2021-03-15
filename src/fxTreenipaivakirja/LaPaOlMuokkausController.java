package fxTreenipaivakirja;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * @author Kristian Koronen
 * @version 15.2.2018
 * Luokka LaPaOlMuokkaus-ikkunan tapahtumien hoitamiseksi
 */
public class LaPaOlMuokkausController implements ModalControllerInterface<String> {
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
    

    @FXML void handleTallenna() { tallenna(); }
    @FXML void handlePeruuta() { peruuta(); }
    
    
    //===========================================================================================================================
    
    
    /**
     * Sulkee ikkunan
     */
    private void peruuta() {
        Dialogs.showMessageDialog("Palataan p‰‰ikkunaan! Mutta ei toimi viel‰.");
    }
    
    
    /**
     * Tallentaa tietoa
     */
    private void tallenna() {
        Dialogs.showMessageDialog("Tallennetaan! Mutta ei toimi viel‰.");
    }
    
}

