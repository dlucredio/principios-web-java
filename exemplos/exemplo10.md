# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 10 - Atributos no escopo de sessão

Neste exemplo veremos como é possível acrescentar atributos no escopo de sessão.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Criar um servlet chamado ```ArmazenarNaSessaoServlet```, que pega um parametro chamado nome e armazena na sessão. Em seguida, imprime na página o nome armazenado

```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>ArmazenarNaSessaoServlet</title>");
    out.println("</head>");
    out.println("<body>");
    String nome = req.getParameter("nome");
    req.getSession().setAttribute("nome", nome);
    out.println("Nome armazenado na sessão:"+nome);
    out.println("</body>");
    out.println("</html>");
}
```

3. Criar um servlet ```ExibirSessaoServlet``` que exibe os dados da sessão

```java
resp.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = resp.getWriter()) {
    out.println("<html>");
    out.println("<head>");
    out.println("<title>ExibirSessaoServlet</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("Dados da sessão:<br/>");
    HttpSession sessao = req.getSession();
    String idSessao = sessao.getId();
    Date dataCriacao = new Date(sessao.getCreationTime());
    Date dataUltimoAcesso = new Date(sessao.getLastAccessedTime());
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS");

    out.println("Id:" + idSessao + "<br/>");
    out.println("Data criação:" + sdf.format(dataCriacao) + "<br/>");
    out.println("Data último acesso:" + sdf.format(dataUltimoAcesso) + "<br/>");
    out.println("Atributos:<br/>");
    Enumeration<String> nomesAtributos = sessao.getAttributeNames();
    while (nomesAtributos.hasMoreElements()) {
        String nomeAtributo = nomesAtributos.nextElement();
        Object valor = sessao.getAttribute(nomeAtributo);
        out.println(nomeAtributo + "=" + valor + "<br/>");
    }
    out.println("</body>");
    out.println("</html>");
}
```

4.Testar a aplicação
- Criar duas sessões diferentes usando browsers diferentes

```http://localhost:8080/DemonstracoesServletJSP/ArmazenarNaSessaoServlet?nome=Daniel```

- Exibir as duas sessões e mostrar que são diferentes

```http://localhost:8080/DemonstracoesServletJSP/ExibirSessaoServlet```

5. Modificar o arquivo ```web.xml``` para reduzir o timeout de sessão para 1 minuto

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
    <display-name>Archetype Created Web Application</display-name>
    <session-config>
        <session-timeout>1</session-timeout>
    </session-config>
</web-app>
```
6. Fim
