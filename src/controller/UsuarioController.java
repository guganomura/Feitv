package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;

/**
 * Controla operações relacionadas ao Usuário: cadastro e login.
 */
public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Cadastra um novo usuário.
     * @throws IllegalArgumentException se algum campo for inválido ou e-mail duplicado.
     */
    public void cadastrar(String nome, String email, String senha, String confirmaSenha) {
        if (nome.isBlank())   throw new IllegalArgumentException("Nome não pode ser vazio.");
        if (email.isBlank())  throw new IllegalArgumentException("E-mail não pode ser vazio.");
        if (senha.isBlank())  throw new IllegalArgumentException("Senha não pode ser vazia.");
        if (senha.length() < 6) throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        if (!senha.equals(confirmaSenha)) throw new IllegalArgumentException("As senhas não coincidem.");

        try {
            dao.cadastrar(nome.trim(), email.trim().toLowerCase(), senha);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("unique")) {
                throw new IllegalArgumentException("Este e-mail já está cadastrado.");
            }
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Autentica o usuário.
     * @return objeto Usuario em caso de sucesso.
     * @throws IllegalArgumentException se as credenciais forem inválidas.
     */
    public Usuario login(String email, String senha) {
        if (email.isBlank() || senha.isBlank()) {
            throw new IllegalArgumentException("Preencha e-mail e senha.");
        }
        try {
            Usuario u = dao.login(email.trim().toLowerCase(), senha);
            if (u == null) throw new IllegalArgumentException("E-mail ou senha incorretos.");
            return u;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fazer login: " + e.getMessage(), e);
        }
    }
}
