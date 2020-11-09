<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<t:layout title="Редактирование статей"><%--@elvariable id="a_id" type="java.lang.Long"--%>
    <script>
        let context = "${pageContext.request.contextPath}";
        let partsCount = 1
    </script>
    <script src="<c:url value="/js/articlePartsAppender.js"/>"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-autosize@1.18.18/jquery.autosize.min.js" type="text/javascript"></script>
    <script src="<c:url value="/js/articleSender.js"/>"></script>
    <div class="column-container container article-view">
        <form class="article-editor" method="post">
            <h1>Редактор</h1>
            <label>
                <input type="text" placeholder="Название" id="article-title">
            </label>
            <div class="article-edit-buttons" data-number="0">
                <img src="<c:url value="/res/text-editor.png"/>" class="add-text part-btn-img" alt="text">
                <img src="<c:url value="/res/image-editor.png"/>" class="add-image part-btn-img" alt="image">
            </div>
            <%--@elvariable id="list" type="java.util.List"--%>
            <c:if test="${not(list eq null)}">
                <script>
                    $('#article-title').val("${title}")
                </script>
                <c:set var="text" value="text:"/>
                <c:set var="img" value="img:"/>
                <c:forEach var="part" items="${list}">
                    <c:if test="${fn:startsWith(part, text)}">
                        <script>
                            buttons = $('.article-edit-buttons[data-number=' + (partsCount-1) + ']')
                            textButton = $('.article-edit-buttons[data-number=' + (partsCount-1) + '] > .add-text')

                            buttons.after("<div class=\"editor-div\" data-type='text' data-number=\"" + partsCount + "\">\n" +
                                "                <img src=\"" + context + CLOSE_IMG_PATH + "\" class=\"div-close part-btn-img\" data-number=\"" + partsCount + "\" alt=\"close\">\n" +
                                "                <label class=\"text-area-label\">\n" +
                                "                    <textarea class=\"editor-text-area-div\" wrap=\"soft\"></textarea>\n" +
                                "                </label>\n" +
                                "            </div>\n" +
                                "            <div class=\"article-edit-buttons\" data-number=\"" + partsCount + "\">\n" +
                                "                <img src=\"" + context + TEXT_IMG_PATH + "\" class=\"add-text part-btn-img\" alt=\"text\">\n" +
                                "                <img src=\"" + context + IMAGE_IMG_PATH + "\" class=\"add-image part-btn-img\" alt=\"image\">\n" +
                                "            </div>")
                            addCloseListener(partsCount)
                            addPartsButtonListeners(partsCount)
                            value = `${fn:substring(part, 5, part.length())}`
                            value = value.replaceAll("&shy;", "\n")
                            $('.editor-div[data-number=' + partsCount + '] .editor-text-area-div').val(value)
                            $('.editor-text-area-div').autosize()
                            partsCount++

                        </script>
                    </c:if>
                    <c:if test="${fn:startsWith(part, img)}">
                        <script>
                            buttons = $('.article-edit-buttons[data-number=' + (partsCount-1) + ']')
                            imageButton = $('.article-edit-buttons[data-number=' + (partsCount-1) + '] > .add-image')
                            buttons.after("<div class=\"editor-div\" data-type='img' data-number=\"" + partsCount + "\">\n" +
                                "                <img src=\"" + context + CLOSE_IMG_PATH + "\" class=\"div-close part-btn-img\" data-number=\"" + partsCount + "\" alt=\"close\">\n" +
                                "                <label class=\"file-label\">\n" +
                                "                    <input type=\"file\" accept=\"image/*\" class=\"article-image\">\n" +
                                "                </label>\n" +
                                "                <div class=\"image-appender\"></div>\n" +
                                "            </div>\n" +
                                "            <div class=\"article-edit-buttons\" data-number=\"" + partsCount + "\">\n" +
                                "                <img src=\"" + context + TEXT_IMG_PATH + "\" class=\"add-text part-btn-img\" alt=\"text\">\n" +
                                "                <img src=\"" + context + IMAGE_IMG_PATH + "\" class=\"add-image part-btn-img\" alt=\"image\">\n" +
                                "            </div>")
                            addCloseListener(partsCount)
                            addFileViewListener(partsCount)
                            addPartsButtonListeners(partsCount)
                            value = "${fn:substring(part, 4, part.length())}"
                            value = value.replaceAll("&shy;", "\n")
                            $('.editor-div[data-number=' + partsCount + '] .image-appender').html("<img class=\"image-preview\" src=\"" + value + "\">")
                            partsCount++
                        </script>
                    </c:if>
                </c:forEach>
            </c:if>
        </form>
        <button class="btn btn-success send-btn" id="sendBtn">Создать</button>
    </div>
    <script>
        addPartsButtonListeners(0)
        $('.editor-text-area-div').autosize()
        addSenderListener("${a_id}")
    </script>
</t:layout>

