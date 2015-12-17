package com.heart.servlet.common;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.heart.bean.HeartNews;
import com.heart.dao.NewsDao;


@WebServlet(name = "fetchNewsServlet", urlPatterns = {"/fetchNews"})
public class FetchNewsServlet extends HttpServlet {

	private static final long serialVersionUID = -4372552976665813081L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		long lastDate = Long.valueOf(request.getParameter("lastDate"));
		
		ArrayList<HeartNews> newsList =
				 new NewsDao().fetchNews(lastDate);
			
		response.getWriter().write(JSONArray.fromObject(newsList).toString());
		
	}

	
	
}
