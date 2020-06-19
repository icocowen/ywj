
var itemHtmlradio =  '<div class="row item-option">' +
'<div class="col-md-12">'+
    '<div class="custom-control custom-radio">'+
        '<input type="radio"  class="custom-control-input" disabled>'+
        '<label class="custom-control-label option-label" style="width: 90%; height: 100%;">'+
            '<span class="option">{{itemvalue}}</span>'+
        '</label>'+
        '<i style="display: inline-block" class="sub-icon sub-item"></i>'+
      '</div>'+
'</div>'+
'</div>';

var itemHtmlcheckbox = '<div class="row item-option">'+
'<div class="col-md-12">'+
      '<div class="custom-control custom-checkbox">'+
          '<input type="checkbox" class="custom-control-input" disabled>'+
          '<label class="custom-control-label option-label-checkbox"  style="width: 90%; height: 100%;">'+
            '<span class="option">{{itemvalue}}</span>'+
          '</label>'+
          '<i style="display: inline-block" class="sub-icon sub-item"></i>'+
        '</div>'+
'</div>'+
'</div>';

var questionItem =  ' <div class="row justify-content-center mb-4 question-item qi">'+
        '<div class="col-md-10 question-item-area">'+
         ' <div class="row no-gutters h4 question-item-header">'+
           ' <div class="col-md-1">&nbsp;<span class="q-index">{{index}}</span>.</div>'+
            '<div class="col-md-10 ">'+
                '<div class="container ques-title" style="padding-left: 0; padding-right:0;">'+
                 ' {{title}}'+
                  '</div>'+
           ' </div>'+
            '{{sub}}'+
         ' </div>'+
         ' <div class="row question-item-body pl-4" >'+
              '<div class="col-md-10 ">'+
              '{{itemGroup}}'+
             ' </div>'+
         ' </div>'+
        '</div>'+
      '</div>';

// '<div class="row qi">'+
// '<div class="col-md-12" id="question-area">'+

var itemHtml = '<div class="row item-option">'+
'<div class="col-md-12">'+
    '<div class="custom-control {{type}}">'+
       '<input type="{{itemtype}}"  class="custom-control-input" ></input>'+
        '<label class="custom-control-label option-label"  style="width: 90%; height: 100%;">'+
            '<span class="option">{{itemvalue}}</span>'+
       '</label>'+
      '</div>'+
'</div>'+
'</div>';


var sub = '<div class="col-md-1">'+
                '<i style="display: inline-block" class="sub-icon sub-item"></i>'+
            '</div>';

var eleTarget = null;

//修改问题 question-item-area
$('#question-area').on('click','.question-item-area', function(e){
    updateQuestion(e.target);
    resetQuestion(e.target)
})

function resetQuestion(t) {
    eleTarget = $(t).parents('.question-item');
    rememberQi('1');
}



function rememberQi(i) {
    return document.getElementById('qi').value = i;
}
function gainQi() {
    return parseInt(document.getElementById('qi').value);
}

function updateQuestion(t) {
    var questionItem = $(t).parents('.question-item');
    var title = questionItem.find('.ques-title').text().trim();
    title = stripscript(title);
    if(questionItem.find('textarea').length != 0) {
        $('#title-warp-text').text(title);  
        $('#text-modal').modal('show');
        return;
    }

    var items = questionItem.find('.item-option');
    var itemTxt = '';
    if (questionItem.find('input[type="radio"]').length != 0) {
     itemTxt =  assembleItemsRadio(items);
      $('#title-warp').text(title);  
      $('#addbtn-area').before(itemTxt);
      $('#radio-modal').modal('show');
    }else{
        itemTxt =  assembleItemsCheckbox(items);
        $('#title-warp-checkbox').text(title);  
        $('#addbtn-area-checkbox').before(itemTxt);
        $('#checkbox-modal').modal('show');
    }
}

function assembleItemsRadio(items) {
    var itemsTxt = '';
    for (let index = 0; index < items.length; index++) {
        const element = items[index];
        var itemValue = $(element).find('.option').text().trim();
        itemValue = stripscript(itemValue);
        itemsTxt += itemHtmlradio.replace('{{itemvalue}}', itemValue);
    }

    return itemsTxt;
}

function assembleItemsCheckbox(items) {
    var itemsTxt = '';
    for (let index = 0; index < items.length; index++) {
        const element = items[index];
        var itemValue = $(element).find('.option').text().trim();
        itemValue = stripscript(itemValue);
        itemsTxt += itemHtmlcheckbox.replace('{{itemvalue}}', itemValue);
    }

    return itemsTxt;
}



// 删除元素
$('#question-area').on('click','.sub-item', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
    $(e.target).parents('.qi').remove();
    freshIndex();
})

function freshIndex() {
    var qis = $('#question-area').find('.qi');
    setQuestionsNum(qis.length);
    for (let index = 0; index < qis.length; index++) {
        const element = qis[index];
        $(element).find('.q-index').text(index+1);
    }
}



