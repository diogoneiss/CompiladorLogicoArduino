import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
Modelo:

inicio:
A=5;
B=2;
An;
Bn;
AnandB;
A=D;
AneB;
fim.
 */

public class Parser {

    public static void main(String[] args) {

		Scanner entrada = new Scanner(System.in);
		ProcessBuilder pb;
		Process p;
		int tempo = 500;
		String x;
		Long start_time;
		Long diff_time;
		String porta;

		System.out.println("Em qual porta o Arduino está localizado ?");
		porta = entrada.nextLine();


		// hashmap das instruções, com ele vou procurar as coisas
		Map<String, Integer> opCodeMap = new HashMap<>();
		// colocar as ops dentro
		preencherHash(opCodeMap);

		// leitura do arquivo como uma única linha, que será splitada em múltiplas
		// instruçoes
		// e limpeza de não instruções
		String entrada = (readFileAsString("testeula.ula"));
		entrada = entrada.replace("inicio:", "");
		entrada = entrada.replace("fim.", "");

		// corte do arquivo em múltiplas linhas
		String[] entradaCortada = entrada.split(";");
		LinhaInstrucao[] listaInstrucoes = new LinhaInstrucao[entradaCortada.length];

		int quantidadeLinhas = entradaCortada.length;

		try {
			PrintWriter arquivoHex = new PrintWriter(new BufferedWriter(new FileWriter("testeula.hex", true)));

			for (int i = 0; i < quantidadeLinhas; i++) {
				// criar o obj e fazer os nétodos do construtor
				listaInstrucoes[i] = new LinhaInstrucao(entradaCortada[i], opCodeMap);

				/*
				 * o asterisco denota uma operação inválida opArduino[0] = Valor de A, em hex
				 * opArduino[1] = valor de B, em hex opArduino[2] = código da operação. Se for
				 * inválida é um *
				 */
				if (listaInstrucoes[i].opArduino[2] != '*') {
					char valorA = listaInstrucoes[i].opArduino[0];
					char valorB = listaInstrucoes[i].opArduino[1];
					char valorOp = listaInstrucoes[i].opArduino[2];

					System.out.println("Operação arduino: " + valorA + valorB + valorOp);
					// TODO: chamar a função sys do arduino com os characteres aqui.
					x = "";
					x += valorA;
					x += valorB;
					x += valorOp;
					
					pb = new ProcessBuilder("envia.exe", porta, x); 
					p = pb.start();
					start_time = System.nanoTime();
					p.waitFor();
					di ff_time = (System.nanoTime() - start_time) / 1000000;

					//escrever *A , *B e *Op.
					arquivoHex.print(valorA);
					arquivoHex.print(valorB);
					arquivoHex.println(valorOp);
				}
			}
			arquivoHex.close();
		}

		

		System.out.println("Fim do programa, é isto.");

	}

    */**
      * Método para preencher o hashmap com os valores da tabela, em estilo
      * key->value
      *
      * @param opCodeMap hash de string e int
      */
    *ivate

    static void pre**
    Códigos das oper 0 lógic 1
    lógic A’An 2 B’Bn 3 A+*AouB 4
    A.B AeB 5 A⊕
    B AxorB 6(A.B)’

    AnandB  ( A + 
		 *  )’ Ano ( A ⊕B )’ Axno A’ + B AnouB A + B’ AouBn B A’. B AneB C A . B ‘ AeBn D
			A’ + B’ AnouBn E
			A’ . B’ F
		 */

		opCodeMap.put("zeroL", 0x0);
		opCodeMap.put("umL", 0x1);
		opCodeMap.put("An", 0x2);
		opCodeMap.put("Bn", 0x3);
		opCodeMap.put("AouB", 0x4);
		opCodeMap.put("AeB", 0x5);
		opCodeMap.put("AxorB", 0x6);
		opCodeMap.put("AnandB", 0x7);
		opCodeMap.put("AnorB", 0x8);
		opCodeMap.put("AxnorB", 0x9);
		opCodeMap.put("AnouB", 0xA);
		opCodeMap.put("AouBn", 0xB);
		opCodeMap.put("AneB", 0xC);
		opCodeMap.put("AeBn", 0xD);
		opCodeMap.put("AnouBn", 0xE);
		opCodeMap.put("AneBn", 0xF);

	}

    /**
	 * @param fileName nome do arquivo na pasta corrente
	 * @return linha completa do arquivo
	 * @author Diogo Neiss
	 */
	public static String readFileAsString(String fileName) {
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

}

