package com.heart.servlet.common;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.heart.bean.RegularShare;
import com.heart.dao.MsgDao;

@WebServlet( name = "fetchMainShareServlet", urlPatterns = {"/fetchMainShare"})
public class FetchMainShareServlet extends HttpServlet {

	private static final long serialVersionUID = 5194263396414154474L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("run the fetchmainshare..");
		try {
			String[] oldmanphone = request.getParameterValues("oldmanPhone");
			
			ArrayList<RegularShare> regularShares =
					  new MsgDao().getLatestShare(oldmanphone);
			
			response.getWriter().write(JSONArray.fromObject(regularShares).toString());
		} catch (Exception e) {
            System.out.println("error in get main share");
			
			response.getWriter().write("err");
		}
		
	}

	
}
