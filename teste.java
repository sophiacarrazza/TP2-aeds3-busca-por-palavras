import java.io.RandomAccessFile;
import java.text.Normalizer;
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
      // System.out.println(stopWord + " - ");
      if (stopWord.compareTo(word) == 0) {
        arq.close();
        return true;
      }
    }
    arq.close();
    return false;
  }

  public static String removerAcentos(String palavra) {
    // Substitui caracteres acentuados por suas versões não acentuadas
    palavra = palavra.replace('\u00E1', 'a'); // á
    palavra = palavra.replace('\u00E0', 'a'); // à
    palavra = palavra.replace('\u00E2', 'a'); // â
    palavra = palavra.replace('\u00E3', 'a'); // ã
    palavra = palavra.replace('\u00E9', 'e'); // é
    palavra = palavra.replace('\u00E8', 'e'); // è
    palavra = palavra.replace('\u00EA', 'e'); // ê
    palavra = palavra.replace('\u00ED', 'i'); // í
    palavra = palavra.replace('\u00EC', 'i'); // ì
    palavra = palavra.replace('\u00EE', 'i'); // î
    palavra = palavra.replace('\u00F3', 'o'); // ó
    palavra = palavra.replace('\u00F2', 'o'); // ò
    palavra = palavra.replace('\u00F4', 'o'); // ô
    palavra = palavra.replace('\u00F5', 'o'); // õ
    palavra = palavra.replace('\u00FA', 'u'); // ú
    palavra = palavra.replace('\u00F9', 'u'); // ù
    palavra = palavra.replace('\u00FB', 'u'); // û
    palavra = palavra.replace('\u00E7', 'c'); // ç
    palavra = palavra.replace('\u00C7', 'C'); // Ç
    return palavra;
  }

  public static String removerAcentos2(String palavra) {
    // Remove os acentos da palavra utilizando Normalizer e depois remove os
    // caracteres especiais
    palavra = Normalizer.normalize(palavra, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "") // Remove os acentos
        .replaceAll("[^a-zA-Z0-9]", "") // Remove caracteres especiais
        .toLowerCase(); // Converte para minúsculas
    return palavra;
  }

  public static String substituirAcentos(String texto) {
    // Remove os acentos usando a classe Normalizer
    String textoSemAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "");
    // Substitui caracteres acentuados específicos por seus equivalentes sem acento
    textoSemAcentos = textoSemAcentos.replaceAll("[áàâã]", "a")
        .replaceAll("[éèê]", "e")
        .replaceAll("[íìî]", "i")
        .replaceAll("[óòôõ]", "o")
        .replaceAll("[úùû]", "u")
        .replaceAll("[ÁÀÂÃ]", "A")
        .replaceAll("[ÉÈÊ]", "E")
        .replaceAll("[ÍÌÎ]", "I")
        .replaceAll("[ÓÒÔÕ]", "O")
        .replaceAll("[ÚÙÛ]", "U")
        .replaceAll("[ç]", "c")
        .replaceAll("[Ç]", "C")
        .replaceAll("[ñ]", "n")
        .replaceAll("[Ñ]", "N");
    return textoSemAcentos;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in, "UTF-8");

    System.out.println("Digite a palavra que deseja verificar se é stopword: ");
    String palavra = scanner.nextLine();
    System.out.println("Palavra digitada: " + palavra);
    palavra = removerAcentos(palavra);
    // palavra = palavra.normalize(texto, Normalizer.Form.NFD);
    System.out.println("Palavra sem acentos: " + palavra);
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