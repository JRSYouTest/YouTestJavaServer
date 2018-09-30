package com.youtestonline.service.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.BasicDBList;

public abstract class BaseService extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected final String CT = "Content-Type";
	protected final String CT_JSON = "application/json";
	protected final String CT_HTML = "text/html";
	protected final String KEY_ERROR = "error";
	
	protected Map<String, String> apiMappings = new HashMap<>();
	
	
	public BaseService(){
		addApiMapping(null, "test");
		addApiMapping("/", "test");
        addApiMapping("/load", "load");
        addApiMapping("/save", "save");
	}
	
	abstract public BaseMongoDAO getDAO();

	protected void addApiMapping(String url, String methodName ) {
		apiMappings.put(url, methodName);
	}
	
	protected String getMappedAPI(String url) {
		if(apiMappings.containsKey(url)) {
			return apiMappings.get(url);
		}
		return null;
	}
	
	protected void setJSONHeader(HttpServletResponse res) {
		res.setHeader(CT, CT_JSON);
		
		enableCOR(res);
	}

	protected void enableCOR(HttpServletResponse res) {
		// COR headers...
		res.setHeader("Access-Control-Allow-Origin" , "*");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		res.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
	}
	
	protected Document getRequestData(HttpServletRequest req) throws IOException {
		String line = null;
    	StringBuffer sb = new StringBuffer();
    	while((line = req.getReader().readLine()) != null) {
    		sb.append(line + "\n");
    	}
    	Document doc = new Document();
    	if(!sb.toString().isEmpty()) {
        	doc = Document.parse(sb.toString());
    	}
		return doc;
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setJSONHeader(resp); // by default - JSON response.
		
		Document respDoc = new Document();
		respDoc.append("servlet", req.getServletPath());
		respDoc.append("Class" , this.getClass().getName());
		
		String apiPath = req.getPathInfo();
		String apiName = getMappedAPI(apiPath);
		if(apiName != null) {
			Method apiMeth;
			try {
				apiMeth = this.getClass().getMethod(apiName, HttpServletRequest.class, HttpServletResponse.class);
				if(apiMeth != null) {
					apiMeth.invoke(this, req, resp);
					return;
				} else {
					respDoc.append(KEY_ERROR, "Couldn't find a valid API mapped to path : " + apiPath);
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				PrintWriter pw = new PrintWriter(new StringWriter());
				e.printStackTrace(pw);
				respDoc.append(KEY_ERROR, pw.toString());
			}
		} else {
			respDoc.append(KEY_ERROR, "Couldn't find a valid API mapped to path : " + apiPath);
		}
		
		resp.getWriter().println(respDoc.toJson());
	}
	
	public void test(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Document doc = new Document();
		doc.append("servlet", req.getServletPath());
		doc.append("Test", "Test Successful");
		
		resp.getWriter().println(doc.toJson());
	}

    public void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Document doc = getRequestData(req);
    	System.out.println(doc.toJson());
    	resp.getWriter().write(doc.toJson());
    }

    public void load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// input
    	Document doc = getRequestData(req);
    	System.out.println(doc.toJson());
    	Document selector = (Document)doc.get("selector");
    	// output
    	BasicDBList dbList = new BasicDBList();
    	if(selector == null) {
        	dbList.addAll(getDAO().loadAll());
    	} else {
        	dbList.addAll(getDAO().findDocuments(selector, null));
    	}
    	resp.getWriter().write(dbList.toString());
    }
}
