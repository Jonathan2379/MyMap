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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.palomakoba.mymap.R;
import com.palomakoba.mymap.adapter.AdapterRecycler;
import com.palomakoba.mymap.bancoDeDados.BancoDeDados;

import java.util.ArrayList;
import java.util.List;

public class ListaEndereco extends AppCompatActivity {//implements SensorEventListener {

    private RecyclerView recyclerView;
    private ImageView duvida, home, limpa_lista;
    BancoDeDados bd;

    private List<ContentValues> listEndereco = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_endereco);

        bd = new BancoDeDados(getApplicationContext());

        buscaID();

        listEndereco = bd.pesquiarPorTodos();

        AdapterRecycler adapter = new AdapterRecycler(listEndereco);

        ligação(adapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        duvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder janela = new AlertDialog.Builder(ListaEndereco.this, R.style.fundo_dialogo);
                janela.setTitle("Ajuda");
                janela.setMessage("Deseja ver mais informações sobre os CEPs?");
                janela.setCancelable(false);
                janela.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://definicao.net/cep/"));
                        startActivity(intent);
                    }
                });
                janela.setNegativeButton("Não", null);
                janela.show();
            }
        });

        limpa_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listEndereco.size() == 0)
                    Toast.makeText(ListaEndereco.this, "A lista já está vazia", Toast.LENGTH_SHORT).show();
                else {

                    AlertDialog.Builder janela = new AlertDialog.Builder(ListaEndereco.this, R.style.fundo_dialogo);
                    janela.setTitle("Atenção!");
                    janela.setMessage("Você realmente deseja apagar a lista de endereços?");
                    janela.setCancelable(false);
                    janela.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            bd.deleteAll();
                            listEndereco.clear();

                            ligação(adapter);
                            AdapterRecycler a = new AdapterRecycler(listEndereco);
                            a.notifyItemRemoved(0);
                        }
                    });
                    janela.setNegativeButton("Não", null);
                    janela.show();
                }
            }
        });
    }

    private void buscaID() {
        duvida = findViewById(R.id.duvida);
        home = findViewById(R.id.back_to_home);
        limpa_lista = findViewById(R.id.limpa_lista);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void ligação(AdapterRecycler adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}