package ie.tcd.cs7cs3.undersvc.resources;


import ie.tcd.cs7cs3.undersvc.core.UserRating;
import ie.tcd.cs7cs3.undersvc.db.UserRatingDAO;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class UserRatingResourceTest {
    private static final UserRatingDAO USER_RATING_DAO = mock(UserRatingDAO.class);
    private static final ResourceExtension RESOURCE_EXTENSION = ResourceExtension.builder()
            .addResource(new UserRatingResource(USER_RATING_DAO))
            .build();
    private static UserRating dummyUserRating;
    private ArgumentCaptor<UserRating> value = ArgumentCaptor.forClass(UserRating.class);

    @BeforeEach
    public void setUp() {
        dummyUserRating = new UserRating();
        dummyUserRating.setId(1);
        dummyUserRating.setUuid(UUID.fromString("cafebabe-dead-beef-babe-cafecafecafe"));
        dummyUserRating.setRating(3);

    }

    @AfterEach
    public void tearDown() { reset(USER_RATING_DAO); }

    @Test
    public void testHandleUserRatingGetByMemberId() {
        UUID uuid = dummyUserRating.getUuid();
        when(USER_RATING_DAO.getRatingById(uuid)).thenReturn(dummyUserRating.getRating());

        String path = "/ratings/" + uuid.toString();
        final WebTarget tgt = RESOURCE_EXTENSION.target(path);
        final double resp = tgt.request().get(new GenericType<Double>() {
        });
        verify(USER_RATING_DAO).getRatingById(uuid);
//        System.out.println("this is RESPONSE");
//        System.out.println(resp);
        assertThat(resp).isEqualTo(dummyUserRating.getRating());

    }

    @Test
    public void testHandleUserRatingUpdateByMemberId() {
        final UserRating expected = dummyUserRating;
        doNothing().when(USER_RATING_DAO).insertRatingsById(dummyUserRating.getUuid(),
                dummyUserRating.getRating());
        String path = "/ratings/" + dummyUserRating.getUuid().toString() + "?rating=" + dummyUserRating.getRating();
        final WebTarget tgt = RESOURCE_EXTENSION.target(path);
        // no extra parameters passed do a get request
        final Response resp = tgt.request().get();
        System.out.println(resp);
        System.out.println("Is response");
        // if the member is not found
        assertThat(resp.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);

    }

}
