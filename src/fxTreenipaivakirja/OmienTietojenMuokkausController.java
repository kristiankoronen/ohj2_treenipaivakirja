package fxTreenipaivakirja;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * @author Kristian Koronen
 * @version 15.2.2018
 * Luokka OmienTietojenMuokkaus-ikkunan tapahtumien hoitamiseksi
 */
public class OmienTietojenMuokkausController implements ModalControllerInterface<String> {
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
    

    @FXML void handlePeruuta() { peruuta(); }
    @FXML void handleTallenna() { tallenna(); }
    
    
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

