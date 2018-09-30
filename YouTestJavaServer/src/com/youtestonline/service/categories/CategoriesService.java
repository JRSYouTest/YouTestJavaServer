package com.youtestonline.service.categories;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.youtestonline.service.base.BaseMongoDAO;
import com.youtestonline.service.base.BaseService;

/**
 * Servlet implementation class CategoriesService
 */
@WebServlet(name = "categories", urlPatterns = { "/categories/*" })
public class CategoriesService extends BaseService {
	private static final long serialVersionUID = 1L;
	
	private CategoriesDAO dao = new CategoriesDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesService() {
        super();
    }

    @Override
    public BaseMongoDAO getDAO() {
    	return dao;
    }


}
