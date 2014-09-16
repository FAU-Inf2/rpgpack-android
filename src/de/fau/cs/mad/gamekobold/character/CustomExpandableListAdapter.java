package de.fau.cs.mad.gamekobold.character;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ReattachingPopup;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.game.CharacterPlayActivity;
import de.fau.cs.mad.gamekobold.jackson.IEditableContent;
import de.fau.cs.mad.gamekobold.jackson.Row;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData;
import de.fau.cs.mad.gamekobold.template_generator.FolderFragment;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;
import de.fau.cs.mad.gamekobold.template_generator.MyClickableSpan;
import de.fau.cs.mad.gamekobold.template_generator.TableFragment;
import de.fau.cs.mad.gamekobold.template_generator.TableFragment.content_type;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements OnCheckedChangeListener {
	  private Context mContext;
	  private String[][] mContents;
	  private String[] mTitles;
	  private content_type[][] mTypes;
	  private String[] mHeadlines;
	  private Table mJacksonTable;
	  TableFragment mBelongsTo;
	  
	  public CustomExpandableListAdapter(Context context, Table jacksonTable, TableFragment belongsTo,
			  String[] headlines, content_type[][] type, String[] groupNames, String[][] contents) {
	    super();
	    if(groupNames.length != contents.length) {
	      throw new IllegalArgumentException("Titles and Contents must be the same size.");
	    }
	    mTypes = type;
	    mHeadlines = headlines;
	    mContext = context;
	    mContents = contents;
	    mTitles = groupNames;
	    mJacksonTable = jacksonTable;
	    mBelongsTo = belongsTo;
	  }
	  @Override
	  public String getChild(int groupPosition, int childPosition) {
	    return mContents[groupPosition][childPosition];
	  }
	  @Override
	  public long getChildId(int groupPosition, int childPosition) {
	    return 0;
	  }
	  
	  //NOTE: childPosition == columnIndex; groupPosition == rowIndex
	  @Override
	  public View getChildView(final int groupPosition, final int childPosition,
			  boolean isLastChild, View convertView, ViewGroup parent) {
//	  Log.d("CustomExpandableListAdapter", "getChildView!");
		  if (convertView == null) {
			  LayoutInflater infalInflater = (LayoutInflater) mContext
					  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  convertView = infalInflater.inflate(R.layout.table_view_child_item,
					  null);
			  
		  }
		  LinearLayout content = (LinearLayout) convertView.findViewById(R.id.content_keeper);
		  content.removeAllViews();
		  final String headline = mHeadlines[childPosition];
		  if(mTypes[groupPosition][childPosition] == content_type.editText){
			  TextView txt = new TextView(mContext);
			  txt.setText(mContents[groupPosition][childPosition]);
			  content.addView(txt);
		  }
		  else if(mTypes[groupPosition][childPosition] == content_type.popup){
			  //			  TextView txt = new TextView(mContext);
			  //			  txt.setText("...");
			  //			  txt.setTextColor(mContext.getResources().getColor(R.color.green));
			  LinearLayout popup = initPopup(headline, mJacksonTable.getEntry(childPosition, groupPosition), childPosition, groupPosition);
//			  popup.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//			  int popupHeight = popup.getMeasuredHeight();
//			  item.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//			  int itemHeight = popup.getMeasuredHeight();
//			  if(popupHeight < itemHeight){
//				  LayoutParams popupParams = popup.getLayoutParams();
//				  LayoutParams p = new LinearLayout.LayoutParams(popupParams.width, itemHeight);
//				  popup.setLayoutParams(p);
//			  }
			  LayoutParams p = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
			  content.setLayoutParams(p);
//			  int paddingBottom = content.getPaddingBottom();
//			  int paddingRight = content.getPaddingRight();
//			  int paddingTop = content.getPaddingTop();
//			  content.setPadding(android.R.attr.expandableListPreferredChildPaddingLeft, paddingTop, paddingRight, paddingBottom);
			  content.addView(popup);
			  
//			  LayoutParams p = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
//			  popup.setLayoutParams(p);
			  content = popup;

			  //			  content = popup;
		  }
		  else if(mTypes[groupPosition][childPosition] == content_type.checkbox){
			  CheckBox cb = new CheckBox(mContext);
			  cb.setButtonDrawable(R.drawable.custom_checkbox);
			  final IEditableContent jacksonEntry =
					  mJacksonTable.getEntry(childPosition, groupPosition);
			  if(jacksonEntry != null) {
				  // sets the onCheckedChangeListener
				  // this is needed so we can take over the changes to our jackson model
				  cb.setOnCheckedChangeListener(this);
				  // sets the associated jackson row to this 
				  cb.setTag(R.id.jackson_row_tag_id, jacksonEntry);
				  // set checked state to jackson state
				  cb.setChecked(Boolean.parseBoolean(jacksonEntry.getContent()));
			  }
			  content.addView(cb);
		  }
		  final TextView item = (TextView) convertView.findViewById(R.id.table_view_item);
//		  item.addTextChangedListener(new TextWatcher(){
//				public void afterTextChanged(Editable s) {
//					resizePopup(item, convView);
//				}
//				public void beforeTextChanged(CharSequence s, int start, int count, int after){
//				}
//				public void onTextChanged(CharSequence s, int start, int before, int count){
//				}
//			});
		  item.setText(headline + ":");
		  final View convView = convertView;
		  
//			resizePopup(convView, convView);
		  //TODO: further cases and positioning + onClickListener
		  //		  item.setText(mContents[groupPosition][childPosition]);
		  //		    row.setTextSize(R.dimen.text_large);
		  return convView;
	  }
	  
//	  protected void resizePopup(View headlineView, View containingView) {
////		  Log.d("CustomExpandableListAdapter", "resizing!");
//		  LinearLayout content = (LinearLayout) containingView.findViewById(R.id.content_keeper);
//		  headlineView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		  int headlineHeight = headlineView.getMeasuredHeight();
//		  content.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		  int contentHeight = content.getMeasuredHeight();
//		  int contentWidth = content.getMeasuredWidth();
//		  Log.d("CustomExpandableListAdapter", "contentHeight ==" + contentHeight);
//		  Log.d("CustomExpandableListAdapter", "headlineHeight ==" + headlineHeight);
//		  if(contentHeight < headlineHeight){
//			  Log.d("CustomExpandableListAdapter", "resizing!");
////			  LayoutParams popupParams = popup.getLayoutParams();
//			  LayoutParams p = new LinearLayout.LayoutParams(contentWidth, headlineHeight);
//			  content.setLayoutParams(p);
//			  View child = content.getChildAt(0);
//			  if(child != null){
//				  Log.d("CustomExpandableListAdapter", "resizing child");
//				  child.setLayoutParams(p);
//			  }
//		  }
//	  }
	  
	  
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
	  public View getGroupView(final int groupPosition, boolean isExpanded,
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
	    final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.group_checkbox);
	    final CheckBox favorite = (CheckBox) convertView.findViewById(R.id.favorite_checkbox);
	    if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterPlayActivity){
	    	checkbox.setVisibility(View.INVISIBLE);
	    }
	    else if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity){
	    	favorite.setVisibility(View.INVISIBLE);
	    }
	    final Row jacksonRow = mJacksonTable.getRow(groupPosition);
	    //important: remove listener before setting -> else listener will be called
	    checkbox.setOnCheckedChangeListener(null);
	    checkbox.setChecked(jacksonRow.isSelected());
	    favorite.setOnCheckedChangeListener(null);
	    favorite.setChecked(jacksonRow.isFavorite());
		Log.d("CUSTOM EXP ADAPTER", "groupNumber: " + groupPosition + "; fav: " +
				jacksonRow.isFavorite() + ", checked: "	+ jacksonRow.isSelected());
	    checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//	    	final Row mRow = jacksonRow;
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				mRow.setSelected(isChecked);
				Log.d("CUSTOM EXP ADAPTER", "groupNumber: " + groupPosition + "checkbon: " +
						isChecked);
				mJacksonTable.getRow(groupPosition).setSelected(isChecked);
				checkbox.setSelected(isChecked);
				if(!isChecked){
					favorite.setChecked(false);
//					mRow.setFavorite(false);
					mJacksonTable.getRow(groupPosition).setFavorite(false);
				}
