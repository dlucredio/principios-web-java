# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 24 - Uma aplicação Spring MVC completa

Este exemplo altera o exemplo da [Demonstração 17](exemplo17.md) para usar Spring MVC

<hr>

Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "20.0.1"
- Apache Maven 3.8.5

<hr>

1. Abrir o projeto da [Demonstração 17](exemplo17.md) e deixar aberto como referência (vamos copiar e colar muito código de lá)
2. Criar um novo projeto Spring
	
	- **Project:** Maven Project
	
	- **Language:** Java
	
	- **Spring Boot:** 2.5.3
	
	- **Group:** br.ufscar.dc.latosensu.web
	
	- **Artifact:** bolaodacopa1-spring
	
	- **Name:** Bolão da copa 1 em Spring
	
	- **Description:** Bolão da copa 1 em Spring
	
	- **Package name:** br.ufscar.dc.latosensu.web
	
	- **Packaging:** Jar
	
	- **Java:** 16
	
	  **Dependências:** Spring Web, Thymeleaf, Spring Boot DevTools, JDBC API, Validation

3. No projeto criado, adicionar os drivers do Derby no arquivo `pom.xml` (é equivalente a copiar todos os arquivos .jar conforme passo 5 da [Demonstração 17](exemplo17.md))

```diff
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

+        <dependency>
+            <groupId>org.apache.derby</groupId>
+            <artifactId>derbyclient</artifactId>
+            <version>10.15.2.0</version>
+        </dependency>
+        <dependency>
+            <groupId>org.apache.derby</groupId>
+            <artifactId>derbytools</artifactId>
+            <version>10.15.2.0</version>
+        </dependency>
+        <dependency>
+            <groupId>org.apache.derby</groupId>
+            <artifactId>derbyshared</artifactId>
+            <version>10.15.2.0</version>
+        </dependency>

    </dependencies>
```

4. Adicionar as propriedades de conexão ao arquivo `src/main/application.properties`:

```properties
spring.datasource.driver-class-name=org.apache.derby.jdbc.ClientDriver
spring.datasource.url=jdbc:derby://localhost:1527/bolao1
spring.datasource.username=bolao1
spring.datasource.password=senha1
```

5. Iniciar o servidor conforme o passo 2 da [Demonstração 17](exemplo17.md))

6. Criar as classes `Usuario` e `Palpite` no diretório `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa1spring/modelo` (basta copiar da [Demonstração 17](exemplo17.md) e mudar o pacote)

- `Usuario.java`:
```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo;

import java.util.Date;

public class Usuario {
    private int id;
    private String nome, email, telefone;
    private Date dataDeNascimento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
```

- `Palpite.java`:
```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo;

public class Palpite {
    private int id;
    private String campeao;
    private String vice;
    private Usuario palpiteiro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCampeao() {
        return campeao;
    }

    public void setCampeao(String campeao) {
        this.campeao = campeao;
    }

    public String getVice() {
        return vice;
    }

    public void setVice(String vice) {
        this.vice = vice;
    }

    public Usuario getPalpiteiro() {
        return palpiteiro;
    }

    public void setPalpiteiro(Usuario palpiteiro) {
        this.palpiteiro = palpiteiro;
    }
}
```

7. Criar as classes DAO, copiando novamente da [Demonstração 17](exemplo17) e mudando o pacote para `br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao`

8. Também vamos usar `@Autowired` para injetar o data source e usar `@Component` para que essas classes possam ser injetadas em um controlador

