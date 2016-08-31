package com.example.avaron.artlive.db;

import android.provider.BaseColumns;

/**
 * Created by Avaron on 27/06/2016.
 */
public final class DBContract {
    public DBContract() {}

    public static abstract class WallpaperEntry implements BaseColumns {
        public static final String TABLE_WALLPAPER = "wallpaper";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_WORK = "work";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_FILENAME = "filename";
        public static final String COLUMN_AUTHOR_ID = "author_id";
    }

    public static abstract class AuthorEntry implements BaseColumns {
        public static final String TABLE_NAME = "author";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME_AUTHOR = "name";
        public static final String COLUMN_BIO = "biography";
        public static final String COLUMN_STYLE = "style_id";
    }

    public static abstract class StyleEntry implements BaseColumns {
        public static final String TABLE_NAME = "style";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME_STYLE = "name";
        public static final String COLUMN_DESCRIPTION = "description";
    }

}