//				Log.d("CUSTOM EXP ADAPTER", "onCheckedChange:"+isChecked);
			}
		});
	    favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//	    	final Row mRow = jacksonRow;
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				mRow.setFavorite(isChecked);
				mJacksonTable.getRow(groupPosition).setFavorite(isChecked);
				favorite.setSelected(isChecked);
				if(isChecked){
					checkbox.setChecked(true);
//					mRow.setSelected(true);
					mJacksonTable.getRow(groupPosition).setSelected(true);
				}
			}
		});
        
	    return convertView;
	  }

	  @Override
	  public boolean hasStableIds() {
	    return true;
	  }

	  @Override
	  public boolean isChildSelectable(int groupPosition, int childPosition) {
	    return true;
	  }
	  
	  /**
	   *  This is called when a checkbox changed its state.
	   */
	  @Override
	  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		  // get the associated entry
		  IEditableContent entry = (IEditableContent) buttonView.getTag(R.id.jackson_row_tag_id);
		  // set the new value
		  entry.setContent(String.valueOf(isChecked));
	  }


	  int styleStart = 0;
	  int cursorLoc = 0;

	  private LinearLayout initPopup(final String headline, final IEditableContent jacksonEntry, final int columnIndex, final int rowIndex){
//		  Log.d("CUSTOM_EXPANDABLE_LIST_ADAPTER", "init_popup");

		  final LinearLayout ll = new LinearLayout(mContext);
		  final TextView newElement = new TextView(mContext);
		  //
		  // JACKSON START
		  //
		  //temporarily disabled, have to take a closer look to popups
		  //			if(jacksonEntry != null) {
		  //				newElement.setText(jacksonEntry.toString());
		  //				newElement.addTextChangedListener(new TextWatcher() {
		  //					final IEditableContent myJacksonEntry = jacksonEntry;
		  //					// callback
		  //					public void afterTextChanged(Editable s) {
		  //						// guard. only set column title if we are not currently inflating with data
		  //						if(!jacksonInflateWithData) {
		  //							myJacksonEntry.setContent(s.toString());
		  //						}
		  //					}
		  //					public void beforeTextChanged(CharSequence s, int start, int count, int after){
		  //					}
		  //					public void onTextChanged(CharSequence s, int start, int before, int count){
		  //					}
		  //				});
		  //			}
		  //
		  // JACKSON END
		  //
		  final TextView txt = (TextView) newElement;
		  LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  final View popupView = inflater.inflate(R.layout.table_view_popup, null);
		  //	        final View layoutContainingHeadline = (View) popupView.findViewById(R.id.popup_content);
		  final TextView popupHeadline = (TextView) popupView.findViewById(R.id.popup_headline);
		  popupHeadline.setText(headline);
		  final EditText inputPopup = (EditText) popupView.findViewById(R.id.popup_editText);



		  final ToggleButton toggleBold = (ToggleButton) popupView.findViewById(R.id.toggle_bold);
		  toggleBold.setOnClickListener(new Button.OnClickListener() {
			  public void onClick(View v) {
				  int selectionStart = inputPopup.getSelectionStart();
				  styleStart = selectionStart;
				  // following code might be needed if they fix this bug: 
				  // http://code.google.com/p/android/issues/detail?id=62508
				  //but atm text marking in popupwindow doesnt work so we don't need to deal with 
				  //selectionEnd != selectionStart
				  /*
	                int selectionEnd = inputPopup.getSelectionEnd();
	                if (selectionStart > selectionEnd){
	                    int temp = selectionEnd;
	                    selectionEnd = selectionStart;
	                    selectionStart = temp;
	                }
	                if (selectionEnd > selectionStart)
	                {
	                    Spannable str = inputPopup.getText();
	                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

	                    boolean exists = false;
	                    for (int i = 0; i < ss.length; i++) {
	                        if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
	                            str.removeSpan(ss[i]);
	                            exists = true;
	                        }
	                    }
	                    if (!exists){
	                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                    }
	                    toggleBold.setChecked(false);
	                }
				   */
			  }
		  });
		  final ToggleButton toggleItalic = (ToggleButton) popupView.findViewById(R.id.toggle_italic);
		  toggleItalic.setOnClickListener(new Button.OnClickListener() {
			  public void onClick(View v) {
				  int selectionStart = inputPopup.getSelectionStart();
				  styleStart = selectionStart;
			  }
		  });
		  final ToggleButton toggleUnderlined = (ToggleButton) popupView.findViewById(R.id.toggle_underline);
		  toggleUnderlined.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		  toggleUnderlined.setOnClickListener(new Button.OnClickListener() {
			  public void onClick(View v) {
				  int selectionStart = inputPopup.getSelectionStart();
				  styleStart = selectionStart;
			  }
		  });


		  inputPopup.addTextChangedListener(new TextWatcher() { 
			  final IEditableContent myJacksonEntry = jacksonEntry;
			  public void afterTextChanged(Editable s) { 
				  int position = Selection.getSelectionStart(inputPopup.getText());
				  if (position < 0){
					  position = 0;
				  }
				  if (position > 0){
					  if (styleStart > position || position > (cursorLoc + 1)){
						  //user changed cursor location, reset
						  styleStart = position - 1;
					  }
					  cursorLoc = position;
					  //	            		Log.d("textstyle", "styleStart == " + styleStart + ", position == " + position);
					  if (toggleBold.isChecked()){
						  StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);
						  for (int i = 0; i < ss.length; i++) {
							  if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
								  s.removeSpan(ss[i]);
							  }
						  }
						  Log.d("CustomExpendableListAdapter", "setting span: bold");
						  s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					  }
					  if (toggleItalic.isChecked()){
						  StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);
						  for (int i = 0; i < ss.length; i++) {
							  if (ss[i].getStyle() == android.graphics.Typeface.ITALIC){
								  s.removeSpan(ss[i]);
							  }
						  }
						  Log.d("CustomExpendableListAdapter", "setting span: italic");
						  s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					  }
					  if (toggleUnderlined.isChecked()){
						  UnderlineSpan[] ss = s.getSpans(styleStart, position, UnderlineSpan.class);
						  for (int i = 0; i < ss.length; i++) {
							  s.removeSpan(ss[i]);
						  }
						  Log.d("CustomExpendableListAdapter", "setting span: underlined");
						  s.setSpan(new UnderlineSpan(), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					  }
					  inputPopup.setText(s);
					  inputPopup.setSelection(position);
					  myJacksonEntry.setContent(s.toString());
				  }

			  }
			  public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
				  //unused
			  } 
			  public void onTextChanged(CharSequence s, int start, int before, int count) { 

			  } 
		  });
	        // TEST
		  if(!jacksonEntry.getContent().isEmpty()) {
			  //	        	inputPopup.setText(jacksonEntry.getContent());
			  inputPopup.setText(jacksonEntry.getContent());
			  if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity){
				  Log.d("CustomExpendableListAdapter", "durchsuche Popup nach Referenzen!");
				  String searchForReferences = inputPopup.getText().toString();
				  Pattern p = Pattern.compile("@");
				  Matcher m = p.matcher(searchForReferences);
				  //following needed for onClick of ClickableSpan to work!
				  inputPopup.setMovementMethod(LinkMovementMethod.getInstance());
				  SpannableStringBuilder span = (SpannableStringBuilder) inputPopup.getText();
				  boolean foundAny = false;
				  while (m.find()){
					  foundAny = true;
					  //	        	    	System.out.print("Start index: " + matcher.start());
					  int startIndex = m.start();
					  int endIndex = startIndex;
					  while(searchForReferences.charAt(endIndex) != ' ' && searchForReferences.charAt(endIndex) != '\n'
							  && searchForReferences.charAt(endIndex) != '\b'){
						  endIndex++;
					  }
//					  String referenceString = searchForReferences.substring(startIndex, endIndex);
//					  SlideoutNavigationActivity.theActiveActivity.getRootFragment().getAllMatrixReferences();
//					  mBelongsTo.getAllMatrixReferences(((SlideoutNavigationActivity) SlideoutNavigationActivity.theActiveActivity).getRootFragment());
					  Log.d("CustomExpandableListAdapter", "Popup: startIndex: " + startIndex + "; endIndex: " + endIndex);
					  Log.d("CustomExpendableListAdapter", "setting span: clickable");
					  span.setSpan(new MyClickableSpan(popupView, mBelongsTo), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					  span.setSpan(new ClickableSpan() {  
//						  @Override
//						  public void onClick(View v) {  
//							  Log.d("MyClickableSpan", "MyClickableSpan -> onClick");
//						  }
//					  }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				  }
				  if(foundAny){
					  inputPopup.setText(span);
				  }
			  }
		  }
	        // TEST END
	        
	        //before
//	        toggleBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//	                if (isChecked) {
//	                	if(toggleItalic.isChecked()){
////	                		inputPopup.setTextAppearance(getActivity(), R.style.italic_bold);
//	                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
//	                	}
//	                	else{
//	                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//	                	}
//	                } else {
//	                	if(toggleItalic.isChecked()){
////	                		inputPopup.setTextAppearance(getActivity(), R.style.italic);
//	                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
//	                	}
//	                	else{
////	                		inputPopup.setTextAppearance(getActivity(), R.style.normal_text);
//	                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//	                	}
//	                }
//	            }
//	        });
	        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
//	        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
	        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//	        int popupHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.popup_height), getResources().getDisplayMetrics()));
//	        int popupWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.popup_width), getResources().getDisplayMetrics()));
//			int popupHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpHeight*0.8), getResources().getDisplayMetrics()));
			final int popupWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpWidth*0.9), mContext.getResources().getDisplayMetrics()));

