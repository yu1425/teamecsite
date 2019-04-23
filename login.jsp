<%@ page language="java" contentType="text/html;
charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/venus.css">
<link href="https://fonts.googleapis.com/css?family=Great+Vibes&amp;subset=latin-ext" rel="stylesheet">
<title>ログイン画面</title>
</head>
<body>
	<script type="text/javascript" src="./js/login.js"></script>
	<jsp:include page="header.jsp"/>
	<div id="contents">
	<div class="top">
		<h1>ログイン画面</h1>
		</div>
		<br>
		<s:form id="loginForm">
			<s:if
				test="userIdErrorMessageList!=null && userIdErrorMessageList.size()>0">
				<div class="error">
                            <s:iterator value="userIdErrorMessageList">
							<s:property /><br>
						</s:iterator>

				</div>
			</s:if>
			<s:if
				test="userPasswordErrorMessageList!=null && userPasswordErrorMessageList.size()>0">
				<div class="error">
						<s:iterator value="userPasswordErrorMessageList">
							<s:property /><br>
						</s:iterator>
				</div>
			</s:if>
			<s:if
				test="isNotUserInfoMessage!=null && !isNotUserInfoMessage.isEmpty()">
				<div class="error">
						<s:iterator value="isNotUserInfoMessage">
							<s:property />
						</s:iterator>
				</div>
			</s:if>
			<table class="vertical-list-table">
				<tr>
					<th scope="row"><s:label value="ユーザーID" /></th>
					<s:if test="#session.savedUserIdFlag == true">
						<td><s:textfield name="userId" class="txt"
								value='%{session.savedUserId}' placeholder="ユーザーID"
								autocomplete="off" /></td>
					</s:if>
					<s:else>
						<td><s:textfield name="userId" class="txt" value='%{userId}'
								placeholder="ユーザーID" autocomplete="off" /></td>
					</s:else>
				</tr>
				<tr>
					<th scope="row"><s:label value="パスワード" /></th>
					<td><s:password name="password" class="txt"
							placeholder="パスワード" autocomplete="off" /></td>
				</tr>
			</table>
			<div class="box">
				<s:if
					test="#session.savedUserIdFlag==true && #session.savedUserId!=null && !#session.savedUserId.isEmpty() ">
					<s:checkbox name="savedUserIdFlag" checked="checked" />
				</s:if>
				<s:else>
					<s:checkbox name="savedUserIdFlag" />
				</s:else>
				<s:label value="ユーザーID保存" class="saveId"/>
			</div>
			<div class="submit_btn_box">
				<s:submit value="ログイン" class="submit_btn" onclick="goLoginAction()" />
			</div>
			<div class="submit_btn_box">
				<div id="contents-btn-set">
					<s:submit value="新規ユーザー登録" class="submit_btn"
						onclick="goCreateUserAction()" />
				</div>
			</div>
			<div class="submit_btn_box">
				<div id="contents-btn-set">
					<s:submit value="パスワード再設定" class="submit_btn"
						onclick="goResetPasswordAction()" />
				</div>
			</div>
		</s:form>
	</div>

</body>
</html>