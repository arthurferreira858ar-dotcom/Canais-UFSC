package br.com.canaisufsc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private void abrir(Canal canal) {
        Intent tela = new Intent(this, SiteActivity.class);
        tela.putExtra(SiteActivity.EXTRA_NOME, canal.nome);
        tela.putExtra(SiteActivity.EXTRA_URL, canal.url);
        startActivity(tela);
    }
}
