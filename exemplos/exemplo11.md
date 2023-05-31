# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 11 - Alo mundo com JSP

Neste exemplo faremos um exemplo simples para mostrar o que é JSP

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Apagar o arquivo ```index.jsp``` que já é criado por padrão
3. Criar um servlet:

```java
package br.ufscar.dc.latosensu.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/AloMundoServlet" })
public class AloMundoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AloMundo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("Alo mundo");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
```

4. Criar um JSP alo mundo com o mesmo texto do servlet

```jsp
<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>Alo mundo</title>
</head>
<body>
    Alo mundo em JSP
</body>
</html>
```

5. Testar
6. Depois de ver o JSP no browser, abrir o servlet gerado para ele, na pasta ```work``` do Tomcat, e comparar com o servlet feito à mão
- Obs: olhar nas propriedades do servidor, o caminho para o Catalina base. Ou abrir o arquivo de configuração do Tomcat e ver onde ele está, para descobrir onde é o Catalina base. Ou, caso esteja usando a extensão "Community Server" do VS Code, olhar na janela "Output" o caminho onde foi feita a implantação.
7. Adicionar um código no servlet, para fazer um for de 1 a 10

```java
for(int i=0;i<10;i++) {
    String linha = "Linha "+i;
    out.println(i+": "+linha+"<br/>");
}
```

8. Adicionar um código equivalente no JSP, para comparar

```jsp
<% for(int i=0;i<10;i++) {
    String linha = "Linha "+i;
    %>
        <%=i%>: <%=linha%> <br/>
<% } %>
```

9. Rodar e ver o efeito
10. Mostrar o servlet gerado
11. Fim
