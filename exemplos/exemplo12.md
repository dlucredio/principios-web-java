# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 12 - Diferentes tipos de erros em JSP

Este exemplo ilustra os diferentes tipos de erros que podem acontecer com JSP

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir o projeto da demonstração anterior
2. Modificar o arquivo ```alomundo.jsp```, para introduzir um erro na diretiva ```<%@page``` ... (Ex: ```<%#paje``` ...)
3. Executar e mostrar que dá erro, e nem gera o servlet
4. Modificar o arquivo ```alomundo.jsp```, corrigindo o erro introduzido em 2
5. Modificar o arquivo ```alomundo.jsp```, introduzindo um erro de compilação

```jsp
<% int a = 2 %>
```

6. Executar e mostrar que dá erro, mas gera o servlet
- Mostrar o servlet e achar o erro
7. Corrigir o erro introduzido em 5, e adicionar código com erro de execução

```jsp
        <% int a = 2;
        String str = null;
        a = str.length();
        %>
```

8. Executar e mostrar que dá erro, gerando o servlet
- Mostrar como rastrear o erro até o JSP. Primeiro, mostrar a saída do Tomcat, e falar que ele aponta a linha do servlet
- Mostrar que às vezes o próprio Tomcat indica o erro no JSP. Mas em muitos casos é preciso rastrear na mão até o JSP
9. Fim
