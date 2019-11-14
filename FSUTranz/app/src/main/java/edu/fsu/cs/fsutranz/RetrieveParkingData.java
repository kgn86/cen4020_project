package edu.fsu.cs.fsutranz;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class RetrieveParkingData extends AsyncTask <String, Void, String>{

     protected String[] doInBackground() {
            String data;
            String delims = "[,]";
            String uri = "https://obs-web05.bfs.fsu.edu/count2.html";
            try {
                Document document = Jsoup.connect(uri).get();
                data = document.clone().text();
                return data.split(delims);
            }
            catch(IOException e){

            }
            return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
