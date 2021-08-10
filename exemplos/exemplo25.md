# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 25 - Uma aplicação Spring MVC completa 2

Este exemplo demonstra uma aplicação Spring MVC completa

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Maven 3.6.3

<hr>

1. Criar um novo projeto Spring
	
	- **Project:** Maven Project
	
	- **Language:** Java
	
	- **Spring Boot:** 2.5.3
	
	- **Group:** br.ufscar.dc.latosensu.web
	
	- **Artifact:** bolaodacopa2
	
	- **Name:** Bolão da copa 2
	
	- **Description:** Bolão da copa 2
	
	- **Package name:** br.ufscar.dc.latosensu.web
	
	- **Packaging:** Jar
	
	- **Java:** 16
	
	  **Dependências:** Spring Web, Thymeleaf, Spring Boot DevTools, Validation, H2, JPA

2. Neste exemplo, não será necessário configurar um SGBD externo, pois utilizaremos JPA com H2 integrado, que já vem configurado quando o adicionamos com uma das dependências no Spring Initializr. Mas por default ele trabalha somente em memória, o que é ruim para testes, pois toda hora é necessário ficar recadastrando usuários e palpites.

3. Para utilizar persistência em disco, adicionar o seguinte conteúdo ao arquivo `src/main/resources/application.properties`:

```properties
# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2
# Datasource
spring.datasource.url=jdbc:h2:file:~/spring-boot-h2-db
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

- Esta configuração diz ao H2 que os dados serão salvos no arquivo `~/spring-boot-h2-db`. (o símbolo `~` se refere ao diretório do usuário)
- As demais propriedades são valores padrão para uso do H2 com JPA no SpringBoot

4. Vamos começar criando as classes de entidade:

- `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/entidades/Usuario.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Date dataDeNascimento;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String telefone, Date dataDeNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataDeNascimento = dataDeNascimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
```

- `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/entidades/Palpite.java`
```java
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
```

- Exercício: observe as classes acima e as anotações. Faça um desenho do modelo de dados que será criado para essas classes

5. Agora vamos criar os DAOs, que no caso do JPA são de responsabilidade do framework:

- classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/repositorios/RepositorioUsuario.java`:
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;

public interface RepositorioUsuario extends CrudRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
```

- classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/repositorios/RepositorioPalpite.java`:
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Palpite;


public interface RepositorioPalpite extends CrudRepository<Palpite, Long> {
    @Query("from Palpite p where p.palpiteiro.email = :email")
    Palpite findByPalpiteiroEmail(@Param("email") String email);
}
```

- Exercício: observe os métodos dos repositórios. Lembre-se que em JPA os métodos básicos, como `save` e `findAll` já estão disponíveis por default. Assim, essas interfaces contém apenas os métodos customizados. Escreva, em português mesmo, o que será recuperado nessas consultas adicionais.

6. Antes de começar as funções, vamos preparar o projeto para que as visões funcionem corretamente.

- Copiar o arquivo `logo.png` da pasta [resources](resources) deste repositório do GitHub para o projeto, para a pasta `src/main/resources/static/imagens` (ou encontrar outra imagem qualquer na web, de sua preferência)

- Criar o arquivo `src/main/resources/static/estilo.css`:

```css
html,
body {
    height: 100%;
}

body.text-center {
    display: flex;
    align-items: center;
    padding-top: 40px;
    padding-bottom: 40px;
    background-color: #f5f5f5;
}

.form-signin {
    width: 100%;
    max-width: 330px;
    padding: 15px;
    margin: auto;
}

.form-signin .checkbox {
    font-weight: 400;
}

.form-signin .form-floating:focus-within {
    z-index: 2;
}

.form-signin input[type="email"] {
    margin-bottom: -1px;
    border-bottom-right-radius: 0;
    border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
    margin-bottom: 10px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
}

.form-signin button[type="submit"] {
    margin-bottom: 20px;
}

.form-novousuario {
    width: 100%;
    max-width: 430px;
    padding: 15px;
    margin: auto;
}

.form-novousuario input {
    margin-bottom: 10px;
}

