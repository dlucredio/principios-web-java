package br.ufscar.dc.latosensu.web.bolaodacopa2.forms;

import javax.validation.constraints.NotBlank;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators.CampeaoEViceDiferentes;

@CampeaoEViceDiferentes(message = "Campeão e vice devem ser diferentes")
public class NovoPalpite {
    
    @NotBlank(message = "Você precisa escolher um campeão")
    private String campeao;

    @NotBlank(message = "Você precisa escolher um vice")
    private String vice;

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
