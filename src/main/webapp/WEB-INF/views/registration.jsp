<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout title="Регистрация"><%--@elvariable id="message" type="java.lang.String"--%>
    <div class="center-container">
        <form class="login-form" method="post">
            <h1>Регистрация</h1>
            <label>
                <input type="email" name="email" placeholder="Email" class="form-field" value="${param.email}">
            </label>
            <label>
                <input type="text" name="nick" placeholder="Никнейм" class="form-field" value="${param.nick}">
            </label>
            <label>
                <input type="password" name="password" placeholder="Пароль" class="form-field">
            </label>
            <label>
                <input type="password" name="password-repeat" placeholder="Повторите пароль" class="form-field">
            </label>
            <p class="message">${message}</p>
            <label>
                <input type="submit" value="Регистрация" class="form-btn">
            </label>
        </form>
    </div>
</t:layout>
