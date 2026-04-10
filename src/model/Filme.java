package model;

/**
 * Representa um Filme, subclasse de Video.
 */
public class Filme extends Video {

    private int    duracao; // em minutos
    private String diretor;

    public Filme(int id, String titulo, String genero, int anoLancamento,
                 int curtidas, int duracao, String diretor) {
        super(id, titulo, genero, anoLancamento, curtidas);
        this.duracao = duracao;
        this.diretor = diretor;
    }

    public int    getDuracao() { return duracao; }
    public String getDiretor() { return diretor; }

    /** Filmes não possuem situação; retorna null. */
    @Override
    public Situacao getSituacao() { return null; }

    @Override
    public String getTipo() { return "FILME"; }
}
