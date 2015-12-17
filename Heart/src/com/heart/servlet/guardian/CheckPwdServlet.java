package com.heart.servlet.guardian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.GuardianDao;
import com.heart.util.GenerateCode;
import com.heart.util.SendCodeMsg;

@WebServlet(name = "checkPwdServlet", urlPatterns = {"/checkpwd"})
public class CheckPwdServlet extends HttpServlet {

	private static final long serialVersionUID = 2610437568674913668L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		
	    if(new GuardianDao().login(phone, pwd))
	    {
	    	
	    	String code = GenerateCode.involk();
	    	
	    	String result = SendCodeMsg.sendMsg(phone, code);

	    	if(result.startsWith("100"))
	    	{
	    		response.getWriter().write(code);
	    	}
	    	else {
	    		response.getWriter().write("error");
			}
	    	
	    	
	    }else {
			response.getWriter().write("error");
		}
	}

	
	
}
