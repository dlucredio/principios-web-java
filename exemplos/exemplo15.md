# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 15 - Expression Language

Este exemplo mostra como utilizar Expression Language para facilitar a escrita de JSPs

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Criar uma classe ```Usuario```, com o seguinte código:

```java
package br.ufscar.dc.latosensu.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {
    private String nomeLogin, nome, senha;
    private Date ultimoAcesso;

    public Usuario() {
        ultimoAcesso = new Date();
    }

    public void setNomeLogin(String nomeLogin) {
        this.nomeLogin = nomeLogin;
    }

    public String getNomeLogin() {
        return nomeLogin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUltimoAcesso() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss:SSS");
        return sdf.format(ultimoAcesso);
    }
}
```

3. Modificar a página ```index.jsp```:

```jsp
    <body>
        Login<br/>
        <form action="login.jsp">
            Usuário: <input type="text" name="usuario" /><br/>
            Senha: <input type="password" name="senha" /><br/>
            <input type="submit" value="Login" />
        </form>
    </body>
```

4. Modificar a página ```login.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.ufscar.dc.latosensu.web.Usuario" %>
<%
String nomeLogin = request.getParameter("usuario");
String senha = request.getParameter("senha");
if(senha.equals(nomeLogin)) {
    Usuario usuario = new Usuario();
    usuario.setNome("Daniel Lucrédio");
    usuario.setNomeLogin(nomeLogin);
    usuario.setSenha(senha);
    session.setAttribute("usuarioLogado", usuario);
%>
<jsp:forward page="principal.jsp"/>
<% }
else { %>
<jsp:forward page="index.jsp" />
<% } %>
```

5. Criar uma página ```principal.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Principal</title>
    </head>
    <body>
        Bem-vindo ${sessionScope.usuarioLogado.nome}
        
        (${sessionScope.usuarioLogado.nomeLogin})!
        <br/>
        Seu último acesso foi em ${sessionScope.usuarioLogado.ultimoAcesso}!<br/>
        Sua senha é ${sessionScope.usuarioLogado.senha}

    </body>
</html>
```

6. Testar
7. Fim
