import client.S3Provider;
import client.S3Service;
import datatech.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Leitor;
import processor.Plantacao;
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

        String bucketName = "bucket-data-tech";
        S3Service conexaoBucket = new S3Service(new S3Provider().getS3Client(), bucketName);

        String uploadfilePath = "C:\\Users\\JOOJ\\Documents\\bases-para-tratar\\base-de-dados-para-tratar.xlsx";
        conexaoBucket.uploadFiles(uploadfilePath);

        conexaoBucket.listObjects();
        conexaoBucket.downloadFiles();
        /*===================================================================================================================*/
        //Leitura

        String nomeArquivo = "base-de-dados-para-tratar.xlsx";

        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        Leitor leitor = new Leitor();
        List<Plantacao> plantacoes = leitor.extrairPlantacao(nomeArquivo, arquivo);

        arquivo.close();

        System.out.println("Plantações extraídas com sucesso");
/*===================================================================================================================*/
        //BD
        ConexaoBanco conexao = new ConexaoBanco();
        conexao.inserirPlantacoesNoBanco(plantacoes);
        Log log = new Log(aplicacao + " ", LocalDateTime.now(), " Plantações inseridas com sucesso no banco de dados");
    }
}
