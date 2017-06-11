package edu.hm.shareit.media;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Abstract class for media with title.
 */
@Entity
@Table(name = "TMedium")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Medium implements Serializable {

    /**
     * Title of the medium.
     */
    @Column(name = "title")
    private String title;


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
     * Default constructor for Jackson.
     */
    protected Medium() {
        this.title = "These aren't the Droids you are looking for!";
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
