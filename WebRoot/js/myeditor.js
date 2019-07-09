//对编辑器的操作最好在编辑器ready之后再做
//	var tb = new Array();//设置工具栏
//	var tb1 = new Array();//设置工具栏
//	tb1.push("fullscreen");
//	tb1.push("source");
//	tb1.push("undo");//撤销
//	tb1.push("redo");//重做
//	tb1.push("bold");//加粗
//	tb1.push("indent");//首行缩进
//	tb.push(tb1);
var ue = UE.getEditor('container');
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function (action) {
    if (action == 'uploadimage') {
        // 这里调用后端我们写的图片上传接口
        return '../image/baiduUploadFile.action';
    } else {
        return this._bkGetActionUrl.call(this, action);
    }
}


