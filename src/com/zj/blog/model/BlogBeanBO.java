package com.zj.blog.model;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class BlogBeanBO {
	
	QueryRunner qr = DBhelper.getQueryRunner();
	
	// ��Ӳ��͵ķ���
	public int add(String categoryId,String title,String titleIntro,String content){
		System.out.println("BlogBO��ִ��sql����ʱ��title��"+title);
		System.out.println("BlogBO��ִ��sql����ʱ��titleIntro"+titleIntro);
		System.out.println("BlogBO��ִ��sql����ʱ��categoryId"+categoryId);
		String sql = "insert into blog(categoryId,title,titleIntro,content,createdtime) values (?,?,?,?,now())";
		//ΪSQL����еģ��趨��������Ҫ����һ������,�����е�ֵ������getParameter���ܹ�����ָ
		String params[]={categoryId,title,titleIntro,content};
		int result=0;
		// ����QueryRunner��update���������SQL��ִ��
		try {
			result = qr.update(sql,params);
			System.out.println("��ǰresult��"+result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// ɾ������
	public void delete(int id){
		String sql = "delete from blog where id="+id;
		try {
			qr.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public BlogBean modify(int id){
		String sql = "select id,title,titleIntro,content,categoryId from blog where id ="+id;
		// ִ�в�ѯ���,����������һ�����ݣ����Է���List
		List list = null;
		try {
			list = (List) qr.query(sql, new BeanListHandler(BlogBean.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ����ֻ��Ҫ��ȥһ����¼
		BlogBean blog = (BlogBean) list.get(0);
		return blog;
	}
	public int postModify(String title,String titleIntro,String content,String categoryId,String id){
		String sql = "update blog set title=?,titleIntro=?,content=?,categoryId=? where id =?";
		String params[]={title,titleIntro,content,categoryId,id};
		int result = 0;
		try {
			result = qr.update(sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//show a blog
	public List getBlog(String id){
		String sql = "(select * from blog where id < "+id+" order by id desc limit 1)"+"union"+   
						"(select * from blog where id >= "+id+" order by id limit 2)";
		List list = null;
		try {
			list = (List) qr.query(sql, new BeanListHandler(BlogBean.class));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// get comments
	public List getComments(String id){
		String sql = "select id,username,content,createdtime from comment where blog_id =" +id+" order by id desc";
		// һƪ�����ж������ۣ����Է���һ��list��
		List commentList = null;
		try {
			commentList = (List)qr.query(sql,new BeanListHandler(CommentBean.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentList;
		
	}
	/*
	 * int pageSize :ÿҳ��ʾ������¼
	 * �����б��ҳ��ʵ�ַ��ع��ж���ҳ
	 * 
	*/
	public long getPageCount(long pageSize){
	
		long pageCount = 0;//����һ���ж���ҳ
		long rowCount = 0; //����һ���ж�������¼
		String sql= "select count(*) from blog";
		List blogList = null;
		try {
			rowCount = (Long)qr.query(sql,new ScalarHandler(1) );
			if(rowCount%pageSize==0){
				pageCount = rowCount / pageSize;
			}else{
				pageCount = (rowCount/pageSize)+1;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return pageCount;//���ع��ж���ҳ
	}
	
	
	/***
	 * ��ҳ����
	 * int pageNow����ʾ�ڼ�ҳ
	 * ArrayList ��ArrayList����ŵľ���һ����BlogBean������Ҫ��ʾ����ļ���
	 * 
	*/
	public List getBlogByPage(int pageSize,int pageNow){
		List blogList = null;
		String sql = "select categoryId,b.id,title,titleIntro,createdtime,name from blog as b,category as c where b.categoryId = c.id order by b.id desc limit "+((pageNow-1)*pageSize) +"," +pageSize;
		try {
			blogList = (List)qr.query(sql, new BeanListHandler(BlogBean.class));
		}catch(SQLException e){
			e.printStackTrace();
		}
		return blogList;
	}
	//������� ��ʾ��ҳ��
	public long getCountByCategory(String categoryId,long pageSize){
		long pageCount = 0;//����һ���ж���ҳ
		long rowCount = 0; //����һ���ж�������¼
		String sql= "select count(*) from blog where categoryId = "+categoryId;
		List blogList = null;
		try {
			rowCount = (Long)qr.query(sql,new ScalarHandler(1) );
			if(rowCount%pageSize==0){
				pageCount = rowCount / pageSize;
			}else{
				pageCount = (rowCount/pageSize)+1;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return pageCount;//���ع��ж���ҳ
	}
	// �������ʾ�����б�
	public List displayAsCategory(String categoryId,int pageSize,int pageNow){
		List blogList = null;
		String sql = "select categoryId,b.id,title,titleIntro,createdtime,name from blog as b,category as c where c.id = b.categoryId and c.id = "+categoryId+" order by b.id desc limit "+((pageNow-1)*pageSize) +"," +pageSize;
		try {
			blogList = (List)qr.query(sql, new BeanListHandler(BlogBean.class));
		}catch(SQLException e){
			e.printStackTrace();
		}
		return blogList;
	}
	//��ȡ��Сid
	public int minId(){
		String sql = "select min(id) from blog";
		int minid = 0;
		try {
			Integer result = (Integer)qr.query(sql,new ScalarHandler(1));
			minid =  result.intValue();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return minid;
	
	}
	
}
