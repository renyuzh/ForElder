package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.heart.bean.RegularPosition;
import com.heart.dao.RegularPositionDao;

@WebServlet(name = "fetchLatestPositionServlet", urlPatterns = {"/fetchLatestPosition"})
public class FetchLatestPositionServlet extends HttpServlet {

	private static final long serialVersionUID = -7951229397993101416L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String oldmanPhone = request.getParameter("oldmanPhone");
		
		RegularPosition position = new RegularPositionDao().getHeartPositionMsgWithWeatherByOldManPhone(oldmanPhone);
		
		response.getWriter().write(JSONObject.fromObject(position).toString());
		
	}

	
	
}
