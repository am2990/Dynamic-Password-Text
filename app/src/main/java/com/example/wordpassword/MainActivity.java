package com.example.wordpassword;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.example.wordpassword.activity.TypeSelectionActivity;


public class MainActivity extends Activity{


	EditText wordedit;
	Button bsubmit;
	String Wordphrase;
	Intent iuser,icheckuser;
	//boolean checkuser;
	String str_usern,checkuser;

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

		bsubmit.setOnClickListener(new View.OnClickListener() {
			@ Override
			public void onClick(View v) {


				Wordphrase = wordedit.getText().toString();
				System.out.println("Word phrase: "+ Wordphrase);
				//String[] strword = Wordphrase.split("\\,");


				String[] arr = Wordphrase.split(" ");

				ArrayList<Object> wordArrayList = new ArrayList<Object>();
				// ArrayList<String> wordArrayList = new ArrayList<String>();
				for ( String word : arr) {

					System.out.println("hello from word "+word);

					wordArrayList.add(word);
					System.out.println("hello from array list "+wordArrayList);
				}
				 
				/* Intent intent = new Intent(MainActivity.this, WordCloudActivity.class);
			        intent.putStringArrayListExtra("wordArrayList", wordArrayList);
			        startActivity(intent);*/

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
					for(int ii = 0; ii < wordsList.size(); ii++){
						for(int jj = 0; jj < k; jj++){
							if(stopwords[jj].contains(wordsList.get(ii).toLowerCase())){
								//  wordsList.remove(ii);
								wordsList.set(ii,"");

								break;
							}
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

					//  startActivity(intent);
				}catch(Exception ex){
					System.out.println(ex);
				}

				// Object class does not implement Serializable interface

				Bundle extra = new Bundle();
				extra.putSerializable("wordArrayList", wordsList);

//			     Intent intent = new Intent(getBaseContext(), SampleTagCloud.class);
				Intent intent = new Intent(getBaseContext(), TypeSelectionActivity.class);
				intent.putExtra("extra", extra);
				intent.putExtra("usern",str_usern);
				intent.putExtra("checkuser", checkuser);

				startActivity(intent);

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
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

}
