# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 6 - Interpretando um HTTP Request

Neste exemplo veremos como ler os dados de uma requisição HTTP.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Criar um novo projeto Web: ```DemonstracoesServletJSP```

```sh
mvn archetype:generate "-DarchetypeGroupId=org.apache.maven.archetypes" "-DarchetypeArtifactId=maven-archetype-webapp" "-DarchetypeVersion=1.4"

Define value for property 'groupId': br.ufscar.dc.latosensu.web
Define value for property 'artifactId': DemonstracoesServletJSP
Define value for property 'version' 1.0-SNAPSHOT: : 
Define value for property 'package' br.ufscar.dc.latosensu.web: : 
Confirm properties configuration:
groupId: br.ufscar.dc.latosensu.web
artifactId: DemonstracoesServletJSP
version: 1.0-SNAPSHOT
package: br.ufscar.dc.latosensu.web

```

2. No projeto criado, no arquivo ```pom.xml```, adicione uma dependência para a API de servlets

```diff
    ...
    <dependencies>
+        <dependency>
+            <groupId>jakarta.servlet</groupId>
+            <artifactId>jakarta.servlet-api</artifactId>
+            <version>5.0.0</version>
+        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    ...
```
3. Crie a pasta ```src/main/java/br/ufscar/dc/latosensu/web``` (a pasta ```src/main``` já deve existir)
4. Modificar o arquivo ```src/main/webapp/WEB-INF/web.xml```:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
  <display-name>Archetype Created Web Application</display-name>
</web-app>
```

5. Adicionar um novo Servlet: ```br.ufscar.dc.latosensu.web.InterpretarRequestServlet```:

```java
package br.ufscar.dc.latosensu.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/InterpretarRequestServlet"})
public class InterpretarRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Interpretando HTTP Request</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Alo mundo, agora no Maven!</h1>");
            String requestURL = req.getRequestURL().toString();
            String protocol = req.getProtocol();
            int port = req.getLocalPort();
            String queryString = req.getQueryString();
            out.println("Requisição: " + requestURL + "<br/>");
            out.println("Protocolo: " + protocol + "<br/>");
            out.println("Porta: " + port + "<br/>");
            out.println("Query: " + queryString + "<br/>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
```

5. Compilar e rodar, e fazer diferentes queries no browser
6. Adicionar uma página HTML, chamada ```testaRequisicoes.html```
7. Adicionar um link para o servlet e testar

```html
<a href="InterpretarRequestServlet?a=123&b=123">Testa</a><br/>
```

8. Adicionar um form para enviar dados via GET e testar

```html
 <form name="teste" action="InterpretarRequestServlet" method="get">
     Nome: <input type="text" name="nome" /> <br/>
     E-mail: <input type="text" name="email" /> <br/>
     Confirmação de e-mail: <input type="text" name="email" /> <br/>
     Senha: <input type="password" name="senha" /><br/>
     Gênero: <input type="radio" name="genero" value="Masculino" /> Masculino
     <input type="radio" name="genero" value="Feminino" /> Feminino <br/>
     Receber notícias: <input type="checkbox" name="receber" value="claro" /><br/>
     <input type="submit" name="enviar" value="enviado" />
 </form>
```

9. Modificar o método para POST (```method="post"```) e testar. Mostrar que agora não funciona pois não tem um tratamento para o POST no servlet
10. Modificar o código do servlet para tratar o POST e mostrar os parâmetros

```java
...
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Interpretando HTTP Request</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Alo mundo, agora no Maven!</h1>");
            String requestURL = req.getRequestURL().toString();
            String protocol = req.getProtocol();
            int port = req.getLocalPort();
            String queryString = req.getQueryString();
            Map<String, String[]> mapaDeParametros = req.getParameterMap();
            Set<Entry<String, String[]>> conjuntoDeParametros = mapaDeParametros.entrySet();
            for (Entry<String, String[]> parametro : conjuntoDeParametros) {
                out.println(parametro.getKey() + ":");
                for (String v : parametro.getValue()) {
                    out.println("[" + v + "] ");
                }
                out.println("<br/>");
            }
            out.println("Requisição: " + requestURL + "<br/>");
            out.println("Protocolo: " + protocol + "<br/>");
            out.println("Porta: " + port + "<br/>");
            out.println("Query: " + queryString + "<br/>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    ...
```

11. Mostrar como pegar parâmetros pelo nome

```java
            out.println("O nome é: "+req.getParameter("nome")+"<br/>");
            boolean receberNoticias = false;
            String strReceberNoticias = req.getParameter("receber");
            if(strReceberNoticias != null && strReceberNoticias.equals("claro"))
                receberNoticias = true;
            out.println("Receber noticias:"+receberNoticias+"<br/>");
```

12. Adicionar um input type hidden e testar

```html
<input type="hidden" name="teste" value="testando" />
```

12. Adicionar um input type file, para mostrar como processar um POST
13. Modificar o enctype do form

```html
enctype="multipart/form-data"
```
14. Adicionar um novo campo no formulário

```html
Foto: <input type="file" name="arquivoFoto"/> <br/>
```

15. Modificar o servlet para receber o arquivo e gravar no Desktop

```java
            File upload = new File("/Users/daniellucredio/Desktop/arquivoRecebido.txt");
            upload.createNewFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(upload)); BufferedReader br = req.getReader()) {
                String linha = null;
                pw.println("Conteúdo recebido");
                while ((linha = br.readLine()) != null) {
                    pw.println(linha);
                }
                pw.println("Fim do conteúdo");
                pw.flush();
            }
```

16. Criar um arquivo texto no desktop só pra ilustrar
17. Testar
   - Agora, com o novo enctype, os ```getParameter``` não funcionam mais
   - Agora tudo deve ser lido e interpretado "na mão"
   - Existem frameworks, como o Jakarta Commons, que fazem essa tarefa
18. Mudar o método de novo para GET, e mostrar que dessa vez não dá para ler
19. Fim
