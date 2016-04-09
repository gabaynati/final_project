package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerListAdapter extends ArrayAdapter<String>{

	    Context context; 
	    int layoutResourceId;    
	    Vector<String> team = null;
	    
	    public PlayerListAdapter(Context context, int layoutResourceId,Vector<String> team) {
	        super(context, layoutResourceId,team);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.team = team;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        teamHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new  teamHolder();
	           // holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
	            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = ( teamHolder)row.getTag();
	        }
	        
	        String player_name = team.elementAt(position);
	        holder.txtTitle.setText(player_name);
	        //holder.imgIcon.setImageResource(weather.icon);
	        
	        return row;
	    }
	    
	    static class  teamHolder
	    {
	       // ImageView imgIcon;
	        TextView txtTitle;
	    }
	
}
