# API RESTful com Play Framework (Java)

Este projeto é uma API RESTful desenvolvida utilizando **Java 17** e o **Play Framework**. Abaixo está o guia completo para configurar o ambiente do zero e rodar a aplicação.

## 🚀 Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas no Windows:

### 1.1 Java JDK 17
O Play Framework requer uma versão LTS específica. Estamos usando a **17**.
* **Verificar instalação:** Abra o CMD e digite `java -version`.
* **Se não tiver:** Baixe e instale o [Eclipse Adoptium (Temurin) JDK 17](https://adoptium.net/temurin/releases/?version=17).

### 1.2 SBT (Scala Build Tool)
Gerenciador de dependências e construção do projeto.
* **Download:** Baixe o instalador `.msi` oficial do [site do SBT](https://www.scala-sbt.org/download/).
* **Instalação:** Execute o instalador e siga o padrão (Next > Next > Finish).


## ▶️ Como Rodar o Projeto

### Via Terminal (Recomendado)
1.  Abra o terminal na raiz do projeto.
2.  Execute:
    ```bash
    sbt run
    ```
3.  Aguarde a mensagem: `Server started, ... listening on http://localhost:9000`.
4.  Acesse no navegador: [http://localhost:9000](http://localhost:9000).

*Dica: O Play Framework possui "Hot Reload". Você pode alterar arquivos `.java` e dar F5 no navegador sem precisar reiniciar o servidor.*
