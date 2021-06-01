# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 5 - Aplicação "Alo mundo" com auxílio de uma IDE

Neste exemplo veremos como é feita uma aplicação Java web com o auxílio de uma ferramenta que facilita as tarefas.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>


Utilizando Apache Maven e Visual Studio Code.

1. Baixar e instalar o Tomcat conforme explicado na [Demonstração 3](exemplo3.md)
2. Criar um novo projeto usando o Apache Maven:

```sh
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4

Define value for property 'groupId': br.ufscar.dc.latosensu.web
Define value for property 'artifactId': AplicacaoAloMundoMaven
Define value for property 'version' 1.0-SNAPSHOT: : 
Define value for property 'package' br.ufscar.dc.latosensu.web: : 
Confirm properties configuration:
groupId: br.ufscar.dc.latosensu.web
artifactId: AplicacaoAloMundoMaven
version: 1.0-SNAPSHOT
package: br.ufscar.dc.latosensu.web
```

3. No projeto criado, no arquivo ```pom.xml```, adicione uma dependência para a API de servlets

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
4. Crie a pasta ```src/main/java/br/ufscar/dc/latosensu/web``` (a pasta ```src/main``` já deve existir)
5. Crie o arquivo ```AloMundoServlet.java``` dentro dessa classe, com o seguinte código:

```java
package br.ufscar.dc.latosensu.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/AloMundoServlet"})
public class AloMundoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
              HttpServletResponse response)
                 throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Primeira aplicação</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Alo mundo, agora no Maven!</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
```

6. Modifique o arquivo ```web.xml``` para ficar com o seguinte conteúdo:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
  <display-name>Archetype Created Web Application</display-name>
</web-app>
```

7. Para compilar e gerar o arquivo ```.war```, execute o seguinte comando no terminal (dentro da pasta do projeto):

```sh
mvn compile war:war
```

8. Será gerado o arquivo ```AplicacaoAloMundoMaven.war``` na pasta ```target```. Clique com o botão direito e escolha ```Run on Tomcat Server```
