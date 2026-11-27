package br.com.canaisufsc;

/** Um link exibido no app. */
final class Canal {
    final String nome, descricao, url;
    /** Identificador do app de celular a abrir, ou null para abrir o site. */
    final String pacote;

    Canal(String nome, String descricao, String url) {
        this(nome, descricao, url, null);
    }

    Canal(String nome, String descricao, String url, String pacote) {
        this.nome = nome;
        this.descricao = descricao;
        this.url = url;
        this.pacote = pacote;
    }
}
