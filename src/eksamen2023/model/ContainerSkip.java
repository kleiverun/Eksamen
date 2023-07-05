package eksamen2023.model;

import java.text.Collator;

/**
 * ContainerSkip klasse
 *
 * @author 7040
 */
public class ContainerSkip extends Skip implements Comparable<ContainerSkip> {

    private final static Collator KOLLATOR = Collator.getInstance();

    private int antallContainere;
    /**
     * KONSTRUKTØR
     * @param regnr
     * @param lengde
     * @param antallBruttorRegTonn
     * @param landkode
     * @param status
     * @param antallContainere 
     */
    public ContainerSkip(String regnr, int lengde, int antallBruttorRegTonn, String landkode, Status status, int antallContainere) {
        super(regnr, lengde, antallBruttorRegTonn, landkode, status);
        this.antallContainere = antallContainere;
    }
    /**
     * Henter antall containere
     * @return 
     */
    public int getAntallContainere() {
        return antallContainere;
    }
    /**
     * Setter antall containere
     * @param antallContainere 
     */
    public void setAntallContainere(int antallContainere) {
        this.antallContainere = antallContainere;
    }
    /**
     * Henter en tekststreng av objektet
     * @return String
     */
    @Override
    public String toString() {
        return "CruiseSkip " + super.toString()+"," + "AntallContainere=" + this.getAntallContainere() + '}';
    }
    /**
     * Henter en tekststreng av objektet som brukes for å skrive til fil
     * @return String
     */
    public String toFile() {
        return "ContainerSkip," + super.toFile()+"," + antallContainere;
    }
    /**
     * Sammenligning
     * @param o
     * @return int
     */
    @Override
    public int compareTo(ContainerSkip o) {
        return KOLLATOR.compare(this.getLandkode(), o.getRegnr());
    }
}