*

/**
 * Classe que conterá o valor estático atual de a, b e a operação contida na
 * linha.
 */
class LinhaInstrucao {
    public static int a = 0;
    public static int b = 0;

    public int op;
    public String linhaComInstrucao;
    public Character[] opArduino;

    LinhaInstrucao(String fraseFiltrar, Map<String, Integer> opCodeMap) {
        op Arduino = new Character[3];

        // converter hex pra dec
        limparString(fraseFiltrar);
        // descobrir a op da linha
        //
        detectarOp(opCodeMap);
        // salvar numa string o codigo a ser transmitido da linha ou, se for uma
        // atribuição, apenas "".
        comunicacaoOp();
    }

    private static void atribuirA(int valor) {
        a = valor;
    }

    private static void atribuirB(int valor) {
        b = valor;
    }

    public void detectarOp(Map<String, Integer> opCodeMap) {
		//indica que é uma operação não contada, como atribuição
		th is.op = -1;
		String linha = this.linhaComInstrucao;
		//v erificar se é atribuição
		if (linha.contains("=")) {
			//separar as partes da atribuição
			String[] corte = linha.split("=");
 
			if (corte[0].charAt(0) == 'A') {
				//atribuir a a o valor a direita do =
				atribuirA(Integer.parseInt(corte[1]));
			} else
				 atribuirB(Integer.parseInt(corte[1]));
		}
		//n ão é uma op de atribuição
		else {
			//procurar a key e retornar o valor
			if (opCodeMap.containsKey(linha.trim()))

    public void comunicacaoOp() {
		//se nao tiver uma operacao valida não há o que retornar.
		if (op == -1) {
			opArduino[2] = '*';
		} else {
			Character valorA = Integer.toHexString(LinhaInstrucao.a).charAt(0);
			Character valorB = Integer.toHexString(LinhaInstrucao.b).charAt(0);
			Character valorC = Integer.toHexString(op).charAt(0);

			System.out.println("----------------");
			System.out.println("Valor da operação: " + v

			

			opArduino[0] = valorA;
			opArduino[1] = valorB;
			opArduino[2] = valorC;
		}
	}

    /**
     * Método to string modificado, para debug do programa.
     *
     * @return String a ser printada.
     */
    @Override
    public String toString() {
        if (this.op != -1) {
            String resp = linhaComInstrucao;
            resp = resp.concat("\t Operação: " + this.op + "\n");

            return resp;
        } else
            return "";

    }

    /**
     * Converter a string splitada em uma string com valores decimais e retirar \n
     *
     * @param frasefiltrar frase a ser filtrada
     */
    private void limparString(String frasefiltrar) {
        if ((!frasefiltrar.equals(" ")) && (!frasefiltrar.equals(""))) {

            if (frasefiltrar.contains("=A"))
                frasefiltrar = frasefiltrar.replace("=A", "=10");
            else if (frasefiltrar.contains("=B"))
                frasefiltrar = frasefiltrar.replace("=B", "=11");
            else if (frasefiltrar.contains("=C"))
                frasefiltrar = frasefiltrar.replace("=C", "=12");
            else if (frasefiltrar.contains("=D"))
                frasefiltrar = frasefiltrar.replace("=D", "=13");
            else if (frasefiltrar.contains("=E"))
                frasefiltrar = frasefiltrar.replace("=E", "=14");
            else if (frasefiltrar.contains("=F"))
                frasefiltrar = frasefiltrar.replace("=F", "=15");

            if (frasefiltrar.contains("\n"))
                frasefiltrar = frasefiltrar.replace("\n", "");

            this.linhaComInstrucao = frasefiltrar;
        }
    }
}
