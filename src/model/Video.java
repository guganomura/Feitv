package model;

/**
 * Classe abstrata que representa um vídeo (Filme ou Série).
 */
public abstract class Video {

    protected int    id;
    protected String titulo;
    protected String genero;
    protected int    anoLancamento;
    protected int    curtidas;

    public Video(int id, String titulo, String genero, int anoLancamento, int curtidas) {
        this.id            = id;
        this.titulo        = titulo;
        this.genero        = genero;
        this.anoLancamento = anoLancamento;
        this.curtidas      = curtidas;
    }

    public int    getId()            { return id; }
    public String getTitulo()        { return titulo; }
    public String getGenero()        { return genero; }
    public int    getAnoLancamento() { return anoLancamento; }
    public int    getCurtidas()      { return curtidas; }
    public void   setCurtidas(int c) { this.curtidas = c; }

    public void curtir()    { curtidas++; }
    public void descurtir() { if (curtidas > 0) curtidas--; }

    public abstract Situacao getSituacao();
    public abstract String   getTipo();

    @Override
    public String toString() { return titulo; }
}
