package br.ufscar.dc.latosensu.web.bolao1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;

public class PalpiteDAO {

    private final static String CRIAR_PALPITE_SQL = "insert into Palpite"
            + " (campeao, vice, palpiteiro)"
            + " values (?,?,?)";

    private final static String LISTAR_PALPITES_SQL = "select"
            + " p.id as palpiteId, p.campeao, p.vice,"
            + " u.id as usuarioId, u.nome, u.email, u.telefone, u.dataDeNascimento"
            + " from Palpite p inner join Usuario u on p.palpiteiro = u.id";

    private final static String LISTAR_PALPITES_POR_SELECAO_SQL = "select"
            + " p.id as palpiteId, p.campeao, p.vice,"
            + " u.id as usuarioId, u.nome, u.email, u.telefone, u.dataDeNascimento"
            + " from Palpite p inner join Usuario u on p.palpiteiro = u.id"
            + " where campeao = ? or vice = ?";

    DataSource dataSource;

    public PalpiteDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Palpite gravarPalpite(Palpite p) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(CRIAR_PALPITE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, p.getCampeao());
            ps.setString(2, p.getVice());
            ps.setInt(3, p.getPalpiteiro().getId());
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    public List<Palpite> listarTodosPalpites() throws SQLException, NamingException {
        List<Palpite> ret = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(LISTAR_PALPITES_SQL)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Palpite p = new Palpite();
                    Usuario u = new Usuario();
                    p.setId(rs.getInt("palpiteId"));
                    p.setCampeao(rs.getString("campeao"));
                    p.setVice(rs.getString("vice"));
                    u.setId(rs.getInt("usuarioId"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefone(rs.getString("telefone"));
                    u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                    p.setPalpiteiro(u);
                    ret.add(p);
                }
            }
        }
        return ret;
    }

    public List<Palpite> listarTodosPalpitesPorSelecao(String selecao) throws SQLException {
        List<Palpite> ret = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(LISTAR_PALPITES_POR_SELECAO_SQL)) {

            ps.setString(1, selecao);
            ps.setString(2, selecao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Palpite p = new Palpite();
                    Usuario u = new Usuario();
                    p.setId(rs.getInt("palpiteId"));
                    p.setCampeao(rs.getString("campeao"));
                    p.setVice(rs.getString("vice"));
                    u.setId(rs.getInt("usuarioId"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefone(rs.getString("telefone"));
                    u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                    p.setPalpiteiro(u);
                    ret.add(p);                }
            }
        }
        return ret;
    }
}
