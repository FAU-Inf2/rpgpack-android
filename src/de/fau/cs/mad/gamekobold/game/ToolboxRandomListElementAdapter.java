package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxRandomListElementAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<String> textViewItems;
    LayoutInflater inflater;
    
    public ToolboxRandomListElementAdapter(Context context, ArrayList<String> names) {
        this.context = context;
        this.textViewItems = names;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_game_toolbox_random_cell, null);
     
        
        TextView textView = (TextView) convertView.findViewById(R.id.listview_item);
        
        textView.setTextSize(30);
        textView.setText(textViewItems.get(position));
        /*
        switch (Integer.parseInt((String) textView.getHint())){
        case 4: 	textView.setBackgroundResource(R.drawable.d4_128x128);
        			break;
        case 6: 	textView.setBackgroundResource(R.drawable.d6_128x128);
        			break;
        case 8: 	textView.setBackgroundResource(R.drawable.d8_128x128);
        			break;
        case 10: 	textView.setBackgroundResource(R.drawable.d10_128x128);
        			break;
        case 12: 	textView.setBackgroundResource(R.drawable.d12_128x128);
        			break;
        case 20: 	textView.setBackgroundResource(R.drawable.d20_128x128);
        			break;
        default: 	textView.setBackgroundResource(R.color.background_green);   
        }
        
	*/
        return convertView;
    }
    
    @Override
    public int getCount() {
        return textViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return textViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
  