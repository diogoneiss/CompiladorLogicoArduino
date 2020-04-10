# CompiladorLogicoArduino

Arquitetura de compputadores III

Parser de instrução em pseudo-linguagem lógica e criação de arquivo com instruções hex, também passadas para o arduino

## Como rodar
É necessário que os arquivos testeula.ula , com as entradas desejadas, esteja na pasta, além do conhecimento de que porta o arduino está. Essa informação será perguntada ao longo da execução.

## Estrutura do projeto 
* Classe privada de leitura e manipulação da entrada
* Classe pública com a manipulação das informações
* Arquivo .c que deve ser compilado dentro do arduino
* envia.exe, executável que envia as informações para o monitor serial do arduino

### Autores
* Diogo Neiss
* Leonardo Lobato
* Lucas Saliba
