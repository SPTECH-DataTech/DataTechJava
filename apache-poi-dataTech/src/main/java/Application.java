import client.S3Provider;
import client.S3Service;
import datatech.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Leitor;
import processor.Plantacao;
import processor.clima.Clima;
import processor.clima.LeitorClima;
import processor.estadoMunicipio.EstadoMunicipio;
import processor.estadoMunicipio.LeitorEstadoMunicipio;
import service.SlackService;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static service.OperationsCounter.registerFalied;
import static service.OperationsCounter.registerSuccess;
import static service.SlackService.errorSlack;


public class Application {
    private List<Log> logs = new ArrayList<Log>();
    String aplicacao = "Main";


    public ConexaoBanco conectarComBanco() {
        return new ConexaoBanco();
    }

    public S3Service conectarComBucket() {
        try {
            String bucketName = "bucket-data-tech";
            System.out.println("Conectando ao bucket: " + bucketName);
            registerSuccess();
            return new S3Service(new S3Provider().getS3Client(), bucketName);
        } catch (Exception e) {
            System.out.println("Houve um erro ao conectar com bucket S3: " + e.getMessage());
            registerFalied();
            errorSlack(e);
            throw new RuntimeException(e);
        }
    }

    public void baixarArquivosS3(S3Service conexaoBucket) {
     try {
         System.out.println("Baixando arquivos em S3...");
         conexaoBucket.listObjects();
         conexaoBucket.downloadFiles();
         registerSuccess();
     } catch (Exception e) {
         System.err.println("Houve um erro ao baixar os arquivos do bucket S3" + e.getMessage());
         errorSlack(e);
         registerFalied();
         throw new RuntimeException(e);
     }
    }

