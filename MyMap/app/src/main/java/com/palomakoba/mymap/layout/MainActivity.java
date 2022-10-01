package com.palomakoba.mymap.layout;

/*
                    ╔═══════════════════════════════════════════════════════════════╗
                    ║                  *** Instituto Eldorado ***                   ║
                    ║   *** Fundação Universidade Federal de Rondônia - UNIR ***    ║
                    ║           *** Bacharelado em Ciência da Computação ***        ║
                    ║                       *** Palomakoba ***                      ║
                    ╚═══════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Disciplina: Desenvolvimento na Plataforma Android II                   ║
            ║         Professor: LUCAS MARQUES DA CUNHA                                      ║
            ║                                                                                ║
            ║         Data da Tarefa 09/09/2022 a 30/09/2022                                 ║
            ║         Data de criação 13/09/2022                                             ║
            ║         Ultima alteração 30/09/2022                                            ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Este projeto foi desenvolvido por Jonathan Oliveira Pinheiro da Costa  ║
            ║              * e-mail: contatojonathan1999@gmail.com                           ║
            ║              * github: https://github.com/Jonathan2379                         ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Componentes do aplicativo:                                             ║
            ║             * Activitys, classes e interface java, drawables, Toobars.         ║
            ║             * Botões, EditTexts, TexView, Recyclerview, ImageViews.            ║
            ║             * validação de campos, Toast, métotodos.                           ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         *** Das informações do compilador e sistema ***                        ║
            ║                                                                                ║
            ║               Android Studio Chipmunk | 2021.2.1 Patch 2                       ║
            ║                Build #AI-212.5712.43.2112.8815526, built on July 10, 2022      ║
            ║                Runtime version: 11.0.12+7-b1504.28-7817840 amd64               ║
            ║                VM: OpenJDK 64-Bit Server VM by Oracle Corporation              ║
            ║                Windows 10 10.0                                                 ║
            ║                GC: G1 Young Generation, G1 Old Generation                      ║
            ║                Memory: 1280M                                                   ║
            ║                Cores: 4                                                        ║
            ║                Registry: external.system.auto.import.disabled=true             ║
            ║                Non-Bundled Plugins: com.tabnine.TabNine (0.7.25),              ║
            ║                com.intellij.marketplace (212.5712.51)                          ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palomakoba.mymap.util.CEP;
import com.palomakoba.mymap.util.CEPService;
import com.palomakoba.mymap.R;
import com.palomakoba.mymap.bancoDeDados.BancoDeDados;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Button btn_busca_endereco, btn_ver_endereco, btn_exibe_endereco;
    private EditText num_cep;
    private Retrofit retrofit;

    private BancoDeDados bd;

    private SensorManager sensorManager;
    private Sensor acelerometro;


    private LocationListener locationListener;
    private LocationManager locationManager;


    private String cep1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscaID();

        buscaCEPonRetrofit();

        //Sensor acelerometro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btn_busca_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escondeTeclado(view);
                buscarCEP();
            }
        });

        btn_ver_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);

                intent.putExtra("tipo", "0");
                startActivity(intent);
            }
        });

        btn_exibe_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaEndereco.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(MainActivity.this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);

        //sensor bateria
        //se houver alteração no nível da bateria, vai chamar o metodo: levelBatteryReceiver
        this.registerReceiver(levelBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        this.unregisterReceiver(this.levelBatteryReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] > 17.8) {

            Toast.makeText(this, "Saindo do aplicativo!", Toast.LENGTH_SHORT).show();

            Thread thread = new Thread();       // para dar o efeito de finaliznado
            try {
                thread.sleep(500);       // faz a thread principal parar por meio segundo
            } catch (InterruptedException e) {
                Log.d("Failure", "Erro ao parar a thread" + e.getMessage());
            }

            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private void buscaCEPonRetrofit() {
        String urlApi = "https://viacep.com.br/ws/";
        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void buscaID() {
        btn_busca_endereco = findViewById(R.id.btn_busca_endereco);
        btn_ver_endereco = findViewById(R.id.btn_ver_endereco);
        btn_exibe_endereco = findViewById(R.id.btn_exibe_endereco);
        num_cep = findViewById(R.id.num_cep);
    }

    private void escondeTeclado(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void buscarCEP() {
        cep1 = num_cep.getText().toString();

        if (cep1.length() != 8) {
            Toast.makeText(this, "Um cep válido tem 8 digitos!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Buscando CEP...", Toast.LENGTH_SHORT).show();

            CEPService cepService = retrofit.create(CEPService.class);
            Call<CEP> call = cepService.recuperarCEP(cep1);
            call.enqueue(new Callback<CEP>() {
                @Override
                public void onResponse(Call<CEP> call, Response<CEP> response) {
                    CEP cep = response.body();
                    janelaDialog(cep);
                }

                @Override
                public void onFailure(Call<CEP> call, Throwable t) {
                    Log.d("Failure", "Houve um erro ao buscar o CEP"+"\n"+t.getMessage());
                    Toast.makeText(MainActivity.this, "Erro ao encontrar CEP. Tente verificar sua rede.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void janelaDialog(@NonNull CEP cep) {
        bd = new BancoDeDados(getApplicationContext());

        if (cep.getCep() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.fundo_dialogo);
            builder.setIcon(R.drawable.mapadialog);
            builder.setMessage("O cep: " + cep1 + ", está situado em: \n\n" + cep.getLogradouro() +
                    ", " + cep.getComplemento() + "\n" + cep.getBairro() + " - " + cep.getLocalidade() + "/" + cep.getUf());
            builder.setCancelable(false);
            builder.setNegativeButton("Salvar no banco de dados", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContentValues cv = new ContentValues();

                    cv.put("cep", cep.getCep());
                    cv.put("logradouro", cep.getLogradouro());
                    cv.put("complemento", cep.getComplemento());
                    cv.put("bairro", cep.getBairro());
                    cv.put("localidade", cep.getLocalidade());
                    cv.put("uf", cep.getUf());

                    long sucesso_ao_inserir = bd.inserir(cv);

                    //verifica se inseriu com sucesso
                    if (sucesso_ao_inserir != -1){
                        Toast.makeText(MainActivity.this, "Seu CEP foi salvo!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MainActivity.this, "Houve um erro inesperado ao inserir seu cep!", Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNeutralButton("OK", null);
            builder.show();
            num_cep.setText("");

        } else {
            Toast.makeText(MainActivity.this, "Desculpe, este CEP não foi encontrado ou não existe.", Toast.LENGTH_LONG).show();
        }
    }

    public BroadcastReceiver levelBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pega o nível atual da bateria
            int nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            if (nivel < 50)
                Toast.makeText(context, "O nível da bateria está abaixo de 50% ", Toast.LENGTH_SHORT).show();
        }
    };
}