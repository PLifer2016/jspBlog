package com.zj.blog.model;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
/*�����������ȡ����Դ���������ݿ����ӣ�������󷵻�һ��QueryRunner����*/
public class DBhelper {
	public static QueryRunner getQueryRunner(){
		// ����Դ����������Ϊ���ݿ����ӳصĹ����ţ�ͨ�������Ի�ȡ���ݿ������ 
		DataSource ds = null;
		try {
			ds = (DataSource) JdbcUtils2.getDataSource();//���ֻд��䣬����һֱ�ȴ�ִ�С�
		} catch (Exception e) {
			e.printStackTrace();
		}
		// DButils�еĺ����࣬���ɶ���ʱ��������Դ����ds
		QueryRunner qr = new QueryRunner(ds);
		return qr;
	}
}
