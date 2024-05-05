# TP2-aeds3-busca-por-palavras
Segundo trabalho prático da disciplina de Algoritmos e Estruturas de Dados III

## RELATÓRIO ##

### 1. Introdução ###
    
O trabalho consiste em implementar um sistema de busca por palavras em uma lista. Para isso, foi utilizado uma lista invertida, que é uma estrutura de dados que armazena, para cada
palavra, uma lista de livros que contém essa palavra. A lista de livros é uma lista encadeada, onde cada termo possui um título e um identificador. A lista invertida é uma árvore AVL, onde cada nó possui uma palavra e uma lista.

### 2. Dificuldades ###  

Tivemos dificuldades ao implementar os métodos read e removeAcentos, mas conseguimos resolver com a colaboração dos colegas de sala. Durante o processo, notamos uma peculiaridade no funcionamento do método removeAcentos, que variava de máquina para máquina. Em algumas, observamos a adição de uma interrogação nas palavras com acentos, pois eram removidos antes de chegarem ao método. No entanto, em outras máquinas, o método removeAcentos operava corretamente.

### 3. Classes e Métodos ###  
- ListaInvertida:
  - ```java
    public class ListaInvertida
    
    isStopWord(String word): Este método verifica se uma palavra é uma "stop word", ou seja, uma palavra comum que geralmente é filtrada em uma busca.  
    
    removeAcentos(String str): Este método remove os acentos de uma string.  
    
    create(String c, int d): Este método insere um dado na lista da chave de forma não ordenada.  
    
    read(String c): Este método retorna a lista de dados de uma determinada chave.  
    
    update(String c, int d, String nc): Este método atualiza cada chave (cada palavra) de um dado (um título de livro) com novas chaves dado um título de livro.
    
    delete(String c, int d): Este método remove o dado de uma chave (mas não apaga a chave nem apaga blocos).  
    
    print(): Este método imprime todas as listas invertidas.
     
- Main:
  - ```java
    public class Main
    
    main(String[] args): Este método é o método principal do programa, onde é feita a leitura dos arquivos de entrada e a execução das operações de inclusão, alteração, remoção e busca de livros.

### 4. Checklist ###

- A inclusão de um livro acrescenta os termos do seu título à lista invertida? Sim, a inclusão de um livro acrescenta os termos do seu título à lista invertida.
- A alteração de um livro modifica a lista invertida removendo ou acrescentando termos do título? Sim, a alteração de um livro modifica a lista invertida removendo e acrescentando termos do título.
- A remoção de um livro gera a remoção dos termos do seu título na lista invertida? Sim, a remoção de um livro gera a remoção dos termos do seu título na lista invertida.
- Há uma busca por palavras que retorna os livros que possuam essas palavras? Sim, há uma busca por palavras que retorna os livros que possuam essas palavras.
- Essa busca pode ser feita com mais de uma palavra? Sim, essa busca pode ser feita com mais de uma palavra.
- As stop words foram removidas de todo o processo? Sim, as stop words foram removidas de todo o processo.
- Que modificação, se alguma, você fez para além dos requisitos mínimos desta tarefa? Criação de um método para remover acentos e caracteres especiais, além da criação de um arquivo contendo todas as stop words para facilitar o processo do trabalho.
- O trabalho está funcionando corretamente? Sim, o trabalho está funcionando corretamente.
- O trabalho está completo? Sim, o trabalho está completo.
- O trabalho é original e não a cópia de um trabalho de um colega? Sim, o trabalho é original e não a cópia de um trabalho de um colega.
