# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 16 - Java Standard Tag Library

Este exemplo mostra como utilizar JSTL para facilitar a escrita de JSPs

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir projeto da demonstração anterior
2. Adicionar biblioteca JSTL via Maven, no arquivo ```pom.xml```

```diff
    <dependencies>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
        </dependency>
+        <dependency>
+            <groupId>org.glassfish.web</groupId>
+            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
+            <version>2.0.0</version>
+        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

3. Modificar o arquivo ```principal.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="local" value="Principal" scope="page" />
<c:set var="acessos" scope="page">
    <table border="1">
        <tr>
            <td>Acessos: 34552</td>
        </tr>
    </table>
</c:set>
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
        <hr>
        Você está em: ${pageScope.local}
        <hr>
        Menu de opções:<br/><br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>
        Conteúdo da página<br/>

        <hr>
        ${pageScope.acessos}
    </body>
</html>
```

4. Modificar arquivo ```login.jsp``` para remover os scriptlets

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${param.usuario == param.senha}">
        <jsp:useBean id="usuarioLogado" class="br.ufscar.dc.latosensu.web.Usuario" scope="session" />
        <jsp:setProperty name="usuarioLogado" property="nome" value="Daniel Lucrédio" />
        <jsp:setProperty name="usuarioLogado" property="nomeLogin" param="usuario" />
        <jsp:setProperty name="usuarioLogado" property="senha" />
        <jsp:forward page="principal.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:forward page="index.jsp" />
    </c:otherwise>
</c:choose>
```

5. Modificar o arquivo ```login.jsp``` para usar JSTL no lugar do ```jsp:setProperty```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${param.usuario == param.senha}">
        <jsp:useBean id="usuarioLogado" class="br.ufscar.dc.latosensu.web.Usuario" scope="session" />
        <c:set target="${usuarioLogado}" property="nome" value="Daniel Lucrédio" />
        <c:set target="${usuarioLogado}" property="nomeLogin" value="${param.usuario}" />
        <c:set target="${usuarioLogado}" property="senha" value="${param.senha}" />
        <jsp:forward page="principal.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:forward page="index.jsp" />
    </c:otherwise>
</c:choose>
```

6. Criar uma classe ```ItemMenu```, com duas propriedades ```String```: ```nome``` e ```link```

```java
package br.ufscar.dc.latosensu.web;

public class ItemMenu {
    private String nome, link;    

    public ItemMenu(String nome, String link) {
        this.nome = nome;
        this.link = link;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
```

7. Criar uma classe ```Menu```:

```java
package br.ufscar.dc.latosensu.web;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    List<ItemMenu> itensMenu;

    public Menu() {
        itensMenu = new ArrayList<ItemMenu>();
        itensMenu.add(new ItemMenu("Principal","principal.jsp"));
        itensMenu.add(new ItemMenu("Notícias","noticias.jsp"));
        itensMenu.add(new ItemMenu("Produtos","produtos.jsp"));
        itensMenu.add(new ItemMenu("Fale conosco","contato.jsp"));
    }

    public List<ItemMenu> getItensMenu() {
        return itensMenu;
    }
}

```

8. Modificar o arquivo ```principal.jsp``` para implementar um menu dinâmico

```jsp
        Menu de opções:<br/><br/>
        <jsp:useBean id="menu" class="br.ufscar.dc.latosensu.web.Menu" scope="application" />
        <c:forEach var="im" items="${menu.itensMenu}">
            <a href="${im.link}">${im.nome}</a><br/>
        </c:forEach>
```

9. Testar a aplicação (limpar, construir e re-implantar)
10. Fim
