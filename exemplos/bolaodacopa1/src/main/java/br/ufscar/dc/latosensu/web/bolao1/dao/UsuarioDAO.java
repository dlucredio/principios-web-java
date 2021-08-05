package br.ufscar.dc.latosensu.web.bolao1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;

public class UsuarioDAO {

    private final static String CRIAR_USUARIO_SQL = "insert into Usuario"
            + " (nome, email, telefone, dataDeNascimento)"
            + " values (?,?,?,?)";

    private final static String BUSCAR_USUARIO_SQL = "select"
            + " id, nome, email, telefone, dataDeNascimento"
            + " from usuario"
            + " where id=?";
    
    DataSource dataSource;

    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Usuario gravarUsuario(Usuario u) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(CRIAR_USUARIO_SQL, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setDate(4, new java.sql.Date(u.getDataDeNascimento().getTime()));
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                u.setId(rs.getInt(1));
            }
        }
        return u;
    }

    public Usuario buscarUsuario(int id) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(BUSCAR_USUARIO_SQL)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                return u;
            }
        }
    }
}
