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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.palomakoba.mymap.util.Permissions;
import com.palomakoba.mymap.R;
import com.palomakoba.mymap.databinding.ActivityMaps2Binding;

import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private ImageView back, limpar, busca;
    private EditText edt_busca;

    private GoogleMap mMap;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private int i = 1;  //para ver lista de ceps ou ver localização atual.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMaps2Binding binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Permissions.validatePermission(permissoes, this, 1);

        buscaID();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                edt_busca.setText("");
            }
        });

    }

    private void buscaID() {
        back = findViewById(R.id.back);
        limpar = findViewById(R.id.limpar);
        busca = findViewById(R.id.busca);
        edt_busca = findViewById(R.id.edt_busca);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        Intent intent = getIntent();
        String local = intent.getStringExtra("tipo");

        String endereco = "";

        if (local.equals("0")) {

            Toast.makeText(this, "Aguarde enquanto busca sua localização Atual!", Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //pega as coordenadas geograficas
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();

                    //cria um objeto que armazena as coordenadas
                    LatLng meu_local = new LatLng(latitude, longitude);

                    if (i == 1) {
                        mMap.clear();   //limpa o mapa
                        //Adiciona uma marcação e da zoom no mapa
                        mMap.addMarker(new MarkerOptions().position(meu_local).title("Você esta aqui!"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meu_local, 16));
                        i = 0;
                    }
                }
            };

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    Double latitude = latLng.latitude;
                    Double longitude = latLng.longitude;
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Novo local").snippet("Local do alfinete inserido! ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
            });

            pedirPermissao();

        } else if (local.equals("2")) {
            endereco = intent.getStringExtra("endereco");

            LatLng locationcep = latLngFromAddress(getApplicationContext(), endereco);
            mMap.addMarker(new MarkerOptions().position(locationcep).title("Seu CEP"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationcep, 16));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    Double latitude = latLng.latitude;
                    Double longitude = latLng.longitude;
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Novo local").snippet("Local do alfinete inserido! ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                }
            });
        }

        busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busca_endereco = edt_busca.getText().toString();

                try {
                    if (busca_endereco.isEmpty())
                        Toast.makeText(MapsActivity2.this, "Por favor insira um local para a busca!", Toast.LENGTH_SHORT).show();
                    else {

                        AlertDialog.Builder janela = new AlertDialog.Builder(MapsActivity2.this, R.style.fundo_dialogo);
                        janela.setTitle("Nova busca");
                        janela.setMessage("Deseja ver no mapa a nova busca?\nRecomenda-se que seja preciso para encontrar o local desejado!");
                        janela.setPositiveButton("Ver no mapa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                removeTeclado(view);

                                LatLng minhaBusca = latLngFromAddress(getApplicationContext(), busca_endereco);

                                if (minhaBusca != null) {

                                    mMap.addMarker(new MarkerOptions().position(minhaBusca).title("Sua Busca"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhaBusca, 16));

                                } else
                                    Toast.makeText(MapsActivity2.this, "Nenhum resultado encontrado para sua pesquisa.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        janela.setNegativeButton("Não", null);
                        janela.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MapsActivity2.this, "Por favor coloque uma entrada válida!", Toast.LENGTH_SHORT).show();
                    Log.d("Failure", "Error" + e.getMessage());
                }
            }
        });
    }

    private void removeTeclado(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public LatLng latLngFromAddress(Context appContext, String strAddress) {
        LatLng latLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List<Address> geoResults = null;
        try {
            geoResults = geocoder.getFromLocationName(strAddress, 1);
            i = 0;
            while (geoResults.size() == 0 && i < 1) {
                geoResults = geocoder.getFromLocationName(strAddress, 1);
                Log.d("Failure", "loops:" + i);
                i++;
            }

            if (geoResults.size() > 0) {
                Address addr = geoResults.get(0);
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return latLng;
    }

    //------------------------------Permissions--------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("TAG", "onRequestPermissionsResult: " + requestCode);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                pedirPermissao();
            }
        }
    }

    private void pedirPermissao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void alertaValidacao() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(getApplicationContext(), R.style.fundo_dialogo_branco);
        alerta.setTitle("Permissões negadas!");
        alerta.setMessage("Para utilizar alguns recursos do aplicativo é necessário aceitar as permissões!");
        alerta.setCancelable(false);
        alerta.setNegativeButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alerta.create().show();
    }
}