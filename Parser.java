import java.io.*;
import java.nio.file.*;

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

    public int a;
    public int b;
    public Operacoes listaOps;

    public static void main(String[] args){

        //leitura do arquivo como uma única linha, que será splitada em múltiplas instruçoes
        String entrada = (readFileAsString("testeula.ula"));
        entrada = entrada.replace("inicio:", "");
        entrada = entrada.replace("fim.", "");
        //corte do arquivo em múltiplas linhas
        String[] entradaCortada = entrada.split(";");

       /* for (String l : entradaCortada) {
            System.out.println(l);
        }*/

        System.out.println("Fim do programa, é isto.");



    }

    /**
     * @author Diogo Neiss
     * @param fileName
     * @return linha completa do arquivo
     */
    public static String readFileAsString(String fileName)
    {
        String data = "";
        try{
            data = new String(Files.readAllBytes(Paths.get(fileName)) );
        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }

}

class LinhaInstrucao {
    public String linhaComInstrucao;

    LinhaInstrucao(){

    }
    LinhaInstrucao(String fraseFiltrar){

    }
}