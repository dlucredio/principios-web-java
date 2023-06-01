# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 23 - Anotações do Spring MVC Controller

Este exemplo ilustra como configurar um MVC controller usando anotações

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "20.0.1"
- Apache Maven 3.8.5

<hr>

1. Abrir o projeto da demonstração anterior
2. Criar um novo controlador (classe `br.ufscar.dc.latosensu.web.demospringweb.RequestsController`) para demonstrar os diferentes tipos de requests:

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/testaRequests")
public class RequestsController {

    @RequestMapping(path="/fazendoGet1", method = RequestMethod.GET)
    public String tratarGet1(Model model) {
        model.addAttribute("mensagem", "Chegou um get aqui 1!");
        return "index";
    }    

    @GetMapping(path="/fazendoGet2")
    public String tratarGet2(Model model) {
        model.addAttribute("mensagem", "Chegou um get aqui 2!");
        return "index";
    }

    @PostMapping
    public String tratarPost(Model model) {
        model.addAttribute("mensagem", "Chegou um post aqui!");
        return "index";
    }
}
```
3. Criar um novo arquivo HTML estático (pasta `src/main/resources/static/form.html`), com um form que envia um POST

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>Form</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
</head>
<body>
    <form action="/testaRequests" method="POST">
        Nome: <input type="text" name="nome" /><br/>
        Email: <input type="text" name="email" /><br/>
        Senha: <input type="password" name="senha" /><br/>
        <input type="submit" value="Enviar" />
    </form>    
</body>
</html>
```

4. Executar e testar. Para testar os gets, basta digitar a URL no navegador. Para testar o post, usar o form
5. Vamos testar agora as anotações mais detalhadas. 
6. Adicionar a dependência ao validador do SpringBoot, no arquivo `pom.xml` (IMPORTANTE: neste ponto é preciso recarregar a aplicação, reiniciando o servidor):

```diff
...
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
+        <dependency>
+            <groupId>org.springframework.boot</groupId>
+            <artifactId>spring-boot-starter-validation</artifactId>
+            <version>3.1.0</version>
+        </dependency>

    </dependencies>
...
```

7. Criar um controlador para produtos (classe `br.ufscar.dc.latosensu.web.demospringweb.ProdutoController`)

```java
package br.ufscar.dc.latosensu.web.demospringweb;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/produto")
public class ProdutoController {

    @GetMapping
    public String getPaginaInicial() {
        return "produto";
    }

    @GetMapping(path = "/encontrar/{id}")
    public String getLivro(@PathVariable String id, Model model) {
        model.addAttribute("mensagem", "Recuperado o livro " + id);
        return "produto";
    }

    @PostMapping(path = "/buscarProduto")
    public String buscarProdutoPeloNome(@RequestParam String nome, Model model) {
        model.addAttribute("mensagem", "Encontrado o livro " + nome);
        return "produto";
    }

    @ModelAttribute(name = "categorias")
    public List<String> getCategorias() {
        return Arrays.asList("Entretenimento", "Moda", "Eletrônicos", "Smartphones");
    }

    @GetMapping(path = "/buscarProdutosPorCategoria")
    public String buscarProdutosPorCategoria(@RequestParam String categoria, Model model) {
        model.addAttribute("mensagem", "Encontrados os livros da categoria " + categoria);
        return "produto";
    }

    @ModelAttribute(name="produtoForm")
    public ProdutoForm getProdutoForm() {
        return new ProdutoForm();
    }
   
    // antes de criar o método a seguir, ir para o passo 8
    @PostMapping(path = "/cadastrarProduto")
    public String cadastrarProduto(@Valid ProdutoForm produtoForm, BindingResult result, RedirectAttributes attr) {
        if(result.hasErrors()) {
            return "produto";
        } else {
            System.out.format("INSERT INTO PRODUTO VALUES (%s,%s,%s)",produtoForm.getId(),produtoForm.getNome(),produtoForm.getCategoria());
            attr.addAttribute("produto", produtoForm.getNome());
            return "redirect:/sucesso.html";
        }
    }
}
```

8. Vai ser necessária uma classe `br.ufscar.dc.latosensu.web.demospringweb.ProdutoForm`:

```java
package br.ufscaar.dc.latosensu.web.demospringweb;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProdutoForm {
    @NotBlank
    private String id;

    @Size(min = 5, max=10)
    private String nome;
    
    private String categoria;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }   
}
```

9. Criar uma página `src/main/resources/templates/produto.html`:

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>Produtos</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
</head>
<body>
    <h1>Produtos</h1>
    <span th:text="${mensagem}">Mensagem</span><br/>

    Opções: <br/>
    <a href="@{/produto/encontrar/15}">Buscar o livro 15</a><br/>

    <br/>
    Buscar pelo nome:<br/>
    <form action="@{/produto/buscarProduto}" method="post">
        Nome: <input type="text" name="nome" />
        <input type="submit" value="Buscar" />
    </form>

    <br/>

    Buscar pela categoria:<br/>
    <form action="@{/produto/buscarProdutosPorCategoria}" method="get">
        Escolha:
        <select name="categoria">
            <option th:each="c:${categorias}" th:value="${c}" th:text="${c}">Categoria</option>
        </select>
        <input type="submit" value="Buscar" />
    </form>

    <br/>
    Cadastrar novo produto:<br/>
    <form action="#" th:action="@{/produto/cadastrarProduto}" th:object="${produtoForm}" method="post">
        Id: <input type="text" th:field="*{id}" id="id"> 
        <span th:if="${#fields.hasErrors('id')}" th:errors="*{id}"></span>
        <br/>
        Nome: <input type="text" th:field="*{nome}" id="nome">
        <span th:if="${#fields.hasErrors('nome')}" th:errors="*{nome}"></span>
        <br/>
        Categoria:
        <select th:field="*{categoria}" id="categoria">
            <option th:each="c:${categorias}" th:value="${c}" th:text="${c}">Categoria</option>
        </select><br/>
        <input type="submit" value="Cadastrar" />
    </form>

</body>
</html>
```

10. Criar uma página `src/main/resources/static/sucesso.html`:

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>Sucesso</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
</head>
<body>
    Produto cadastrado com sucesso!    
</body>
</html>
```

11. Testar (no final, reparar que o nome do produto foi repassado junto com o redirect)
12. Fim
