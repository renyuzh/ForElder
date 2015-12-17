package com.heart.servlet.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.heart.bean.CodeResult;
import com.jspsmart.upload.SmartUpload;

@WebServlet(name= "uploadfileServlet", urlPatterns = {"/uploadFile"})
public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -2232036220504177006L;
	public static final String DIAN = ".";

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("run the uploding...");
		

		ArrayList<CodeResult> codeResults = new ArrayList<>();
		
		SmartUpload smartUpload = new SmartUpload();
		smartUpload.initialize(this.getServletConfig(), request, response);
		smartUpload.setDenyPhysicalPath(true);
		smartUpload.setMaxFileSize(1024 * 1024 * 10);// 设置允许上传文件最大为10M

		try {
			smartUpload.upload();			

			for (int i = 0; i < smartUpload.getFiles().getCount(); i++) {
				com.jspsmart.upload.File myFile = smartUpload.getFiles()
						.getFile(i);
				if (myFile.isMissing()) {
					continue;
				}

				String filePath = "/heartUpload/";
				filePath += UUID.randomUUID().toString() + DIAN
						+ myFile.getFileExt();

				myFile.saveAs(filePath, SmartUpload.SAVE_VIRTUAL);
                
				CodeResult codeResult = new CodeResult();
				codeResult.setValidValue(0);
				codeResult.setInfo(filePath);
				
				codeResults.add(codeResult);
				
			}
			
			JSONArray array = JSONArray.fromObject(codeResults);
			System.out.println("upload file : " + array.toString());
			response.getWriter().write(array.toString());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			CodeResult codeResult = new CodeResult();
			codeResult.setValidValue(1);
			codeResult.setInfo("");
			JSONObject jsonObject = JSONObject.fromObject(codeResult);
			response.getWriter().write(jsonObject.toString());
			
		}

		

	}

}
