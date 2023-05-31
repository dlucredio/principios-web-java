# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 17 - Uma aplicação completa com JSP + Servlets

Este exemplo mostra como desenvolver uma aplicação completa utilizando JSPs e Servlets

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
- Apache Derby 10.15.2.0

<hr>

1. Criar um novo projeto Web: ```BolaoDaCopa1```

```sh
mvn archetype:generate "-DarchetypeGroupId=org.apache.maven.archetypes" "-DarchetypeArtifactId=maven-archetype-webapp" "-DarchetypeVersion=1.4"

Define value for property 'groupId': br.ufscar.dc.latosensu.web.bolao1
Define value for property 'artifactId': BolaoDaCopa1
Define value for property 'version' 1.0-SNAPSHOT: : 
Define value for property 'package' br.ufscar.dc.latosensu.web.bolao1: : 
Confirm properties configuration:
groupId: br.ufscar.dc.latosensu.web.bolao1
artifactId: BolaoDaCopa1
version: 1.0-SNAPSHOT
package: br.ufscar.dc.latosensu.web.bolao1

```

2. Iniciar o banco de dados (Apache Derby)

- Abrir um terminal
- Navegar até uma pasta onde será criado o banco de dados, por exemplo:

```sh
cd /Users/daniellucredio/DerbyDatabases
```

- Caso não tenha sido configurada ainda, exportar a variável JAVA_HOME, que deve apontar para a instalação do JDK. Por exemplo, no MacOS, é algo parecido com o seguinte:

```sh
export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-16.jdk/Contents/Home
```

- Executar o servidor. Ao executar nessa pasta, o banco será criado dentro dela. (Obs: neste exemplo, o Derby foi instalado em ```~/Programs/db-derby-10.15.2.0-bin```. Substitua pela instalação local)

```sh
~/Programs/db-derby-10.15.2.0-bin/bin/startNetworkServer
```

3. No projeto Maven, criar o arquivo ```src/main/sql/database.sql```:

```sql
CONNECT 'jdbc:derby://localhost:1527/bolao1;create=true' user 'bolao1';
CALL SYSCS_UTIL.SYSCS_CREATE_USER('bolao1', 'senha1'); 

create table Usuario
(
id integer not null generated always as identity (start with 1, increment by 1),
nome varchar(256) not null,
email varchar(256) not null,
telefone varchar(24) not null,
dataDeNascimento date,
CONSTRAINT primary_key PRIMARY KEY (id)
);

create table Palpite
(
id integer not null generated always as identity (start with 1, increment by 1),
campeao varchar(256) not null,
vice varchar(256) not null,
palpiteiro integer references Usuario (id)
);
```

4. Executar o comando de criação do banco de dados (Obs: neste exemplo, o Derby foi instalado em ```~/Programs/db-derby-10.15.2.0-bin```. Substitua pela instalação local):

```sh
~/Programs/db-derby-10.15.2.0-bin/bin/ij src/main/sql/database.sql
```

5. Configurar o Tomcat para acessar o banco de dados

- Abrir o arquivo ```server.xml``` do Tomcat, e adicionar o seguinte trecho:

```diff
    ...
    <GlobalNamingResources>
        ...
+        <Resource name="jdbc/bolao1" auth="Container" type="javax.sql.DataSource"
+                    maxActive="100" maxIdle="30" maxWait="10000"
+                    username="bolao1" password="senha1"
+                    driverClassName="org.apache.derby.jdbc.ClientDriver"
+                    url="jdbc:derby://localhost:1527/bolao1" />
    </GlobalNamingResources>
```

- Copiar todos os arquivos ```*.jar``` da pasta ```lib``` da instalação do Derby para a pasta ```lib``` do Tomcat

6. Configure o projeto Maven

- No projeto criado, no arquivo ```pom.xml```, adicione uma dependência para a API de servlets, Apache Commons BeanUtils e JSTL

```diff
    ...
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
+        <dependency>
+            <groupId>jakarta.servlet</groupId>
+            <artifactId>jakarta.servlet-api</artifactId>
+            <version>5.0.0</version>
+        </dependency>
+        <dependency>
+            <groupId>commons-beanutils</groupId>
+            <artifactId>commons-beanutils</artifactId>
+            <version>1.9.4</version>
+        </dependency>
+        <dependency>
+            <groupId>org.glassfish.web</groupId>
+            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
+            <version>2.0.0</version>
+        </dependency>
    </dependencies>
    ...
```

- Crie o arquivo ```src/main/webapp/META-INF/context.xml```:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context path="/BolaoDaCopa1">
  <ResourceLink global="jdbc/bolao1" name="jdbc/bolao1local" type="javax.sql.DataSource"/>
