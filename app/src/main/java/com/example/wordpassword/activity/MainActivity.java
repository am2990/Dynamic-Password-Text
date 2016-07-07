package com.example.wordpassword.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.content.Context;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.example.wordpassword.R;
import com.example.wordpassword.helper.CSVeditor;


public class MainActivity extends Activity{

	EditText wordedit;
	Button bsubmit;
	String Wordphrase, word1;
	Intent iuser,icheckuser;
	String str_usern,checkuser;
	Context mContext;
	TextView quote1,quote2,quote3,ex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		iuser=getIntent();
		icheckuser = getIntent();

		checkuser = icheckuser.getStringExtra("checkuser");
		str_usern = iuser.getStringExtra("usern");
		bsubmit=(Button) findViewById(R.id.bsubmit);
		wordedit = (EditText) findViewById(R.id.et1);
		quote1 = (TextView) findViewById(R.id.quote1);
		quote2 = (TextView) findViewById(R.id.quote2);
		quote3 = (TextView) findViewById(R.id.quote3);
		ex = (TextView) findViewById(R.id.ex);
		mContext = this;
		bsubmit.setOnClickListener(new View.OnClickListener() {
			@ Override
			public void onClick(View v) {


				word1 = wordedit.getText().toString().replaceAll("\"[^a-zA-Z ]\"", "");
				Wordphrase = word1.replaceAll("\\,", "").replaceAll("\\;", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\.", "");
				System.out.println("Word phrase: "+ Wordphrase);
				//String[] strword = Wordphrase.split("\\,");


				String[] arr = Wordphrase.split(" ");

				ArrayList<Object> wordArrayList = new ArrayList<Object>();
				// ArrayList<String> wordArrayList = new ArrayList<String>();
				for ( String word : arr) {

					System.out.println("hello from word "+word);
					if(!wordArrayList.contains(word))
						wordArrayList.add(word);
					System.out.println("hello from array list "+wordArrayList);
				}
				 
				/* Intent intent = new Intent(MainActivity.this, WordCloudActivity.class);
			        intent.putStringArrayListExtra("wordArrayList", wordArrayList);
			        startActivity(intent);*/

				int k=0,i,j;
				ArrayList<String> wordsList = new ArrayList<String>();
				String sCurrentLine;
				ArrayList<String> stopwords = new ArrayList<String>();
				try{
					ContextWrapper context = null;
					//@SuppressWarnings("null")
					//AssetManager am = context.getAssets();
					//InputStream is = am.open("test.txt");
					//InputStream is = getResources().openRawResource(R.raw.test);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(getAssets().open("MYSTWORD.TXT")));
					// FileReader fr=new FileReader(getAssets().open("MYSTWORD.TXT");
					//  BufferedReader br= new BufferedReader(fr);
					while ((sCurrentLine = br.readLine()) != null){
						//System.out.println("hello from while");
						stopwords.add(sCurrentLine);

					}
					// String s="I love this phone, its super fast and there's so much new and cool things with jelly bean....but of recently I've seen some bugs.";
					// String s = wordArrayList.toString();
					String s = "";

					for (Object ss : wordArrayList)
					{
						s += ss + " ";
					}
					// System.out.println(s);
					System.out.println("String value of s"+ s);
					StringBuilder builder = new StringBuilder(s);
					String[] words = builder.toString().split("\\s");
					for (String word : words){
						wordsList.add(word);
						//  System.out.println("hello from words list "+wordsList);
					}
					// Remove stop words from the orignal sentence
					for(int ii = 0; ii < wordsList.size(); ii++){
						if(stopwords.contains(wordsList.get(ii).toLowerCase())){
							wordsList.set(ii,"");
						}
					}
					System.out.println("hello from words list "+wordsList);
					       
					     /*   for (String str : wordsList){
					            System.out.print("yo yo"+str+" ");
					        }  */
					       /* Intent intent = new Intent(getBaseContext(), SampleTagCloud.class);
							 
						    
							intent.putExtra("extra", str);*/
					wordsList.removeAll(Collections.singleton(""));
					System.out.println("hello from words list "+wordsList);
					if( wordsList.size() < 3 ){
						Toast.makeText(mContext, "Need more words !!!", Toast.LENGTH_LONG).show();
						return;
					}

					//  startActivity(intent);
				}catch(Exception ex){
					System.out.println(ex);
				}

				// Object class does not implement Serializable interface
if (isNetworkAvailable()) {
	Bundle extra = new Bundle();
	extra.putSerializable("wordArrayList", wordsList);

//			     Intent intent = new Intent(getBaseContext(), SampleTagCloud.class);
	Intent intent = new Intent(getBaseContext(), TypeSelectionActivity.class);
	intent.putExtra("extra", extra);
	intent.putExtra("usern", str_usern);
	intent.putExtra("checkuser", checkuser);

	long spentTime = Calendar.getInstance().getTimeInMillis() - startTime;
	CSVeditor.shared().recordTimeStamp(spentTime, 6);

	startActivity(intent);
}
				else{
	Toast.makeText(MainActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
				}
			}

		/*	private void stopwords(ArrayList<Object> wordArrayList) {
				// TODO Auto-generated method stub
				int k=0,i,j;
				ArrayList<String> wordsList = new ArrayList<String>();
				String sCurrentLine;
				String[] stopwords = new String[2000];
				try{
					ContextWrapper context = null;
					//@SuppressWarnings("null")
					//AssetManager am = context.getAssets();
					//InputStream is = am.open("test.txt");
					//InputStream is = getResources().openRawResource(R.raw.test);
					 BufferedReader br = new BufferedReader(
			                 new InputStreamReader(getAssets().open("MYSTWORD.TXT")));
				       // FileReader fr=new FileReader(getAssets().open("MYSTWORD.TXT");
				      //  BufferedReader br= new BufferedReader(fr);
				        while ((sCurrentLine = br.readLine()) != null){
				        	//System.out.println("hello from while");
				            stopwords[k]=sCurrentLine;
				            k++;
				        }
				        String s="I love this phone, its super fast and there's so much new and cool things with jelly bean....but of recently I've seen some bugs.";
				       // String s = wordArrayList.toString();
				        System.out.println("String value of s"+ s);
				        StringBuilder builder = new StringBuilder(s);
				        String[] words = builder.toString().split("\\s");
				        for (String word : words){
				            wordsList.add(word);
				          //  System.out.println("hello from words list "+wordsList);
				        }
				        for(int ii = 0; ii < wordsList.size(); ii++){
				            for(int jj = 0; jj < k; jj++){
				                if(stopwords[jj].contains(wordsList.get(ii).toLowerCase())){
				                    wordsList.remove(ii);
				                   
				                    break;
				                }
				             }
				        }
				        System.out.println("hello from words list "+wordsList);
				        for (String str : wordsList){
				            System.out.print(str+" ");
				        }  
				       /* Intent intent = new Intent(getBaseContext(), SampleTagCloud.class);
						 
					    
						intent.putExtra("extra", str);*/


			//  startActivity(intent);
				 /*   }catch(Exception ex){
				        System.out.println(ex);
				    }
				//Bundle extra = new Bundle();
			   //  extra.putSerializable("wordArrayList", wordArrayList);

			    
		//	}*/

		});

		quote1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wordedit.setText("hard work is the key to success", BufferType.EDITABLE);
			}
		});
		quote2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wordedit.setText("with great power, comes great responsibility", BufferType.EDITABLE);
			}
		});
		quote3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wordedit.setText("an eye for an eye makes the whole world blind", BufferType.EDITABLE);
			}
		});

	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	long startTime;
	@Override
	protected void onResume() {
		super.onResume();
		startTime = Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public void onBackPressed() {

		Toast.makeText(MainActivity.this, "Please complete the signup process", Toast.LENGTH_SHORT).show();
	}
}
