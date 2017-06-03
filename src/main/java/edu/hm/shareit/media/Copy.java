package edu.hm.shareit.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An exemplar of a medium bound to an owner.
 */
@Entity
@Table(name = "TCopy")
public class Copy {

    @Column(name = "medium")
    private final Medium medium;
    @Column(name = "owner")
    private final String owner;

    /**
     * Creates an exemplar of a medium and is bound to an owner.
     *
     * @param medium The medium
     * @param owner  The owner
     */
    public Copy(Medium medium, String owner) {
        if (medium == null || owner == null) {
            throw new NullPointerException();
        }
        this.medium = medium;
        this.owner = owner;
    }

    /**
     * Getter for medium.
     *
     * @return medium
     */
    public Medium getMedium() {
        return medium;
    }

    /**
     * Getter for owner.
     *
     * @return owner
     */
    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Copy{"
                + "medium=" + medium
                + ", owner='" + owner + '\''
                + '}';
    }
}