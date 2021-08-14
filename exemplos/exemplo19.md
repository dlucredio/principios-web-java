# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 19 - Internacionalização no Spring MVC

([Exemplo adaptado do material do prof. Delano Medeiros Beder](https://raw.githubusercontent.com/delanobeder/DSW1/master/Modulo06/Roteiro06-02.md))

Este exemplo introduz os conceitos básicos de internacionalização com o Spring MVC, capturando o idioma do navegador para exibir mensagens e formatos localizados.

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Maven 3.6.3

<hr>

1. Abrir o projeto da demonstração anterior

2. Alterar o arquivo `src/main/resources/application.properties`

```properties
spring.messages.basename = messages
```

3. Adicionar a biblioteca Native2Ascii (https://native2ascii.net/) via Maven, adicionando as seguintes linhas ao arquivo `pom.xml`:

```diff
    ...
    <build>
        <plugins>
+            <plugin>
+                <groupId>org.codehaus.mojo</groupId>
+                <artifactId>native2ascii-maven-plugin</artifactId>
+                <version>1.0-beta-1</version>
+                <executions>
+                    <execution>
+                        <id>native2ascii-utf8-resources</id>
+                        <goals>
+                            <goal>native2ascii</goal>
+                        </goals>
+                        <configuration>
+                            <dest>${project.build.directory}/classes</dest>
+                            <src>${project.resources[0].directory}</src>
+                            <encoding>UTF-8</encoding>
+                        </configuration>
+                    </execution>
+                </executions>
+            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

4. Atualizar o controlador `AlomundoController`:

```diff
package br.ufscar.dc.latosensu.web.alomundomvc;

+import java.text.DateFormat;
import java.util.Calendar;
+import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlomundoController {
    @GetMapping("/")
+    public String index(Model model, Locale locale) {
+        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.FULL, locale);
        Calendar cal = Calendar.getInstance();
    
+        model.addAttribute("dateString", dateFormat.format(cal.getTime()));
+        model.addAttribute("date", cal.getTime());      
        return "index";
    }
}
```

5. Atualizar a visão `index.html`:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:text="#{title.text}">Alô Mundo</title>
</head>
<body>
    <h1 th:text="#{welcome.text}">Alô Mundo</h1>

    <h4><span th:text="${dateString}">16 Março 2020</span></h4>
    <h4><span th:text="${#dates.format(date)}">16 Março 2020</span></h4>
</body>
</html>
```

6. Adicionar os arquivos de propriedades (I18n):

**Obs: Assegure-se que esses arquivos são salvos no formato UTF-8**

- Arquivo `src/main/resources/messages.properties`:

```properties
welcome.text = Hi. Welcome to Everyone.
title.text = Hello World
```

- Arquivo `src/main/resources/messages_pt_BR.properties`

```properties
welcome.text = Olá. Bem-vindo a todos.
title.text = Olá Mundo
```

- Arquivo `src/main/resources/messages_fr.properties`	

```properties
welcome.text = Salut. Bienvenue à tous.
title.text = Salut le Monde
```

- Arquivo `src/main/resources/messages_ja.properties`	

```properties
welcome.text = こんにちは、皆さん、ようこそ。
title.text = こんにちは世界
```

7. Executar e ver o efeito

- A página `index.html` é renderizada com a horário/data corrente (traduzido de acordo com o idioma do browser)
- Alterar a linguagem do navegador e recarregar a página para ver o efeito

8. Agora vamos adicionar um menu para selecionar a linguagem

9. Baixar as imagens das [quatro bandeiras aqui](./resources/)

10. Salvar as imagens na pasta `src/main/resources/static/flags`

11. Alterar o arquivo `index.html`

```diff
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">

<head>
    <title th:text="#{title.text}">Alô Mundo</title>
</head>

<body>
+    <div style="right: 40px; position: absolute">
+        <a th:href="@{''(lang=pt_BR)}"><img alt="" th:src="@{/flags/Brasil.gif}" style="width: 50px;" /></a>
+        <a th:href="@{''(lang=en)}"><img alt="" th:src="@{/flags/EUA.gif}" style="width: 50px;" /></a>
+        <a th:href="@{''(lang=fr)}"><img alt="" th:src="@{/flags/Franca.gif}" style="width: 50px;" /></a>
+        <a th:href="@{''(lang=ja)}"><img alt="" th:src="@{/flags/Japao.gif}" style="width: 50px;" /></a>
+    </div>
    <h1 th:text="#{welcome.text}">Alô Mundo</h1>

    <h4><span th:text="${dateString}">16 Março 2020</span></h4>
    <h4><span th:text="${#dates.format(date)}">16 Março 2020</span></h4>
</body>

</html>
```

12. Adicionar a classe de configuração da aplicação:

```java
package br.ufscar.dc.latosensu.web.alomundomvc;

import java.util.Locale;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ComponentScan(basePackages = "br.ufscar.dc.latosensu.web.alomundomvc")
public class AlomundoConfiguration implements WebMvcConfigurer{
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("pt","BR"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
```

13. Executar e ver o efeito. Agora é possível escolher a linguagem pelo menu

---

Leituras adicionais

- Guide to Internationalization in Spring Boot
  
  https://www.baeldung.com/spring-boot-internationalization



- Your Step-by-Step Guide to Spring Boot Internationalization

  https://phrase.com/blog/posts/spring-boot-internationalization/