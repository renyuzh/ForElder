package com.heart.servlet.oldman;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.service.OldManService;

@WebServlet(name = "oldManRegisterServlet", urlPatterns = { "/oldmanRegister" })
public class Register extends HttpServlet {

	
	private static final long serialVersionUID = 6712611403673237630L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		
		if(new OldManService().register(phone, pwd, name))
		{
			writer.write("ok");
		}
		else {
			writer.write("err");
		}
		
		writer.close();
	}

	
}
