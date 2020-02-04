package ie.tcd.cs7cs3.undersvc.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeometryUtils {
    public static MultiPoint WKT2MultiPoint(final String wkt) throws ParseException {
        final WKTReader r = new WKTReader();
        final Geometry g = r.read(wkt);
        return (MultiPoint) g;
    }
}
