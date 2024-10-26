import client.S3Provider;
import client.S3Service;
import commons.BancoConnection;
import datatech.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Leitor;
import processor.Plantacao;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Log> logs = new ArrayList<Log>();

        //S3
        //Acessar bucket
        String bucketName = "bucket-data-tech";
        S3Service s3Service = new S3Service(new S3Provider().getS3Client(), bucketName);

        //Fazer upload do arquivo
        String uploadfilePath = "C:\\Users\\vini_\\Desktop\\upload-bases\\base-de-dados-para-tratar.xlsx";
        s3Service.uploadFiles(uploadfilePath);

        //Fazer download do arquivo no bucket
        s3Service.listObjects();
        s3Service.downloadFiles();
        /*===================================================================================================================*/
        //Leitura

        String nomeArquivo = "base-de-dados-para-tratar.xlsx";

        // Carregando o arquivo excel
        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo
        Leitor leitor = new Leitor();
        List<Plantacao> plantacoes = leitor.extrairPlantacao(nomeArquivo, arquivo);

        // Fechando o arquivo após a extração
        arquivo.close();

        System.out.println("Plantações extraídas com sucesso");
/*===================================================================================================================*/
        //BD
        System.out.println("Realizando conexão com o Banco de Dados...");
        JdbcTemplate conexao = new BancoConnection().gerarNovaConeexao();

        System.out.println("Inserindo dados lidos no Banco de dados...");
        for (Plantacao plantacao : plantacoes) {
            conexao.update("INSERT INTO plantacao (fk_municipio, ano, area_plantada, quantidade_colhida, valor_total_reais) VALUES (?,?,?,?,?)",
                    plantacao.getMunicipio(), plantacao.getAno(), plantacao.getAreaPlantada(), plantacao.getQuantidadeColhida(), plantacao.getValorReais());
        }
        System.out.println("Inserções encerradas");
    }

}
