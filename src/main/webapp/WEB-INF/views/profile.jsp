<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout title="Профиль">
    <div class="center-container">
        <div class="profile">
            <h1>Ваш профиль</h1>
            <h3>Ник: </h3>
            <p>${user.getNickname()}</p>
            <h3>Email: </h3>
            <p>${user.getEmail()}</p>
        </div>
        <a href="<c:url value="/logout"/>" class="logout-button" id="logout">Выйти</a>
    </div>
</t:layout>