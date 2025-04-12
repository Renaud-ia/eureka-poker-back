package fr.eurekapoker.parties.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class PersistanceS3 implements PersistanceFichiers {
    private static final Logger logger = LoggerFactory.getLogger(PersistanceS3.class);
    private static final String S3_BUCKET_NAME = System.getenv("S3_BUCKET_NAME");
    private static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");
    private static final String AWS_SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
    private static final String AWS_REGION = System.getenv("AWS_REGION");

    private AmazonS3 s3Client;

    public PersistanceS3() {
        if (S3_BUCKET_NAME == null || AWS_ACCESS_KEY_ID == null || AWS_SECRET_ACCESS_KEY == null) {
            return;
        }

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Override
    public void enregistrerFichier(String contenuFichier) {
        if (S3_BUCKET_NAME == null || contenuFichier == null || contenuFichier.isEmpty()) {
            logger.warn("Conf non disponible, le fichier ne sera pas persisté");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String datePath = sdf.format(new Date());

        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = timestamp + ".txt";

        String s3Key = datePath + "/" + fileName;

        InputStream contentStream = new ByteArrayInputStream(contenuFichier.getBytes());

        try {
            s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3Key, contentStream, null));
            logger.info("Fichier enregistré sous {}", s3Key);
        }

        catch (Exception e) {
            logger.error("Impossible d'enregistrer le fichier{}", String.valueOf(e));
        }
    }
}
