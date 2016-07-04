package com.example.wordpassword.activity;
/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * SampleTagCloud class:
 * this is a sample program to show how the 3D Tag Cloud can be used.
 * It Creates the activity and sets the ContentView to our TagCloudView class
 */
public class SampleTagCloud extends Activity {

	private final String TAG = "SampleTagCloud";

	String str1="";
	String stritr;
	int g=0;
	int p=0;
	Intent iuser,icheckuser,iselected,inotSelected;
	String str_usern,checkuser;
	//ArrayList<Object> objects;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Step0: to get a full-screen View:
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Bundle extra = getIntent().getBundleExtra("extra");

		Boolean signUp = getIntent().getBooleanExtra("SIGN_UP", false);

//		ArrayList<String> objects = (ArrayList<String>) extra.getSerializable("wordArrayList");
		iuser=getIntent();
		icheckuser = getIntent();
		iselected = getIntent();
		inotSelected = getIntent();
		checkuser = icheckuser.getStringExtra("checkuser");
		str_usern = iuser.getStringExtra("usern");
		ArrayList<String> selected = new ArrayList<String>();
		ArrayList<String> notSelected = new ArrayList<String>();
		// get selected words array
		if(checkuser.equalsIgnoreCase("false")) {
			selected = (ArrayList<String>) extra.getSerializable("selectedWordArrayList");
			// get not selected words array
			notSelected = (ArrayList<String>) extra.getSerializable("notSelectedWordArrayList");
			System.out.println("selected sampletag: " + selected);
			System.out.println("notselected sampletag: " + notSelected);
		}
		else{
			selected = iselected.getStringArrayListExtra("selected");
			notSelected = inotSelected.getStringArrayListExtra("notSelected");
		}
		ArrayList<String> wordList = new ArrayList<>();
		ArrayList<String> removeselected = new ArrayList<>(selected);

		Random r = new Random();
		int size = removeselected.size();
		if( size > 3) {
			// pick 3 words from selected list
			for (int i = 0; i < size - 3; i++) {
				int ran = r.nextInt(removeselected.size());
				String word = removeselected.get(ran);
//				Log.d(TAG, "selected word from S" + word);
				removeselected.remove(word);

			}

		}
		for (int i = 0; i < removeselected.size() ; i++) {

			String word = removeselected.get(i);
			Log.d(TAG, "selected word from S" + word);

		}

		wordList.addAll(removeselected);

//		wordList.addAll(notSelected);





		// @SuppressWarnings("unchecked")
		//Step1: get screen resolution:
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		//Step2: create the required TagList:
		//notice: All tags must have unique text field
		//if not, only the first occurrence will be added and the rest will be ignored

		List<Tag> myTagList = createTags(removeselected, notSelected);


		//Step3: create our TagCloudview and set it as the content of our MainActivity
		mTagCloudView = new TagCloudView(this, width, height, myTagList, wordList, checkuser, str_usern, selected, notSelected, signUp ); //passing current context
		System.out.println("value for objects: "+wordList);
		setContentView(mTagCloudView);
		mTagCloudView.requestFocus();
		mTagCloudView.setFocusableInTouchMode(true);

		//Step4: (Optional) adding a new tag and resetting the whole 3D TagCloud
		//you can also add individual tags later:
		//mTagCloudView.addTag(new Tag("AAA", 5, "http://www.aaa.com"));
		// .... (several other tasg can be added similarly )
		//indivual tags will be placed along with the previous tags without moving 
		//old ones around. Thus, after adding many individual tags, the TagCloud 
		//might not be evenly distributed anymore. reset() re-positions all the tags:
		//mTagCloudView.reset();

