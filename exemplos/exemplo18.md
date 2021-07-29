# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 18 - Um primeiro projeto usando Spring MVC

([Exemplo adaptado do material do prof. Delano Medeiros Beder](https://raw.githubusercontent.com/delanobeder/DSW1/master/Modulo06/Roteiro06-01.md))

Este exemplo introduz os conceitos básicos do Spring MVC

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Maven 3.6.3

<hr>

1. Criar um novo projeto Spring (https://start.spring.io/)
	
	- **Project:** Maven Project
	
	- **Language:** Java
	
	- **Spring Boot:** 2.5.3
	
	- **Group:** br.ufscar.dc.latosensu.web
	
	- **Artifact:** alomundo-mvc
	
	- **Name:** Alo Mundo MVC
	
	- **Description:** Alo Mundo MVC
	
	- **Package name:** br.ufscar.dc.latosensu.web
	
	- **Packaging:** Jar
	
	- **Java:** 16
	
	  **Dependências:** Spring Web, Thymeleaf e Spring Boot DevTools
	
2. Baixar o arquivo .zip e descompactar em um diretório (```alomundo-mvc```)

- Alternativamente, é possível criar o projeto no Visual Studio Code (com as extensões Spring instaladas)

- Executar comando "Spring Initializr: Create a Maven Project" e utilizar as mesmas configurações acima

3. Abrir a classe ```br.ufscar.dc.latosensu.web.alomundomvc.AlomundoMvcApplication``` (Bootstrap class) para visualizar a classe principal da aplicação

4. Abrir um terminal no diretório do projeto e executar a aplicação através do comando `mvnw spring-boot:run`

- Alternativamente, é possível executar pelo Visual Studio Code, abrindo a aba `Spring Boot Dashboard`, encontrando a aplicação e executando

- Abrir http://localhost:8080 e notar que nenhuma pagina é apresentada

5. Criar o controlador `br.ufscar.dc.latosensu.web.alomundomvc.AlomundoController`
    
```java
package br.ufscar.dc.latosensu.web.alomundomvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlomundoController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
```

6. Criar a visão `index.html` (no diretório `/src/main/resources/templates/`)

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Alô Mundo</title>
</head>
<body>
    <h1>Alô Mundo</h1>
</body>
</html>
```

7. Executar novamente (basta recarregar a página) e ver o efeito (a página `index.html` é renderizada)

8. Alterar o controlador para enviar o horário/data corrente para a visão

```java
package br.ufscar.dc.latosensu.web.alomundomvc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlomundoController {
    @GetMapping("/")
    public String index(Model model) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
    
        model.addAttribute("date", dateFormat.format(cal.getTime()));        
        return "index";
    }
}
```

9. Alterar a visão para apresentar  o horário/data corrente

```diff
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">

<head>
    <title>Alô Mundo</title>
</head>

<body>
    <h1>Alô Mundo</h1>

+    <h4><span th:text="${date}">16 Março 2020</span></h4>
</body>

</html>
```

10. Executar novamente e ver o efeito (a página `index.html` é renderizada com a horário/data corrente)

11. Compare este exemplo com o procedimento utilizado nos exemplos com JSP e Servlet, por exemplo, a [Demonstração 7](exemplo7.md).

11. Fim

---

Leituras adicionais

- Spring Quickstart Guide

  https://spring.io/quickstart

  

- Tutorial: Thymeleaf + Spring
  
  https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

  

- Spring MVC Tutorial

  https://www.javatpoint.com/spring-mvc-tutorial
  
  
  
- Serving Web Content with Spring MVC

  https://spring.io/guides/gs/serving-web-content/