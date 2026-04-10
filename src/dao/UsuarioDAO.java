package dao;

import model.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Acesso ao banco de dados para a entidade Usuario.
 */
public class UsuarioDAO {

    /** Cadastra um novo usuário. Lança SQLException se e-mail já existir. */
    public void cadastrar(String nome, String email, String senha) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, hash(senha));
            ps.executeUpdate();
        }
    }

    /**
     * Autentica o usuário. Retorna o objeto Usuario em caso de sucesso,
     * ou null se as credenciais forem inválidas.
     */
    public Usuario login(String email, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hash(senha));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
            return null;
        }
    }

    // ── Utilitário ────────────────────────────────────────────────────────────

    private String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 não disponível", e);
        }
    }
}
