package ie.tcd.cs7cs3.undersvc.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * GeometryUtils holds static utility methods for doing stuff with geometry.
 */
public class GeometryUtils {
    /**
     * WKT2MultiPoint is a convenience method for turning a well-known-text representation of a MultiPoint Geometry
     * to an object you can actually do useful stuff with.
     * @param wkt the Well-Known-Text representation of the MultiPoint
     * @return {@link org.locationtech.jts.geom.MultiPoint}
     * @throws ParseException if the WKT representation is not valid.
     */
    public static MultiPoint WKT2MultiPoint(final String wkt) throws ParseException {
        final WKTReader r = new WKTReader();
        final Geometry g = r.read(wkt);
        return (MultiPoint) g;
    }
}
