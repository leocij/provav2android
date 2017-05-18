package com.lemelo.controle_v5_android_v2;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
    private Controle controle;
    private Acao acao;

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

        final Button btnMainEmprestimo = (Button) findViewById(R.id.btnMainEmprestimo);
        btnMainEmprestimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaEmprestimo();
            }
        });

        final Button btnMainParcelamento = (Button) findViewById(R.id.btnMainParcelamento);
        btnMainParcelamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaParcelamento();
            }
        });

    }

    private void CarregaTelaParcelamento() {
        setContentView(R.layout.activity_parcelamento);
    }

    private void CarregaTelaEmprestimo() {
        setContentView(R.layout.activity_emprestimo);

        final EditText txtEmprestimoData = (EditText) findViewById(R.id.txtEmprestimoData);
        final EditText txtEmprestimoDescricao = (EditText) findViewById(R.id.txtEmprestimoDescricao);
        final EditText txtEmprestimoValor = (EditText) findViewById(R.id.txtEmprestimoValor);
        final EditText txtEmprestimoLimitePgto = (EditText) findViewById(R.id.txtEmprestimoLimitePgto);

        SimpleDateFormat formatEmprestimo = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        txtEmprestimoData.setText(formatEmprestimo.format(Calendar.getInstance().getTime()));
        formatEmprestimo = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.YEAR, 2);
        txtEmprestimoLimitePgto.setText(formatEmprestimo.format(c.getTime()));

        final ProgressBar pbEmprestimo = (ProgressBar) findViewById(R.id.pbEmprestimo);
        pbEmprestimo.setVisibility(View.GONE);

        final Button btnEmprestimoSalvar = (Button) findViewById(R.id.btnEmprestimoSalvar);
        final SimpleDateFormat finalFormatEmprestimo = formatEmprestimo;
        btnEmprestimoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postEmprestimos = new JSONObject();

                try {
                    java.util.Date utilEmprestimo = finalFormatEmprestimo.parse(txtEmprestimoData.getText().toString());
                    java.sql.Date sqlEmprestimo = new java.sql.Date(utilEmprestimo.getTime());
                    postEmprestimos.put("data", sqlEmprestimo);
                    postEmprestimos.put("descricao", txtEmprestimoDescricao.getText().toString());
                    String valorEmprestimo = txtEmprestimoValor.getText().toString();
                    if(valorEmprestimo.equals("")){
                        valorEmprestimo = "0.0";
                    }
                    postEmprestimos.put("valor", new BigDecimal(valorEmprestimo));
                    utilEmprestimo = finalFormatEmprestimo.parse(txtEmprestimoLimitePgto.getText().toString());
                    sqlEmprestimo = new java.sql.Date(utilEmprestimo.getTime());
                    postEmprestimos.put("limitepgto", sqlEmprestimo);

                    ControlePostAsync controlePostAsync = new ControlePostAsync();
                    pbEmprestimo.setVisibility(View.VISIBLE);
                    controlePostAsync.setProgressBar(pbEmprestimo);
                    controlePostAsync.setContext(MainActivity.this);
                    String postRetorno = controlePostAsync.execute(serverSide + "emprestimos", postEmprestimos.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button btnEmprestimoMostrar = (Button) findViewById(R.id.btnEmprestimoMostrar);
        btnEmprestimoMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imprimeEmprestimos();
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

        final Button btnEmprestimoVoltar = (Button) findViewById(R.id.btnEmprestimoVoltar);
        btnEmprestimoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaPrincipal();
            }
        });
    }

    private void imprimeEmprestimos() throws ExecutionException, InterruptedException, JSONException, ParseException {
        setContentView(R.layout.activity_imprime_emprestimos);
        final ProgressBar pbImprimeEmprestimos = (ProgressBar) findViewById(R.id.pbImprimeEmprestimos);
        pbImprimeEmprestimos.setVisibility(View.GONE);

        final Button btnImprimeEmprestimosVoltar = (Button) findViewById(R.id.btnImprimeEmprestimosVoltar);
        btnImprimeEmprestimosVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaEmprestimo();
            }
        });

        ControleGetAsync controleGetAsync = new ControleGetAsync();
        pbImprimeEmprestimos.setVisibility(View.VISIBLE);
        controleGetAsync.setProgressBar(pbImprimeEmprestimos);
        controleGetAsync.setContext(MainActivity.this);

        String emprestimos = controleGetAsync.execute(serverSide + "emprestimos", this.getCookie()).get();

        JSONArray jsonArrayEmprestimos = new JSONArray(emprestimos);

        List<Emprestimo> listEmprestimos = new ArrayList<>();
        for(int i = 0; i < jsonArrayEmprestimos.length(); i++){
            Emprestimo e1 = new Emprestimo();
            JSONObject jsonImprimeEmprestimos = jsonArrayEmprestimos.getJSONObject(i);

            if(jsonImprimeEmprestimos.has("identifier")){
                e1.setIdentifier(jsonImprimeEmprestimos.getLong("identifier"));
            }
            if(jsonImprimeEmprestimos.has("data")){
                String dataImprimeEmprestimos = jsonImprimeEmprestimos.getString("data").toString();
                DateFormat formatImprimeEmprestimos = new SimpleDateFormat("yyyy-MM-dd");
                Date utilImprimeEmprestimos = new Date(formatImprimeEmprestimos.parse(dataImprimeEmprestimos).getTime());
                java.sql.Date sqlImprimeEmprestimos = new java.sql.Date(utilImprimeEmprestimos.getTime());
                e1.setData(sqlImprimeEmprestimos);
            }
            if(jsonImprimeEmprestimos.has("descricao")){
                e1.setDescricao(jsonImprimeEmprestimos.getString("descricao"));
            }
            if(jsonImprimeEmprestimos.has("valor")){
                e1.setValor(new BigDecimal(jsonImprimeEmprestimos.getDouble("valor")));
            }
            if(jsonImprimeEmprestimos.has("limitepgto")){
                String dataImprimeEmprestimos = jsonImprimeEmprestimos.getString("limitepgto").toString();
                DateFormat formatImprimeEmprestimos = new SimpleDateFormat("yyyy-MM-dd");
                Date utilImprimeEmprestimos = new Date(formatImprimeEmprestimos.parse(dataImprimeEmprestimos).getTime());
                java.sql.Date sqlImprimeEmprestimos = new java.sql.Date(utilImprimeEmprestimos.getTime());
                e1.setLimitepgto(sqlImprimeEmprestimos);
            }
            listEmprestimos.add(e1);
        }

        ArrayAdapter<Emprestimo> emprestimoArrayAdapter = new ArrayAdapter<Emprestimo>(MainActivity.this,android.R.layout.simple_list_item_1,listEmprestimos);
        ListView lvImprimeEmprestimos = (ListView) findViewById(R.id.lvImprimeEmprestimos);
        lvImprimeEmprestimos.setAdapter(emprestimoArrayAdapter);

        lvImprimeEmprestimos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emprestimo emprestimoSelecionado = (Emprestimo) parent.getItemAtPosition(position);
                trataEmprestimoSelecionado(emprestimoSelecionado);
            }
        });
    }

    private void trataEmprestimoSelecionado(final Emprestimo emprestimoSelecionado) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Editar / Apagar?");
        dialogo.setMessage(emprestimoSelecionado.toString());

        dialogo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CarregaTelaEditarEmprestimo(emprestimoSelecionado);
            }
        });

        dialogo.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Long idDelete = emprestimoSelecionado.getIdentifier();
                ControleDeleteAsync controleDeleteAsync = new ControleDeleteAsync();
                controleDeleteAsync.execute(serverSide + "emprestimos/" + idDelete, MainActivity.this.getCookie());

                try {
                    imprimeEmprestimos();
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

        dialogo.setNeutralButton("Cancelar", null);
        dialogo.show();
    }

    private void CarregaTelaEditarEmprestimo(final Emprestimo emprestimoSelecionado) {
        setContentView(R.layout.activity_edita_emprestimo);
        final EditText txtEditaEmprestimoData = (EditText) findViewById(R.id.txtEditaEmprestimoData);
        txtEditaEmprestimoData.setText(emprestimoSelecionado.getData().toString());
        final EditText txtEditaEmprestimoDescricao = (EditText) findViewById(R.id.txtEditaEmprestimoDescricao);
        txtEditaEmprestimoDescricao.setText(emprestimoSelecionado.getDescricao().toString());
        final EditText txtEditaEmprestimoValor = (EditText) findViewById(R.id.txtEditaEmprestimoValor);
        txtEditaEmprestimoValor.setText(emprestimoSelecionado.getValor().toString());
        final EditText txtEditaEmprestimoLimitePgto = (EditText) findViewById(R.id.txtEditaEmprestimoLimitePgto);
        txtEditaEmprestimoLimitePgto.setText(emprestimoSelecionado.getLimitepgto().toString());

        final ProgressBar pbEditaEmprestimo = (ProgressBar) findViewById(R.id.pbEditaEmprestimo);
        pbEditaEmprestimo.setVisibility(View.GONE);

        final Button btnEditaEmprestimoSalvar = (Button) findViewById(R.id.btnEditaEmprestimoSalvar);
        btnEditaEmprestimoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    java.util.Date date = simpleDateFormat.parse(txtEditaEmprestimoData.getText().toString());
                    java.sql.Date date1 = new java.sql.Date(date.getTime());
                    jsonObject.put("data",date1);
                    jsonObject.put("descricao",txtEditaEmprestimoDescricao.getText().toString());
                    String s = txtEditaEmprestimoValor.getText().toString();
                    if(s.equals("")){
                        s = "0.0";
                    }
                    jsonObject.put("valor",new BigDecimal(s));
                    date = simpleDateFormat.parse(txtEditaEmprestimoLimitePgto.getText().toString());
                    date1 = new java.sql.Date(date.getTime());
                    jsonObject.put("limitepgto",date1);

                    ControlePutAsync controlePutAsync = new ControlePutAsync();
                    pbEditaEmprestimo.setVisibility(View.VISIBLE);
                    controlePutAsync.setProgressBar(pbEditaEmprestimo);
                    controlePutAsync.setContext(MainActivity.this);
                    String postRetorno = controlePutAsync.execute(serverSide + "emprestimos/" + emprestimoSelecionado.getIdentifier(), jsonObject.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

        final Button btnEditaEmprestimoCancelar = (Button) findViewById(R.id.btnEditaEmprestimoCancelar);
        btnEditaEmprestimoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imprimeEmprestimos();
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

    private void CarregaTelaBarganha() throws InterruptedException, ExecutionException, ParseException, JSONException {
        setContentView(R.layout.activity_barganha);

        final EditText txtBarganhaData = (EditText) findViewById(R.id.txtBarganhaData);
        final EditText txtBarganhaDescricao = (EditText) findViewById(R.id.txtBarganhaDescricao);
        final EditText txtBarganhaValor = (EditText) findViewById(R.id.txtBarganhaValor);

        final SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        txtBarganhaData.setText(data.format(Calendar.getInstance().getTime()));

        final ProgressBar pbBarganha = (ProgressBar) findViewById(R.id.pbBarganha);
        pbBarganha.setVisibility(View.GONE);

        final Button btnBarganhaSalvar = (Button) findViewById(R.id.btnBarganhaSalvar);
        btnBarganhaSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postBarganhas = new JSONObject();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    java.util.Date d = sdf.parse(txtBarganhaData.getText().toString());
                    java.sql.Date dataSql = new java.sql.Date(d.getTime());
                    postBarganhas.put("data",dataSql);
                    postBarganhas.put("descricao",txtBarganhaDescricao.getText().toString());
                    String valorStr = txtBarganhaValor.getText().toString();
                    if(valorStr.equals("")){
                        valorStr = "0.0";
                    }
                    postBarganhas.put("valor", new BigDecimal(valorStr));

                    ControlePostAsync controlePostAsync = new ControlePostAsync();
                    pbBarganha.setVisibility(View.VISIBLE);
                    controlePostAsync.setProgressBar(pbBarganha);
                    controlePostAsync.setContext(MainActivity.this);
                    String postRetorno = controlePostAsync.execute(serverSide + "barganhas", postBarganhas.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button txtBarganhaMostra = (Button) findViewById(R.id.txtBarganhaMostra);
        txtBarganhaMostra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imprimeBarganhas();
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

        final Button btnBarganhaVoltar = (Button) findViewById(R.id.btnBarganhaVoltar);
        btnBarganhaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregaTelaPrincipal();
            }
        });
    }

    private void imprimeBarganhas() throws ExecutionException, InterruptedException, JSONException, ParseException {
        setContentView(R.layout.activity_imprime_barganhas);
        final BarganhaGetAsync barganhaGetAsync = new BarganhaGetAsync();

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
            ListView lvImprimeBarganhas = (ListView) findViewById(R.id.lvImprimeBarganhas);
            lvImprimeBarganhas.setAdapter(arrayAdapter);

            lvImprimeBarganhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Barganha barganhaSelecionado = (Barganha) parent.getItemAtPosition(position);
                    trataBarganhaSelecionado(barganhaSelecionado);
                }
            });
        }

        final Button btnImprimeBarganhasVoltar = (Button) findViewById(R.id.btnImprimeBarganhasVoltar);
        btnImprimeBarganhasVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CarregaTelaBarganha();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void trataBarganhaSelecionado(final Barganha barganhaSelecionado) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Editar / Apagar?");
        dialogo.setMessage(barganhaSelecionado.toString());

        dialogo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CarregaTelaEditarBarganha(barganhaSelecionado);

            }
        });

        dialogo.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Long idDelete = barganhaSelecionado.getIdentifier();
                ControleDeleteAsync controleDeleteAsync = new ControleDeleteAsync();
                controleDeleteAsync.execute(serverSide + "barganhas/" + idDelete, MainActivity.this.getCookie());

                try {
                    imprimeBarganhas();
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

        dialogo.setNeutralButton("Cancelar", null);
        dialogo.show();
    }

    private void CarregaTelaEditarBarganha(final Barganha barganhaSelecionado) {
        setContentView(R.layout.activity_edita_barganha);

        final EditText txtEditaBarganhaData = (EditText) findViewById(R.id.txtEditaBarganhaData);
        txtEditaBarganhaData.setText(barganhaSelecionado.getData().toString());
        final EditText txtEditaBarganhaDescricao = (EditText) findViewById(R.id.txtEditaBarganhaDescricao);
        txtEditaBarganhaDescricao.setText(barganhaSelecionado.getDescricao().toString());
        final EditText txtEditaBarganhaValor = (EditText) findViewById(R.id.txtEditaBarganhaValor);
        txtEditaBarganhaValor.setText(barganhaSelecionado.getValor().toString());

        final ProgressBar pbEditaBarganha = (ProgressBar) findViewById(R.id.pbEditaBarganha);
        pbEditaBarganha.setVisibility(View.GONE);

        final Button btnEditaBarganhaSalvar = (Button) findViewById(R.id.btnEditaBarganhaSalvar);
        btnEditaBarganhaSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonEditaBarganha = new JSONObject();
                final SimpleDateFormat formatEditaBarganha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    java.util.Date utilEditaBarganha = formatEditaBarganha.parse(txtEditaBarganhaData.getText().toString());
                    java.sql.Date sqlEditaBarganha = new java.sql.Date(utilEditaBarganha.getTime());
                    jsonEditaBarganha.put("data", sqlEditaBarganha);
                    jsonEditaBarganha.put("descricao", txtEditaBarganhaDescricao.getText().toString());
                    String valorEditaBarganha = txtEditaBarganhaValor.getText().toString();
                    if(valorEditaBarganha.equals("")){
                        valorEditaBarganha = "0.0";
                    }
                    jsonEditaBarganha.put("valor", new BigDecimal(valorEditaBarganha));

                    ControlePutAsync controlePutAsync = new ControlePutAsync();
                    pbEditaBarganha.setVisibility(View.VISIBLE);
                    controlePutAsync.setProgressBar(pbEditaBarganha);
                    controlePutAsync.setContext(MainActivity.this);
                    String postRetorno = controlePutAsync.execute(serverSide + "barganhas/" + barganhaSelecionado.getIdentifier(), jsonEditaBarganha.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button btnEditaBarganhaCancelar = (Button) findViewById(R.id.btnEditaBarganhaCancelar);
        btnEditaBarganhaCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imprimeBarganhas();
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

        for(int i = 0; i < arr.length(); i++) {
            Controle c1 = new Controle();
            JSONObject jsonObject = arr.getJSONObject(i);

            if (jsonObject.has("identifier")) {
                c1.setIdentifier(jsonObject.getLong("identifier"));
            }

            if (jsonObject.has("data")) {
                String strData = jsonObject.getString("data").toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date utilData = new Date(dateFormat.parse(strData).getTime());
                java.sql.Date sqlData = new java.sql.Date(utilData.getTime());
                c1.setData(sqlData);
            }

            if (jsonObject.has("descricao")) {
                c1.setDescricao(jsonObject.getString("descricao"));
            }

            if (jsonObject.has("entrada")) {
                c1.setEntrada(new BigDecimal(jsonObject.getDouble("entrada")));
            }

            if (jsonObject.has("saida")) {
                c1.setSaida(new BigDecimal(jsonObject.getDouble("saida")));
            }
            listControles.add(c1);
        }


            ArrayAdapter<Controle> arrayAdapter = new ArrayAdapter<Controle>(MainActivity.this, android.R.layout.simple_list_item_1, listControles);
            ListView lvImprimeControles = (ListView) findViewById(R.id.lvImprimeControles);
            lvImprimeControles.setAdapter(arrayAdapter);

            lvImprimeControles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    Controle controleSelecionado = (Controle) parent.getItemAtPosition(position);
                    trataControleSelecionado(controleSelecionado);
                }
            });
    }

    private void trataControleSelecionado(final Controle controleSelecionado) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Editar / Apagar?");
        dialogo.setMessage(controleSelecionado.toString());

        dialogo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controle = controleSelecionado;
                acao = Acao.EDITAR;

                CarregaTelaEditarControle(controleSelecionado);

            }
        });

        dialogo.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Long idDelete = controleSelecionado.getIdentifier();
                ControleDeleteAsync controleDeleteAsync = new ControleDeleteAsync();
                controleDeleteAsync.execute(serverSide + "controles/" + idDelete, MainActivity.this.getCookie());

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

        dialogo.setNeutralButton("Cancelar", null);
        dialogo.show();
    }

    private void CarregaTelaEditarControle(final Controle controleSelecionado) {
        setContentView(R.layout.activity_edita_controle);

        final EditText txtEditaControleData = (EditText) findViewById(R.id.txtEditaControleData);
        txtEditaControleData.setText(controleSelecionado.getData().toString());
        final EditText txtEditaControleDescricao = (EditText) findViewById(R.id.txtEditaControleDescricao);
        txtEditaControleDescricao.setText(controleSelecionado.getDescricao().toString());
        final EditText txtEditaControleEntrada = (EditText) findViewById(R.id.txtEditaControleEntrada);
        txtEditaControleEntrada.setText(controleSelecionado.getEntrada().toString());
        final EditText txtEditaControleSaida = (EditText) findViewById(R.id.txtEditaControleSaida);
        txtEditaControleSaida.setText(controleSelecionado.getSaida().toString());

        final ProgressBar pbEditaControle = (ProgressBar) findViewById(R.id.pbEditaControle);
        pbEditaControle.setVisibility(View.GONE);

        final Button btnEditaControleSalvar = (Button) findViewById(R.id.btnEditaControleSalvar);
        btnEditaControleSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonEditaControle = new JSONObject();

                final SimpleDateFormat formatEditaControle = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    java.util.Date utilEditaControle = formatEditaControle.parse(txtEditaControleData.getText().toString());
                    java.sql.Date sqlEditaControle = new java.sql.Date(utilEditaControle.getTime());
                    jsonEditaControle.put("data",sqlEditaControle);
                    jsonEditaControle.put("descricao",txtEditaControleDescricao.getText().toString());
                    String entradaEditaControle = txtEditaControleEntrada.getText().toString();
                    if(entradaEditaControle.equals("")){
                        entradaEditaControle = "0.0";
                    }
                    jsonEditaControle.put("entrada",new BigDecimal(entradaEditaControle));
                    String saidaEditaControle = txtEditaControleSaida.getText().toString();
                    if(saidaEditaControle.equals("")){
                        saidaEditaControle = "0.0";
                    }
                    jsonEditaControle.put("saida", new BigDecimal(saidaEditaControle));

                    ControlePutAsync controlePutAsync = new ControlePutAsync();
                    pbEditaControle.setVisibility(View.VISIBLE);
                    controlePutAsync.setProgressBar(pbEditaControle);
                    controlePutAsync.setContext(MainActivity.this);
                    String postRetorno = controlePutAsync.execute(serverSide + "controles/" + controleSelecionado.getIdentifier(), jsonEditaControle.toString(), MainActivity.this.getCookie()).get();

                    System.out.println(postRetorno);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


        final Button btnEditaControleCancelar = (Button) findViewById(R.id.btnEditaControleCancelar);
        btnEditaControleCancelar.setOnClickListener(new View.OnClickListener() {
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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}


