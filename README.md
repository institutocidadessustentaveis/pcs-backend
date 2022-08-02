# Programa Cidades Sustentáveis
Código-fonte do projeto do [Programa Cidades Sustentáveis](https://www.cidadessustentaveis.org.br/inicial/home).

## Principais tecnologias e documentações de apoio

- [Java](https://docs.oracle.com/en/java/) v8;
- [Maven](https://maven.apache.org/guides/);
- [Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/) v2.1.3;
- [PostgreSQL](https://www.postgresql.org/docs/) v11;
- [PostGIS](https://postgis.net/documentation/) v3.0.1;
- [Sprint Tools 4 For Eclipse](https://spring.io/tools);

## Ferramentas necessárias para execução do projeto e instruções de instalação:

- [Git](https://git-scm.com/downloads);
- [Java](https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html) v8;
- [Maven](https://maven.apache.org/install.html);
- [PostgreSQL](https://www.postgresql.org/download/) v11;
- [PostGIS](https://postgis.net/install/) v3.0.1;

## Como clonar o projeto

Com o Git, Java, Maven, PostgreSQL e PostGIS instalados, acesso o terminal e execute o comando:

`git clone https://github.com/programacidadessustentaveis/pcs-backend.git`

Após a clonagem, através do próprio terminal, acesse o diretório com o código-fonte, e execute o seguinte comando para instalar as depedências e bibliotecas do projeto:

`mvn install`

Caso esteja utilizando uma IDE, a própria ferramenta irá instalar as dependências do projeto.

## Como executar o projeto

Após a instalação das dependências e bibliotecas do projeto, para executar o projeto em servidor local, execute o comando:

`mvn spring-boot:run`

Caso esteja utilizando um IDE, basta executar o comando de execução da própria ferramenta;

Após o carregamento, o projeto estará sendo executado na porta 8080 de seu servidor local.
