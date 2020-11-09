function addSenderListener(id) {
    $('#sendBtn').click(async function () {
        let response = {}
        response['title'] = $('#article-title').val()
        let partsList = document.getElementsByClassName('editor-div')
        for(let i = 0; i < partsList.length; i++){
            if(partsList[i].dataset.type === 'text'){
                response['part-' + i] = partsList[i].getElementsByClassName('editor-text-area-div')[0].value
                response['type-' + i] = "text"
            }
            else {
                let files = partsList[i].getElementsByClassName('article-image')[0].files
                if(files.length > 0) {
                    let data = new FormData()
                    data.append('img', files[0])
                    await $.ajax({
                        url: context + '/media',
                        type: 'POST',
                        data: data,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (msg) {
                            response['part-' + i] = msg
                            response['type-' + i] = "img"
                        },
                        error: function (msg) {
                            console.log(msg)
                        }
                    })
                }
                else {
                    let preview = partsList[i].getElementsByClassName('image-preview')
                    if(preview.length > 0) {
                        let src = preview[0].getAttribute("src")
                        response['part-' + i] = src.substring(src.indexOf('=') + 1)
                        response['type-' + i] = "img"
                    }
                    else {
                        response['part-' + i] = ""
                        response['type-' + i] = "img"
                    }
                }
            }
        }
        if(id === "") {
            $.ajax({
                url: context + '/edit',
                type: 'POST',
                data: response,
                error: function(msg){
                    console.log(msg)
                },
                success: function (data, textStatus) {
                    window.location.href = context + '/userArticles';
                }
            })
        }
        else {
            $.ajax({
                url: context + '/edit?id=' + id,
                type: 'POST',
                data: response,
                error: function(msg){
                    console.log(msg)
                },
                success: function (data, textStatus) {
                    window.location.href = context + '/userArticles';
                }
            })
        }
    })
}