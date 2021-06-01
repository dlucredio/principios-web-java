# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 14 - Transferindo controle para outros recursos web

Este exemplo mostra como redirecionar, encaminhar e incluir outros recursos a partir de um JSP

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Adicionar um formulário para login em ```index.jsp```

```jsp
<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>Aplicação</title>
</head>
<body>
    Login<br/>
    <form action="login.jsp">
        Usuário: <input type="text" name="usuario" /><br/>
        Senha: <input type="password" name="senha" /><br/>
        <input type="submit" value="Login" />
    </form>
</body>
</html>
```

3. Criar arquivo ```login.jsp```, com a lógica de controle de login

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String usuario = request.getParameter("usuario");
String senha = request.getParameter("senha");
if(usuario.equalsIgnoreCase(senha)) {
%>
<jsp:forward page="sucesso.jsp">
    <jsp:param name="nomeCompleto" value="Daniel Lucrédio" />
    <jsp:param name="ultimoAcesso" value="10/02/2010" />
</jsp:forward>
<% }
else { %>
<jsp:forward page="index.jsp" />
<% } %>
```

4. Criar a página ```sucesso.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        Bem-vindo <%=request.getParameter("nomeCompleto")%>!
        <br/>
        Seu último acesso foi em <%=request.getParameter("ultimoAcesso")%>!
    </body>
</html>
```

5. Testar a aplicação
6. Mostrar o código gerado para ```login.jsp```, identificar a linha do forward, e como os parâmetros são adicionados ao request
7. Criar a página ```cabecalho.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
Bem-vindo <%=request.getParameter("nomeCompleto")%>!
<br/>
Seu último acesso foi em <%=request.getParameter("ultimoAcesso")%>!
<hr>
```

8. Modificar a página ```sucesso.jsp``` para incluir o ```cabecalho.jsp```

```jsp
%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="cabecalho.jsp" />
        Menu de opções:<br/><br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
    </body>
</html>
```

9. Testar a aplicação
10. Mostrar o código gerado para sucesso.jsp, e identificar a linha do include
11. Modificar ```sucesso.jsp``` para usar ```<%@include%>```

```jsp
<%@include file="cabecalho.jsp" %>
```

12. Mostrar o código gerado agora, e comparar
13. Adicionar, na página ```sucesso.jsp```, o seguinte código

```jsp
<% String voceEstaEm = "Home --> Bem-vindo"; %>
```

14. Modificar a página ```cabecalho.jsp``` para acessar a variável local

```jsp
Você está em: <%=voceEstaEm%>!
```

15. Testar
16. Fim
