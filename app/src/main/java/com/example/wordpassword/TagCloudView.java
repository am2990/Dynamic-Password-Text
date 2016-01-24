package com.example.wordpassword;
/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpassword.activity.UsernameActivity;
import com.example.wordpassword.db.DatabaseHelper;
import com.example.wordpassword.util.User;

public class TagCloudView extends RelativeLayout {
	RelativeLayout navigation_bar;
	TextView mTextView1;
	int count = 0;
	int count1=0;
	String vv ="";
	String temp="";
	String ss ="";
	Object[] arr=new Object[100];
	Object[] array=new Object[100];
	boolean flag1=true;
	DatabaseHelper db;
	User user = new User();
	public TagCloudView(Context mContext, int width, int height, List<Tag> tagList, ArrayList<String> objects, String checkuser, String str_usern, ArrayList<String> selected, ArrayList<String> notSelected) {
		this(mContext, width, height, tagList, 20 , 34, 1, objects, checkuser, str_usern, selected, notSelected); //default for min/max text size
		System.out.println("usernameintag: " + str_usern);
		System.out.println("checkuserintag: "+ checkuser);
		System.out.println("selected tagcloudview: " + selected);
		System.out.println("nselected tagcloudview: "+ notSelected);
		db = new DatabaseHelper(mContext);

	}
	public TagCloudView(Context mContext, int width, int height, List<Tag> tagList, 
			int textSizeMin, int textSizeMax, int scrollSpeed, ArrayList<String> objects, String checkuser, String str_usern, ArrayList<String> selected, ArrayList<String> notSelected) {



		super(mContext);
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

		mTextView = new ArrayList<TextView>();
		mParams = new ArrayList<RelativeLayout.LayoutParams>();
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
			/*case MotionEvent.ACTION_UP:  //now it is clicked!!!!		
			dx = x - centerX;
			dy = y - centerY;			
			break;*/
		}

