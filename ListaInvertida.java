
/*********
 * LISTA INVERTIDA
 * String chave, int dado
 * 
 * Os nomes dos métodos foram mantidos em inglês
 * apenas para manter a coerência com o resto da
 * disciplina:
 * - boolean create(String chave, int dado)
 * - int[] read(int chave)
 * - boolean delete(String chave, int dado)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2020
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ListaInvertida {

  String nomeArquivoDicionario;
  String nomeArquivoBlocos;
  RandomAccessFile arqDicionario;
  RandomAccessFile arqBlocos;
  int quantidadeDadosPorBloco;

  class Bloco {

    short quantidade; // quantidade de dados presentes na lista
    short quantidadeMaxima; // quantidade máxima de dados que a lista pode conter
    int[] dados; // sequência de dados armazenados no bloco
    long proximo; // ponteiro para o bloco sequinte da mesma chave
    short bytesPorBloco; // size fixo do cesto em bytes

    public Bloco(int qtdmax) throws Exception {
      quantidade = 0;
      quantidadeMaxima = (short) qtdmax;
      dados = new int[quantidadeMaxima];
      proximo = -1;
      bytesPorBloco = (short) (2 + 4 * quantidadeMaxima + 8);
    }

    public byte[] toByteArray() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeShort(quantidade);
      int i = 0;
      while (i < quantidade) {
        dos.writeInt(dados[i]);
        i++;
      }
      while (i < quantidadeMaxima) {
        dos.writeInt(-1);
        i++;
      }
      dos.writeLong(proximo);
      return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
      quantidade = dis.readShort();
      int i = 0;
      while (i < quantidadeMaxima) {
        dados[i] = dis.readInt();
        i++;
      }
      proximo = dis.readLong();
    }

    // Insere um valor no bloco
    public boolean create(int d) {
      if (full())
        return false;
      int i = quantidade - 1;
      while (i >= 0 && d < dados[i]) {
        dados[i + 1] = dados[i];
        i--;
      }
      i++;
      dados[i] = d;
      quantidade++;
      return true;
    }

    // Lê um valor no bloco
    public boolean test(int d) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && d > dados[i])
        i++;
      if (i < quantidade && d == dados[i])
        return true;
      else
        return false;
    }

    // Remove um valor do bloco
    public boolean delete(int d) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && d > dados[i])
        i++;
      if (d == dados[i]) {
        while (i < quantidade - 1) {
          dados[i] = dados[i + 1];
          i++;
        }
        quantidade--;
        return true;
      } else
        return false;
    }

    public int last() {
      return dados[quantidade - 1];
    }

    public int[] list() {
      int[] lista = new int[quantidade];
      for (int i = 0; i < quantidade; i++)
        lista[i] = dados[i];
      return lista;
    }

    public boolean empty() {
      return quantidade == 0;
    }

    public boolean full() {
      return quantidade == quantidadeMaxima;
    }

    public String toString() {
      String s = "\nQuantidade: " + quantidade + "\n| ";
      int i = 0;
      while (i < quantidade) {
        s += dados[i] + " | ";
        i++;
      }
      while (i < quantidadeMaxima) {
        s += "- | ";
        i++;
      }
      return s;
    }

    public long next() {
      return proximo;
    }

    public void setNext(long p) {
      proximo = p;
    }

    public int size() {
      return bytesPorBloco;
    }

  }

  public ListaInvertida(int n, String nd, String nc) throws Exception {
    quantidadeDadosPorBloco = n;
    nomeArquivoDicionario = nd;
    nomeArquivoBlocos = nc;

    arqDicionario = new RandomAccessFile(nomeArquivoDicionario, "rw");
    arqBlocos = new RandomAccessFile(nomeArquivoBlocos, "rw");
  }

  // Verifica se a palavra é uma stopword
  public static boolean isStopWord(String word) throws Exception {
    RandomAccessFile arq = new RandomAccessFile("dados/stopwords.txt", "rw");
    String stopWord;

    while ((stopWord = arq.readLine()) != null) {
      int tam = stopWord.length();
      // se a palavra tiver espaco no final (um problema do arquivo.txt), remove ele
      while (tam > 0 && stopWord.charAt(tam - 1) == ' ') {
        stopWord = stopWord.substring(0, tam - 1);
        tam = stopWord.length(); // atualiza o valor de tam
      }
      if (stopWord.compareTo(word) == 0) {
        arq.close();
        return true;
      }
    }
    arq.close();
    return false;
  }

  // funcao para tirar os acentos de uma String
  public static String removeAcentos(String str) {
    return str.replaceAll("[ãâáàä]", "a").replaceAll("[êéèë]", "e").replaceAll("[îíìï]", "i").replaceAll("[õôóòö]", "o")
        .replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÁÀÄ]", "A").replaceAll("[ÊÉÈË]", "E").replaceAll("[ÎÍÌÏ]", "I")
        .replaceAll("[ÕÔÓÒÖ]", "O").replaceAll("[ÛÚÙÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n')
        .replace('Ñ', 'N');
  }

  // Insere um dado na lista da chave de forma NÃO ORDENADA
  public boolean create(String c, int d) throws Exception {

    // Percorre toda a lista testando se já não existe
    // o dado associado a essa chave
    int[] lista = read(c);
    for (int i = 0; i < lista.length; i++)
      if (lista[i] == d)
        return false;

    String chave = "";
    long endereco = -1;
    boolean jaExiste = false;

    // separa a chave em varias chaves de suas palavras com o mesmo endereco de
    // bloco
    String[] palavras = c.split(" ");
    for (int i = 0; i < palavras.length; i++) {
      // localiza a chave no dicionário
      arqDicionario.seek(0);
      while (arqDicionario.getFilePointer() != arqDicionario.length()) {
        chave = arqDicionario.readUTF();
        endereco = arqDicionario.readLong();
        if (chave.compareTo(palavras[i]) == 0) {
          jaExiste = true;
          break;
        }
      }
      if (!isStopWord(palavras[i])) {
        palavras[i] = removeAcentos(palavras[i]);
        if (!jaExiste) { // Se não encontrou, cria um novo bloco para as palavras
          // Cria um novo bloco
          Bloco b = new Bloco(quantidadeDadosPorBloco);
          endereco = arqBlocos.length();
          arqBlocos.seek(endereco);
          arqBlocos.write(b.toByteArray());

          // Insere a nova chave no dicionário
          arqDicionario.seek(arqDicionario.length());
          arqDicionario.writeUTF(palavras[i]);
          arqDicionario.writeLong(endereco);
        }

        // Cria um laço para percorrer todos os blocos encadeados nesse endereço
        Bloco b1 = new Bloco(quantidadeDadosPorBloco);
        byte[] bd;
        while (endereco != -1) {
          long proximo = -1;

          // Carrega o bloco
          arqBlocos.seek(endereco);
          bd = new byte[b1.size()];
          arqBlocos.read(bd);
          b1.fromByteArray(bd);

          // Testa se o dado cabe nesse bloco
          if (!b1.full()) {
            b1.create(d);
          } else {
            // Avança para o próximo bloco
            proximo = b1.next();
            if (proximo == -1) {
              // Se não existir um novo bloco, cria esse novo bloco
              Bloco b2 = new Bloco(quantidadeDadosPorBloco);
              proximo = arqBlocos.length();
              arqBlocos.seek(proximo);
              arqBlocos.write(b2.toByteArray());

              // Atualiza o ponteiro do bloco anterior
              b1.setNext(proximo);
            }
          }

          // Atualiza o bloco atual
          arqBlocos.seek(endereco);
          arqBlocos.write(b1.toByteArray());
          endereco = proximo;
        }

      }
    }

    return true;
  }

  // Retorna a lista de dados de uma determinada chave
  public int[] read(String c) throws Exception {

    ArrayList<Integer> lista = new ArrayList<>();

    String chave = "";
    long endereco = -1;
    boolean jaExiste = false;

    // localiza a chave no dicionário
    arqDicionario.seek(0);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      chave = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (chave.compareTo(c) == 0) {
        jaExiste = true;
        break;
      }
    }
    if (!jaExiste)
      return new int[0];

    // Cria um laço para percorrer todos os blocos encadeados nesse endereço
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {

      // Carrega o bloco
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);

      // Acrescenta cada valor à lista
      int[] lb = b.list();
      for (int i = 0; i < lb.length; i++)
        lista.add(lb[i]);

      // Avança para o próximo bloco
      endereco = b.next();

    }

    // Constrói o vetor de respostas
    lista.sort(null);
    int[] resposta = new int[lista.size()];
    for (int j = 0; j < lista.size(); j++)
      resposta[j] = (int) lista.get(j);
    return resposta;
  }

  // Função update que atualiza cada chave (cada palavra) de um dado(um
  // titulo de livro) com novas chaves dado um titulo de livro
  public boolean update(String c, int d, String nc) throws Exception {

    // Percorre toda a lista testando se já não existe
    // o dado associado a essa chave
    int[] lista = read(nc);
    for (int i = 0; i < lista.length; i++)
      if (lista[i] == d)
        return false;

    // separa a chave em varias chaves de suas palavras com o mesmo endereco de
    // bloco
    String[] palavras = nc.split(" ");
    for (int i = 0; i < palavras.length; i++) {
      if (!isStopWord(palavras[i])) {
        palavras[i] = removeAcentos(palavras[i]);

        // for que percorre cada palavra chave do titulo original e compara as
        // diferentes palavras do titulo novo
        // para ver se a palavra chave do titulo original é diferente da palavra chave
        // do titulo novo
        // se for diferente, ele deleta a palavra chave do titulo original e insere a
        // palavra chave do titulo novo
        for (int j = 0; j < palavras.length; j++) {
          if (!isStopWord(palavras[j])) {
            palavras[j] = removeAcentos(palavras[j]);
            if (palavras[i].compareTo(palavras[j]) != 0) {
              delete(palavras[i], d);
              create(palavras[j], d);
            }
          }
        }
      }
    }
    return true;
  }

  // Remove o dado de uma chave (mas não apaga a chave nem apaga blocos)
  public boolean delete(String c, int d) throws Exception {

    String chave = "";
    long endereco = -1;
    boolean jaExiste = false;

    // localiza a chave no dicionário
    arqDicionario.seek(0);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {
      chave = arqDicionario.readUTF();
      endereco = arqDicionario.readLong();
      if (chave.compareTo(c) == 0) {
        jaExiste = true;
        break;
      }
    }
    if (!jaExiste)
      return false;

    // Cria um laço para percorrer todos os blocos encadeados nesse endereço
    Bloco b = new Bloco(quantidadeDadosPorBloco);
    byte[] bd;
    while (endereco != -1) {

      // Carrega o bloco
      arqBlocos.seek(endereco);
      bd = new byte[b.size()];
      arqBlocos.read(bd);
      b.fromByteArray(bd);

      // Testa se o valor está neste bloco e sai do laço
      if (b.test(d)) {
        b.delete(d);
        arqBlocos.seek(endereco);
        arqBlocos.write(b.toByteArray());
        return true;
      }

      // Avança para o próximo bloco
      endereco = b.next();
    }

    // chave não encontrada
    return false;

  }

  public void print() throws Exception {

    System.out.println("\nLISTAS INVERTIDAS:");

    // Percorre todas as chaves
    arqDicionario.seek(0);
    while (arqDicionario.getFilePointer() != arqDicionario.length()) {

      String chave = arqDicionario.readUTF();
      long endereco = arqDicionario.readLong();

      // Percorre a lista desta chave
      ArrayList<Integer> lista = new ArrayList<>();
      Bloco b = new Bloco(quantidadeDadosPorBloco);
      byte[] bd;
      while (endereco != -1) {

        // Carrega o bloco
        arqBlocos.seek(endereco);
        bd = new byte[b.size()];
        arqBlocos.read(bd);
        b.fromByteArray(bd);

        // Acrescenta cada valor à lista
        int[] lb = b.list();
        for (int i = 0; i < lb.length; i++)
          lista.add(lb[i]);

        // Avança para o próximo bloco
        endereco = b.next();
      }

      // Imprime a chave e sua lista
      System.out.print(chave + ": ");
      lista.sort(null);
      for (int j = 0; j < lista.size(); j++)
        System.out.print(lista.get(j) + " ");
      System.out.println();
    }
  }
}
