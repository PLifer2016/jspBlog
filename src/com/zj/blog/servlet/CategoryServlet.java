package com.zj.blog.servlet;

import java.io.IOException;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.zj.blog.model.CategoryBean;
import com.zj.blog.model.CategoryBeanBO;
import com.zj.blog.model.UserBean;

public class CategoryServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//��ֹ�Ƿ���¼
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null){
			request.getRequestDispatcher("/admin/login.jsp").forward(request,response);
		}else{
			
			request.setCharacterEncoding("utf-8");
			//�������һ�����������������ĸ���������add����delete...
			String type = request.getParameter("type");
			String id = request.getParameter("id");
			String categoryName = request.getParameter("categoryName");
			String level = request.getParameter("level");
			CategoryBeanBO cbb = new CategoryBeanBO();
			int result = 0;
			String message="";
			if(type.equals("add")){
				cbb.add(categoryName, level);
				if (result == 1) {
					message = "��ϲ����ӷ���ɹ���";
				} else {
					message = "Sorry!��ӷ���ʧ�ܣ�";
				}
				request.setAttribute("message", message);
				request.getRequestDispatcher("/admin/showResult.jsp").forward(request,response);
				
			}else if(type.equals("delete")){
				cbb.delete(id);
				request.getRequestDispatcher("/servlet/CategoryServlet?type=display").forward(request, response);
			}else if(type.equals("display")){
				List list = cbb.display();
				request.setAttribute("list",list);
				request.getRequestDispatcher("/admin/adminCategoryList.jsp").forward(request, response);
			}else if(type.equals("modify")){
				CategoryBean cg = cbb.modify(id);
				request.setAttribute("category", cg);
				request.getRequestDispatcher("/admin/modifyCategory.jsp").forward(request, response);
			}else if(type.equals("postModify")){
				cbb.postModify(categoryName, level, id);
				request.getRequestDispatcher("/servlet/CategoryServlet?type=display").forward(request,response);
			}
			
		}
	
	}
	
}
