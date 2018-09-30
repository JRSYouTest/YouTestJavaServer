package com.youtestonline.service.questions;

import com.youtestonline.service.base.BaseMongoDAO;

public class QuestionsDAO extends BaseMongoDAO {
	@Override
	protected String getCollectionName() {
		return "questions";
	}
}
