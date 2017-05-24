package com.lemelo.controle_v5_android_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by leoci on 26/04/2017.
 */

class UserPostAsync extends AsyncTask<String,Integer,String> {
    private String cookie;
    ProgressBar progressBar;
    MainActivity context;
    private ProgressDialog progressDialog = null;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String data = "";
        String[] cookies2 = new String[0];
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(params[1].getBytes().length);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            //TODO pegar Token.

            int codeResponse = httpURLConnection.getResponseCode();

            cookie = httpURLConnection.getHeaderField("Set-Cookie");

            String[] cookies1 = cookie.split(";");
            cookies2 = cookies1[0].split("=");

            //setCookie(cookies2[1]);

            //System.out.println("Cookie: " + cookie);
            //System.out.println("Cookie2[1]: " + cookies2[1]);

            if (codeResponse != 200) {
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

        //System.out.println("Data: " + data);
        if(cookies2.length>0){
            return cookies2[1];
        } else {
            return null;
        }
    }

    @Override
    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(context, "Passei onPreExecute", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, "Bem Vindo!", Toast.LENGTH_LONG).show();
    }

    protected void onProgressUpdate(Integer... progress){
        super.onProgressUpdate(progress);
        Toast.makeText(context, "Passei onProgressUpdate: " + progress, Toast.LENGTH_LONG).show();
        if(this.progressBar!=null){
            progressBar.setProgress(progress[0]);
        }
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


}
