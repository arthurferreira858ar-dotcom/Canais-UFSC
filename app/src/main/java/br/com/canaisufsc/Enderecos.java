package br.com.canaisufsc;

/** Decide quais endereços podem abrir dentro do app. */
final class Enderecos {
    private Enderecos() {}

    /** Somente páginas com conexão segura (https) em ufsc.br e seus subdomínios. */
    static boolean daUfsc(String esquema, String servidor) {
        if (!"https".equalsIgnoreCase(esquema) || servidor == null) return false;
        String s = servidor.toLowerCase(java.util.Locale.ROOT);
        return s.equals("ufsc.br") || s.endsWith(".ufsc.br");
    }
}
