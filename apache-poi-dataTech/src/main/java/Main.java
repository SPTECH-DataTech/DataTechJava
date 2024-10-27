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
        List<Log> logs = new ArrayList<Log>();
        String aplicacao = "Main";
        ConexaoBanco conexaoBanco = new ConexaoBanco();

        String bucketName = "bucket-data-tech";
        S3Service conexaoBucket = new S3Service(new S3Provider().getS3Client(), bucketName);

        //Fazer download do arquivo no bucket
        conexaoBucket.listObjects();
        conexaoBucket.downloadFiles();

        /*===================================================================================================================*/

        //Leitura
        String nomeArquivo = "download-basesbase-de-dados-para-tratar.xlsx";
        String nomeArquivoClima = "downloadsdados_83083_M_1985-01-01_1985-12-31.xlsx";


        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        Path caminhoClima = Path.of(nomeArquivoClima);
        InputStream arquivoClima = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo

        Leitor leitor = new Leitor();
        LeitorClima leitorClima = new LeitorClima();

        List<Plantacao> plantacoes = leitor.extrairPlantacao(nomeArquivo, arquivo);
//        List<Clima> climas = leitorClima.extrairClimas(nomeArquivoClima, arquivoClima);

        arquivo.close();
        Log logExtracaoBase = new Log(aplicacao, LocalDateTime.now(), "Plantações extraídas com sucesso");
        conexaoBanco.inserirLogNoBanco(logExtracaoBase);
        System.out.println("Plantações extraídas com sucesso");
        /*====================================================================================*/

        //BD
        Log logConexaoBanco = new Log(aplicacao, LocalDateTime.now(), "Realizando conexão com o Banco de Dados...");
        conexaoBanco.inserirLogNoBanco(logConexaoBanco);
        System.out.println("Realizando conexão com o Banco de Dados...");

        ConexaoBanco conexao = new ConexaoBanco();
        Log logInsercaoBanco = new Log(aplicacao, LocalDateTime.now(), "Inserindo dados lidos no Banco de dados...");
        conexaoBanco.inserirLogNoBanco(logInsercaoBanco);
        System.out.println("Inserindo dados lidos no Banco de dados...");
        conexao.inserirPlantacoesNoBanco(plantacoes);

        Log logSucesso = new Log(aplicacao + " ", LocalDateTime.now(), " Plantações inseridas com sucesso no banco de dados");
        conexao.inserirLogNoBanco(logSucesso);

        System.out.println("Inserções encerradas");

    }
}
