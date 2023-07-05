package eksamen2023.model;

import java.text.Collator;

/**
 *  Klasse for land
 * @author 7040
 */
public class Land implements Comparable<Land> {

    private String landkode;
    private String landsnavn;
    private String hovedstad;
    private final static Collator KOLLATOR = Collator.getInstance();
    /**
     * Oppretting av et Land objekt
     * @param landkode
     * @param landsnavn
     * @param hovedstad 
     */
    public Land(String landkode, String landsnavn, String hovedstad) {
        this.landkode = landkode;
        this.landsnavn = landsnavn;
        this.hovedstad = hovedstad;
    }
    /**
     * Henter landkode
     * @return String
     */
    public String getLandkode() {
        return landkode;
    }
    /**
     * Setter landkode for dette objektet
     * @param landkode 
     */
    public void setLandkode(String landkode) {
        this.landkode = landkode;
    }
    /**
     * Henter dette landsnavnet
     * @return String landsnavn
     */
    public String getLandsnavn() {
        return landsnavn;
    }
    /**
     * Setter dette landsnavnet
     * @param String landsnavn 
     */
    public void setLandsnavn(String landsnavn) {
        this.landsnavn = landsnavn;
    }
    /**
     * Henter hovedstad
     * @return 
     */
    public String getHovedstad() {
        return hovedstad;
    }
    /**
     * Setter hovedstad
     * @param String hovedstad 
     */
    public void setHovedstad(String hovedstad) {
        this.hovedstad = hovedstad;
    }
    /**
     * Teksten som skrives til fil
     * @return 
     */
    public String toFile() {
        return landkode + "," + landsnavn + "," + hovedstad;
    }
    /**
     * Sammenligning av land
     * @param land
     * @return 
     */
    @Override
    public int compareTo(Land land) {
        return KOLLATOR.compare(this.getLandkode(), land.getLandkode());
    }
}
