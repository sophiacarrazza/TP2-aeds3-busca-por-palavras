import java.io.RandomAccessFile;
import java.util.Scanner;

class teste {
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
      System.out.println(stopWord + " - ");
      if (stopWord.compareTo(word) == 0) {
        arq.close();
        return true;
      }
    }
    arq.close();
    return false;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Digite a palavra que deseja verificar se é stopword: ");
    String palavra = scanner.nextLine();

    try {
      if (isStopWord(palavra)) {
        System.out.println("É stopword");
      } else {
        System.out.println("Não é stopword");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    scanner.close();
  }

}