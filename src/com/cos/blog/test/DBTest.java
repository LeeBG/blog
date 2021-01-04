package com.cos.blog.test;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

import com.cos.blog.config.DB;
//디비 테스트
public class DBTest {
	@Test
	public void 디비연결() {
		Connection conn = DB.getConnection();
		assertNotNull(conn);
	}
}
