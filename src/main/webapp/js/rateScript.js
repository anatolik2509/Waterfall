const likeIconPath = '/res/like.png'
const dislikeIconPath = '/res/dislike.png'
const likeActiveIconPath = '/res/like_active.png'
const dislikeActiveIconPath = '/res/dislike_active.png'
const saveIcon = '/res/save.png'
const savedIcon = '/res/saved.png'

function addListeners(context, id, auth) {
    let articles = $(".article");
    for(let i = 0; i < articles.length; i++){
        if(articles[i].dataset.id == id){
            update(context, articles[i].dataset.userRate, articles[i])
            if(articles[i].dataset.saved == "true"){
                articles[i].getElementsByClassName("save-btn")[0].setAttribute("src", context + savedIcon)
            }
            else {
                articles[i].getElementsByClassName("save-btn")[0].setAttribute("src", context + saveIcon)
            }
            articles[i].getElementsByClassName("like")[0].addEventListener("click", evt => {
                let prevValue = articles[i].dataset.userRate;
                let prevRate = articles[i].getElementsByClassName("article-rate")[0].innerHTML;
                if(auth){
                    if(prevValue == '1'){
                        articles[i].dataset.userRate = '0';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) - 1).toString()
                        $.ajax(context + "/rate/article?value=0&id=" + articles[i].dataset.id);
                        update(context, '0',articles[i])
                    }
                    else if(prevValue == '-1') {
                        articles[i].dataset.userRate = '1';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) + 2).toString()
                        $.ajax(context + "/rate/article?value=1&id=" + articles[i].dataset.id);
                        update(context, '1',articles[i])
                    }
                    else {
                        articles[i].dataset.userRate = '1';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) + 1).toString()
                        $.ajax(context + "/rate/article?value=1&id=" + articles[i].dataset.id);
                        update(context, '1',articles[i])
                    }
                }
            })
            articles[i].getElementsByClassName("dislike")[0].addEventListener("click", evt => {
                let prevValue = articles[i].dataset.userRate;
                let prevRate = articles[i].getElementsByClassName("article-rate")[0].innerHTML;
                if(auth){
                    if(prevValue == '-1'){
                        articles[i].dataset.userRate = '0';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) + 1).toString()
                        $.ajax(context + "/rate/article?value=0&id=" + articles[i].dataset.id);
                        update(context, '0',articles[i])
                    }
                    else if(prevValue == '1') {
                        articles[i].dataset.userRate = '-1';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) - 2).toString()
                        $.ajax(context + "/rate/article?value=-1&id=" + articles[i].dataset.id);
                        update(context, '-1',articles[i])
                    }
                    else {
                        articles[i].dataset.userRate = '-1';
                        articles[i].getElementsByClassName("article-rate")[0].innerHTML = (parseInt(prevRate) - 1).toString()
                        $.ajax(context + "/rate/article?value=-1&id=" + articles[i].dataset.id);
                        update(context, '-1',articles[i])
                    }
                }
            })
            let commentBtn = articles[i].getElementsByClassName("comment-a")[0];
            if(commentBtn !== undefined) {
                commentBtn.addEventListener("click", evt => {
                    $.ajax(context + "/saveOffset?offset=" + window.pageYOffset + "&lastArticle=" + articleAmount)
                    document.location.href = context + "/article?id=" + articles[i].dataset.id;
                })
            }
            articles[i].getElementsByClassName("save-btn")[0].addEventListener("click", evt => {
                let value = articles[i].dataset.saved;
                $.ajax(context + "/save/article?id=" + articles[i].dataset.id);
                if(value == "true"){
                    articles[i].getElementsByClassName("save-btn")[0].setAttribute("src", context + saveIcon)
                    articles[i].dataset.saved = "false"
                }
                else {
                    articles[i].getElementsByClassName("save-btn")[0].setAttribute("src", context + savedIcon)
                    articles[i].dataset.saved = "true"
                }
            })
        }
    }
}

function update(context, value, el) {
    switch (value) {
        case '0' :
            el.getElementsByClassName("like")[0].setAttribute("src", context + likeIconPath)
            el.getElementsByClassName("dislike")[0].setAttribute("src", context + dislikeIconPath)
            break
        case '1':
            el.getElementsByClassName("like")[0].setAttribute("src", context + likeActiveIconPath)
            el.getElementsByClassName("dislike")[0].setAttribute("src", context + dislikeIconPath)
            break
        case '-1':
            el.getElementsByClassName("like")[0].setAttribute("src", context + likeIconPath)
            el.getElementsByClassName("dislike")[0].setAttribute("src", context + dislikeActiveIconPath)
            break
    }
}
