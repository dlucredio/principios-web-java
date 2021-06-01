# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 4 - Aplicação "Alo mundo" manual

Neste exemplo veremos como é feita uma aplicação Java web, a partir do zero e manualmente. Existem outras formas mais produtivas, mas este exemplo tem propósito didático

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Criar uma pasta ```AplicacaoAloMundo```
2. Criar uma pasta ```src/latosensu```
3. Criar arquivo texto ```AloMundoServlet.java```

```java
package latosensu;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        out.println("<h1>Alo mundo!</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
```

4. Compilar

```javac -cp /<caminho_para_tomcat>/lib/servlet-api.jar AloMundoServlet.java```

5. Mostrar que gerou um ```.class```
6. Montar a estrutura da aplicação
   - Criar a pasta ```WEB-INF/classes/latosensu```
   - Mover o .class para ```WEB-INF/classes/latosensu```
   - Criar o arquivo ```web.xml```na pasta ```WEB-INF```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
    <servlet>
        <servlet-name>AloMundo</servlet-name>
        <servlet-class>latosensu.AloMundoServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>AloMundo</servlet-name>
        <url-pattern>/TestarAloMundo</url-pattern>
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>
```

7. Criar o arquivo .war (executar a partir do Desktop, ou pasta que contem AplicacaoAloMundo)

```sh
<caminho_para_java>/jar -cvf AplicacaoAloMundo.war -C ./AplicacaoAloMundo .
```

8. Fazer o deploy
   - Rodar o tomcat
   - Entrar no ```localhost:8080```  e fazer o deploy pela aplicação ```manager```
9. Testar a aplicação, abrindo a URL ```http://localhost:8080/AplicacaoAloMundo/TestarAloMundo```
   - Fazer o undeploy da aplicação, pela aplicação ```manager```
10. Apagar o arquivo ```web.xml```
11. modificar o código fonte, adicionando as seguintes linhas:

```diff
package latosensu;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
+ import jakarta.servlet.annotation.WebServlet;

+ @WebServlet(urlPatterns = {"/AloMundoServlet"})
public class AloMundoServlet extends HttpServlet {
...
```

12. Refazer o processo e testar, abrindo agora a URL ```http://localhost:8080/AplicacaoAloMundo/AloMundoServlet```
13. Fazer undeploy da aplicação e parar o tomcat
14. Fim
