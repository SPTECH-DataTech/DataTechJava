package client;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import datatech.log.Log;
import writer.ConexaoBanco;

public class S3Service {
    private S3Client s3Client;
    private String bucketName;
    private String aplicacao = "conexao-bucket";
    ConexaoBanco conexao = new ConexaoBanco();


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
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Bucket criado com sucesso: " + bucketName);
            System.out.println(log);
            conexao.inserirLogNoBanco(log);

        } catch (S3Exception e) {
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Erro ao criar o bucket: " + e.getMessage() + bucketName);
            System.out.println(log);
            conexao.inserirLogNoBanco(log);
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

            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + fileName);
            System.out.println(log);
            conexao.inserirLogNoBanco(log);
        } catch (S3Exception e) {
            System.err.println();
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Erro ao fazer upload do arquivo: " + filePath + e.getMessage());
            System.out.println(log);
            conexao.inserirLogNoBanco(log);
        }
    }

    public void listObjects() {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();

            String descricaoLog = "\" Objetos no bucket \" + bucketName + \":\"";
            for (S3Object s3Object : objects) {
                descricaoLog += ("- " + s3Object.key() + "\n");
                System.out.println("- " + s3Object.key());
            }
            Log log = new Log(this.aplicacao,LocalDateTime.now(), descricaoLog);
            System.out.println(log);
            conexao.inserirLogNoBanco(log);

        } catch (S3Exception e) {
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Erro ao listar objetos no bucket: " + e.getMessage());
            System.err.println(" Erro ao listar objetos no bucket: " + e.getMessage());
            conexao.inserirLogNoBanco(log);
        }
    }

    public void downloadFiles() {
        try {
            String diretorioDownload = "./download-bases";
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket("bucket-data-tech")
                        .key(object.key())
                        .build();

                String filePath = diretorioDownload + object.key();

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                Files.copy(inputStream, new File(filePath).toPath());
                Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Arquivo baixado: " + object.key());
                System.out.println("Arquivo baixado: " + object.key());
                conexao.inserirLogNoBanco(log);
            }
        } catch (IOException | S3Exception e) {
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), " Erro ao fazer download dos arquivos: " + e.getMessage());
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
            conexao.inserirLogNoBanco(log);
        }
    }
}
