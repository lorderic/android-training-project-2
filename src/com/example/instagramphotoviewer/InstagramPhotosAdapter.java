package com.example.instagramphotoviewer;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos){
		super(context, android.R.layout.simple_list_item_1, photos);
	}

	// Take the data source at position, converts it into a row of views
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item
		InstagramPhoto photo = getItem(position);
		
		// Check if we are using a recycled view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		
		// Lookup the subview within the template
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		
		// Populate the subviews 
		SpannableString spanStr = new SpannableString(photo.getUsername() + " -- " + photo.getCaption());
		spanStr.setSpan(new StyleSpan(Typeface.BOLD), 0, photo.getUsername().length(), 0);
		tvCaption.setText(spanStr);
		
		// Set the image height before loading
		imgPhoto.getLayoutParams().height = photo.getImageHeight();
		
		// Reset the image from the recycled view
		imgPhoto.setImageResource(0);
		
		// Ask for the photo to be added to the imageview based on the photo url
		Picasso.with(getContext()).load(photo.getImageURL()).into(imgPhoto);
		
		// Return the view for that data item
		return convertView;
	}
	
	// getView method (int position)
	// Default takes the model and call toString()
	
}
