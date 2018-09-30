package com.youtestonline.service.categories;

import com.youtestonline.service.base.BaseMongoDAO;

public class CategoriesDAO extends BaseMongoDAO {
	@Override
	protected String getCollectionName() {
		return "categories";
	}
	
}
