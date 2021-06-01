# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 7 - Redirecionamento e encaminhamento

Neste exemplo veremos como os servlets podem cooperar

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Criar um arquivo ```pagina.html``` qualquer
3. Adicionar um novo servlet ```RedirecionadorServlet```, com o seguinte código:

```java
response.sendRedirect("pagina.html");
```

4. Testar, abrindo o servlet, e observar que mudou a URL no browser
5. Adicionar um novo servlet ```EncaminhadorServlet```, com o seguinte código:

```java
request.getRequestDispatcher("pagina.html").forward(request, response);
```

6. Testar e observar que não mudou a URL no browser
7. Adicionar um novo servlet ```ImprimirParametrosServlet```, que imprime todos os parâmetros

```java
package br.ufscar.dc.latosensu.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/ImprimirParametrosServlet" })
public class ImprimirParametrosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Interpretando HTTP Request</title>");
            out.println("</head>");
            out.println("<body>");
            Map<String, String[]> mapaDeParametros = req.getParameterMap();
            Set<Entry<String, String[]>> conjuntoDeParametros = mapaDeParametros.entrySet();
            for (Entry<String, String[]> parametro : conjuntoDeParametros) {
                out.println(parametro.getKey() + ":");
                for (String v : parametro.getValue()) {
                    out.println("[" + v + "] ");
                }
                out.println("<br/>");
            }
        }

    }
}

```

8. Modificar os servlets ```RedirecionadorServlet``` e ```EncaminhadorServlet``` para chamar o novo servlet ```ImprimirParametrosServlet``` ao invés de ```pagina.html``` e testar
9. Criar dois arquivos html, um ```cabecalho.html``` e um ```rodape.html``` (não usar caracteres especiais, use &aacute; etc)
10. Criar um novo servlet, chamado ```TestaIncludeServlet```, que faz a inclusão dos dois HTMLs gerados no passo anterior

```java
      ...
      request.getRequestDispatcher("cabecalho.html").include(request, response);
      ...
      request.getRequestDispatcher("rodape.html").include(request, response);
      ...
```

11. Testar a aplicação
12. Fim
