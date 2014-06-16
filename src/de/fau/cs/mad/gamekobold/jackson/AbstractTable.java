package de.fau.cs.mad.gamekobold.jackson;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import de.fau.cs.mad.gamekobold.template_generator.MainTemplateGenerator;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @Type(value = Table.class, name = "table"), @Type(value = ContainerTable.class, name = "containertable") })
public abstract class AbstractTable {
	// TODO anstatt hier den table watcher vllt irgendwie im GeneralFragment eine setTitle funktion und dort mit einbringen
	@JsonIgnore
	public TextWatcher tableNameTextWatcher;
	
	public String tableName;
	
	public abstract void print();
	
	public AbstractTable() {
		tableNameTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			//	Log.d("YEEE", "HAAAAAAAAAAAAAAAAAAAAAAW");
				tableName = s.toString();
				MainTemplateGenerator.saveTemplate();
			}
		};
	}
}
