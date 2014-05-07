package de.fau.mad.sqlite;

import android.provider.BaseColumns;

public class KoboldDBContract {

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public KoboldDBContract() {}

        /* Inner class that defines the table contents */
        public static abstract class KoboldEntry implements BaseColumns {
            public static final String TABLE_NAME = "koboldentry";
            public static final String COLUMN_NAME_ENTRY_ID = "koboldentryid";
            public static final String COLUMN_NAME_FEATURE = "feature";
            public static final String COLUMN_NAME_VALUE = "value";
            public static final String COLUMN_NAME_DESCRIPTION = "description";
        }
}
