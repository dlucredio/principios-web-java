package br.ufscar.dc.latosensu.web.bolaodacopa2.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Palpite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne
    private Usuario palpiteiro;

    private String campeao;
    private String vice;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Usuario getPalpiteiro() {
        return palpiteiro;
    }
    public void setPalpiteiro(Usuario palpiteiro) {
        this.palpiteiro = palpiteiro;
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
