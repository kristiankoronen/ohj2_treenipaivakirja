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
 * Lajit-luokka, joka
 *     - yll‰pit‰‰ listaa kaikista lajeista,
 *     - osaa lis‰t‰, muokata tai poistaa lajeja,
 *     - osaa k‰yd‰ iteraattorin avulla kaikki listan alkiot l‰pi,
 *     - osaa lukea tiedostosta listaan lajeja
 *     - ja osaa tallentaa tiedostoon tietonsa.
 * Mit‰ ei itse kykene tekem‰‰n antaa Laji-luokan teht‰v‰ksi. 
 * 
 * @author Kristian Koronen
 * @version 21.3.2018
 *
 */
public class Lajit implements Iterable<Laji> {
    /** Lista lajeista */
    private final ArrayList<Laji> lajiAlkiot = new ArrayList<Laji>();
    private boolean muutettu = false;
    private String tiedostonNimi = "lajit";

    
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
     *  Lajit lajit = new Lajit();
     *  Laji laji1 = new Laji(), laji2 = new Laji();
     *  laji1.taytaLajinTiedot();
     *  laji2.taytaLajinTiedot();
     *  String hakemisto = "treenipaivakirja_tiedostot_testit";
     *  String tiedNimi = hakemisto+"/treenittesti";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  lajit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  lajit.lisaa(laji1);
     *  lajit.lisaa(laji2);
     *  lajit.tallenna();
     *  lajit = new Lajit();            // Poistetaan vanhat luomalla uusi
     *  lajit.lueTiedostosta(tiedNimi);   // johon ladataan tiedot tiedostosta.
     *  lajit.lisaa(laji2);
     *  lajit.tallenna();
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
                Laji laji = new Laji();
                laji.parse(rivi); // voisi olla virhek‰sittely
                lisaa(laji);
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
            fo.println(";lid|laji|tavoite h/vko|tavoite h/kk");
            for (Laji lajialkio : lajiAlkiot) {
                fo.println(lajialkio.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    
    /**
     * Poistaa lajin.
     * @param laji Poistettava laji
     * @return Palauttaa 1, jos poisto onnistui, muuten 0
     */
    public int poista(Laji laji) {
        if (lajiAlkiot.remove(laji)) {
            muutettu = true;
            return 1;
        }
        return 0;
    }
    
    
    /**
     * Iteraattori kaikkien lajien l‰pik‰ymiseen.
     * @return laji-iteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Lajit lajeja = new Lajit();
     *  Laji laji1 = new Laji(); laji1.rekisteroi(); lajeja.lisaa(laji1);
     *  Laji laji2 = new Laji(); laji2.rekisteroi(); lajeja.lisaa(laji2);
     *  Laji laji3 = new Laji(); laji3.rekisteroi(); lajeja.lisaa(laji3);
     *  Laji laji4 = new Laji(); laji4.rekisteroi(); lajeja.lisaa(laji4);
     *  Laji laji5 = new Laji(); laji5.rekisteroi(); lajeja.lisaa(laji5);
     *  Laji laji6 = new Laji();
     * 
     *  Iterator<Laji> i2 = lajeja.iterator();
     *  i2.next() === laji1;
     *  i2.next() === laji2;
     *  i2.next() === laji3;
     *  i2.next() === laji4;
     *  i2.next() === laji5;
     *  i2.next() === laji6; #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int ljid[] = {1,2,3,4,5};
     *  
     *  for (Laji laj : lajeja) { 
     *    laj.getId() === ljid[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Laji> iterator() {
        return lajiAlkiot.iterator();
    }
    

    /**
     * Laskee listan pituuden/alkioiden lukum‰‰r‰n.
     * @return Palauttaa listan koon.
     */
    private int getLkm() {
        return lajiAlkiot.size();
    }
    
    
    /**
     * Lis‰‰ lajin listaan.
     * @param laji Olio, joka lis‰t‰‰n listaan.
     */
    public void lisaa(Laji laji) {
        lajiAlkiot.add(laji);
        muutettu = true;
    }

    
    /**
     * Muokkaa lajia.
     * @param laji Muokattava laji
     * @param nimi Uusi nimi lajille
     */
    public void muokkaa(Laji laji, String nimi) {
        laji.setNimi(nimi);
        muutettu = true;
    }
    
    
    /**
     * Asettaa muutoksen arvoksi true.
     */
    public void muutettu() {
        muutettu = true;
    }
    
    
    /**
     * Palauttaa kohdassa i olevan alkion.
     * @param i Listan kohta, josta otetaan alkio
     * @return Lajiolion
     */
    public Laji anna(int i) {
        return lajiAlkiot.get(i);
    }
    
    
    /**
     * @return Palauttaa listan lajeista.
     */
    public List<Laji> annaLajit() {
        return lajiAlkiot;
    }
    
    /**
     * Testip‰‰ohjelma luokalle Lajit.
     * @param args ei k‰ytet‰.
     */
    public static void main(String[] args) {
        Lajit lajit = new Lajit();
        Laji laji1 = new Laji(), laji2 = new Laji(), laji3 = new Laji(), laji4 = new Laji(), laji5 = new Laji();
        
        laji1.rekisteroi();
        laji2.rekisteroi();
        laji3.rekisteroi();
        laji4.rekisteroi();
        laji5.rekisteroi();        
        
        laji1.taytaLajinTiedot();
        laji2.taytaLajinTiedot();
        laji3.taytaLajinTiedot();
        laji4.taytaLajinTiedot();
        laji5.taytaLajinTiedot();
        
        lajit.lisaa(laji1);
        lajit.lisaa(laji2);
        lajit.lisaa(laji3);
        lajit.lisaa(laji4);
        lajit.lisaa(laji5);
        
        System.out.println("======================== Lajit testi ========================");
        
        for (Laji laj : lajit) laj.tulosta(System.out);
        for (int i = 0; i < lajit.getLkm(); i++) lajit.lajiAlkiot.get(i).tulosta(System.out);
    }
}