		return true;
	}
	/*String urlMaker(String url){
		if 	(	(url.substring(0,7).equalsIgnoreCase("http://")) 	|| 
				(url.substring(0,8).equalsIgnoreCase("https://"))
			)
			return url;
		else
			return "http://"+url;
	}*/

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
		List<Tag> tempTagList=new ArrayList<Tag>();
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
			if (found==false)
				tempTagList.add(tempTag1);

		}
		return tempTagList;
	}

	//for handling the click on the tags
	//onclick open the tag url in a new window. Back button will bring you back to TagCloud
	View.OnClickListener OnTagClickListener(final String url,final ArrayList<String> objects, final String checkuser, final String str_usern, final ArrayList<String> selected, final ArrayList<String> notSelected){
		return new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				//	System.out.println("hello");
				/*List<String> myList = new ArrayList<String>();
				myList.add(url);
				myList.toArray();
				String st = null
				System.out.println("MyList: "+myList.toArray());	*/
				//System.out.println("url length"+url.length());

				temp=url;
			/*	if(url!=""){

					count++;
					System.out.println("url length"+count);
				}*/
				//int p = 0;
				/*if(p < count){
					//String temp="";
					temp=url;
					System.out.println("for temp"+temp);
					arr[p]=temp;
					System.out.println(arr[p]);
					p++;
				}*/




				//}



				/*	if(url!=""){
				for(int ii=0;ii<=count;ii++){
					arr[ii]=url;
					System.out.println("array url"+arr[ii]);
				}
			}*/			    	
				try{

					Uri uri = Uri.parse( urlMaker(url) );
					//String vs= uri.toString();
					//System.out.println("yo yo"+ uri);
					//TODO get the tag instance from mTagCloud
					System.out.println("uri: "+uri.toString());
					Tag tempTag = mTagCloud.getTag(uri.toString());
					tempTag.toggle();
//					Tag tempTag = new Tag();
//					tempTag.toggle();
//					tempTag.setText(uri.toString());
					//List<?>=new ArrayList<?>;
					//int[] myList;


					System.out.println("temptag false-"+ tempTag.getText());
//					tempTag.setColorR(56);tempTag.setColorB(70);tempTag.setColorG(239);
//					mTagCloud.Replace(tempTag, uri.toString().trim());
					//				
					mTagCloud.update();

					System.out.println("false");
					System.out.println(objects);
					System.out.println("flag"+ flag1);
					int length=objects.size();
					System.out.println("lenght"+length);
					// for(int i=0;i<length; i++){
					System.out.println("temp value"+ temp);
					boolean setval = tempTag.isSelected();
					if (tempTag.isSelected()){
					if(objects.contains(temp)){

						/* ss=ss.concat(temp).concat(" ");
						 String[] ary=ss.split(",");
						 System.out.println("ary: "+ary);*/

						count1++;
						System.out.println("count1: "+ count1);


						if(count1 < length){
							flag1=true;
							System.out.println("true"+ flag1);
						}
						else if(count1==length){
							System.out.println("no need");
							flag1=false;
							System.out.println("false : "+ flag1);
						}
					} 

					if(flag1== false){


						System.out.println("ok");
						/*Intent intent = new Intent(mContext, NextActivity.class);
						 intent.putExtra("aaa", "extra");
						 startActivity(intent);*/
						if(checkuser.equalsIgnoreCase("false")){

							user.setUsername(str_usern);
							user.setNselected(notSelected.toString());
							user.setSelected(selected.toString());
							System.out.println("selected tagcloudview 1: " + selected.toString());
							System.out.println("not selected tagcloudview 1: "+ notSelected.toString());
							db.addUser(user);
						}

						callNextAct();

					}
					else{
						System.out.println("not ok");
					}
				}
					else if(!(tempTag.isSelected())){
						if(objects.contains(temp)){

							/* ss=ss.concat(temp).concat(" ");
							 String[] ary=ss.split(",");
							 System.out.println("ary: "+ary);*/

							count1--;
							System.out.println("count1: "+ count1);


						/*	if(count1 < length){
								flag1=true;
								System.out.println("true"+ flag1);
							}
							else if(count==length){
								System.out.println("no need");
								flag1=false;
								System.out.println("false : "+ flag1);
							}
						} 

						if(flag1== false){


							System.out.println("not ok");
							/*Intent intent = new Intent(mContext, NextActivity.class);
							 intent.putExtra("aaa", "extra");
							 startActivity(intent);*/
							

						}
					/*	else{
							System.out.println("ok");
						}*/
					}
					//ArrayList<Object>=new ArrayList<Object>;
					//Iterator<Object> itr = objects.iterator();

					/*for(int i=0;i<length; i++)
				{
				    if(itr.next()==arr[i]);
				    {
				        count1++;
				    }
				}
				if(count1 == length)
				{
				    System.out.println("Both are equal");
				}
				else
				{
				    System.out.println("Both are not equal");
				}*/
					//array=	objects.toArray();
					//System.out.println("array's array: "+array.toString());


					/*	if(p==(count-1)){
				array=	(String[])objects.toArray();
				System.out.println(array);
					if(Arrays.equals(array, arr)){
						System.out.println("u r right");
					}
					else{
						System.out.println("wrong password");
					}
				}
				else{
					System.out.println("more to select");
				}*/
					/*ArrayList<String> myList = new ArrayList<String>();
				//Iterator<Tag> itt = myList.iterator();
				String hh="";
			//	while(itt.hasNext()){
				//String ui =tempTag.toString();
			//	System.out.println("value of ui"+ ui);
				//while(uri.toString()!= ""){
						myList.add(i);
						//myList.toArray();
						//System.out.println("yo yo honey singh"+ myList.toArray());
						for (Objects s : myList)
						{

						    hh += s + "\t";
						}

				//}
				*/
				}
				catch(Exception e){
					Toast.makeText(mContext,"wrong selection !!", Toast.LENGTH_SHORT).show();




					//System.out.println("wrong word chosen");
					System.out.println(e);
				}



			}


		};



	}

	private void callNextAct() {
		// TODO Auto-generated method stub

		Intent intent = new Intent(mContext, UsernameActivity.class);

		mContext.startActivity(intent);
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
