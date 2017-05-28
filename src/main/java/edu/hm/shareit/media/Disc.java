package edu.hm.shareit.media;

/**
 * A Class for Discs.
 */
public class Disc extends Medium {
    /**
     * Barcode of Disc.
     */
    private final String barcode;
    /**
     * Director of Disc.
     */
    private String director;
    /**
     * Fsk of Disc.
     */
    private int fsk;

    /**
     * <a href="https://www.youtube.com/watch?v=oHg5SJYRHA0">Click me</a>
     */
    private Disc() {
        this("Never gonna", "give you", "up", 0);
    }

    /**
     * Creates a representation of a disc.
     * Not to be mistaken as an exemplar of the disc.
     *
     * @param title    Title of the disc
     * @param barcode  Barcode of the disc
     * @param director Director of the disc
     * @param fsk      FSK of the disc
     */
    public Disc(String title, String director, String barcode, int fsk) {
        super(title);
        barcode = barcode.replaceAll("-", "");
        if (barcode == null || director == null) {
            throw new NullPointerException();
        }
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    /**
     * Copy ctor
     * @param disc
     */
    public Disc(Disc disc) {
        super(disc.getTitle());
        if (disc.getBarcode() == null || disc.getDirector() == null) {
            throw new NullPointerException();
        }

        this.director = disc.getDirector();
        this.barcode = disc.getBarcode().replaceAll("-","");
        this.fsk = disc.getFsk();
    }


    /**
     * Setter for fks.
     *
     * @param fsk The fsk.
     */
    public void setFsk(int fsk) {
        this.fsk = fsk;
    }

    /**
     * Setter for director.
     *
     * @param director The director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Getter for barcode.
     *
     * @return barcode
     */
    public String getBarcode() {
        return this.barcode;
    }

    /**
     * Getter for director.
     *
     * @return director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Getter for fsk.
     *
     * @return fsk
     */
    public int getFsk() {
        return fsk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Disc disc = (Disc) o;

        return getBarcode().equals(disc.getBarcode());
    }

    @Override
    public int hashCode() {
        return getBarcode().hashCode();
    }

    @Override
    public String toString() {
        return "Disc{"
                + "barcode='" + barcode + '\''
                + ", director='" + director + '\''
                + ", fsk=" + fsk
                + '}';
    }
}
