package com.quizpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
	// use hibernate here. move these to app.pro
	@Autowired
	DataSource dataSource;

	public boolean verifyUser(String userIdOrUsername, String password) {
		try (
				Connection con = dataSource.getConnection();
				) {
			String sqlQuery = "SELECT * FROM myusers WHERE (userId=? OR username=?) AND password=?";
			PreparedStatement ps = con.prepareStatement(sqlQuery);
			ps.setString(1, userIdOrUsername);
			ps.setString(2, userIdOrUsername);
			ps.setString(3, password);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void updateOTP(String userIdOrUsername, String otp) {
		try (
				Connection con = dataSource.getConnection();
				) {
			String sqlQuery = "UPDATE myusers SET otp=? WHERE userId=? OR username=?";
			PreparedStatement ps = con.prepareStatement(sqlQuery);
			ps.setString(1, otp);
			ps.setString(2, userIdOrUsername);
			ps.setString(3, userIdOrUsername);
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean verifyOTP(String userIdOrUsername, String otp) {
		try (
				Connection con = dataSource.getConnection();
				) {
			String sqlQuery = "SELECT * FROM myusers WHERE (userId=? OR username=?) AND otp=?";
			PreparedStatement ps = con.prepareStatement(sqlQuery);
			ps.setString(1, userIdOrUsername);
			ps.setString(2, userIdOrUsername);
			ps.setString(3, otp);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public String getEmailByUserIdOrUsername(String userIdOrUsername) {
		String sql = "SELECT email FROM myusers WHERE userId = ? OR username = ?";
		try (
				Connection con = dataSource.getConnection(); 
				PreparedStatement ps = con.prepareStatement(sql)
						) {
			ps.setString(1, userIdOrUsername);
			ps.setString(2, userIdOrUsername);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("email");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean updatePassword(String userIdOrUsername, String newPassword) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE myusers SET password=? WHERE userId=? OR username=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, newPassword);
			ps.setString(2, userIdOrUsername);
			ps.setString(3, userIdOrUsername);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}