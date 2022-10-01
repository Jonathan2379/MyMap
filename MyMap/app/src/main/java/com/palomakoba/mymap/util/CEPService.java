package com.palomakoba.mymap.util;

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

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Call;

public interface CEPService {

    @GET("{cep}/json/")
    Call<CEP> recuperarCEP(@Path("cep") String cep);
}
