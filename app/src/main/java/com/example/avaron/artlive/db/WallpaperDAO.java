package com.example.avaron.artlive.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaron on 28/06/2016.
 */
public class WallpaperDAO {
    protected SQLiteDatabase database;
    private DbHelper dbHelper;
    private Context mContext;
    /*private String[] allColumnsWallpaper = {
            DBContract.WallpaperEntry.COLUMN_WORK,
            DBContract.WallpaperEntry.COLUMN_AUTHOR,
            DBContract.WallpaperEntry.COLUMN_STYLE,
            DBContract.WallpaperEntry.COLUMN_DESCRIPTION,
            DBContract.WallpaperEntry.COLUMN_THUMBNAIL,
            DBContract.WallpaperEntry.COLUMN_FILENAME
    };*/

    public WallpaperDAO(Context context) {
        this.mContext = context;
        dbHelper = new DbHelper(context);
    }

    public void open() {
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
            throw new Error("Unable to create database");
        }
        try {
            dbHelper.openDataBase();
        }catch(SQLiteException sqle){
            System.out.println(sqle.toString());
            throw sqle;
        }
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public List<Wallpaper> getWallpaperQuery(String query) {
        List<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
        Wallpaper wallpaper = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(query, null);
        }catch(SQLiteException sqle) {
            System.out.println(sqle.toString());
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            wallpaper = cursorToWallpaper(cursor);
            wallpapers.add(wallpaper);
        }
        cursor.close();
        return wallpapers;
    }

    public Wallpaper getWallpaperById(long wallpaperId) {
        List<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
        Wallpaper wallpaper = null;
        Cursor cursor = null;
        String query = "SELECT * FROM "+DBContract.WallpaperEntry.TABLE_WALLPAPER+" WHERE "+DBContract.WallpaperEntry._ID+" = "+wallpaperId;
        try {
            cursor = database.rawQuery(query, null);
        }catch(SQLiteException sqle) {
            System.out.println(sqle.toString());
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            wallpaper = cursorToWallpaper(cursor);
            wallpapers.add(wallpaper);
        }
        wallpaper = wallpapers.get(0);
        cursor.close();
        return wallpaper;
    }

    public Author getAuthorById(long authorId) {
        List<Author> authors = new ArrayList<Author>();
        Author author = null;
        Cursor cursor = null;
        String query = "SELECT * FROM "+DBContract.AuthorEntry.TABLE_NAME+" WHERE "+DBContract.AuthorEntry._ID+" = "+authorId;
        try {
            cursor = database.rawQuery(query, null);
        }catch(SQLiteException sqle) {
            System.out.println(sqle.toString());
        }
        if(cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                author = cursorToAuthor(cursor);
                authors.add(author);
            }
            author = authors.get(0);
        }
        cursor.close();
        return author;
    }

    public Style getStyleById(long styleId) {
        List<Style> styles = new ArrayList<Style>();
        Style style = null;
        Cursor cursor = null;
        String query = "SELECT * FROM "+DBContract.StyleEntry.TABLE_NAME+" WHERE "+DBContract.StyleEntry._ID+" = "+styleId;
        try {
            cursor = database.rawQuery(query, null);
        }catch(SQLiteException sqle) {
            System.out.println(sqle.toString());
        }
        if(cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                style = cursorToStyle(cursor);
                styles.add(style);
            }
            style = styles.get(0);
        }
        cursor.close();
        return style;
    }

    public List<Wallpaper> getAllWallpapers() {
        List<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
        Wallpaper wallpaper = null;
        Cursor cursor = null;
        try {
//            cursor = database.query(DBContract.WallpaperEntry.TABLE_WALLPAPER, null, null, null, null, null, null);
            cursor = database.rawQuery("SELECT * FROM wallpaper", null);
        }catch(SQLiteException sqle) {
            System.out.println(sqle.toString());
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            wallpaper = cursorToWallpaper(cursor);
            wallpapers.add(wallpaper);
        }
        cursor.close();
        return wallpapers;
    }

    private Wallpaper cursorToWallpaper(Cursor cursor) {
        Wallpaper wallpaper = new Wallpaper();
        wallpaper.setId(cursor.getLong(0));
        wallpaper.setWorkName(cursor.getString(1));
        wallpaper.setAuthor(cursor.getString(2));
        wallpaper.setDescription(cursor.getString(3));
        wallpaper.setThumbNail(cursor.getString(4));
        wallpaper.setFileName(cursor.getString(5));
        wallpaper.setAuthorId(cursor.getLong(6));
        return wallpaper;
    }

    private Author cursorToAuthor(Cursor cursor) {
        Author author = new Author();
        author.setId(cursor.getLong(0));
        author.setName(cursor.getString(1));
        author.setBiography(cursor.getString(2));
        author.setStyleId(cursor.getLong(3));
        return author;
    }

    private Style cursorToStyle(Cursor cursor) {
        Style style = new Style();
        style.setId(cursor.getLong(0));
        style.setName(cursor.getString(1));
        style.setDescription(cursor.getString(2));
        return style;
    }
}
