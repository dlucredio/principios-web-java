# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 2 - Exemplo de geração de conteúdo dinâmico + interativo

Neste exemplo veremos como conteúdo dinâmico e interativo pode ser produzido. A ideia é apenas conhecer os conceitos fundamentais. Este exemplo é didático e não se destina ao uso em produção.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Rodar o servidor da [demonstração anterior](exemplo1.md)
2. Criar, na pasta www, um arquivo HTML: teste.hml

```html
<html>
    <head>
      <title>Este é o título da página</title>
      <meta charset="UTF-8">
    </head>
    <body>
        <h1>Cadastro</h1>
        <hr>
        <p>Digite seus dados.</p>
        <form action="executar" method="GET">
              E-mail: <input name="email" type="text" /><br/>
              Senha: <input name="senha" type="password" /><br/>
              Sexo: <input type="radio" name="sexo" value="masculino" /> Masculino
              <input type="radio" name="sexo" value="feminino" /> Feminino
              <br/>
              Receber notícias:
              <input type="checkbox" name="cinema" value="cinema" />
              <br />
              Cadastrar meu e-mail no site:
              <input type="checkbox" name="cadastrar" value="cadastrar" />
              <br />
              <input type="submit" value="Enviar" />
        </form>
    </body>
</html>
```

3. Abrir no browser, e simular um cadastro. Mostrar a janela do servidor, o que foi enviado
4. Modificar o código do servidor, para tratar a requisição, e testar novamente

```diff
private void tratarRequisicao(BufferedReader input, DataOutputStream output) throws Exception {
  String tmp = input.readLine();
  if (tmp.startsWith("GET")) {
     StringTokenizer st = new StringTokenizer(tmp);
     st.nextToken();
     String arquivo = st.nextToken().substring(1);
     System.out.println("Requisição: " + arquivo);
+            if (arquivo.startsWith("executar")) {
+                String email = "";
+                String conteudoForm = arquivo.substring(9);
+                StringTokenizer st2 = new StringTokenizer(conteudoForm, "&");
+                while (st2.hasMoreTokens()) {
+                    String s = st2.nextToken();
+                    int i = s.indexOf('=');
+                    String nomeParam = s.substring(0, i);
+                    String valorParam = s.substring(i + 1);
+                    if (nomeParam.equals("email")) {
+                        email = valorParam;
+                    }
+                    System.out.println("INSERT INTO TABELA " + nomeParam + " = " + valorParam);
+                }
+                String resposta = "HTTP/1.0 200 OK\r\n";
+                resposta += "Connection: close\r\n";
+                resposta += "Server: Exemplo\r\n";
+                resposta += "Content-Type: text/html\r\n";
+                resposta += "\r\n";
+                resposta += "<html>";
+                resposta += "    <head>";
+                resposta += "      <title>Este é o título da página</title>";
+                resposta += "      <meta charset=\"UTF-8\">";
+                resposta += "    </head>";
+                resposta += "    <body>";
+                resposta += "        <h1>Cadastro</h1>";
+                resposta += "        <hr>";
+                resposta += "        <p>Cadastro do e-mail " + email + " bem-sucedido!</p>";
+                resposta += "    </body>";
+                resposta += "</html>";
+                output.writeBytes(resposta);
+            } else {
+                // Continua a partir da linha a seguir, sem modificar
                System.out.println("Requisição: " + arquivo);
                ...
+            }
     ...
```

5. Mudar para gerar conteúdo interativo

```java
                resposta += "    <body>";
                resposta += "        <h1>Cadastro</h1>";
                resposta += "        <hr>";
                resposta += "        <div id=\"resultado\">Cadastro do e-mail " + email + " bem-sucedido!</div>";
                resposta += "        <button onclick=\"document.getElementById('resultado').style.display='none';\">Dispensar</button>";
                resposta += "    </body>";
```

6. Mudar o método do formulário para ```POST``` (no arquivo ```teste.html```) e testar
7. Modificar o servidor GatoTom para tratar requisições ```POST```

```java
    private void tratarRequisicao(BufferedReader input, DataOutputStream output) throws Exception {
        String tmp = input.readLine();
        if (tmp != null && tmp.startsWith("POST")) {
            int tamanho = 0;
            while (true) {
                System.out.println(tmp);
                tmp = input.readLine();
                // if(tmp.startsWith("Content-Length:")) {
                //     tamanho = Integer.parseInt(tmp.substring(16));
                // }
                if (tmp.trim().isEmpty()) {
                    //    System.out.println("Início conteúdo POST ("+tamanho+" bytes):");
                    //    for(int i=0;i<tamanho;i++) {
                    //        int b = input.read();
                    //        System.out.print((char) b);
                    //    }
                    //    System.out.println("\nFim conteúdo POST");
                    return;
                }
            }
        }
        else if (tmp != null && tmp.startsWith("GET")) {
        ...
```

8. Testar no browser
9. Descomentar as linhas acima, e testar
10. Fim
