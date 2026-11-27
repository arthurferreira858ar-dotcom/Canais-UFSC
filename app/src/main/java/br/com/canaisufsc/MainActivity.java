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
            view.setOnClickListener(v -> abrir(canal.url));
            lista.addView(view);
        }
    }

    /** Abre no navegador. O app nunca vê sua senha. */
    private void abrir(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.sem_navegador, Toast.LENGTH_LONG).show();
        }
    }
}