- `UsuarioDAO.java`:
```diff
+package br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.NamingException;
import javax.sql.DataSource;

-import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;

+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.stereotype.Component;

+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Usuario;

+@Component
public class UsuarioDAO {

    private final static String CRIAR_USUARIO_SQL = "insert into Usuario"
            + " (nome, email, telefone, dataDeNascimento)"
            + " values (?,?,?,?)";

    private final static String BUSCAR_USUARIO_SQL = "select"
            + " id, nome, email, telefone, dataDeNascimento"
            + " from usuario"
            + " where id=?";
    
+    @Autowired
    DataSource dataSource;

-    public UsuarioDAO(DataSource dataSource) {
-        this.dataSource = dataSource;
-    }
    
    public Usuario gravarUsuario(Usuario u) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(CRIAR_USUARIO_SQL, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setDate(4, new java.sql.Date(u.getDataDeNascimento().getTime()));
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                u.setId(rs.getInt(1));
            }
        }
        return u;
    }

    public Usuario buscarUsuario(int id) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(BUSCAR_USUARIO_SQL)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                return u;
            }
        }
    }
}
```

- `PalpiteDAO.java`:
```diff
+package br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

-import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
-import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.stereotype.Component;

+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Palpite;
+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Usuario;

+@Component
public class PalpiteDAO {

    private final static String CRIAR_PALPITE_SQL = "insert into Palpite"
            + " (campeao, vice, palpiteiro)"
            + " values (?,?,?)";

    private final static String LISTAR_PALPITES_SQL = "select"
            + " p.id as palpiteId, p.campeao, p.vice,"
            + " u.id as usuarioId, u.nome, u.email, u.telefone, u.dataDeNascimento"
            + " from Palpite p inner join Usuario u on p.palpiteiro = u.id";

    private final static String LISTAR_PALPITES_POR_SELECAO_SQL = "select"
            + " p.id as palpiteId, p.campeao, p.vice,"
            + " u.id as usuarioId, u.nome, u.email, u.telefone, u.dataDeNascimento"
            + " from Palpite p inner join Usuario u on p.palpiteiro = u.id"
            + " where campeao = ? or vice = ?";

+    @Autowired
    DataSource dataSource;

-    public PalpiteDAO(DataSource dataSource) {
-        this.dataSource = dataSource;
-    }

    public Palpite gravarPalpite(Palpite p) throws SQLException, NamingException {
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(CRIAR_PALPITE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, p.getCampeao());
            ps.setString(2, p.getVice());
            ps.setInt(3, p.getPalpiteiro().getId());
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    public List<Palpite> listarTodosPalpites() throws SQLException, NamingException {
        List<Palpite> ret = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(LISTAR_PALPITES_SQL)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Palpite p = new Palpite();
                    Usuario u = new Usuario();
                    p.setId(rs.getInt("palpiteId"));
                    p.setCampeao(rs.getString("campeao"));
                    p.setVice(rs.getString("vice"));
                    u.setId(rs.getInt("usuarioId"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefone(rs.getString("telefone"));
                    u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                    p.setPalpiteiro(u);
                    ret.add(p);
                }
            }
        }
        return ret;
    }

    public List<Palpite> listarTodosPalpitesPorSelecao(String selecao) throws SQLException {
        List<Palpite> ret = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(LISTAR_PALPITES_POR_SELECAO_SQL)) {

            ps.setString(1, selecao);
            ps.setString(2, selecao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Palpite p = new Palpite();
                    Usuario u = new Usuario();
                    p.setId(rs.getInt("palpiteId"));
                    p.setCampeao(rs.getString("campeao"));
                    p.setVice(rs.getString("vice"));
                    u.setId(rs.getInt("usuarioId"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefone(rs.getString("telefone"));
                    u.setDataDeNascimento(new Date(rs.getDate("dataDeNascimento").getTime()));
                    p.setPalpiteiro(u);
                    ret.add(p);                }
            }
        }
        return ret;
    }
}
```

9. Vamos testar para ver se funcionou
10. Criar um controlador de teste e uma página simples

