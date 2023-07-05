package eksamen2023.model;

/**
 * Enum-statusene for skipene
 *
 * @author 7040
 */
public enum Status {
    I_HAVN("I_HAVN"),
    UNDERVEIS("UNDERVEIS"),
    I_OPPLAG("I_OPPLAG"),
    TIL_REPERASJON("TIL_REPERASJON");

    private String tekst;

    Status(String tekst) {
        this.tekst = tekst;
    }

    public String toString() {
        return tekst;
    }
}
