import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AdicionarPalavraNoArquivo {
    public static void main(String palavra) throws IOException {
    	//Criar um novo arquivo, caso ele exista não fqz nada
    	String fileName = "/home/lucas/Documentos/arquivo.txt";
    	File file = new File(fileName);
    	
    	//Debug
    	try {
    		boolean criado = file.createNewFile();
    		if (criado)
    		{
    			//System.out.println("Debug: Arquivo criado com sucesso!");
    		}else {
    			//System.out.println("Debug: Arquivo já existe!");
    		}
    	}catch(IOException e){
    		e.printStackTrace();
			
		}
    	
    	//Contar o número de linhas
    	LineNumberReader lnr = new LineNumberReader(new FileReader("/home/lucas/Documentos/arquivo.txt"));
    	lnr.skip((Long.MAX_VALUE));
    	int retorno = lnr.getLineNumber();
    	
    	//Obter a data de hoje
    	LocalDate hoje = LocalDate.now();	
    	DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	String dataFormatada = hoje.format(formato);
    	
    	// abrir o arquivo
        FileWriter fw = new FileWriter("/home/lucas/Documentos/arquivo.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        
        // escrever a palavra no arquivo
        bw.write("LOG: " + ++retorno + " | Mensagem recebida: "  + palavra + " | Data de hoje: " + dataFormatada + "\n");
        

        // fechar o arquivo
        bw.close();
        fw.close();
    }
}