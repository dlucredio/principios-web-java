package br.ufscar.dc.latosensu.web.bolao1.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NovoPalpiteFormBean {
    private String nome;
    private String email;
    private String telefone;
    private String dataDeNascimento;
    private String campeao;
    private String vice;

    public List<String> validar() {
        List<String> mensagens = new ArrayList<String>();
        if (nome.trim().length() == 0) {
            mensagens.add("Nome não pode ser vazio!");
        }
        if (!email.contains("@")) {
            mensagens.add("E-mail está em formato incorreto!");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.parse(dataDeNascimento);
        } catch (ParseException pe) {
            mensagens.add("Data de nascimento deve estar no formato dd/mm/aaaa!");
        }
        if (campeao.equalsIgnoreCase(vice)) {
            mensagens.add("Campeão e vice devem ser diferentes!");
        }
        return (mensagens.isEmpty() ? null : mensagens);
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCampeao() {
        return campeao;
    }

    public void setCampeao(String campeao) {
        this.campeao = campeao;
    }

    public String getVice() {
        return vice;
    }

    public void setVice(String vice) {
        this.vice = vice;
    }

}
