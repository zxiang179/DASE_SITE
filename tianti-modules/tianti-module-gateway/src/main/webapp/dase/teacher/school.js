var app = angular.module('school', ['ui.bootstrap']);
app.controller('schoolCtrl',['$scope', '$http',schoolCtrl]);
function schoolCtrl($scope, $http) {
	var vm = $scope;
	vm.numPerPage = 10;
	vm.maxSize = 5;
	vm.currentPage = 0;
	
	vm.pageChange = pageChange;
	vm.getImage = getImage;
	init();
	function init(){
		getPagedList(0);
	}
	
	function pageChange(){
		getPagedList(vm.currentPage-1);
	}
	
    
    function getPagedList(currentPage){
    	var params = window.location.search.split("?")[1].split("&");
    	var type = 3;
    	for(var i in params){
    		if(params[i].split("=")[0] == "type")
    			type = params[i].split("=")[1];
    	}
    	$http({
            method: 'GET',
            url: window.server+'tianti-module-interface/teacher//teacher-list',
            params:{
            	"current":currentPage,
            	"size":$scope.numPerPage,
            	"type":type
            }
        }).then(function successCallback(res) {
        	vm.total = res.data.totalElements;
            vm.teachers = res.data.content;
            for(var i=0;i<vm.teachers.length;i++){
    			vm.teachers[i].briefInfo = JSON.parse(vm.teachers[i].briefInfo);
    			var content = vm.teachers[i].briefInfo;
    			if(vm.teachers[i].briefInfo.tel==undefined){
    				vm.teachers[i].briefInfo.tel = "无";
    			}
    			content = "联系电话："+vm.teachers[i].briefInfo.tel+"\n 办公时间："+vm.teachers[i].briefInfo.officeHours+", 电子邮件："+vm.teachers[i].briefInfo.email+" ,办公地点："+vm.teachers[i].briefInfo.officeAddress+" ,通讯地址："+vm.teachers[i].briefInfo.postalAddress;
    			content = content.substring(0,60)+"...";
    			vm.teachers[i].tel = "联系电话："+vm.teachers[i].briefInfo.tel;
    			vm.teachers[i].briefInfo = content;
    			vm.getImage(vm.teachers[i].pict_url,vm.teachers[i]);
    			vm.teachers[i].href = window.server+"dase-module-gateway/dase/teacher/single_teacher.html?teacherId="+vm.teachers[i].id;
            }
            
        }, function errorCallback(response) {
            // 请求失败执行代码
        });
    }
    
    
    
    function getImage(picturename,teacher) {
		$http({
					url : window.server+"/tianti-module-interface/teacher/picture?picturename="+picturename,
					method : 'GET',
					headers : {
						'responseType' : 'arraybuffer',
						'Content-type' : 'text/plain, */*'
					}
				}).success(function(res) {
					teacher.picture = 'data:image/jpeg;base64,'+res;
				}).error(function(error){
					teacher.picture = 'data:image/jpeg;base64,'+res;
				});
	}
    
}
