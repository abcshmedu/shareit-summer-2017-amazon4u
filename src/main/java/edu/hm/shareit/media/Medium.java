package edu.hm.shareit.media;

import javax.persistence.*;

/**
 * Abstract class for media with title.
 */
@Entity
@Table(name = "TMedium")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Medium {

    /**
     * Title of the medium.
     */
    @Column(name = "title")
    private String title;

    /**
     * Default constructor for Jackson.
     */
    private Medium() {
        this.title = "These aren't the Droids you are looking for!";
    }

    /**
     * Contructor to create a medium
     * (not to be mistaken as an exemplar).
     *
     * @param title The title of the medium
     */
    public Medium(String title) {
        if (title == null) {
            throw new NullPointerException();
        }
        this.title = title;
    }

    /**
     * Getter for title.
     *
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     *
     * @param title The title of the medium.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Medium{"
                + "title='" + title
                + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
