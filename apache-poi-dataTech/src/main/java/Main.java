
import client.S3Service;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.clima.Clima;
import processor.plantacao.Plantacao;
import writer.ConexaoBanco;

import java.io.IOException;
import java.util.List;

import static service.SlackService.sendMessage;


public class Main {

    public static void main(String[] args) throws IOException {
        Application aplicacao = new Application();
        ConexaoBanco conexaoBanco = aplicacao.conectarComBanco();
        JdbcTemplate conexao = conexaoBanco.getConnection();
        S3Service conexaoBucket = aplicacao.conectarComBucket();
        aplicacao.baixarArquivosS3(conexaoBucket);
        //===================================================================================================================
        //Leitura
        List<Plantacao> plantacoes = aplicacao.lerArquivoPlantacoes(conexao);
        //List<EstadoMunicipio> estadosMunicipios = aplicacao.lerArquivoEstadoMunicipio();

//        List<Clima> climas = aplicacao.lerArquivoClima();

        //====================================================================================
        //BD
        aplicacao.inserirPlantacoesNoBanco(plantacoes);
        //aplicacao.inserirEstadoMunicipioNoBanco(estadosMunicipios);
//       aplicacao.inserirClimasNobanco(climas);
        sendMessage();
        //===================================================================================================================
        aplicacao.inserirLogEmArquivo();
    }
}
