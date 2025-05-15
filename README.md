#  SPOTFEI

**Disciplina**: CCM310  - ARQUITETURA DE SOFTWARE E PROGRAMAÇÃO ORIENTADA A OBJETOS

**Aluno**: *Ester Pereira dos Santos Nascimento*  
**RA**: *22.123.110-3*  
**Curso**: *Ciência da Computação*  


---

## 1. Introdução

O presente relatório descreve o desenvolvimento do projeto **Spotifei**, uma aplicação desktop que simula um sistema de informações sobre músicas e playlists. O projeto foi elaborado de forma individual como parte da avaliação da disciplina CCM310.

 O objetivo do Spotifei  é permitir o **gerenciamento de informações musicais**, como busca, curtidas, histórico e playlists. Toda a estrutura foi desenvolvida com foco em orientação a objetos, persistência de dados e separação clara entre camadas.

---

## 2. Objetivos do Projeto

O projeto tem como principais objetivos:

- Aplicar conceitos de programação orientada a objetos;
- Projetar uma aplicação com interface gráfica usando Java Swing;
- Implementar acesso e persistência de dados com JDBC e PostgreSQL;
- Utilizar o padrão arquitetural MVC (Model-View-Controller);
- Desenvolver um sistema interativo, funcional e amigável ao usuário.

---

## 3. Tecnologias e Arquitetura

### Tecnologias Utilizadas

- **Java (Swing)**: Interface gráfica da aplicação.
- **JDBC**: Comunicação entre aplicação Java e banco de dados PostgreSQL.
- **PostgreSQL**: Armazenamento e manipulação de dados relacionais.
- **NetBeans**: Ambiente de desenvolvimento.


### Estrutura do Projeto

- **Model**: Representa as entidades principais (`Usuario`, `Musica`, `Playlist`, `Artista`, `HistoricoBusca`, etc.).
- **View**: Telas gráficas interativas construídas com `JFrame` e `JPanel`.

---

## 4. Funcionalidades Implementadas

Como o projeto foi feito individualmente, **não foi implementado o perfil de administrador**.

### Funcionalidades do Usuário

- **Cadastro e Login**:
  - Criação de novos usuários e autenticação via email e senha.
- **Alteração de Senha**:
  - Após login, o usuário pode alterar sua senha de forma segura.
- **Exclusão de Conta**:
  - O usuário pode excluir sua conta, com remoção em cascata de suas playlists e dados relacionados.

### Funcionalidades de Música

- **Busca de Músicas**:
  - Por nome da música, artista ou gênero.
  - Exibição de informações como nome, artista, gênero e duração.

- **Curtidas 💖 e Descurtidas 💔**:
  - Usuários podem curtir ou descurtir músicas.
  - As preferências são armazenadas no banco de dados.

### Gerenciamento de Playlists

- Criar, editar e excluir playlists.
- Adicionar ou remover músicas nas playlists.
- Interface gráfica dedicada e intuitiva.

### Histórico de Uso

- Visualizar últimas 10 buscas realizadas.
- Listar músicas curtidas e descurtidas.

---

## 5. Banco de Dados

A persistência dos dados é feita no PostgreSQL. A estrutura do banco contempla tabelas para:

- `usuarios` 
- `musicas`
- `artistas`
- `generos`
- `playlists`
- `musicas_playlists`
- `curtidas`, `descurtidas`
- `historico_buscas`

Todas as operações são feitas com `PreparedStatements`, garantindo segurança e eficiência nas queries.

---

## 6. Resultados e Considerações Finais

O desenvolvimento do Spotifei permitiu consolidar os conhecimentos sobre:

- Programação orientada a objetos em Java;
- Integração com banco de dados PostgreSQL;
- Criação de interfaces gráficas responsivas com Java Swing.

As funcionalidades atendem integralmente os requisitos definidos para projetos individuais. O sistema é estável, com persistência adequada de dados e uma interface que facilita a navegação e o uso do sistema.



---





## 7. Conclusão

O projeto Spotifei foi uma oportunidade prática de integrar os principais conhecimentos do curso em um sistema funcional, realista e bem estruturado. Além dos aprendizados técnicos, o trabalho exigiu organização, controle de versão e documentação adequada.


---


