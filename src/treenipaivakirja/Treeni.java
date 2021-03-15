package treenipaivakirja;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Treenip‰iv‰kirjan Treeni-luokka, joka
 *     - osaa asettaa itselleen kaikki vaaditut tiedot,
 *     - osaa kertoa halutut tiedot itsest‰‰n,
 *     - osaa verrata itse‰‰n toiseen treeni-olioon,
 *     - osaa esitt‰‰ itsens‰ String-muodossa
 *     - ja asettaa itselleen tiedot parsimalla luetusta tiedoston rivist‰.
 * 
 * @author Kristian Koronen
 * @version 21.3.2018
 *
 */
public class Treeni implements Comparable<Treeni> {
    /** Staattinen muuttuja luokan Treeni olioille id-numeroita varten */
    private static int seuraavaId = 1;
    
    private int    id;
    private String pvm            = "";
    private int    laji           = 0; 
    private int    kestoMin       = 0;
    private int    treenipaikka   = 0;
    private String muistiinpanoja = "";
    
    
    /**
     * Arvotaan satunnainen kokonaisluku valille [ala, yla].
     * @param ala arvonnan alaraja
     * @param yla arvonnan ylaraja
     * @return Palauttaa satunnaisen luvun v‰lilt‰ [ala, yla].
     */
    public static int rand(int ala, int yla) {
        double n = (yla - ala) * Math.random() + ala;
        return (int)Math.round(n);
    }
    
    
    /**
     * Rekisteroi olion antamalla sille id-numeron ja samalla nostaa Treeni-olioiden yhteista.
     * staattista muuttujaa seuraavaId yhdella.
     * @example
     * <pre name="test">
     *   Treeni futista = new Treeni();
     *   futista.getId() === 0;
     *   futista.rekisteroi();
     *   Treeni futista1 = new Treeni();
     *   futista1.rekisteroi();
     *   int idNumero = futista.getId();
     *   int idNumero1 = futista1.getId();
     *   idNumero === idNumero1 - 1;
     *   futista.rekisteroi();
     *   futista.getId() === idNumero + 2;
     * </pre>
     */
    public void rekisteroi() {             
        id = seuraavaId;                        
        seuraavaId++;
    }
    
    
    /**
     * @return Palauttaa treenin p‰iv‰m‰‰r‰n.
     */
    public String getPvm() {
        return pvm;
    }
    
    
    /**
     * Metodi tulostaa olion tiedot parametrina annettuun tietovirtaan.
     * @param out Tietovirta, johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        // out.printf("%03d\n\n", id);
        // out.printf("%10s %10s %4d %3s %10s\n", pvm, laji, kestoMin, "min", treenipaikka);
        out.print(muistiinpanoja);
        
    }
    
    
    /**
     * Metodi tulostaa olion tiedot parametrina annettuun tietovirtaan.
     * @param os Tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Tayttaa treenin tiedot automaattisesti. Valiaikainen ratkaisu, jotta 
     * voidaan nahda nopeasti luokan oikeanlainen toiminta.
     */
    public void taytaTreeninTiedot() {
        pvm = String.format("%02d.%02d.%04d", rand(1,28), rand(1,12), 2018);
        laji = 1;
        kestoMin = rand(15, 180);
        treenipaikka = 1;
        muistiinpanoja = "K‰vin kaveriporukan kanssa pelailemassa futista lumihangessa. Raskasta hommaa!";
    }
    
    
    /**
     * Hakee treenin id-numeron.
     * @return Palauttaa treenin id-numeron.
     */
    public int getId() {
        return id;
    }
    
    
    /**
     * Antaa k:n kent‰n sis‰llˆn merkkijonona.
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + id;
        case 1: return "" + pvm;
        case 2: return "" + laji;
        case 3: return "" + kestoMin;
        case 4: return "" + treenipaikka;
        case 5: return "" + muistiinpanoja;
        default: return "";
        }
    }
    
    
    /**
     * Selvit‰‰ treenin tiedot | erotellusta merkkijonosta.
     * Pit‰‰ huolen ett‰ seuraavaId on suurempi kuin tuleva id.
     * @param rivi josta treenin tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Treeni treeni = new Treeni();
     *   treeni.parse("   3  |  03.02.2018   | 3 | 120 | 4 | Liukupotku s‰‰rille!  ");
     *   treeni.getId() === 3;
     *   treeni.toString().startsWith("3|03.02.2018|3|") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu |
     *
     *   treeni.rekisteroi();
     *   int n = treeni.getId();
     *   treeni.parse(""+(n+20));       // Otetaan merkkijonosta vain idnumero
     *   treeni.rekisteroi();           // ja tarkistetaan ett‰ seuraavalla kertaa tulee yht‰ isompi
     *   treeni.getId() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        pvm = Mjonot.erota(sb, '|', pvm);
        laji = Mjonot.erota(sb, '|', laji);
        kestoMin = Mjonot.erota(sb, '|', kestoMin);
        treenipaikka = Mjonot.erota(sb, '|', treenipaikka);
        muistiinpanoja = Mjonot.erota(sb, '|', muistiinpanoja);
    }

    
    /**
     * Vertaa kahta treeni‰ kesken‰‰n p‰iv‰m‰‰r‰n mukaan.
     * @param treeni Mihin verrataan
     * @example
     * <pre name="test">
     *   Treeni treeni1 = new Treeni(), treeni2 = new Treeni(), treeni3 = new Treeni();
     *   treeni1.parse("   3  |  03.02.2018   | 3 | 120 | 4 | Liukupotku s‰‰rille!  ");
     *   treeni2.parse("   3  |  01.03.2012   | 3 | 120 | 4 | Liukupotku s‰‰rille!  ");
     *   treeni3.parse("   3  |  03.02.2018   | 3 | 120 | 4 | Liukupotku s‰‰rille!  ");
     *   treeni1.compareTo(treeni2) === -1;
     *   treeni2.compareTo(treeni1) === 1;
     *   treeni1.compareTo(treeni3) === 0;
     * </pre>
     */
    @Override
    public int compareTo(Treeni treeni) {
        if (treeni == null) return 1;
        StringBuilder pvm1 = new StringBuilder(treeni.getPvm());
        String pvm1pp = Mjonot.erota(pvm1,'.');
        int pp1 = Mjonot.erotaInt(pvm1pp, 0);
        String pvm1kk = Mjonot.erota(pvm1,'.');
        int kk1 = Mjonot.erotaInt(pvm1kk, 0);
        String pvm1vv = Mjonot.erota(pvm1,'.');
        int vv1 = Mjonot.erotaInt(pvm1vv, 0);
        int paivienSumma1 = pp1 + kk1*50 + vv1*1000;
        
        StringBuilder pvm2 = new StringBuilder(pvm);
        String pvm2pp = Mjonot.erota(pvm2,'.');
        int pp2 = Mjonot.erotaInt(pvm2pp, 0);
        String pvm2kk = Mjonot.erota(pvm2,'.');
        int kk2 = Mjonot.erotaInt(pvm2kk, 0);
        String pvm2vv = Mjonot.erota(pvm2,'.');
        int vv2 = Mjonot.erotaInt(pvm2vv, 0);
        int paivienSumma2 = pp2 + kk2*50 + vv2*1000;
        
        if (paivienSumma1 > paivienSumma2) return 1;
        if (paivienSumma1 < paivienSumma2) return -1;
        return 0;
    }
    
    
    /**
     * Asettaa tiedostosta luetun idnumeron treenille.
     * @param idnumero joka treenille asetetaan
     */
    public void setId(int idnumero) {
        id = idnumero;
        seuraavaId = idnumero + 1; 
    }
    
    
    /**
     * Asettaa treenille p‰iv‰m‰‰r‰n.
     * @param paivamaara K‰ytt‰j‰n treenille syˆtt‰m‰ p‰iv‰m‰‰r‰ 
     */
    public void setPvm(String paivamaara) {
        pvm = paivamaara;
    }
    
    
    /**
     * Asettaa treenille lajin.
     * @param lajiId K‰ytt‰j‰n valitsemaa lajia vastaava lajin tunnusnumero
     */
    public void setLaji(int lajiId) {
        laji = lajiId;
    }
    
    
    /**
     * Asettaa treenille keston minuutteina.
     * @param kesto K‰ytt‰j‰n syˆtt‰m‰ treenin kesto minuutteina
     */
    public void setKestoMin(int kesto) {
        kestoMin = kesto;
    }
    
    
    /**
     * Asettaa treenille treenipaikan.
     * @param treenipaikkaId K‰ytt‰j‰n valitsemaa treenipaikkaa vastaava treenipaikan tunnusnumero
     */
    public void setTreenipaikka(int treenipaikkaId) {
        treenipaikka = treenipaikkaId;
    }
    
    
    /**
     * Asettaa treenille muistiinpanot.
     * @param muistiinpanot K‰ytt‰j‰n syˆtt‰m‰t muistiinpanot
     */
    public void setMuistiinpanoja(String muistiinpanot) {
        muistiinpanoja = muistiinpanot;
    }
    
    
    /**
     * Palauttaa treenin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return j‰sen tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Treeni treeni = new Treeni();                                                                              
     *   treeni.parse("   3  |  03.02.2018   | 3 | 120 | 4 | Liukupotku s‰‰rille!  ");                                                                             
     *   treeni.toString().startsWith("3|03.02.2018|3|") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu |
     * </pre>
     */
    @Override
    public String toString() {
        return  "" +
                id + "|" +
                pvm + "|" +
                laji + "|" +
                kestoMin + "|" +
                treenipaikka + "|" +
                muistiinpanoja;
    }
    
    
    /**
     * Testip‰‰ohjelma luokalle.
     * @param args ei k‰ytet‰
     */
    public static void main(String[] args) {        
        Treeni treeni = new Treeni();                                                             
        treeni.parse("   3  |  03.02.2018   | Futis | 120 | Viitaniemi | Liukupotku s‰‰rille!  ");
        System.out.println(treeni.toString());
        treeni.rekisteroi();
        System.out.println(treeni.toString());
        treeni.rekisteroi();
        System.out.println(treeni.toString());
        
        Treeni treeni1 = new Treeni();
        treeni1.rekisteroi();
        System.out.println(treeni1.toString());
        treeni1.rekisteroi();
        System.out.println(treeni1.toString());        
    }        
}
