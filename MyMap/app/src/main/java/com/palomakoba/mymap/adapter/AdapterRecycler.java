package com.palomakoba.mymap.adapter;

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

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palomakoba.mymap.R;
import com.palomakoba.mymap.bancoDeDados.BancoDeDados;
import com.palomakoba.mymap.layout.MapsActivity2;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.MyViewHolder> {

    private List<ContentValues> listEndereco = new ArrayList<>();

    private FragmentManager fragmentManager = null;

    private Context context = null;

    public AdapterRecycler(List<ContentValues> listEndereco) {
        this.listEndereco = listEndereco;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_endereco, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContentValues cv = new ContentValues();
        cv = listEndereco.get(position);

        holder.cep.setText(cv.getAsString("cep"));
        holder.logradouro.setText(cv.getAsString("logradouro"));
        holder.complemento.setText(cv.getAsString("complemento"));
        holder.bairro.setText(cv.getAsString("bairro"));
        holder.uf.setText(cv.getAsString("uf"));
        holder.localidade.setText(cv.getAsString("localidade"));
        holder.text_id.setText(cv.getAsString("id"));
        holder.text_id.setAlpha(0); // Esconde o ID
    }

    @Override
    public int getItemCount() {
        return listEndereco.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView cep, logradouro, complemento, bairro, localidade, uf, text_id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            buscaID(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.fundo_dialogo);
                    builder.setTitle("Confirme a exclusão");
                    builder.setMessage("Você realmente deseja excluir este local?");
                    builder.setCancelable(false);
                    builder.setNegativeButton("Não", null);
                    builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BancoDeDados db = new BancoDeDados(itemView.getContext());

                            int aux_lista = Integer.parseInt(text_id.getText().toString());
                            db.deletarRegistro(aux_lista);

                            Toast.makeText(itemView.getContext(), "Localização removida com sucesso!", Toast.LENGTH_SHORT).show();

                            listEndereco.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

                        }
                    });
                    builder.show();
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), MapsActivity2.class);
                    String endereco = logradouro.getText().toString() + ", " + bairro.getText().toString()
                            + " - " + localidade.getText().toString() + "/" + uf.getText().toString();

                    intent.putExtra("endereco", endereco);
                    intent.putExtra("tipo", "2");

                    Toast.makeText(itemView.getContext(), "Aguarde enquanto mostra a localização do CEP!", Toast.LENGTH_SHORT).show();

                    itemView.getContext().startActivity(intent);

                }
            });
        }

        private void buscaID(View itemView) {
            cep = itemView.findViewById(R.id.cep);
            localidade = itemView.findViewById(R.id.localidade);
            logradouro = itemView.findViewById(R.id.logradouro);
            complemento = itemView.findViewById(R.id.complemento);
            bairro = itemView.findViewById(R.id.bairro);
            uf = itemView.findViewById(R.id.uf);
            text_id = itemView.findViewById(R.id.txt_id);
        }
    }
}
