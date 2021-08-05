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