.form-novopalpite {
    width: 100%;
    max-width: 430px;
    padding: 15px;
    margin: auto;
}

.form-novopalpite input {
    margin-bottom: 10px;
}

.form-novopalpite button[type="submit"] {
    margin-top: 20px;
}

.logo {
    margin-right: 12px;
}
```

- Exercício: à medida que as páginas são criadas, nas próximas etapas, tente identificar como o estilo de cada elemento é definido nesse arquivo de estilo

- Criar o arquivo `src/main/resources/templates/error.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.1/examples/sign-in/">

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body>
    <main>
        <h1>Ops... algum erro aconteceu!</h1>
        <h2>Nossos engenheiros estão trabalhando nisso</h2>
        <a href="/">Voltar para o início</a>
    </main>
</body>

</html>
```

7. Vamos começar criando um cadastro de usuários e login

8. Criar o arquivo `src/main/resources/templates/index.html` (exemplo adaptado do [site do bootstrap](https://getbootstrap.com/docs/5.1/examples/sign-in/)):

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body class="text-center">
    <main class="form-signin">
        <form action="#" th:action="@{/usuario/login}" th:object="${dadosLogin}" method="post">
            <img class="mb-4" th:src="@{/imagens/logo.png}" alt="" width="72" height="72">
            <h1 class="h3 mb-3 fw-normal">Bolão da copa</h1>
            <div th:if="${!#strings.isEmpty(mensagem)}" class="alert" th:classAppend="${tipoMensagem=='erro'?'alert-danger':'alert-success'}" th:text="${mensagem}">Mensagem</div>

            <div class="form-floating">
                <input type="email" th:field="*{email}" class="form-control" id="floatingInput" placeholder="name@example.com">
                <label for="floatingInput">Email</label>
            </div>
            <div class="form-floating">
                <input type="password" th:field="*{senha}" class="form-control" id="floatingPassword" placeholder="Password">
                <label for="floatingPassword">Senha</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary" type="submit">Entrar</button>

            <a th:href="@{/usuario/novo}">Não tem conta? Registre-se aqui!</a>
        </form>
    </main>
</body>

</html>
```

- Exercício 1: Observe que os links são definidos com `th:href` ou `th:action` ao invés de `a:href` ou `action`, como em HTML simples. Por que?
- Exercício 2: Para que serve a tag `th:object` na definição do formulário de login?
- Exercício 3: Por que nos campos `th:field` do formulário, é utilizado o símbolo `*` ao invés de `$`, mais comum no ThymeLeaf?
- Exercício 4: Essa página funciona corretamente se for aberta diretamente? Ou é necessário passar por um controlador Spring primeiro? Por que?
- Exercício 5: Quando o usuário clicar em `Entrar`, o que acontece? Qual link será chamado?
- Exercício 6: Quando o usuário clicar em `Registre-se aqui!`, o que acontece? Qual link será chamado?

9. Criar o controlador `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/controladores/ControladorPrincipal.java`:

```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.support.SessionStatus;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.DadosLogin;

@Controller
public class ControladorPrincipal {

    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("dadosLogin", new DadosLogin());
        return "login";
    }

    @GetMapping(path = "/principal")
    public String principal() {
        return "principal";
    }

    @GetMapping(path = "/sair")
    public String sair(Model model, SessionStatus status) {
        model.addAttribute("dadosLogin", new DadosLogin());
        status.setComplete();
        return "login";
    }
}
```
- Esse controlador precisa de uma nova classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/DadosLogin.java`:

```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms;

