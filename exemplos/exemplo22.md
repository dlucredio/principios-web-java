# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 22 - Configurando beans

Este exemplo ilustra como configurar beans para a aplicação

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "20.0.1"
- Apache Maven 3.8.5

<hr>

1. Abrir o projeto da demonstração anterior
2. Modificar a classe `Mensagem` para não utilizar mais a anotação de componente

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import java.text.SimpleDateFormat;
import java.util.Date;

-import org.springframework.stereotype.Component;

-@Component
public class Mensagem {
    String texto;

    public Mensagem() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        texto = "Olá! Agora são " + sdf.format(new Date());
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
```
3. Criar a classe `br.ufscar.dc.latosensu.web.demospringweb.DemoDiConfiguration`

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDiConfiguration {
    // Importante: se essa classe estiver no mesmo pacote
    // ou subpacote da classe principal da aplicação, não
    // é necessário configurar nada.
    // Caso contrário, é necessário usar a anotação
    // @ComponentScan (pesquise sobre o assunto caso
    // queira entender melhor)

    @Bean
    public Map<String, Mensagem> mensagensPadrao() {
        Map<String, Mensagem> map = new HashMap<>();
        map.put("erro", getMensagemErro());
        map.put("sucesso", getMensagemSucesso());
        return map;
    }

    @Bean(name = "mensagemErro")
    public Mensagem getMensagemErro() {
        Mensagem m = new Mensagem();
        m.setTexto("Aconteceu um erro");
        return m;
    }

    @Bean(name = "mensagemSucesso")
    public Mensagem getMensagemSucesso() {
        Mensagem m = new Mensagem();
        m.setTexto("Operação bem-sucedida");
        return m;
    }
}

```

4. Injetar as dependências no controlador

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoDiController {

    @Resource            
    private Map<String, Mensagem> mensagensPadrao;
    // @Autowired não é recomendado para tipos que não são "nossos"
    // como no caso Map, que é um tipo Java
    // @Resource vai buscar pelo nome primeiro

    @Autowired
    Mensagem mensagemErro;

    @Autowired
    Mensagem mensagemSucesso;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("mensagem", mensagensPadrao.get("sucesso"));
        //model.addAttribute("mensagem", mensagemErro)
        //model.addAttribute("mensagem", mensagemSucesso)
        return "index";
    }
}
```
5. Executar e ver o efeito
6. Vamos ver que a injeção pode ser feita em qualquer componente. Criar um novo componente Spring (classe `br.ufscar.dc.latosensu.web.demospringweb.Resposta`)

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class Resposta {
    @Resource
    private Map<String, Mensagem> mensagensPadrao;

    public String gerarResposta(String tipo) {
        return "A resposta é: "+mensagensPadrao.get(tipo).getTexto();
    }
}
```
7. Modificar o controlador, para usar o novo componente, ao invés das mensagens diretamen te

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoDiController {
    @Autowired
    Resposta resposta;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("mensagem", resposta.gerarResposta("erro"));
        return "index";
    }
}
```

8. Como agora a resposta já é uma string, modificar `index.html`:

```diff
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Demonstração DI</title>
</head>
<body>
-    <h1>Valor da mensagem: <span th:text="${mensagem.texto}">Mensagem</span></h1>
+    <h1>Valor da mensagem: <span th:text="${mensagem}">Mensagem</span></h1>
</body>
</html>
```

5. Fim
