/**
 * Created by YJL on 2017/12/20.
 */
(function() {
	'use strict';
	var teacherApp = angular.module('teacherApp', [ 'ngFileUpload' ]);
	teacherApp.controller('AddTeacherCtrl', [ '$http', '$scope', 'Upload',
			AddTeacherCtrl ]);
	function AddTeacherCtrl($http, $scope, Upload) {
		var vm = $scope;

		vm.teacher = {};
		vm.teacher.pict_url = "default.jpg";

		vm.reset = reset;
		vm.submit = submit;
		vm.cancel = cancel;
		vm.submitImage = submitImage;
		vm.chooseFile = chooseFile;
		vm.imageChange = imageChange;
		vm.getImage = getImage;

		init();
		function init(){
//			var ueSpecInfo = UE.getEditor('specInfo');
//			var uePapers = UE.getEditor('papers');
			var teacherid = window.location.search.split("=")[1];
			if(teacherid != null)
				$http({
						url : window.server+"tianti-module-interface/teacher/teacher/"+teacherid,
						method : 'get',
					}).success(function(res) {
						vm.teacher = res;
						vm.teacher.briefInfo = JSON.parse(vm.teacher.briefInfo);
						getImage(res.pict_url);
						ueSpecInfo.ready(function() {
						    //设置编辑器的内容
							ueSpecInfo.setContent(vm.teacher.specInfo.substring(1,vm.teacher.specInfo.length-1));
						});
						uePapers.ready(function() {
						    //设置编辑器的内容
							uePapers.setContent(vm.teacher.papers.substring(1,vm.teacher.papers.length-1));
						});
						
				});
			
		}
		
		function cancel() {
			vm.file = null;
		}

		// function getFile() {
		// fileReader.readAsDataUrl($scope.file,$scope) .then(function(result) {
		// vm.teacher.imageSrc = result; });
		// }

		function chooseFile() {
			document.getElementById('hiddenFile').click();
		}

		function reset() {
			document.getElementById("myForm").reset();
		}


		
		
		function submitImage() {
			var param = {};
			param.picture = vm.file;

			Upload
					.upload(
							{
								url : window.server+"tianti-module-interface/teacher/upload-picture",
								data : param,
								method : 'post',
								arrayKey : ''
							}).then(function(data) {
						alert("success");
					}, function(error) {
						alert("error");
					}, function() {

					});
		}

		function submit() {
//			var teacherJson = JSON.stringify(vm.teacher);
//			console.log(teacherJson)
			vm.teacher.specInfo = ueSpecInfo.getContent();
			vm.teacher.papers = uePapers.getContent();
			$http(
					{
						url : window.server+"tianti-module-interface/teacher/save",
						method : 'POST',
						data : {
							'teacherJson' : vm.teacher
						}
					}).success(function(res) {
				alert('保存成功')
			});
		}

		function imageChange(sourceId, target_Id) {
			preImg(sourceId, target_Id);
		}

	
		function getImage(picturename) {
			var params = {};
			
			params.picturename = picturename;
			$http(
					{
						url : window.server+"tianti-module-interface/teacher/picture",
						method : 'GET',
						params : params,
						headers : {
							'responseType' : 'arraybuffer'
						}
					}).success(function(res) {
						document.getElementById('showImg').src = 'data:image/jpeg;base64,'+res;
			});
		}
		
		function getFileUrl(sourceId) {
			var url;
			if (navigator.userAgent.indexOf("MSIE") >= 1) { // IE
				url = document.getElementById(sourceId).value;
			} else if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
				url = window.URL.createObjectURL(document
						.getElementById(sourceId).files.item(0));
			} else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
				url = window.URL.createObjectURL(document
						.getElementById(sourceId).files.item(0));
			}
			return url;
		}

		/**
		 * 将本地图片 显示到浏览器上
		 */
		function preImg(sourceId, targetId) {
			var url = getFileUrl(sourceId);
			
			var imgPre = document.getElementById(targetId);
			imgPre.src = url;
		}
	}

})();
""