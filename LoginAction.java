package com.internousdev.venus.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.venus.dao.CartInfoDAO;
import com.internousdev.venus.dao.UserInfoDAO;
import com.internousdev.venus.dto.CartInfoDTO;
import com.internousdev.venus.dto.UserInfoDTO;
import com.internousdev.venus.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {

	private String userId;
	private String password;
	private Map<String, Object> session;
	private boolean savedUserIdFlag;
	private List<CartInfoDTO> cartInfoDTOList;
	private List<String> userIdErrorMessageList;
	private List<String> userPasswordErrorMessageList;
	private String isNotUserInfoMessage;
	private int allTotalPrice;

	public String execute() throws SQLException {

		if (session.isEmpty()) {
			return "sessionTimeout";
		}

		session.remove("userIdForCreateUser");
		session.remove("password");

		String result = ERROR;

		if (savedUserIdFlag) {
			session.put("savedUserIdFlag", true);
			session.put("saveduserId", userId);
		} else {
			session.remove("savedUserIdFlag");
			session.remove("saveduserId");
		}

		InputChecker inputChecker = new InputChecker();
		userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false,
				false);
		userPasswordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false,
				false, false);

		if (userIdErrorMessageList.size() > 0 || userPasswordErrorMessageList.size() > 0) {
			session.put("logined", 0);
			return result;
		}

		UserInfoDAO userInfoDAO = new UserInfoDAO();
		if (userInfoDAO.isExistsUserIdAndPassword(userId, password)) {
			if (userInfoDAO.login(userId, password) > 0) {
				@SuppressWarnings("unchecked")
				// カートの情報をユーザーに紐づける。
				// sessionからカート情報を取得
				List<CartInfoDTO> cartInfoDTOListBySession = (List<CartInfoDTO>) session.get("cartInfoDTOList");
				if (cartInfoDTOListBySession != null) {
					boolean cartresult = changeCartInfo(cartInfoDTOListBySession);
					if (!cartresult) {
						return "dberror";
					}
				}
				if (session.containsKey("cartFlag")) {
					session.remove("cartFlag");
					CartInfoDAO cartInfoDAO = new CartInfoDAO();
					allTotalPrice = cartInfoDAO.getAllTotalPrice(userId);
					result = "cart";
				} else {
					result = SUCCESS;
				}
				// ユーザー情報をsessionに登録する
				UserInfoDTO userInfoDTO = userInfoDAO.getUserInfo(userId, password);
				session.put("userId", userInfoDTO.getUserId());
				session.put("logined", 1);
			}
		} else {
			isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";
		}
		return result;
	}

	private boolean changeCartInfo(List<CartInfoDTO> cartInfoDTOListBySession) throws SQLException {
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		int count = 0;
		// 仮のユーザーIDを定義
		String tempUserId = session.get("tempUserId").toString();
		boolean result = false;

		for (CartInfoDTO dto : cartInfoDTOListBySession) {
			// sessionに入っている（画面に映っている）データと同じものがDBに存在するか確認
			if (cartInfoDAO.isExistsCartInfo(userId, dto.getProductId())) {
				// 存在する場合は、カート情報テーブルの購入個数を更新し、tempUserIdのデータは削除する
				count += cartInfoDAO.updateProductCount(userId, dto.getProductId(), dto.getProductCount());
				cartInfoDAO.delete(String.valueOf(dto.getProductId()), tempUserId);
				// 存在しない場合は、ユーザーIDをtempUserIdからuserIdに変更する。
			} else {
				count += cartInfoDAO.linkToUserId(tempUserId, userId, dto.getProductId());

			}
		}
		if (count == cartInfoDTOListBySession.size()) {
			cartInfoDTOList = cartInfoDAO.getCartInfoDTOList(userId);
			result = true;
		}
		return result;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSavedUserIdFlag() {
		return savedUserIdFlag;
	}

	public void setSavedUserIdFlag(boolean savedUserIdFlag) {
		this.savedUserIdFlag = savedUserIdFlag;
	}

	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}

	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}

	public List<String> getUserPasswordErrorMessageList() {
		return userPasswordErrorMessageList;
	}

	public void setUserPasswordErrorMessageList(List<String> userPasswordErrorMessageList) {
		this.userPasswordErrorMessageList = userPasswordErrorMessageList;
	}

	public String getIsNotUserInfoMessage() {
		return isNotUserInfoMessage;
	}

	public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
		this.isNotUserInfoMessage = isNotUserInfoMessage;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public int getAllTotalPrice() {
		return allTotalPrice;
	}

	public void setAllTotalPrice(int allTotalPrice) {
		this.allTotalPrice = allTotalPrice;
	}
}