public class DadosLogin {
    private String email;
    private String senha;
    
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
}
```

- Exercício 1: Observe que, por enquanto, apenas o método `index` é utilizado. Para que ele serve? (Dica: olhe os exercícios 2 e 3 do passo anterior)
- Exercício 2: Para que você acha que vão servir os métodos `principal` e `sair`?

10. Criar a página de cadastro de usuário `src/main/resources/templates/cadastrar.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body class="text-center">

    <main class="form-novousuario">
        <form action="#" th:action="@{/usuario/gravar}" th:object="${novoUsuario}" method="post">
            <img class="mb-4" th:src="@{/imagens/logo.png}" alt="" width="72" height="72">
            <h1 class="h3 mb-3 fw-normal">Bolão da copa - novo usuário</h1>

            <div class="alert alert-danger" th:if="${#fields.hasErrors('nome')}" th:errors="*{nome}"></div>
            <div class="form-floating">
                <input type="text" th:field="*{nome}" th:class="form-control" th:classAppend="${#fields.hasErrors('nome')?'is-invalid':''}" id="floatingInput" placeholder="Fulano da silva">
                <label for="floatingInput">Nome</label>
            </div>
            <div class="alert alert-danger" th:if="${#fields.hasErrors('telefone')}" th:errors="*{telefone}"></div>
            <div class="form-floating">
                <input type="text" th:field="*{telefone}" class="form-control" th:classAppend="${#fields.hasErrors('telefone')?'is-invalid':''}" id="floatingInput" placeholder="(xx) xxxxx-xxxx">
                <label for="floatingInput">Telefone</label>
            </div>
            <div class="alert alert-danger" th:if="${#fields.hasErrors('dataDeNascimento')}" th:errors="*{dataDeNascimento}"></div>
            <div class="form-floating">
                <input type="text" th:field="*{dataDeNascimento}" class="form-control" th:classAppend="${#fields.hasErrors('dataDeNascimento')?'is-invalid':''}" id="floatingInput" placeholder="31/10/1980">
                <label for="floatingInput">Data de nascimento</label>
            </div>
            <div class="alert alert-danger" th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></div>
            <div class="form-floating">
                <input type="email" th:field="*{email}" class="form-control" th:classAppend="${#fields.hasErrors('email')?'is-invalid':''}" id="floatingInput" placeholder="nome@exemplo.com">
                <label for="floatingInput">Email</label>
            </div>
            <div class="alert alert-danger" th:if="${#fields.hasErrors('senha')}" th:errors="*{senha}"></div>
            <div class="form-floating">
                <input type="password" th:field="*{senha}" class="form-control" th:classAppend="${#fields.hasErrors('senha')?'is-invalid':''}" id="floatingPassword" placeholder="Digite uma senha">
                <label for="floatingPassword">Senha</label>
            </div>
            <div class="alert alert-danger" th:if="${#fields.hasErrors('confirmarSenha')}" th:errors="*{confirmarSenha}"></div>
            <div class="form-floating">
                <input type="password" th:field="*{confirmarSenha}" class="form-control" th:classAppend="${#fields.hasErrors('confirmarSenha')?'is-invalid':''}" id="floatingPassword" placeholder="Confirme a senha">
                <label for="floatingPassword">Confirmação da senha</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary" type="submit">Registrar</button>
        </form>
    </main>
</body>

</html>
```

- Exercício 1: Cada campo do formulário tem um elemento `div class="alert alert-danger"` e um elemento `input`. O que cada um desses elementos faz?
- Exercício 2: O que acontece se o usuário clicar no botão `Registrar`?

11. Criar o controlador `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/controladores/ControladorUsuario.java`:

```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.DadosLogin;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoUsuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioUsuario;

@Controller
@SessionAttributes("usuarioLogado")
@RequestMapping(path = "/usuario")
public class ControladorUsuario {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    @GetMapping(path = "/novo")
    public String cadastrar(Model model) {
        model.addAttribute("novoUsuario", new NovoUsuario());
        return "cadastrar";
    }

    @PostMapping(path = "/gravar")
    public String gravar(Model model, @Valid NovoUsuario form, BindingResult result) throws ParseException {
        if (result.hasErrors()) {
            return "cadastrar";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataDeNascimento = sdf.parse(form.getDataDeNascimento());
            Usuario u = new Usuario(form.getNome(), form.getEmail(), form.getSenha(), form.getTelefone(),
                    dataDeNascimento);
            repositorioUsuario.save(u);
            model.addAttribute("mensagem", "Usuário registrado com sucesso!");
            model.addAttribute("tipoMensagem", "sucesso");
            model.addAttribute("dadosLogin", new DadosLogin());
            return "login";
        }
    }

