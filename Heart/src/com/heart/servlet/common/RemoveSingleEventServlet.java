package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.AgendaDao;

@WebServlet(name="removeSingleEventServlet", urlPatterns = {"/removeSingleEvent"})
public class RemoveSingleEventServlet extends HttpServlet {


	private static final long serialVersionUID = 4974464418768416126L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String _id = request.getParameter("_id");
		
		System.out.println(_id + " is removing");
		new AgendaDao().deleteSingleEvent(_id);
		
		response.getWriter().write("1");
	}

	
}
