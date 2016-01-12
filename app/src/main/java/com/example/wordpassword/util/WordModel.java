package com.example.wordpassword.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apurv on 12/23/2015.
 */
public class WordModel {

    private final String TAG = this.getClass().getCanonicalName();

    private int _id;

    private String original_sentence;
    private String trimmed_sentence;
    private String word;

    private int passwordSelection;

    private String serealized_synonyms;
    private String serealized_antonyms;;
    private String serealized_similiars;;

    private HashMap<String, ArrayList<String>> synonyms = null;
    private HashMap<String, ArrayList<String>> antonyms = null;
    private HashMap<String, ArrayList<String>> similar = null;


    public WordModel(String word){
        this.word = word;
        synonyms = new HashMap<>();
        ArrayList<String> arr_sy = new ArrayList<String>();
        arr_sy.add(word);
        synonyms.put(word,arr_sy);

        antonyms = new HashMap<>();
        ArrayList<String> arr_an = new ArrayList<String>();
        arr_an.add(word);
        antonyms.put(word,arr_an);

        similar = new HashMap<>();
        ArrayList<String> arr_si = new ArrayList<String>();
        arr_si.add(word);
        similar.put(word,arr_si);

    }

    public void addSynonyms(String key, String word){
        if(synonyms == null)
            synonyms = new HashMap<>();
        if(synonyms.get(key) != null)
            synonyms.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            synonyms.put(key,arr);
        }

    }

    public void addWord(String key, String word, int type){
        HashMap<String, ArrayList<String>> hm = null;
        // somehow get a deep copy of the array list objects. how should this be implemented ?
        switch (type){
            case Constants.ANTONYMS:
                if(antonyms == null) {
                    antonyms = new HashMap<>();
                    Log.d(TAG, "Antonyms Null");
                }
                hm = antonyms;
                Log.d(TAG, "Assigned Antonyms with size" + antonyms.size());
                break;
            case Constants.SYNONYMS:
                hm = synonyms;
                break;
            case Constants.SIMILAR:
                hm = similar;
                break;
        }

        if(hm.get(key) == null ){
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            hm.put(key, arr);
            Log.d(TAG, key+ " Key does not exist adding word "+ word +":"+ hm.get(key).size());
        }else if(hm.get(key) != null && !(hm.get(key).contains(word))){
            hm.get(key).add(word);
            Log.d(TAG, key+ " Key exists adding word "+ word +":"+ hm.get(key).size());
        }

    }

    public String[] getWordList(int type, String word){
        String[] arr = {};
        HashMap<String, ArrayList<String>> hm = null;
        switch (type){
            case Constants.ANTONYMS:
                hm = this.antonyms;
                break;
            case Constants.SYNONYMS:
                hm = this.synonyms;
                break;
            case Constants.SIMILAR:
                hm = this.similar;
                break;
        }


        if(hm != null) {
            arr = hm.get(word).toArray(new String[0]);
            return arr;
        }
        else
            return arr;
    }

    public void addAntonyms(String key, String word){
        if(antonyms == null)
            antonyms = new HashMap<>();
        if(antonyms.get(key) != null  &&  !(antonyms.get(key).contains(word)))
            antonyms.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            antonyms.put(key,arr);
        }
    }

    public void addSimilar(String key, String word){
        if(similar == null)
            similar = new HashMap<>();
        if(similar.get(key) != null)
            similar.get(key).add(word);
        else {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(word);
            similar.put(key,arr);
        }
    }

    public String getSerealizedSynonyms() {
        return serealized_synonyms;
    }

    public void setSerealizedSynonyms(String serealized_synonyms) {
        this.serealized_synonyms = serealized_synonyms;
    }

    public String getSerealizedAntonyms() {
        return serealized_antonyms;
    }

    public void setSerealizedAntonyms(String serealized_antonyms) {
        this.serealized_antonyms = serealized_antonyms;
    }

    public String getSerealizedSimiliars() {
        return serealized_similiars;
    }

    public void setSerealizedSimiliars(String serealized_similiars) {
        this.serealized_similiars = serealized_similiars;
    }

    public int getPasswordSelection() {
        return passwordSelection;
    }

    public void setPasswordSelection(int passwordSelection) {
        this.passwordSelection = passwordSelection;
    }

    public String getTrimmedSentence() {
        return trimmed_sentence;
    }

    public void setTrimmedSentence(String trimmed_sentence) {
        this.trimmed_sentence = trimmed_sentence;
    }

    public String getOriginalSentence() {
        return original_sentence;
    }

    public void setOriginalSentence(String original_sentence) {
        this.original_sentence = original_sentence;
    }
}
