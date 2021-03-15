/**
 * 
 */
package treenipaivakirja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Treenipaikat-luokka, joka
 *     - yll‰pit‰‰ listaa kaikista treenipaikoista,
 *     - osaa lis‰t‰, muokata ja poistaa treenipaikkoja,
 *     - osaa iteraatorin avulla k‰yd‰ l‰pi listan kaikki alkiot,
 *     - osaa lukea tiedostosta listaan treenipaikkoja,
 *     - ja osaa tallentaa tiedostoon listan treenipaikat.
 * 
 * @author Kristian Koronen
 * @version 22.3.2018
 *
 */
public class Treenipaikat implements Iterable<Treenipaikka> {
    private final ArrayList<Treenipaikka> treenipaikkaAlkiot = new ArrayList<Treenipaikka>();
    private String tiedostonNimi = "treenipaikat";
    private boolean muutettu = false;
    
    
    /**
     * Muokkaa haluttua treenipaikkaa.
     * @param paikka Muokattava treenipaikka
     * @param nimi Uusi nimi treenipaikalle
     */
    public void muokkaa(Treenipaikka paikka, String nimi) {
        paikka.setNimi(nimi);
        muutettu = true;
    }
    
    
    /**
     * Asettaa muutoksen arvoksi true.
     */
    public void muutettu() {
        muutettu = true;
    }
    
    
    /**
     * Antaa listan kaikista lajeista.
     * @return Palauttaa listan lajeista
     */
    public List<Treenipaikka> annaTreenipaikat() {
        return treenipaikkaAlkiot;
    }
    
    
    /**
     * Poistaa treenipaikan.
     * @param treenipaikka poistettava paikka
     * @return Palauttaa 1, jos poisto onnistui, tai 0 jos ei
     */
    public int poista(Treenipaikka treenipaikka) {
        if (treenipaikkaAlkiot.remove(treenipaikka)) {
            muutettu = true;
            return 1;
        }
        return 0;
    }
    
    
    /**
     * Lukee lajit tiedostosta. 
     * @param tied tiedoston nimi
     * @throws SailoException jos lukeminen ep‰onnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Treenipaikat treenipaikat = new Treenipaikat();
     *  Treenipaikka treenipaikka1 = new Treenipaikka(), treenipaikka2 = new Treenipaikka();
     *  treenipaikka1.taytaTreenipaikanTiedot();
     *  treenipaikka2.taytaTreenipaikanTiedot();
     *  String hakemisto = "treenipaivakirja_tiedostot_testit";
     *  String tiedNimi = hakemisto+"/treenittesti";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  treenipaikat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  treenipaikat.lisaa(treenipaikka1);
     *  treenipaikat.lisaa(treenipaikka2);
     *  treenipaikat.tallenna();
     *  treenipaikat = new Treenipaikat();            // Poistetaan vanhat luomalla uusi
     *  treenipaikat.lueTiedostosta(tiedNimi);        // johon ladataan tiedot tiedostosta.
     *  treenipaikat.lisaa(treenipaikka2);
     *  treenipaikat.tallenna();
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
                Treenipaikka treenipaikka = new Treenipaikka();
                treenipaikka.parse(rivi);
                lisaa(treenipaikka);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisest‰ tiedostosta.
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen.
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
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen.
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
     * Tallentaa treenit tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * 2|03.04.2018|1|120|2|Liukupotku s‰‰rille!
     * 3|04.04.2018|1|60|2|Liukupotku s‰‰rille!
     * </pre>
     * @throws SailoException jos talletus ep‰onnistuu
     */
    public void tallenna() throws SailoException { 
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimet‰");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(";tpid|treenipaikka");
            for (Treenipaikka treenipaikka : treenipaikkaAlkiot) {
                fo.println(treenipaikka.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    
    /**
     * Iteraattori kaikkien treenipaikkojen l‰pik‰ymiseen.
     * @return treenipaikka-iteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Treenipaikat treenipaikkoja = new Treenipaikat();
     *  Treenipaikka treenipaikka1 = new Treenipaikka(); treenipaikka1.rekisteroi(); treenipaikkoja.lisaa(treenipaikka1);
     *  Treenipaikka treenipaikka2 = new Treenipaikka(); treenipaikka2.rekisteroi(); treenipaikkoja.lisaa(treenipaikka2);
     *  Treenipaikka treenipaikka3 = new Treenipaikka(); treenipaikka3.rekisteroi(); treenipaikkoja.lisaa(treenipaikka3);
     *  Treenipaikka treenipaikka4 = new Treenipaikka(); treenipaikka4.rekisteroi(); treenipaikkoja.lisaa(treenipaikka4);
     *  Treenipaikka treenipaikka5 = new Treenipaikka(); treenipaikka5.rekisteroi(); treenipaikkoja.lisaa(treenipaikka5);
     *  Treenipaikka treenipaikka6 = new Treenipaikka();
     * 
     *  Iterator<Treenipaikka> i2 = treenipaikkoja.iterator();
     *  i2.next() === treenipaikka1;
     *  i2.next() === treenipaikka2;
     *  i2.next() === treenipaikka3;
     *  i2.next() === treenipaikka4;
     *  i2.next() === treenipaikka5;
     *  i2.next() === treenipaikka6; #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int tjid[] = {1,2,3,4,5};
     *  
     *  for (Treenipaikka treenip : treenipaikkoja) { 
     *    treenip.getId() === tjid[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Treenipaikka> iterator() {
        return treenipaikkaAlkiot.iterator();
    }
    
    
    /**
     * Laskee listan pituuden/alkioiden lukum‰‰r‰n.
     * @return Palauttaa listan koon.
     */
    private int getLkm() {
        return treenipaikkaAlkiot.size();
    }
    
    
    /**
     * Lis‰‰ treenipaikan listaan.
     * @param treenip Olio, joka lis‰t‰‰n listaan.
     */
    public void lisaa(Treenipaikka treenip) {
        treenipaikkaAlkiot.add(treenip);
        muutettu = true;
    }
    

    /**
     * Testip‰‰ohjelma luokalle.
     * @param args ei k‰ytet‰
     */
    public static void main(String[] args) {
        Treenipaikat treenipaikkoja = new Treenipaikat();
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
        
        treenipaikka1.taytaTreenipaikanTiedot();
        treenipaikka2.taytaTreenipaikanTiedot();
        treenipaikka3.taytaTreenipaikanTiedot();
        treenipaikka4.taytaTreenipaikanTiedot();
        treenipaikka5.taytaTreenipaikanTiedot();
        
        treenipaikkoja.lisaa(treenipaikka1);
        treenipaikkoja.lisaa(treenipaikka2);
        treenipaikkoja.lisaa(treenipaikka3);
        treenipaikkoja.lisaa(treenipaikka4);
        treenipaikkoja.lisaa(treenipaikka5);
        
        System.out.println("======================== treenipaikat testi ========================");
        
        for (Treenipaikka treenip : treenipaikkoja) treenip.tulosta(System.out);
        for (int i = 0; i < treenipaikkoja.getLkm(); i++) treenipaikkoja.treenipaikkaAlkiot.get(i).tulosta(System.out);
    }
}
