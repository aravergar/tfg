package com.example.avaron.artlive.activities;

/**
 * Created by Avaron on 05/06/2015.
 */

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.example.avaron.artlive.R;
import com.example.avaron.artlive.db.Wallpaper;
import com.example.avaron.artlive.db.WallpaperDAO;
import com.example.avaron.artlive.services.MyWallpaperService;
import com.example.avaron.artlive.adapters.ThumbAdapter;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Wallpaper> values;
    public static String filenames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowTitleEnabled(true);
//        actBar.setLogo(R.drawable.logo);
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);
//        actBar.setHomeAsUpIndicator(R.drawable.logo);
        actBar.setTitle("Fondos Artísticos");

        GridView gridView = (GridView) findViewById(R.id.gridview);


        WallpaperDAO wallpaperDAO = new WallpaperDAO(this);
        wallpaperDAO.open();

        values = wallpaperDAO.getAllWallpapers();

        gridView.setAdapter(new ThumbAdapter(this, values));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Wallpaper wallpaper = values.get(position);
                if(wallpaper!=null) {
                    if(wallpaper.getFileName()!=null) {
                        filenames = wallpaper.getFileName();
                        launchWallpaperActivity(wallpaper.getId());
                    }
                }
            }
        });

        wallpaperDAO.close();
    }

    public String getFilenames() {
        return filenames;
    }

    public List<Wallpaper> getValues() {
        return values;
    }

    public void launchWallpaperActivity(long wallpaperId) {
        Intent wallpaperActivity = new Intent(this, WallpaperActivity.class);
        wallpaperActivity.putExtra("_id",wallpaperId);
        startActivity(wallpaperActivity);
    }

    public void testWallpaper(View view) {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, MyWallpaperService.class));

        startActivity(intent);
    }

    public void onWallpaperSettings(View view) {
        Intent wallpaperSettings = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(wallpaperSettings);
    }
}

