function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
}
String.prototype.trim=function() {  
    return this.replace(/(^\s*)|(\s*$)/g,'');  
}; 
function DataCtrl($scope) {

  switch(testSuits.driverType)
  {

    case 'UIAutomator':
    case 'Selendroid':
    case 'UIAutomation':
      testSuits.appInfo.packageURI=testSuits.appInfo.packageURI.substr(testSuits.appInfo.packageURI.lastIndexOf('\\')+1);
    break;
    case 'ChromeDriver':
    case 'IOSWebkit':
    default:
    break;
  }
  
  $scope.testSuits = testSuits;

  var startd = new Date(Date.parse(testSuits.starttime.replace(/-/g, "/"))); 
  var endd = new Date(Date.parse(testSuits.endtime.replace(/-/g, "/"))); 
  $scope.testSuits['totaltime']=(endd.getTime()-startd.getTime())/1000.0;
  $.each($scope.testSuits['suits'],function (i,suit) {
    suit['id']=md5(JSON.stringify(suit));
    suit['passrate']=suit['total']==0?'':(100*(suit['success'])/(suit['total'])).toFixed(2)+"%";
    $.each(suit['testclass'],function(j,testclass)
      {
        testclass['id']=md5(JSON.stringify(testclass));
        $.each(testclass['cases'],function(j,singleclass)
          {
            singleclass['id']=md5(JSON.stringify(singleclass));
            var startd = new Date(Date.parse(singleclass.starttime.replace(/-/g, "/"))); 
            var endd = new Date(Date.parse(singleclass.endtime.replace(/-/g, "/"))); 
            singleclass['totaltime']=(endd.getTime()-startd.getTime())/1000.0;
          });

      });
  });
}