//增加问题
$('.add-question').on('click', function(e){
    if(gainQi() === 0) {
        addQuestion(e.target);
    }{
        replaceQuestion(e.target);
    }
    rememberQi('0');
    freshIndex();
    closeModal(e.target);
});

function replaceQuestion(e) {
    var updateEle = modalToQuestion(e);
    if(eleTarget != null) {
        eleTarget.replaceWith(updateEle);
    }
}


function closeModal(e){
     $(e).parents('.modal').modal('hide');
}

$('#radio-modal').on('hide.bs.modal', function (e) {
    clearOldEle(e.target);
})
$('#checkbox-modal').on('hide.bs.modal', function (e) {
    clearOldEle(e.target);
})

$('#text-modal').on('hide.bs.modal', function (e) {
    clearOldEle(e.target);
})

function clearOldEle(t) {
    var modal = $(t);
    modal.find('.add-question-title').text('标题');
    modal.find('.item-option').remove();
}

function addQuestion(e) {
    var newQuestion = modalToQuestion(e);
    $('#question-area').append(newQuestion);
}

function modalToQuestion(e) {
    var parent = $(e).parent().prev('.modal-body');
    var title = parent.find('.add-question-title').text().trim();
    var items = parent.find('.item-option').not('.add-btn');
    var itemTxt = '';
    var questionSub = '';
    if(parent.hasClass('radio')) {
        itemTxt = processItemRadio(items);
        questionSub = questionItem.replace('{{sub}}', sub);
    }else if(parent.hasClass('checkbox')){
        itemTxt = processItemCheckbox(items);
        questionSub = questionItem.replace('{{sub}}', sub);
    }else if(parent.hasClass('text')){
        questionSub = questionItem.replace('{{sub}}', sub);

        var i = parseInt(getQuestionsNum()) + 1;
        var questionIndex = questionSub.replace('{{index}}', i);
        setQuestionsNum(i);
        var questionTitle = questionIndex.replace('{{title}}', title);
        var newQuestion = questionTitle.replace('{{itemGroup}}', '<textarea class="form-control"  rows="1" disabled></textarea>');
        
        return newQuestion;
    }
   
    var i = parseInt(getQuestionsNum()) + 1;
    var questionIndex = questionSub.replace('{{index}}', i);
    setQuestionsNum(i);
    var questionTitle = questionIndex.replace('{{title}}', title);
    var newQuestion = questionTitle.replace('{{itemGroup}}', itemTxt);
    return newQuestion;
}

function getQuestionsNum(){
    return document.getElementById('questionsNum').value;
}
function setQuestionsNum(i){
    return document.getElementById('questionsNum').value = i;
}

function processItemRadio(items) {
    var itemTxt = '';
    for (let index = 0; index < items.length; index++) {
        var element = items[index];
        // custom-radio
        var option = $(element).find('.option').text().trim();
        var newType = itemHtml.replace('{{itemtype}}', 'radio').replace('{{type}}', 'custom-radio');
        itemTxt += newType.replace('{{itemvalue}}', option);
    }
    return itemTxt;
}



function processItemCheckbox(items) {
    var itemTxt = '';
    for (let index = 0; index < items.length; index++) {
        var element = items[index];
        var option = $(element).find('.option').text().trim();
        var newType = itemHtml.replace('{{itemtype}}', 'checkbox').replace('{{type}}', 'custom-checkbox');;
        itemTxt += newType.replace('{{itemvalue}}', option);
    }
    return itemTxt;
}















$('.radio-title ').on('click','#title-warp', function () {
    txtTransitionToInput("title-inp-new",'.radio-title');
});

$('.checkbox-title').on('click','#title-warp-checkbox', function () {
    txtTransitionToInput("title-inp-new-checkbox",'.checkbox-title');
});
$('.text-title').on('click','#title-warp-text', function () {
    txtTransitionToInput("title-inp-new-text",'.text-title');
});
$('#questionnaire-title').on('click','#title-warp-question', function () {
    txtTransitionToInput("title-inp-new-question",'#questionnaire-title');
});




function txtTransitionToInput(target, warp) {
    var radioArea = $(warp);
    var oldValue = radioArea.text();
    oldValue = stripscript(oldValue);
    var ele = '<input class="form-control " id="'+target+'" value="'+oldValue.trim()+'" type="text" placeholder="输入题目">'
    radioArea.text('').append(ele);
    document.getElementById(target).focus();
}


$('.radio-title').on('blur','#title-inp-new', function (e) {
    inpTransitionToTxt(e, '.radio-title', 'title-warp');
});

$('.checkbox-title').on('blur','#title-inp-new-checkbox', function (e) {
    inpTransitionToTxt(e, '.checkbox-title', 'title-warp-checkbox');
});