    public List<Plantacao> lerArquivoPlantacoes(JdbcTemplate conexao) throws IOException {
        String directory = "downloaded-bases/";
        // Lista os arquivos na pasta de downloads
        Stream<Path> files = Files.list(Paths.get(directory));

        // Filtra os arquivos removendo pastas e só pegando arquivos .xlsx e que começam com
        // "plantacao"
        Stream<Path> arquivosPlantacao = files.filter(
                file -> !Files.isDirectory(file)
                        && file.getFileName().toString().toLowerCase().endsWith(".xlsx")
                        && file.getFileName().toString().toLowerCase().startsWith("plantacao")
        );

        // Converte a stream para lista (não dá pra fazer foreach em Stream)
        List<Path> plantacaoList = arquivosPlantacao.toList();

        for (Path arquivoPlantacao : plantacaoList) {
            try (InputStream arquivoPlantacoes = Files.newInputStream(arquivoPlantacao)) {
                Leitor leitor = new Leitor();
                List<Plantacao> plantacoes = leitor.extrairPlantacao(arquivoPlantacao.getFileName().toString(), arquivoPlantacoes, conexao);

                Log logExtracaoBase = new Log("OK", aplicacao, LocalDateTime.now(), "Plantações extraídas com sucesso");
//                conectarComBanco().inserirLogNoBanco(logExtracaoBase);
                System.out.println("Plantações extraídas com sucesso");
                registerSuccess();
                return plantacoes;
            } catch (Exception e) {
                System.err.println("Houve um erro ao registar planatações: " + e.getMessage());
                errorSlack(e);
                registerFalied();
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public List<Clima> lerArquivoClima() throws IOException {
        String nomeArquivoClima = "../downloaded-bases/dados_83083_M_1985-01-01_1985-12-31.xlsx";
        List<Clima> climas = new ArrayList<>();

        try {
            Path caminhoClima = Path.of(nomeArquivoClima);
            InputStream arquivoClima = Files.newInputStream(caminhoClima);

            LeitorClima leitorClima = new LeitorClima();
            climas = leitorClima.extrairClimas(caminhoClima, arquivoClima);

            Log logExtracaoBase = new Log("OK", aplicacao, LocalDateTime.now(), "Climas registrados com sucesso");
//            conectarComBanco().inserirLogNoBanco(logExtracaoBase);
            System.out.println("Climas registrados com sucesso");
            registerSuccess();
        } catch (Exception e) {
            errorSlack(e);
            Log log = new Log("ERRO", aplicacao, LocalDateTime.now(), "Erro ao registrar climas");
//            conectarComBanco().inserirLogNoBanco(log);
            registerFalied();
            throw new RuntimeException(e);

        }

        return climas;
    }

    public List<EstadoMunicipio> lerArquivoEstadoMunicipio() throws IOException {
        String nomeArquivoEstadoMunicipio = "downloaded-bases/Base-de-Dados-Municipios-_Editada_.xlsx";

        List<EstadoMunicipio> estadoMunicipios = new ArrayList<>();
        try {
            Path caminhoEstadoMunicipio = Path.of(nomeArquivoEstadoMunicipio);
            InputStream arquivoEstadoMunicipio = Files.newInputStream(caminhoEstadoMunicipio);

            Log logExtracaoBase = new Log("OK", aplicacao, LocalDateTime.now(), "EstadoMunicipios registrados com sucesso");
//            conectarComBanco().inserirLogNoBanco(logExtracaoBase);
            System.out.println("EstadoMunicipios registrados com sucesso");

            LeitorEstadoMunicipio leitorEstadoMunicipio = new LeitorEstadoMunicipio();
            estadoMunicipios = leitorEstadoMunicipio.extrairEstadoMunicipio(caminhoEstadoMunicipio, arquivoEstadoMunicipio);
            registerSuccess();
        } catch (Exception e) {
            System.err.println("Erro inesperado em ler EstadoMunicipios: " + e.getMessage());
            Log log = new Log("ERRO", aplicacao, LocalDateTime.now(), "Falha ao registrar municípios");
//            conectarComBanco().inserirLogNoBanco(log);
            errorSlack(e);
            registerFalied();
        }
        return estadoMunicipios;
    }

    public void inserirPlantacoesNoBanco(List<Plantacao> plantacoes) {
        Log logInsercaoBanco = new Log("OK", aplicacao, LocalDateTime.now(), "Inserindo dados de plantações lidos no Banco de dados...");
//        conectarComBanco().inserirLogNoBanco(logInsercaoBanco);
        System.out.println("Inserindo dados de plantações lidos no Banco de dados...");

        try {
            conectarComBanco().inserirPlantacoesNoBanco(plantacoes);
            Log logSucesso = new Log("OK", aplicacao + " ", LocalDateTime.now(), " Plantações inseridas com sucesso no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logSucesso);
            System.out.println("Inserções encerradas");
            registerSuccess();
        } catch (Exception e) {
            Log logFalha = new Log("ERRO", aplicacao + " ", LocalDateTime.now(), "Falha ao inserir plantações no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logFalha);
            registerFalied();
            throw e;
        }
    }

    public void inserirClimasNobanco(List<Clima> climas) {
        Log log = new Log("OK", aplicacao, LocalDateTime.now(), "Inserindo dados de clima lidos no Banco de dados...");
//        conectarComBanco().inserirLogNoBanco(log);
        System.out.println("Inserindo dados de clima lidos no Banco de dados...");

        try {
            conectarComBanco().inserirClimasNoBanco(climas);
            Log logSucesso = new Log("OK", aplicacao + " ", LocalDateTime.now(), "Climas inseridos com sucesso no banco de dados");
            System.out.println("Climas inseridos com sucesso no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logSucesso);
            registerSuccess();
        } catch (Exception e) {
            Log logFalha = new Log("ERRO", aplicacao + " ", LocalDateTime.now(), "Falha ao inserir climas no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logFalha);
            registerFalied();
            throw e;
        }
    }

    public void inserirEstadoMunicipioNoBanco(List<EstadoMunicipio> estadoMunicipios) {
        Log log = new Log("OK", aplicacao, LocalDateTime.now(), "Inserindo dados de EstadoMunicipios lidos no Banco de dados...");
//        conectarComBanco().inserirLogNoBanco(log);
        System.out.println("Inserindo dados de EstadoMunicipios lidos no Banco de dados...");

        try {
            Log logSucesso = new Log("OK", aplicacao + " ", LocalDateTime.now(), "EstadoMunicipios inseridos com sucesso no banco de dados");
            System.out.println("EstadoMunicipios inseridos com sucesso no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logSucesso);
            registerSuccess();
        } catch (Exception e) {
            Log logFalha = new Log("ERRO", aplicacao + " ", LocalDateTime.now(), "Falha ao inserir EstadoMunicipios no banco de dados");
//            conectarComBanco().inserirLogNoBanco(logFalha);
            registerFalied();
            throw e;
        }
    }

}
