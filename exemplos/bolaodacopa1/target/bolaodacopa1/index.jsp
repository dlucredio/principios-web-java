<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:remove scope="session" var="novoPalpite" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolão da copa</title>
        <link rel="stylesheet" type="text/css" href="estilo.css" />        
    </head>
    <body>
        <h1>Bem-vindo ao bolão da Copa 2010!</h1>
        <hr>
        <c:if test="${!empty mensagem}">
            ${mensagem}
            <hr>
        </c:if>        
        <p>Este é o bolão da Copa<p>
        <p>Escolha o que deseja fazer:</p>
        <a href="palpiteForm.jsp">Fazer um palpite</a><br/>
        <a href="VerPalpitesServlet">Ver todos os palpites</a><br/>
        <a href="VerPalpitesServlet?selecao=Brasil">Ver todos os palpites envolvendo o Brasil</a><br/>    
    </body>
</html>
