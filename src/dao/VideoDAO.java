package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso ao banco de dados para vídeos (Filmes e Séries).
 */
public class VideoDAO {

    /**
     * Busca vídeos cujo título contenha o termo informado (case-insensitive).
     * Retorna lista com curtidas totais calculadas.
     */
    public List<Video> buscarPorNome(String termo) throws SQLException {
        String sql =
            "SELECT v.id, v.titulo, v.genero, v.ano_lancamento, v.tipo," +
            "       COUNT(c.id_usuario) AS num_curtidas," +
            "       f.duracao, f.diretor," +
            "       s.total_temporadas, s.total_episodios, s.status" +
            " FROM videos v" +
            " LEFT JOIN curtidas c ON v.id = c.id_video" +
            " LEFT JOIN filmes   f ON v.id = f.id" +
            " LEFT JOIN series   s ON v.id = s.id" +
            " WHERE LOWER(v.titulo) LIKE LOWER(?)" +
            " GROUP BY v.id, f.duracao, f.diretor, s.total_temporadas, s.total_episodios, s.status" +
            " ORDER BY v.titulo";

        List<Video> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /** Lista todos os vídeos do banco (usado para adicionar a listas). */
    public List<Video> listarTodos() throws SQLException {
        return buscarPorNome("");
    }

    /** Busca um vídeo pelo id, com curtidas calculadas. */
    public Video buscarPorId(int id) throws SQLException {
        String sql =
            "SELECT v.id, v.titulo, v.genero, v.ano_lancamento, v.tipo," +
            "       COUNT(c.id_usuario) AS num_curtidas," +
            "       f.duracao, f.diretor," +
            "       s.total_temporadas, s.total_episodios, s.status" +
            " FROM videos v" +
            " LEFT JOIN curtidas c ON v.id = c.id_video" +
            " LEFT JOIN filmes   f ON v.id = f.id" +
            " LEFT JOIN series   s ON v.id = s.id" +
            " WHERE v.id = ?" +
            " GROUP BY v.id, f.duracao, f.diretor, s.total_temporadas, s.total_episodios, s.status";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapear(rs) : null;
        }
    }

    // ── Curtidas ──────────────────────────────────────────────────────────────

    /** Verifica se o usuário já curtiu o vídeo. */
    public boolean usuarioCurtiu(int idUsuario, int idVideo) throws SQLException {
        String sql = "SELECT 1 FROM curtidas WHERE id_usuario = ? AND id_video = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            return ps.executeQuery().next();
        }
    }

    /** Registra uma curtida (ignora se já existir). */
    public void curtir(int idUsuario, int idVideo) throws SQLException {
        String sql = "INSERT INTO curtidas (id_usuario, id_video) VALUES (?,?) ON CONFLICT DO NOTHING";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            ps.executeUpdate();
        }
    }

    /** Remove uma curtida. */
    public void descurtir(int idUsuario, int idVideo) throws SQLException {
        String sql = "DELETE FROM curtidas WHERE id_usuario = ? AND id_video = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideo);
            ps.executeUpdate();
        }
    }

    /** Conta as curtidas de um vídeo. */
    public int contarCurtidas(int idVideo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM curtidas WHERE id_video = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideo);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── Mapeamento ResultSet → Video ──────────────────────────────────────────

    private Video mapear(ResultSet rs) throws SQLException {
        int    id       = rs.getInt("id");
        String titulo   = rs.getString("titulo");
        String genero   = rs.getString("genero");
        int    ano      = rs.getInt("ano_lancamento");
        int    curtidas = rs.getInt("num_curtidas");
        String tipo     = rs.getString("tipo");

        if ("FILME".equals(tipo)) {
            return new Filme(id, titulo, genero, ano, curtidas,
                             rs.getInt("duracao"),
                             rs.getString("diretor"));
        } else {
            return new Serie(id, titulo, genero, ano, curtidas,
                             rs.getInt("total_temporadas"),
                             rs.getInt("total_episodios"),
                             rs.getString("status"));
        }
    }
}
