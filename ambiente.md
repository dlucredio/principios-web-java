# Princípios de programação para Web utilizando Java
## Prof. Daniel Lucrédio

### Configurando o ambiente

Para conseguir rodar os [exemplos](exemplos) deste curso, são necessárias as seguintes instalações:

- Java Software Development Kit. Recomendado: [OpenJDK versão 14 ou superior](https://adoptopenjdk.net/)
- [Apache Maven v3.6 ou superior](https://maven.apache.org/)
- Um editor/IDE. Recomendados:
    - [Visual Studio Code](https://code.visualstudio.com/) + [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) + [Tomcat for Java](https://marketplace.visualstudio.com/items?itemName=adashen.vscode-tomcat) + [Spring Boot Tools](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-spring-boot) + [Spring Initializr](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr) + [Spring Boot Dashboard](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-boot-dashboard) + [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack)
    - [IntelliJ](https://www.jetbrains.com/idea/)
    - [Eclipse](https://www.eclipse.org/)
    - [Apache NetBeans](https://netbeans.apache.org/)
- [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/current/reference/html/cli.html) (opcional)
- Um SGBD. Recomendado:
    - [Apache Derby](http://db.apache.org/derby/)
        - Basta descompactar e executar. É preciso ter a variável JAVA_HOME configurada:
            - No MacOS: ```export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-16.jdk/Contents/Home```

Esses vídeos explicam de um jeito bem fácil como configurar um ambiente como o que vamos precisar:

- [Java + Tomcat no VSCode](https://youtu.be/23rN0oDdOKg)
- [Spring Boot no VSCode](https://youtu.be/dkmlOi_MNb4)