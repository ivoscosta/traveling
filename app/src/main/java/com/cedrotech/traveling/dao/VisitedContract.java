package com.cedrotech.traveling.dao;

import android.provider.BaseColumns;

/**
 * Created by ivo on 06/06/16.
 */
public final class VisitedContract {

    public VisitedContract() {
    }

    public static abstract class Country implements BaseColumns {
        public static final String TABLE_NAME = "visited_country";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ISO = "iso";
        public static final String COLUMN_SHORTNAME = "shortname";
        public static final String COLUMN_LONGNAME = "longname";
        public static final String COLUMN_CALLINGCODE = "callingcode";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_CULTURE = "culture";
        public static final String COLUMN_VISITED_DATE = "visited_date";
    }
}