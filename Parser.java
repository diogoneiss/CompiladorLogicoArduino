import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

		//hashmap das instruções, com ele vou procurar as coisas
		Map<String, Integer> opCodeMap = new HashMap<>();
		//colocar as ops dentro
		preencherHash(opCodeMap);

		//leitura do arquivo como uma única linha, que será splitada em múltiplas instruçoes
		// e limpeza de não instruções
		String entrada = (readFileAsString("testeula.ula"));
		entrada = entrada.replace("inicio:", "");
		entrada = entrada.replace("fim.", "");

		//corte do arquivo em múltiplas linhas
		String[] entradaCortada = entrada.split(";");
		LinhaInstrucao[] listaInstrucoes = new LinhaInstrucao[entradaCortada.length];

		int quantidadeLinhas = entradaCortada.length;

		//construir os objetos
		for (int i = 0; i < quantidadeLinhas; i++) {
			listaInstrucoes[i] = new LinhaInstrucao(entradaCortada[i], opCodeMap);
			//System.out.print(listaInstrucoes[i]);

			//a cada 60 i's verificar se tá atribuindo direitinho
			/*
			if(i % 60 == 0){
				System.out.println("Valor de A: "+LinhaInstrucao.a +"\nValor de B: "+LinhaInstrucao.b);
			}

			 */
		}

		//pegar a instrução dentro do objeto
		for (int i = 0; i < quantidadeLinhas; i++) {
			if (!listaInstrucoes[i].opArduino.equals("")) {
				System.out.println("Operação arduino: " + listaInstrucoes[i].opArduino);
			}
		}


		System.out.println("Fim do programa, é isto.");

	}

	/**
	 * Método para preencher o hashmap com os valores da tabela, em estilo key->value
	 *
	 * @param opCodeMap hash de string e int
	 */
	private static void preencherHash(Map<String, Integer> opCodeMap) {
		/*
			Códigos das operações

			0 lógico zeroL 0
			1 lógico umL 1
			A’ An 2
			B’ Bn 3
			A + B AouB 4
			A . B AeB 5
			A ⊕B AxorB 6
			( A . B )’ AnandB 7
			( A + B )’ AnorB 8
			( A ⊕B )’ AxnorB 9
			A’ + B AnouB A
			A + B’ AouBn B
			A’. B AneB C
			A . B ‘ AeBn D
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

/**
 * Classe que conterá o valor estático atual de a, b e a operação contida na linha.
 */
class LinhaInstrucao {
	public static int a = 0;
	public static int b = 0;

	public int op;
	public String linhaComInstrucao;
	public String opArduino;

	LinhaInstrucao(String fraseFiltrar, Map<String, Integer> opCodeMap) {
		//converter hex pra dec
		limparString(fraseFiltrar);
		//descobrir a op da linha
		detectarOp(opCodeMap);
		//salvar numa string o codigo a ser transmitido da linha ou, se for uma atribuição, apenas "".
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
		this.op = -1;
		String linha = this.linhaComInstrucao;
		//verificar se é atribuição
		if (linha.contains("=")) {
			//separar as partes da atribuição
			String[] corte = linha.split("=");

			if (corte[0].charAt(0) == 'A') {
				//atribuir a a o valor a direita do =
				atribuirA(Integer.parseInt(corte[1]));
			} else
				atribuirB(Integer.parseInt(corte[1]));
		}
		//não é uma op de atribuição
		else {
			//procurar a key e retornar o valor
			if (opCodeMap.containsKey(linha.trim()))
				this.op = opCodeMap.get(linha.trim());
		}
	}


	public void comunicacaoOp() {
		//se nao tiver uma operacao valida não há o que retornar.
		if (op == -1) {
			this.opArduino = "";
		} else {
			String valorA = Integer.toHexString(LinhaInstrucao.a);
			String valorB = Integer.toHexString(LinhaInstrucao.b);
			String valorC = Integer.toHexString(op);

			String retorno = valorA.concat(valorB.concat(valorC));


			this.opArduino = retorno;
		}
	}

	/**
	 * Método to string modificado, para debug do programa.
	 *
	 * @return
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