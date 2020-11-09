let articleAmount = 0;
let updated;
let context;
const articlesToLoad = 5
const loadBorder = 100
let requestSend = true


function request() {
    if(document.body.scrollHeight - window.pageYOffset - document.body.clientHeight < loadBorder && requestSend){
        requestSend = false
        $.ajax({url:context + "/feed?begin=" + articleAmount + "&end=" + (articleAmount + articlesToLoad) + "&updated=" + updated + "&feedType=" + feedType,
            success:function (msg) {
                requestSend = true
                $("#article-appender").append(msg)
                articleAmount += articlesToLoad
            }});
    }
}


function scrollController() {
    if(articleAmount === 0) {
        request()
    }
    document.addEventListener("scroll", request)
}
