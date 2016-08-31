package com.example.avaron.artlive.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Avaron on 28/06/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
//    private static String DB_PATH = "/data/data/com.example.avaron.artlive/databases/";
    private static String DB_PATH;
    private static String DB_NAME = "Wallpaper.db";
    private final Context mContext;

    private SQLiteDatabase database;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
/*    private static final String SQL_CREATE_WALLPAPER =
            "CREATE TABLE " + DBContract.WallpaperEntry.TABLE_WALLPAPER + " (" +
                    DBContract.WallpaperEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.WallpaperEntry.COLUMN_WORK + TEXT_TYPE + COMMA_SEP +
                    DBContract.WallpaperEntry.COLUMN_AUTHOR + TEXT_TYPE + COMMA_SEP +
                    DBContract.WallpaperEntry.COLUMN_STYLE + TEXT_TYPE + COMMA_SEP +
                    DBContract.WallpaperEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DBContract.WallpaperEntry.COLUMN_THUMBNAIL + TEXT_TYPE + COMMA_SEP +
                    DBContract.WallpaperEntry.COLUMN_FILENAME + TEXT_TYPE +


    // Any other options for the CREATE commandal
            " )";*/

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.WallpaperEntry.TABLE_WALLPAPER;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "Wallpaper.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
//            this.getWritableDatabase();
            SQLiteDatabase db = this.getWritableDatabase();

            if (db.isOpen()){
                db.close();
            }
        }
        dbExist = checkDataBase();
        if(!dbExist){
            this.getWritableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                e.toString();
//                throw new Error("Error copying database");
            }
        }
        this.close();
    }
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            e.toString();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    private void copyDataBase() throws IOException{
        InputStream myInput = null;
        try{
            myInput = mContext.getResources().getAssets().open(DB_NAME);
        }catch(IOException e) {
            e.toString();
        }

        String outFileName = DB_PATH;
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(outFileName);
        }catch(IOException e){
            e.toString();
        }
        byte[] buffer = new byte[2048];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLiteException{
        //Open the database
        String myPath = DB_PATH;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if(database != null)
            database.close();
        super.close();

    }

    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        if(newVersion > oldVersion) {
            mContext.deleteDatabase(DB_NAME);
        }
        /*db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);*/
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void open() throws SQLiteException {
        database = this.getWritableDatabase();
    }
}
