package com.youtestonline.service.base;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class BaseMongoDAO {

	protected static final String HOST 		= "localhost";
	protected static final int PORT 		= 27017;
	protected static final String DATABASE 	= "YouTest";
	
	protected MongoClient client = null;
	
	abstract protected String getCollectionName();
	
	public List<Document> loadAll(){
		return findDocuments(null, null);
	}

	public List<Document> findDocuments(Document condition,Document options ) {
		MongoCollection<Document> coll = getCollection(getCollectionName());
		FindIterable<Document> result = null;
		List<Document> docs = null;
		
		if(condition == null) {
			result = coll.find();
		} else {
			if(options != null) {
				
			} else {
				result = coll.find(condition);
			}
		}
		if(result != null) {
			docs = (List<Document>)result.into(new ArrayList<Document>());
		}
		return docs;
	}
	
	protected MongoClient getClient() {
		if(client == null || client.isLocked()) {
			client = new MongoClient( HOST, PORT ); 
		      // Creating Credentials 
		      /*
		      MongoCredential credential; 
		      credential = MongoCredential.createCredential("sampleUser", "myDb", 
		         "password".toCharArray()); 
		      System.out.println("Connected to the database successfully");  
		      */
		}
		return client;
	}

	protected void closeClient() {
		if(client != null && client.isLocked() == false) {
			client.close(); 
		}
	}

	protected MongoDatabase getDatabase() {
	      MongoClient client = getClient(); 
	      
	      //Accessing the database 
	      MongoDatabase database = client.getDatabase(DATABASE);
	      return database;
	}
	
	protected MongoCollection<Document> getCollection(String name) {
	      //Accessing the database 
	      MongoDatabase database = getDatabase();  
	      
	      //Creating a collection 
	      
	      MongoCollection<Document> collection = database.getCollection(name);
	      if(collection == null){
	          database.createCollection(name); 
	          System.out.println(name + " : Collection created successfully"); 
	      } else {
	          System.out.println("Loaded Collection : " + name); 
	      }
	      return collection;
	}
}