    @PostMapping(path = "/login")
    public String login(Model model, DadosLogin dadosLogin) {
        Usuario u = repositorioUsuario.findByEmail(dadosLogin.getEmail());
        if (u != null && u.getSenha().equals(dadosLogin.getSenha())) {
            model.addAttribute("usuarioLogado", u);
            return "principal";
        } else {
            model.addAttribute("mensagem", "Dados de login errados!");
            model.addAttribute("tipoMensagem", "erro");
            return "login";
        }
    }
}
```

- Para entender completamente esse controlador, ainda é necessária a classe `NovoUsuario`. Mas já é possível entender a lógica geral de cada método.
- Exercício 1: Para que serve o atributo `repositorioUsuario`?
- Exercício 2: Para que serve o método `cadastrar`?
- Exercício 3: Para que serve o método `gravar`?
- Exercício 4: Para que serve o método `login`?

12. Criar a classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/NovoUsuario.java`:

```java
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
```

- Essa classe é apenas uma classe que armazena os dados do formulário de cadastro. Observe como cada atributo/setter/getter tem correspondência com um campo da página `cadastrar.html` (passo 10)
- É aqui também que é feita a validação. Observe as anotações em cada campo
- Exercício 1: ainda não definimos todas as validações customizadas, mas, a partir dos nomes, o que você acha que é um cadastro de usuário válido?
- Exercício 2: veja que em quase todos os casos, definimos algumas mensagens próprias. Mas na confirmação de senha e no caso de e-mail já cadastrado, não tem mensagem definida! Veja, no código a seguir, se consegue identificar a origem da mensagem que será exibida!

13. Vamos criar agora todos os validadores utilizados no cadastro de usuários:

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/EmailNaoCadastrado.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { EmailNaoCadastradoValidator.class })
public @interface EmailNaoCadastrado {

    String message() default "Este email já está cadastrado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/EmailNaoCadastradoValidator.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioUsuario;

public class EmailNaoCadastradoValidator implements ConstraintValidator<EmailNaoCadastrado, String> {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    public boolean isValid(String email, ConstraintValidatorContext context) {
        // Busca no banco de dados um usuário com o mesmo e-mail
        Usuario u = repositorioUsuario.findByEmail(email);
        if (u == null) {
            // Se não existir, ok
            return true;
        } else {
            // Se já existir, não podemos deixar cadastrar duplicado
            return false;
        }
    }
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/FormatoData.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FormatoDataValidator.class})
public @interface FormatoData {

    String message() default "Data deve seguir formato dd/mm/aaaa";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/FormatoDataValidator.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FormatoDataValidator implements ConstraintValidator<FormatoData, String> {
    public boolean isValid(String data, ConstraintValidatorContext context) {
        // Tenta converter a data com o formato específico
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            // Se acontecer algum erro, é porque a data está
            // em formato errado
            return false;
        }
    }
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/SenhaConfirmada.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SenhaConfirmadaValidator.class })
public @interface SenhaConfirmada {

