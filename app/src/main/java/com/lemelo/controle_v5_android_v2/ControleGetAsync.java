package com.lemelo.controle_v5_android_v2;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by leoci on 26/04/2017.
 */

public class ControleGetAsync extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        StringBuffer buffer = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Cookie","JSESSIONID=" + params[1]);
            httpURLConnection.setDoInput(true);

            int codeResponse = httpURLConnection.getResponseCode();

            if (codeResponse != 200) {
                throw new RuntimeException("Erro HTTP: " + httpURLConnection.getResponseCode());
            }

            InputStream inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String linha;
            buffer = new StringBuffer();
            while((linha = reader.readLine()) != null){
                buffer.append(linha);
                buffer.append("\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    @Override
    protected void onPostExecute(String buffer){

    }
}

