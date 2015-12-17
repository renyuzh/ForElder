package com.heart.servlet.guardian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.OldManDao;

import net.sf.json.JSONObject;

@WebServlet(name = "searchingUnBindServlet", urlPatterns = {"/searchingUnBind"})
public class SearchingUnBindServlet extends HttpServlet {

	private static final long serialVersionUID = -1155223600644183802L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String searchingText = request.getParameter("searchingPhone");
		
		JSONObject jsonObject = JSONObject.fromObject(new OldManDao().searchForUnBindOldByPhone(searchingText));
		
		response.getWriter().write(jsonObject.toString());
		
	}

	
	
}
