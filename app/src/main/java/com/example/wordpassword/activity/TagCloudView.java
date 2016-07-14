package com.example.wordpassword.activity;
/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpassword.helper.CSVeditor;
import com.example.wordpassword.helper.DatabaseHelper;
import com.example.wordpassword.util.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class TagCloudView extends RelativeLayout {

	private static final String TAG = TagCloudView.class.getSimpleName();

	private Boolean signUp = false;

	int count1=0;
	int count_wrong =1;
	String temp="";

	boolean flag1=true;
	DatabaseHelper db;
	User user = new User();

	private String userName;

	Intent calleeIntent = null;

	public TagCloudView(Context mContext, int width, int height, List<Tag> tagList, ArrayList<String> objects, String checkuser, String str_usern, ArrayList<String> selected, ArrayList<String> notSelected, Boolean signUp) {
		this(mContext, width, height, tagList, 20 , 34, 1, objects, checkuser, str_usern, selected, notSelected, signUp); //default for min/max text size
		userName = str_usern;
		db = new DatabaseHelper(mContext);

	}
	public TagCloudView(Context mContext, int width, int height, List<Tag> tagList,
						int textSizeMin, int textSizeMax, int scrollSpeed, ArrayList<String> objects, String checkuser, String str_usern, ArrayList<String> selected, ArrayList<String> notSelected, Boolean signUp) {

		super(mContext);
		this.signUp = signUp;

		this.mContext= mContext;
		this.textSizeMin = textSizeMin;
		this.textSizeMax= textSizeMax;
		db = new DatabaseHelper(mContext);
		tspeed = scrollSpeed;

		//set the center of the sphere on center of our screen:
		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(centerX * 0.95f , centerY * 0.95f ); //use 95% of screen
		//since we set tag margins from left of screen, we shift the whole tags to left so that
		//it looks more realistic and symmetric relative to center of screen in X direction
		shiftLeft = (int)(Math.min(centerX * 0.15f , centerY * 0.15f ));

		// initialize the TagCloud from a list of tags
		//Filter() func. screens tagList and ignores Tags with same text (Case Insensitive)
		mTagCloud = new TagCloud(Filter(tagList), (int) radius ,
				textSizeMin,
				textSizeMax
		);
		float[] tempColor1 = {0.9412f,0.7686f,0.2f,1}; //rgb Alpha 
		//{1f,0f,0f,1}  red       {0.3882f,0.21568f,0.0f,1} orange
		//{0.9412f,0.7686f,0.2f,1} light orange
		//float[] tempColor2 = {1f,0f,0f,1}; //rgb Alpha
		float[] tempColor2 = {1f,0f,0f,1};
		//{0f,0f,1f,1}  blue      {0.1294f,0.1294f,0.1294f,1} grey
		//{0.9412f,0.7686f,0.2f,1} light orange
		//float[] tempColor3 = {0.1294f,0.1294f,0.1294f,1};
		mTagCloud.setTagColor1(tempColor1);//higher color
		mTagCloud.setTagColor2(tempColor2);//lower color
		mTagCloud.setRadius((int) radius);
		mTagCloud.create(true); // to put each Tag at its correct initial location


		//update the transparency/scale of tags
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		mTextView = new ArrayList<>();

		mParams = new ArrayList<>();
		//Now Draw the 3D objects: for all the tags in the TagCloud
		Iterator<?> it=mTagCloud.iterator();
		Tag tempTag;
		int i=0;

		while (it.hasNext()){
			tempTag= (Tag) it.next();
			tempTag.setParamNo(i); //store the parameter No. related to this tag

			mTextView.add(new TextView(this.mContext));
			mTextView.get(i).setText(tempTag.getText());

			mParams.add(new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT
					)
			);
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
			mParams.get(i).setMargins(
					(int) (centerX -shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()),
					0,
					0);
			mTextView.get(i).setLayoutParams(mParams.get(i));

			mTextView.get(i).setSingleLine(true);
			int mergedColor = Color.argb( (int)	(tempTag.getAlpha() * 255),
					(int)	(tempTag.getColorR() * 255),
					(int)	(tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(i).setTextColor(mergedColor);
			mTextView.get(i).setTextSize((int)(tempTag.getTextSize() * tempTag.getScale()));
			addView(mTextView.get(i));
			mTextView.get(i).setOnClickListener(OnTagClickListener(tempTag.getUrl(), objects, checkuser, str_usern, selected, notSelected));
			i++;
		}


	}


	public void setCalleeIntent(Intent i){
		this.calleeIntent = i;
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
	}

	public void addTag(Tag newTag){
		mTagCloud.add(newTag);

		int i= mTextView.size();
		newTag.setParamNo(i);

		mTextView.add(new TextView(this.mContext));
		mTextView.get(i).setText(newTag.getText());

		mParams.add(new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT
				)
		);
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mParams.get(i).setMargins(
				(int) (centerX -shiftLeft + newTag.getLoc2DX()),
				(int) (centerY + newTag.getLoc2DY()),
				0,
				0);
		mTextView.get(i).setLayoutParams(mParams.get(i));

		mTextView.get(i).setSingleLine(true);
		int mergedColor = Color.argb( (int)	(newTag.getAlpha() * 255),
				(int)	(newTag.getColorR() * 255),
				(int)	(newTag.getColorG() * 255),
				(int) (newTag.getColorB() * 255));
		mTextView.get(i).setTextColor(mergedColor);
		mTextView.get(i).setTextSize((int)(newTag.getTextSize() * newTag.getScale()));
		addView(mTextView.get(i));
		mTextView.get(i).setOnClickListener(OnTagClickListener(newTag.getUrl(), null, null, null, null,null));
	}

	public boolean Replace(Tag newTag, String oldTagText){
		boolean result=false;
		int j= mTagCloud.Replace(newTag, oldTagText);
		if (j>=0) { //then oldTagText was found and replaced with newTag data			
			Iterator<?> it=mTagCloud.iterator();
			Tag tempTag;
			while (it.hasNext()){
				tempTag= (Tag) it.next();
				mParams.get(tempTag.getParamNo()).setMargins(
						(int) (centerX -shiftLeft+ tempTag.getLoc2DX()),
						(int) (centerY + tempTag.getLoc2DY()),
						0,
						0);
				mTextView.get(tempTag.getParamNo()).setText(tempTag.getText());
				mTextView.get(tempTag.getParamNo()).setTextSize(
						(int)(tempTag.getTextSize() * tempTag.getScale()));
				int mergedColor = Color.argb( (int)	(tempTag.getAlpha() * 255),
						(int)	(tempTag.getColorR() * 255),
						(int)	(tempTag.getColorG() * 255),
						(int) (tempTag.getColorB() * 255));
				mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
				mTextView.get(tempTag.getParamNo()).bringToFront();
			}
			result=true;
		}
		return result;
	}

	public void reset(){
		mTagCloud.reset();

		Iterator<?> it=mTagCloud.iterator();
		Tag tempTag;
		while (it.hasNext()){
			tempTag= (Tag) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX -shiftLeft+ tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()),
					0,
					0);
			mTextView.get(tempTag.getParamNo()).setTextSize((int)(tempTag.getTextSize() * tempTag.getScale()));
			int mergedColor = Color.argb( (int)	(tempTag.getAlpha() * 255),
					(int)	(tempTag.getColorR() * 255),
					(int)	(tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		mAngleX = ( y)*tspeed * TRACKBALL_SCALE_FACTOR;
		mAngleY = (-x)*tspeed * TRACKBALL_SCALE_FACTOR;

		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		Iterator<?> it=mTagCloud.iterator();
		Tag tempTag;
		while (it.hasNext()){
			tempTag= (Tag) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX -shiftLeft+ tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()),
					0,
					0);
			mTextView.get(tempTag.getParamNo()).setTextSize((int)(tempTag.getTextSize() * tempTag.getScale()));
			int mergedColor = Color.argb( (int)	(tempTag.getAlpha() * 255),
					(int)	(tempTag.getColorR() * 255),
					(int)	(tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				//rotate elements depending on how far the selection point is from center of cloud
				float dx = x - centerX;
				float dy = y - centerY;
				mAngleX = ( dy/radius) *tspeed * TOUCH_SCALE_FACTOR;
				mAngleY = (-dx/radius) *tspeed * TOUCH_SCALE_FACTOR;

				mTagCloud.setAngleX(mAngleX);
				mTagCloud.setAngleY(mAngleY);
				mTagCloud.update();

				Iterator<?> it=mTagCloud.iterator();
				Tag tempTag;
				while (it.hasNext()){
					tempTag= (Tag) it.next();
					mParams.get(tempTag.getParamNo()).setMargins(
							(int) (centerX -shiftLeft + tempTag.getLoc2DX()),
							(int) (centerY + tempTag.getLoc2DY()),
							0,
							0);
					mTextView.get(tempTag.getParamNo()).setTextSize((int)(tempTag.getTextSize() * tempTag.getScale()));
					int mergedColor = Color.argb( (int)	(tempTag.getAlpha() * 255),
							(int)	(tempTag.getColorR() * 255),
							(int)	(tempTag.getColorG() * 255),
							(int) (tempTag.getColorB() * 255));
					mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
					mTextView.get(tempTag.getParamNo()).bringToFront();


				}

				break;
		}

		return true;
	}

	String urlMaker(String url){

		System.out.println("hello from tag cloud"+ url);

		if(url != ""){
			System.out.println("value of url"+url);

			return url;
		}
		else{
			System.out.println("do nothing");
			return null;

		}

	}

	//the filter function makes sure that there all elements are having unique Text field:
	List<Tag> Filter(List<Tag> tagList){
		//current implementation is O(n^2) but since the number of tags are not that many,
		//it is acceptable.
		List<Tag> tempTagList= new ArrayList<>();
		Iterator<Tag> itr = tagList.iterator();
		Iterator<Tag> itrInternal;
		Tag tempTag1, tempTag2;
		//for all elements of TagList
		while (itr.hasNext()){

			tempTag1 = (itr.next());

			boolean found = false;
			//go over all elements of temoTagList
			itrInternal = tempTagList.iterator();
			while (itrInternal.hasNext()){

				tempTag2 = (itrInternal.next());
				if (tempTag2.getText().equalsIgnoreCase(tempTag1.getText())){


					found = true;
					break;
				}



			}
			if (!found)
				tempTagList.add(tempTag1);

		}
		return tempTagList;
	}

	int selectionCount = 0;

	//for handling the click on the tags
	//onclick open the tag url in a new window. Back button will bring you back to TagCloud
	View.OnClickListener OnTagClickListener(final String url,final ArrayList<String> objects, final String checkuser, final String str_usern, final ArrayList<String> selected, final ArrayList<String> notSelected){
		return new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				temp=url;
				try{

					Uri uri = Uri.parse(urlMaker(url));

					System.out.println("uri: "+uri.toString());

					Tag tempTag = mTagCloud.getTag(uri.toString());
					tempTag.toggle();

					mTagCloud.update();

					int length=objects.size();

					boolean setval = tempTag.isSelected();

					if (tempTag.isSelected()){
						selectionCount++;
						if(objects.contains(temp)){
							count1++;

							if(count1 < length){
								flag1=true;
							}
							else if(count1==length && selectionCount == count1){
								System.out.println("no need");
								flag1=false;
							}
						}
						else if( selectionCount > length){

							if(signUp) {
								// user is signing up but got the password wrong
								CSVeditor.shared().setSuccessLogin(false);
							}
							else {
								// user is signing in but got the password wrong
								//update failed login attempts
								UsernameActivity.stopScreenSharing();
								CSVeditor.shared().setSuccessLogin(false);
							}
							if(count_wrong < 3) {
								count_wrong++;
								Toast.makeText(mContext, "wrong selection !!", Toast.LENGTH_SHORT).show();
								Log.v(TAG, "selectionCount > length");
								selectionCount = 0;
								count1 = 0;
								flag1 = true;
								reset();
							}
							else{

								Toast.makeText(mContext, "SignUp again", Toast.LENGTH_LONG).show();
								Intent intent = new Intent(mContext, UsernameActivity.class);
								//intent.putExtra("USERNAME", userName);
								Bundle extra = new Bundle();
								extra.putSerializable("selectedWordArrayList", selected);
								extra.putSerializable("notSelectedWordArrayList", notSelected);
								intent.putExtra("extra", extra);
								intent.putExtra("usern",str_usern);
								intent.putExtra("checkuser", checkuser);
								mContext.startActivity(intent);
							}
						}

						if(!flag1){
							Toast.makeText(mContext, "Correct Password !!", Toast.LENGTH_LONG).show();

							if(signUp) {
								// user is signing up
								Log.v(TAG,"signup");
								long timeSpent = Calendar.getInstance().getTimeInMillis() - SampleTagCloud.startTimeAtSampleTagCloud;
								CSVeditor.shared().recordTimeStamp(timeSpent, 9);
								long totalTimeSpent = Calendar.getInstance().getTimeInMillis() - UsernameActivity.startTime;
								CSVeditor.shared().recordTimeStamp(totalTimeSpent, 10);

								UsernameActivity.stopScreenSharing();
							}
							else {
								// user is signing in
								Log.v(TAG,"signin");
								long timeSpent = Calendar.getInstance().getTimeInMillis() - SampleTagCloud.startTimeAtSampleTagCloud;
								CSVeditor.shared().recordTimeStamp(timeSpent, 9);
								long totalTimeSpent = Calendar.getInstance().getTimeInMillis() - UsernameActivity.startTime;
								CSVeditor.shared().recordTimeStamp(totalTimeSpent, 10);

								CSVeditor.shared().setSuccessLogin(true);

								UsernameActivity.stopScreenSharing();
							}

							if(checkuser.equalsIgnoreCase("false")){

								user.setUsername(str_usern);
								user.setNselected(notSelected.toString());
								user.setSelected(selected.toString());
								System.out.println("selected tagcloudview 1: " + selected.toString());
								System.out.println("not selected tagcloudview 1: "+ notSelected.toString());
								db.addUser(user);
							}

							callNextAct(signUp);

						}
						else{
							System.out.println("not ok");

						}
						System.out.println("selected count1: " + count1 +"," + selectionCount);
					}
					else if(!(tempTag.isSelected())){
						selectionCount--;

						if(objects.contains(temp)){
							count1--;
						}
						System.out.println("unselected count1: " + count1 +"," + selectionCount);
					}
				}
				catch(Exception e){
					Toast.makeText(mContext,"WRONG SELECTION !!", Toast.LENGTH_SHORT).show();
					Log.v(TAG,"exception wrong selection");
				}
			}
		};
	}

	private void callNextAct(Boolean signUp) {

		if(signUp) {
			Toast.makeText(mContext, "signup successful", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext, UsernameActivity.class);
			mContext.startActivity(intent);
		}
		else {
			Toast.makeText(mContext, "Sign-in successful", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext, FeedbackActivity.class);
			intent.putExtra("USERNAME", userName);
			mContext.startActivity(intent);
		}
	}

	private final float TOUCH_SCALE_FACTOR = .8f;
	private final float TRACKBALL_SCALE_FACTOR = 10;
	private float tspeed;
	private TagCloud mTagCloud;
	private float mAngleX =0;
	private float mAngleY =0;
	private float centerX, centerY;
	private float radius;
	private Context mContext;
	private int textSizeMin, textSizeMax;
	private List<TextView> mTextView;
	private List<RelativeLayout.LayoutParams> mParams;
	private int shiftLeft;
}
