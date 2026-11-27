package br.com.canaisufsc;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_main);

        LinearLayout lista = findViewById(R.id.lista);
        for (Canal canal : Canais.TODOS) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_canal, lista, false);
            ((TextView) view.findViewById(R.id.nome)).setText(canal.nome);
            ((TextView) view.findViewById(R.id.descricao)).setText(canal.descricao);
            view.setOnClickListener(v -> abrir(canal));
            lista.addView(view);
        }
    }

    /** Abre o app de celular do canal, quando houver; caso contrário, o site. */
    private void abrir(Canal canal) {
        if (canal.pacote == null) {
            abrirLink(canal.url);
            return;
        }
        Intent app = getPackageManager().getLaunchIntentForPackage(canal.pacote);
        if (app != null) {
            startActivity(app);
        } else {
            // App não instalado: mostra a página dele na loja.
            abrirLink("https://play.google.com/store/apps/details?id=" + canal.pacote);
        }
    }

    /** Abre no navegador ou na loja. Este app nunca vê sua senha. */
    private void abrirLink(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.sem_navegador, Toast.LENGTH_LONG).show();
        }
    }
}
