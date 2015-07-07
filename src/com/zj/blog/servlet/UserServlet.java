package com.zj.blog.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.zj.blog.model.UserBean;
import com.zj.blog.model.UserBeanBO;

public class UserServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String type = request.getParameter("type");
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String reNewPassword = request.getParameter("reNewPassword");
		UserBeanBO ub = new UserBeanBO();
		UserBean user = null;
		HttpSession session = null;
		String message="";
		if(type==null){
			type = "";
		}
		if(type.equals("login")){
			List list = ub.login(userName, password);
			if(list != null && list.size()>0){
				user = (UserBean)list.get(0);
				session = request.getSession();
				session.setAttribute("user", user);
				request.getRequestDispatcher("/servlet/AdminBlogServlet?type=admin").forward(request, response);
			}else{
				message = "�û�������������������µ�¼��";
				request.setAttribute("message",message);
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
		}else if(type.equals("logOut")){
			session.invalidate();
			response.sendRedirect("/blog/index.jsp");//������ҪЩ����·��
		}else if(type.equals("change")){
			session = request.getSession();
			user = (UserBean)session.getAttribute("user");
			if(!user.getPassword().equals(oldPassword)){
				request.setAttribute("message","Sorry���������ԭ���벻��ȷ��");
				request.getRequestDispatcher("/admin/showResult.jsp").forward(request, response);
			}else if(!newPassword.equals(reNewPassword)){
				request.setAttribute("message","Sorry���������������벻��ͬ��");
				request.getRequestDispatcher("/admin/showResult.jsp").forward(request, response);
			}else{
				
				String id = request.getParameter("id");
				int result = ub.changePassword(newPassword,id);
				if(result>0){
					request.setAttribute("message","�޸ĳɹ���");
				}else{
					request.setAttribute("message","�޸ĳɹ���");
				}
				request.getRequestDispatcher("/admin/showResult.jsp").forward(request, response);
			}
			
		}
	}
}
