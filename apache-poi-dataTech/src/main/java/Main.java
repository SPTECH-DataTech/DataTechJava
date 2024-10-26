import client.S3Provider;
import client.S3Service;
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
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //S3
        //Acessar bucket
        String bucketName = "bucket-data-tech";
        S3Service s3Service = new S3Service(new S3Provider().getS3Client(), bucketName);

       /* //Fazer upload do arquivo
        String uploadfilePath = "C:\\Users\\JOOJ\\AppData\\Desktop\\upload-bases\\base-de-dados-para-tratar.xlsx";
        s3Service.uploadFiles(uploadfilePath);
*/
        //Fazer upload do arquivo de clima
        String uploadfilePathClima = "C:\\Users\\sdssd\\Downloads\\bases-minas-1985\\$2a$10$GykUAnrS37vFzhNB8gywzef6VaMVb02fTcgIt8ZGbsl6XML48UTWy\\clima\\dados_83083_M_1985-01-01_1985-12-31.xlsx";


        s3Service.uploadFiles(uploadfilePathClima);


        //Fazer download do arquivo no bucket
        s3Service.listObjects();
        s3Service.downloadFiles();
        /*===================================================================================================================*/
        //Leitura

    /*    String nomeArquivo = "base-de-dados-para-tratar.xlsx";
*/
        String nomeArquivoClima = "C:\\Users\\sdssd\\Downloads\\bases-minas-1985\\downloadsdados_83083_M_1985-01-01_1985-12-31.xlsx";


     /*   // Carregando o arquivo excel
        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);
*/
        Path caminho = Path.of(nomeArquivoClima);
        InputStream arquivo = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo
        Leitor leitor = new Leitor();
        LeitorClima leitorClima = new LeitorClima();

      /*  List<Plantacao> plantacoes = leitor.extrairPlantacao(nomeArquivo, arquivo);*/
        List<Clima> climas = leitorClima.extrairClimas(nomeArquivoClima, arquivo);

        // Fechando o arquivo após a extração
        arquivo.close();
/*====================================================================================*/
        //BD
        System.out.println("Plantações extraídas com sucesso");
/*===============================*/
        System.out.println("Realizando conexão com o Banco de Dados...");
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        for (Clima clima : climas) {
            connection.update("INSERT INTO climaMunicipioDash (data, temperaturaMax, temperaturaMin, umidadeMedia) VALUES (?,?,?,?)",
                    clima.getDataMedicao(), clima.getMediaTemperaturaMaxima(), clima.getMediaTemperaturaMinima(), clima.getUmidadeAr()
                    );
        }

       /* System.out.println("Inserindo dados lidos no Banco de dados...");
        for (Plantacao plantacao : plantacoes) {
            connection.update("INSERT INTO plantacao (fk_municipio, ano, area_plantada, quantidade_colhida, valor_total_reais) VALUES (?,?,?,?,?)",
                    plantacao.getMunicipio(), plantacao.getAno(), plantacao.getAreaPlantada(), plantacao.getQuantidadeColhida(), plantacao.getValorReais());
        }*/
        System.out.println("Inserções encerradas");



    }

}
