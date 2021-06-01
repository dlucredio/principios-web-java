# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 8 - Atributos no escopo de requisição

Neste exemplo veremos como é possível acrescentar atributos no escopo de requisição.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Criar três servlets: ```ServletA```, ```ServletB``` e ```ServletC```
3. O ```ServletA``` adiciona um atributo na requisição e faz forward para o ServletB

```java
req.setAttribute("valor", new Integer(100));
req.getRequestDispatcher("ServletB").forward(req, resp);
```

4. O ```ServletB``` recupera o atributo, modifica-o e o imprime, além de incluir o ```ServletC```

```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Servlet ServletB</title>");
    out.println("</head>");
    out.println("<body>");
    Integer i = (Integer) req.getAttribute("valor");
    i = i + 30;
    req.setAttribute("valor", i);
    out.println("Conteúdo gerado no ServletB: Valor=" + i + "<br/>");
    req.getRequestDispatcher("ServletC").include(req, resp);
    out.println("</body>");
    out.println("</html>");
}
```

5. O ```ServletC``` recupera o atributo, modifica-o e o imprime

```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Servlet ServletB</title>");
    out.println("</head>");
    out.println("<body>");
    Integer i = (Integer) req.getAttribute("valor");
    i = i + 1000;
    req.setAttribute("valor", i);
    out.println("Conteúdo gerado no ServletC: Valor=" + i + "<br/>");
    out.println("</body>");
    out.println("</html>");
}
```

6. Testar a aplicação e mostrar que o valor está sendo passado de um servlet para outro. Se chamar o ```ServletB``` direto irá dar uma exceção.
7. Fim
