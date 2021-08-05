<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Bolão da Copa</title>
            <link rel="stylesheet" type="text/css" href="estilo.css" />
        </head>

        <body>
            <h1>Novo palpite</h1>
            <hr>
            <c:if test="${!empty requestScope.mensagens}">
                <ul class="erro">
                    <c:forEach items="${requestScope.mensagens}" var="mensagem">
                        <li>${mensagem}</li>
                    </c:forEach>
                </ul>
                <hr>
            </c:if>
            <form action="NovoPalpiteServlet" method="post">
                Digite seus dados:<br />
                Nome: <input name="nome" type="text" value="${sessionScope.novoPalpite.nome}" /><br />
                E-mail: <input name="email" type="text" value="${sessionScope.novoPalpite.email}" /><br />
                Telefone: <input name="telefone" type="text" value="${sessionScope.novoPalpite.telefone}" /><br />
                Data de nascimento: <input name="dataDeNascimento" type="text"
                    value="${sessionScope.novoPalpite.dataDeNascimento}" /><br />
                Campeão: <input name="campeao" type="text" value="${sessionScope.novoPalpite.campeao}" /><br />
                Vice: <input name="vice" type="text" value="${sessionScope.novoPalpite.vice}" /><br />
                <input type="submit" value="Enviar" />
            </form>
        </body>

        </html>