package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma lista de reprodução de vídeos pertencente a um Usuário.
 */
public class ListaReproducao {

    private int      id;
    private String   nome;
    private Usuario  dono;
    private List<Video> videos;

    public ListaReproducao(int id, String nome, Usuario dono) {
        this.id     = id;
        this.nome   = nome;
        this.dono   = dono;
        this.videos = new ArrayList<>();
    }

    public int         getId()     { return id; }
    public String      getNome()   { return nome; }
    public Usuario     getDono()   { return dono; }
    public List<Video> getVideos() { return videos; }

    public void setNome(String nome) { this.nome = nome; }

    public void adicionarVideo(Video v) {
        if (!videos.contains(v)) videos.add(v);
    }

    public void removerVideo(Video v) { videos.remove(v); }

    @Override
    public String toString() { return nome; }
}
