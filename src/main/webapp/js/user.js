$('#user-card').on('click','#motto', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
    eleTrinsToInp('#motto', 'motto-inp');
})

$('#user-card').on('blur','#motto-inp', function(e){
    inpTrinsToTxt('#motto', 'motto-inp');
    e.stopPropagation();
    e.stopImmediatePropagation();
    updatemotto();
})

$('#user-card').on('click','#motto-inp', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
})


$('#user-card').on('click','.bind-email', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
    eleTrinsToInp('#email', 'email-inp');
})

$('#user-card').on('blur','#email-inp', function(e){
    inpTrinsToTxt('#email', 'email-inp');
    e.stopPropagation();
    e.stopImmediatePropagation();
})

$('#user-card').on('click','#email-inp', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
})


$('#user-card').on('click','.bind-phnum', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
    eleTrinsToInp('#phnum', 'phnum-inp');
})

$('#user-card').on('blur','#phnum-inp', function(e){
    inpTrinsToTxt('#phnum', 'phnum-inp');
    e.stopPropagation();
    e.stopImmediatePropagation();
    updateNum();
})

$('#user-card').on('click','#phnum-inp', function(e){
    e.stopPropagation();
    e.stopImmediatePropagation();
})



function inpTrinsToTxt(warp, target) {
    var ele = $(warp);
    var newMotto = document.getElementById(target).value.trim();
    ele.text(newMotto);
}

$('#confirm-alter').on('click', function () {
    var oldpwd = $('#old-pwd').val().trim();
    var newpwd = $('#new-pwd').val().trim();
    var confirmpwd = $('#confirm-pwd').val().trim();
    if (newpwd !== confirmpwd) {
        alert('新密码与确认密码不匹配');
        $('#confirm-pwd').focus();
    }
    http("/update", {_method: 'PUT', "ol": oldpwd, "npwd": newpwd, "confirm": confirmpwd,"t":"ap"}, function (m) {
        if (m === '更新成功') {
            $('#alter-dowm').collapse('hide');
            alert(m);
        }else {
            alert(m);
        }
    })
});


function eleTrinsToInp(warp, target) {
    var ele = $(warp);
    var motto = ele.text().trim();
    var eleTxt = '<input class="form-control  form-control-sm" id="'+target+'" value="'+motto+'" type="text" placeholder="格言">';
    ele.text('').append(eleTxt);
    document.getElementById(target).focus();
}

function updateNum() {
    var num = getNum();
    if (num === '')  return;
    http('/update', {_method: "PUT", "phnum":num, "t":"pn"}, function (m) {
        if (m === "更新成功")  $('.bind-phnum').remove();
        $('#phnum').parents('[data-toggle="popover"]').attr({
            'data-content' : m
        }).popover('show');
        setTimeout(function () {
            $('#phnum').parents('[data-toggle="popover"]').popover('hide');
        }, 2000);
    });
}


function updatemotto() {

    var motto = $('#motto').text().trim();
    if (base === motto) return;
    if (motto === '')  return;
    http('/update', {_method: "PUT", "motto":motto, "t":"mt"}, function (m) {
        $('#motto').attr({'data-content':m}).popover('show');
        setTimeout(function () {
            $('#motto').popover('hide');
        }, 2000);
    });
}

function getNum() {
    return $('#phnum').text().trim();
}

function http( url ,data, fn) {
    $.ajax({
        type: "post",
        url: url,
        data:  data,
        success: function(e){
            if (fn) {
                fn(e);
            }
        },
        error: function () {
            alert("操作失败!");
        }
    });

}

$('#start-upload-avatar').on('click' , function () {
    uploadFile ();
});


function uploadFile(){
    var formData = new FormData();
    var imgFile = document.getElementById('select-avatar').files[0]; // 获取图片文件

    formData.append('newAvatar', imgFile);

    if(imgFile){
        var formarea = $('#form-area');
        formarea.hide();
        $('.progress').show();

        var url = "/uploadAvatar";
        xhr = new XMLHttpRequest();
        xhr.open("post",url,true);
        // xhr.onload = uploadComplete; //请求完成
        xhr.onerror =  uploadFailed; //请求失败
        xhr.upload.onprogress = progressFunction;
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                if (xhr.responseText === 'false') {
                    $('.progress-plan').removeClass('bg-success').addClass('bg-danger');
                }else {
                    setTimeout(function () {
                        location.reload();
                    }, 1000);
                }
            }

        };
        xhr.send(formData);
    }else{
        $('#select-avatar').click();
    }
}


function uploadFailed() {
    $('.progress-plan').removeClass('bg-success').addClass('bg-danger');
    alert("上传失败！");
}

$('#avatar-modal').on('show.bs.modal', function (e) {
    $('.progress').hide();
    $('#form-area').show();
});

function progressFunction(e) {
    var bar = $('.progress-plan');
    var res = e.loaded/e.total * 100;
    if (res >= 1) {
        bar.removeClass('bg-danger').addClass('bg-success');
    }
    bar.width( res+ '%');
}




//

