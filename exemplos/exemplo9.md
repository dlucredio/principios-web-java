# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 9 - Atributos no escopo de aplicação

Neste exemplo veremos como é possível acrescentar atributos no escopo de aplicação.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Modificar o ```ServletA``` para adicionar um atributo no ```ServletContext```, e mostrar na página, caso esteja definido

```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Servlet ServletA</title>");
    out.println("</head>");
    out.println("<body>");
    getServletContext().setAttribute("valor", 100);
    out.println("Página gerada pelo ServletA: ");
    if (getServletContext().getAttribute("valor") == null) {
        out.println("Valor não encontrado!");
    } else {
        Integer i = (Integer) getServletContext().getAttribute("valor");
        out.println("Valor=" + i);
    }
    out.println("</body>");
    out.println("</html>");
}
```

3. O ```ServletB``` e ```ServletC``` fazem apenas a exibição na página, de forma similar ao passo 4

- ```ServletB```
```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Servlet ServletB</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("Página gerada pelo ServletB: ");
    if (getServletContext().getAttribute("valor") == null) {
        out.println("Valor não encontrado!");
    } else {
        Integer i = (Integer) getServletContext().getAttribute("valor");
        out.println("Valor=" + i);
    }
    out.println("</body>");
    out.println("</html>");
}
```

- ```ServletC```
```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Servlet ServletC</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("Página gerada pelo ServletC: ");
    if (getServletContext().getAttribute("valor") == null) {
        out.println("Valor não encontrado!");
    } else {
        Integer i = (Integer) getServletContext().getAttribute("valor");
        out.println("Valor=" + i);
    }
    out.println("</body>");
    out.println("</html>");
}
```

4. Testar a aplicação
   - Abrir ```ServletB``` e/ou ```ServletC```, e notar que não existe valor
   - Agora abrir o ```ServletA``` antes
   - Abrir ```ServletC``` em outro browser, para simular outro cliente
5. Fim