- Classe `src/main/java/br/ufscar/dc/latosensu/web/bolaodacopa1spring/controladores/TesteControlador.java`:
```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao.UsuarioDAO;
import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Usuario;

@Controller
public class TesteControlador {

    @Autowired
    UsuarioDAO usuarioDAO;

    @GetMapping
    public String teste(Model model) {
        Usuario u;
        try {
            u = usuarioDAO.buscarUsuario(1);
            model.addAttribute("mensagem",  "Deu:"+u.getNome());
        } catch (SQLException e) {
            model.addAttribute("mensagem", e.getMessage());
            e.printStackTrace();
        } catch (NamingException e) {
            model.addAttribute("mensagem", e.getMessage());
            e.printStackTrace();
        }
        return "teste";
    }
}
```

- Página `src/main/resources/templates/teste.html`:
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>Page Title</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
</head>
<body>
    <span th:text="${mensagem}">Mensagem</span>    
</body>
</html>
```

11. Testar e ver se retorna algum usuário corretamente
12. Se não tiver nenhum usuário inserido, trocar no controlador para inserir primeiro
13. Depois de testar, apagar o controlador e página de teste
14. Criar (copiando da [Demonstração 17](exemplo17.md)) a página `src/main/resources/templates/index.html`:

```diff
-<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
-<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
-<c:remove scope="session" var="novoPalpite" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />        
    </head>
    <body>
        <h1>Bem-vindo ao bolão da Copa 2010!</h1>
        <hr>
-        <c:if test="${!empty mensagem}">
-            ${mensagem}
-            <hr>
-        </c:if>        
+        <div th:if="${!#strings.isEmpty(mensagem)}">
+            <span th:text="${mensagem}">Mensagem</span>
+            <hr/>
+        </div>
        <p>Este é o bolão da Copa<p>
        <p>Escolha o que deseja fazer:</p>
-        <a href="palpiteForm.jsp">Fazer um palpite</a><br/>
-        <a href="VerPalpitesServlet">Ver todos os palpites</a><br/>
-        <a href="VerPalpitesServlet?selecao=Brasil">Ver todos os palpites envolvendo o Brasil</a><br/>    
+        <a th:href="@{/palpiteForm}">Fazer um palpite</a><br/>
+        <a th:href="@{/verPalpites}">Ver todos os palpites</a><br/>
+        <a th:href="@{/verPalpites?selecao=Brasil}">Ver todos os palpites envolvendo o Brasil</a><br/>    
    </body>
</html>
```

15. Copiar o arquivo `estilo.css` da [Demonstração 17](exemplo17.md)) para o diretório `src/main/resources/static/estilo.css`, mas alterando o elemento de erro:

```diff
body { background-color: #99EF99;}
h1 { font-style: italic;}
p { color: maroon; }
a {color: black; font-variant: small-caps; font-size: 1.5em;}
table { border-collapse:collapse; }
table, th, td { border: 1px solid black; }
th { background-color:green; color:yellow; }
td { text-align: center; }
td.esquerda { text-align: left; }
-ul.erro { color: red; }
+span.erro { color: red; }
```

16. Vamos agora fazer o formulário. Para isso vamos precisar da validação customizada

17. Criar o arquivo `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/forms/validators/FormatoData.java`

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FormatoDataValidator.class})
public @interface FormatoData {

    String message() default "Data deve seguir formato dd/mm/aaaa";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

18. Criar o arquivo `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/forms/validators/FormatoDataValidator.java`

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FormatoDataValidator implements ConstraintValidator<FormatoData, String> {
    public boolean isValid(String data, ConstraintValidatorContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
```
19. Criar o arquivo `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/forms/validators/CampeaoEViceDiferentes.java`

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CampeaoEViceDiferentesValidator.class})
public @interface CampeaoEViceDiferentes {

