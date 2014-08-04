package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxRandomElementAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<String> textViewValues;
    LayoutInflater inflater;
    
    public ToolboxRandomElementAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.textViewValues = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView;

        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_game_toolbox_random_cell, null);
     
        TextView textView = (TextView) convertView.findViewById(R.id.grid_item);
        textView.setText(textViewValues.get(position));
        textView.setHint(textViewValues.get(position));

        return convertView;
    }
    
    @Override
    public int getCount() {
        return textViewValues.size();
    }

    @Override
    public Object getItem(int position) {
        return textViewValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
  