    String message() default "A confirmação da senha não corresponde à senha";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/SenhaConfirmadaValidator.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoUsuario;

public class SenhaConfirmadaValidator implements ConstraintValidator<SenhaConfirmada, NovoUsuario> {
    public boolean isValid(NovoUsuario nu, ConstraintValidatorContext context) {
        // A senha e confirmação de senha são iguais?
        boolean valido=nu.getSenha().equals(nu.getConfirmarSenha());
        if(!valido) {
            // Se não for, vamos adicionar o erro no campo "confirmarSenha"
            // Para que o ThymeLeaf possa exibir a mensagem no campo de confirmação
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode( "confirmarSenha" ).addConstraintViolation();
        }
        return valido;   
    }
}
```

- Exercício: tente encontrar, na página `cadastrar.html`, o local exato onde são exibidas as mensagens de erro

14. Já é possível testar o cadastro e o login

- Abrir um terminal no diretório do projeto e executar a aplicação através do comando `mvnw spring-boot:run`

- Alternativamente, é possível executar pelo Visual Studio Code, abrindo a aba `Spring Boot Dashboard`, encontrando a aplicação e executando

- Abrir http://localhost:8080

- O cadastro funciona. O erro de login também funciona. Mas caso o login seja bem-sucedido, vai acusar erro, pois não existe ainda a página principal

- Tente entender TUDO o que foi feito até agora antes de seguir adiante. Em particular, revisite os exercícios da etapa 11, referente ao controlador de usuário:

- Exercício 1: Para que serve o atributo `repositorioUsuario`? Como ele funciona?
- Exercício 2: Para que serve o método `cadastrar`? Como ele funciona?
- Exercício 3: Para que serve o método `gravar`? Como ele funciona?
- Exercício 4: Para que serve o método `login`? Como ele funciona?

15. Agora vamos fazer a página principal e o cadastro de um novo palpite

16. Criar a página `src/main/resources/templates/principal.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <div class="container-fluid">
            <img th:src="@{/imagens/logo.png}" class="logo" alt="" width="48" height="48">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" th:href="@{/principal}">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/palpite/novo}">Novo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/palpite/ver}">Ver palpites</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a class="btn btn-outline-success" th:href="@{/sair}">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="container">
        <div class="bg-light p-5 rounded">
            <h1>Bolão da copa</h1>
            <p class="lead">Participe da melhor competição do futebol mundial de um jeito muito mais intere$$ante!</p>
            <p class="lead">Entrando no bolão, todos os jogos passarão a ser importantes! Até mesmo o clássico Azerbaijão e Moldávia!</p>
            <p class="lead">Escolha o que quer fazer:</p>
            <p><a class="btn btn-lg btn-primary" th:href="@{/palpite/novo}" role="button">Palpitar &raquo;</a></p>
            <p><a class="btn btn-lg btn-primary" th:href="@{/palpite/ver}" role="button">Ver os palpites até agora &raquo;</a></p>
        </div>
    </main>

</body>

</html>
```

- Observe como a barra de navegação resume todas as opções da aplicação
- Teste a opção `Sair`. O que exatamente acontece quando o usuário clica nesse elemento?

17. Agora vamos fazer a opção de registrar um palpite. Para isso, vamos pré-carregar as seleções possíveis, para que o usuário possa escolher.

18. Primeiro, vamos definir as seleções em um arquivo [YAML](https://pt.wikipedia.org/wiki/YAML). É bem auto-explicativo. O arquivo é `src/main/resources/application.yml`:

```yaml
bolao:
    selecoes:
        - África Do Sul
        - Alemanha
        - Argélia
        - Argentina
        - Austrália
        - Brasil
        - Camarões
        - Chile
        - Coreia Do Sul
        - Coreia Do Norte
        - Costa Do Marfim
        - Dinamarca
        - Eslováquia
        - Eslovênia
        - Espanha
        - Estados Unidos
        - França
        - Gana
        - Grécia
        - Holanda
        - Honduras
        - Inglaterra
        - Itália
        - Japão
        - México
        - Nigéria
        - Nova Zelândia
        - Paraguai
        - Portugal
        - Sérvia
        - Suíça
        - Uruguai
```

19. Para acessar os dados do arquivo, definimos uma classe de configuração, chamada `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/Bolaodacopa2Config.java`

```java
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
```

- Observe como a raiz do arquivo `YAML`, chamada `bolao`, é definida nesta classe
- Observe como a propriedade `selecoes`, que tem um `getter` e um `setter`, se corresponde com a lista de Strings do arquivo `YAML``
- Devido ao mecanismo de injeção de dependências do Spring, nada mais é necessário, apenas injetar essa classe onde for necessária (veja a seguir)

20. Criar o controlador `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/controladores/ControladorPalpite.java`:

