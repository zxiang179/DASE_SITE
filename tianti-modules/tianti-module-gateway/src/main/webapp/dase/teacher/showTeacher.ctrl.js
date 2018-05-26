/**
 * Created by YJL on 2017/12/20.
 */
angular.module('teacherApp',[]).controller('showSingleCtrl',["$scope","$http",showSingleCtrl]);

function showSingleCtrl($scope,$http){
	var vm = $scope;
	
	
	init();
	function init(){
		var teacherid = window.location.search.split("=")[1];
		if(teacherid != null)
			$http({
					url : window.server+"tianti-module-interface/teacher/teacher/"+teacherid,
					method : 'get',
				}).success(function(res) {
					vm.teacher = res;
					vm.teacher.briefInfo = JSON.parse(vm.teacher.briefInfo);
					getImage(res.pict_url);
					vm.teacher.specInfo = vm.teacher.specInfo.substring(1,vm.teacher.specInfo.length-1);
					vm.teacher.papers = vm.teacher.papers.substring(1,vm.teacher.papers.length-1);
					
			});
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
					},
					cache : true
				}).success(function(res) {
					document.getElementById('showImg').src = 'data:image/jpeg;base64,'+res;
		});
	}
}

angular.module('teacherApp').filter("parseHTML",function($sce){
	return function(text){
		return $sce.trustAsHtml(text);
	}
})