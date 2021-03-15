package treenipaivakirja;

import java.io.File;
import java.util.List;

/**
 * Luokka, joka huolehtii treeneist‰, lajeista ja treenipaikoista. P‰‰osin metodit
 * ovat vain "v‰litt‰j‰metodeja" treeneille, lajeille ja treenipaikoille.
 * 
 * Tekem‰tt‰ viel‰ luokat olosuhteille, ja niille asianmukaiset "v‰litt‰j‰metodit"
 * sek‰ iteraattori itse tehdylle tietorakeentelle treeneihin.
 * 
 * @author Kristian Koronen
 * @version 22.3.2018
 *
 */
public class Treenipaivakirja {
    private  Treenit      treenit      = new Treenit();
    private  Lajit        lajit        = new Lajit();
    private  Treenipaikat treenipaikat = new Treenipaikat();
    
    
    /**
     * Asettaa muutoksen arvoksi true.
     */
    public void muutettu() {
        treenit.muutettu();
        lajit.muutettu();
        treenipaikat.muutettu();
    }
    
    
    /**
     * Lajittelee treenit p‰iv‰m‰‰r‰n mukaan.
     */
    public void lajittele() {
        if (treenit != null) treenit.lajittele();
    }
    
    
    /**
     * Hakee paikassa i sijaitsevan Treeni-olion viitteen. 
     * @param i Paikka taulukossa treeniAlkiot
     * @return Palauttaa viitteen paikasta lˆytyv‰‰n olioon.
     * @example
     * <pre name="test">
     *   #THROWS IndexOutOfBoundsException
     *   Treenipaivakirja treenipaivakirja = new Treenipaivakirja();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treenipaivakirja.lisaa(treeni1);
     *   treenipaivakirja.lisaa(treeni2);
     *   treenipaivakirja.anna(0) === treeni1;
     *   treenipaivakirja.anna(1) === treeni2;
     *   treenipaivakirja.anna(-22); #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public Treeni anna(int i) {
        return treenit.anna(i);
    }
    
    
    /**
     * Hakee kohdassa i olevan lajin listasta.
     * @param i indeksi, josta laji haetaan
     * @return Palauttaa viitteen lajiolioon
     */
    public Laji annaLaji(int i) {
        return lajit.anna(i);
    }

    
    /**
     * Antaa taulukon treeneist‰.
     * @return Palauttaa taulukon treeneist‰.
     */
    public Treeni[] annaTreenit() {
        return treenit.annaTreenit();
    }
    
    
    /**
     * Antaa listan lajeista.
     * @return Palauttaa listan lajeista
     */
    public List<Laji> annaLajit() {
        return lajit.annaLajit();
    }

    
    /**
     * Antaa listan kaikista lajeista.
     * @return Palauttaa listan lajeista
     */
    public List<Treenipaikka> annaTreenipaikat() {
        return treenipaikat.annaTreenipaikat();
    }
        
    
    /**
     * Hakee Treenit-olion taulukon treeniAlkiot Treeni-olioiden lukum‰‰r‰n.
     * @return Palauttaa treenimerkintˆjen lukum‰‰r‰n.
     * @example
     * <pre name="test">
     *   Treenipaivakirja treenipaivakirja = new Treenipaivakirja();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treenipaivakirja.getLkm() === 0;
     *   treenipaivakirja.lisaa(treeni1); treenipaivakirja.getLkm() === 1;
     *   treenipaivakirja.lisaa(treeni2); treenipaivakirja.getLkm() === 2;
     *   treenipaivakirja.lisaa(treeni1); treenipaivakirja.getLkm() === 2;
     * </pre>
     */
    public int getLkm() {
        return treenit.getLkm();
    }
    
    
    /**
     * @param treeni Lis‰tt‰v‰ treeni
     * <pre name="test">
     *   Treenipaivakirja treenipaivakirja = new Treenipaivakirja();
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni();
     *   treenipaivakirja.lisaa(treeni1); treenipaivakirja.getLkm() === 1;
     *   treenipaivakirja.lisaa(treeni2); treenipaivakirja.getLkm() === 2;
     *   treenipaivakirja.anna(0) === treeni1;
     *   treenipaivakirja.anna(1) === treeni2;
     *   treenipaivakirja.lisaa(treeni2); treenipaivakirja.getLkm() === 2;
     *   treenipaivakirja.anna(1) == treeni1 === false;
     *   treenipaivakirja.anna(0) == treeni1 === true;
     *   Treeni treeni3 = new Treeni(), treeni4 = new Treeni(), treeni5 = new Treeni(), treeni6 = new Treeni();
     *   treenipaivakirja.lisaa(treeni3); treenipaivakirja.getLkm() === 3;
     *   treenipaivakirja.lisaa(treeni4); treenipaivakirja.getLkm() === 4;
     *   treenipaivakirja.lisaa(treeni5); treenipaivakirja.getLkm() === 5;
     *   treenipaivakirja.lisaa(treeni6); treenipaivakirja.getLkm() === 6;
     *   treeni1 == treeni2 === false;
     *   treeni3 == treeni4 === false;
     *   treeni1 == treeni6 === false;
     * </pre>
     */
    public void lisaa(Treeni treeni) {
        treenit.lisaa(treeni);       
    }
    
    
    /**
     * Lis‰‰ lajin listaan.
     * @param laji Lis‰tt‰v‰ laji
     */
    public void lisaa(Laji laji) {
        lajit.lisaa(laji);
    }
    
    
    /**
     * Lis‰‰ treenipaikam listaan.
     * @param treenipaikka Lis‰tt‰v‰ treenipaikka
     */
    public void lisaa(Treenipaikka treenipaikka) {
        treenipaikat.lisaa(treenipaikka);
    }
    
    
    /**
     * Poistaa parametrina annetun treenin.
     * @param treeni Poistettava treeni
     * @return Palauttaa 1, jos poisto onnistui ja 0 jos ei
     */
    public int poista(Treeni treeni) {
        if (treeni == null) return 0;
        return treenit.poista(treeni.getId());
    }
    
    
    /**
     * Poistaa lajin.
     * @param laji Poistettava laji
     * @return Palauttaa 1, jos poisto onnistui ja 0 jos ei
     */
    public int poista(Laji laji) {
        if (laji == null) return 0;
        return lajit.poista(laji);
    }
    
    
    /**
     * Poistaa treenipaikan.
     * @param treenipaikka Poistettava paikka
     * @return Palauttaa 1, jos poisto onnistui ja 0 jos ei
     */
    public int poista(Treenipaikka treenipaikka) {
        if (treenipaikka == null) return 0;
        return treenipaikat.poista(treenipaikka);
    }
    
    
    /**
     * Muokkaa lajia.
     * @param laji Muokattava laji
     * @param nimi Uusi nimi lajille
     */
    public void muokkaa(Laji laji, String nimi) {
        lajit.muokkaa(laji, nimi);
    }
    
    
    /**
     * Muokkaa treenipaikkaa.
     * @param paikka Muokattava treenipaikka
     * @param nimi Uusi nimi treenipaikalle
     */
    public void muokkaa(Treenipaikka paikka, String nimi) {
        treenipaikat.muokkaa(paikka, nimi);
    }
    
    
    /**
     * Etsii laji-id:t‰ vastaavan lajin.
     * @param hakuehto Mink‰ mukaan haetaan listasta
     * @param kentta lajin kentta, johon hakuehtoa verrataan
     * @param palautus mik‰ kentta palautetaan
     * @return Palauttaa halutun lajin kentan tai tyhjan merkkijonon, jos ei loydy
     */
    public String etsiLaji(String hakuehto, int kentta, int palautus) {
        for (Laji laj : lajit) 
            if (laj.anna(kentta).equals(hakuehto)) return laj.anna(palautus);
        return "";
    }
    
    
    /**
     * Etsii treenipaikka-id:t‰ vastaavan treenipaikan.
     * @param hakuehto Mink‰ mukaan haetaan listasta
     * @param kentta treenipaikan kentt‰, johon hakuehtoa verrataan
     * @param palautus mik‰ treenipaikan kentt‰ palautetaan
     * @return Palauttaa halutun kent‰n tai tyhj‰n merkkijonon, jos ei lˆydy
     */
    public String etsiPaikka(String hakuehto, int kentta, int palautus) {
        for (Treenipaikka treenip : treenipaikat) 
            if (treenip.anna(kentta).equals(hakuehto)) return treenip.anna(palautus);
        return "";                                         
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet.
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        treenit.setTiedostonNimi(hakemistonNimi + "treenit");
        lajit.setTiedostonNimi(hakemistonNimi + "lajit"); 
        treenipaikat.setTiedostonNimi(hakemistonNimi + "treenipaikat");
    }
    
    
    /**
     * Lukee treenipaivakirjan tiedot tiedostosta.
     * @param nimi jota k‰yte‰‰n lukemisessa
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        treenit = new Treenit(); 
        lajit = new Lajit();

        setTiedosto(nimi);
        treenit.lueTiedostosta();
        lajit.lueTiedostosta();
        treenipaikat.lueTiedostosta();
    }


    /**
     * Tallenttaa kerhon tiedot tiedostoon.  
     * Vaikka j‰senten tallettamien ep‰onistuisi, niin yritet‰‰n silti tallettaa
     * harrastuksia ennen poikkeuksen heitt‰mist‰.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            treenit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            lajit.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        
        try {
            treenipaikat.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    
    
    /**
     * Testip‰‰ohjelma luokalle Treenip‰iv‰kirja.
     * @param args ei k‰ytet‰
     */
    public static void main(String[] args) {       
        Treenipaivakirja treenipaivakirja = new Treenipaivakirja();
        
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
        
        treenipaivakirja.lisaa(treeni1);
        treenipaivakirja.lisaa(treeni2);
        treenipaivakirja.lisaa(treeni3);
        treenipaivakirja.lisaa(treeni4);
        treenipaivakirja.lisaa(treeni5);
        treenipaivakirja.lisaa(treeni6);
        
        System.out.println("======================== Treenip‰iv‰kirjan testit ========================");
        
        for (int i = 0; i < treenipaivakirja.getLkm(); i++) {  // TODO: Toteuta ilman metodeja getLkm ja anna. K‰yt‰ iteraattoria.
            Treeni treeni = treenipaivakirja.anna(i);
            System.out.println("Treenin numero : " + i);
            treeni.tulosta(System.out);
        }
    }
}
