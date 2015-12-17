package com.heart.servlet.common;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.heart.bean.HeartMsg;
import com.heart.dao.AgendaDao;

@WebServlet(name = "fetchMainAgendaServlet", urlPatterns = {"/fetchMainAgenda"})
public class FetchMainAgendaServlet extends HttpServlet {


	private static final long serialVersionUID = -3032688950514639063L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		try {
			String[] phones = request.getParameterValues("oldmanphone");
			
			ArrayList<HeartMsg> heartMsgs = new AgendaDao().getMainAgenda(phones);
			
			response.setContentType("text/xml;charset=UTF-8");  
			response.setHeader("Cache-Control", "no-cache");
			
			response.getWriter().write(JSONArray.fromObject(heartMsgs).toString());
		} catch (Exception e) {
			
            System.out.println("error in get main agenda" + e);
			
			response.getWriter().write("err");
		}
	}

	
	
}
