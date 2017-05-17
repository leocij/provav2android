package com.lemelo.controle_v5_android_v2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String serverSide;
    private String cookie;
    private String resposta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarregaTelaLogin();

    }

    private void CarregaTelaLogin() {
        setContentView(R.layout.activity_login);

        final ProgressBar pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.GONE);

        final Button btnLoginLogin = (Button) findViewById(R.id.btnLoginLogin);
        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtLoginUsername = (EditText) findViewById(R.id.txtLoginUsername);
                final EditText txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);

                String username = txtLoginUsername.getText().toString();
                String password = txtLoginPassword.getText().toString();
                String postParamaters = "username=" + username + "&password=" + password;

                UserPostAsync userPostAsync = new UserPostAsync();

                // AWS
                serverSide = "http://ec2-54-207-44-125.sa-east-1.compute.amazonaws.com:5000/";

                // Heroku
                //serverSide = "https://provav2.herokuapp.com/";

                // Rede Casa
                //serverSide = "http://192.168.1.7:5000/";

                // Rede Celular
                //serverSide = "http://192.168.43.147:5000/";

                try {

                    pbLogin.setVisibility(View.VISIBLE);
                    userPostAsync.setProgressBar(pbLogin);
                    userPostAsync.setContext(MainActivity.this);
                    resposta = userPostAsync.execute(serverSide + "login",postParamaters).get();

                    if(resposta != null){
                        setCookie(resposta);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(resposta != null){
                    hideSoftKeyboard(MainActivity.this);
                    CarregaTelaPrincipal();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuário ou senha incorretos!", Toast.LENGTH_LONG).show();
                    //CarregaTelaLogin();
                }
            }

            public void hideSoftKeyboard(Activity activity) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            }
        });

        final Button btnLoginCancel = (Button) findViewById(R.id.btnLoginCancel);
        btnLoginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaPrincipal();
            }
        });
    }

    private void CarregaTelaPrincipal() {
        setContentView(R.layout.activity_main);

        final Button btnMainControle = (Button) findViewById(R.id.btnMainControle);
        btnMainControle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CarregaTelaControle();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button btnMainBarganha = (Button) findViewById(R.id.btnMainBarganha);
        btnMainBarganha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CarregaTelaBarganha();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CarregaTelaBarganha() throws InterruptedException, ExecutionException, ParseException, JSONException {
        setContentView(R.layout.activity_barganha);

        imprimeBarganhas();

        final Button btnBarganhaVoltar = (Button) findViewById(R.id.btnBarganhaVoltar);
        btnBarganhaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaPrincipal();
            }
        });
    }

    private void imprimeBarganhas() throws ExecutionException, InterruptedException, JSONException, ParseException {
        BarganhaGetAsync barganhaGetAsync = new BarganhaGetAsync();

        String barganhas = barganhaGetAsync.execute(serverSide + "barganhas", this.getCookie()).get();

        JSONArray arr = null;

        arr = new JSONArray(barganhas);

        List<Barganha> listBarganhas = new ArrayList<>();

        for(int i = 0; i < arr.length(); i++){
            Barganha b1 = new Barganha();
            JSONObject jsonObject = arr.getJSONObject(i);

            if(jsonObject.has("identifier")){
                b1.setIdentifier(jsonObject.getLong("identifier"));
            }

            if(jsonObject.has("data")){
                String strData = jsonObject.getString("data").toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date utilData = new Date(dateFormat.parse(strData).getTime());
                java.sql.Date sqlData = new java.sql.Date(utilData.getTime());
                b1.setData(sqlData);
            }

            if(jsonObject.has("descricao")){
                b1.setDescricao(jsonObject.getString("descricao"));
            }

            if(jsonObject.has("valor")){
                b1.setValor(new BigDecimal(jsonObject.getDouble("valor")));
            }

            listBarganhas.add(b1);

            ArrayAdapter<Barganha> arrayAdapter = new ArrayAdapter<Barganha>(MainActivity.this, android.R.layout.simple_list_item_1, listBarganhas);
            ListView lvBarganhas = (ListView) findViewById(R.id.lvBarganhas);
            lvBarganhas.setAdapter(arrayAdapter);
        }
    }

    private void CarregaTelaControle() throws ExecutionException, InterruptedException, JSONException, ParseException {
        setContentView(R.layout.activity_controle);

        final EditText txtControleData = (EditText) findViewById(R.id.txtControleData);
        final EditText txtControleDescricao = (EditText) findViewById(R.id.txtControleDescricao);
        final EditText txtControleEntrada = (EditText) findViewById(R.id.txtControleEntrada);
        final EditText txtControleSaida = (EditText) findViewById(R.id.txtControleSaida);

        final SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        txtControleData.setText(data.format(Calendar.getInstance().getTime()));

        final ProgressBar pbControle = (ProgressBar) findViewById(R.id.pbControle);
        pbControle.setVisibility(View.GONE);

        final Button btnControleSalvar = (Button) findViewById(R.id.btnControleSalvar);
        btnControleSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject postControles = new JSONObject();

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date d = sdf.parse(txtControleData.getText().toString());
                    java.sql.Date dataSql = new java.sql.Date(d.getTime());
                    postControles.put("data",dataSql);
                    postControles.put("descricao",txtControleDescricao.getText().toString());

                    String entradaStr = txtControleEntrada.getText().toString();

                    if(entradaStr.equals("")){
                        entradaStr = "0.0";
                    }

                    postControles.put("entrada",new BigDecimal(entradaStr));

                    String saidaStr = txtControleSaida.getText().toString();

                    if(saidaStr.equals("")){
                        saidaStr = "0.0";
                    }
                    postControles.put("saida",new BigDecimal(saidaStr));

                    ControlePostAsync controlePostAsync = new ControlePostAsync();
                    pbControle.setVisibility(View.VISIBLE);
                    controlePostAsync.setProgressBar(pbControle);
                    controlePostAsync.setContext(MainActivity.this);
                    String postRetorno = controlePostAsync.execute(serverSide + "controles", postControles.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button btnControleVoltar = (Button) findViewById(R.id.btnControleVoltar);
        btnControleVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaPrincipal();
            }
        });

        final Button btControleMostrar = (Button) findViewById(R.id.btControleMostrar);
        btControleMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imprimeControles();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void imprimeControles() throws ExecutionException, InterruptedException, JSONException, ParseException {
        setContentView(R.layout.activity_imprime_controles);

        final ProgressBar pbImprimeControles = (ProgressBar) findViewById(R.id.pbImprimeControles);
        pbImprimeControles.setVisibility(View.GONE);

        final Button btnImprimeControlesVoltar = (Button) findViewById(R.id.btnImprimeControlesVoltar);
        btnImprimeControlesVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CarregaTelaControle();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //System.out.println("Cookie no Post: " + this.getCookie());

        ControleGetAsync controleGetAsync = new ControleGetAsync();
        pbImprimeControles.setVisibility(View.VISIBLE);
        controleGetAsync.setProgressBar(pbImprimeControles);
        controleGetAsync.setContext(MainActivity.this);

        String controles = controleGetAsync.execute(serverSide + "controles", this.getCookie()).get();

        JSONArray arr = null;

        arr = new JSONArray(controles);

        //List<String> list = new ArrayList<String>();
        List<Controle> listControles = new ArrayList<>();

        //DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        for(int i = 0; i < arr.length(); i++){
            Controle c1 = new Controle();
            JSONObject jsonObject = arr.getJSONObject(i);

            if(jsonObject.has("identifier")){
                c1.setIdentifier(jsonObject.getLong("identifier"));
            }

            if(jsonObject.has("data")){
                String strData = jsonObject.getString("data").toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date utilData = new Date(dateFormat.parse(strData).getTime());
                java.sql.Date sqlData = new java.sql.Date(utilData.getTime());
                c1.setData(sqlData);
            }

            if(jsonObject.has("descricao")){
                c1.setDescricao(jsonObject.getString("descricao"));
            }

            if(jsonObject.has("entrada")){
                c1.setEntrada(new BigDecimal(jsonObject.getDouble("entrada")));
            }

            if(jsonObject.has("saida")){
                c1.setSaida(new BigDecimal(jsonObject.getDouble("saida")));
            }

            listControles.add(c1);

            ArrayAdapter<Controle> arrayAdapter = new ArrayAdapter<Controle>(MainActivity.this, android.R.layout.simple_list_item_1, listControles);
            ListView lvImprimeControles = (ListView) findViewById(R.id.lvImprimeControles);
            lvImprimeControles.setAdapter(arrayAdapter);
        }
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}


