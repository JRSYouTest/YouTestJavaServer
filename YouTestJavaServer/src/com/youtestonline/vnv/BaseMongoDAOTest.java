package com.youtestonline.vnv;

import java.util.List;

import org.bson.Document;

import com.youtestonline.service.base.BaseMongoDAO;

public class BaseMongoDAOTest {

	public static void main(String[] args) {
		BaseMongoDAO bmd = new BaseMongoDAO() {@Override
		protected String getCollectionName() {
			return "questions";
		}};
		List<Document> docs = bmd.findDocuments(null, null);
		for(Document doc: docs) {
			System.out.println(doc.toJson());
		}
	}

}
