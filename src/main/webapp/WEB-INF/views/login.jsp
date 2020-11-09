<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:layout title="Вход"><%--@elvariable id="message" type="java.lang.String"--%>
    <div class="center-container">
        <form method="get" class="login-form">
            <h1>
                Вход
            </h1>
            <label>
                <input type="email" placeholder="Email" name="email" class="form-field" value="${param.email}">
            </label>
            <label>
                <input type="password" placeholder="Пароль" name="password" class="form-field">
            </label>
            <p class="message">${message}</p>
            <label>
                <input type="submit" value="Войти" class="form-btn">
            </label>
            <div>
                Нет аккаунта?
                <a class="to-registration underline-on-hover" href="<c:url value="/registration"/>">
                    Регистрация
                </a>
            </div>
        </form>
    </div>
</t:layout>
