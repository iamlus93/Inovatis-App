package com.inovatis.inovatis.notepad;

/**
 * Created by Lud' on 21/10/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class DbHelper extends SQLiteOpenHelper
{
    private SQLiteDatabase mDatabase;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TakeNote.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PersistenceContract.NoteEntry.TABLE_NAME + " (" +
                    PersistenceContract.NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    PersistenceContract.NoteEntry.KEY_TITLE + " TEXT," +
                    PersistenceContract.NoteEntry.KEY_TEXT + " TEXT," +
                    PersistenceContract.NoteEntry.KEY_DATE + " LONG)";


    private static DbHelper mInstance = null;

    //global point of access
    public static synchronized DbHelper getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DbHelper(context.getApplicationContext());
        }
        return mInstance;
    }


    private DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //TODO: implement in next version
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //TODO: implement in next version
    }

    //Select all notes from database
    public ArrayList<Note> getAllNotes()
    {
        ArrayList<Note> allNotes = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM " + PersistenceContract.NoteEntry.TABLE_NAME +
                " ORDER BY " + PersistenceContract.NoteEntry.KEY_DATE + " DESC";

        mDatabase = this.getWritableDatabase();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do
            {
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setText(cursor.getString(2));
                note.setDate(cursor.getLong(3));
                allNotes.add(note);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return allNotes;
    }


    //Insert a single note, returns the generated key
    public long insertNote(Note note)
    {
        ContentValues values = new ContentValues();
        values.put(PersistenceContract.NoteEntry.KEY_TITLE, note.getTitle());
        values.put(PersistenceContract.NoteEntry.KEY_TEXT, note.getText());
        values.put(PersistenceContract.NoteEntry.KEY_DATE, note.getLongDate());

        mDatabase = this.getWritableDatabase();
        long key = mDatabase.insert(PersistenceContract.NoteEntry.TABLE_NAME, null, values);
        mDatabase.close();
        return key;
    }

    //Update a single note
    public int updateNote(Note note)
    {
        ContentValues values = new ContentValues();
        values.put(PersistenceContract.NoteEntry.KEY_TITLE, note.getTitle());
        values.put(PersistenceContract.NoteEntry.KEY_TEXT, note.getText());
        values.put(PersistenceContract.NoteEntry.KEY_DATE, note.getLongDate());

        mDatabase = this.getWritableDatabase();
        int rowsAffected = mDatabase.update(PersistenceContract.NoteEntry.TABLE_NAME, values, PersistenceContract.NoteEntry._ID + " = ?", new String[] { String.valueOf(note.getID()) });
        mDatabase.close();
        return rowsAffected;
    }

    //Delete a single note
    public int deleteNote(long id)
    {
        mDatabase = this.getWritableDatabase();
        int rowsDeleted = mDatabase.delete(PersistenceContract.NoteEntry.TABLE_NAME, PersistenceContract.NoteEntry._ID + " = ?", new String[] { String.valueOf(id) });
        mDatabase.close();
        return rowsDeleted;
    }



}
