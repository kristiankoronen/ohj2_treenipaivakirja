package treenipaivakirja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Treenit luokka, joka 
 *     - ylläpitää taulukkoa kaikista treeneistä,
 *     - osaa lisätä uuden treenin,
 *     - osaa poistaa tai muokata valitun treenin,
 *     - osaa lukea tiedostosta taulukkoon treenejä,
 *     - osaa kasvattaa taulukkoa tarvittaessa,
 *     - osaa lajitella treenit päivämäärän mukaan
 *     - ja osaa tallentaa tiedostoon tietonsa.
 * Mitä luokka ei kykene itse tekemään, välittää eteenpäin Treeni-luokalle.
 *     
 * Vielä tekemättä: 
 *     - iteraattori taulukon läpikäymiseen.   
 * 
 * @author Kristian Koronen
 * @version 21.3.2018
 *
 */
public class Treenit {
    private static final int MAX_TREENEJA = 5; 
    private int lkm;
    private Treeni[] treeniAlkiot = new Treeni[MAX_TREENEJA];
    private boolean muutettu = false;
    private String tiedostonNimi = "treenit";
    
    
    /**
     * Antaa taulukon kaikista treenimerkinnöistä.
     * @return Palauttaa taulukon treeneistä.
     */
    public Treeni[] annaTreenit() {
        Treeni[] treenit = new Treeni[lkm];
        for (int i = 0; i < lkm; i++) treenit[i] = treeniAlkiot[i];
        return treenit;
    }
    
    
    /**
     * Lajittelee treenit päivämäärän mukaan.
     * @param treenit Lajiteltava taulukko
     */
    public static void lajittele(Treeni[] treenit) {
        if (treenit != null) Arrays.sort(treenit);
    }
    
    
    /**
     * Lajittelee treenit päivämäärän mukaan.
     */
    public void lajittele() {
        lajittele(treeniAlkiot);
    }
    
    
    /**
     * Lukee treenit tiedostosta. 
     * @param tied tiedoston nimi
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Treenit treenit = new Treenit();
     *  Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *  treeni1.taytaTreeninTiedot();
     *  treeni2.taytaTreeninTiedot();
     *  String hakemisto = "treenipaivakirja_tiedostot_testit";
     *  String tiedNimi = hakemisto+"/treenittesti";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  treenit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  treenit.lisaa(treeni1);
     *  treenit.lisaa(treeni2);
     *  treenit.tallenna();
     *  treenit = new Treenit();            // Poistetaan vanhat luomalla uusi
     *  treenit.lueTiedostosta(tiedNimi);   // johon ladataan tiedot tiedostosta.
     *  treenit.lisaa(treeni2);
     *  treenit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {                
        setTiedostonNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
    
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                Treeni treeni = new Treeni();
                treeni.parse(rivi); // voisi olla virhekäsittely
                lisaa(treeni);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta.
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Tallentaa treenit tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * 2|03.04.2018|1|120|2|Liukupotku säärille!
     * 3|04.04.2018|1|60|2|Liukupotku säärille!
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {   // TODO: Testit korjattava ainakin(tee ensin tiedostosta luku)
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(";id|päivämäärä|laji|kesto/min|paikka|muistiinpanoja");
            for (int i = 0; i < lkm; i++) {
                fo.println(treeniAlkiot[i].toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }             
        muutettu = false;        
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonNimi;
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta.
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonNimi(String nimi) {
        tiedostonNimi = nimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen.
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi + ".bak";
    }
    
    
    /**
     * Kasvattaa taulukon treeniAlkiot kokoa.
     * @example
     * <pre name="test">
     *   Treenit treeneja = new Treenit();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni(), treeni3 = new Treeni(), 
     *          treeni4 = new Treeni(), treeni5 = new Treeni(), treeni6 = new Treeni();
     *   treeneja.lisaa(treeni1); treeneja.getLkm() === 1;
     *   treeneja.lisaa(treeni2); treeneja.getLkm() === 2;
     *   treeneja.lisaa(treeni3); treeneja.getLkm() === 3;
     *   treeneja.lisaa(treeni4); treeneja.getLkm() === 4;
     *   treeneja.lisaa(treeni5); treeneja.getLkm() === 5;
     *   treeneja.lisaa(treeni6); treeneja.getLkm() === 6;
     * </pre>
     */
    public void kasvataTaulukkoa() {
        Treeni[] isompiTaulukko = new Treeni[lkm * 2 + 1];
        
        for (int i = 0; i < getLkm(); i++) isompiTaulukko[i] = treeniAlkiot[i];
        
        treeniAlkiot = isompiTaulukko;
    }
    
    
    /**
     * Lisää treeniAlkiot-taulukkoon uuden treenin/harjoituksen ja kasvattaa taulukkoa, jos se on täynnä.
     * @param treeni Treeni-olio, joka lisätään taulukkoon.
     * @example
     * <pre name="test">
     *   Treenit treeneja = new Treenit();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treeneja.lisaa(treeni1); treeneja.getLkm() === 1;
     *   treeneja.lisaa(treeni2); treeneja.getLkm() === 2;
     *   treeneja.anna(0) === treeni1;
     *   treeneja.anna(1) === treeni2;
     *   treeneja.lisaa(treeni2); treeneja.getLkm() === 2;
     *   treeneja.anna(1) == treeni1 === false;
     *   treeneja.anna(0) == treeni1 === true;
     *   Treeni treeni3 = new Treeni(), treeni4 = new Treeni(), treeni5 = new Treeni(), treeni6 = new Treeni();
     *   treeneja.lisaa(treeni3); treeneja.getLkm() === 3;
     *   treeneja.lisaa(treeni4); treeneja.getLkm() === 4;
     *   treeneja.lisaa(treeni5); treeneja.getLkm() === 5;
     *   treeneja.lisaa(treeni6); treeneja.getLkm() === 6;
     *   treeni1 == treeni2 === false;
     *   treeni3 == treeni4 === false;
     *   treeni1 == treeni6 === false;
     * </pre>
     */
    public void lisaa(Treeni treeni) {
        if (lkm >= treeniAlkiot.length) kasvataTaulukkoa();
        
        for (int i = getLkm() - 1; i >= 0; i--)       // Tarkistetaan löytyykö taulukosta lisättävä harjoitus/treeni ja 
            if (anna(i) == treeni) return;            // palataan lisäämättä, jos löytyy.
        
        treeniAlkiot[lkm++] = treeni;
        muutettu = true;
    }
    
    
    /** 
     * Poistaa treenin jolla on valittu tunnusnumero.
     * @param id poistettavan treenin tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Treenit treenit = new Treenit(); 
     * Treeni treeni1 = new Treeni(), treeni2 = new Treeni(), treeni3 = new Treeni(); 
     * treeni1.rekisteroi(); treeni2.rekisteroi(); treeni3.rekisteroi(); 
     * int id1 = treeni1.getId(); 
     * treenit.lisaa(treeni1); treenit.lisaa(treeni2); treenit.lisaa(treeni3); 
     * treenit.poista(id1+1) === 1; 
     * treenit.getLkm() === 2; 
     * treenit.poista(id1) === 1; treenit.getLkm() === 1; 
     * treenit.poista(id1+3) === 0; treenit.getLkm() === 1; 
     * </pre> 
     */ 
    public int poista(int id) { 
        int ind = etsiIndeksi(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            treeniAlkiot[i] = treeniAlkiot[i + 1]; 
        treeniAlkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    }
    
    
    /**
     * Asettaa muutoksen arvoksi true.
     */
    public void muutettu() {
        muutettu = true;
    }
    
    
    /**
     * Etsii taulukosta treeniAlkiot treenin tunnusnumeroa vastaavan alkion paikan.
     * @param id Taulukosta etsittävän treenin tunnusnumero
     * @return Palauttaa taulukon indeksin, josta etsitty treeni löytyi tai -1, jos ei löytynyt.
     */
    public int etsiIndeksi(int id) { // TODO: Tee testit!
        for (int i = 0; i < lkm; i++)
            if (id == treeniAlkiot[i].getId()) return i;
        return -1;
    }
    
    
    /**
     * Antaa parametrina annetussa paikassa i sijaitsevan taulukon treeniAlkiot Treeni-olion viitteen.
     * @param i Taulukon treeniAlkiot kohta, josta haetaan Treeni-olio.
     * @return Palauttaa parametrina annetusta paikasta i löytyneen Treeni-olion viitteen.
     * @throws IndexOutOfBoundsException jos paikka ei ole sallitulla alueella
     * @example
     * <pre name="test">
     *   #THROWS IndexOutOfBoundsException
     *   Treenit treeneja = new Treenit();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treeneja.lisaa(treeni1);
     *   treeneja.lisaa(treeni2);
     *   treeneja.anna(0) === treeni1;
     *   treeneja.anna(1) === treeni2;
     *   treeneja.anna(-22); #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public Treeni anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm < i)
                throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        
        return treeniAlkiot[i];
    }
    
    
    /**
     * Hakee Treeni-olioiden lukumäärän.
     * @return Palauttaa Treeni-olioiden lukumäärän taulukossa treeniAlkiot.
     * @example
     * <pre name="test">
     *   Treenit treeneja = new Treenit();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treeneja.getLkm() === 0;
     *   treeneja.lisaa(treeni1); treeneja.getLkm() === 1;
     *   treeneja.lisaa(treeni2); treeneja.getLkm() === 2;
     *   treeneja.lisaa(treeni1); treeneja.getLkm() === 2;
     * </pre>
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Testipääohjelma luokalle Treenit.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Treenit treenit = new Treenit();
        Treeni treeni1 = new Treeni(), treeni2 = new Treeni(), treeni3 = new Treeni(),
               treeni4 = new Treeni(), treeni5 = new Treeni(), treeni6 = new Treeni();
        treeni1.rekisteroi();
        treeni1.taytaTreeninTiedot();
        treeni2.rekisteroi();
        treeni2.taytaTreeninTiedot();
        treeni3.rekisteroi();        
        treeni3.taytaTreeninTiedot();
        treeni4.rekisteroi();        
        treeni4.taytaTreeninTiedot();
        treeni5.rekisteroi();        
        treeni5.taytaTreeninTiedot();
        treeni6.rekisteroi();        
        treeni6.taytaTreeninTiedot();
        
        treenit.lisaa(treeni1);
        treenit.lisaa(treeni2);
        treenit.lisaa(treeni3);
        treenit.lisaa(treeni4);
        treenit.lisaa(treeni5);
        treenit.lisaa(treeni6);
        
        System.out.println("======================== Treenit testi ========================");
        
        for (int i = 0; i < treenit.getLkm(); i++) {        // TODO: Toteuta ilman metodeja getLkm ja anna. Käytä iteraattoria.
            Treeni treeni = treenit.anna(i);
            System.out.println("Treenin numero : " + i);
            treeni.tulosta(System.out);
        }
    }
}
