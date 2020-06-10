package gg.scenarios.terra.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Reference;
import lombok.Getter;
import org.bson.Document;

@Getter
public class Mongo {

    private MongoClient client = null;
    private MongoDatabase database = null;

    private MongoCollection<Document> stats;
    private MongoCollection<Document> profiles;
    private MongoClientURI uri;
    private Terra terra;
    private Reference reference;

    public Mongo(Terra terra) {
        this.terra = terra;
        this.reference = terra.getReference();

        uri = new MongoClientURI(reference.getUri());
        try {
            client = new MongoClient(uri);
        }catch (Exception e){
            System.out.println("Error loading Mongo, !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!");
        }
        if (client != null){
            database = client.getDatabase(reference.getMongoDb());
            stats = database.getCollection(reference.getMongoStats());
            profiles = database.getCollection(reference.getMongoProfile());
        }
    }
}
