package br.ufscar.dc.latosensu.web.bolaodacopa2.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators.EmailNaoCadastrado;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators.FormatoData;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators.SenhaConfirmada;

@SenhaConfirmada
public class NovoUsuario {

    @NotNull
    @Size(min = 5, max = 64, message = "Nome deve possuir entre 5 e 64 caracteres")
    private String nome;

    @NotNull
    @Size(min = 5, max = 64, message = "Email deve possuir entre 5 e 64 caracteres")
    @Email(message = "Email está em formato incorreto")
    @EmailNaoCadastrado
    private String email;

    @NotNull
    @Size(min = 8, max = 64, message = "A senha deve possuir entre 8 e 64 caracteres")
    private String senha;

    private String confirmarSenha;

    @Pattern(regexp = "\\(\\d{2}\\) (\\d{5}|\\d{4})-\\d{4}", message = "O telefone deve estar no formato (xx) xxxxx-xxxx")
    private String telefone;

    @FormatoData(message = "A data deve estar no formato dd/mm/aaaa")
    private String dataDeNascimento;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
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

}
