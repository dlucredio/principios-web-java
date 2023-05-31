# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Demonstração 3 - Instalando e rodando o Tomcat pela primeira vez

Neste exemplo veremos como instalar e rodar o Apache Tomcat.

<hr>
Exemplo verificado no seguinte ambiente:

- Visual Studio Code - 1.56.2
- openjdk version "16.0.1" 2021-04-20
- Apache Tomcat 10.0.6
- Apache Maven 3.6.3
<hr>

1. Baixar o [Tomcat](http://tomcat.apache.org/) e mostrar o arquivo baixado
2. Descompactar em uma pasta sem espaços ou acentos
3. Mostrar a estrutura de diretórios
4. Mostrar ```server.xml```
5. Mostrar ```tomcat-users.xml```
6. Rodar o tomcat
   - No windows
      - Abrir o Windows PowerShell
      - Rodar
      
      ```sh
      $env:JAVA_HOME="C:\<caminho_para_java>\jreXXX"
      $env:CATALINA_HOME="C:\<caminho_para_tomcat>\apache-tomcat-10.1.9"
      ```
      - Executar ```startup.bat``` para iniciar e ```shutdown.bat``` para encerrar
   - No Linux/Mac
      - Abrir um terminal
      - Rodar ```echo $JAVA_HOME``` ou ```export JAVA_HOME="/<caminho_para_java>/jreXXX"```
      - Executar ```startup.sh``` para iniciar e ```shutdown.sh``` para encerrar (pode ser necessário dar permissão antes)
7. Abrir localhost:8080
   - Mostrar que não tem acesso no manager
   - Modificar ```tomcat-users.xml```, adicionando a seguinte linha

```xml
<user username="admin" password="admin" roles="manager-gui" />
```

   - Parar tomcat e reiniciar, e tentar novamente
8. Mostrar aplicações ```manager``` e ```status```
9. Fim

