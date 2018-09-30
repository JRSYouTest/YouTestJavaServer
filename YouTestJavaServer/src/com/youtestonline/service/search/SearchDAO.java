package com.youtestonline.service.search;

import com.youtestonline.service.base.BaseMongoDAO;

public class SearchDAO extends BaseMongoDAO {
	@Override
	protected String getCollectionName() {
		return "searchterms";
	}
}
