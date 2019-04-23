package com.internousdev.venus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.internousdev.venus.dto.UserInfoDTO;
import com.internousdev.venus.util.DBConnector;

public class UserInfoDAO {

	private DBConnector db = new DBConnector();
	private Connection con = db.getConnection();


//	ユーザー作成
	public int createUser( String familyName, String firstName, String familyNameKana,
			String firstNameKana,int sex, String email, String userId, String password) {
		int count = 0;
		String sql = "INSERT INTO user_info(user_id,password,family_name,first_name,family_name_kana,first_name_kana,sex,email,regist_date,update_date) VALUES(?,?,?,?,?,?,?,?,now(),now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, familyName);
			ps.setString(4, firstName);
			ps.setString(5, familyNameKana);
			ps.setString(6, firstNameKana);
			ps.setInt(7, sex);
			ps.setString(8, email);

			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	// ユーザーIDを引き数にそのユーザーが本当にいるかデータベースを確認
	public boolean isExistsUserId(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		boolean result = false;
		String sql = "SELECT count(*) as count FROM user_info WHERE user_id=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt("count") > 0) {
					result = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// ユーザーIDとパスワードを引き数にそのユーザーが本当にいるかデータベースを確認
	public boolean isExistsUserIdAndPassword(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		boolean result = false;

		String sql = "SELECT count(*) as count FROM user_info WHERE user_id=? AND password=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt("count") > 0) {
					result = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// パスワード再設定で現在のパスワードを新しいパスワードに更新
	public int resetPassword(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "UPDATE user_info SET password=?, update_date=now() WHERE user_id=?";
		int result = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, userId);

			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public UserInfoDTO getUserId(String userId) {
		DBConnector db=new DBConnector();
		Connection con=db.getConnection();
		UserInfoDTO dto=new UserInfoDTO();
		String sql="SELECT*FROM user_info WHERE user_id=?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				dto.setFamilyName(rs.getString("family_name"));
				dto.setFirstName(rs.getString("first_name"));
				dto.setFamilyNameKana(rs.getString("family_name_kana"));
				dto.setFirstNameKana(rs.getString("first_name_kana"));
				dto.setSex(rs.getString("sex"));
				dto.setEmail(rs.getString("email"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return dto;


	}

	public UserInfoDTO getUserInfo(String userId, String password) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		String sql = "select * from user_info where user_id=? and password=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				userInfoDTO.setid(resultSet.getInt("id"));
				userInfoDTO.setUserId(resultSet.getString("user_id"));
				userInfoDTO.setPassword(resultSet.getString("password"));
				userInfoDTO.setFamilyName(resultSet.getString("family_name"));
				userInfoDTO.setFirstName(resultSet.getString("first_name"));
				userInfoDTO.setFamilyNameKana(resultSet.getString("family_name_kana"));
				userInfoDTO.setSex(resultSet.getString("sex"));
				userInfoDTO.setEmail(resultSet.getString("email"));
				userInfoDTO.setLogined(resultSet.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userInfoDTO;
	}

	public UserInfoDTO getUserInfo(String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		String sql = "select * from user_info where user_id=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				userInfoDTO.setid(resultSet.getInt("id"));
				userInfoDTO.setUserId(resultSet.getString("user_id"));
				userInfoDTO.setPassword(resultSet.getString("password"));
				userInfoDTO.setFamilyName(resultSet.getString("family_name"));
				userInfoDTO.setFirstName(resultSet.getString("first_name"));
				userInfoDTO.setFamilyNameKana(resultSet.getString("family_name_kana"));
				userInfoDTO.setFirstNameKana(resultSet.getString("first_name_kana"));
				userInfoDTO.setSex(resultSet.getString("sex"));
				userInfoDTO.setEmail(resultSet.getString("email"));
				userInfoDTO.setLogined(resultSet.getInt("logined"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userInfoDTO;
	}

//ユーザーIDとパスワードを引数にユーザー情報がDBに存在するか確認
	public boolean isExistUserInfo(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		boolean result = false;
		String sql = "select count(*) as count form user_Info where user_Id = ? AND password = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getInt("count") > 0) {
					result = true;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}return result;
	}

	//ユーザーIDを引数にユーザー情報がDBに存在するか確認
		public boolean isExistUserInfo(String userId) {
			DBConnector db = new DBConnector();
			Connection con = db.getConnection();
			boolean result = false;
			String sql = "select count(*) as count form user_Info where user_Id = ?";
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, userId);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					if(rs.getInt("count") > 0) {
						result = true;
					}
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					con.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}return result;
		}

    //ユーザーIDとパスワードを引数にログイン情報を登録
	public int login(String userId, String password) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int result=0;
		String sql ="update user_info set logined = 1, update_date = now() where user_Id = ? AND password = ? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, password);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	//ユーザーIDを引数にログアウト情報を登録
	public int logout(String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int result=0;
		String sql ="update user_info set logined = 0, update_date = now() where user_Id = ? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}