</Context>
```

- Modifique o arquivo ```src/main/webapp/WEB-INF/web.xml```:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
  <display-name>Archetype Created Web Application</display-name>
</web-app>
```
- Crie a pasta ```src/main/java/br/ufscar/dc/latosensu/web/bolao1```

7. Criar os Value Objects

- ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/beans/Usuario.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.beans;

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

- ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/beans/Palpite.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.beans;

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

8. Criar os Data Access Objects

- ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/dao/UsuarioDAO.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;

public class UsuarioDAO {

    private final static String CRIAR_USUARIO_SQL = "insert into Usuario"
            + " (nome, email, telefone, dataDeNascimento)"
            + " values (?,?,?,?)";

    private final static String BUSCAR_USUARIO_SQL = "select"
            + " id, nome, email, telefone, dataDeNascimento"
            + " from usuario"
            + " where id=?";
    
    DataSource dataSource;

    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
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

- ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/dao/PalpiteDAO.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.dao;

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

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;

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

    DataSource dataSource;

    public PalpiteDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

9. Modificar o arquivo ```src/main/webapp/index.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />        
    </head>
    <body>
        <h1>Bem-vindo ao bolão da Copa 2010!</h1>
        <hr>
        <p>Este é o bolão da Copa<p>
        <p>Escolha o que deseja fazer:</p>
        <a href="#">Fazer um palpite</a><br/>
        <a href="#">Ver todos os palpites</a><br/>
        <a href="#">Ver todos os palpites envolvendo o Brasil</a><br/>
    </body>
</html>
```

10. Criar o arquivo ```src/main/webapp/estilo.css```:

```css
body { background-color: #99EF99;}
h1 { font-style: italic;}
p { color: maroon; }
a {color: black; font-variant: small-caps; font-size: 1.5em;}
table { border-collapse:collapse; }
table, th, td { border: 1px solid black; }
th { background-color:green; color:yellow; }
td { text-align: center; }
td.esquerda { text-align: left; }
ul.erro { color: red; }
```

11. Testar e mostrar o que já tem
12. Modificar o arquivo ```index.jsp```, para adicionar o link para fazer um palpite

```diff
        <p>Escolha o que deseja fazer:</p>
+        <a href="palpiteForm.jsp">Fazer um palpite</a><br/>
        <a href="#">Ver todos os palpites</a><br/>
        <a href="#">Ver todos os palpites envolvendo o Brasil</a><br/>
```

13. Criar o arquivo ```src/main/webapp/palpiteForm.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da Copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Novo palpite</h1>
        <hr>

        <form action="NovoPalpiteServlet" method="post">
            Digite seus dados:<br/>
            Nome: <input name="nome" type="text" value="" /><br/>
            E-mail: <input name="email" type="text" value="" /><br/>
            Telefone: <input name="telefone" type="text" value="" /><br/>
            Data de nascimento: <input name="dataDeNascimento" type="text" value="" /><br/>
            Campeão: <input name="campeao" type="text" value="" /><br/>
            Vice: <input name="vice" type="text" value="" /><br/>
            <input type="submit" value="Enviar"/>
        </form>
    </body>
</html>
```

14. Criar o bean do formulário

- Arquivo ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/forms/NovoPalpiteFormBean.java```:

```java
package br.ufscar.dc.latosensu.web.bolao1.forms;

public class NovoPalpiteFormBean {
    private String nome;
    private String email;
    private String telefone;
    private String dataDeNascimento;
    private String campeao;
    private String vice;

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

15. Criar o controlador ```src/main/java/br/ufscar/dc/latosensu/web/bolao1/servlets/NovoPalpiteServlet.java```:

```java
package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;

import org.apache.commons.beanutils.BeanUtils;

import br.ufscar.dc.latosensu.web.bolao1.forms.NovoPalpiteFormBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/NovoPalpiteServlet"})
public class NovoPalpiteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        NovoPalpiteFormBean npfb = new NovoPalpiteFormBean();
        try {
            // Obs: BeanUtils é uma classe da biblioteca
            // Apache Commons BeanUtils
            // http://commons.apache.org/beanutils/
            BeanUtils.populate(npfb, req.getParameterMap());
            req.getSession().setAttribute("novoPalpite", npfb);
            req.getRequestDispatcher("confirmarPalpite.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
```

16. Criar página ```src/main/webapp/erro.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Opa, parece que ocorreu um problema</h1>
        Descrição: ${mensagem}
    </body>
</html>
```

17. Testar para ver o erro, modificar o servlet (depois não esqueça de consertar):

```diff
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        NovoPalpiteFormBean npfb = new NovoPalpiteFormBean();
        try {
            // Obs: BeanUtils é uma classe da biblioteca
            // Apache Commons BeanUtils
            // http://commons.apache.org/beanutils/
            BeanUtils.populate(npfb, req.getParameterMap());
+            int a = 0;
+            int b = 2/a;
            req.getSession().setAttribute("novoPalpite", npfb);
            req.getRequestDispatcher("confirmarPalpite.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
```
18. Criar página ```src/main/webapp/confirmarPalpite.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        Nome: ${sessionScope.novoPalpite.nome}<br/>
        E-mail: ${sessionScope.novoPalpite.email}<br/>
        Telefone: ${sessionScope.novoPalpite.telefone}<br/>
        Data de nascimento: ${sessionScope.novoPalpite.dataDeNascimento}<br/>
        Campeão: ${sessionScope.novoPalpite.campeao}<br/>
        Vice: ${sessionScope.novoPalpite.vice}<br/>
        <br/>
        <a href="GravarPalpiteServlet">Confirmar</a>
        <a href="palpiteForm.jsp">Modificar</a>
        <a href="index.jsp">Cancelar</a>
    </body>
</html>
```

19. Mostrar rodando. Testar a opção "Modificar" e mostrar que não aparecem os campos
20. Para mostrar, alterar ```palpiteForm.jsp```

```jsp
            Nome: <input name="nome" type="text" value="${sessionScope.novoPalpite.nome}" /><br/>
            E-mail: <input name="email" type="text" value="${sessionScope.novoPalpite.email}" /><br/>
            Telefone: <input name="telefone" type="text" value="${sessionScope.novoPalpite.telefone}" /><br/>
            Data de nascimento: <input name="dataDeNascimento" type="text" value="${sessionScope.novoPalpite.dataDeNascimento}" /><br/>
            Campeão: <input name="campeao" type="text" value="${sessionScope.novoPalpite.campeao}" /><br/>
            Vice: <input name="vice" type="text" value="${sessionScope.novoPalpite.vice}" /><br/>
```

21. Alterar também ```index.jsp```

```diff
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
+<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
+<c:remove scope="session" var="novoPalpite" />
<html>
...
```

22. Testar novamente e mostrar que ao clicar em "Modificar", os campos agora aparecem preenchidos. E também, ao abrir a aplicação em ```index.jsp```, os campos não são preenchidos com o último palpite.
23. Agora vamos fazer a validação
24. Modificar a classe ```NovoPalpiteFormBean```, incluindo um método de validação

```java
    public List<String> validar() {
        List<String> mensagens = new ArrayList<String>();
        if (nome.trim().length() == 0) {
            mensagens.add("Nome não pode ser vazio!");
        }
        if (!email.contains("@")) {
            mensagens.add("E-mail está em formato incorreto!");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.parse(dataDeNascimento);
        } catch (ParseException pe) {
            mensagens.add("Data de nascimento deve estar no formato dd/mm/aaaa!");
        }
        if (campeao.equalsIgnoreCase(vice)) {
            mensagens.add("Campeão e vice devem ser diferentes!");
        }
        return (mensagens.isEmpty() ? null : mensagens);
    }
```
25. Modificar o controlador ```NovoPalpiteServlet```, para incluir a lógica de validação

```diff
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        NovoPalpiteFormBean npfb = new NovoPalpiteFormBean();
        try {
            // Obs: BeanUtils é uma classe da biblioteca
            // Apache Commons BeanUtils
            // http://commons.apache.org/beanutils/
            BeanUtils.populate(npfb, req.getParameterMap());
            req.getSession().setAttribute("novoPalpite", npfb);
-            req.getRequestDispatcher("confirmarPalpite.jsp").forward(req, resp);
+            List<String> mensagens = npfb.validar();
+            if (mensagens == null) {
+                req.getRequestDispatcher("confirmarPalpite.jsp").forward(req, resp);
+            } else {
+                req.setAttribute("mensagens", mensagens);
+                req.getRequestDispatcher("palpiteForm.jsp").forward(req, resp);
+            }
        } catch (Exception e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
```

26. Modificar ```palpiteForm.jsp```

```diff
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
+<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da Copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Novo palpite</h1>
        <hr>
+        <c:if test="${!empty requestScope.mensagens}">
+            <ul class="erro">
+            <c:forEach items="${requestScope.mensagens}" var="mensagem">
+                <li>${mensagem}</li>
+            </c:forEach>
+            </ul>
+            <hr>
+        </c:if>

        <form action="NovoPalpiteServlet" method="post">
        ...
```

27. Testar a validação
28. Criar o controlador ```src/main/java/br/dc/ufscar/latosensu/web/bolao1/servlets/GravarPalpiteServlet.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.beans.Usuario;
import br.ufscar.dc.latosensu.web.bolao1.dao.PalpiteDAO;
import br.ufscar.dc.latosensu.web.bolao1.dao.UsuarioDAO;
import br.ufscar.dc.latosensu.web.bolao1.forms.NovoPalpiteFormBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/GravarPalpiteServlet" })
public class GravarPalpiteServlet extends HttpServlet {
    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/bolao1local"); // Irá recuperar conforme context.xml
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NovoPalpiteFormBean npfb = (NovoPalpiteFormBean) req.getSession().getAttribute("novoPalpite");
        req.getSession().removeAttribute("novoPalpite");

        UsuarioDAO udao = new UsuarioDAO(dataSource);
        PalpiteDAO pdao = new PalpiteDAO(dataSource);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNascimento = null;
        try {
            dataNascimento = sdf.parse(npfb.getDataDeNascimento());
        } catch (ParseException e) {
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
        try {
            Usuario u = new Usuario();
            u.setNome(npfb.getNome());
            u.setEmail(npfb.getEmail());
            u.setTelefone(npfb.getTelefone());
            u.setDataDeNascimento(dataNascimento);
            u = udao.gravarUsuario(u);
            Palpite p = new Palpite();
            p.setCampeao(npfb.getCampeao());
            p.setVice(npfb.getVice());
            p.setPalpiteiro(u);
            p = pdao.gravarPalpite(p);
            req.setAttribute("mensagem", "Obrigado pelo palpite!");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
```
29. Modificar o arquivo ```index.jsp```, para incluir a mensagem

```diff
    <body>
        <h1>Bem-vindo ao bolão da Copa 2010!</h1>
        <hr>
+        <c:if test="${!empty mensagem}">
+            ${mensagem}
+            <hr>
+        </c:if>
        <p>Este é o bolão da Copa<p>
```

30. Testar a aplicação
31. Criar controlador ```src/main/java/br/dc/ufscar/latosensu/web/bolao1/servlets/VerPalpitesServlet.java```

```java
package br.ufscar.dc.latosensu.web.bolao1.servlets;

import java.io.IOException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ufscar.dc.latosensu.web.bolao1.beans.Palpite;
import br.ufscar.dc.latosensu.web.bolao1.dao.PalpiteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/VerPalpitesServlet" })
public class VerPalpitesServlet extends HttpServlet {
    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/bolao1local");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PalpiteDAO pdao = new PalpiteDAO(dataSource);
        String selecao = req.getParameter("selecao");
        List<Palpite> todosPalpites = null;
        try {
            if (selecao == null) {
                todosPalpites = pdao.listarTodosPalpites();
            } else {
                todosPalpites = pdao.listarTodosPalpitesPorSelecao(selecao);
            }
            req.setAttribute("listaPalpites", todosPalpites);
            req.getRequestDispatcher("listaPalpites.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensagem", e.getLocalizedMessage());
            req.getRequestDispatcher("erro.jsp").forward(req, resp);
        }
    }
}
```

32. Criar página ```src/main/webapp/listaPalpites.jsp```

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />
    </head>
    <body>
        <h1>Palpites da copa</h1>
        <hr>
        <c:if test="${empty requestScope.listaPalpites}">
            Não há palpites!
        </c:if>
        <c:if test="${!empty requestScope.listaPalpites}">
            <table>
                <tr>
                    <th class="esquerda">Usuário</th>
                    <th>Campeão</th>
                    <th>Vice</th>
                </tr>
                <c:forEach items="${requestScope.listaPalpites}" var="palpite">
                    <tr>
                        <td class="esquerda">${palpite.palpiteiro.nome}</td>
                        <td>${palpite.campeao}</td>
                        <td>${palpite.vice}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>
```

33. Modificar o arquivo ```index.jsp``` para incluir os links

```diff
        <p>Escolha o que deseja fazer:</p>
        <a href="palpiteForm.jsp">Fazer um palpite</a><br/>
+        <a href="VerPalpitesServlet">Ver todos os palpites</a><br/>
+        <a href="VerPalpitesServlet?selecao=Brasil">Ver todos os palpites envolvendo o Brasil</a><br/>
```

34. Testar
35. Fim


<hr>
Exercícios de fixação (em ordem crescente de dificuldade)


a) Adicionar uma opção para que o usuário possa pesquisar palpites por qualquer seleção (e não apenas o Brasil)

b) Adicionar uma opção para mostrar todos os palpites de um determinado usuário

c) Modificar a aplicação para que só seja possível adicionar seleções de uma lista pré-definida (que está armazenada no banco de dados)

d) Adicionar mais duas opções ao palpite (terceiro e quarto lugares, além de campeão e vice). Em seguida, adicionar uma função onde é informado o resultado da Copa, e a aplicação mostra quem ganhou o bolão, com base em alguma fórmula de sua escolha

e) Adicionar login e senha para uso do sistema, com papeis específicos aos usuários, como: usuário comum só pode fazer palpites, administrador pode acessar as demais funções