//	        mainView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//			int mainWidth = mainView.getMeasuredWidth();
//			int mainHeight = mainView.getMeasuredHeight();
//	        final PopupWindow popup = new PopupWindow(popupView, popupWidth, popupHeight, true);
			final ReattachingPopup popup = new ReattachingPopup(mBelongsTo, popupView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
			
//			final ReattachingPopup popup = new ReattachingPopup(this, popupView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
			popup.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),""));
//			popup.setOutsideTouchable(false);
	        popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	        
	        final Button addRefButton = (Button) popupView.findViewById(R.id.add_ref);
	        final TableFragment tf = mBelongsTo; 
	        addRefButton.setOnClickListener(new Button.OnClickListener() {
	        	public void onClick(View v) {
//	        		Animation slide_up = AnimationUtils.loadAnimation(TemplateGeneratorActivity.theActiveActivity, R.animator.slide_up);
	                LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                View popupReferencesView = inflater.inflate(R.layout.table_view_references, null);
	                LinearLayout reference_list = (LinearLayout) popupReferencesView.findViewById(R.id.reference_list);
	                final ReattachingPopup popupReferences = new ReattachingPopup(tf, popupReferencesView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
	                popupReferences.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),""));
	                popupReferences.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//	                popupReferences.setOnDismissListener(new OnDismissListener() {
//						@Override
//						public void onDismiss() {
//					        Log.d("inputPopup", "now selection set");
//		                    inputPopup.setSelection(inputPopup.getText().length());
//		                    inputPopup.requestFocus();
//						}
//					});
	                ArrayList<String> allRefs = getAllElementsToRef(((SlideoutNavigationActivity) SlideoutNavigationActivity.theActiveActivity).getRootFragment());
	                for(String aReference : allRefs){
	                	TextView oneLine = new TextView(SlideoutNavigationActivity.theActiveActivity);
	                	oneLine.setText(aReference);
	                	oneLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
	                	           mContext.getResources().getDimension(R.dimen.text_large));
	                	oneLine.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String lastCharOfPopup = inputPopup.getText().toString();
								if(lastCharOfPopup.length() > 0){
									lastCharOfPopup = lastCharOfPopup.substring(lastCharOfPopup.length() - 1);
									if(!lastCharOfPopup.equals("\n") && !lastCharOfPopup.equals(" ")){
										inputPopup.append(" ");
									}
								}
								inputPopup.append("@" + ((TextView) v).getText()+ " ");
								popupReferences.dismiss();
							}
						});
	                	reference_list.addView(oneLine);
	                }
