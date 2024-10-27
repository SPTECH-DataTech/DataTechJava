import client.S3Provider;
import client.S3Service;
import datatech.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Leitor;
import processor.Plantacao;
import processor.clima.Clima;
import processor.clima.LeitorClima;
import writer.ConexaoBanco;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //S3
        List<Log> logs = new ArrayList<Log>();
        String aplicacao = "Main";

        String bucketName = "bucket-data-tech";
        S3Service conexaoBucket = new S3Service(new S3Provider().getS3Client(), bucketName);

        //Fazer download do arquivo no bucket
        conexaoBucket.listObjects();
        conexaoBucket.downloadFiles();

        /*===================================================================================================================*/

        //Leitura
        String nomeArquivo = "./base-de-dados-para-tratar.xlsx";
        String nomeArquivoClima = "downloadsdados_83083_M_1985-01-01_1985-12-31.xlsx";

        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);


        Path caminhoClima = Path.of(nomeArquivoClima);
        InputStream arquivoClima = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo

        Leitor leitor = new Leitor();
        LeitorClima leitorClima = new LeitorClima();

        List<Plantacao> plantacoes = leitor.extrairPlantacao(nomeArquivo, arquivo);
        List<Clima> climas = leitorClima.extrairClimas(nomeArquivoClima, arquivoClima);

        arquivo.close();

        /*====================================================================================*/

        //BD

        ConexaoBanco conexao = new ConexaoBanco();
        conexao.inserirPlantacoesNoBanco(plantacoes);
        Log log = new Log(aplicacao + " ", LocalDateTime.now(), " Plantações inseridas com sucesso no banco de dados");

        System.out.println("Plantações extraídas com sucesso");

        /*===============================*/

        System.out.println("Realizando conexão com o Banco de Dados...");
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        System.out.println("Inserindo dados lidos no Banco de dados...");

        for (Clima clima : climas) {
            connection.update("INSERT INTO climaMunicipioDash (data, temperaturaMax, temperaturaMin, umidadeMedia) VALUES (?,?,?,?)",
                    clima.getDataMedicao(), clima.getMediaTemperaturaMaxima(), clima.getMediaTemperaturaMinima(), clima.getUmidadeAr()
            );
        }
        System.out.println("Inserções encerradas");


    }
}
