package ie.tcd.cs7cs3.undersvc.client;

import ie.tcd.cs7cs3.undersvc.api.GroupResponse;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * UnderHTTPClient is a HTTP client for talking to undersvc.
 */
public class UnderHTTPClient {
    private Client client;
    private String scheme;
    private String host;
    private int port;

    /**
     * UnderHTTPClient creates a new instance of UnderHTTPClient.
     * By default, this talks to https://localhost:8080
     */
    public UnderHTTPClient() {
        this.client = ClientBuilder.newClient();
        this.scheme = "https://";
        this.host = "localhost";
        this.port = 8080;
    }

    /**
     * withHost sets the host of the client to the given hostname.
     * @param host the required hostname
     * @return UnderHTTPClient
     */
    public UnderHTTPClient withHost(final String host) {
        this.host = host;
        return this;
    }

    /**
     * withPort sets the port of the client to the given port.
     * @param port the required port
     * @return UnderHTTPClient
     */
    public UnderHTTPClient withPort(final int port) {
        this.port = port;
        return this;
    }

    /**
     * insecure sets the scheme of the client to http:// (plain-text)
     * @return UnderHTTPClient
     */
    public UnderHTTPClient insecure() {
        this.scheme = "http://";
        return this;
    }

    /**
     * base returns the base target
     * @return the base webtarget
     */
    private WebTarget base() {
        return client.target(String.format("%s%s:%d", scheme, host, port));
    }

    /**
     * groups returns the webtarget for the groups resource
     * @return the groups webtarget
     */
    private WebTarget groups() {
        return base().path("/groups");
    }

    /**
     * group returns the webtarget for the given group
     * @return the group webtarget
     */
    private WebTarget group(final long groupID) {
        return base().path(String.format("groups/%d", groupID));
    }

    /**
     * listGroups returns a list of all the known groups.
     * @return a list of known groups
     */
    public List<GroupResponse> listGroups() {
        final Invocation.Builder invocationBuilder = groups().request(MediaType.APPLICATION_JSON_TYPE);
        return invocationBuilder.get(new GenericType<List<GroupResponse>>() {});
    }

    /**
     * getGroup gets an existing group by ID
     * @param groupID the groupID to fetch
     * @return the existing GroupResponse.
     */
    public GroupResponse getGroup(final long groupID) throws UnderHTTPClientException {
        final Invocation.Builder invocationBuilder = group(groupID).request(MediaType.APPLICATION_JSON_TYPE);
        final Response resp = invocationBuilder.get();
        bailOnBadResponse(resp);
        return resp.readEntity(GroupResponse.class);
    }

    /**
     * createGroup creates a new group
     * @param g the group to be created. ID parameter is ignored for obvious reasons.
     * @return the created GroupResponse.
     */

    public GroupResponse createGroup(final GroupResponse g) throws UnderHTTPClientException {
        final Invocation.Builder invocationBuilder = groups().request(MediaType.APPLICATION_JSON_TYPE);
        final Response resp = invocationBuilder.post(Entity.entity(g, MediaType.APPLICATION_JSON));
        bailOnBadResponse(resp);
        return resp.readEntity(GroupResponse.class);
    }

    /**
     * updateGroup updates a group a new group
     * @param g the group to be updated.
     * @return the created GroupResponse.
     */
    public GroupResponse updateGroup(final GroupResponse g) throws UnderHTTPClientException {
        final Invocation.Builder invocationBuilder = group(g.getGroupId()).request(MediaType.APPLICATION_JSON_TYPE);
        final Response resp = invocationBuilder.post(Entity.entity(g, MediaType.APPLICATION_JSON));
        bailOnBadResponse(resp);
        return resp.readEntity(GroupResponse.class);
    }

    void bailOnBadResponse(final Response resp) throws UnderHTTPClientException {
        if (resp.getStatus() != 200) {
            throw new UnderHTTPClientException(String.format("expected 200 OK response but got %d\nbody:%s", resp.getStatus(), resp.readEntity(String.class)));
        }
    }
}