		//Step5: (Optional) Replacing one of the previous tags with a new tag
		//you have to create a newTag and pass it in together 
		//with the Text of the existing Tag that you want to replace
		//Tag newTag=new Tag("Illinois", 9, "http://www.illinois.com");
		//in order to replace previous tag with text "Google" with this new one:
		//boolean result=mTagCloudView.Replace(newTag, "google");
		//result will be true if "google" was found and replaced. else result is false
	}

	static long startTimeAtSampleTagCloud;
	protected void onResume() {
		super.onResume();
		startTimeAtSampleTagCloud = Calendar.getInstance().getTimeInMillis();
	}

	protected void onPause() {
		super.onPause();
	}

	private List<Tag> createTags(ArrayList<String> selected, ArrayList<String> notSelected){

		//int count = objects.size();
//	        System.out.println(count);
		//create the list of tags with popularity values and related url
		List<Tag> tempList = new ArrayList<>();
		List<String> list = selected;
//		Iterator<String> iterator = list.iterator();
		String listString = "";
		Random r = new Random();
		// pick 3 words from selected list
		for(int i =0 ; i < 3; i++){
			int ran = r.nextInt(list.size());
			String word = list.get(ran);
			tempList.add(new Tag(word, r.nextInt(10), word));
			Log.d(TAG, "selected word from S" + word);
			list.remove(word);

		}
		list = notSelected;
		for(int i =0 ; i < 7; i++){
			//TODO if list size if less than 7 randomly add words
			int ran = r.nextInt(list.size());
			String word = list.get(ran);
			tempList.add(new Tag(word, r.nextInt(10), word));
			Log.d(TAG, "selected word from NS" + word);
			list.remove(word);
		}

//		for (Object s : list)
//		{
//			listString += s + "\t";
//		}
//
//		System.out.println("list string"+listString+" ");
//
//
//		while(iterator.hasNext())
//		{
//
//
//
//			String str = (String)iterator.next();
//
//			tempList.add(new Tag(str,5,str));
//
//
//		}


//		tempList.add(new Tag("Google", 7));  //1,4,7,... assumed values for popularity
//		tempList.add(new Tag("Yahoo", 3));
//		tempList.add(new Tag("CNN", 4));
//		tempList.add(new Tag("Facebook", 2));
		//	System.out.println(tempList.subList(0, 1));

		//tempList.add(new Tag())
		/*tempList.add(new Tag("MSNBC", 5, "www.msnbc.com"));
		tempList.add(new Tag("CNBC", 5, "www.CNBC.com"));
		tempList.add(new Tag("Facebook", 7, "www.facebook.com"));
		tempList.add(new Tag("Youtube", 3, "www.youtube.com"));
		tempList.add(new Tag("BlogSpot", 5, "www.blogspot.com"));
		tempList.add(new Tag("Bing", 3, "www.bing.com"));
		tempList.add(new Tag("Wikipedia", 8, "www.wikipedia.com"));
		tempList.add(new Tag("Twitter", 5, "www.twitter.com"));
		tempList.add(new Tag("Msn", 1, "www.msn.com"));
		tempList.add(new Tag("Amazon", 3, "www.amazon.com"));
		tempList.add(new Tag("Ebay", 7, "www.ebay.com"));
		tempList.add(new Tag("LinkedIn", 5, "www.linkedin.com"));
		tempList.add(new Tag("Live", 7, "www.live.com"));
		tempList.add(new Tag("Microsoft", 3, "www.microsoft.com"));
		tempList.add(new Tag("Flicker", 1, "www.flicker.com"));
		tempList.add(new Tag("Apple", 5, "www.apple.com"));
		tempList.add(new Tag("Paypal", 5, "www.paypal.com"));
		tempList.add(new Tag("Craigslist", 7, "www.craigslist.com"));
		tempList.add(new Tag("Imdb", 2, "www.imdb.com"));
		tempList.add(new Tag("Ask", 4, "www.ask.com"));
		tempList.add(new Tag("Weibo", 1, "www.weibo.com"));
		tempList.add(new Tag("Tagin!", 8, "http://scyp.idrc.ocad.ca/projects/tagin"));
		tempList.add(new Tag("Shiftehfar", 8, "www.shiftehfar.org"));
		tempList.add(new Tag("Soso", 5, "www.google.com"));
		tempList.add(new Tag("BBC", 5, "www.bbc.co.uk"));*/
		return tempList;
	}

	private TagCloudView mTagCloudView;

	@Override
	public void onBackPressed() {
		Log.v(TAG,"onBackPressed");
		Toast.makeText(SampleTagCloud.this, "Please select words from cloud", Toast.LENGTH_SHORT).show();
	}
}
