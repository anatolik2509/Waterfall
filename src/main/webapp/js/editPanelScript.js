function addEditPanelListener(context, id){
    $('.article[data-id=' + id + '] .edit-btn').click(function () {
        document.location.href = context + "/edit?id=" + id;
    })
    $('.article[data-id=' + id + '] .delete-btn').click(function () {
        $.ajax({
            url: context + "/delete?id=" + id,
            type: 'DELETE',
            success:function (msg) {
                $('.article[data-id=' + id + ']').remove()
                articleAmount--;
            }
        })
    })
}