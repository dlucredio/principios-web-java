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

2. Neste exemplo, não será necessário configurar um SGBD externo, pois utilizaremos o H2 integrado, que já vem configurado quando o adicionamos com uma das dependências no Spring Initializr

3. Também utilizaremos JPA, que elimina a necessidade de SQL para comandos mais simples

4. Criar a classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa2/entidades/Usuario.java`

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

5. Copiar o arquivo `logo.png` da pasta [resources](resources) deste repositório do GitHub para o projeto, para a pasta `src/main/resources/static/imagens` (ou encontrar outra imagem qualquer na web, de sua preferência)

6. Criar o arquivo `src/main/resources/static/estilo.css`:

```css
html,
body {
  height: 100%;
}

body {
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
```

7. Criar o arquivo `src/main/resources/templates/index.html` (exemplo adaptado do [site do bootstrap](https://getbootstrap.com/docs/5.1/examples/sign-in/)):

```html
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.87.0">
    <title>Bolão da copa</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.1/examples/sign-in/">

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj"
        crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link href="estilo.css" rel="stylesheet">
</head>

<body class="text-center">

    <main class="form-signin">
        <form>
            <img class="mb-4" src="imagens/logo.png" alt="" width="72" height="72">
            <h1 class="h3 mb-3 fw-normal">Bolão da copa</h1>

            <div class="form-floating">
                <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com">
                <label for="floatingInput">Email</label>
            </div>
            <div class="form-floating">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
                <label for="floatingPassword">Senha</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary" type="submit">Entrar</button>

            <a href="@{/usuario/novo}">Não tem conta? Registre-se aqui!</a>
        </form>
    </main>
</body>

</html>
```

8. 