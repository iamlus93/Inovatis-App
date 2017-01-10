package com.inovatis.inovatis.notepad;

/**
 * Created by Lud' on 21/10/2016.
 */
import android.provider.BaseColumns;


public final class PersistenceContract {

    //private constructor prevents instantiating the contract class
    private PersistenceContract() {}

    //Inner class that defines the database table column names
    public static abstract class NoteEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "note";
        public static final String KEY_TITLE = "title";
        public static final String KEY_TEXT = "text";
        public static final String KEY_DATE = "date";
    }
}
