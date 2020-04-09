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


    public static void main(String[] args){

        //leitura do arquivo como uma única linha, que será splitada em múltiplas instruçoes
        String entrada = (readFileAsString("testeula.ula"));
        entrada = entrada.replace("inicio:", "");
        entrada = entrada.replace("fim.", "");
        //corte do arquivo em múltiplas linhas
        String[] entradaCortada = entrada.split(";");
        LinhaInstrucao[] listaInstrucoes = new LinhaInstrucao[entradaCortada.length];

        //construir os objetos
       for (int i = 0; i < entradaCortada.length; i++) {
            listaInstrucoes[i] = new LinhaInstrucao(entradaCortada[i]);
           System.out.print(listaInstrucoes[i]);
       }

       System.out.println("Fim do programa, é isto.");

    }

    /**
     * @author Diogo Neiss
     * @param fileName nome do arquivo na pasta corrente
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
        limparString(fraseFiltrar);
    }

    @Override
    public String toString() {
        return this.linhaComInstrucao;
    }

    /**
     * Converter a string splitada em um objeto devido e organizado.
     * @param frasefiltrar frase a ser filtrada
     */
    private void limparString(String frasefiltrar){
        if(frasefiltrar.equals(" ") && frasefiltrar.equals("")){
            frasefiltrar += ';';

            if(frasefiltrar.contains("=A"))
                frasefiltrar = frasefiltrar.replace("=A", "=10");
            if(frasefiltrar.contains("=B"))
                frasefiltrar = frasefiltrar.replace("=B", "=11");
            if(frasefiltrar.contains("=C"))
                frasefiltrar = frasefiltrar.replace("=C", "=12");
            if(frasefiltrar.contains("=D"))
                frasefiltrar = frasefiltrar.replace("=D", "=13");
            if(frasefiltrar.contains("=E"))
                frasefiltrar = frasefiltrar.replace("=E", "=14");
            if(frasefiltrar.contains("=F"))
                frasefiltrar = frasefiltrar.replace("=F", "=15");

            this.linhaComInstrucao = frasefiltrar;
        }
    }
}