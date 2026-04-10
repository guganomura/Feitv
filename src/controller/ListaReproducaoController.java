package controller;

import dao.ListaReproducaoDAO;
import model.ListaReproducao;
import model.Usuario;
import model.Video;

import java.sql.SQLException;
import java.util.List;

/**
 * Controla operações sobre ListaReproducao: CRUD e gerenciamento de vídeos.
 */
public class ListaReproducaoController {

    private final ListaReproducaoDAO dao = new ListaReproducaoDAO();

    /** Retorna as listas do usuário. */
    public List<ListaReproducao> listar(Usuario usuario) {
        try {
            return dao.listarPorUsuario(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar listas: " + e.getMessage(), e);
        }
    }

    /** Cria uma nova lista com o nome informado. */
    public ListaReproducao criar(String nome, Usuario usuario) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da lista não pode ser vazio.");
        }
        try {
            return dao.criar(nome.trim(), usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar lista: " + e.getMessage(), e);
        }
    }

    /** Renomeia uma lista existente. */
    public void editar(ListaReproducao lista, String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O nome da lista não pode ser vazio.");
        }
        try {
            dao.editar(lista.getId(), novoNome.trim());
            lista.setNome(novoNome.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar lista: " + e.getMessage(), e);
        }
    }

    /** Exclui a lista. */
    public void excluir(ListaReproducao lista) {
        try {
            dao.excluir(lista.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir lista: " + e.getMessage(), e);
        }
    }

    /** Adiciona um vídeo à lista (persistindo no BD). */
    public void adicionarVideo(ListaReproducao lista, Video video) {
        try {
            dao.adicionarVideo(lista.getId(), video.getId());
            lista.adicionarVideo(video);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar vídeo: " + e.getMessage(), e);
        }
    }

    /** Remove um vídeo da lista (persistindo no BD). */
    public void removerVideo(ListaReproducao lista, Video video) {
        try {
            dao.removerVideo(lista.getId(), video.getId());
            lista.removerVideo(video);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover vídeo: " + e.getMessage(), e);
        }
    }

    /** Carrega os vídeos de uma lista a partir do BD. */
    public void carregarVideos(ListaReproducao lista) {
        try {
            dao.carregarVideos(lista);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar vídeos: " + e.getMessage(), e);
        }
    }

    /** Conta vídeos de uma lista. */
    public int contarVideos(int idLista) {
        try {
            return dao.contarVideos(idLista);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar vídeos: " + e.getMessage(), e);
        }
    }
}
