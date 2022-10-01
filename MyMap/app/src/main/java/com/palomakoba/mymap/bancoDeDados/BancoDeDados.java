package com.palomakoba.mymap.bancoDeDados;

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

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MYMAPSDB";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE_CEP =
            "CREATE TABLE ceptable ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "cep TEXT, logradouro TEXT, complemento TEXT, "
                    + "bairro TEXT, localidade TEXT, uf TEXT);";

    public BancoDeDados(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CEP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ceptable");
        onCreate(sqLiteDatabase);
    }

    public long inserir(ContentValues cv) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert("ceptable", null, cv);
        return id;
    }

    public List<ContentValues> pesquiarPorTodos() {
        String sql = "SELECT * FROM ceptable ORDER BY id";
        String where[] = null;
        return pesquisar(sql, where);
    }

    @SuppressLint("Range")
    private List<ContentValues> pesquisar(String sql, String where[]) {
        List<ContentValues> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, where);

        if (c.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                cv.put("id", c.getInt(c.getColumnIndex("id")));

                cv.put("cep", c.getString(c.getColumnIndex("cep")));

                cv.put("logradouro", c.getString(c.getColumnIndex("logradouro")));

                cv.put("complemento", c.getString(c.getColumnIndex("complemento")));

                cv.put("bairro", c.getString(c.getColumnIndex("bairro")));

                cv.put("localidade", c.getString(c.getColumnIndex("localidade")));

                cv.put("uf", c.getString(c.getColumnIndex("uf")));

                lista.add(cv);
            } while (c.moveToNext());
        }
        return lista;
    }

    public void deletarRegistro(int id) {
        String where = "id=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("ceptable", where, null);
        db.close();
    }

    public void deleteAll() {
        String where = "id>" + 0;
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("ceptable", where, null);
        db.close();
    }

}
