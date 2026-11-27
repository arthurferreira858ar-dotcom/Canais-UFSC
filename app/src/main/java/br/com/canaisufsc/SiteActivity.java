package br.com.canaisufsc;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Mostra um site da UFSC dentro do app.
 * Nenhum código é injetado nas páginas e nada do que é digitado é lido.
 */
public class SiteActivity extends Activity {
    static final String EXTRA_NOME = "nome";
    static final String EXTRA_URL = "url";
    private static final int ESCOLHER_ARQUIVO = 1;

    private WebView web;
    private ProgressBar progresso;
    private TextView servidor;
    private ValueCallback<Uri[]> envioPendente;
    private String enderecoInicial;

    @Override
    protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_site);

        enderecoInicial = getIntent().getStringExtra(EXTRA_URL);
        if (enderecoInicial == null) {
            finish();
            return;
        }
        ((TextView) findViewById(R.id.titulo)).setText(getIntent().getStringExtra(EXTRA_NOME));
        servidor = findViewById(R.id.servidor);
        progresso = findViewById(R.id.progresso);
        web = findViewById(R.id.web);
        findViewById(R.id.inicio).setOnClickListener(v -> finish());
        findViewById(R.id.navegador).setOnClickListener(v -> {
            String atual = web.getUrl();
            abrirFora(Uri.parse(atual != null ? atual : enderecoInicial));
        });

        configurar();
        if (estado == null || web.restoreState(estado) == null) {
            web.loadUrl(enderecoInicial);
        }
    }

    private void configurar() {
        WebSettings ajustes = web.getSettings();
        ajustes.setJavaScriptEnabled(true);
        ajustes.setDomStorageEnabled(true);
        ajustes.setAllowFileAccess(false);
        ajustes.setBuiltInZoomControls(true);
        ajustes.setDisplayZoomControls(false);
        ajustes.setUseWideViewPort(true);
        ajustes.setLoadWithOverviewMode(true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest pedido) {
                if (!pedido.isForMainFrame()) return false;
                Uri destino = pedido.getUrl();
                if (Enderecos.daUfsc(destino.getScheme(), destino.getHost())) return false;
                // Fora da UFSC: sai do app, para o usuário ver onde está.
                abrirFora(destino);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap icone) {
                servidor.setText(Uri.parse(url).getHost());
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest pedido, WebResourceError erro) {
                if (pedido.isForMainFrame()) avisar(R.string.erro_carregar);
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int valor) {
                progresso.setProgress(valor);
                progresso.setVisibility(valor < 100 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> resposta,
                                             FileChooserParams opcoes) {
                if (envioPendente != null) envioPendente.onReceiveValue(null);
                envioPendente = resposta;
                Intent escolha = opcoes.createIntent();
                if (opcoes.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE) {
                    escolha.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                try {
                    startActivityForResult(escolha, ESCOLHER_ARQUIVO);
                } catch (ActivityNotFoundException e) {
                    envioPendente = null;
                    avisar(R.string.sem_arquivos);
                    return false;
                }
                return true;
            }
        });

        web.setDownloadListener((url, agente, disposicao, tipo, tamanho) ->
                baixar(url, agente, disposicao, tipo));
    }

    /** Baixa com o gerenciador do Android, mantendo o login apenas em endereços da UFSC. */
    private void baixar(String url, String agente, String disposicao, String tipo) {
        Uri origem = Uri.parse(url);
        if (!Enderecos.daUfsc(origem.getScheme(), origem.getHost())) {
            if ("https".equals(origem.getScheme())) abrirFora(origem);
            else avisar(R.string.erro_baixar);
            return;
        }
        try {
            String nome = URLUtil.guessFileName(url, disposicao, tipo);
            DownloadManager.Request pedido = new DownloadManager.Request(origem);
            String cookies = CookieManager.getInstance().getCookie(url);
            if (cookies != null) pedido.addRequestHeader("Cookie", cookies);
            pedido.addRequestHeader("User-Agent", agente);
            pedido.setMimeType(tipo);
            pedido.setTitle(nome);
            pedido.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                pedido.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nome);
            } else {
                pedido.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, nome);
            }
            getSystemService(DownloadManager.class).enqueue(pedido);
            avisar(R.string.baixando);
        } catch (RuntimeException e) {
            avisar(R.string.erro_baixar);
        }
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, Intent dados) {
        super.onActivityResult(codigo, resultado, dados);
        if (codigo != ESCOLHER_ARQUIVO || envioPendente == null) return;
        Uri[] arquivos = null;
        if (resultado == RESULT_OK && dados != null) {
            ClipData varios = dados.getClipData();
            if (varios != null) {
                arquivos = new Uri[varios.getItemCount()];
                for (int i = 0; i < arquivos.length; i++) arquivos[i] = varios.getItemAt(i).getUri();
            } else {
                arquivos = WebChromeClient.FileChooserParams.parseResult(resultado, dados);
            }
        }
        envioPendente.onReceiveValue(arquivos);
        envioPendente = null;
    }

    private void abrirFora(Uri destino) {
        String esquema = destino.getScheme();
        if (!("https".equals(esquema) || "http".equals(esquema)
                || "mailto".equals(esquema) || "tel".equals(esquema))) {
            avisar(R.string.sem_navegador);
            return;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, destino));
        } catch (ActivityNotFoundException e) {
            avisar(R.string.sem_navegador);
        }
    }

    private void avisar(int mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

    /** O botão voltar do celular retorna à página anterior antes de fechar o site. */
    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        if (web != null) web.saveState(estado);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieManager.getInstance().flush();
    }

    @Override
    protected void onDestroy() {
        if (web != null) web.destroy();
        super.onDestroy();
    }
}