```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import br.ufscar.dc.latosensu.web.bolaodacopa2.Bolaodacopa2Config;
import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Palpite;
import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;
import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoPalpite;
import br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios.RepositorioPalpite;

@Controller
@RequestMapping(path = "/palpite")
public class ControladorPalpite {

    @Autowired
    Bolaodacopa2Config config;

    @Autowired
    RepositorioPalpite repositorioPalpite;

    @ModelAttribute(name = "selecoes")
    List<String> selecoes() {
        return config.getSelecoes();
    }

    @GetMapping(path = "/novo")
    public String novo(Model model, @SessionAttribute(name = "usuarioLogado") Usuario usuarioLogado) {
        Palpite p = repositorioPalpite.findByPalpiteiroEmail(usuarioLogado.getEmail());
        if (p != null) {
            model.addAttribute("novoPalpite", p);
            model.addAttribute("jaFezPalpiteAntes", true);
        } else {
            model.addAttribute("novoPalpite", new NovoPalpite());
        }
        return "palpiteForm";
    }

    @PostMapping(path = "/gravar")
    public String gravar(Model model, @Valid NovoPalpite form, BindingResult result,
            @SessionAttribute(name = "usuarioLogado") Usuario usuarioLogado) {
        if (result.hasErrors()) {
            return "palpiteForm";
        } else {
            Palpite p = repositorioPalpite.findByPalpiteiroEmail(usuarioLogado.getEmail());
            if (p == null) {
                p = new Palpite();
                p.setPalpiteiro(usuarioLogado);
            }
            p.setCampeao(form.getCampeao());
            p.setVice(form.getVice());

            repositorioPalpite.save(p);

            return "palpiteRegistrado";
        }
    }

    @GetMapping(path = "/ver")
    public String ver(Model model) {
        model.addAttribute("todosPalpites", repositorioPalpite.findAll());
        return "listaPalpites";
    }
}
```

- Exercício 1: para que serve o atributo `config`? (dica, veja o passo anterior)
- Exercício 2: para que serve o atributo `repositorioPalpite`?
- Exercício 3: para que serve o método `selecoes()`?
- Exercício 4: para que serve o método `novo()`?
- Exercício 5: para que serve o método `gravar()`?
- Exercício 6: para que serve o método `ver()`?

21. Vamos criar a página de cadastro do palpite `src/main/resources/templates/palpiteForm.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <div class="container-fluid">
            <img th:src="@{/imagens/logo.png}" class="logo" alt="" width="48" height="48">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" th:href="@{/principal}">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" th:href="@{/palpite/novo}">Novo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/palpite/ver}">Ver palpites</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a class="btn btn-outline-success" th:href="@{/sair}">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="form-novopalpite">
        <div class="bg-light p-5 rounded">
            <h4>Novo palpite</h4>

            <div class="alert alert-warning" th:if="${jaFezPalpiteAntes}">
                Atenção! Você já fez um palpite antes. Ao enviar novo palpite, o anterior será apagado!
            </div>

            <div class="alert alert-danger" th:if="${#fields.hasErrors('novoPalpite')}" th:errors="${novoPalpite}"></div>

            <form action="#" th:action="@{/palpite/gravar}" th:object="${novoPalpite}" method="post">
                <label class="col-sm-4 control-label" for="campeao">Campeão</label>
                <div class="col-sm-12">
                    <select th:field="*{campeao}" class="form-select" th:classAppend="${#fields.hasErrors('campeao')?'is-invalid':''}" aria-label="Default select example" name="campeao" label="Campeão">
                        <option selected value="">Escolher</option>
                        <option th:each="s:${selecoes}" th:value="${s}" th:text="${s}">Seleção</option>
                    </select>
                    <div class="alert alert-danger" th:if="${#fields.hasErrors('campeao')}" th:errors="*{campeao}"></div>
                </div>
                <label class="col-sm-4 control-label" for="vice">Vice</label>
                <div class="col-sm-12">
                    <select th:field="*{vice}" class="form-select" th:classAppend="${#fields.hasErrors('vice')?'is-invalid':''}" aria-label="Default select example" name="vice" label="Vice">
                        <option selected value="">Escolher</option>
                        <option th:each="s:${selecoes}" th:value="${s}" th:text="${s}">Seleção</option>
                    </select>
                    <div class="alert alert-danger" th:if="${#fields.hasErrors('vice')}" th:errors="*{vice}"></div>
                </div>

                <button class="w-100 btn btn-lg btn-primary" type="submit">Enviar</button>
            </form>
        </div>
    </main>

</body>

</html>
```

