/// <reference path="common.js" />

window.isLocalhost = window.location.hostname == "localhost";

window.ctx = "";
if (window.location.protocol == "file:") {
    var url = window.location.href.split("/dase/");
    if (url.length == 2) {
        window.ctx = url[0] + "/";
    }
}
else {
    window.ctx = window.location.protocol + "//" + window.location.host + "/dase-module-gateway/";
}


window.root = window.ctx + "dase/";
window.static = window.ctx + "static/";
window.notImgUrl = window.static + "images/img_null.png";

//window.server = "http://58.198.176.86:80/";
//window.server = "http://dase.ecnu.edu.cn";
window.server = "http://localhost:8080/";
window.serverPath = window.server + "tianti-module-interface/";
//后台上传的图片地址
window.serverUploadPath = window.server+"dase-module-admin"

jc.require.url.push(window.static + "js/jquery.pagination.js");
jc.require.url.push(window.static + "js/jquery.nicescroll.min.js");
jc.require.url.push(window.static + "css/common.css");
jc.require.url.push(window.static + "js/uiExtend.js");


jc.require.success = function () {
    if ($("html").niceScroll) {
        $("html").niceScroll({ zindex: 9999, autohidemode: false, cursorwidth: "4px", cursorcolor: "#333", cursorborder: 0, cursoropacitymax: 0.8 });
    }
}


window.routerList = {
    index: window.root + "index/index.html",
    menuAndTextList: window.root + "menuAndTextlist/menuAndTextlist.html",
    menuAndDetail: window.root + "menuAndDetail/menuAndDetail.html",
    //新增老师
//    menuAndTeacherList:window.root+"teacher/single_teacher.html"
    menuAndTeacherList:window.root+"teacher/school.html"
}


window.path = {
    cmsApiColumnList: window.serverPath + "cms/api/column/list",
    cmsApiArticleList: window.serverPath + "cms/api/article/list",
    cmsApiArticleDetail: window.serverPath + "cms/api/article/detail",
    cmsApiArticleNext: window.serverPath + "cms/api/article/next",
    cmsApiArticlePre: window.serverPath + "cms/api/article/pre"
}

window.arraySortASC = function (array, keyName) {

    if (!array) return array;

    array.sort(function (num1, num2) {
        return parseInt(num1[keyName]) - parseInt(num2[keyName]);
    });

    return array;

}


window.resource = function (path, data, fnSuccess, async) {

    var _data = data;

    if (async == undefined) {
        async = true;
    }


    $.ajax({
        url: window.path[path],
        data: data,
        success: function (res) {

            if (!fnSuccess) return false;

            if (res.success) {

                var data = res.data;

                fnSuccess(data);

                if (isLocalhost) {
                    console.log("%c isSuccessTrue : " + window.path[path] + "?" + jc.param.stringify(_data), "color: green", data);
                }
                else {
                    console.log("%c isSuccessFail : " + window.path[path] + "?" + jc.param.stringify(_data), "color: red", res.msg);
                }

            }
            else {
                alert("isNull : " + window.path[path] + "?" + jc.param.stringify(_data));
            }

        },
        error: function (res) {
            console.log("%c isError : " + window.path[path] + "?" + jc.param.stringify(_data), "color: red", res.msg);
        },
        async: async
    });

}



window.router = function (routerListName, param, only) {

    var nowParam = jc.param.getObject();
    var resultParam = "";


    if (!routerListName) {
        routerListName = "index";
    }

    if (only) {
        nowParam = param;
    }
    else {
        for (var attr in param) {
            nowParam[attr] = param[attr];
        }
    }


    resultParam = jc.param.stringify(nowParam);
    if (resultParam) {
        resultParam = "?" + resultParam;
    }

    if (routerListName == "index") {
        resultParam = "";
    }

    window.location.href = window.routerList[routerListName] + resultParam;


    //window.location.href=

}
