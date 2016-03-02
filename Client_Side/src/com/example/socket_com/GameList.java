package com.example.socket_com;

import com.example.hs.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;


public class GameList extends Activity{
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TableLayout games=new TableLayout(getBaseContext());
		TableRow tr=new TableRow(getBaseContext());
		/*
		tr.addView(view);
		tbl.addView(tr);
		*/
	}
}
