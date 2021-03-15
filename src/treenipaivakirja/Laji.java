package treenipaivakirja;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Laji-luokka, joka
 *     - osaa asettaa itselleen tietoja,
 *     - osaa kertoa itsest‰‰n pyydettyj‰ tietoja,
 *     - osaa esitt‰‰ itsens‰ String-muodossa
 *     - ja osaa asettaa itselleen tiedot parsimalla luetusta tiedoston rivist‰.
 * Viel‰ tekem‰tt‰:
 *     - Tavoitteiden lis‰ys, muokkaus ja poisto.
 * 
 * @author Kristian Koronen
 * @version 21.3.2018
 *
 */
public class Laji {
    private int    id              = 0;
    private String laji            = "";
    private int    viikkoTavoite   = 0;
    private int    kuukausiTavoite = 0;

    /** Staattinen muuttuja luokan Laji olioille id numeron rekisterˆinti‰ varten */
    private static int seuraavaId = 1;
    
    
    /**
     * T‰ytt‰‰ lajin tiedot automaattisesti. Valiaikainen ratkaisu, jotta 
     * voidaan nahda nopeasti luokan oikeanlainen toiminta.
     */
    public void taytaLajinTiedot() {
        laji = "Futis" + String.format("%03d", Treeni.rand(1,99));
        kuukausiTavoite = Treeni.rand(1,60);
        viikkoTavoite = Treeni.rand(1,20);
        
    }
    
    
    /**
     * Tulostaa lajin tiedot parametrina annettuun tietovirtaan.
     * @param out Tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.printf("%03d\n%10s Viikkotavoite: %2d h KuukausiTavoite: %2d h\n", id, laji, viikkoTavoite, kuukausiTavoite);   
    }
    
    

     /**
     * Rekisteroi olion antamalla sille id-numeron ja samalla nostaa Laji-olioiden yhteista
     * staattista muuttujaa seuraavaId yhdella.
     * @example
     * <pre name="test">
     *   Laji laji1 = new Laji();
     *   laji1.getId() === 0;
     *   laji1.rekisteroi();
     *   Laji laji2 = new Laji();
     *   laji2.rekisteroi();
     *   int idNumero1 = laji1.getId();
     *   int idNumero2 = laji2.getId();
     *   idNumero1 = idNumero2 - 1;
     *   laji1.rekisteroi();
     *   laji1.getId() === idNumero1;
     * </pre>
     */
    public void rekisteroi() {     
        if (getId() != 0) return;   
        id = seuraavaId;           
        seuraavaId++;
    }
    
    
    /**
     * Antaa lajin id-numeron.
     * @return Palauttaa lajin id-numeron.
     */
    public int getId() {
        return id;
    }

    
    /**
     * Lis‰‰ lajille viikkotavoitteen.
     * @param h Tavoite tunteina
     */
    public void lisaaVkoTavoite(int h) { // TODO: Tee testit!
        if (0 <= h && h <= 168) {
            viikkoTavoite = h;
            kuukausiTavoite = h;
        }
    }
    
    
    /**
     * Lis‰‰ lajille kuukausitavoitteen.
     * @param h tavoite tunteina
     */
    public void lisaaKkTavoite(int h) {
        if (viikkoTavoite <= h && h <= 5208) kuukausiTavoite = h;  // TODO: Tee testit!
    }
    
    
    /**
     * Antaa k:n kent‰n sis‰llˆn merkkijonona.
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + id;
        case 1: return "" + laji;
        case 2: return "" + viikkoTavoite;
        case 3: return "" + kuukausiTavoite;
        default: return "";                  
        }
    }
    
    
    /**
     * Selvit‰‰ treenin tiedot | erotellusta merkkijonosta
     * Pit‰‰ huolen ett‰ seuraavaId on suurempi kuin tuleva id.
     * @param rivi josta treenin tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Laji laji = new Laji();
     *   laji.parse(" 3 |  jooga  | 0 | 0 ");
     *   laji.getId() === 3;
     *   laji.toString().startsWith("3|jooga|0|0") === true; 
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        laji = Mjonot.erota(sb, '|', laji);
        viikkoTavoite = Mjonot.erota(sb, '|', viikkoTavoite);
        kuukausiTavoite = Mjonot.erota(sb, '|', kuukausiTavoite);        
    }
    
    
    /**
     * Asettaa tiedostosta luetun idnumeron lajille.
     * @param idnumero joka asetetaan lajille
     */
    public void setId(int idnumero) {
        id = idnumero;
        seuraavaId = idnumero + 1; 
    }
    

    /**
     * Asettaa lajille nimen.
     * @param lajin nimi
     */
    public void setNimi(String lajin) {
        laji = lajin;
    }
    
    
    /**
     * Palauttaa treenin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return j‰sen tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Laji laji = new Laji();                                                                              
     *   laji.parse("   3  | jooga | 0 | 4");                                                                             
     *   laji.toString().startsWith("3|jooga|0|4") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu |
     * </pre>
     */
    @Override
    public String toString() {
        return  "" +
                id + "|" +                
                laji + "|" +
                viikkoTavoite + "|" +
                kuukausiTavoite;
    }
    
    
    /**
     * Testip‰‰ohjelma luokalle Laji.
     * @param args ei k‰ytet‰
     */
    public static void main(String[] args) {
        Laji laji1 = new Laji(), laji2 = new Laji(), laji3 = new Laji(), laji4 = new Laji(), laji5 = new Laji();
        
        laji1.rekisteroi();
        laji2.rekisteroi();
        laji3.rekisteroi();
        laji4.rekisteroi();
        laji5.rekisteroi();
        
        laji1.tulosta(System.out);
        laji2.tulosta(System.out);
        laji3.tulosta(System.out);
        laji4.tulosta(System.out);
        laji5.tulosta(System.out);
        
        laji1.taytaLajinTiedot();
        laji2.taytaLajinTiedot();
        laji3.taytaLajinTiedot();
        laji4.taytaLajinTiedot();
        laji5.taytaLajinTiedot();
        
        laji1.tulosta(System.out);
        laji2.tulosta(System.out);
        laji3.tulosta(System.out);
        laji4.tulosta(System.out);
        laji5.tulosta(System.out);
    }
}
