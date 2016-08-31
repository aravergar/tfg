package com.example.avaron.artlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.avaron.artlive.R;
import com.example.avaron.artlive.db.Wallpaper;

import java.util.List;

/**
 * Created by Avaron on 21/06/2016.
 */
public class ThumbAdapter extends BaseAdapter {
    private Context context;
//    private int columnWidth;
    private List<Wallpaper> wallpapers;

    public ThumbAdapter(Context c, List<Wallpaper> wallpapers) {
        context = c;
        this.wallpapers = wallpapers;
    }

    public int getCount() {
        return wallpapers.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
//            imageView = new ImageView(context);
            gridView = new GridView(context);

            gridView = inflater.inflate(R.layout.wallpaper_grid, null);

            imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

//            imageView = (ImageView) gridView.findViewById(R.id.grid_image);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(getImageId(wallpapers.get(position).getThumbNail()));
        return imageView;
    }

    public int getImageId(String imageName) {
        return context.getResources().getIdentifier(imageName.substring(0, imageName.lastIndexOf('.')), "drawable", context.getPackageName());
    }
}
