/**
 * 
 */
package treenipaivakirja;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Treenipaikka-luokka, joka
 *     - osaa asettaa itselleen tietoja,
 *     - osaa kertoa itsest‰‰n pyydettyj‰ tietoja,
 *     - osaa esitt‰‰ itsens‰ String-muodossa
 *     - ja osaa asettaa itselleen tiedot parsimalla luetusta tiedoston rivist‰.
 * 
 * @author Kristian Koronen
 * @version 22.3.2018
 *
 */
public class Treenipaikka {
    private int    id         = 0;
    private String paikanNimi = "";
    
    /** Staattinen muuttuja luokan Treenipaikka olioille id numeron rekisterˆinti‰ varten. */
    private static int seuraavaId = 1;
    
    
    /**
    * Rekisteroi olion antamalla sille id-numeron ja samalla nostaa Treenipaikka-olioiden yhteista
    * staattista muuttujaa seuraavaId yhdella.
    * @example
    * <pre name="test">
    *   Treenipaikka treenipaikka1 = new Treenipaikka();
    *   treenipaikka1.getId() === 0;
    *   treenipaikka1.rekisteroi();
    *   Treenipaikka treenipaikka2 = new Treenipaikka();
    *   treenipaikka2.rekisteroi();
    *   int idNumero1 = treenipaikka1.getId();
    *   int idNumero2 = treenipaikka2.getId();
    *   idNumero1 = idNumero2 - 1;
    *   treenipaikka1.rekisteroi();
    *   treenipaikka1.getId() === idNumero1;
    * </pre>
    */
    public void rekisteroi() {            
        if (getId() != 0) return;         
        id = seuraavaId;                
        seuraavaId++;                     
    }


    /**
    * @return Palauttaa treenipaikan id-numeron.
    */
    public int getId() {
        return id;
    }

    
    /**
     * T‰ytt‰‰ treenipaikan tiedot automaattisesti. Valiaikainen ratkaisu, jotta 
     * voidaan nahda nopeasti luokan oikeanlainen toiminta.
     */
    public void taytaTreenipaikanTiedot() {
        paikanNimi = "Viitaniemi" + String.format("%03d", Treeni.rand(1,99));                
    }
    

    /**
    * Tulostaa lajin tiedot parametrina annettuun tietovirtaan.
    * @param out Tietovirta, johon tulostetaan.
    */
    public void tulosta(PrintStream out) {
        out.printf("%03d\n%10s\n", id, paikanNimi);
    }
   
    
    /**
     * Antaa halutun attribuutin merkkijonona.
     * @param k attribuutti, joka palautetaan
     * @return Palauttaa attribuutin merkkijonona tai tyhj‰n merkkijonon, jos ei lˆydy.
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + id;
        case 1: return "" + paikanNimi; 
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
     *   Treenipaikka treenipaikka = new Treenipaikka();
     *   treenipaikka.parse("   3  |  Laajavuori ");
     *   treenipaikka.getId() === 3;
     *   treenipaikka.toString().startsWith("3|Laajavuori") === true; 
     * 
     * </pre>
     */
    public void parse(String rivi) {  
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        paikanNimi = Mjonot.erota(sb, '|', paikanNimi);   
    }
    
    
    /**
     * Asettaa idnumeron treenille.
     * @param idnumero Asetettava numero 
     */
    public void setId(int idnumero) {
        id = idnumero;
        seuraavaId = idnumero + 1; 
    }
    
    
    /**
     * Asettaa treenipaikalle nimen.
     * @param paikan nimi
     */
    public void setNimi(String paikan) {
        paikanNimi = paikan;
    }
    
    
    /**
     * Palauttaa treenin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return j‰sen tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Treenipaikka treenipaikka = new Treenipaikka();                                                                              
     *   treenipaikka.parse("   3  | Honkalan kuntosali");                                                                             
     *   treenipaikka.toString().startsWith("3|Honkalan kuntosali") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu |
     * </pre>
     */
    @Override
    public String toString() {
        return  "" +
                id + "|" +                
                paikanNimi;
    }
    
   
    /**
     * Testip‰‰ohjelma luokalle.
     * @param args ei k‰ytet‰
     */
    public static void main(String[] args) {
        Treenipaikka treenipaikka1 = new Treenipaikka(), 
                     treenipaikka2 = new Treenipaikka(), 
                     treenipaikka3 = new Treenipaikka(), 
                     treenipaikka4 = new Treenipaikka(), 
                     treenipaikka5 = new Treenipaikka();
        
        treenipaikka1.rekisteroi();
        treenipaikka2.rekisteroi();
        treenipaikka3.rekisteroi();
        treenipaikka4.rekisteroi();
        treenipaikka5.rekisteroi();
        
        treenipaikka1.tulosta(System.out);
        treenipaikka2.tulosta(System.out);
        treenipaikka3.tulosta(System.out);
        treenipaikka4.tulosta(System.out);
        treenipaikka5.tulosta(System.out);
        
        treenipaikka1.taytaTreenipaikanTiedot();
        treenipaikka2.taytaTreenipaikanTiedot();
        treenipaikka3.taytaTreenipaikanTiedot();
        treenipaikka4.taytaTreenipaikanTiedot();
        treenipaikka5.taytaTreenipaikanTiedot();
        
        treenipaikka1.tulosta(System.out);
        treenipaikka2.tulosta(System.out);
        treenipaikka3.tulosta(System.out);
        treenipaikka4.tulosta(System.out);
        treenipaikka5.tulosta(System.out);   
    }
}
