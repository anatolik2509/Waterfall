function addCommentRateListener(context, id, auth){
    let comment = $('.comment[data-id='+ id +']')
    let rate = $('.comment[data-id='+ id +'] > .comment-footer > .rate > .comment-rate')
    let like = $('.comment[data-id='+ id +'] > .comment-footer > .rate > .like')
    let dislike = $('.comment[data-id='+ id +'] > .comment-footer > .rate > .dislike')
    updateCommentIcons(context, comment.data('userRate'), like, dislike)
    like.click(function () {
        let prevValue = comment.data('userRate');
        let prevRate = rate.text();
        if(auth){
            if(prevValue == '1'){
                comment.data('userRate', 0);
                rate.text((parseInt(prevRate) - 1).toString())
                $.ajax(context + "/rate/comment?value=0&id=" + comment.data('id'));
                updateCommentIcons(context, 0, like, dislike)
            }
            else if(prevValue == '-1') {
                comment.data('userRate', 1);
                rate.text((parseInt(prevRate) + 2).toString())
                $.ajax(context + "/rate/comment?value=1&id=" + comment.data('id'));
                updateCommentIcons(context, 1, like, dislike)
            }
            else {
                comment.data('userRate', 1);
                rate.text((parseInt(prevRate) + 1).toString())
                $.ajax(context + "/rate/comment?value=1&id=" + comment.data('id'));
                updateCommentIcons(context, 1, like, dislike)
            }
        }
    })

    dislike.click(function () {
        let prevValue = comment.data('userRate');
        let prevRate = rate.text();
        if(auth){
            if(prevValue == '1'){
                comment.data('userRate', -1);
                rate.text((parseInt(prevRate) - 2).toString())
                $.ajax(context + "/rate/comment?value=-1&id=" + comment.data('id'));
                updateCommentIcons(context, -1, like, dislike)
            }
            else if(prevValue == '-1') {
                comment.data('userRate', 0);
                rate.text((parseInt(prevRate) + 1).toString())
                $.ajax(context + "/rate/comment?value=0&id=" + comment.data('id'));
                updateCommentIcons(context, 0, like, dislike)
            }
            else {
                comment.data('userRate', -1);
                rate.text((parseInt(prevRate) - 1).toString())
                $.ajax(context + "/rate/comment?value=-1&id=" + comment.data('id'));
                updateCommentIcons(context, -1, like, dislike)
            }
        }
    })
}

function updateCommentIcons(context, value, like, dislike) {
    switch (value) {
        case 0 :
            like.attr("src", context + likeIconPath)
            dislike.attr("src", context + dislikeIconPath)
            break
        case 1:
            like.attr("src", context + likeActiveIconPath)
            dislike.attr("src", context + dislikeIconPath)
            break
        case -1:
            like.attr("src", context + likeIconPath)
            dislike.attr("src", context + dislikeActiveIconPath)
            break
    }
}
