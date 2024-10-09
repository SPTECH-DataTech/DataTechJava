package client;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class S3Service {
    private S3Client s3Client;
    private String bucketName;

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
            System.out.println("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Erro ao criar o bucket: " + e.getMessage());
        }
    }

    public void uploadFiles(String filePath){
       try {
           File file = new File(filePath);
           /*String fileName = file.getName();*/

           String uniqueFileName = UUID.randomUUID().toString();
           PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                   .bucket(bucketName)
                   .key(uniqueFileName)
                   .build();

           s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));


           System.out.println("Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + uniqueFileName);
       } catch (S3Exception e) {
           System.err.println("Erro ao fazer upload do arquivo: " + e.getMessage());
       }
    }

    public void listObjects()   {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            System.out.println("Objetos no bucket " + bucketName + ":");
            for (S3Object s3Object : objects){
                System.out.println("- " + s3Object.key());
            }

        }
        catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
        }
    }
}