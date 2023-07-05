package eksamen2023.model;

import java.text.Collator;

/**
 * Cruiseskip klasse
 *
 * @author 7040
 */
public class CruiseSkip extends Skip implements Comparable<Land> {

    private final static Collator KOLLATOR = Collator.getInstance();
    private int antalLugarer;

    /**
     * Konstruktør
     *
     * @param regnr
     * @param lengde
     * @param antallBruttorRegTonn
     * @param landkode
     * @param status
     * @param antalLugarer
     */
    public CruiseSkip(String regnr, int lengde, int antallBruttorRegTonn, String landkode, Status status, int antalLugarer) {
        super(regnr, lengde, antallBruttorRegTonn, landkode, status);
        this.antalLugarer = antalLugarer;
    }

    /**
     * Henter antall lugarer
     *
     * @return int
     */
    public int getAntalLugarer() {
        return antalLugarer;
    }

    /**
     * Setter antall lugarer
     *
     * @param int
     */
    public void setAntalLugarer(int antalLugarer) {
        this.antalLugarer = antalLugarer;
    }

    /**
     * Henter en toString av objektet
     *
     * @return String
     */
    @Override
    public String toString() {
        return "CruiseSkip{" + super.toString() + "antalLugarer=" + antalLugarer + '}';
    }

    /**
     * Teksten for å skrive til fil
     *
     * @return String
     */
    public String toFile() {
        return "CruiseSkip," + super.toFile()+"," + antalLugarer;
    }

    /**
     * Sammenligning
     *
     * @param land
     * @return
     */
    @Override
    public int compareTo(Land land) {
        return KOLLATOR.compare(this.getLandkode(), land.getLandkode());

    }

}
