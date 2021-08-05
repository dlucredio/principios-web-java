package br.ufscar.dc.latosensu.web.bolao1.beans;

public class Palpite {
    private int id;
    private String campeao;
    private String vice;
    private Usuario palpiteiro;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public Usuario getPalpiteiro() {
        return palpiteiro;
    }
    public void setPalpiteiro(Usuario palpiteiro) {
        this.palpiteiro = palpiteiro;
    }
}
