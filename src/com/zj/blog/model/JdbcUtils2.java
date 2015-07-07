package com.zj.blog.model;
import java.io.IOException;

import java.io.InputStream;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class JdbcUtils2 {

	private JdbcUtils2() {}

	// ����BasicDataSource�ĸ���ӿڵ���ʽ�����ds�Ĳ�����
	private static DataSource ds;

	static {

		try {

			InputStream in = JdbcUtils2.class.getClassLoader().getResourceAsStream("config.properties");

			Properties prop = new Properties();

			prop.load(in);// ͨ��������������ؽ�����

			// ͨ���������ȡ
			ds = BasicDataSourceFactory.createDataSource(prop);

		} catch (Exception e) {throw new ExceptionInInitializerError();}

	}
	// ������ӳ�
	public static DataSource getDataSource() {
		return ds;
	}
}