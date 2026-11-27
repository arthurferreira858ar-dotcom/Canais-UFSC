package br.com.canaisufsc;

import java.util.Arrays;
import java.util.List;

/** Lista de links, na ordem em que aparecem. Para mudar um canal, edite somente este arquivo. */
final class Canais {
    private Canais() {}

    static final List<Canal> TODOS = Arrays.asList(
            new Canal("CAGR", "Matrícula, histórico e notas",
                    "https://cagr.sistemas.ufsc.br/"),
            new Canal("Moodle", "Aulas, materiais e atividades",
                    "https://moodle.ufsc.br/"),
            new Canal("RU", "Restaurante Universitário e cardápio",
                    "https://ru.ufsc.br/"),
            new Canal("Biblioteca", "Acervo, renovação e serviços",
                    "https://portal.bu.ufsc.br/"),
            new Canal("Notícias", "Comunicados e reportagens da UFSC",
                    "https://noticias.ufsc.br/")
    );
}
