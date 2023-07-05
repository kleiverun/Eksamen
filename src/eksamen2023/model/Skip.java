/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eksamen2023.model;

/**
 *  * Abstract klasse for skip, blir brukt i et hashmap for lagring av skip
 * @author 7040
 *
 */
public abstract class Skip {

    private String regnr;
    private int lengde;
    private int antallBruttoRegTonn;
    private String landkode;
    private Status status;
    /**
     * Konstruktør
     * @param regnr
     * @param lengde
     * @param antallBruttoRegTonn
     * @param landkode
     * @param status 
     */
    public Skip(String regnr, int lengde, int antallBruttoRegTonn, String landkode, Status status) {
        this.regnr = regnr;
        this.lengde = lengde;
        this.antallBruttoRegTonn = antallBruttoRegTonn;
        this.status = status;
        this.landkode = landkode;
    }
    /**
     * Hent regnr
     * @return String
     */
    public String getRegnr() {
        return regnr;
    }
    /**
     * Setter regnr
     * @param regnr 
     */
    public void setRegnr(String regnr) {
        this.regnr = regnr;
    }
    /**
     * Henter båtlengde
     * @return int
     */
    public int getLengde() {
        return lengde;
    }
    /**
     * Setter lengde på skipet
     * @param int lengde 
     */
    public void setLengde(int lengde) {
        this.lengde = lengde;
    }
    /**
     * Henter antall brutto registrert tonn
     * @return int
     */
    public int getAntallBruttoRegTonn() {
        return antallBruttoRegTonn;
    }
    /**
     * Setter antall brutto registrert tonn
     * @param antallBruttoRegTonn 
     */
    public void setAntallBruttoRegTonn(int antallBruttoRegTonn) {
        this.antallBruttoRegTonn = antallBruttoRegTonn;
    }
    /**
     * Henter status 
     * @return Status
     */
    public Status getStatus() {
        return status;
    }
    /**
     * Setter status, param : Status
     * @param status 
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    /**
     * Tostring teksten
     * @return String
     */
    @Override
    public String toString() {
        return "Skip{" + "regnr=" + regnr + ", lengde=" + lengde
                + ", antallBruttoRegTonn=" + antallBruttoRegTonn + ", status=" + status;
    }
    /**
     * Skriv til-fil metoden
     * @return String
     */
    public String toFile() {
        return this.getRegnr() + "," + this.lengde + "," + this.antallBruttoRegTonn + "," + this.status + "," + this.landkode;
    }
    /**
     * Henter landkode
     * @return String
     */
    public String getLandkode() {
        return landkode;
    }
    /**
     * Setter landkode
     * @param String landkode 
     */
    public void setLandkode(String landkode) {
        this.landkode = landkode;
    }

}
