import client.S3Provider;
import client.S3Service;
import org.springframework.jdbc.core.JdbcTemplate;
import writer.ConexaoBanco;

public class Main {

    public static void main(String[] args) {
        S3Service s3Service = new S3Service(new S3Provider().getS3Client(), "bucket-data-tech");


  /*s3Service.uploadFiles("C:/Users/sdssd/Desktop/file.txt");*/
        s3Service.listObjects();
        s3Service.downloadFiles();
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        connection.update("""
                INSERT INTO dataTech.usuario (cpf, nome, email, senha, fk_empresa) values
                ("12345678901", "teste-conexao", "teste@gmail", "minhasenha123", 1);
                """);
    }

}
