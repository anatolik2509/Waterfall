<%--@elvariable id="auth" type="java.lang.Boolean"--%>
<%--@elvariable id="userRate" type="java.lang.Integer"--%>
<%@tag description="comment template" pageEncoding="UTF-16" %>
<%@tag import="ru.itis.antonov.waterfall.models.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="commentObject" required="true" type="ru.itis.antonov.waterfall.models.Comment" %>
<%@attribute name="recursive" type="java.lang.Boolean" %>
<div class="comment" data-id="${commentObject.id}" data-user-rate="${commentObject.userRate}">
    <div class="comment-header">
        <p class="author">${commentObject.author.nickname}</p>
        <p class="comment-date">${commentObject.date.toLocaleString()}</p>
    </div>
    <div class="comment-content">
        ${commentObject.content}
    </div>
    <div class="comment-footer">
        <div class="rate">
            <img class="rate-btn like" src="<c:url value="/res/like.png"/>" alt="like">
            <p class="comment-rate">${commentObject.rate}</p>
            <img class="rate-btn dislike" src="<c:url value="/res/dislike.png"/>" alt="dislike">
            <div class="response-btn underline-on-hover"
                 data-id="${commentObject.id}" data-author-name="${commentObject.author.nickname}">
                Ответить
            </div>
        </div>
    </div>
    <c:if test="${recursive}">
        <div class="nested-comment">
            <c:forEach var="comment" items="${commentObject.childes}">
                <t:comment commentObject="${comment}" recursive="${true}"/>
            </c:forEach>
        </div>
    </c:if>
    <script>
        addResponseListeners("${commentObject.id}")
        addCommentRateListener("${pageContext.request.contextPath}", ${commentObject.id}, ${auth})
    </script>
</div>