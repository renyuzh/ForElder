package com.heart.servlet.guardian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.heart.dao.GuardianDao;
import com.heart.service.GuardianService;

@WebServlet(name = "guardianLoginServlet", urlPatterns = {"/guardianLogin"})
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -2220922120185208308L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("run the login..");
		String pwd = request.getParameter("pwd");
		String phone = request.getParameter("phone");
		
		if(new GuardianDao().login(phone, pwd))
		{
			
			response.getWriter().write
			(JSONArray.fromObject(new GuardianService().initBinded(phone)).toString());
			
		}
		else {
			response.getWriter().write("err");
		}
		
	}
	
	

	
}