- Se você compreendeu corretamente o cadastro de usuário, não deve ter nenhuma dificuldade para compreender 100% do que está sendo feito aqui

22. Vamos precisar (assim como no cadastro de usuário) de uma classe para o formulário/validação. Veja a seguir:

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/CampeaoEViceDiferentes.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CampeaoEViceDiferentesValidator.class})
public @interface CampeaoEViceDiferentes {

    String message() default "Campeão e vice devem ser diferentes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/validators/CampeaoEViceDiferentesValidator.java`
```java
package br.ufscar.dc.latosensu.web.bolaodacopa2.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.ufscar.dc.latosensu.web.bolaodacopa2.forms.NovoPalpite;

public class CampeaoEViceDiferentesValidator implements ConstraintValidator<CampeaoEViceDiferentes, NovoPalpite> {
    public boolean isValid(NovoPalpite novoPalpite, ConstraintValidatorContext context) {
        return !novoPalpite.getCampeao().equals(novoPalpite.getVice());
    }
}
```

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/forms/NovoPalpite.java`:

```java
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
```

- Exercício: com base no código acima, o que é um palpite válido?

23. Para concluir, basta inserir uma página de conclusão do palpite. Página `src/main/resources/templates/palpiteRegistrado.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <div class="container-fluid">
            <img th:src="@{/imagens/logo.png}" class="logo" alt="" width="48" height="48">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" th:href="@{/principal}">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" th:href="@{/palpite/novo}">Novo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/palpite/ver}">Ver palpites</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a class="btn btn-outline-success" th:href="@{/sair}">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="container">
        <div class="bg-light p-5 rounded">
            <h1>Seu palpite foi registrado!</h1>
            <p><a class="btn btn-lg btn-primary" th:href="@{/palpite/ver}" role="button">Ver os palpites até agora &raquo;</a></p>
        </div>
    </main>


</body>

</html>
```

24. Já é possível testar o cadastro de palpites

- Exercício: note que o sistema registra somente um palpite por usuário. Como isso é garantido?

25. A última parte (ver os palpites) também já está praticamente pronta. Basta a página `src/main/resources/templates/listaPalpites.html`:

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bolão da copa</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link th:href="@{/estilo.css}" rel="stylesheet">
</head>

<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <div class="container-fluid">
            <img th:src="@{/imagens/logo.png}" class="logo" alt="" width="48" height="48">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" th:href="@{/principal}">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/palpite/novo}">Novo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" th:href="@{/palpite/ver}">Ver palpites</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a class="btn btn-outline-success" th:href="@{/sair}">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="container">
        <div class="bg-light p-5 rounded">
            <h3 th:if="${#lists.isEmpty(todosPalpites)}">Não há palpites até o momento</h3>
            <table th:unless="${#lists.isEmpty(todosPalpites)}" class="table table-striped">
                <thead>
                    <tr>
                        <th scope="col">Palpiteiro</th>
                        <th scope="col">Campeão</th>
                        <th scope="col">Vice</th>
                    </tr>
                </thead>
                <tbody>
                    <tr th:each="p: ${todosPalpites}"></tr>
                    <td>[[${p.palpiteiro.nome}]]</td>
                    <td>[[${p.campeao}]]</td>
                    <td>[[${p.vice}]]</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </main>

</body>

</html>
```

26. Fim

<hr>
Exercícios de fixação (em ordem crescente de dificuldade):


a) Ao exibir a lista de palpites, destacar o palpite do usuário que está logado

b) Ao terminar o login, mostrar diretamente a lista de palpites, ao invés da página principal atual

c) Modificar a aplicação para deixá-la com suporte internacional completo (todas as mensagens são exibidas na língua definida no navegador)

d) Utilizar [Spring Security](https://spring.io/projects/spring-security) para gerenciar o login/sessão.

O [curso do prof. Delano Beder, do DC/UFSCar](https://github.com/delanobeder/DSW1/) tem alguns [exemplos completos](https://github.com/delanobeder/DSW1/blob/master/Modulo08/README.md) onde a internacionalização e Spring Security são utilizados.