package com.example.avaron.artlive.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.avaron.artlive.R;
import com.example.avaron.artlive.db.Style;
import com.example.avaron.artlive.db.WallpaperDAO;

/**
 * Created by Avaron on 10/07/2016.
 */
public class StyleActivity extends AppCompatActivity{
    private long styleId;
    private Style style;
    private TextView styleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.style_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowTitleEnabled(true);
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        styleId = b.getLong("_id");
        WallpaperDAO wallpaperDAO = new WallpaperDAO(this);
        wallpaperDAO.open();
        style = wallpaperDAO.getStyleById(styleId);

        actBar.setTitle(style.getName());

        styleText = (TextView) findViewById(R.id.text_style_description);
        styleText.setText(style.getDescription());

        wallpaperDAO.close();
    }
}
