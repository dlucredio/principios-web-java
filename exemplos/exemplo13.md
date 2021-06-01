# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 13 - Objetos implícitos em JSP

Este exemplo mostra como recuperar parâmetros usando um objeto implícito em JSP

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Modificar o arquivo ```alomundo.jsp```:

```jsp
<%
int num = Integer.parseInt(request.getParameter("num"));
String nome = request.getParameter("nome");
for(int i=0;i<num;i++) {
%>
Olá <%=nome%>!<br/>
<% } %>
```

3. Testar passando diferentes parâmetros
4. Fim