<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="article" type="ru.itis.antonov.waterfall.models.Article"--%>
<t:layout title="${article.title}">
    <script src="<c:url value="/js/commentSender.js"/>"></script>
    <script src="<c:url value="/js/responseScript.js"/>"></script>
    <script src="<c:url value="/js/rateScript.js"/>"></script>
    <script src="<c:url value="/js/commentRateScript.js"/>"></script>
    <div class="column-container container article-view">
        <a href="<c:url value="/feed"/>" class="btn btn-secondary btn-lg active back-btn">Назад</a>
        <t:article articleObject="${article}" hideCommentBtn="${true}"/>
        <div class="comment-container">
            <h3>Комментарии</h3>
            <%--@elvariable id="commentList" type="java.util.List"--%>
            <c:forEach var="comment" items="${article.comments}">
                <t:comment commentObject="${comment}" recursive="${true}"/>
            </c:forEach>
        </div>
        <%--@elvariable id="auth" type="java.lang.Boolean"--%>
    </div>
    <c:if test="${auth}">
        <div class="comment-form container">
            <div id="response">

            </div>
            <div class="row align-items-center">
                <label class="col-9">
                    <input type="text" name="comment-content" id="comment-field"
                           data-article-id="${article.id}" class="comment-field">
                </label>
                <label class=" col-3">
                    <input type="submit" class="comment-btn" id="comment-btn">
                </label>
            </div>
        </div>
    </c:if>
    <script>
        addCommentListener("${pageContext.request.contextPath}/addComment")
    </script>
</t:layout>
