# Canais UFSC (não oficial)

App Android com os canais da UFSC mais usados por estudantes: CAGR, Moodle,
RU, Biblioteca e Notícias.
Não tem vínculo com a universidade. Os links abrem no navegador ou no app da
rede social; o app não pede nem armazena senhas.

## Gerar o APK no Android Studio
1. Extraia o projeto e use **File > Open** na pasta `canais-ufsc`.
2. Aguarde a sincronização do Gradle (precisa de internet).
3. **Build > Build App Bundle(s) / APK(s) > Build APK(s)**.
4. O arquivo fica em `app/build/outputs/apk/debug/app-debug.apk`.

## Gerar o APK sem instalar nada (GitHub)
1. Crie um repositório e envie o **conteúdo** desta pasta para a raiz dele,
   incluindo a pasta oculta `.github`.
2. Abra a aba **Actions**, escolha **Gerar APK** e aguarde terminar.
3. Baixe o artefato `canais-ufsc-apk`, extraia e instale o APK no celular
   (o Android pedirá para permitir a instalação dessa fonte).

O APK gerado é de teste (debug). Para publicar na Play Store é preciso gerar
uma versão assinada.

## Alterar links
Edite `app/src/main/java/br/com/canaisufsc/Canais.java`.
