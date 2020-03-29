package ie.tcd.cs7cs3.undersvc.core;


import javax.persistence.*;
import java.util.UUID;

/**
 * Userr Rating is an entity class for representing all the ratings that a user got,
 * in the particular time frame
 */
@Entity
@Table(name = "user_ratings")
@NamedQueries(
        {
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.UserRating.getRatingsByMemberId",
                        query = "SELECT u.rating FROM UserRating u WHERE u.uuid = :memberID"
                ),
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.UserRating.updateRatingsByMemberId",
                        query = ""
                )
        }
)

public class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "rating")
    private double rating;

    //Constructors
    public UserRating () {}

    public UserRating(final long id, final UUID uuid, final double rating) {
        this.id = id;
        this.rating = rating;
        this.uuid = uuid;
    }

    //Getter n Setter
    public long getId() { return id; }

    public UUID getUuid() { return uuid;}

    public void setId(long id) { this.id = id; }

    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }
}
