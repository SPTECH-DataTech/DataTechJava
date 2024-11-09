import client.S3Service;
import processor.Plantacao;
import processor.clima.Clima;
import writer.ConexaoBanco;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Application aplicacao = new Application();
        ConexaoBanco conexaoBanco = aplicacao.conectarComBanco();

        S3Service conexaoBucket = aplicacao.conectarComBucket();

        //Fazer download do arquivo no bucket
        aplicacao.baixarArquivosS3(conexaoBucket);

        /*===================================================================================================================*/

        //Leitura

        List<Plantacao> plantacoes = aplicacao.lerArquivoPlantacoes();

        List<Clima> climas= aplicacao.lerArquivoClima();

        /*====================================================================================*/

        //BD
        aplicacao.inserirPlantacoesNoBanco(plantacoes);

        aplicacao.inserirClimasNobanco(climas);
    }
}
