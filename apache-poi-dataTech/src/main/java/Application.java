import client.S3Provider;
import client.S3Service;
import datatech.log.Log;
import processor.plantacao.LeitorPlantacao;
import processor.plantacao.Plantacao;
import processor.clima.Clima;
import processor.clima.LeitorClima;
import processor.estadoMunicipio.EstadoMunicipio;
import processor.estadoMunicipio.LeitorEstadoMunicipio;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Log> logs = new ArrayList<Log>();
    String aplicacao = "Main";

    public ConexaoBanco conectarComBanco() {
        return new ConexaoBanco();
    }

    public S3Service conectarComBucket() {
        String bucketName = "bucket-data-tech";
        return new S3Service(new S3Provider().getS3Client(), bucketName);
    }

    public void baixarArquivosS3(S3Service conexaoBucket) {
        conexaoBucket.listObjects();
        conexaoBucket.downloadFiles();
    }

    public List<Plantacao> lerArquivoPlantacoes() throws IOException {
        String nomeArquivoPlantacoes = "download-basesdownload-basesbase-de-dados-para-tratar-com-tipo.xlsx";

        Path caminhoPlantacoes = Path.of(nomeArquivoPlantacoes);
        InputStream arquivoPlantacoes = Files.newInputStream(caminhoPlantacoes);

        LeitorPlantacao leitorPlantacao = new LeitorPlantacao();

        List<Plantacao> plantacoes = leitorPlantacao.extrairDados(caminhoPlantacoes, arquivoPlantacoes);

        arquivoPlantacoes.close();

        Log logExtracaoBase = new Log(aplicacao, LocalDateTime.now(), "Plantações extraídas com sucesso");
        conectarComBanco().inserirLogNoBanco(logExtracaoBase);
        System.out.println("Plantações extraídas com sucesso");

        return plantacoes;
    }

    public List<Clima> lerArquivoClima() throws IOException {
        String nomeArquivoClima = "download-basesdados_83083_M_1985-01-01_1985-12-31.xlsx";

        Path caminhoClima = Path.of(nomeArquivoClima);
        InputStream arquivoClima = Files.newInputStream(caminhoClima);

        LeitorClima leitorClima = new LeitorClima();
        List<Clima> climas = leitorClima.extrairDados(caminhoClima, arquivoClima);

        Log logExtracaoBase = new Log(aplicacao, LocalDateTime.now(), "Climas registrados com sucesso");
        conectarComBanco().inserirLogNoBanco(logExtracaoBase);
        System.out.println("Climas registrados com sucesso");

        return climas;
    }

    public List<EstadoMunicipio> lerArquivoEstadoMunicipio() throws IOException {
        String nomeArquivoEstadoMunicipio = "Base-de-Dados-Municipios-_Editada_.xlsx";

        Path caminhoEstadoMunicipio = Path.of(nomeArquivoEstadoMunicipio);
        InputStream arquivoEstadoMunicipio = Files.newInputStream(caminhoEstadoMunicipio);

        LeitorEstadoMunicipio leitorEstadoMunicipio = new LeitorEstadoMunicipio();
        List<EstadoMunicipio> estadoMunicipios = leitorEstadoMunicipio.extrairDados(caminhoEstadoMunicipio, arquivoEstadoMunicipio);

        Log logExtracaoBase = new Log(aplicacao, LocalDateTime.now(), "EstadoMunicipios registrados com sucesso");
        conectarComBanco().inserirLogNoBanco(logExtracaoBase);
        System.out.println("EstadoMunicipios registrados com sucesso");

        return estadoMunicipios;
    }

    public void inserirPlantacoesNoBanco(List<Plantacao> plantacoes) {
        Log logInsercaoBanco = new Log(aplicacao, LocalDateTime.now(), "Inserindo dados de plantações lidos no Banco de dados...");
        conectarComBanco().inserirLogNoBanco(logInsercaoBanco);
        System.out.println("Inserindo dados de plantações lidos no Banco de dados...");

        try {
            conectarComBanco().inserirPlantacoesNoBanco(plantacoes);
            Log logSucesso = new Log(aplicacao + " ", LocalDateTime.now(), " Plantações inseridas com sucesso no banco de dados");
            conectarComBanco().inserirLogNoBanco(logSucesso);
            System.out.println("Inserções encerradas");
        } catch (Exception e) {
            Log logFalha = new Log(aplicacao + " ", LocalDateTime.now(), "Falha ao inserir plantações no banco de dados");
            conectarComBanco().inserirLogNoBanco(logFalha);
            throw e;
        }
    }

    public void inserirClimasNobanco(List<Clima> climas) {
        Log log = new Log(aplicacao, LocalDateTime.now(), "Inserindo dados de clima lidos no Banco de dados...");
        conectarComBanco().inserirLogNoBanco(log);
        System.out.println("Inserindo dados de clima lidos no Banco de dados...");

        try {
            conectarComBanco().inserirClimasNoBanco(climas);
            Log logSucesso = new Log(aplicacao + " ", LocalDateTime.now(), "Climas inseridos com sucesso no banco de dados");
            System.out.println("Climas inseridos com sucesso no banco de dados");
            conectarComBanco().inserirLogNoBanco(logSucesso);
        } catch (Exception e) {
            Log logFalha = new Log(aplicacao + " ", LocalDateTime.now(), "Falha ao inserir climas no banco de dados");
            conectarComBanco().inserirLogNoBanco(logFalha);
            throw e;
        }
    }

    public void inserirEstadoMunicipioNoBanco(List<EstadoMunicipio> estadoMunicipios) {
        Log log = new Log(aplicacao, LocalDateTime.now(), "Inserindo dados de EstadoMunicipios lidos no Banco de dados...");
        conectarComBanco().inserirLogNoBanco(log);
        System.out.println("Inserindo dados de EstadoMunicipios lidos no Banco de dados...");

        try {
            conectarComBanco().inserirEstadoMunicipioNoBanco(estadoMunicipios);
            Log logSucesso = new Log(aplicacao + " ", LocalDateTime.now(), "EstadoMunicipios inseridos com sucesso no banco de dados");
            System.out.println("EstadoMunicipios inseridos com sucesso no banco de dados");
            conectarComBanco().inserirLogNoBanco(logSucesso);
        } catch (Exception e) {
            Log logFalha = new Log(aplicacao + " ", LocalDateTime.now(), "Falha ao inserir EstadoMunicipios no banco de dados");
            conectarComBanco().inserirLogNoBanco(logFalha);
            throw e;
        }
    }

}
