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