//	                reference_view.startAnimation(slide_up);
	    			popupReferences.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
	        	}
	        });
	        //following does not work yet
//	        inputPopup.setOnFocusChangeListener(new OnFocusChangeListener() {
//				
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					InputMethodManager inputMgr = (InputMethodManager)TemplateGeneratorActivity.theActiveActivity.
//							getSystemService(Context.INPUT_METHOD_SERVICE);
//					if(hasFocus){
//						inputMgr.showSoftInput(inputPopup, InputMethodManager.SHOW_FORCED);
////						inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
////								InputMethodManager.SHOW_IMPLICIT);
//						
////						TemplateGeneratorActivity.theActiveActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//				        Log.d("focus_change", "SHOW software-keyboard");
//					}
//					else{
//						inputMgr.hideSoftInputFromWindow(inputPopup.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
////						inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
////								InputMethodManager.HIDE_IMPLICIT_ONLY);
////						TemplateGeneratorActivity.theActiveActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////				        Log.d("focus_change", "HIDE software-keyboard");
//					}
//				}
//			});
	        
	        ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					popupHeadline.setText(headline);
					//old version... but we need to take the content as parent, not popupView
//					popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);
					popup.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
					InputMethodManager inputMgr = (InputMethodManager)SlideoutNavigationActivity.theActiveActivity.
							getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMgr.showSoftInput(inputPopup, InputMethodManager.SHOW_FORCED);
				}
			});
			popupView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					inputPopup.requestFocus();
				}
			});
