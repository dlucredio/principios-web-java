package br.ufscar.dc.latosensu.web.bolaodacopa2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("bolao")
public class Bolaodacopa2Config {
    private List<String> selecoes = new ArrayList<>();

    public List<String> getSelecoes() {
        return selecoes;
    }

    public void setSelecoes(List<String> selecoes) {
        this.selecoes = selecoes;
    }
}
