package com.example.avaron.artlive.activities;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avaron.artlive.R;
import com.example.avaron.artlive.db.Author;
import com.example.avaron.artlive.db.Wallpaper;
import com.example.avaron.artlive.db.WallpaperDAO;
import com.example.avaron.artlive.services.MyWallpaperService;

/**
 * Created by Avaron on 08/07/2016.
 */
public class WallpaperActivity extends AppCompatActivity {
    private long wallpaperId;
    private Wallpaper wallpaper;
    private Author author;
    private ImageView imageView;
    private TextView descriptionText;
    private TextView authorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wallpaper_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowTitleEnabled(true);
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        wallpaperId = b.getLong("_id");
        WallpaperDAO wallpaperDAO = new WallpaperDAO(this);
        wallpaperDAO.open();
        wallpaper = wallpaperDAO.getWallpaperById(wallpaperId);
        author = wallpaperDAO.getAuthorById(wallpaper.getAuthorId());
        imageView = (ImageView) findViewById(R.id.wallpaper_image);
        String thumb = wallpaper.getThumbNail();
        int id = getResources().getIdentifier(thumb.substring(0, thumb.lastIndexOf('.')), "drawable", "com.example.avaron.artlive");

        imageView.setImageResource(id);
        actBar.setTitle(wallpaper.getWorkName());

        descriptionText = (TextView) findViewById(R.id.text_work_description);
        descriptionText.setText(wallpaper.getDescription());

        authorText = (TextView) findViewById(R.id.text_work_author);
        authorText.setText(wallpaper.getAuthor());

        imageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wallpaper!=null) {
                    launchWallpaper();
                }
            }
        });

        wallpaperDAO.close();
    }

    public void launchWallpaper() {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, MyWallpaperService.class));
        startActivity(intent);
    }

    public void launchAuthorActivity(View view) {
        Intent authorActivity = new Intent(this, AuthorActivity.class);
        authorActivity.putExtra("_id",wallpaper.getAuthorId());
        startActivity(authorActivity);
    }
}
