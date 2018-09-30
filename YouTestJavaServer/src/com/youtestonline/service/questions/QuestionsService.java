package com.youtestonline.service.questions;

import javax.servlet.annotation.WebServlet;

import com.youtestonline.service.base.BaseMongoDAO;
import com.youtestonline.service.base.BaseService;

/**
 * Servlet implementation class QuestionService
 */
@WebServlet(name = "questions", urlPatterns = { "/questions/*" })
public class QuestionsService extends BaseService{
	private static final long serialVersionUID = 1L;

	private QuestionsDAO dao = new QuestionsDAO();

	public QuestionsService(){
        super();
    }

	@Override
	public BaseMongoDAO getDAO() {
		return dao;
	}

}
