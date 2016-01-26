<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 16-1-11
  Time: 上午11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
</head>
<body id="body-bg">
<section class="login-sec">
    <div id="articleMsg">
        <article class="message">
          <p class="error" id="msg"></p>
        </article>
    </div>
    <article class="login-article">
        <ul>
            <li class="username">
                 <label for="J_username">&nbsp;</label>
                <input type="text" name="username" id="J_username">
            </li>
            <li class="username">
                <label for="J_password">&nbsp;</label>
                <input type="password" name="userpwd" id="J_password">
            </li>
        </ul>
        <aside class="login-submit-button" id="submit-button">
            <button type="button" id="loginBtn" onclick="location='index.html'"></button>
            "
        </aside>
    </article>
</section>

</body>
</html>