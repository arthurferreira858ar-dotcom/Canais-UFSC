package br.com.canaisufsc;

import java.util.Arrays;
import java.util.List;

/** Lista de links, na ordem em que aparecem. Para mudar um canal, edite somente este arquivo. */
final class Canais {
    private Canais() {}

    static final List<Canal> TODOS = Arrays.asList(
            new Canal("CAGR", "Matrícula, histórico e notas",
                    "https://cagr.sistemas.ufsc.br/"),
            new Canal("Moodle", "Abre o app. No 1º acesso, use moodle.ufsc.br",
                    "https://moodle.ufsc.br/", "com.moodle.moodlemobile"),
            new Canal("RU", "Cardápio, saldo e passes (app UFSC)",
                    "https://app.ufsc.br/"),
            new Canal("Biblioteca", "Acervo, renovação e serviços",
                    "https://portal.bu.ufsc.br/"),
            new Canal("Notícias", "Comunicados e reportagens da UFSC",
                    "https://noticias.ufsc.br/")
    );
}
