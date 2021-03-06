<%--@elvariable id="auth" type="java.lang.Boolean"--%>
<%--@elvariable id="userRate" type="java.lang.Integer"--%>
<%@tag description="article template" pageEncoding="UTF-16" %>
<%@tag import="ru.itis.antonov.waterfall.models.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="articleObject" required="true" type="ru.itis.antonov.waterfall.models.Article" %>
<%@attribute name="hideCommentBtn" type="java.lang.Boolean"%>
<%@attribute name="editPanel" type="java.lang.Boolean"%>
<div class="article" data-id="${articleObject.id}" data-user-rate="${articleObject.userRate}" data-saved="${articleObject.saved}">
    <div class="article-header">
        <div class="article-info">
            <p class="author">${articleObject.author.nickname}</p>
                <c:if test="${not (articleObject.group.id eq -1)}">
                    <p class="group">
                        ${articleObject.group.name}
                    </p>
                </c:if>
            <p class="article-date">${articleObject.date.toLocaleString()}</p>
        </div>
        <h3 class="article-title">${articleObject.title}</h3>
    </div>
    <div class="article-content">
        ${articleObject.content}
    </div>
    <div class="article-footer">
        <div class="rate col-4">
            <img class="rate-btn like" src="<c:url value="/res/like.png"/>" alt="like">
            <p class="article-rate">${articleObject.rate}</p>
            <img class="rate-btn dislike" src="<c:url value="/res/dislike.png"/>" alt="dislike">
        </div>
        <div class="col-4 comment-div">
            <c:if test="${not hideCommentBtn}">
                <div class="comment-a">
                    <p class="comment-count">${articleObject.commentAmount}</p>
                    <img class="rate-btn comment-img" src="<c:url value="/res/comment.png"/>" alt="comments">
                </div>
            </c:if>
        </div>
        <div class="col-4">
            <c:if test="${editPanel}">
                <img class="delete-btn" src="<c:url value="/res/delete.png"/>" alt="save">
                <img class="edit-btn" src="<c:url value="/res/pencil.png"/>" alt="save">
            </c:if>
            <img class="save-btn rate-btn" src="<c:url value="/res/save.png"/>" alt="save">
        </div>
    </div>
    <script>
        addListeners("${pageContext.request.contextPath}", ${articleObject.id}, ${auth})
        <c:if test="${editPanel}">
            addEditPanelListener("${pageContext.request.contextPath}", ${articleObject.id})
        </c:if>
    </script>
</div>
