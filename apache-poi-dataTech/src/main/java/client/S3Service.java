package client;

import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import datatech.log.Log;
import writer.ConexaoBanco;

public class S3Service {
    private S3Client s3Client;
    private String bucketName;
    private String aplicacao = "conexao-bucket";
    ConexaoBanco conexao = new ConexaoBanco();
    private List<Log> logs = new ArrayList<>();

    public S3Service(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void createBucket() throws IOException {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            Log log = new Log("OK", this.aplicacao + " ", LocalDateTime.now(), " Bucket criado com sucesso: " + bucketName);
            logs.add(log);
//            conexao.inserirLogNoBanco(log);

        } catch (S3Exception e) {
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), " Erro ao criar o bucket: " + bucketName);
            System.out.println(log);
            logs.add(log);
//            conexao.inserirLogNoBanco(log);
        }
    }

    public void uploadFiles(String filePath) throws IOException {
        try {
            File file = new File(filePath);
            String fileName = file.getName();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            Log log = new Log("OK", this.aplicacao + " ", LocalDateTime.now(), " Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + fileName);
            System.out.println(log);
            logs.add(log);
            conexao.inserirLogNoBanco(log);
        } catch (S3Exception e) {
            System.err.println();
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), " Erro ao fazer upload do arquivo: " + filePath);
            System.out.println(log);
            logs.add(log);
//            conexao.inserirLogNoBanco(log);
        }
    }

    public void listObjects() throws IOException {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();

            for (S3Object s3Object : objects) {
                System.out.println("- " + s3Object.key());
            }
        } catch (S3Exception e) {
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), " Erro ao listar objetos no bucket: ");
            System.err.println(" Erro ao listar objetos no bucket: ");
            logs.add(log);
//            conexao.inserirLogNoBanco(log);
        }
    }

    public void downloadFiles() throws IOException {
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
                Log log = new Log("OK", this.aplicacao + " ", LocalDateTime.now(), " Arquivo baixado: " + object.key());
                System.out.println("Arquivo baixado: " + object.key());
                logs.add(log);
//                conexao.inserirLogNoBanco(log);
            }
        } catch (IOException | S3Exception e) {
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), " Erro ao fazer download dos arquivos: ");
            System.err.println("Erro ao fazer download dos arquivos");
            logs.add(log);
//            conexao.inserirLogNoBanco(log);
        }
    }
}
