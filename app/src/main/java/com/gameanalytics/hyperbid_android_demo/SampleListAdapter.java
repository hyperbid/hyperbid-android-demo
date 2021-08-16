package com.gameanalytics.hyperbid_android_demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class SampleListAdapter extends ArrayAdapter<String> {
    public static class Entry {
        private String      _name;      // the name of the sample
        private Bitmap      _image;     // the logo used for the sample

        public Entry(String name, Bitmap image) {
            _name       = name;
            _image      = image;
        }

        /*accessors*/
        public String getName() {
            return _name;
        }

        public Bitmap getImage() {
            return _image;
        }
    }

    private Activity            _context;
    private ArrayList<Entry>    _entries;

    /*
     * constructor inherited from the Adapter class
     * param0 is the current context for the view
     * param1 is the names of the current entries
     * */
    public SampleListAdapter(Activity context, String[] list) {
        super(context, 0, list);
        _context = context;
        _entries = new ArrayList<>();
    }

    /*adds a new entry to the list*/
    public void addEntry(Entry e) {
        _entries.add(e);
    }

    /*self-explanatory*/
    public Entry getEntry(int id) {
        return id < _entries.size() ? _entries.get(id) : null;
    }

    /*sets the current entry view*/
    @Override
    public View getView(int entryId, View view, ViewGroup parent) {
        View currentView = _context.getLayoutInflater().inflate(R.layout.sample_view_entry, null, true);

        TextView  entryNameView = (TextView)currentView.findViewById(R.id.sample_name);
        //ImageView entryIconView = (ImageView)currentView.findViewById(R.id.sample_icon);

        if(entryId < _entries.size()) /* unlikely to be false */ {
            Entry entry = _entries.get(entryId);
            entryNameView.setText(entry.getName());
            //entryIconView.setImageBitmap(entry.getImage());
        }
        else {
            Log.e("", "Invalid entryId: " + entryId);
        }

            return currentView;
        }
    }
