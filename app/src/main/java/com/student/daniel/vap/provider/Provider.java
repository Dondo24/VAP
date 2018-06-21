package com.student.daniel.vap.provider;

import android.provider.BaseColumns;
    public interface Provider {
        public interface VAP extends BaseColumns {
            public static final String TABLE_NAME = "vap";
            public static final String TYPE = "type";
            public static final String AMOUNT = "amount";
            public static final String DESCRIPTION = "description";
            public static final String TIMESTAMP = "timestamp";
        }
    }