$('.text-title').on('blur','#title-inp-new-text', function (e) {
    inpTransitionToTxt(e, '.text-title', 'title-warp-text');
});

$('#questionnaire-title').on('blur','#title-inp-new-question', function (e) {
    inpTransitionToTxt(e, '#questionnaire-title', 'title-warp-question');
});

function inpTransitionToTxt(e, warp, parent) {
    var radioArea = $(warp);
    var newValue = e.target.value.trim();
    newValue = stripscript(newValue);
    if(!newValue) newValue = "标题";
    var ele = '<div class="container add-question-title" id="'+parent+'" style="padding-left: 0; padding-right:0;">'+newValue+'</div>';
    radioArea.text('').append(ele);
}



$('#option-radion-area').on('click','.option', function(e){
    txtTransToInputItem(e.target);
});
$('#option-radion-area').on('blur','.form-control-sm', function(e){
    inputTransTotxtItem(e.target);
});
$('#option-radion-area').on('click','.sub-item', function(e){
    removeItem(e.target);
});


$('#option-checkbox-area').on('click','.option', function(e){
    txtTransToInputItem(e.target);
});
$('#option-checkbox-area').on('blur','.form-control-sm', function(e){
    inputTransTotxtItem(e.target);
});
$('#option-checkbox-area').on('click','.sub-item', function(e){
    removeItem(e.target);
});


function removeItem(e) {
    $(e).parents('.item-option').remove();
}


function inputTransTotxtItem(e) {
    var newValue = e.value.trim();
    var warp = $(e).parent();
    if(!newValue) {
        newValue = "选项值";
    }
    newValue = stripscript(newValue);
    var ele = '<span class="option">'+newValue+'</span>';
    warp.text('').append(ele);
}


function txtTransToInputItem(e) {
    var ele = $(e).parent();
    var txt = ele.text().trim();
    txt = stripscript(txt);
    var txtele = '<input class="form-control form-control-sm" type="text" value="'+txt+'"  placeholder="选项值">';
    ele.text('').append(txtele);
    ele.children('.form-control-sm').focus();
}

function stripscript(s) 
{ 
    var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")        //格式 RegExp("[在中间定义特殊过滤字符]")
    var rs = ""; 
    for (var i = 0; i < s.length; i++) { 
    rs = rs+s.substr(i, 1).replace(pattern, ''); 
    }
    return rs;
}

$('#addbtn').on('click', function() {
    $('#addbtn-area').before(itemHtmlradio.replace('{{itemvalue}}', '选项值'));
});

$('#addbtn-checkbox').on('click', function() {
    $('#addbtn-area-checkbox').before(itemHtmlcheckbox.replace('{{itemvalue}}', '选项值'));
});


//提交设计
$('#submit-design').on('click', function () {
    var data = encodeModel();
    upDesign(data);
});


function encodeModel() {
    var modalstr = '';
    var qntitle = getQnTitle();
    modalstr += encodeQntitle(qntitle);
    var items = getQitem();
    var qiItem = '';
    for (var i = 0; i < items.length; i++) {
        var item = $(items[i]);
        var type = parseType(item);
        var value = parseItemTitle(item);
        var options = parseOption(item);
        qiItem += encodeItemType(type);
        qiItem += encodeItemTitle(value);
        if (options.length !== 0){
            qiItem += encodeItemOption(options);
        }
        qiItem += '§¤§';
    }
    modalstr += qiItem.substr(0, qiItem.length - 3);
    return modalstr;
}


function encodeQntitle(t) {
    return t+"§§0";
}

function encodeItemType(it) {
    return '§;§'+it+'§;§';
}


function encodeItemTitle(it) {
    return '〒§'+it+'§〒';
}

function encodeItemOption(arr) {
    var str = arr[0];
    for (var i = 1; i < arr.length; i++) {
        str += '〒'+arr[i];
    }
    return str;
}



function parseOption(item) {
    var it = item.find('.item-option');
    var value = [];
    for (var i = 0; i < it.length; i++) {
        value[i] = $(it[i]).find('.option').text().trim();
    }
    return value;
}

function parseItemTitle(item) {
    return  item.find('.ques-title').text().trim();
}


// item 时jquery对象
function parseType(item){
    if (item.find('textarea').length !== 0){  //text
        return 'text';
    } 
    var isRadio = item.find('input[type="radio"]').length !== 0;
    if (isRadio){ //radio
        return 'radio';
    }else{  //checkbox
        return 'checkbox';
    } 
}

function getQitem() {
    return $('#question-area .qi');
}


function getQnTitle() {
    return $('#title-warp-question').text().trim();
}




function upDesign(data) {
    $.ajax({
        type: 'POST',
        url: '/save/questionnaire',
        data:  'm='+data ,
        success: function(e){
           location.href = e;
        },
        error: function () {
            alert("新建问卷失败！！");
        }
    });

}



