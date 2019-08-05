function markdown(textarea, toolbar, preview) {
    var options = {};
    options.strings = {
        bold: 'Bold <strong> Ctrl+B',
        boldexample: 'Bold text',

        italic: 'Italics <em> Ctrl+I',
        italicexample: 'Italics text',

        link: 'Link <a> Ctrl+L',
        linkdescription: 'Enter link',

        quote:  'Blockquote <blockquote> Ctrl+Q',
        quoteexample: 'Blockquote',

        code: 'Code <pre><code> Ctrl+K',
        codeexample: 'Code',

        image: 'Image <img> Ctrl+G',
        imagedescription: 'Image',

        olist: 'oList <ol> Ctrl+O',
        ulist: 'uList <ul> Ctrl+U',
        litem: 'List',

        heading: 'Heading <h1>/<h2> Ctrl+H',
        headingexample: 'Heading',

        hr: 'Hr <hr> Ctrl+R',
        more: 'More Ctrl+M',

        undo: 'Undo - Ctrl+Z',
        redo: 'Redo - Ctrl+Y',
        redomac: 'Redomac - Ctrl+Shift+Z',

        imagedialog: '<p><b>Insert picture</b></p><p>Enter link</p>',
        linkdialog: '<p><b>Insert Link</b></p><p>Enter link</p>',

        ok: 'Ok',
        cancel: 'Cancel'
    };

    var converter = new Markdown.Converter(),
        editor = new Markdown.Editor(converter, '', options),
        diffMatch = new diff_match_patch(), last = '', preview = $('#md-preview'),
        mark = '@mark' + Math.ceil(Math.random() * 100000000) + '@',
        span = '<span class="diff" />';


    Markdown.Extra.init(converter, {
        extensions  :   ["tables", "fenced_code_gfm", "def_list", "attr_list", "footnotes"]
    });


    converter.hooks.chain('postConversion', function (html) {

        html = html.replace(/<\/?(\!doctype|html|head|body|link|title|input|select|button|textarea|style|noscript)[^>]*>/ig, function (all) {
            return all.replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/'/g, '&#039;')
                .replace(/"/g, '&quot;');
        });

        // clear hard breaks
        html = html.replace(/\s*((?:<br>\n)+)\s*(<\/?(?:p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|address|form|fieldset|iframe|hr|legend|article|section|nav|aside|hgroup|header|footer|figcaption|li|dd|dt)[^\w])/gm, '$2');

        if (html.indexOf('<!--more-->') > 0) {
            var parts = html.split(/\s*<\!\-\-more\-\->\s*/),
                summary = parts.shift(),
                details = parts.join('');

            html = '<div class="summary">' + summary + '</div>'
                + '<div class="details">' + details + '</div>';
        }


        var diffs = diffMatch.diff_main(last, html);
        last = html;

        if (diffs.length > 0) {
            var stack = [], markStr = mark;

            for (var i = 0; i < diffs.length; i ++) {
                var diff = diffs[i], op = diff[0], str = diff[1]
                sp = str.lastIndexOf('<'), ep = str.lastIndexOf('>');

                if (op != 0) {
                    if (sp >=0 && sp > ep) {
                        if (op > 0) {
                            stack.push(str.substring(0, sp) + markStr + str.substring(sp));
                        } else {
                            var lastStr = stack[stack.length - 1], lastSp = lastStr.lastIndexOf('<');
                            stack[stack.length - 1] = lastStr.substring(0, lastSp) + markStr + lastStr.substring(lastSp);
                        }
                    } else {
                        if (op > 0) {
                            stack.push(str + markStr);
                        } else {
                            stack.push(markStr);
                        }
                    }

                    markStr = '';
                } else {
                    stack.push(str);
                }
            }

            html = stack.join('');

            if (!markStr) {
                var pos = html.indexOf(mark), prev = html.substring(0, pos),
                    next = html.substr(pos + mark.length),
                    sp = prev.lastIndexOf('<'), ep = prev.lastIndexOf('>');

                if (sp >= 0 && sp > ep) {
                    html = prev.substring(0, sp) + span + prev.substring(sp) + next;
                } else {
                    html = prev + span + next;
                }
            }
        }

        return html;
    });

    editor.hooks.chain('onPreviewRefresh', function () {
        var diff = $('.diff', preview), scrolled = false;

        $('img', preview).load(function () {
            if (scrolled) {
                preview.scrollTo(diff, {
                    offset  :   - 50
                });
            }
        });

        if (diff.length > 0) {
            var p = diff.position(), lh = diff.parent().css('line-height');
            lh = !!lh ? parseInt(lh) : 0;

            if (p.top < 0 || p.top > preview.height() - lh) {
                preview.scrollTo(diff, {
                    offset  :   - 50
                });
                scrolled = true;
            }
        }
    });

    var input = $('#text'), th = textarea.height(), ph = preview.height();

    editor.hooks.chain('enterFakeFullScreen', function () {
        th = textarea.height();
        ph = preview.height();
        $(document.body).addClass('fullscreen');
        var h = $(window).height() - toolbar.outerHeight();

        textarea.css('height', h);
        preview.css('height', h);
    });

    editor.hooks.chain('enterFullScreen', function () {
        $(document.body).addClass('fullscreen');

        var h = window.screen.height - toolbar.outerHeight();
        textarea.css('height', h);
        preview.css('height', h);
    });

    editor.hooks.chain('exitFullScreen', function () {
        $(document.body).removeClass('fullscreen');
        textarea.height(th);
        preview.height(ph);
    });

    editor.run();


    var edittab = $('#md-button-bar').prepend('<div class="md-edittab"><a href="#md-editarea" class="active">Write</a><a href="#md-preview">Preview</a></div>'),
        editarea = $(textarea.parent()).attr("id", "md-editarea");

    $(".md-edittab a").click(function() {
        $(".md-edittab a").removeClass('active');
        $(this).addClass("active");
        $("#md-editarea, #md-preview").addClass("md-hidetab");

        var selected_tab = $(this).attr("href"),
            selected_el = $(selected_tab).removeClass("md-hidetab");


        if (selected_tab == "#md-preview") {
            $("#md-button-row").addClass("md-visualhide");
        } else {
            $("#md-button-row").removeClass("md-visualhide");
        }


        $("#md-preview").outerHeight($("#md-editarea").innerHeight());

        return false;
    });
}