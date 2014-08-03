package de.fau.cs.mad.gamekobold.character;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
	  private Context mContext;
	  private String[][] mContents;
	  private String[] mTitles;
	  
	  public CustomExpandableListAdapter(Context context, String[] titles, String[][] contents) {
	    super();
	    if(titles.length != contents.length) {
	      throw new IllegalArgumentException("Titles and Contents must be the same size.");
	    }
	    
	    mContext = context;
	    mContents = contents;
	    mTitles = titles;
	  }
	  @Override
	  public String getChild(int groupPosition, int childPosition) {
	    return mContents[groupPosition][childPosition];
	  }
	  @Override
	  public long getChildId(int groupPosition, int childPosition) {
	    return 0;
	  }
	  @Override
	  public View getChildView(int groupPosition, int childPosition,
			  boolean isLastChild, View convertView, ViewGroup parent) {
		  if (convertView == null) {
			  LayoutInflater infalInflater = (LayoutInflater) mContext
					  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  convertView = infalInflater.inflate(R.layout.table_view_child_item,
					  null);
		  }
		  TextView item = (TextView) convertView.findViewById(R.id.table_view_item);
		  item.setText(mContents[groupPosition][childPosition]);
		  //		    row.setTextSize(R.dimen.text_large);
		  return convertView;
	  }
	  
	  @Override
	  public int getChildrenCount(int groupPosition) {
	    return mContents[groupPosition].length;
	  }
	  @Override
	  public String[] getGroup(int groupPosition) {
	    return mContents[groupPosition];
	  }
	  @Override
	  public int getGroupCount() {
	    return mContents.length;
	  }
	  @Override
	  public long getGroupId(int groupPosition) {
	    return 0;
	  }
	  @Override
	  public View getGroupView(int groupPosition, boolean isExpanded,
	      View convertView, ViewGroup parent) {
	    if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.table_view_group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.rowName);
	    item.setTypeface(Typeface.DEFAULT_BOLD);
	    item.setPaintFlags(item.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
	    item.setText(mTitles[groupPosition]);
//	    row.setTextSize(R.dimen.text_large);
	    return convertView;
	  }

	  @Override
	  public boolean hasStableIds() {
	    return false;
	  }

	  @Override
	  public boolean isChildSelectable(int groupPosition, int childPosition) {
	    return true;
	  }

	}