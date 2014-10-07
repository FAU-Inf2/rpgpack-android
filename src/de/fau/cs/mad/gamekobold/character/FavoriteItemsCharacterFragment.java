//package de.fau.cs.mad.gamekobold.character;
//
//import java.util.List;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import de.fau.cs.mad.gamekobold.R;
//import de.fau.cs.mad.gamekobold.ThumbnailLoader;
//import de.fau.cs.mad.gamekobold.game.GameCharacter;
//import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
//import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
//import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
//import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
//import de.fau.cs.mad.gamekobold.matrix.MatrixViewArrayAdapter;
//import de.fau.cs.mad.gamekobold.template_generator.CustomDrawerLayout;
//import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;
//
//public class FavoriteItemsCharacterFragment extends GeneralFragment {
//
//	MatrixViewArrayAdapter gridAdapter;
//	public List<MatrixItem> itemsList = null;
//
//	public FavoriteItemsCharacterFragment() {
//		super();
//		setRetainInstance(true);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		super.onCreateView(inflater, container, savedInstanceState);
//		View view = (CustomDrawerLayout) inflater.inflate(
//				R.layout.character_favorite_items, null);
//
//		GameCharacter curCharacter = null;
//		CharacterSheet curSheet = null;
//		String path = "";
//
//		ImageButton charIcon = (ImageButton) view
//				.findViewById(R.id.imageButtonCharacterIcon);
//
//		TextView charName = (TextView) view
//				.findViewById(R.id.character_name_edittext);
//
//		TextView charLevel = (TextView) view
//				.findViewById(R.id.character_level_edittext);
//
//		GridView gridViewMatrix = (GridView) view
//				.findViewById(R.id.gridViewFavMatrixItems);
//
//		ListView listViewTable = (ListView) view
//				.findViewById(R.id.listViewFavTableItems);
//
//		final Bitmap bitmap = ThumbnailLoader.loadThumbnail(curCharacter.getIconPath(), getActivity());
//		if(bitmap == null) {
//			// set some default game icon
//			charIcon.setImageResource(R.drawable.game_default_white);
//		}
//		else {
//			// set game icon
//			charIcon.setImageBitmap(bitmap);
//		}
//
//		charName.setText(curCharacter.getCharacterName());
//
//		//TODO find out fav items!
////		for (AbstractTable at : curSheet.getRootTable().subTables){
////			if (at.isFavorite() && at instanceof MatrixTable) {
////				at.
////			}
////		}
////		 
////		
////		if (gridAdapter == null) {
////			gridAdapter = new MatrixViewArrayAdapter(
////					getActivity(), itemsList);
////			// adapter.jacksonTable = jacksonTable;
////		}
////
////		gridViewMatrix.setAdapter(gridAdapter);
////		
//		
//		return view;
//	}
//
//	@Override
//	protected void addItemList() {
//		// nothing to add in WelcomeFragment
//	}
//
//	@Override
//	public void showDialog() {
//		// no dialog to show
//	}
//}
