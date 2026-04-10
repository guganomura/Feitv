package controller;

import dao.VideoDAO;
import model.Video;

import java.sql.SQLException;
import java.util.List;

/**
 * Controla operações sobre Vídeos: busca, curtir e descurtir.
 */
public class VideoController {

    private final VideoDAO dao = new VideoDAO();

    /** Busca vídeos por nome. Retorna lista vazia se nenhum for encontrado. */
    public List<Video> buscar(String termo) {
        try {
            return dao.buscarPorNome(termo == null ? "" : termo.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vídeos: " + e.getMessage(), e);
        }
    }

    /** Lista todos os vídeos. */
    public List<Video> listarTodos() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vídeos: " + e.getMessage(), e);
        }
    }

    /**
     * Alterna a curtida do usuário sobre o vídeo.
     * @return true se curtiu, false se descurtiu.
     */
    public boolean alternarCurtida(int idUsuario, Video video) {
        try {
            boolean jaCurtiu = dao.usuarioCurtiu(idUsuario, video.getId());
            if (jaCurtiu) {
                dao.descurtir(idUsuario, video.getId());
                video.descurtir();
                return false;
            } else {
                dao.curtir(idUsuario, video.getId());
                video.curtir();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao curtir/descurtir: " + e.getMessage(), e);
        }
    }

    /** Verifica se o usuário já curtiu o vídeo. */
    public boolean usuarioCurtiu(int idUsuario, int idVideo) {
        try {
            return dao.usuarioCurtiu(idUsuario, idVideo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar curtida: " + e.getMessage(), e);
        }
    }

    /** Retorna a contagem atualizada de curtidas de um vídeo. */
    public int contarCurtidas(int idVideo) {
        try {
            return dao.contarCurtidas(idVideo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar curtidas: " + e.getMessage(), e);
        }
    }
}
