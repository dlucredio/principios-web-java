# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 20 - Injeção de dependência no Spring

Este exemplo introduz a injeção de dependência no Spring MVC

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "20.0.1"
- Apache Maven 3.8.5

<hr>

1. Criar um novo projeto Spring
	
	- **Project:** Maven Project
	
	- **Language:** Java
	
	- **Spring Boot:** 3.1.0
	
	- **Group:** br.ufscar.dc.latosensu.web
	
	- **Artifact:** demo-springweb
	
	- **Name:** Demo SpringWeb
	
	- **Description:** Demo SpringWeb
	
	- **Package name:** br.ufscar.dc.latosensu.web.demospringweb
	
	- **Packaging:** Jar
	
	- **Java:** 20
	
	  **Dependências:** Spring Web, Thymeleaf e Spring Boot DevTools

2. Criar uma classe `br.ufscar.dc.latosensu.web.demospringweb.Mensagem`

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensagem {
    String texto;

    public Mensagem() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        texto = "Olá! Agora são "+sdf.format(new Date());
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }    
}
```

3. Criar o controlador `br.ufscar.dc.latosensu.web.demospringweb.DemoDiController`
    
```java
package br.ufscar.dc.latosensu.web.demospringweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoDiController {
    Mensagem m = new Mensagem();
    
    @GetMapping
    public String index(Model model) {

        model.addAttribute("mensagem", m);
        return "index";
    }
}
```

4. Criar a visão `index.html` (no diretório `/src/main/resources/templates/`)

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Demonstração DI</title>
</head>
<body>
    <h1>Valor da mensagem: <span th:text="${mensagem.texto}">Mensagem</span></h1>
</body>
</html>
```

5. Executar e ver o efeito (a mensagem é inicializada uma única vez e reutilizada em cada request)

6. Alterar o controlador para que a mensagem seja inicializada dentro do request:

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoDiController {
-    Mensagem m = new Mensagem();
    @GetMapping
    public String index(Model model) {
+        Mensagem m = new Mensagem();
        model.addAttribute("mensagem", m);
        return "index";
    }
}
```

7. Executar e ver que agora cada novo request cria uma nova mensagem

8. Vamos agora usar injeção de dependência

9. Modificar o controlador para injetar a mensagem automaticamente:

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoDiController {
+    @Autowired
+    Mensagem m;

    @GetMapping
    public String index(Model model) {
-        Mensagem m = new Mensagem();
        model.addAttribute("mensagem", m);
        return "index";
    }
}
```
10. Modificar a classe `Mensagem` para que ela possa ser gerenciada pelo Spring

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import java.text.SimpleDateFormat;
import java.util.Date;

+import org.springframework.stereotype.Component;

+@Component
public class Mensagem {
    String texto;

    public Mensagem() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        texto = "Olá! Agora são "+sdf.format(new Date());
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
```

11. Testar e ver que a mensagem volta a ser um Singleton (a mesma para várias sessões)
12. Adicionar uma anotação para que a classe tenha o escopo de sessão

```diff
package br.ufscar.dc.latosensu.web.demospringweb;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
+import org.springframework.web.context.annotation.SessionScope;

@Component
+@SessionScope
public class Mensagem {
    String texto;

    public Mensagem() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        texto = "Olá! Agora são "+sdf.format(new Date());
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
```

13. Testar em diferentes navegadores e observar que o componente tem um escopo de sessão

14. Fim