    String message() default "Campeão e vice devem ser diferentes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

20. Criar o arquivo `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/forms/validators/CampeaoEViceDiferentesValidator.java`

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.NovoPalpiteFormBean;

public class CampeaoEViceDiferentesValidator implements ConstraintValidator<CampeaoEViceDiferentes, NovoPalpiteFormBean> {
    public boolean isValid(NovoPalpiteFormBean npfb, ConstraintValidatorContext context) {
        return !npfb.getCampeao().equals(npfb.getVice());
    }
}
```

21. Copiar o arquivo `NovoPalpiteFormBean.java` da [Demonstração 17](exemplo17.md) para o diretório `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/forms`, alterando a validação para usar anotações:

```diff
-package br.ufscar.dc.latosensu.web.bolao1.forms;
+package br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms;

-import java.text.ParseException;
-import java.text.SimpleDateFormat;
-import java.util.ArrayList;
-import java.util.List;
+import javax.validation.constraints.Email;
+import javax.validation.constraints.NotNull;
+import javax.validation.constraints.Pattern;
+import javax.validation.constraints.Size;

+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators.CampeaoEViceDiferentes;
+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.validators.FormatoData;

+@CampeaoEViceDiferentes
public class NovoPalpiteFormBean {

+    @NotNull
+    @Size(min = 5, max = 64)
    private String nome;

+    @NotNull
+    @Email    
    private String email;

+    @Pattern(regexp = "\\(\\d{2}\\) (\\d{5}|\\d{4})-\\d{4}")
+   private String telefone;

+    @FormatoData
    private String dataDeNascimento;

+    @NotNull
+    @Size(min = 3, max = 64)
    private String campeao;

+    @NotNull
+    @Size(min = 3, max = 64)
    private String vice;

-    public List<String> validar() {
-        List<String> mensagens = new ArrayList<String>();
-        if (nome.trim().length() == 0) {
-            mensagens.add("Nome não pode ser vazio!");
-        }
-        if (!email.contains("@")) {
-            mensagens.add("E-mail está em formato incorreto!");
-        }
-        try {
-            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
-            sdf.parse(dataDeNascimento);
-        } catch (ParseException pe) {
-            mensagens.add("Data de nascimento deve estar no formato dd/mm/aaaa!");
-        }
-        if (campeao.equalsIgnoreCase(vice)) {
-            mensagens.add("Campeão e vice devem ser diferentes!");
-        }
-        return (mensagens.isEmpty() ? null : mensagens);
-    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCampeao() {
        return campeao;
    }

    public void setCampeao(String campeao) {
        this.campeao = campeao;
    }

    public String getVice() {
        return vice;
    }

    public void setVice(String vice) {
        this.vice = vice;
    }
}
```

22. Copiar o arquivo `palpiteForm.jsp` da [Demonstração 17](exemplo17.md) para o diretório `src/main/resources/templates/palpiteForm.html`, trocando a lógica de JSP/EL para ThymeLeaf:

```diff
--<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
--<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Bolão da Copa</title>
    <link rel="stylesheet" type="text/css" href="estilo.css" />
</head>

<body>
    <h1>Novo palpite</h1>
    <hr>
-    <c:if test="${!empty requestScope.mensagens}">
-        <ul class="erro">
-            <c:forEach items="${requestScope.mensagens}" var="mensagem">
-                <li>${mensagem}</li>
-            </c:forEach>
-        </ul>
-        <hr>
-    </c:if>
+    <div th:if="${#fields.hasErrors('novoPalpiteFormBean')}" >
+        <span class="erro" th:errors="${novoPalpiteFormBean}"></span><br />
+        <hr/>
+    </div>
-    <form action="NovoPalpiteServlet" method="post">
+    <form action="#" th:action="@{/novoPalpite}" th:object="${novoPalpiteFormBean}" method="post">
        Digite seus dados:<br />
-        Nome: <input name="nome" type="text" value="${sessionScope.novoPalpite.nome}" /><br />
+        Nome: <input id="nome" type="text" th:field="*{nome}" />
+        <span class="erro" th:if="${#fields.hasErrors('nome')}" th:errors="*{nome}"></span><br />
-        E-mail: <input name="email" type="text" value="${sessionScope.novoPalpite.email}" /><br />
+        E-mail: <input id="email" type="text" th:field="*{email}" />
+        <span class="erro" th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></span><br />
-        Telefone: <input name="telefone" type="text" value="${sessionScope.novoPalpite.telefone}" /><br />
+        Telefone: <input id="telefone" type="text" th:field="*{telefone}" />
+        <span class="erro" th:if="${#fields.hasErrors('telefone')}" th:errors="*{telefone}"></span><br />
-        Data de nascimento: <input name="dataDeNascimento" type="text" value="${sessionScope.novoPalpite.dataDeNascimento}" /><br />
+        Data de nascimento: <input id="dataDeNascimento" type="text" th:field="*{dataDeNascimento}" />
+        <span class="erro" th:if="${#fields.hasErrors('dataDeNascimento')}" th:errors="*{dataDeNascimento}"></span><br />
-        Campeão: <input name="campeao" type="text" value="${sessionScope.novoPalpite.campeao}" /><br />
+        Campeão: <input id="campeao" type="text" th:field="*{campeao}" />
+        <span class="erro" th:if="${#fields.hasErrors('campeao')}" th:errors="*{campeao}"></span><br />
-        Vice: <input name="vice" type="text" value="${sessionScope.novoPalpite.vice}" /><br />
+        Vice: <input id="vice" type="text" th:field="*{vice}" />
+        <span class="erro" th:if="${#fields.hasErrors('vice')}" th:errors="*{vice}"></span><br />
        <input type="submit" value="Enviar" />
    </form>
</body>

</html>
```

23. Criar o novo controlador `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/controladores/PalpiteControlador.java` (comparar com `NovoPalpiteServlet` da [Demonstração 17](exemplo17.md)):

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.NovoPalpiteFormBean;

@Controller
public class PalpiteControlador {

    @ModelAttribute
    NovoPalpiteFormBean getNovoPalpiteFormBean() {
        return new NovoPalpiteFormBean();
    }

    // Necessário para carregar o bean antes de mostrar a página
    @GetMapping(path = "/palpiteForm")
    public String abrirPalpiteForm() {
        return "palpiteForm";
    }

    @PostMapping(path = "/novoPalpite")
    public String novoPalpite(@Valid NovoPalpiteFormBean npfb, BindingResult result) {
        if (result.hasErrors()) {
            return "palpiteForm";
        } else {
            return "confirmarPalpite";
        }
    }
}
```

24. Testar e verificar a validação
25. Copiar a página `confirmarPalpite.jsp` da [Demonstração 17](exemplo17.md) para o diretório `src/main/resources/templates/confirmarPalpite.html`, trocando a lógica de JSP/EL para ThymeLeaf:

```diff
-<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da Copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Novo palpite</h1>
        Atenção! Deseja realmente enviar seu palpite?
        <br/>
        Uma vez enviado, você está automaticamente aceitando
        pagar R$ 20,00 de inscrição.
        <br/><br/>
-        Nome: ${sessionScope.novoPalpite.nome}<br/>
-        E-mail: ${sessionScope.novoPalpite.email}<br/>
-        Telefone: ${sessionScope.novoPalpite.telefone}<br/>
-        Data de nascimento: ${sessionScope.novoPalpite.dataDeNascimento}<br/>
-        Campeão: ${sessionScope.novoPalpite.campeao}<br/>
-        Vice: ${sessionScope.novoPalpite.vice}<br/>
+        Nome: [[${novoPalpiteFormBean.nome}]]<br/>
+        E-mail: [[${novoPalpiteFormBean.email}]]<br/>
+        Telefone: [[${novoPalpiteFormBean.telefone}]]<br/>
+        Data de nascimento: [[${novoPalpiteFormBean.dataDeNascimento}]]<br/>
+        Campeão: [[${novoPalpiteFormBean.campeao}]]<br/>
+        Vice: [[${novoPalpiteFormBean.vice}]]<br/>

        <br/>
-        <a href="GravarPalpiteServlet">Confirmar</a>
-        <a href="palpiteForm.jsp">Modificar</a>
-        <a href="index.jsp">Cancelar</a>
+        <a th:href="@{/gravarPalpite}">Confirmar</a>
+        <a th:href="@{/palpiteForm}">Modificar</a>
+        <a th:href="@{/}">Cancelar</a>
    </body>
</html>
```

26. Testar e observar que funciona, mas ao clicar em `Modificar` os valores são perdidos. Precisamos de uma sessão
27. Criar a classe `src/main/java/br/ufscar/latosensu/web/bolaodacopa1spring/controladores/Sessao.java`:

```java
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.NovoPalpiteFormBean;

@Component
@SessionScope
public class Sessao {
    NovoPalpiteFormBean npfb = new NovoPalpiteFormBean();

    NovoPalpiteFormBean getNovoPalpiteFormBean() {
        return npfb;
    }
}
```

28. Modificar o controlador para acessar o form que está na sessão, ao invés de inicializar um novo:

```diff
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores;

import javax.validation.Valid;

+import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.NovoPalpiteFormBean;

@Controller
public class PalpiteControlador {
+    @Autowired
+    Sessao sessao;

    @ModelAttribute
    NovoPalpiteFormBean getNovoPalpiteFormBean() {
-        return new NovoPalpiteFormBean();
+        return sessao.getNovoPalpiteFormBean();
    }

    @GetMapping(path = "/palpiteForm")
    public String abrirPalpiteForm() {
        return "palpiteForm";
    }

    @PostMapping(path = "/novoPalpite")
    public String novoPalpite(@Valid NovoPalpiteFormBean npfb, BindingResult result) {
        if (result.hasErrors()) {
            return "palpiteForm";
        } else {
            return "confirmarPalpite";
        }
    }
}
```

29. Testar e verificar que agora funciona, mas os dados ficam lá se você cancelar ou recarregar a página inicial (comparar com os passos 21-24 da [Demonstração 17](exemplo17.md)).
30. Vamos invalidar a sessão caso o usuário acesse a página inicial. Modificar o controlador `PalpiteControlador.java`, adicionando o seguinte método:

```java
    @GetMapping(path="/") 
    public String abrirSite(HttpSession session) {
        session.invalidate();
        return "index";
    }
```

31. Testar de novo e ver que agora os dados do palpite somem
32. Agora vamos concluir a gravação. Modificar o controlador `br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores.PalpiteControlador` para incluir um novo método (comparar com `GravarPalpiteServlet` da [Demonstração 17](exemplo17.md))

```diff
package br.ufscar.dc.latosensu.web.bolaodacopa1spring.controladores;

+import java.sql.SQLException;
+import java.text.ParseException;
+import java.text.SimpleDateFormat;
+import java.util.Date;

+import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
+import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao.PalpiteDAO;
+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.dao.UsuarioDAO;
import br.ufscar.dc.latosensu.web.bolaodacopa1spring.forms.NovoPalpiteFormBean;
+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Palpite;
+import br.ufscar.dc.latosensu.web.bolaodacopa1spring.modelo.Usuario;

@Controller
public class PalpiteControlador {
    @Autowired
    Sessao sessao;

+    @Autowired
+    UsuarioDAO usuarioDAO;

+    @Autowired
+    PalpiteDAO palpiteDAO;

    @ModelAttribute
    NovoPalpiteFormBean getNovoPalpiteFormBean() {
        return sessao.getNovoPalpiteFormBean();
    }

    @GetMapping(path = "/")
    public String abrirSite(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping(path = "/palpiteForm")
    public String abrirPalpiteForm() {
        return "palpiteForm";
    }

    @PostMapping(path = "/novoPalpite")
    public String novoPalpite(@Valid NovoPalpiteFormBean npfb, BindingResult result) {
        if (result.hasErrors()) {
            return "palpiteForm";
        } else {
            return "confirmarPalpite";
        }
    }

+    @GetMapping(path = "/gravarPalpite")
+    public String gravarPalpite(HttpSession session, Model model) throws ParseException, SQLException, NamingException {
+        NovoPalpiteFormBean npfb = sessao.getNovoPalpiteFormBean();
+        session.invalidate();

+        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
+        Date dataNascimento = sdf.parse(npfb.getDataDeNascimento());
+        Usuario u = new Usuario();
+        u.setNome(npfb.getNome());
+        u.setEmail(npfb.getEmail());
+        u.setTelefone(npfb.getTelefone());
+        u.setDataDeNascimento(dataNascimento);
+        u = usuarioDAO.gravarUsuario(u);
+        Palpite p = new Palpite();
+        p.setCampeao(npfb.getCampeao());
+        p.setVice(npfb.getVice());
+        p.setPalpiteiro(u);
+        p = palpiteDAO.gravarPalpite(p);

+        model.addAttribute("mensagem", "Obrigado pelo palpite!");
+        return "index";
+    }
}
``` 

33. Testar e ver que funciona
34. Agora vamos adicionar um tratamento de erro. Copiar o arquivo `erro.jsp` da [Demonstração 17](exemplo17.md) para a pasta `src/main/resources/templates/error.html`, modificando para suprimir a mensagem

```diff
-<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Opa, parece que ocorreu um problema</h1>
-        Descrição: ${mensagem}
    </body>
</html>

```

35. Para testar, é só desligar o banco de dados e tentar gravar o palpite
36. Agora vamos adicionar o visualizador de palpites. Adicionar o seguinte método ao controlador (comparar com `VerPalpitesServlet` da [Demonstração 17](exemplo17.md)):

```java
    @GetMapping(path = "/verPalpites")
    public String verPalpites(@RequestParam(required = false) String selecao, Model model) throws SQLException, NamingException {
        List<Palpite> todosPalpites = null;
        if (selecao == null) {
            todosPalpites = palpiteDAO.listarTodosPalpites();
        } else {
            todosPalpites = palpiteDAO.listarTodosPalpitesPorSelecao(selecao);
        }
        model.addAttribute("listaPalpites", todosPalpites);
        return "listaPalpites";
    }
```

37. Copiar a página `listaPalpites.jsp` da [Demonstração 17](exemplo17.md) para o diretório `src/main/resources/templates/listaPalpites.html`, modificando de JSP/EL para ThymeLeaf:

```diff
-<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
-<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Palpites da copa</h1>
        <hr>
-        <c:if test="${empty requestScope.listaPalpites}">
-            Não há palpites!
-        </c:if>
+        <span th:if="${#lists.isEmpty(listaPalpites)}">
+            Não há palpites!
+        </span>

-        <c:if test="${!empty requestScope.listaPalpites}">
-            <table>
+            <table th:unless="${#lists.isEmpty(listaPalpites)}">
                <tr>
                    <th class="esquerda">Usuário</th>
                    <th>Campeão</th>
                    <th>Vice</th>
                </tr>
-                <c:forEach items="${requestScope.listaPalpites}" var="palpite">
-                    <tr>
+                    <tr th:each="palpite:${listaPalpites}">
-                        <td class="esquerda">${palpite.palpiteiro.nome}</td>
-                        <td>${palpite.campeao}</td>
-                        <td>${palpite.vice}</td>
+                        <td class="esquerda">[[${palpite.palpiteiro.nome}]]</td>
+                        <td>[[${palpite.campeao}]]</td>
+                        <td>[[${palpite.vice}]]</td>
                    </tr>
-                </c:forEach>
            </table>
-        </c:if>
    </body>
</html>
```

38. Fim
