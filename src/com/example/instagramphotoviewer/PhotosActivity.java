package com.example.instagramphotoviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class PhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "d2c3b06c22a043c2acabe8cf4653f214";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        fetchPopularPhotos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /* helper methods */
    private void fetchPopularPhotos(){
    	photos = new ArrayList<InstagramPhoto>();
    	aPhotos = new InstagramPhotosAdapter(this, photos);
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	lvPhotos.setAdapter(aPhotos);

    	String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	// Trigger the network request
    	client.get(popularURL, new JsonHttpResponseHandler() {
    		// define success and failure callbacks
    		@Override
    		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    			// Handle the successful response (popular photos JSON)
    			JSONArray photosJSON = null;
    			try {
    				photos.clear();
    				photosJSON = response.getJSONArray("data");
    				for (int i = 0; i < photosJSON.length(); i++) {
    					JSONObject photoJSON = photosJSON.getJSONObject(i);
    					InstagramPhoto photo = new InstagramPhoto();
    					photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
    					if (!photoJSON.isNull("caption")) {
    						photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
    					}
    					photo.setImageURL(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
    					photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
    					photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
    					photos.add(photo);
    				}
    				aPhotos.notifyDataSetChanged();
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
    		}
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
    			super.onFailure(statusCode, headers, responseString, throwable);
    		}
    		
    	});
    }
}
