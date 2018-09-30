package com.youtestonline.service.search;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.bson.Document;

import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.youtestonline.service.base.BaseMongoDAO;
import com.youtestonline.service.base.BaseService;

@WebServlet(name = "search", urlPatterns = { "/search/*" })
public class SerarchService extends BaseService{

	private static final long serialVersionUID = 7152601304443365772L;
	private static GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	
	private SearchDAO dao = new SearchDAO();
	
	public SerarchService() {
		super();
	}
	
	@Override
	public BaseMongoDAO getDAO() {
		return dao;
	}

	public void test(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		// read form data;
		System.out.println(req.getContentType());
		//String data = req.getParameter("audio");
		/*
		if(req.getParts() != null) {
			System.out.println("Multi Parts : " + req.getParts().size());
		}
		*/
		BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
		byte[] b = new byte[10000000];
		int len = bis.read(b);
		bis.close();
		System.out.println(len);
		
		long timeout = 1000*2; // 2 seconds
		System.out.println("Testing speech to text.");
		
		Document doc = new Document();
		doc.append("servlet", req.getServletPath());
		doc.append("Test", "Test Successful");
		
		YouTestGSpeechResponseListener listener = new YouTestGSpeechResponseListener(req, doc);
		duplex.addResponseListener(listener);
		
		// recognize speech
		//duplex.recognize(arg0, arg1);
		
		long start = System.currentTimeMillis();
		while(!listener.responseReceived) {
			if((System.currentTimeMillis() - start)>timeout) {
				System.out.println("unable to find any response. Timing out.");
				break;
			} else {
				System.out.println("waiting for search response.");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(listener.responseReceived) {
			System.out.println("search response received.");
		}
		duplex.removeResponseListener(listener);
		
		// prepare final response.
		System.out.println(doc.toJson());
		resp.getWriter().println(doc.toJson());
	}
}

class YouTestGSpeechResponseListener implements GSpeechResponseListener{
	HttpServletRequest req = null; 
	Document responseDoc = null;
	boolean responseReceived = false;
	
	public YouTestGSpeechResponseListener(HttpServletRequest req, Document responseDoc){
		this.req = req;
		this.responseDoc = responseDoc;
	}
	
	@Override
	public void onResponse(GoogleResponse gr) {
		responseDoc.append("responses", gr.getAllPossibleResponses());
		System.out.println(responseDoc.toJson());
		responseReceived = true;
	}
	
}
