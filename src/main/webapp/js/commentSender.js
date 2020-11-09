function addCommentListener(context) {
    let commentField = $('#comment-field')
    let id = commentField.data("articleId");
    $('#comment-btn').click(() => {
        let responseId = commentField.data("responseId");
        let value = commentField.val();
        $.ajax({url:context,
            method: "post",
            data:{
                'id':id,
                'content':value,
                'response_id':responseId
            },
            success: function (msg) {
                if(responseId === undefined || responseId === ""){
                    $('.comment-container').append(msg)
                }
                else {
                    $('.comment[data-id=' + responseId + '] > .nested-comment').append(msg)
                }
            },
            error: function (msg) {
                console.log(msg)
            }
        })
        commentField.val('');
        document.getElementById("response").innerHTML = ""
        commentField.data('responseId', '')
    })
}