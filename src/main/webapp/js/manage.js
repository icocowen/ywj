function changeIcon(t, filename, status, cl, newStatus) {
    var t = $(t).parents('.ev');
    t.children('img').prop({"src":  "../images/"+filename+'.png'});
    t.find('.new-status').text(newStatus);
    t.parents('.question-brief').find('.status-item')
        .removeClass('badge-success badge-warning badge-secondary')
        .addClass(cl).text(status);
}