# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 1 -  HTTP do lado do cliente e servidor
Neste exemplo veremos como pode ser implementado um servidor web simples, utilizando Java. A ideia é apenas conhecer os conceitos fundamentais. Este exemplo é didático e não se destina ao uso em produção.
<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Abrir um terminal que tenha instalado `openssl` e digitar:

```sh
openssl s_client -connect dlucredio.com:443
... (vai aparecer as credenciais SSL)
GET / HTTP/1.1
Host: dlucredio.com
(dar dois enters - não pode demorar muito)
...
```

2. Criar um novo projeto Java no Apache Maven, chamado ```GatoTom```

```sh
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4

Define value for property 'groupId': br.ufscar.dc.latosensu.web
Define value for property 'artifactId': GatoTom
Define value for property 'version' 1.0-SNAPSHOT: : 
Define value for property 'package' br.ufscar.dc.latosensu.web: : 
Confirm properties configuration:
groupId: br.ufscar.dc.latosensu.web
artifactId: GatoTom
version: 1.0-SNAPSHOT
package: br.ufscar.dc.latosensu.web
```

3. Criar uma classe chamada ```GatoTom```

```java
package br.ufscar.dc.latosensu.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class GatoTom extends Thread {
    private static String PASTA_WWW = "/Users/daniellucredio/Desktop/www";

    public void run() {
        try (ServerSocket serversocket = new ServerSocket(8080)) {
            System.out.println("Aguardando requisições");
            while (true) {
                Socket connectionsocket = serversocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(connectionsocket.getInputStream()));
                DataOutputStream output = new DataOutputStream(connectionsocket.getOutputStream());
                tratarRequisicao(input, output);
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tratarRequisicao(BufferedReader input, DataOutputStream output) throws Exception {
        String tmp = input.readLine();
        if (tmp != null && tmp.startsWith("GET")) {
            StringTokenizer st = new StringTokenizer(tmp);
            st.nextToken();
            String arquivo = st.nextToken().substring(1);
            System.out.println("Requisição: " + arquivo);
            File f = new File(PASTA_WWW + File.separator + arquivo);
            if (!f.exists() || !f.isFile()) {
                String resposta = "HTTP/1.0 404 Not Found\r\n";
                resposta += "Connection: close\r\n";
                resposta += "Server: Exemplo\r\n";
                resposta += "\r\n";
                output.writeBytes(resposta);
                System.out.println("Arquivo não existe");
                return;
            }
            try (FileInputStream fis = new FileInputStream(f)) {
                String resposta = "HTTP/1.0 200 OK\r\n";
                resposta += "Connection: close\r\n";
                resposta += "Server: Exemplo\r\n";
                if (arquivo.endsWith(".html")) {
                    resposta += "Content-Type: text/html\r\n";
                } else if (arquivo.endsWith(".jpg")) {
                    resposta += "Content-Type: image/jpeg\r\n";
                } else if (arquivo.endsWith(".css")) {
                    resposta += "Content-Type: text/css\r\n";
                }
                resposta += "\r\n";
                output.writeBytes(resposta);
                while (true) {
                    int b = fis.read();
                    if (b == -1)
                        break;
                    output.write(b);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GatoTom().start();
    }
}
```

4. Compilar e rodar

```sh
mvn compile exec:java -D"exec.mainClass=br.ufscar.dc.latosensu.web.GatoTom"
```

5. Mostrar a pasta ```www```, criar um arquivo ```teste.html``` com conteúdo textual qualquer dentro, testar no browser
6. Criar uma imagem ```.jpg``` qualquer, salvar na pasta ```www``` com o nome ```imagem.jpg```, e testar no browser
7. Fim