//			txt.setTextSize(mContext.getResources().getDimension(R.dimen.text_xxl));
			txt.setText(mContext.getResources().getString(R.string.new_element));
			txt.setTypeface(null, Typeface.BOLD_ITALIC);
			txt.setTextColor(mContext.getResources().getColor(R.color.dark_green));

			ll.addView(txt);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
			ll.setLayoutParams(p);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			return ll;
	  }
	  
	  private ArrayList<String> getAllElementsToRef(FolderFragment fragmentToSearch){
	    	ArrayList<String> results = new ArrayList<String>();
			Log.d("popupReferences", "subdirs: " + fragmentToSearch.dataAdapter.getAll().length);
	        for(FolderElementData currentDatum  : fragmentToSearch.dataAdapter.getAll()){
	        	GeneralFragment currentFragment = currentDatum.childFragment;
	        	if(currentFragment instanceof FolderFragment){
	    			Log.d("popupReferences", "folderfragment found, descending now");
	        		ArrayList<String> toAdd = getAllElementsToRef((FolderFragment) currentFragment);
	        		results.addAll(toAdd);
	        	}
	        	else if(currentFragment instanceof TableFragment){
	    			Log.d("popupReferences", "tableview found");
	        	}
	        	else if(currentFragment instanceof MatrixFragment){
	    			Log.d("popupReferences", "matrix found. Elements:" + (((MatrixFragment) currentFragment).itemsList).size());
	        		for(MatrixItem oneItem : ((MatrixFragment) currentFragment).itemsList){
	        			String oneName = oneItem.getItemName();
	        			results.add(oneName);
	        		}
	        	}
	        	else{
	    			Log.d("popupReferences", "unhandled element found!!!");
	        	}
	        }
	        return results;
	    }
	  
}