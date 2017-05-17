package com.lemelo.controle_v5_android_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by leoci on 16/05/2017.
 */

class ControlePostAsync extends AsyncTask<String, String, String> {

    ProgressBar progressBar;
    Context context;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        String data = "";

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Cookie","JSESSIONID=" + params[2]);
            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            int codeResponse = httpURLConnection.getResponseCode();

            if (codeResponse != 201) {
                throw new RuntimeException("Erro HTTP: " + httpURLConnection.getResponseCode());
            }

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, "Dados salvos", Toast.LENGTH_LONG).show();

    }

    protected void onProgressUpdate(Integer... progress){
        progressBar.setProgress(progress[0]);
    }

}
