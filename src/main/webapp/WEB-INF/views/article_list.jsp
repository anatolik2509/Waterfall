<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="articleList" type="java.util.List"--%>

<c:forEach var="article" items="${articleList}">
    <t:article articleObject="${article}" editPanel="${editPanel}"/>
</c:forEach>
