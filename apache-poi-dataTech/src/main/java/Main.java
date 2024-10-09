import client.S3Provider;
import client.S3Service;

public class Main {

    public static void main(String[] args) {
        S3Service s3Service = new S3Service(new S3Provider().getS3Client(), "bucket-data-tech");

        s3Service.createBucket();
        s3Service.uploadFiles("C:/Users/sdssd/Desktop/file.txt");
        s3Service.listObjects();
    }

}
