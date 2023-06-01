# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 21 - Anotação @AutoWired

Este exemplo ilustra alguns usos da anotação AutoWired

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "20.0.1"
- Apache Maven 3.8.5

<hr>

1. Abrir o projeto da demonstração anterior
2. Modificar o controlador para inicializar a mensagem com um texto diferente

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DemoDiController {
-    @Autowired
    Mensagem mensagem;

+    @Autowired
+    public void setMensagem(Mensagem m) {
+        this.mensagem=m;
+        this.mensagem.setTexto("Alo mundo sem horário");
+    }

    @GetMapping
    public String index() {
        return "index";
    }
}

```

3. Não vai funcionar, pois o @Autowired dispara a injeção em tempo de criação do controlador (quando não há sessões)
4. Modificar a mensagem para usar escopo default (singleton)

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
-import org.springframework.web.context.annotation.SessionScope;

@Component
-@SessionScope
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
5. Fim
