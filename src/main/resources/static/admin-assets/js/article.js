
$('#tags').tagsInput({
    width: '100%',
    height: '35px',
    defaultText: 'Tags'
});

$('.toggle').toggles({
    on: true,
    text: {
        on: 'On',
        off: 'Off'
    }
});

$(".select2").select2({
    width: '100%'
});

var tale = new $.tale();


function subArticle(status) {
    var title = $('#articleForm input[name=title]').val();
    var content = $('#text').val();
    if (title == '') {
        tale.alertWarn('Enter title ');
        return;
    }
    if (content == '') {
        tale.alertWarn('Enter content');
        return;
    }
    $('#content-editor').val(content);
    $("#articleForm #status").val(status);
    $("#articleForm #categories").val($('#multiple-sel').val());
    var params = $("#articleForm").serialize();
    var url = $('#articleForm #cid').val() != '' ? '/admin-assets/article/modify' : '/admin-assets/article/publish';
    tale.post({
        url:url,
        data:params,
        success: function (result) {
            if (result && result.success) {
                tale.alertOk({
                    text:'The article was saved successfully',
                    then: function () {
                        setTimeout(function () {
                            window.location.href = '/admin-assets/article';
                        }, 500);
                    }
                });
            } else {
                tale.alertError(result.msg || 'Save Article Failed');
            }
        }
    });
}

var textarea = $('#text'),
    toolbar = $('<div class="markdown-editor" id="md-button-bar" />').insertBefore(textarea.parent())
preview = $('<div id="md-preview" class="md-hidetab" />').insertAfter('.markdown-editor');

markdown(textarea, toolbar, preview);







$('div.allow-false').toggles({
    off: true,
    text: {
        on: 'On',
        off: 'Off'
    }
});