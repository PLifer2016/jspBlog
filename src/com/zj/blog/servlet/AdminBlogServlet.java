package com.zj.blog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.zj.blog.model.BlogBean;
import com.zj.blog.model.BlogBeanBO;
import com.zj.blog.model.CategoryBean;
import com.zj.blog.model.CategoryBeanBO;
import com.zj.blog.model.DBhelper;
import com.zj.blog.model.UserBean;

public class AdminBlogServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ��ֹ�Ƿ���¼
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		// ��ֹ���룬�ڽ���֮ǰ��ת��UTF-8
		request.setCharacterEncoding("utf-8");
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String titleIntro = request.getParameter("titleIntro");
		String content = request.getParameter("content");
		String categoryId = request.getParameter("category");
		

		
		BlogBeanBO blogBO = new BlogBeanBO();
		CategoryBeanBO cbb = new CategoryBeanBO();
		String message = "";
		int result = 0;
		int pageSize = 4;
		int pageNow = 1;
		if (user == null) {
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
		}else {

			if (type.equals("add")) {
				System.out.println("add�ύ������title��"+title);
				System.out.println("add�ύ������titleIntro"+titleIntro);
				System.out.println("add�ύ������categoryId"+categoryId);
				 int res = blogBO.add(categoryId,title,titleIntro,content);
				System.out.println("���ص���res"+res);
				if (res == 1) {
					message = "��Ӳ��ĳɹ���";
				} else {
					message = "��Ӳ���ʧ�ܣ�";
				}
				// forword to another page display
				request.setAttribute("message", message);
				request.getRequestDispatcher("/admin/showResult.jsp").forward(request, response);
			} else if(type.equals("preAdd")) {
				List list = cbb.display();
				request.setAttribute("list", list);
				request.getRequestDispatcher("/admin/addBlog.jsp").forward(request, response);
			} else if (type.equals("admin")) {
				long pageCount = blogBO.getPageCount(pageSize);
				List blogList = blogBO.getBlogByPage(pageSize,pageNow);
				request.setAttribute("pageCount", Long.toString(pageCount));
				request.setAttribute("blogList", blogList);
				request.getRequestDispatcher("/admin/admin.jsp").forward(request, response);
			} else if (type.equals("delete")) {
				blogBO.delete(Integer.parseInt(id));
				// ������servlet��ӳ���ַ��web.xml��ĵ�ַ
				request.getRequestDispatcher("/servlet/AdminBlogServlet?type=admin")
						.forward(request, response);
			} else if (type.equals("modify")) {
				BlogBean blog = blogBO.modify(Integer.parseInt(id));
				List categoryList = cbb.display();

				request.setAttribute("blog", blog);
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/admin/modifyBlog.jsp").forward(request, response);
			} else if (type.equals("postModify")) {
				result = blogBO.postModify(title,titleIntro,content,categoryId, id);
				if (result == 1) {
					message = "��ϲ���޸ĳɹ���";
				} else {
					message = "Sorry!�޸�ʧ�ܣ��뷵�������޸ģ�";
				}
				request.setAttribute("message", message);
				request.getRequestDispatcher("/admin/showResult.jsp").forward(
						request, response);
			}else if(type.equals("page")){
				long pageCount = blogBO.getPageCount(pageSize);
				String s_pageNow = request.getParameter("pageNow");
				if(s_pageNow != null){
					pageNow = Integer.valueOf(s_pageNow).intValue();//��������ڿգ��������һҳ����ʾ��һҳ
				}
				List blogList = (List)blogBO.getBlogByPage(pageSize, pageNow);
				request.setAttribute("pageCount", Long.toString(pageCount));
				request.setAttribute("blogList",blogList);
				request.getRequestDispatcher("/admin/admin.jsp").forward(request,response);
				
			}
		}
	}

}
