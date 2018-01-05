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

}
