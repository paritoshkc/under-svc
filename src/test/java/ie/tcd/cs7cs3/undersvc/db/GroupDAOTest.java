package ie.tcd.cs7cs3.undersvc.db;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static ie.tcd.cs7cs3.undersvc.utils.GeometryUtils.WKT2MultiPoint;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

/**
 * Unit tests for {@link GroupDAO}
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class GroupDAOTest {
    // If you want to run tests against a local MySQL instance, uncomment:
    /**
    public DAOTestExtension dte = DAOTestExtension.newBuilder()
            .setShowSql(true)
            .setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.mysql.MySQL56SpatialDialect")
            .setUrl("jdbc:mysql://localhost:3306/testdb")
            .setUsername("root")
            .setProperty(AvailableSettings.PASS, "password")
            .setDriver(com.mysql.jdbc.Driver.class)
            .addEntityClass(Group.class)
            .addEntityClass(GroupMember.class)
            .addEntityClass(GroupRestriction.class)
            .build();
    **/

    public DAOTestExtension dte = DAOTestExtension.newBuilder()
            .setShowSql(true)
            .setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.h2geodb.GeoDBDialect")
            .setDriver(org.h2.Driver.class)
            .setHbm2DdlAuto("create")
            .addEntityClass(Group.class)
            .addEntityClass(GroupMember.class)
            .addEntityClass(GroupRestriction.class)
            .build();

    private GroupDAO groupDAO;

    private List<Group> dummyGroups;

    @BeforeEach
    public void setUp() throws Exception {
        final MultiPoint p = WKT2MultiPoint("MULTIPOINT((37.516455 126.721757))");
        final Group g = new Group();
        final GroupRestriction r1 = new GroupRestriction();
        final GroupMember m1 = new GroupMember();
        g.setCreationTimestamp(0L);
        g.setDepartureTimestamp(1000L);
        g.setState("FORMING");
        g.setPoints(p);
        g.addGroupMember(m1);
        g.addGroupRestriction(r1);
        m1.setUuid(UUID.fromString("deadbeef-dead-beef-dead-deadbeefdead"));
        m1.setGroup(g);
        r1.setType("MaxMembers");
        r1.setValue(3);
        r1.setGroup(g);
        groupDAO = new GroupDAO(dte.getSessionFactory());
        groupDAO.create(g);
        dummyGroups = ImmutableList.of(g);
    }

    @Test
    public void testCreate() {
        final Optional<Group> actual = groupDAO.findById((long) 1);
        assertThat(actual.isPresent());
        assertThat(actual.get()).isEqualTo(dummyGroups.get(0));
    }

    @Test
    public void testFindAll() {
        final List<Group> actual = groupDAO.findAll();
        assertThat(actual).isEqualTo(dummyGroups);
    }

    @Test
    public void testDeleteGroupMemberByUUID() {
        final UUID toDelete = dummyGroups.get(0).getGroupMembers().get(0).getUuid();
        final long groupID = dummyGroups.get(0).getId();
        groupDAO.deleteMemberByUuid(groupID, ImmutableList.of(toDelete));
        final Group updated = groupDAO.findById(groupID).get();
        assertThat(updated.getGroupMembers()).isEmpty();
    }

    @Test
    @Disabled
    public void ensureCreationBeforeDeparture() {
        // TODO: implement
        fail("implement me");
    }

    @Test
    @Disabled
    public void testUpdateGroup() {
        fail("implement me");
    }
}
