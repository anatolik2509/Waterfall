const CLOSE_IMG_PATH = "/res/close.png"
const TEXT_IMG_PATH = "/res/text-editor.png"
const IMAGE_IMG_PATH = "/res/image-editor.png"

function addPartsButtonListeners(buttonsNumber) {
    let buttons = $('.article-edit-buttons[data-number=' + buttonsNumber + ']')
    let textButton = $('.article-edit-buttons[data-number=' + buttonsNumber + '] > .add-text')
    let imageButton = $('.article-edit-buttons[data-number=' + buttonsNumber + '] > .add-image')
    textButton.click(function () {
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
        $('.editor-text-area-div').autosize()
        partsCount++
    })
    imageButton.click(function () {
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
        partsCount++
    })
}

function addCloseListener(number) {
    $('.div-close[data-number=' + number + ']').click(function () {
        $('.editor-div[data-number=' + number + ']').remove()
        $('.article-edit-buttons[data-number=' + number + ']').remove()
    })
}

function addFileViewListener(number) {
    $('.editor-div[data-number=' + number + '] .article-image').change(function () {
        let file = this.files;
        if(file.length > 0){
            let reader = new FileReader()
            reader.onload = function(e) {
                let editorDiv = $('.editor-div[data-number=' + number + '] .image-appender')
                editorDiv.html('')
                editorDiv.append('<img class="image-preview" src="' + e.target.result + '" alt="image">')
            }
            reader.readAsDataURL(file[0])
        }
        else {
            $('.editor-div[data-number=' + number + '] .image-appender').html('')
        }
    })
}
