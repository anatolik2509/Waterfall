<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<t:layout title="Статьи">
    <div class="column-container" id="article-appender">
        <div class="top-menu">
            </div>
        <div class="dropdown show categories-dropdown">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" data-toggle="dropdown">
                Категории
            </a>

            <div class="dropdown-menu">
                <a href="<c:url value="/feed?feedType=fr"/>" class="dropdown-item" id="fr">Свежее</a>
                <a href="<c:url value="/feed?feedType=d"/>" class="dropdown-item" id="d">Лучшее за день</a>
                <a href="<c:url value="/feed?feedType=w"/>" class="dropdown-item" id="w">Лучшее за неделю</a>
                <a href="<c:url value="/feed?feedType=m"/>" class="dropdown-item" id="m">Лучшее за месяц</a>
                <a href="<c:url value="/feed?feedType=y"/>" class="dropdown-item" id="y">Лучшее за год</a>
                <a href="<c:url value="/feed?feedType=all"/>" class="dropdown-item" id="all">Лучшее за всё время</a>

            </div>
        </div>
    </div>
    <script src="<c:url value="/js/rateScript.js"/>"></script>
    <script src="<c:url value="/js/articleLoader.js"/>"></script>
    <script>
        context = "${pageContext.request.contextPath}";
        updated = ${now.time};
        feedType = '${feedType}';
    </script>
    <c:if test="${not (lastArticle eq null)}">
        <%--@elvariable id="savedOffset" type="java.lang.Integer"--%>
        <script>
            articleAmount = ${lastArticle};
            $.ajax({url:context + "/feed?begin=" + 0 + "&end=" + articleAmount + "&updated=" + updated + "&feedType=" + "${feedType}",
                success:function (msg) {
                    requestSend = true
                    $("#article-appender").append(msg)
                    window.scrollTo(0, ${savedOffset})
                }});
        </script>
    </c:if>
    <script>
        scrollController()
    </script>
    <%--@elvariable id="feedType" type="java.lang.String"--%>
    <%--@elvariable id="lastArticle" type="java.lang.Integer"--%>
</t:layout>

