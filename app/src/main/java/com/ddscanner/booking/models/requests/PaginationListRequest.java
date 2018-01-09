package com.ddscanner.booking.models.requests;


import java.util.HashMap;
import java.util.Map;

public class PaginationListRequest extends HashMap<String, Object> {

    private static final String KEY_PAGE = "page";
    private static final String KEY_LIMIT = "limit";

    public void setKeyPage(int page) {
        put(KEY_PAGE, page);
    }

    public void setLimit(int limit) {
        put(KEY_LIMIT, limit);
    }

    public void putSouthWestLat(double lat) {
        put("lat_bottom", String.valueOf(lat));
    }

    public void putSouthWestLng(double lng) {
        put("lng_left", String.valueOf(lng));
    }

    public void putNorthEastLat(double lat) {
        put("lat_top", String.valueOf(lat));
    }

    public void putNorthEastLng(double lng) {
        put("lng_right", String.valueOf(lng));
    }

    public double getSouthWestLat() {
        return Double.valueOf((String) get("lat_bottom"));
    }

    public double getSouthWestLng() {
        return Double.valueOf((String) get("lng_left"));
    }

    public double getNorthEastLat() {
        return Double.valueOf((String) get("lat_top"));
    }

    public double getNorthEastLng() {
        return Double.valueOf((String) get("lng_right"));
    }

}
