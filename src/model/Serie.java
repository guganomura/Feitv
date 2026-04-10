package model;

/**
 * Representa uma Série, subclasse de Video que implementa Situacao.
 * A própria série é sua situação (retorna this em getSituacao()).
 */
public class Serie extends Video implements Situacao {

    private int    totalTemporadas;
    private int    totalEpisodios;
    private String status; // "Em Andamento", "Finalizada", "Cancelada"

    public Serie(int id, String titulo, String genero, int anoLancamento,
                 int curtidas, int totalTemporadas, int totalEpisodios, String status) {
        super(id, titulo, genero, anoLancamento, curtidas);
        this.totalTemporadas = totalTemporadas;
        this.totalEpisodios  = totalEpisodios;
        this.status          = status;
    }

    public int    getTotalTemporadas() { return totalTemporadas; }
    public int    getTotalEpisodios()  { return totalEpisodios; }
    public String getStatus()          { return status; }

    /** A própria série implementa Situacao; retorna this. */
    @Override
    public Situacao getSituacao() { return this; }

    @Override
    public String getNome() { return status; }

    @Override
    public String getDescricao() {
        return totalTemporadas + " temporada(s) · " + totalEpisodios + " episódio(s)";
    }

    @Override
    public String getTipo() { return "SERIE"; }
}
