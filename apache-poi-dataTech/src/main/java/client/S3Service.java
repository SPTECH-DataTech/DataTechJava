package client;

import commons.BancoConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import datatech.log.Log;
import writer.ConexaoBanco;

public class S3Service {
    private S3Client s3Client;
    private String bucketName;
    private String aplicacao = "conexao-bucket";
    BancoConnection conexao = new BancoConnection();


    public S3Service(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void createBucket() {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            Log log = new Log(this.aplicacao, LocalDate.now(), "Bucket criado com sucesso: " + bucketName);
            conexao.inserirLogNoBanco(log);

        } catch (S3Exception e) {
            Log log = new Log(this.aplicacao, LocalDate.now(), "Erro ao criar o bucket: " + e.getMessage() + bucketName);
        }
    }

    public void uploadFiles(String filePath) {
        try {
            File file = new File(filePath);
            String fileName = file.getName();

//            String uniqueFileName = UUID.randomUUID().toString() + ".txt";
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            System.out.println();
            Log log = new Log(this.aplicacao, LocalDate.now(), "Arquivo '" + file.getName() + "' enviado com sucesso com o nome: ");
            conexao.inserirLogNoBanco(log);
        } catch (S3Exception e) {
            System.err.println();
            Log log = new Log(this.aplicacao, LocalDate.now(), "Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }

    public void listObjects() {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            System.out.println("Objetos no bucket " + bucketName + ":");
            for (S3Object s3Object : objects) {
                System.out.println("- " + s3Object.key());
            }

        } catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
        }
    }

    public void downloadFiles() {
        try {
            String diretorioDownload = "C:\\GitHub\\DataTechJava\\";
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket("bucket-data-tech")
                        .key(object.key())
                        .build();

                String filePath = diretorioDownload + object.key();

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                Files.copy(inputStream, new File(filePath).toPath());
                System.out.println("Arquivo baixado: " + object.key());
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }
    }
}
