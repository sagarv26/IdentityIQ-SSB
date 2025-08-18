//csvDownloadLink
//certBackBtn

var jsonconfigdata=null;

function getLang(){
return (navigator.languages && navigator.languages.length) ? navigator.languages[0] : navigator.language;
}

jQuery(document).ready(function(){

formatHTMLTags();
initSmallScreenOptions();
initPluginOptions();

});

function formatHTMLTags() {
if (jQuery("sp-column-data").length)
{
//console.log("changing class");
jQuery("sp-column-data > span").addClass("sweCustTextHide");

jQuery("sp-column-data").find('span').each(function() {
if (!$(this).find(".swecertscompleteaccessinfo").length)
{
//console.log("fixing html content formatHTMLTags");
var eachstr=$(this).text();
if(eachstr.indexOf("class=\"swecertscompleteaccessinfo\"") != -1)
{
$(this).text('');
$(this).append(eachstr);
}
}
});

jQuery("sp-column-data > span").removeClass("sweCustTextHide");

}
else
//if page not loaded then wait
setTimeout(formatHTMLTags,500);
}

function formatInnerHTMLTags(thisObj) {
if (!thisObj.find(".swecertscompleteaccessinfo").length)
{
//console.log("fixing html content formatInnerHTMLTags");
thisObj.find("sp-column-data > span").addClass("sweCustTextHide");
thisObj.find('span').each(function() {
var eachstr=$(this).text();
if(eachstr.indexOf("class=\"swecertscompleteaccessinfo\"") != -1)
{
$(this).text('');
$(this).append(eachstr);
//alert("found ICORE::: "+ $(this).text());
}

});
thisObj.find("sp-column-data > span").removeClass("sweCustTextHide");
}
}

function loadPluginOptions() {

formatHTMLTags();

if(jsonconfigdata.REMOVESELECTEVERYTHING == true)
initRemoveEverythingDOMEvent();

if(jsonconfigdata.SHOW_FEEDBACK_IN_SIGNOFF_PAGE == true)
initFeedbackOptions();

initFormatExpandCollapse();

if(jsonconfigdata.SHOW_CUSTOM_SIGNOFF_BTN == true)
initCustomSignoffButton();

if(jsonconfigdata.ROLE_WIZARD_OPTION == true)
initEntOwnerCertRevokeOptions();


if(jsonconfigdata.SHOW_EXPAND_ALL == true)
{
initAddExpandCollapseAll();
initBannerLegend();
}

if(jsonconfigdata.SHOW_FIXED_HEADER == true)
initFixedHeader();

if(jsonconfigdata.SHOW_CUSTOM_FILE_EXPORT == true)
loadCustomFileExportOptions();

if(jsonconfigdata.SHOW_FILE_DECISIONS_IMPORT == true)
loadDecisionUploadOptions();



if(jsonconfigdata.SHOW_UNCOMPLETED_CERT_CBT_POPUP == true)
loadUnCompletedCBT();



//if(jsonconfigdata.DISPLAY_PREVALENCE_SCORES == true && jsonconfigdata.PREVALENCE_SCORES_CACHE != true)
// refreshPrevalenceScoreCacheforallCertUsers();

}

function initBannerLegend() {
if (jQuery("sp-data-table").length)
{
addBannerLegend();
jQuery("body").on('DOMSubtreeModified', "sp-data-table",function() {
addBannerLegend()
//resetSelectAllButton();
});
}
else
//if page not loaded then wait
setTimeout(initBannerLegend,1000);
}

function addBannerLegend()  {
//console.log("in addBannerLegend: "+ jQuery(".custcertslegend").length);

//if(!jQuery("#panelHeadingLeftuiCertificationItemWorksheetColumns-DecisionsLeft").length)
if(jQuery("sp-data-table").length)
{
if (!jQuery("#swecustomCertsBanner").length)
{
var custombannerHtml='';
if(undefined != jsonconfigdata.CUSTOM_BANNER_MSG && jsonconfigdata.CUSTOM_BANNER_MSG.length)
custombannerHtml='<div id="swecustomCertsBanner">'+
'<span class="customCertsBannerContent"><span class=\"fa fa-bullhorn swecustomCertsBannerIcon\"/><span class="customCertsBannerText">'+jsonconfigdata.CUSTOM_BANNER_MSG+'</span>'+
'</span></span>';



if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true)
custombannerHtml='';


if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true && (jsonconfigdata.SHOW_ENT_OWNER_CERT_BANNER_POSITIVE == false || jsonconfigdata.SHOW_ENT_OWNER_CERT_BANNER_POSITIVE == true))
custombannerHtml='<div id="swecustomCertsBanner">'+
'<span class="customCertsBannerContent"><span class=\"fa fa-bullhorn swecustomCertsBannerIcon\"/><span class="customCertsBannerTextPositingClickable">'+jsonconfigdata.CUSTOM_BANNER_MSG+'</span>'+
'</span></span>';

if(custombannerHtml.length)
jQuery("sp-data-table").parent().parent().prepend(custombannerHtml);
}

/*
if(!jQuery("#custCertsLegend").length)
{
//console.log("adding legend..");

var accessgainedinprevrolehtml="";
if(jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS != true)
accessgainedinprevrolehtml='<tr><td><span class="legend-jobchange-violation" >Granted in Prior Job</span></td><td><span class="legend-Description">User recently transferred roles,  but granted access prior to that</span></td></tr>';

var sharedmailboxhtml="";
if(jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS == true)
sharedmailboxhtml='<tr><td><span class="legend-access-SharedMailBox" ><span class=\"fa fa-envelope flagaccessinfoicon\"/>Shared Mail Box</span></td><td><span class="legend-Description">Any Shared Mail Box</span></td></tr>';



var legendHtml='<legend id="custCertsLegend" class="custcertslegend">'+
'<table class="table table-striped m-b-none text-md">'+
'<thead>'+
'<tr>'+
'<th style="width: 20%;"><span class="legend-header" title="These flags may appear in the certification and below is the description">Legend</span></th>'+
'<th style="width: 80%;"><span class="legend-header"></span></th>'+
'</tr>'+
'</thead>'+
'<tbody>'+
'<tr><td><span class="legend-access-priv" ><span class="fa fa-warning legendicon"/>HIGH RISK</span></td><td><span class="legend-Description">Any SOX or Privileged access will be tagged as High Risk</span></td></tr>'+
//'<tr><td><span class="legend-access-cbt" >CBT EXPIRED</span></td><td><span class="legend-Description">Access Tagged with Computer Based Training (CBT) requirements expired for the User</span></td></tr>'+
accessgainedinprevrolehtml+
//'<tr><td><span class="legend-plain-last-Used" ><span class="fa fa-clock-o accessinfoicon"/>Last Login</span> </td><td><span class="legend-Description">User last logged into this access less than 3 months ago.  NOTE: Login information is currently only available for AWS and Sphinx entitlements.</span></td></tr>'+
'<tr><td><span class="legend-less-stale-last-Used" ><span class="fa fa-clock-o accessinfoicon"/> Last Login</span> </td><td><span class="legend-Description">User last logged into this access more than 3 months ago.  NOTE: Login information is currently only available for AWS and Sphinx entitlements.</span></td></tr>'+
'<tr><td><span class="legend-stale-account" ><span class="fa fa-clock-o accessinfoicon"/> Last Login</span> </td><td><span class="legend-Description">User last logged into this access more than 6 months ago.  NOTE: Login information is currently only available for AWS and Sphinx entitlements.</span></td></tr>'+
//'<tr><td><span class="legend-never-used" >Last Login: Never</span> </td><td><span class="legend-Description">User has never logged into this access and was granted access less than 6 months ago.  NOTE: Login information is currently only available for AWS and Sphinx entitlements.</span></td></tr>'+
'<tr><td><span class="legend-stale-account" >Last Login: Never</span> </td><td><span class="legend-Description">User has never logged into this access and was granted access more than 6 months ago.  NOTE: Login information is currently only available for AWS and Sphinx entitlements.</span></td></tr>'+sharedmailboxhtml+
'</tbody></table></legend>';


jQuery("sp-data-table").parent().parent().append(legendHtml);

//console.log("adding legend Completed");
}
*/
}
}

function getEntOwnerAccessDescFeedbackResults() {

payload = {
certId: jsonconfigdata.CERT_ID
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getEntOwnerAccessDescFeedbackResults');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);


if(undefined != resultData)
{
google.charts.load("current", {packages:["corechart"]});
if(jQuery("#accessDescFeedbackDetailsModalDialog").length)
jQuery(accessDescFeedbackDetailsModalDialog).remove();

if(!jQuery("#accessDescFeedbackDetailsModalDialog").length)
{
createEntOwnerAccessDescFeedbackModal(resultData);
accessDescFeedbackApplyPagination(resultData);
}
else
{
//if(jQuery("#accessDescFeedbackPagination").length)
//jQuery("#accessDescFeedbackPagination").children().remove();
//accessDescFeedbackApplyPagination(resultData);
}
}
else
SailPoint.EXCEPTION_ALERT("Failed to get Access Description Feedback Details");


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to get Role Details");
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});


/* var accessDescFeedbackDetailsdata=[{"accessId" : "123", "appName" : "ABS suite","displayName" : "Read Only","description" : "this gives access to ABS suite pagination jQuery plugin and included js file, cdn bootstrap path into head section of index. Step 2: Created HTML table listing into index. Step 3: We will create ajax jquery method to get data from rest call and store into global js variable.","attribute" : "role","rating" : [["Reason","Rating"],["Helpful",5],["Too Technical",2],["Missing Info",6],["Both",3]]},{"accessId" : "124","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "125","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "126","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "127","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "128","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "129","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "130","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "131","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "132","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance pagination jQuery plugin and included js file, cdn bootstrap path into head section of index. Step 2: Created HTML table listing into index. Step 3: We will create ajax jquery method to get data from rest call and store into global js variable.","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]}];
*/


}

function accessDescFeedbackApplyPagination(accessDescFeedbackDetailsdata) {
//console.log("in accessDescFeedbackApplyPagination : "+accessDescFeedbackDetailsdata);
var totalRecords = 0;
var records = [];
var displayRecords = [];
var recPerPage = 4;
var page = 1;
var totalPages = 0;

records = accessDescFeedbackDetailsdata;
//console.log(records);
totalRecords = records.length;
totalPages = Math.ceil(totalRecords / recPerPage);

jQuery("#accessDescFeedbackPagination").twbsPagination({
totalPages: totalPages,
visiblePages: 6,
onPageClick:function (event, page) {
displayRecordsIndex = Math.max(page - 1, 0) * recPerPage;
endRec = (displayRecordsIndex) + recPerPage;

displayRecords = records.slice(displayRecordsIndex, endRec);
getEntOwnerAccessDescFeedbackModal(displayRecords);
}
});

//getEntOwnerAccessDescFeedbackModal(accessDescFeedbackDetailsdata);
}

function createEntOwnerAccessDescFeedbackModal(accessDescFeedbackDetailsdata) {

var accessDescFeedbackDetailshtml='';

if(accessDescFeedbackDetailsdata.length < 1) {
accessDescFeedbackDetailshtml= '<div class="panel-heading">'+
'<h4 class="spContentTitle">Failed to get Access Description Feedback</h4>'+
'</div>';
}
else {
accessDescFeedbackDetailshtml='<div class="panel-heading">'+
'<h4 class="spContentTitle">Head over to  <span class="accessdescCertFeedbackMyAssetsLink" onClick="accessDescFeedbackRedirect()">My Assets</span> to remediate these issues before the next Metadata Certification campaign</h4>'+
'</div>'+

'<div role="presentation" class="panel ng-isolate-scope">'+
'<div role="presentation" class="table-responsive-wide-title ng-scope accessDescFeedbackTabdiv">'+
'<div class="accessDescFeedbackTabSection">'+
'<table class="table table-striped m-b-none text-md roledetailstable">'+
'<thead>'+
'<tr>'+
'<th style="width:20%" class="ng-binding">Application</th>'+
'<th style="width:10%"  class="ng-binding">Attribute</th>'+
'<th style="width:20%"  class="ng-binding">Access</th>'+
'<th style="width:40%" class="ng-binding">Description</th>'+
'<th style="width:10%"  class="ng-binding">Rating</th>'+
'</tr>'+
'</thead>'+
'<tbody id="accessDescFeedbackDetailsBody">';



accessDescFeedbackDetailshtml=accessDescFeedbackDetailshtml+'</tbody>'+
'</table>'+
'</div>'+
'<div>'+
'<ul id="accessDescFeedbackPagination" class="pagination-sm"></ul>'+
'</div>'+
'</div>'+
'</div>';
}

var customDownloadBtn='<a id="accessDescFeedbackDownloadLink" role="button" type="button" class="btn btn-white btn-sm" title="Download Access Description Feedback" onclick="accessDescFeedbackDownloadtoFile()"><span class="fa fa-download fa-lg accessDescFeedbackDownload" role="presentation"></span><img class="accessDescFeedbackDownloadInprogress" href="#"  src="../images/progress.gif" alt="Progress"></img></a>';

var accessDescFeedbackDetailModalDialog='<div id="accessDescFeedbackDetailsModalDialog" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+
'<div class="modal-header">'+
'<button data-dismiss="modal" type="button" class="close ng-scope customRoleDetailsClose" aria-hidden="true" tabindex="50">×</button>'+
'<h4 class="modal-title ng-binding" id="CustomaccessDescFeedbackDetailsmodalTitle"><span id="CustomaccessDescFeedbackDetailsmodalTitleSpan">Manager Description Rating Summary</span></h4>'+
'</div>'+
'<div class="modal-body custroledetailsmodalbody">'+
'<div class="accessdescfeedbackLegend">'+
'<span class="accessdescfeedHelpfulbox"></span><span class="accessdescfeedHelpfullbl">Helpful</span>'+
'<span class="accessdescfeedTechbox"></span><span class="accessdescfeedTechlbl">Too Technical</span>'+
'<span class="accessdescfeedMissingbox"></span><span class="accessdescfeedMissinglbl">Missing Info</span>'+
'<span class="accessdescfeedBothbox"></span><span class="accessdescfeedBothlbl">Too Technical & Missing Info</span>'+
customDownloadBtn+
'</div>'+
'<div id="accessDescFeedbackDetailsSection" class="offliceCerts panel m-b panel-default ng-scope">'+accessDescFeedbackDetailshtml+


'</div>'+

'</div>'+

'</div>'+
'</div>'+
'</div>';

if (jQuery( "#csvDownloadLink" ).length && !jQuery("#accessDescFeedbackDetailsModalDialog").length)
{
jQuery("#csvDownloadLink").each(function() {
$(this).parent().append(accessDescFeedbackDetailModalDialog);
});
}

}

function accessDescFeedbackRedirect() {
window.open(jsonconfigdata.ENT_OWNER_MY_ASSETS_URL, "_blank", "x=y");
}

function accessDescFeedbackDownloadtoFile() {
jQuery(".accessDescFeedbackDownload").hide();
jQuery(".accessDescFeedbackDownloadInprogress").show();

/*var accessDescFeedbackDetailsdata=[{"accessId" : "123", "appName" : "ABS suite","displayName" : "Read Only","description" : "this gives access to ABS suite","attribute" : "role","rating" : [["Reason","Rating"],["Helpful",5],["Too Technical",2],["Missing Info",6],["Both",3]]},{"accessId" : "124","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "125","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "126","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "127","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",1],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "128","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "129","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "130","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "131","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]},{"accessId" : "132","appName" : "Finance","displayName" : "Reports","description" : "this gives access to Finance","attribute" : "group","rating" : [["Reason","Rating"],["Helpful",10],["Too Technical",4],["Missing Info",3],["Both",2]]}];
*/

payload = {
certId: jsonconfigdata.CERT_ID
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/downloadEntOwnerAccessDescFeedbackResults');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);


if(undefined != resultData)
{
var csvContent ='';
resultData.forEach(function(eachRow) {
csvContent +=eachRow+"\r\n";
});
//downloadFile(csvContent, resultData.certName+'.csv');
downloadFile(csvContent, "AccessDesciptionFeedbackExport_"+new Date().toLocaleString()+'.csv');
}
else
SailPoint.EXCEPTION_ALERT("Failed to Download Access Description Feedback Details");

jQuery(".accessDescFeedbackDownload").show();
jQuery(".accessDescFeedbackDownloadInprogress").hide();

},
failure:function(transport){
jQuery(".accessDescFeedbackDownload").show();
jQuery(".accessDescFeedbackDownloadInprogress").hide();
SailPoint.EXCEPTION_ALERT("Failed to get Role Details");
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);

}
});






}

function getEntOwnerAccessDescFeedbackModal(accessDescFeedbackDetailsdata) {
//console.log("in getEntOwnerAccessDescFeedbackModal : "+accessDescFeedbackDetailsdata);


var accessDescFeedbackDetailshtml='';

if(accessDescFeedbackDetailsdata.length)
{

jQuery.map( accessDescFeedbackDetailsdata,function(eachAccess) {

var feedbackmetaData=
'<div class="roleDetailscompleteaccessinfo">'+
'<span class="accessdispName">'+
//'<span class="accessinfoicon"> <span class="fa fa-lock"></span></span>'+
'<span class="eachAccessDispName"><b>'+eachAccess.displayName+'</b></span>';

if(undefined != eachAccess.isHighRisk && eachAccess.isHighRisk)
feedbackmetaData=feedbackmetaData+'<span ng-if="eachAccess.isHighRisk" class="roleDetailsaccess-priv" title="This is a High Risk Entitlement">HIGH RISK</span>';

if(undefined != eachAccess.cbtinfo && eachAccess.cbtinfo.length > 1)
feedbackmetaData=feedbackmetaData+'<span class="roleDetailsaccess-cbt" title="'+eachAccess.cbtinfo+'">CBT</span>';

if(undefined != eachAccess.isNPI && eachAccess.isNPI)
feedbackmetaData=feedbackmetaData+'<span ng-if="eachAccess.isNPI" class="roleDetailsaccess-npi" title="Non-Public Information" >NPI</span>';

if(undefined != eachAccess.isPCI && eachAccess.isPCI)
feedbackmetaData=feedbackmetaData+'<span ng-if="eachAccess.isPCI" class="roleDetailsaccess-pci" title="Payment Card Information">PCI</span>';

feedbackmetaData=feedbackmetaData+'</span>'+
'</div>';

accessDescFeedbackDetailshtml=accessDescFeedbackDetailshtml+  '<tr class="ng-scope">'+
'<td title="Access Application Name" class="ng-binding">'+eachAccess.appName+

'</td>'+
'<td title="Access Attribute" class="ng-binding">'+eachAccess.attribute+

'</td>'+
'<td title="Access Display Name" class="ng-binding">'+feedbackmetaData+

'</td>'+
'<td title="Access Description" class="ng-binding">'+eachAccess.description+

'</td>'+


'<td title="Rating" class="ng-binding">'+
'<div id="pieChart_'+eachAccess.accessId+'"><img class="accessDescFeedbackChartLoadInprogress" href="#"  src="../images/progress.gif" alt="Progress"></img></div>'+
'</td>'+
'</tr>';
});


}


if(jQuery("#accessDescFeedbackDetailsModalDialog").length)
{
if(jQuery("#accessDescFeedbackDetailsBody").length)
{
jQuery("#accessDescFeedbackDetailsBody").children().remove();
jQuery("#accessDescFeedbackDetailsBody").append(accessDescFeedbackDetailshtml);
//jQuery("#CustomaccessDescFeedbackDetailsmodalTitleSpan").remove();
//jQuery("#CustomaccessDescFeedbackDetailsmodalTitle").append('<span id="CustomaccessDescFeedbackDetailsmodalTitleSpan">Access Details - '+roleDispName+'</span>');
jQuery("#accessDescFeedbackDetailsModalDialog").modal({keyboard: true, show: true});
jQuery(".accessDescFeedbackChartLoadInprogress").show();
}
}

if(accessDescFeedbackDetailsdata.length)
jQuery.map( accessDescFeedbackDetailsdata,function(eachAccess) {
//console.log("eachAccess.rating123: "+eachAccess.rating);
google.charts.setOnLoadCallback(function () {
accessDescFeedbackdrawChart(eachAccess.accessId, eachAccess.rating);
});
});

}

function accessDescFeedbackdrawChart(eachAccessDescFeedbackChartId, eachAccessDescFeedbackChartData) {
var data = google.visualization.arrayToDataTable(eachAccessDescFeedbackChartData);
var options = {
pieHole: 0.4,
legend: 'none',
slices: [{color: '#C869E1'},{color: '#51BE30'},{color: '#E0A56A'},{color: '#EFCE3B'}],
chartArea:{left:0,top:0,width:'100%',height:'100%'},
width: 70,
height: 70
};
//console.log("accessDescFeedbackdrawChart: "+eachAccessDescFeedbackChartId);
var chart = new google.visualization.PieChart(document.getElementById("pieChart_"+eachAccessDescFeedbackChartId));
chart.draw(data, options);
jQuery(".accessDescFeedbackChartLoadInprogress").hide();
}

function loadBasicOptionsonPluginError() {
console.log("in loadBasicOptionsonPluginError");
if (jQuery("sp-column-data").length)
{
formatHTMLTags();
if(jQuery(".swecertscompleteaccessinfo").length)
{
formatExpandCollapse();
// register dom event for any change in column data
//needs to improve this if its hitting performance. this is called for each row and column of the table
jQuery("body").on('DOMNodeInserted', "sp-column-data",function() {
formatExpandCollapseEvent($(this));
//resetSelectAllButton();
});

initAddExpandCollapseAll();
}
}
else
//if page not loaded then wait
setTimeout(loadBasicOptionsonPluginError,1000);

}

function initFeedbackOptions() {
if (jQuery( "#certSignOffOverlayBtn" ).length)
{
loadFeedBackOptions();
}
else
//if page not loaded then wait
setTimeout(initFeedbackOptions,1000);
}

function initCustomSignoffButton() {
if (jQuery( "#certSignOffOverlayBtn" ).length)
{
loadsystemIDCertRevokeOptions();

}
else
//if page not loaded then wait
setTimeout(initCustomSignoffButton,1000);
}

function initEntOwnerCertRevokeOptions() {
if (jQuery( "#certSignOffOverlayBtn" ).length)
{
loadEntOwnerCertRevokeOptions();

}
else
//if page not loaded then wait
setTimeout(initEntOwnerCertRevokeOptions,1000);
}

function removeGroupByCheckbox(thisObj) {

if (thisObj.length)
{
thisObj.each(function() {

var checkbox=$(this);
checkbox.children().last().remove();

});
}
else
//if page not loaded then wait
setTimeout(removeGroupByCheckbox,1000);
}

function intiremoveGroupByCheckbox() {

if (jQuery(".bg-section-header.checkbox-col.ng-scope").length)
{
jQuery(".bg-section-header.checkbox-col.ng-scope").each(function() {

var checkbox=$(this);
checkbox.children().last().remove();

});
}
else
//if page not loaded then wait
setTimeout(intiremoveGroupByCheckbox,1000);
}

////cbt popup////
function loadUnCompletedCBT() {

if (jQuery( "#csvDownloadLink" ).length)
{

jQuery("#csvDownloadLink").each(function() {

var optionshtml=getUnCompletedCBTModal();


$(this).parent().append(optionshtml);
jQuery("#UnCompletedCBTModalDialog").modal({backdrop: "static"});

});
}
else
//if page not loaded then wait
setTimeout(loadUnCompletedCBT,1000);
}

function certsCustomGetRoleDetails(roleId) {

payload = {
roleId: roleId
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getRoleDetails');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);


if(undefined != resultData && undefined != resultData.roleAccessDetails)
getRoleDetailsModal(resultData.roleDispName,resultData.roleAccessDetails);
else
SailPoint.EXCEPTION_ALERT("Failed to get Role Details");


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to get Role Details");
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function getRoleDetailsModal(roleDispName, roleDetailsdata) {
//console.log("in getRoleDetailsModal length: "+jQuery("#roleDetailsModalDialog").length);


var roleDetailshtml='';

if(roleDetailsdata.length < 1)
{
roleDetailshtml= '<div class="panel-heading">'+
'<h4 class="spContentTitle">Role is Empty or Access Dynamically Calculated</h4>'+
'</div>';
}
else
{



roleDetailshtml='<div class="panel-heading">'+
'<h4 class="spContentTitle">Below are the (<b>'+roleDetailsdata.length+'</b>) different access that make up this Role</h4>'+
'</div>'+

'<div role="presentation" class="panel ng-isolate-scope">'+
'<div role="presentation" class="table-responsive-wide-title ng-scope roledetailstablediv">'+
'<table class="table table-striped m-b-none text-md roledetailstable">'+
'<thead>'+
'<tr>'+
'<th style="width:30%"  class="ng-binding">Access Name</th>'+
'<th style="width:40%" class="ng-binding">Description</th>'+
'<th style="width:20%" class="ng-binding">Application</th>'+
'<th style="width:10%"  class="ng-binding">Attribute</th>'+
'</tr>'+
'</thead>'+
'<tbody>';

jQuery.map( roleDetailsdata,function(eachAccess) {

var rolemetaData=
'<div class="roleDetailscompleteaccessinfo">'+
'<span class="accessdispName">'+
'<span class="accessinfoicon"> <span class="fa fa-lock"></span></span>'+
'<span class="eachAccessDispName"><b>'+eachAccess.displayName+'</b></span>';

if(undefined != eachAccess.isHighRisk && eachAccess.isHighRisk)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isHighRisk" class="roleDetailsaccess-priv" title="This is a High Risk Entitlement">HIGH RISK</span>';

if(undefined != eachAccess.cbtinfo && eachAccess.cbtinfo.length > 1)
rolemetaData=rolemetaData+'<span class="roleDetailsaccess-cbt" title="'+eachAccess.cbtinfo+'">CBT</span>';

if(undefined != eachAccess.isNPI && eachAccess.isNPI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isNPI" class="roleDetailsaccess-npi" title="Non-Public Information" >NPI</span>';

if(undefined != eachAccess.isPCI && eachAccess.isPCI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isPCI" class="roleDetailsaccess-pci" title="Payment Card Information">PCI</span>';

rolemetaData=rolemetaData+'</span>'+
'</div>';

roleDetailshtml=roleDetailshtml+  '<tr class="ng-scope">'+
'<td title="Access Display Name" class="ng-binding">'+rolemetaData+

'</td>'+
'<td title="Access Description" class="ng-binding">'+eachAccess.description+

'</td>'+
'<td title="Access Application Name" class="ng-binding">'+eachAccess.appName+

'</td>'+
'<td title="Access Attribute" class="ng-binding">'+eachAccess.attribute+

'</td>'+
'</tr>';
});

roleDetailshtml=roleDetailshtml+'</tbody>'+
'</table>'+
'</div>'+
'</div>';
}

var roleDetailModalDialog='<div id="roleDetailsModalDialog" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+
'<div class="modal-header">'+
'<button data-dismiss="modal" type="button" class="close ng-scope customRoleDetailsClose" aria-hidden="true" tabindex="50">×</button>'+
'<h4 class="modal-title ng-binding" id="CustomRoleDetailsmodalTitle"><span id="CustomRoleDetailsmodalTitleSpan">Access Details - '+roleDispName+'</span></h4>'+
'</div>'+
'<div class="modal-body custroledetailsmodalbody">'+
'<div id="roledetailsinstrs">'+
'<div id="roleDetailsSection" class="offliceCerts panel m-b panel-default ng-scope">'+roleDetailshtml+



'</div>'+
'</div>'+

'</div>'+

'</div>'+
'</div>'+
'</div>';

if (jQuery( "#csvDownloadLink" ).length && !jQuery("#roleDetailsModalDialog").length)
{
jQuery("#csvDownloadLink").each(function() {
$(this).parent().append(roleDetailModalDialog);
//jQuery("#roleDetailsModalDialog").modal({backdrop: false ,keyboard: true, show: true});
jQuery("#roleDetailsModalDialog").modal({keyboard: true, show: true});

});
}
else if(jQuery("#roleDetailsModalDialog").length)
{
if(jQuery("#roleDetailsSection").length)
jQuery("#roleDetailsSection").children().remove();
jQuery("#roleDetailsSection").append(roleDetailshtml);
jQuery("#CustomRoleDetailsmodalTitleSpan").remove();
jQuery("#CustomRoleDetailsmodalTitle").append('<span id="CustomRoleDetailsmodalTitleSpan">Access Details - '+roleDispName+'</span>');
jQuery("#roleDetailsModalDialog").modal({keyboard: true, show: true});
}
/*
jQuery("#roleDetailsCancelBtn").on( 'click',function () {
if(jQuery("#roleDetailsModalDialog").length)
jQuery("#roleDetailsModalDialog").children().remove();
});

*/
}

function getUnCompletedCBTModal() {

var CertsModalDialog='<div id="UnCompletedCBTModalDialog" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+
'<div class="modal-header">'+
'<h4 class="modal-title ng-binding" id="modalTitle">User Access Certification CBT Error</h4>'+
'</div>'+
'<div class="modal-body">'+
'<div class="formError">You have not completed User Access Certification CBT required course(s) and please follow below steps.</div>'+
'<div id="offliceCertsInstrs">'+
'<div class="offliceCerts panel m-b panel-default ng-scope">'+
'<div class="panel-heading">'+
'<h4 class="spContentTitle">Instructions to Complete CBT</h4>'+
'</div>'+

'<ul class="list-group">'+
'<li class="list-group-item ng-binding">In order to access and complete your certification, please visit <a href="https://learning.capitalone.com">One Learn</a> and complete the following CBT "<b>'+jsonconfigdata.CERT_CBT_STRING+'</b>", once completed, you will be able to access and complete your certification.</li>'+
'</ul>'+

'</div>'+
'</div>'+

'</div>'+
'</div>'+
'</div>'+
'</div>';

return CertsModalDialog;
}

function loadCustomFileExportOptions() {

if (jQuery( "#csvDownloadLink" ).length)
{

var csvdownloadParentNode=jQuery("#csvDownloadLink").parent();
jQuery("#csvDownloadLink").remove();

var customDownloadBtn='<a id="csvDownloadLink" role="button" type="button" class="btn btn-white btn-sm" title="Download Certifications Page" onclick="customCertsDownloadtoFile()"><span class="fa fa-download fa-lg swecertDownload" role="presentation"></span><img class="swecertDownloadInprogress" href="#"  src="../images/progress.gif" alt="Progress"></img></a>';



csvdownloadParentNode.append(customDownloadBtn);

}
else
//if page not loaded then wait
setTimeout(loadCustomFileExportOptions,1000);
}

////off-line file upload////
function loadDecisionUploadOptions() {

if (jQuery( "#csvDownloadLink" ).length)
{

jQuery("#csvDownloadLink").each(function() {

var optionshtml='<button id="sweimportfiledecisions" role="button" type="button" class="btn btn-white btn-sm" title="Import Decisions"><span class="fa fa-upload fa-lg" role="presentation" data-toggle="modal" data-backdrop="static"  data-target="#OfflineCertsModalDialog"></span></button>';


optionshtml=optionshtml+getOfflineCertsUploadModal();


$(this).parent().append(optionshtml);

offlineCertsAddButtonEvents();

});
}
else
//if page not loaded then wait
setTimeout(loadDecisionUploadOptions,1000);
}

var filedownloadInprogress=false;

function customCertsDownloadtoFile() {
if(!filedownloadInprogress)
customCertsDownloadtoFileAPICall();
}

function customCertsDownloadtoFileAPICall() {
filedownloadInprogress=true;
jQuery(".swecertDownload").hide();
jQuery(".swecertDownloadInprogress").show();
payload = {
certId: jsonconfigdata.CERT_ID
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/certDownloadtoFile');
if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true)
resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/entOwnerCertDownloadtoFile');

// code to fix the browser time on large file export
Ext.Ajax.timeout= 900000; //15 mins
Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//console.log("success: "+transport.responseText);
if(null != transport.responseText && undefined != transport.responseText)
{
var resultData = Ext.JSON.decode(transport.responseText);
if(undefined != resultData.fileData && undefined != resultData.certName)
{
//console.log("resultData: "+resultData);
//console.log("fileData: "+resultData.fileData);
//console.log("certName: "+resultData.certName);
var csvContent ='';
resultData.fileData.forEach(function(eachRow) {
csvContent +=eachRow+"\r\n";
});
//console.log("csvContent: "+csvContent);
/* var hiddenElement = document.createElement('a');
hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csvContent);
hiddenElement.target = '_blank';
hiddenElement.download = resultData.certName+'.csv';
hiddenElement.click();
*/
downloadFile(csvContent, resultData.certName+'.csv');
filedownloadInprogress=false;
}
else
{SailPoint.EXCEPTION_ALERT("Error while downloading the file: Missing Download Data"); filedownloadInprogress=false; }

}
else
{SailPoint.EXCEPTION_ALERT("Error while downloading the file: Empty Response"); filedownloadInprogress=false; }

jQuery(".swecertDownload").show();
jQuery(".swecertDownloadInprogress").hide();
},
failure:function(transport){
console.log("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
SailPoint.EXCEPTION_ALERT("Error while downloading the file");

jQuery(".swecertDownload").show();
jQuery(".swecertDownloadInprogress").hide();
}
});
}

function downloadFile(data, fileName) {
var csvData = data;
var blob = new Blob([ csvData ], {
type : "application/csv;charset=utf-8;"
});

if (window.navigator.msSaveBlob) {
// FOR IE BROWSER
navigator.msSaveBlob(blob, fileName);
} else {
// FOR OTHER BROWSERS
var link = document.createElement("a");
var csvUrl = URL.createObjectURL(blob);
link.href = csvUrl;
link.style = "visibility:hidden";
link.download = fileName;
document.body.appendChild(link);
link.click();
document.body.removeChild(link);
}
}

function offliceCertsAddActionButtonsBack() {
var buttonHTML='<button type="button" id="offlineCertsCancelBtn" class="btn  btn-cancel"  data-dismiss="modal">Cancel</button>'+
'<button type="button" id="offlineCertsSubmitBtn" class="btn  btn-info btn-default">Submit</button>';
jQuery("#offlineCertsFooter").append(buttonHTML);
offlineCertsAddButtonEvents();
}

function offlineCertsAddButtonEvents() {
jQuery("#offlineCertsSubmitBtn").on( 'click',function () {

offlineCertInsertInprogress();
parseCSVFile();
});
jQuery("#offlineCertsCancelBtn").on( 'click',function () {

offlineCertsResetModal();
if(undefined != tempfileUploadStatsJson)
window.location.reload();
});


}

function offlineCertInsertInprogress(validationMessage)
{
jQuery(".sweofflineCertsValidation").children().remove();
var validationHTML='<div id="offlineCertsProgressSection"><div class="spContentTitle">Upload Results</div><div class="offlineCertsProgress" ><img href="#"  src="../images/progress.gif" alt="Progress"></div></div>';
jQuery(".sweofflineCertsValidation").append(validationHTML);

}

function offlineCertsResetModal()
{
jQuery(".sweofflineCertsValidation").children().remove();
jQuery("#swefileuploadinputele").val('');
}
function offliceCertsDisplayValidation(validationMessage)
{
offlineCertsResetModal();
var validationHTML='<div class="spContentTitle">Upload Results</div><div><ul><li class="formError">'+validationMessage+'</li></ul></div>';
jQuery(".sweofflineCertsValidation").append(validationHTML);

}
function offliceCertsDisplayStatusMessage()
{
jQuery("#offliceCertsInstrs").children().remove();
jQuery("#offliceCertsInputFile").children().remove();
jQuery("#offlineCertsFooter").children().remove();
var buttonHTML='<button type="button" id="offlineCertsOkBtn" class="btn  btn-cancel"  data-dismiss="modal">Ok</button>';
jQuery("#offlineCertsFooter").append(buttonHTML);
jQuery("#offlineCertsProgressSection").children().remove();



jQuery("#offlineCertsOkBtn").on( 'click',function () {

//jQuery("#csvDownloadLink").parent().remove('#OfflineCertsModalDialog');
window.location.reload();
});

}


function offliceCertsFileUploadResults(fileUploadStatsJson) {
offlineCertsResetModal();

var validationHTML='<div class="swecertsfileuploadstats">'+
'<div class="spContentTitle">Upload Results</div>'+
'<ul class="alert alert-info">'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Line Items In the Uploaded File: </b> '+rowsinuploadedfile+'</li>'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Items Approved using this Upload: </b> '+ fileUploadStatsJson.numberofitemsapproved+'</li>'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Items Revoked using this Upload: </b> '+ fileUploadStatsJson.numberofitemsrevoked+'</li>'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Line Items Pending to Complete: </b> '+ fileUploadStatsJson.OpenItems+'</li>'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Line Items in the Certification: </b> '+ fileUploadStatsJson.TotalItems+'</li>'+
'<li class="list-group-item ng-binding"><b class="ng-binding">Total Number of Line Items Completed: </b> '+ fileUploadStatsJson.CompletedItems+'</li>'+
'</ul>';

if(fileUploadStatsJson.isCertCompleted==true)
{
validationHTML=validationHTML+'<div class="alert alert-success"><span class="swecertscompletedlabel">All the line items have been successfully decisioned. Click '+"'OK'"+' to view the Certification Page and to complete the certification by signing off on your decisions.</span></div>';

/*
// to show feedback options in file upload popup
if(jsonconfigdata.SHOW_FEEDBACK_IN_SIGNOFF_PAGE == true)
{

var validationHTML=validationHTML+'<div class="swecertsfeedbackdivUniversal" >';
validationHTML=validationHTML+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(5)"></span>';
validationHTML=validationHTML+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(4)"></span>';
validationHTML=validationHTML+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog"onclick="feedbackOnlclickActions(3)"></span>';
validationHTML=validationHTML+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(2)"></span>';
validationHTML=validationHTML+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(1)"></span>';
validationHTML=validationHTML+'<span class="Universalfeedbacklabel"> :Feedback</span></div>';

validationHTML=validationHTML+getfeedbackModalHTML();
addFeedbackmodalbuttonEvents();

}
*/

offliceCertsDisplayStatusMessage();
}

else
{

if(undefined != fileUploadStatsJson && fileUploadStatsJson.numberofitemsapproved == 0 && fileUploadStatsJson.numberofitemsrevoked == 0)
validationHTML=validationHTML+'<div class="alert alert-warning"><span class="swecertscompletedlabel">Please check the file for '+"'ItemId'"+' Column as none of the line items are decisioned. Download the file again and upload with '+"'Decision'"+' column updated or click '+"'Cancel'"+' to action items manually from the user interface</span></div>'
else
validationHTML=validationHTML+'<div class="alert alert-warning"><span class="swecertscompletedlabel">Some of the line items in the Certification has been successfully decisioned which can be shown under completed tab. Undecided line items shown in the Open tab. Please click '+"'Cancel'"+' to action items manaully from the user interface or select another file and click submit to complete the certification</span></div>'


offliceCertsAddActionButtonsBack();
}

validationHTML=validationHTML+'</div></div>'


jQuery(".sweofflineCertsValidation").append(validationHTML);
}

function getOfflineCertsUploadModal() {
var exportButton="";
if(jsonconfigdata.SHOW_CUSTOM_FILE_EXPORT == true)
exportButton='<a id="csvDownloadLink" role="button" type="button" class="btn btn-white btn-sm" title="Download Certifications Page" onclick="customCertsDownloadtoFile()"><span class="fa fa-download fa-lg swecertDownload" role="presentation"></span><img class="swecertDownloadInprogress" href="#"  src="../images/progress.gif" alt="Progress"></img></a>';
else
exportButton='<a id="csvDownloadLink" ng-href="'+certificationURL+'" ng-click="ctrl.downloadCSV()" class="btn btn-white btn-sm" href="'+certificationURL+'"><span class="fa fa-download fa-lg" role="presentation"></span><span class="sr-only ng-binding">Export certification to file</span></a>';



var offliceCertsModalDialog='<div id="OfflineCertsModalDialog" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+
'<div class="modal-header">'+
'<h4 class="modal-title ng-binding" id="modalTitle">Upload Offline Certification Decisions File</h4>'+
'</div>'+
'<div class="modal-body">'+
'<div id="offliceCertsInstrs">'+
'<div class="offliceCerts panel m-b panel-default ng-scope">'+
'<div class="panel-heading">'+
'<h4 class="spContentTitle">File Instructions</h4>'+
'</div>'+

'<ul class="list-group">'+
'<li class="list-group-item ng-binding">Click on '+exportButton+'  to Download the CSV File.</li>'+
'<li class="list-group-item ng-binding">Update the last two columns (Decision, Decision Comment), <span class="swecertlastlastcol">Decision</span> with <span class="instrsApprove">Approve</span> or <span class="instrsRevoke">Revoke</span> and Optional <span class ="swecertlastcol">Decision Comment</span> with Justification</li><li class="list-group-item ng-binding">Upload rest of the file with the same format.</li></ul>'+

'</div>'+
'</div>'+

'<div id="offliceCertsInputFile"><div class="spContentTitle">Import CSV File </div>'+
'<div class="swefileuploadinput">'+
'<input type="file" id="swefileuploadinputele" name="swefileuploadinputfilename">'+
'</div></div>'+

'<div class="sweofflineCertsValidation"></div>'+
'</div>'+

'<div id="offlineCertsFooter" class="modal-footer bg-light lt ng-scope">'+
'<button type="button" id="offlineCertsCancelBtn" class="btn  btn-cancel"  data-dismiss="modal">Cancel</button>'+
'<button type="button" id="offlineCertsSubmitBtn" class="btn  btn-info btn-default">Submit</button>'+
'</div>'+
'</div>'+
'</div>'+
'</div>';

return offliceCertsModalDialog;
}

/*
var offlineCertsModule = angular.module('offlineCerts', ['ui.bootstrap']);

offlineCertsModule.controller('offlineCertsController',function($scope,$uibModal) {

$scope.doFileUploadAction =function(template) {
alert ("sss");

}
});
*/

var rowsinuploadedfile=0;
function parseCSVFile() {
var validationmsg='';
var importedFile = jQuery("#swefileuploadinputele")[0].files[0];
if (importedFile){
var fileName=importedFile.name;

var index = fileName.lastIndexOf(".");
var strsubstring = fileName.substring(index, fileName.length);

if (strsubstring == '.csv')
{
if (typeof (FileReader) != "undefined") {
var reader = new FileReader();
reader.onload =function (e) {

var fileDataArray = new Array();
var rows = e.target.result.split("\r\n");
var validationerror=false;
var isapproveorrevoke=false;
rowsinuploadedfile=rows.length-1;
for (var i = 0; i < rows.length; i++) {

if(rows[i].trim() != '')
{
var cells = rows[i].split(",");
if(cells.length <3)
{
validationmsg="Total Number of columns less than 3 in Row "+ (i+1);
validationerror=true;
break;
}

if(i==0)
{
if(cells[0] !='ItemId' || cells[cells.length-2] !='Decision' || cells[cells.length-1] !='Decision Comment')
{
validationmsg="Failed to upload because data is missing from columns (insert all columns) <b>ItemId</b>,  <b>Decision</b>, and <b>Decision Comment</b>.";
validationerror=true;
break;
}
}

var eachRow = {};

eachRow.Decision = cells[cells.length-2];

if(eachRow.Decision.toLowerCase() == 'approve' || eachRow.Decision.toLowerCase() == 'approved' || eachRow.Decision.toLowerCase() == 'certify' || eachRow.Decision.toLowerCase() == 'certified' || eachRow.Decision.toLowerCase() == 'revoke' || eachRow.Decision.toLowerCase() == 'reject' || eachRow.Decision.toLowerCase() == 'revoked' || eachRow.Decision.toLowerCase() == 'rejected')
{
isapproveorrevoke=true;
eachRow.ItemId = cells[0];
eachRow.DecisionComments = cells[cells.length-1];
fileDataArray.push(eachRow);
}
}
}

if(validationerror)
offliceCertsDisplayValidation(validationmsg);
else if (!isapproveorrevoke)
{
validationmsg="None of the Decision column rows populated with Approve or Revoke";
offliceCertsDisplayValidation(validationmsg);
}
else if(fileDataArray.length > 0)
{
//alert (fileDataArray);
//alert(reader.result);
//var csvFileJson = JSON.stringify(reader.result);
//var csvFileJson =JSON.parse(JSON.stringify(reader.result));
//alert(csvFileJson);
var csvFileJson = JSON.stringify(fileDataArray);
invokeFileImportRestAPI(csvFileJson);
}
}
reader.readAsText(importedFile);
} else {
validationmsg="This browser does not support HTML5.";
offliceCertsDisplayValidation(validationmsg);
}
} else {
validationmsg="Please upload a valid CSV file.";
offliceCertsDisplayValidation(validationmsg);
}
}
else{
validationmsg="File not found.";
offliceCertsDisplayValidation(validationmsg);
}
}

var tempfileUploadStatsJson;
function invokeFileImportRestAPI(csvFileJson) {
jQuery("#offlineCertsFooter").children().remove();

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/certsImportDecisions');

payload = {
certId: jsonconfigdata.CERT_ID,
csvFileJson:csvFileJson
};

// code to fix the browser time on large file import
Ext.Ajax.timeout= 900000; //15 mins
Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){


var fileUploadStatsJson= Ext.JSON.decode(transport.responseText);
offliceCertsFileUploadResults(fileUploadStatsJson);
tempfileUploadStatsJson=fileUploadStatsJson;

},
failure:function(transport){
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error While uploading the file");

var validationHTML='<div class="alert alert-info"><span class="swecertsinprogresslabel">File Upload Process is still running in the background with '+rowsinuploadedfile+' Rows and please check the Certifications page after some time for status. Click on Ok to view Certifications Page</span></div>';
jQuery(".sweofflineCertsValidation").append(validationHTML);
offliceCertsDisplayStatusMessage();

}
});


}

function initApproveRevokeMoreButtons() {
if (jQuery("i.fa.fa-bars").length || jQuery(".cert-action-Approved").length || jQuery(".cert-action-Remediated").length)
{

}
else
//if page not loaded then wait
setTimeout(initApproveRevokeMoreButtons,1000);
}

function initFormatExpandCollapse() {
if (jQuery("sp-column-data").length)
{
formatExpandCollapse();
// register dom event for any change in column data
//needs to improve this if its hitting performance. this is called for each row and column of the table
jQuery("body").on('DOMNodeInserted', "sp-column-data",function() {
formatExpandCollapseEvent($(this));
//resetSelectAllButton();
});

}
else
//if page not loaded then wait
setTimeout(initFormatExpandCollapse,1000);
}

function initSmallScreenOptions() {


jQuery("body").on('DOMNodeInserted', ".more-less-toggle.ng-isolate-scope",function() {
formatExpandCollapseEventSmallScreen($(this));
//resetSelectAllButton();
});
/*
jQuery("body").on('DOMNodeInserted', "sp-certification-item-card",function() {
formatExpandCollapseEventSmallScreen($(this));
//resetSelectAllButton();
});

jQuery("body").on('DOMNodeInserted', "sp-certification-entity-list",function() {
formatExpandCollapseEventSmallScreen($(this));
//resetSelectAllButton();
});
*/
}

function formatExpandCollapseEventSmallScreen(thisObj) {
var window_width = $(window).width();
//console.log("on small screen: window_width: "+window_width);
//console.log("swecertscompleteaccessinfo length: " +thisObj.find(".swecertscompleteaccessinfo").length);
//if (!thisObj.find(".swecertscompleteaccessinfo").length)
{
//console.log("fixing html content");
thisObj.removeAttr( "text" );
//console.log("thisObj.text(): "+ thisObj.text());
thisObj.find('span').each(function() {
var eachstr=$(this).text();
if(eachstr.indexOf("class=\"swecertscompleteaccessinfo\"") != -1)
{
$(this).text('');
$(this).append(eachstr);
//alert("found ICORE::: "+ $(this).text());
$(this).attr( "ng-bind-html","text | encodeHtml" );
//$(this).parent().removeClass().addClass("ng-isolate-scope");
//$(this).parent().last().remove();
}
//console.log("$(this).find('.Role').length: "+$(this).find('.Role').length);
if($(this).find('.Role').length)
$(this).find('.Role').remove();
$(this).find('.sweexpcollsymbolsection').hide();
$(this).find('.eachRoleAccessDetails').hide();
$(this).find('.custDescFeedbackYesNoSection').hide();
});
}
}

// need to check the performance of this as we added lot of events for resetSelectAllButton
function initAddExpandCollapseAll() {
if (jQuery("thead").length && jQuery(".normal-col.ng-binding.ng-scope").length)
{
addExpandCollapseAll();


jQuery("body").on('DOMSubtreeModified', ".normal-col.ng-binding.ng-scope",function() {
addExpandCollapseAllEvent($(this));
});



/*
///////comment this section in case of performance issue and go with click events
jQuery("body").on('DOMSubtreeModified', ".pagination-sm.ng-untouched.ng-valid.ng-isolate-scope.pagination.ng-not-empty.ng-dirty.ng-valid-parse",function() {

resetSelectAllButton();
});


jQuery("body").on('DOMSubtreeModified', ".cert-id-list.data-table.ng-scope",function() {

resetSelectAllButton();
});

jQuery("body").on('DOMSubtreeModified', ".dropdown-menu.dropdown-menu-right.group-by-dropdown-menu",function() {

resetSelectAllButton();
});
*/

}
else
//if page not loaded then wait
setTimeout(initAddExpandCollapseAll,1000);
}

function initRemoveEverythingDOMEvent() {

//remove select every thing only if the total items to be reviewed is < x number
if (jQuery(".current-page-info.ng-binding").length)
{

jQuery("body").on('DOMSubtreeModified', ".dropdown.open",function() {
removeSelectEverything($(this));
//alert("selecteverytingchagned");
});

//remove group by select all check box
//intiremoveGroupByCheckbox();
/*
jQuery("body").on('DOMSubtreeModified', ".bg-section-header.checkbox-col.ng-scope",function() {
removeGroupByCheckbox($(this));
//alert("selecteverytingchagned");
});
*/

///////Ent owner cert self certify usecase to handle checking unchecking the select check box
if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true)
{
jQuery("body").on('DOMSubtreeModified', ".selfReviewItem",function() {
//console.log("unchecking the self item..");
$(this).removeClass("fa-check-square-o").addClass("fa-square-o");
});
}

}
else
//if page not loaded then wait
setTimeout(initRemoveEverythingDOMEvent);
}

////////this method is to remove "Select Every Thing" option///////
function removeSelectEverything(thisObj) {

thisObj.parent().find('.dropdown-menu').each(function() {
if($(this).children().length && $(this).children().last().length && $(this).children('li').length > 1 && ($(this).children().last().text().trim().indexOf("Select Everything") != -1 || $(this).children().last().text().indexOf("Deselect Everything") != -1))
{
//alert($(this).children('li').last().text());
$(this).children().last().remove();
}
});
}

function initFixedHeader() {

if (jQuery(".data-table-full").length && jQuery("sp-column-data").length)
{
var tableEle=jQuery(".data-table-full").find("table");

// console.log("tableEle: "+tableEle);
if(undefined != tableEle)
{
$(this).scroll(function() {
tableEle=jQuery(".data-table-full").find("table");
// CloneDecisionDiv(".panel-heading.data-table-heading","#uiCertificationItemWorksheetColumns-DecisionsLeft");
if(jQuery(".data-table.panel").length && jQuery(".data-table-overlap").length)
{
CloneHeader(tableEle,".data-table.panel");
}
//CloneHeader2(".table",".data-table.panel");


});
}
}
else
//if page not loaded then wait
setTimeout(initFixedHeader,1000);
}

function CloneHeader(tableEle, containertableid) {

//var scroll = jQuery(containertableid).scrollTop();
var table_top = tableEle.offset().top;
//var footer_top = jQuery(".panel-footer.data-table-footer.ng-scope").offset().top;

var scroll = $(window).scrollTop();


//if (scroll!=0)
if(scroll > table_top)//(table_top-jQuery(".panel-heading.data-table-heading").height()))
{
//alert (height);
//console.log("fixed header satisfied scroll: "+ scroll +"table_top: "+ table_top);

clone_table = jQuery("#certstableheaderclone");
if(clone_table.length == 0){
clone_table = tableEle.clone(true);
// clone_table.css({position:'fixed','pointer-events': 'none',top:0});
// clone_table.css({position:'fixed',top:0});
//  clone_table.css({position:'fixed',top:jQuery(".panel-heading.data-table-heading").parent().height()});
//jQuery("#certstableheaderclone").css({visibility:'hidden'});
//jQuery("#certstableheaderclone thead").css({visibility:'visible'});
//clone_table.height(jQuery(tableid +" thead").height());
clone_table.attr('id', 'certstableheaderclone');
clone_table.width(jQuery(containertableid).width());// + jQuery(datatableoverlap).width());
jQuery(containertableid).append(clone_table);



// Find the max width of each column across all tables. Only look at 'th' or 'td' cells in the first row of each table.
var col_widths = [];
var col_heights = [];
// var col_class = [];
tableEle.find("thead").find("tr:first th").each(function(index1, element2) {
col_widths[index1]= Math.max(col_widths[index1] || 0, jQuery(element2).width());
col_heights[index1]= Math.max(col_heights[index1] || 0, jQuery(element2).height());
//col_class[index1]=jQuery(element2).attr('class');
});
//alert (col_widths2);
//alert (jQuery("#certstableheaderclone thead").find("tr:first th").length);
jQuery("#certstableheaderclone thead").find("tr:first th").each(function(index2, element3) {
if(index2==0)
{
jQuery(element3).attr('id',"selectAllCheckbox");
jQuery(element3).children().last().remove();
}
else if(index2==jQuery("#certstableheaderclone thead").find("tr:first th").length-1)
jQuery(element3).attr('id',"lastcloneheadercolumn");
else
jQuery(element3).attr('id',"cloneheadercolumn");

jQuery(element3).width(col_widths[index2]);
jQuery(element3).height(col_heights[index2]+12);



//jQuery(element3).height(jQuery(tableid +" thead"));
//jQuery(element3).attr('class',col_class[index2]);
});
}
}
else
{
jQuery("#certstableheaderclone").remove();
}

}

/*
function CloneDecisionDiv(tableid, containertableid)
{

//var scroll = jQuery(containertableid).scrollTop();
var table_top = jQuery(tableid).offset().top;
//var footer_top = jQuery(".panel-footer.data-table-footer.ng-scope").offset().top;

var scroll = $(window).scrollTop();
//if (scroll!=0)
if(scroll > table_top)
{
//alert (height);

clone_table = jQuery("#certsdecisiondivclone");
if(clone_table.length == 0){
clone_table = jQuery(tableid).clone(true);
// clone_table.css({position:'fixed','pointer-events': 'none',top:0});
clone_table.css({position:'fixed',top:0});
//clone_table.width(jQuery(tableid).width());
clone_table.width(jQuery(containertableid).width());



//clone_table.width(jQuery("#certsdecisiondivclone").width()-1500);
clone_table.attr('id', 'certsdecisiondivclone');
jQuery(containertableid).append(clone_table);
jQuery("#certsdecisiondivclone").css({visibility:'visible'});
// jQuery("#certsdecisiondivclone thead").css({visibility:'visible'});



}
}
else
{
jQuery("#certsdecisiondivclone").remove();
}

}
*/

function addExpandCollapseAll() {

if(!jQuery( "#swecertsexpand-all" ).length && !jQuery( "#swecertscollapse-all" ).length)
{
jQuery( "thead" ).find("th").each(function() {

if($(this).text() !=null && $(this).text().trim() =="Access Description")
{
$(this).prepend('<span id="swecertscollapse-all" class=\"fa fa-angle-up swecertscollapse-all\" title="Collapse All"></span>');
$(this).prepend('<span id="swecertsexpand-all" class=\"fa fa-angle-down swecertsexpand-all\" title="Expand All"></span>');

jQuery( "#swecertscollapse-all").hide();

expandCollapseAllOnClickEvent($(this));
}

});
}
}

function expandCollapseAllOnClickEvent(thisObj) {

thisObj.click(function() {

var expbutton=thisObj.find(".swecertsexpand-all");
var colbutton=thisObj.find(".swecertscollapse-all");
if(expbutton.is(":visible"))
{
expbutton.hide();
colbutton.show();
forceExpand();
}
else
{
expbutton.show();
colbutton.hide();
forceCollapse();
}

});
}

function addExpandCollapseAllEvent(thisObj) {

if(!jQuery( "#swecertsexpand-all" ).length && !jQuery( "#swecertscollapse-all" ).length)
{
thisObj.each(function() {

if($(this).text() !=null && $(this).text().trim() =="Access Description")
{

$(this).prepend('<span id="swecertscollapse-all" class=\"fa fa-angle-up swecertscollapse-all\" title="Collapse All"></span>');
$(this).prepend('<span id="swecertsexpand-all" class=\"fa fa-angle-down swecertsexpand-all\" title="Expand All"></span>');

jQuery( "#swecertscollapse-all").hide();

expandCollapseAllOnClickEvent($(this));
}

});
}
}

function resetSelectAllButton() {
var expbutton=jQuery(".swecertsexpand-all");
var colbutton=jQuery(".swecertscollapse-all");
//alert (colbutton.is(":visible"));
if(colbutton.is(":visible"))
{
var colbutton=jQuery(".swecertscollapse-all");
expbutton.show();
colbutton.hide();
forceCollapse();
}

}

function forceCollapse() {

jQuery(".expcol-accessdesccls").each(function() {

var colbutton=$(this).find(".sweexpcollsymbol2");
if(colbutton.length)
colbutton.removeClass().addClass("fa fa-angle-down sweexpcollsymbol");

//$(this).parent().find(".access-details").toggle("slow");
$(this).parent().find(".access-details").hide();
});
}

function forceExpand() {

jQuery(".expcol-accessdesccls").each(function() {

var expbutton=$(this).find(".sweexpcollsymbol");
if(expbutton.length)
{
expbutton.removeClass().addClass("fa fa-angle-up sweexpcollsymbol2");
//registerRoleAccessDetailsClickEventExpand($(this).parent());
}

//$(this).parent().find(".access-details").toggle("slow");
$(this).parent().find(".access-details").show();
});
}

function eachForceExpandEvent(thisObj) {

thisObj.find('.expcol-accessdesccls').each(function() {

var expbutton=$(this).find(".sweexpcollsymbol");
if(expbutton.length)
{
expbutton.removeClass().addClass("fa fa-angle-up sweexpcollsymbol2");
//registerRoleAccessDetailsClickEventExpand($(this).parent());
}

//$(this).parent().find(".access-details").toggle("slow");
$(this).parent().find(".access-details").show();
});
}

/////this method is to format the expand collapse content/////
function formatExpandCollapseEvent(thisObj) {

formatInnerHTMLTags(thisObj);



thisObj.find('.swecertscompleteaccessinfo').each(function() {
registerOnClickEvents($(this));

// load the flags and other
var accessinfosection=$(this);

if(undefined != accessinfosection)
{

var certItemId = accessinfosection.children().first().attr('class');
var accessId = accessinfosection.children().first().next().attr('class');
var accessType = accessinfosection.children().first().next().next().attr('class');
//console.log("certItemId: "+ certItemId);
var nobutton=accessinfosection.find(".swecustDescFeedbackNo");
var feedbacksection=accessinfosection.find('.custDescFeedbackYesNoSection');
//console.log("nobutton: "+ nobutton.length);
if(null != jsonconfigdata && jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS != true && (nobutton.length || !feedbacksection.length))
{
if(feedbacksection.length && undefined != accessId && undefined != accessType && (accessType == 'Ent' || accessType == 'Role'))
getExistingAccessDescFeedbackRating($(this),certItemId,accessId);
}

if($(this).find(".dummy-otherdata-flag").length && $(this).find(".dummy-otherdata-info").length)
getOtherData(thisObj,certItemId,accessId);
if($(this).find(".dummy-prev-flag").length && $(this).find(".dummy-prev-info").length && jsonconfigdata.DISPLAY_PREVALENCE_SCORES)
getprevalenceScoresData(thisObj,certItemId,accessId);



}
});


// if collapse button enabled then show expand everthing on each page click or other events
var colbutton=jQuery(".swecertscollapse-all");
if(colbutton.is(":visible"))
eachForceExpandEvent(thisObj);


if(undefined != jsonconfigdata && null != jsonconfigdata && jsonconfigdata.HIDE_PREV_REVOKES)
{
thisObj.find(".cert-item-warnings > .text-danger.ng-binding.ng-scope > .fa.fa-exclamation-triangle").css({color:'white'});
thisObj.find(".cert-item-warnings > .text-danger.ng-binding.ng-scope").css({color:'white'});
}


//removeAttributeFromDisplayNameDOMEvent(thisObj);

}

/////this method is to format the expand collapse content/////
function formatExpandCollapse() {


jQuery(".swecertscompleteaccessinfo").each(function() {
registerOnClickEvents($(this));

// add flags and other
var accessinfosection=$(this);
if(undefined != accessinfosection)
{
var certItemId = accessinfosection.find('span:first').attr('class');
var accessId = accessinfosection.find('span:nth-child(2)').attr('class');
var accessType = accessinfosection.find('span:nth-child(3)').attr('class');
//console.log("certItemId: "+ certItemId);
var feedbacksection=accessinfosection.find('.custDescFeedbackYesNoSection');

if(null != jsonconfigdata && jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS != true && feedbacksection.length && undefined != accessId && undefined != accessType && (accessType == 'Ent' || accessType == 'Role'))
getExistingAccessDescFeedbackRating($(this),certItemId,accessId);

if($(this).find(".dummy-otherdata-flag").length && $(this).find(".dummy-otherdata-info").length)
getOtherData($(this),certItemId,accessId);

if($(this).find(".dummy-prev-flag").length && $(this).find(".dummy-prev-info").length && jsonconfigdata.DISPLAY_PREVALENCE_SCORES)
getprevalenceScoresData($(this),certItemId,accessId);

}
});

if(undefined != jsonconfigdata && null != jsonconfigdata && jsonconfigdata.HIDE_PREV_REVOKES)
{
jQuery(".cert-item-warnings > .text-danger.ng-binding.ng-scope > .fa.fa-exclamation-triangle").css({color:'white'});
jQuery(".cert-item-warnings > .text-danger.ng-binding.ng-scope").css({color:'white'});
}




//removeAttributeFromDisplayName();


}

var bearerToken='';

function refreshPrevalenceScoreCacheforallCertUsers() {
if(undefined != jsonconfigdata.CERT_TARGET_USERS_LIST)
{

/*
Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getPrevScoresBearerToken'),
method: 'GET',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
success:function(transport){

if(undefined != transport.responseText && transport.responseText.indexOf('Bearer') != -1)
{
console.log("got Bearer Token");
bearerToken=Ext.JSON.decode(transport.responseText);
jsonconfigdata.CERT_TARGET_USERS_LIST.forEach(function(eachIdentName) {
refreshPrevalenceScoresCachePerUser(eachIdentName);
});
}

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("Failed to PrevScoresBearerToken: "+identName);
}
});
*/
bearerToken=jsonconfigdata.PREVALENCE_DEV_EXCHANGE_TOKEN;
jsonconfigdata.CERT_TARGET_USERS_LIST.forEach(function(eachIdentName) {
refreshPrevalenceScoresCachePerUser(eachIdentName);
});

}
}

function refreshPrevalenceScoresCachePerUser(identName) {

payload = {
identName: identName,
certId: jsonconfigdata.CERT_ID,
bearerToken: bearerToken
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/refreshPrevalenceScoresCachePerUser'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
console.log("refreshPrevalenceScoresCachePerUser for : "+identName);
//console.log("refreshPrevalenceScoresCachePerUser for : "+identName+" result: "+transport.responseText);

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("Failed to Refresh Prevalence Scores Cache: "+identName);
}
});


}

function updateAccessDescFeedback(rating,certItemId,feedbacktext,accessId) {

payload = {
rating: rating,
certItemId: certItemId,
feedbacktext: feedbacktext,
certId: jsonconfigdata.CERT_ID,
accessId: accessId
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/updateAccessDescFeedback'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){

if (transport.responseText.indexOf("Success") ==-1)
console.log("Thanks for the Feedback, but error while updating");
else
console.log("Thank you for the feedback");


},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("Thanks for the Feedback, but error while updating");
}
});


}

function getExistingAccessDescFeedbackNoResponse(feedbacksection,certItemId,accessId) {

payload = {
certItemId: certItemId
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getExistingAccessDescFeedback'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
console.log("transport.responseText "+ transport.responseText);
if(undefined != transport.responseText && transport.responseText.length)
{
var existingFeedbackText=Ext.JSON.decode(transport.responseText);
//console.log("existingFeedbackText: "+ existingFeedbackText);
if(undefined != existingFeedbackText && existingFeedbackText.length > 0)
{
//feedbacksection.find("input[name='swecustDescFeedbackAnswer'] [value=existingFeedbackText]").attr('checked', true);
feedbacksection.find(".swecustDescFeedbackRadiocontainer").each(function() {
// console.log("$(this).text() "+ $(this).text());
// console.log("existingFeedbackText "+ existingFeedbackText);
// console.log("existingFeedbackText.length "+ existingFeedbackText.length);
if(undefined != $(this).text() &&  $(this).text().indexOf(existingFeedbackText) != -1)
{
$(this).find(".swecustDescFeedbackRadiocheckmark").removeClass().addClass("swecustDescFeedbackRadiocheckmark-checked");
}
});
}
}
var noselectedbutton=feedbacksection.find(".swecustDescFeedbackNoSelected");
noselectedbutton.attr("id","item_"+certItemId);
noselectedbutton.attr("data-toggle","dropdown");
noselectedbutton.attr("aria-haspopup","true");
noselectedbutton.attr("aria-expanded","true");




},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("Thanks for the Feedback, but error while updating");
}
});


}

function clearExistingAccessDescFeedback(certItemId) {

payload = {
certItemId: certItemId
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/clearExistingAccessDescFeedback'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){

if (transport.responseText.indexOf("Success") ==-1)
console.log("Thanks for the Feedback, but error while updating");
else
console.log("Thank you for the feedback");

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("error while clearing");
}
});


}

function insertAccessDescFeedback(rating,certItemId,accessId,feedbacktext) {

payload = {
rating: rating,
certItemId: certItemId,
accessId: accessId,
feedbacktext: feedbacktext,
certId: jsonconfigdata.CERT_ID
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/insertAccessDescFeedback'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){

if (transport.responseText.indexOf("Success") ==-1)
console.log("Thanks for the Feedback, but error while updating");
else
console.log("Thank you for the feedback");


},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("Thanks for the Feedback, but error while updating");
}
});


}

function registerOnClickEvents(thisObj) {

//expand collapse click event
thisObj.find(".expcol-accessdesccls").each(function() {
$(this).unbind();
$(this).click(function(ev) {
// ev.stopPropagation();
var expbutton=$(this).find(".sweexpcollsymbol");
var colbutton=$(this).find(".sweexpcollsymbol2");
//console.log(expbutton.length);
if(expbutton.length)
{
expbutton.removeClass().addClass("fa fa-angle-up sweexpcollsymbol2");
}
else if(colbutton.length)
colbutton.removeClass().addClass("fa fa-angle-down sweexpcollsymbol");

//$(this).parent().find(".access-details").toggle("slow");
$(this).parent().find(".access-details").toggle();

});
});

registerRoleAccessDetailsClickEventExpand(thisObj);
swecustDescFeedbackYesClickEvent(thisObj);
swecustDescFeedbackNoClickEvent(thisObj);
swecustDescFeedbackNoSelectedClickEvent(thisObj);
swecustDescFeedbackClearRatingClickEvent(thisObj);
swecustDescFeedbackNoReasonClickEvent(thisObj);



}

function swecustDescFeedbackNoReasonClickEvent(thisObj) {

thisObj.find(".swecustDescFeedbackRadiocontainer").each(function() {
$(this).unbind();
$(this).click(function(ev) {
var accessInfosection=thisObj;
//console.log("accessInfosection.length: "+accessInfosection.length);
if(accessInfosection.length)
{
var certItemId = accessInfosection.find('span:first').attr('class');
var accessId = accessInfosection.find('span:nth-child(2)').attr('class');
var feedbackanswer=$(this).text();
console.log("feedbackanswer: "+feedbackanswer);
updateAccessDescFeedback('0',certItemId,feedbackanswer,accessId);

}
//ev.stopPropagation();
});


});
}

function swecustDescFeedbackClearRatingClickEvent(thisObj) {

thisObj.find(".swedescfeedbackafterclearlink").each(function() {
$(this).unbind();
$(this).click(function(ev) {
var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
var accessInfosection=thisObj;
if(feedbacksection.length && accessInfosection.length)
{
var certItemId = accessInfosection.find('span:first').attr('class');
var accessId = accessInfosection.find('span:nth-child(2)').attr('class');

var yesbuttonselected=feedbacksection.find(".swecustDescFeedbackYesSelected");
var nobuttonselected=feedbacksection.find(".swecustDescFeedbackNoSelected");
var clearbuttonselected=feedbacksection.find(".swedescfeedbackafterclearlink");

if(yesbuttonselected.length)
{
yesbuttonselected.removeClass().addClass("swecustDescFeedbackYes");
swecustDescFeedbackYesClickEvent(thisObj);
}
if(nobuttonselected.length)
{
nobuttonselected.removeClass().addClass("swecustDescFeedbackNo");
swecustDescFeedbackNoClickEvent(thisObj);
}
if(clearbuttonselected.length)
clearbuttonselected.removeClass().addClass("swedescfeedbackafterclearlinknot-active");

clearExistingAccessDescFeedback(certItemId);
swedescClearClickedApplytoAllAccess(accessId)
}
//ev.stopPropagation();
});


});
}

function swecustDescFeedbackYesClickEvent(thisObj) {

thisObj.find(".swecustDescFeedbackYes").each(function() {
$(this).unbind();
$(this).click(function(ev) {
var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
var accessInfosection=thisObj;
if(feedbacksection.length && accessInfosection.length)
{
var certItemId = accessInfosection.find('span:first').attr('class');
var accessId = accessInfosection.find('span:nth-child(2)').attr('class');
var yesbutton=feedbacksection.find(".swecustDescFeedbackYes");
var nobuttonselected=feedbacksection.find(".swecustDescFeedbackNoSelected");
var clearbutton=feedbacksection.find(".swedescfeedbackafterclearlinknot-active");

if(yesbutton.length)
{
yesbutton.removeClass().addClass("swecustDescFeedbackYesSelected");
insertAccessDescFeedback('1',certItemId,accessId,"");
swedescYesNoClickedApplytoAllAccess('1',accessId);
}
if(nobuttonselected.length)
{
nobuttonselected.removeClass().addClass("swecustDescFeedbackNo");
swecustDescFeedbackNoClickEvent(thisObj);
}
if(clearbutton.length)
{
clearbutton.removeClass().addClass("swedescfeedbackafterclearlink");
swecustDescFeedbackClearRatingClickEvent(thisObj);
}

}
//ev.stopPropagation();
});


});
}

function swecustDescFeedbackNoSelectedClickEvent(thisObj) {
thisObj.find(".swecustDescFeedbackNoSelected").each(function() {
$(this).unbind();
$(this).click(function(ev) {
var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
var accessInfosection=thisObj;
if(feedbacksection.length && accessInfosection.length)
{
var certItemId = accessInfosection.find('span:first').attr('class');
var accessId = accessInfosection.find('span:nth-child(2)').attr('class');
//feedbacksection.find("input[name='swecustDescFeedbackAnswer']").attr("checked", false);
feedbacksection.find(".swecustDescFeedbackRadiocontainer").each(function() {
$(this).find(".swecustDescFeedbackRadiocheckmark-checked").removeClass().addClass("swecustDescFeedbackRadiocheckmark");
});
getExistingAccessDescFeedbackNoResponse(feedbacksection,certItemId,accessId);
}
});
});
}

function swecustDescFeedbackNoClickEvent(thisObj) {
thisObj.find(".swecustDescFeedbackNo").each(function() {
$(this).unbind();
$(this).click(function(ev) {
var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
var accessInfosection=thisObj;
if(feedbacksection.length && accessInfosection.length)
{
var certItemId = accessInfosection.find('span:first').attr('class');
var accessId = accessInfosection.find('span:nth-child(2)').attr('class');
var yesbuttonselected=feedbacksection.find(".swecustDescFeedbackYesSelected");
var nobutton=feedbacksection.find(".swecustDescFeedbackNo");
var clearbutton=feedbacksection.find(".swedescfeedbackafterclearlinknot-active");

if(yesbuttonselected.length)
{
yesbuttonselected.removeClass().addClass("swecustDescFeedbackYes");
swecustDescFeedbackYesClickEvent(thisObj);
}
if(nobutton.length)
{
nobutton.removeClass().addClass("swecustDescFeedbackNoSelected");
insertAccessDescFeedback('0',certItemId,accessId,"");
swedescYesNoClickedApplytoAllAccess('0',accessId);
nobutton.attr("id","item_"+certItemId);
nobutton.attr("data-toggle","dropdown");
nobutton.attr("aria-haspopup","true");
nobutton.attr("aria-expanded","true");
swecustDescFeedbackNoReasonClickEvent(thisObj);
swecustDescFeedbackNoSelectedClickEvent(thisObj);
feedbacksection.find(".swecustDescFeedbackRadiocontainer").each(function() {
$(this).find(".swecustDescFeedbackRadiocheckmark-checked").removeClass().addClass("swecustDescFeedbackRadiocheckmark");
});
//feedbacksection.find("input[name='swecustDescFeedbackAnswer']").attr("checked", false);
}

if(clearbutton.length)
{
clearbutton.removeClass().addClass("swedescfeedbackafterclearlink");
swecustDescFeedbackClearRatingClickEvent(thisObj);
}
/*ev.stopPropagation();
if(jQuery("#item_"+certItemId).length)
jQuery("#item_"+certItemId).dropdown('toggle');
*/
}

});


});
}

function swedescYesNoClickedApplytoAllAccess(rating,accessId) {
jQuery(".Id_"+accessId).each(function() {

var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
if(feedbacksection.length)
{
var accessInfosection=$(this).closest('.swecertscompleteaccessinfo');
if(rating == '0')
{
var yesbuttonselected=feedbacksection.find(".swecustDescFeedbackYesSelected");
var nobutton=feedbacksection.find(".swecustDescFeedbackNo");
if(yesbuttonselected.length)
yesbuttonselected.removeClass().addClass("swecustDescFeedbackYes");

if(nobutton.length)
{
var accessInfosection=$(this).closest('.swecertscompleteaccessinfo');
var certItemId = accessInfosection.find('span:first').attr('class');
nobutton.removeClass().addClass("swecustDescFeedbackNoSelected");
nobutton.attr("id","item_"+certItemId);
nobutton.attr("data-toggle","dropdown");
nobutton.attr("aria-haspopup","true");
nobutton.attr("aria-expanded","true");
swecustDescFeedbackNoSelectedClickEvent(accessInfosection);
swecustDescFeedbackNoReasonClickEvent(accessInfosection);
}
}
else
{
var yesbutton=feedbacksection.find(".swecustDescFeedbackYes");
var nobuttonselected=feedbacksection.find(".swecustDescFeedbackNoSelected");
if(yesbutton.length)
yesbutton.removeClass().addClass("swecustDescFeedbackYesSelected");
if(nobuttonselected.length)
nobuttonselected.removeClass().addClass("swecustDescFeedbackNo");
}
var clearbutton=feedbacksection.find(".swedescfeedbackafterclearlinknot-active");
if(clearbutton.length)
{
clearbutton.removeClass().addClass("swedescfeedbackafterclearlink");
swecustDescFeedbackClearRatingClickEvent(accessInfosection);
}
}

});

}

function swedescClearClickedApplytoAllAccess(accessId) {
jQuery(".Id_"+accessId).each(function() {

var feedbacksection=$(this).closest('.custDescFeedbackYesNoSection');
if(feedbacksection.length)
{
var yesbuttonselected=feedbacksection.find(".swecustDescFeedbackYesSelected");
var nobuttonselected=feedbacksection.find(".swecustDescFeedbackNoSelected");
var accessInfosection=$(this).closest('.swecertscompleteaccessinfo');
if(yesbuttonselected.length)
{
yesbuttonselected.removeClass().addClass("swecustDescFeedbackYes");
swecustDescFeedbackYesClickEvent(accessInfosection);
}
if(nobuttonselected.length)
{
nobuttonselected.removeClass().addClass("swecustDescFeedbackNo");
swecustDescFeedbackNoClickEvent(accessInfosection);
}
var clearselected=feedbacksection.find(".swedescfeedbackafterclearlink");
if(clearselected.length)
clearselected.removeClass().addClass("swedescfeedbackafterclearlinknot-active");

}

});

}

function findandReplaceAttribute(thisObj) {

var eachstr=thisObj.text();
var attrIndex=-1;
if(undefined != eachstr)
attrIndex=eachstr.lastIndexOf(" on ");

if(attrIndex != -1)
{
var actualdispName = eachstr.substring(0, attrIndex);
thisObj.text(actualdispName);
}
}

function removeAttributeFromDisplayName() {
jQuery("sp-certification-display-name-column").each(function() {

$(this).find('span').each(function() {
if($(this).find('a').length)
{
$(this).find('a').each(function() {
findandReplaceAttribute($(this));
});
}
else
{
findandReplaceAttribute($(this));
}
});
/*$(this).find('a').each(function() {
findandReplaceAttribute($(this));
});
*/
});
}

function removeAttributeFromDisplayNameDOMEvent(thisObj) {
thisObj.find("sp-certification-display-name-column").each(function() {

$(this).find('span').each(function() {

if($(this).find('a').length)
{
$(this).find('a').each(function() {
findandReplaceAttribute($(this));
});
}
else
{
findandReplaceAttribute($(this));
}

});
/* $(this).find('a').each(function() {
findandReplaceAttribute($(this));
});
*/
});
}

function registerRoleAccessDetailsClickEventExpand(thisObj) {

thisObj.parent().find(".eachRoleAccessDetails").each(function() {
//$(this).attr('onClick', "certsCustomGetRoleDetails('"+roleId+"');");
$(this).click(function(ev) {
var roleId = $(this).find('a').attr('class');
//alert (roleId);
certsCustomGetRoleDetails(roleId);
ev.stopPropagation();
});


});
}

////feedback////
function loadFeedBackOptions() {

if(!jQuery("#feedbackSurveyMain").length && !feedbackSubmited)
addFeedbackPluginatMessageLayoverSingoff();

jQuery("body").on('DOMSubtreeModified', "#certSignOffOverlayBtn",function() {
//alert("certSignOffBtnchagned");
//console.log("feedback dom event: "+ jQuery("#feedbackSurveyMain").length);
if(!jQuery("#feedbackSurveyMain").length && !feedbackSubmited)
addFeedbackPluginatMessageLayoverSingoff();

});


}

function loadEntOwnerCertRevokeOptions() {

if(jQuery("#certSignOffOverlayBtn").length)
{

var completCertBtn= '<button id="certSignOffOverlayBtn" onclick="customCompleteEntOwnerCertClicked()" class="btn btn-success btn-lg m-t ng-binding" ng-keydown="ctrl.onQueryKeyDown($event)">'+
'Complete Certification'+
'</button>';
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);

jQuery("body").on('DOMSubtreeModified', "#certSignOffOverlayBtn",function() {
//alert("certSignOffBtnchagned");
//console.log("feedback dom event: "+ jQuery("#feedbackSurveyMain").length);
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);

});

}

}

var revokedDecisions=[];
function customCompleteEntOwnerCertClicked() {
revokedDecisions=[];
payload = {
certId: jsonconfigdata.CERT_ID
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/entOwnerCertRevokesorSignoff');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);
//console.log("result after saving decisions: "+ resultData);

if(undefined == resultData)
{
//SailPoint.EXCEPTION_ALERT("Failed to Complete Certification");
window.location.reload();
}
else
{
if(undefined != resultData && undefined != resultData.userRevokeAccessDetails)
getEntOwnerRevocationConfirmationModal(resultData.userRevokeAccessDetails);
else
{
//redirect to home page
var redirectURL="";

if(null != window.location.protocol && window.location.hostname)
redirectURL = window.location.protocol+"//"+window.location.hostname;
if(null != window.location.port)
redirectURL=redirectURL+":"+window.location.port;
if(null != window.location.pathname)
{
var parts = window.location.pathname.split('/');
redirectURL=redirectURL+"/"+parts[1];
}

//console.log("redirectURL: "+ redirectURL);

if(null != redirectURL && "" != redirectURL)
window.location.href = redirectURL;
}
//console.log("resultData.isCertCompleted: "+ resultData.isCertCompleted);
//	 if(resultData.isCertCompleted)
// window.location.reload();
}

},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to Complete Certification");
console.log("filed to Complete Certification: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function getEntOwnerRevocationConfirmationModal(userRevokeAccessDetails) {
//console.log("in getRoleDetailsModal length: "+jQuery("#roleDetailsModalDialog").length);


var userRevokeAccessDetailshtml='';

if(userRevokeAccessDetails.length < 1)
{
userRevokeAccessDetailshtml= '<div class="panel-heading">'+
'<h4 class="spContentTitle">No Details Found</h4>'+
'</div>';
}
else
{



userRevokeAccessDetailshtml='<div class="panel-heading">'+
'<h4 class="spContentTitle">Below are the (<b>'+userRevokeAccessDetails.length+'</b>) access(es) that you have selected to revoke and granted via role</h4>'+
'</div>'+

'<div role="presentation" class="panel ng-isolate-scope">'+
'<div role="presentation" class="table-responsive-wide-title ng-scope roledetailstablediv">'+
'<table class="table table-striped m-b-none text-md roledetailstable">'+
'<thead>'+
'<tr>'+
'<th style="width:15%"  class="ng-binding">User</th>'+
'<th style="width:20%" class="ng-binding">Application</th>'+
'<th style="width:20%"  class="ng-binding">Access</th>'+
'<th style="width:5%"  class="ng-binding">Granted By Role</th>'+
'<th style="width:30%" class="ng-binding">Description</th>'+
'<th style="width:10%"  class="ng-binding">Account</th>'+
'<th style="width:10%"  class="ng-binding">Action</th>'+
'</tr>'+
'</thead>'+
'<tbody>';

jQuery.map( userRevokeAccessDetails,function(eachAccess) {
revokedDecisions.push(eachAccess.itemId);
var rolemetaData=
'<div class="roleDetailscompleteaccessinfo">'+
'<span class="accessdispName">'+
'<span class="accessinfoicon"> <span class="fa fa-lock"></span></span>'+
'<span class="eachAccessDispName"><b>'+eachAccess.displayName+'</b></span>';

if(undefined != eachAccess.isHighRisk && eachAccess.isHighRisk)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isHighRisk" class="roleDetailsaccess-priv" title="This is a High Risk Entitlement">HIGH RISK</span>';

if(undefined != eachAccess.cbtinfo && eachAccess.cbtinfo.length > 1)
rolemetaData=rolemetaData+'<span class="roleDetailsaccess-cbt" title="'+eachAccess.cbtinfo+'">CBT</span>';

if(undefined != eachAccess.isNPI && eachAccess.isNPI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isNPI" class="roleDetailsaccess-npi" title="Non-Public Information" >NPI</span>';

if(undefined != eachAccess.isPCI && eachAccess.isPCI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isPCI" class="roleDetailsaccess-pci" title="Payment Card Information">PCI</span>';

rolemetaData=rolemetaData+'</span>'+
'</div>';

var roleWizardOptionHtml='<td>To Remove Entitlement From Role, please Contact:  <span class=\"fa fa-envelope popupemailclickicon\"></span><a target="_blank" rel="noopener noreferrer" href="mailto:"'+jsonconfigdata.ROLES_TEAM_EMAIL+'">Roles Team</a></td>';

//console.log("eachAccess.roleWizardOption: "+eachAccess.roleWizardOption);
if(undefined != eachAccess.grantedByRoleName && eachAccess.roleWizardOption)
roleWizardOptionHtml='<td title="Decision" class="ng-binding">'+'<a class=\"eachRoleAccessDetails\" title="Remove Entitlement From Role" onclick="entOwnerCertinvokeRoleWizardWF(\''+eachAccess.grantedByRoleId+'\',\''+eachAccess.itemId+'\')">Remove Entitlement From Role</a>';



userRevokeAccessDetailshtml=userRevokeAccessDetailshtml+  '<tr class="ng-scope">'+
'<td title="User FullName" class="ng-binding">'+eachAccess.userFullName+

'</td>'+
'<td title="Access Application Name" class="ng-binding">'+eachAccess.appName+

'</td>'+

'<td title="Access Display Name" class="ng-binding">'+rolemetaData+

'</td>'+
'<td title="Granted By Role" class="ng-binding">'+eachAccess.grantedByRoleName+

'</td>'+
'<td title="Access Description" class="ng-binding">'+eachAccess.description+

'</td>'+

'<td title="Account Name" class="ng-binding">'+eachAccess.accountName+

'</td>'+

roleWizardOptionHtml+

'</td>'+

'</tr>';
});

userRevokeAccessDetailshtml=userRevokeAccessDetailshtml+'</tbody>'+
'</table>'+
'</div>'+
'</div>';
}

var userRevokeAccessDetailsModalDialog=getEntOwnerCertsCustomPopup(userRevokeAccessDetailshtml);

if (jQuery( "#csvDownloadLink" ).length && !jQuery("#systemIdCertDecisionOptionsModal").length)
{
jQuery("#csvDownloadLink").each(function() {
$(this).parent().append(userRevokeAccessDetailsModalDialog);
jQuery("#systemIdCertDecisionOptionsModal").modal({keyboard: true, show: true, backdrop: "static"});

});

}
else if(jQuery("#roleDetailsModalDialog").length)
{
if(jQuery("#roleDetailsSection").length)
jQuery("#roleDetailsSection").children().remove();
jQuery("#roleDetailsSection").append(userRevokeAccessDetailshtml);
jQuery("#systemIdCertDecisionOptionsModal").modal({keyboard: true, show: true, backdrop: "static"});
}

}

function getEntOwnerCertsCustomPopup(userRevokeAccessDetailshtml) {

var approveBtn='<span class="systemIdCertsModalApproveSection"><span class="systemIdCertsModalApproveHelpLbl">Change Decisions</span><span class="systemIdCertsModalApproveBtn"><button id="ApproveBtnModal" data-dismiss="modal" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Approved" onclick= "custEntOwnerCertApproveDecisionsClicked()" ng-class="{\'cert-action-current-decision\' : actionColumnCtrl.isCurrentDecision(decision),\'returned-item\' : actionColumnCtrl.isReturnedItem()}" ng-disabled="!actionColumnCtrl.isEnabled()" aria-pressed="false" aria-label="" ">'+
'<span class="fa fa-lg" aria-hidden="true"></span><span class="fa fa-thumbs-up systemIdCertsModalApproveIcon" aria-hidden="true"></span><span class="customCertApproveModalLbl">Approve</span>'+
'</button></span></span>';

var revokeBtn='<span class="systemIdCertsModalRevokeSection"><span class="systemIdCertsModalRevokeHelpLbl">Yes, I still want to revoke the access</span><span class="systemIdCertsModalRevokeBtn"><button id="RevokeBtnModal" data-dismiss="modal" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Remediated" onclick= "custCertSignoffClicked()"  ng-class="{\'cert-action-current-decision\' : actionColumnCtrl.isCurrentDecision(decision),\'returned-item\' : actionColumnCtrl.isReturnedItem()}" ng-disabled="!actionColumnCtrl.isEnabled()" aria-pressed="false" aria-label="">'+
'<span class="fa fa-lg" aria-hidden="true"></span> <span class="fa fa-thumbs-down systemIdCertsModalRevokeIcon" aria-hidden="true"></span><span class="customCertRevokeModalLbl">Revoke</span>'+
'</button></span></span>';

var CertsModalDialog='<div id="systemIdCertDecisionOptionsModal" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+

'<div class="modal-body custroledetailsmodalbody">'+
'<div class="customCertSysIDRevokeHeader"><span class="fa fa-exclamation-triangle customCertRevokeOptionIcon" aria-hidden="true"></span> Below Access were granted via role and revoking below may result is getting access back. Click on the below links "Remove Entitlement from Role" which will redirect to Role Wizard tool where you can submit the request for removing the entitlement from the role. or Email Roles Team.  Finally you can only approve these.</div>'+

'<div id="roledetailsinstrs">'+
'<div id="roleDetailsSection" class="offliceCerts panel m-b panel-default ng-scope">'+userRevokeAccessDetailshtml+

'</div>'+
'</div>'+
'</div>'+

'<div id="systemIdCertsModalFooter" class="modal-footer bg-light lt ng-scope">'+ approveBtn //+ revokeBtn

'</div>'+
'</div>'+
'</div>'+
'</div>';

return CertsModalDialog;
}

function entOwnerCertinvokeRoleWizardWF(grantedByRoleName, certItemId) {

payload = {
grantedByRoleName: grantedByRoleName,
certItemId: certItemId
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/entOwnerCertinvokeRoleWizardWF');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);
//console.log("result after saving decisions: "+ resultData);

if(undefined == resultData)
{
SailPoint.EXCEPTION_ALERT("Failed to Revert Certification Decisions");
}
else if(undefined != resultData && resultData.length > 0)
window.open(resultData, "_blank", "x=y");
//window.open(resultData, 'RoleWizard', 'location=1,status=1,scrollbars=1, resizable=1, directories=1, toolbar=1, titlebar=1');


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to invoke RoleWizard");
console.log("Failed to invoke entOwnerCertinvokeRoleWizardWF: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function custEntOwnerCertApproveDecisionsClicked() {

payload = {
certId: jsonconfigdata.CERT_ID,
revokedDecisions: revokedDecisions
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/entOwnerApproveCertDecisions');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);
//console.log("result after saving decisions: "+ resultData);

if(undefined == resultData)
{
SailPoint.EXCEPTION_ALERT("Failed to Revert Certification Decisions");
}
else
window.location.reload();


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to Revert Certification Decisions");
console.log("Failed to Revert Certification Decisions: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function loadsystemIDCertRevokeOptions() {

if(jQuery("#certSignOffOverlayBtn").length)
{

var completCertBtn= '<button id="certSignOffOverlayBtn" onclick="customCompleteCertificationClicked()" class="btn btn-success btn-lg m-t ng-binding" ng-keydown="ctrl.onQueryKeyDown($event)">'+
'Complete Certification'+
'</button>';
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);

jQuery("body").on('DOMSubtreeModified', "#certSignOffOverlayBtn",function() {
//alert("certSignOffBtnchagned");
//console.log("feedback dom event: "+ jQuery("#feedbackSurveyMain").length);
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);

});

}

}

function customCompleteCertificationClicked() {

var completCertBtn= '<button id="certSignOffOverlayBtn" disabled ="true" class="btn btn-success btn-lg m-t ng-binding" ng-keydown="ctrl.onQueryKeyDown($event)">'+
'<span class="customsystemIdCertCompleteButton fa fa-spinner"></span>'+'Complete Certification'+
'</button>';
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);

var isSystemIDCert = false;
if(jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS == true)
isSystemIDCert = true;

revokedDecisions=[];
payload = {
certId: jsonconfigdata.CERT_ID,
isSystemIDCert: isSystemIDCert
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/checkCertRevokesorSignoff');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);
//console.log("result after saving decisions: "+ resultData);


if(undefined != resultData && resultData.length)
{

if(jQuery("#systemIdCertDecisionOptionsModal").length)
jQuery(systemIdCertDecisionOptionsModal).remove();

createRevocationConfirmationModalStructure(resultData);
revocationConfirmationModalApplyPagination(resultData);
}
else
{
//redirect to home page
var redirectURL="";

if(null != window.location.protocol && window.location.hostname)
redirectURL = window.location.protocol+"//"+window.location.hostname;
if(null != window.location.port)
redirectURL=redirectURL+":"+window.location.port;
if(null != window.location.pathname)
{
var parts = window.location.pathname.split('/');
redirectURL=redirectURL+"/"+parts[1];
}

//console.log("redirectURL: "+ redirectURL);

if(null != redirectURL && "" != redirectURL)
window.location.href = redirectURL;
}
//console.log("resultData.isCertCompleted: "+ resultData.isCertCompleted);
//	 if(resultData.isCertCompleted)
// window.location.reload();


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to Complete Certification");
console.log("filed to Complete Certification: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function revocationConfirmationModalApplyPagination(userRevokeAccessDetails) {
//console.log("in revocationConfirmationModalApplyPagination : "+accessDescFeedbackDetailsdata);
var totalRecords = 0;
var records = [];
var displayRecords = [];
var recPerPage = 10;
var page = 1;
var totalPages = 0;

records = userRevokeAccessDetails;
//console.log(records);
totalRecords = records.length;
totalPages = Math.ceil(totalRecords / recPerPage);

jQuery("#revocationConfirmationModalPagination").twbsPagination({
totalPages: totalPages,
visiblePages: 6,
onPageClick:function (event, page) {
displayRecordsIndex = Math.max(page - 1, 0) * recPerPage;
endRec = (displayRecordsIndex) + recPerPage;

displayRecords = records.slice(displayRecordsIndex, endRec);
getRevocationConfirmationModal(displayRecords);
}
});

}

function createRevocationConfirmationModalStructure(userRevokeAccessDetails) {
//console.log("in getRoleDetailsModal length: "+jQuery("#roleDetailsModalDialog").length);


var userRevokeAccessDetailshtml='';

if(userRevokeAccessDetails.length < 1)
{
userRevokeAccessDetailshtml= '<div class="panel-heading">'+
'<h4 class="spContentTitle">No Details Found</h4>'+
'</div>';
}
else
{



userRevokeAccessDetailshtml='<div class="panel-heading">'+
'<h4 class="spContentTitle">Below are the (<b>'+userRevokeAccessDetails.length+'</b>) access(es) that you have selected to revoke</h4>'+
'</div>'+

'<div role="presentation" class="panel ng-isolate-scope">'+
'<div role="presentation" class="table-responsive-wide-title ng-scope roledetailstablediv">'+
'<div class="revocationConfirmationTabSection">'+
'<table class="table table-striped m-b-none text-md roledetailstable" id="revocationConfirmationTable">'+
'<thead>'+
'<tr>'+
'<th style="width:15%"  class="ng-binding">User</th>'+
'<th style="width:5%"  class="ng-binding">Type</th>'+
'<th style="width:15%" class="ng-binding">Application</th>'+
'<th style="width:15%"  class="ng-binding">Access</th>'+
'<th style="width:30%" class="ng-binding">Description</th>'+
'<th style="width:5%"  class="ng-binding">Account</th>'+
'<th style="width:10%"  class="ng-binding"></th>'+
'</tr>'+
'</thead>'+
'<tbody id="revocationConfirmationModalDetailsBody">';



userRevokeAccessDetailshtml=userRevokeAccessDetailshtml+'</tbody>'+
'</table>'+
'</div>'+
'<div>'+
'<ul id="revocationConfirmationModalPagination" class="pagination-sm"></ul>'+
'</div>'+
'</div>'+
'</div>';
}

var userRevokeAccessDetailsModalDialog=getSystemIdCertsCustomPopup(userRevokeAccessDetailshtml);

if (jQuery( "#csvDownloadLink" ).length && !jQuery("#systemIdCertDecisionOptionsModal").length)
{
jQuery("#csvDownloadLink").each(function() {
$(this).parent().append(userRevokeAccessDetailsModalDialog);
//jQuery("#systemIdCertDecisionOptionsModal").modal({keyboard: true, show: true, backdrop: "static"});
if(undefined != jQuery("#revocationConfirmationTable"))
jQuery("#revocationConfirmationTable").tableHeadFixer({'z-index' : 50});

});

}


}

var approveClickArray=[];
var revokeClickArray=[];
function getRevocationConfirmationModal(userRevokeAccessDetails) {
//console.log("in getRevocationConfirmationModal: "+userRevokeAccessDetails);


var userRevokeAccessDetailshtml='';

if(userRevokeAccessDetails.length < 1)
{
userRevokeAccessDetailshtml= '<div class="panel-heading">'+
'<h4 class="spContentTitle">No Details Found</h4>'+
'</div>';
}
else
{


jQuery.map( userRevokeAccessDetails,function(eachAccess) {
eachItemId=eachAccess.itemId;


var approvedSelectedClass='';
var revokedSelectedClass=' cert-action-current-decision';
if(undefined != approveClickArray && approveClickArray.indexOf(eachItemId) != -1)
{
approvedSelectedClass=' cert-action-current-decision';
revokedSelectedClass=''
}
else
{
if(undefined != revokeClickArray && revokeClickArray.indexOf(eachItemId) == -1)
revokeClickArray.push(eachItemId);
//if(undefined != revokeClickArray && revokeClickArray.indexOf(eachItemId) != -1)
revokedSelectedClass=' cert-action-current-decision';
}

var rolemetaData=
'<div class="roleDetailscompleteaccessinfo">'+
'<span class="accessdispName">'+
'<span class="accessinfoicon"> <span class="fa fa-lock"></span></span>'+
'<span class="eachAccessDispName"><b>'+eachAccess.displayName+'</b></span>';

if(undefined != eachAccess.isHighRisk && eachAccess.isHighRisk)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isHighRisk" class="roleDetailsaccess-priv" title="This is a High Risk Entitlement">HIGH RISK</span>';

if(undefined != eachAccess.cbtinfo && eachAccess.cbtinfo.length > 1)
rolemetaData=rolemetaData+'<span class="roleDetailsaccess-cbt" title="'+eachAccess.cbtinfo+'">CBT</span>';

if(undefined != eachAccess.isNPI && eachAccess.isNPI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isNPI" class="roleDetailsaccess-npi" title="Non-Public Information" >NPI</span>';

if(undefined != eachAccess.isPCI && eachAccess.isPCI)
rolemetaData=rolemetaData+'<span ng-if="eachAccess.isPCI" class="roleDetailsaccess-pci" title="Payment Card Information">PCI</span>';

rolemetaData=rolemetaData+'</span>'+
'</div>';

userRevokeAccessDetailshtml=userRevokeAccessDetailshtml+  '<tr class="ng-scope">'+
'<td title="User FullName" class="ng-binding">'+eachAccess.userFullName+

'</td>'+
'<td title="Access Type" class="ng-binding">'+eachAccess.type+

'</td>'+
'<td title="Access Application Name" class="ng-binding">'+eachAccess.appName+

'</td>'+

'<td title="Access Display Name" class="ng-binding">'+rolemetaData+

'</td>'+
'<td title="Access Description" class="ng-binding">'+eachAccess.description+

'</td>'+

'<td title="Account Name" class="ng-binding">'+eachAccess.accountName+

'</td>'+
'<td title="Decision" class="ng-binding">'+
'<button onclick="decisionsApproveButtonClicked(event,\''+eachItemId+'\')" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Approved'+approvedSelectedClass+'" aria-pressed="false" aria-label="">'+
'<i class="fa fa-lg" aria-hidden="true"></i> <span class="customCertApproveLbl">Approve</span>'+
'</button>'+

'<button onclick="decisionsRevokeButtonClicked(event,\''+eachItemId+'\')"  class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Remediated'+revokedSelectedClass+'"  aria-pressed="false" aria-label="">'+
'<i class="fa fa-lg" aria-hidden="true"></i> <span class="customCertRevokeLbl">Revoke</span>'+
'</button>'+

'</td>'+
'</tr>';
});


}



//console.log("userRevokeAccessDetailshtml length: "+userRevokeAccessDetailshtml);
if(jQuery("#systemIdCertDecisionOptionsModal").length && jQuery("#revocationConfirmationModalDetailsBody").length)
{
jQuery("#revocationConfirmationModalDetailsBody").children().remove();
jQuery("#revocationConfirmationModalDetailsBody").append(userRevokeAccessDetailshtml);
//jQuery("#CustomaccessDescFeedbackDetailsmodalTitleSpan").remove();
//jQuery("#CustomaccessDescFeedbackDetailsmodalTitle").append('<span id="CustomaccessDescFeedbackDetailsmodalTitleSpan">Access Details - '+roleDispName+'</span>');
//jQuery("#systemIdCertDecisionOptionsModal").modal({keyboard: true, show: true, backdrop: "static"});
jQuery("#systemIdCertDecisionOptionsModal").modal({keyboard: true, show: true});

// modal close event
jQuery("#systemIdCertDecisionOptionsModal").on("hidden.bs.modal",function () {
//console.log("systemIdCertDecisionOptionsModal closed");
approveClickArray=[];
revokeClickArray=[];
});
}


var completCertBtn= '<button id="certSignOffOverlayBtn" onclick="customCompleteCertificationClicked()" class="btn btn-success btn-lg m-t ng-binding" ng-keydown="ctrl.onQueryKeyDown($event)">'+
'Complete Certification'+
'</button>';
jQuery("#certSignOffOverlayBtn").replaceWith(completCertBtn);
}

function changeButtonStyle(event) {

var btnNode=null;
// console.log("event.nodeName: "+event.nodeName);
if(event.nodeName == "BUTTON" || event.nodeName == "button")
btnNode= $( event.target );
else
btnNode=$( event.target).closest('button');

//console.log("btnNode.attr('class'): "+ btnNode.attr('class'));

if(undefined != btnNode && btnNode.attr('class').indexOf('cert-action-current-decision') == -1);
btnNode.addClass('cert-action-current-decision');

btnNode.siblings().each(function() {
//console.log("$(this).attr('class'): "+ $(this).attr('class'));
$(this).removeClass('cert-action-current-decision');
});



}

function decisionsApproveButtonClicked(event,itemId)
{
if(undefined == approveClickArray)
approveClickArray=[];

//console.log("itemId: "+itemId);
//console.log("approveClickArray.indexOf(itemId): "+approveClickArray.indexOf(itemId));

if(approveClickArray.indexOf(itemId) == -1)
{
changeButtonStyle(event);
approveClickArray.push(itemId);
if(revokeClickArray.indexOf(itemId) != -1)
revokeClickArray.splice(revokeClickArray.indexOf(itemId),1);

}



}
function decisionsRevokeButtonClicked(event,itemId)
{
if(undefined == revokeClickArray)
revokeClickArray=[];

//console.log("itemId: "+itemId);
//console.log("revokeClickArray.indexOf(itemId): "+revokeClickArray.indexOf(itemId));

if(revokeClickArray.indexOf(itemId) == -1)
{
changeButtonStyle(event);
revokeClickArray.push(itemId);
if(approveClickArray.indexOf(itemId) != -1)
approveClickArray.splice(approveClickArray.indexOf(itemId),1);

}


}
function getSystemIdCertsCustomPopup(userRevokeAccessDetailshtml)
{

var approveBtn='<span class="systemIdCertsModalApproveSection"><span class="systemIdCertsModalApproveBtn"><button id="SystemIdCertCustApproveBtnModal" data-dismiss="modal" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Approved" onclick= "custCertRevertDecisionsClicked()" ng-class="{\'cert-action-current-decision\' : actionColumnCtrl.isCurrentDecision(decision),\'returned-item\' : actionColumnCtrl.isReturnedItem()}" ng-disabled="!actionColumnCtrl.isEnabled()" aria-pressed="false" aria-label="" ">'+
'<span class="fa fa-lg" aria-hidden="true"></span></span><span class="customCertApproveModalLbl">Complete Certification</span>'+
'</button></span></span>';

var revokeBtn='<span class="systemIdCertsModalRevokeSection"><span class="systemIdCertsModalRevokeHelpLbl"></span><span class="systemIdCertsModalRevokeBtn"><button id="RevokeBtnModal" data-dismiss="modal" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Remediated" onclick= "custCertSignoffClicked()"  ng-class="{\'cert-action-current-decision\' : actionColumnCtrl.isCurrentDecision(decision),\'returned-item\' : actionColumnCtrl.isReturnedItem()}" ng-disabled="!actionColumnCtrl.isEnabled()" aria-pressed="false" aria-label="">'+
'<span class="fa fa-lg" aria-hidden="true"></span> <span class="fa fa-thumbs-down systemIdCertsModalRevokeIcon" aria-hidden="true"></span><span class="customCertRevokeModalLbl">Complete Certification</span>'+
'</button></span></span>';

var CertsModalDialog='<div id="systemIdCertDecisionOptionsModal" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+

'<div class="modal-body custroledetailsmodalbody">'+
'<button data-dismiss="modal" type="button" class="close customsystemIdCertDecisionOptionsModalCloseBtn" aria-hidden="true" tabindex="50">×</button>'+
'<div class="customCertSysIDRevokeHeader"><span class="fa fa-exclamation-triangle customCertRevokeOptionIcon" aria-hidden="true"></span> Revoking/Deleting a System Account can have severe downstream impacts to some applications, and in some cases, result in Sev Incidents. You should only revoke/delete a System Account if you are 100% sure that it will have no negative downstream impacts. If you want to change any of the decisions below, click on Approve for that specific access.  If you wish to move forward with the Revoke Decisions, click on Complete Certification.</div>'+


'<div id="roledetailsinstrs">'+
'<div id="roleDetailsSection" class="offliceCerts panel m-b panel-default ng-scope">'+userRevokeAccessDetailshtml+

'</div>'+
'</div>'+
'</div>'+

//'<div id="systemIdCertsModalFooter" class="modal-footer bg-light lt ng-scope">'+ approveBtn + revokeBtn
'<div id="systemIdCertsModalFooter" class="modal-footer bg-light lt ng-scope">'+ approveBtn

'</div>'+
'</div>'+
'</div>'+
'</div>';

return CertsModalDialog;
}

function custCertRevertDecisionsClicked()
{


var completCertBtn= '<button id="SystemIdCertCustApproveBtnModal" disabled = "true" data-dismiss="modal" class="btn btn-white btn-sm icon-btn m-r-xs customcert-action-Approved" ng-class="{\'cert-action-current-decision\' : actionColumnCtrl.isCurrentDecision(decision),\'returned-item\' : actionColumnCtrl.isReturnedItem()}" ng-disabled="!actionColumnCtrl.isEnabled()" aria-pressed="false" aria-label="" ">'+
'<span class="customsystemIdCertCompleteButton fa fa-spinner"></span>'+
'<span class="fa fa-lg" aria-hidden="true"></span></span><span class="customCertApproveModalLbl">Complete Certification</span>'+
'</button>';
jQuery("#SystemIdCertCustApproveBtnModal").replaceWith(completCertBtn);



payload = {
certId: jsonconfigdata.CERT_ID,
revokedDecisions: revokeClickArray,
approvedDecisions: approveClickArray

};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/sysIdCompleteCertSaveDecisions');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
//alert("success: "+transport.responseText);
var resultData = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);
//console.log("result after saving decisions: "+ resultData);

if(undefined == resultData)
{
SailPoint.EXCEPTION_ALERT("Failed to Revert Certification Decisions");
}
else
{
//redirect to home page
var redirectURL="";

if(null != window.location.protocol && window.location.hostname)
redirectURL = window.location.protocol+"//"+window.location.hostname;
if(null != window.location.port)
redirectURL=redirectURL+":"+window.location.port;
if(null != window.location.pathname)
{
var parts = window.location.pathname.split('/');
redirectURL=redirectURL+"/"+parts[1];
}

//console.log("redirectURL: "+ redirectURL);

if(null != redirectURL && "" != redirectURL)
window.location.href = redirectURL;
//window.location.reload();

}
// window.location.reload();


},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to Revert Certification Decisions");
console.log("Failed to Revert Certification Decisions: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}

function custCertSignoffClicked()
{

payload = {
certId: jsonconfigdata.CERT_ID
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/customSignoffCertification');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
// do something
var resultData = Ext.JSON.decode(transport.responseText);

if(undefined == resultData)
{
SailPoint.EXCEPTION_ALERT("Failed to Complete Certification");
}
else
{

//redirect to home page
var redirectURL="";

if(null != window.location.protocol && window.location.hostname)
redirectURL = window.location.protocol+"//"+window.location.hostname;
if(null != window.location.port)
redirectURL=redirectURL+":"+window.location.port;
if(null != window.location.pathname)
{
var parts = window.location.pathname.split('/');
redirectURL=redirectURL+"/"+parts[1];
}

//console.log("redirectURL: "+ redirectURL);

if(null != redirectURL && "" != redirectURL)
window.location.href = redirectURL;
//window.location.reload();
}

},
failure:function(transport){
SailPoint.EXCEPTION_ALERT("Failed to Complete Certification");
console.log("Failed to Complete Certification: "+ transport);
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
}
});

}
/*
function addFeedbackPluginatSingoff()
{
var divfeedbackmain='<div class="swecertsfeedbackdivUniversal" >';
var optionshtml='<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(5)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(4)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog"onclick="feedbackOnlclickActions(3)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(2)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(1)"></span>';
optionshtml=optionshtml+'<span class="Universalfeedbacklabel"> :Feedback</span></div>';

jQuery("#certSignOffBtn").each(function() {
$(this).parent().append(divfeedbackmain+optionshtml);
});

if(jQuery("#certSignOffBtn").length)
{
jQuery("#csvDownloadLink").parent().append(getfeedbackModalHTML());
addFeedbackmodalbuttonEvents();
}
}
*/

function displayFeedback(flag)
{
if(flag)
jQuery(".swecertsfeedbacklayoversection").show();
else
jQuery(".swecertsfeedbacklayoversection").hide();
}

function addFeedbackPluginatMessageLayoverSingoff()
{

var choiceshtml='<label class="feedbackEachRadiocontainer">Yes<input id="surveyAnswerYes" type="radio" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" name="surveyAnswer" onclick="displayFeedback(1)" value="Yes"/>'+
'<span class="feedbackEachRadiocheckmark"></span></label>';
choiceshtml=choiceshtml+'<label class="feedbackEachRadiocontainer">No<input id="surveyAnswerNo" type="radio" name="surveyAnswer" onclick="displayFeedback(0)" value="No"/>'+
'<span class="feedbackEachRadiocheckmark"></span></label>'




var questionhtml='<li class="list-group-item ng-binding">'+
'<span class="feedbackquestions question1 ng-binding ng-scope">'+
'<b class="ng-binding">Would you like to participate in the Survey?</b>'+
'</span>'+
'<div class="feedbackfields">'+ choiceshtml+
'</div>'+
'</li>';

var feedbacksurveyMain=  '<div id="feedbackSurveyMain">'+
'<div class="customsurveySection">'+
'<div class="custom-panel-heading">'+
//'<h4 class="custsurveyTitle">Satisfaction Survey</h4>'+
'<ul class="list-group">'+questionhtml+ '</ul>'+
'</div>'+

// '<ul class="list-group">'+questionhtml+ '</ul>'+

'</div>'+
'</div>';

/* var divfeedbackmain=feedbacksurveyMain+'<div class="swecertsfeedbacklayoversection"><span class="swecertsfeedbackdivLayoverUniversal" >';
var optionshtml='<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(5)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(4)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog"onclick="feedbackOnlclickActions(3)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(2)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" data-toggle="modal" data-backdrop="static"  data-target="#feedbackModalDialog" onclick="feedbackOnlclickActions(1)"></span>';
optionshtml=optionshtml+'<span class="Universalfeedbacklabel"> :Feedback</span></span></div>';
*/
jQuery("#certSignOffOverlayBtn").each(function() {
//$(this).parent().append(divfeedbackmain+optionshtml);
$(this).parent().append(feedbacksurveyMain);
});




//if(starratingcount > 0)
//loadFeedBackSubmitted(starratingcount);
//else
if(jQuery("#certSignOffOverlayBtn").length && !jQuery("#feedbackModalDialog").length)
{
jQuery("#csvDownloadLink").parent().append(getfeedbackModalHTML());
addFeedbackmodalbuttonEvents();
}
}

/*
var feedbackQuestions = [
{question : "Is the Certification UI helpful to Complete Certification?", type : "radio"},
{question : "Is the Certification Description helpful to Complete Certification?", type : "radio"},
{question : "Is the Certification Options helpful to Complete Certification?", type : "radio"}
];
*/

function getfeedbackModalHTML()
{
var feedbackQuestions=jsonconfigdata.feedbackQuestions;
//alert (feedbackQuestions);
if(null != feedbackQuestions && undefined != feedbackQuestions)
{

var questionhtml='';
var index=0;

var optionshtml = '<div class="swecertsfeedbacklayoversection">'+getStartRatingHTML();
optionshtml=optionshtml+'<button class="startRatingReset" onclick="resetStars()" type="button" class="btn btn-white btn-sm m-l-xs" ng-click="resetchartsettings()" title="Reset">Reset</button></div>';




if(undefined != feedbackQuestions)
feedbackQuestions.map(function(eachquestionmap){

var choices=eachquestionmap.choice;
var choiceshtml='';

if(undefined != choices)
choices.forEach(function(eachChoice) {
choiceshtml=choiceshtml+'<label class="feedbackEachRadiocontainer">'+eachChoice+'<input type="radio" name="answer'+index+'" value="'+eachChoice+'"/>'+
'<span class="feedbackEachRadiocheckmark"></span></label>';

});


questionhtml= questionhtml+'<li class="list-group-item ng-binding">'+
'<span class="feedbackquestions question1 ng-binding ng-scope">'+
'<b class="ng-binding">'+eachquestionmap.question+' : </b>'+
'</span>'+
'<div class="feedbackfields">'+ choiceshtml+
'</div>'+
'</li>';

index++;
});




}
questionhtml= questionhtml+'<li class="list-group-item ng-binding">'+
'<span class="feedbackquestions question1 ng-binding ng-scope">'+
'<b class="ng-binding">Your overall rating : </b>'+
'</span>'+
'<div class="feedbackfields">'+ optionshtml+
'</div>'+
'</li>';

var feedbackModalHTML='<div id="feedbackModalDialog" uib-modal-window="modal-window" class="modal fade ng-scope ng-isolate-scope in"  animate="animate"  role="dialog">'+
'<div class="modal-dialog modal-lg" aria-labelledby="modalTitle">'+
'<div class="modal-content">'+
'<div class="modal-header">'+
'<h4 class="modal-title ng-binding" id="modalTitle">Feedback Dialog</h4>'+
'</div>'+
'<div class="modal-body">'+
'<div id="feedbackfieldsInstrs">'+
'<div class="feedbackpanel panel m-b panel-default ng-scope">'+
'<div class="panel-heading">'+
'<h4 class="spContentTitle">Feedback Questions</h4>'+
'</div>'+

'<ul class="list-group">'+questionhtml+ '</ul>'+

'</div>'+
'</div>'+

'<div id="plainFeedbackheader"><div class="spContentTitle">Additional Feedback (open text)</div>'+
'<div class="feedbackplaintext">'+
'<textarea maxlength="400" class="form-control ng-pristine"  id="plainFeedbacktextarea" placeholder="Enter additional feedback here"></textarea>'+
'</div></div>'+

'<div class="feedbackValidation"></div>'+
'</div>'+

'<div id="feedbackFooter" class="modal-footer bg-light lt ng-scope">'+
'<button type="button" id="feedbackCancelBtn" class="btn  btn-cancel"  data-dismiss="modal">Cancel</button>'+
'<button type="button" id="feedbackSubmitBtn" class="btn  btn-info btn-default">Submit</button>'+
'</div>'+
'</div>'+
'</div>'+
'</div>';

return feedbackModalHTML;
}

var feedbackSubmited=false;

function addFeedbackmodalbuttonEvents()
{
jQuery("#feedbackSubmitBtn").on( 'click',function () {

$('#feedbackModalDialog').modal('toggle');

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/postFeedback');

var feedbacktext=jQuery("#plainFeedbacktextarea").val();
var qanda='';
var feedbackQuestions=jsonconfigdata.feedbackQuestions;
for (var i = 0; i < feedbackQuestions.length; i++) {
var eachquestionmap=feedbackQuestions[i];
var eachAnswer=jQuery("input[name='answer"+i+"']:checked").val();
if(undefined == eachAnswer || null == eachAnswer || '' == eachAnswer)
eachAnswer='No Response';

if(qanda =='')
qanda=eachquestionmap.question+" : "+eachAnswer;
else
qanda=qanda+", "+eachquestionmap.question+" : "+eachAnswer;
}

//alert (qanda);
postFeedbacktext(starratingcount,feedbacktext, qanda,resturl,"Certifications Feedback");
// $("#surveyAnswerNo").attr("disabled", true);
//$("#surveyAnswerYes").attr("disabled", true);
jQuery("#feedbackSurveyMain").hide();
feedbackSubmited=true;


});

jQuery("#feedbackCancelBtn").on( 'click',function () {
//jQuery("#surveyAnswerYes").attr("checked", false);
//jQuery("#surveyAnswerNo").attr("checked", true);
jQuery("input[name='surveyAnswer']").attr("checked", false);
resetStars();
});

// modal close event
jQuery("#feedbackModalDialog").on("hidden.bs.modal",function () {

jQuery("#feedbackModalDialog")
.find("input:not([type=hidden]),textarea,select")
.val('')
.end()
.find("input[type=checkbox], input[type=radio]")
.prop("checked", "")
.end();

// alert("modalclosed");

});



}

var starratingcount=0;
function feedbackOnlclickActions(selrowcount)
{
starratingcount=selrowcount;
loadFeedBackSubmitted(starratingcount);
// showFeedbackDailogbox(selrowcount);
}

function showMessagebox(message)
{
Ext.MessageBox.show({
title:'Sent Feedback',
msg:message,
buttons: Ext.MessageBox.OK,
icon: Ext.MessageBox.INFO
});
}


function getcustomCsrfToken ()
{
//var csrftoken = scrubTokens();
var csrftoken=null;

if(undefined != jsonconfigdata && null != jsonconfigdata && jsonconfigdata.SCRUB_TOKENS)
csrftoken = scrubTokens();

csrftoken=PluginHelper.getCsrfToken();


return csrftoken;
}

function scrubTokens() {
var csrfCookieValue = null;
var xsrfCookieValue = null;
var csrfName = "CSRF-TOKEN";
var xsrfName = "XSRF-TOKEN";
var xsrfExists = false;
var csrfExists = false;
var newCookies = [];

if (document.cookie && document.cookie != '')
{
//console.log("before document.cookie: "+document.cookie);
var cookies = document.cookie.split(';');
for (var i = 0; i < cookies.length; i++)
{
var cookie = cookies[i].trim();
if (cookie.substring(0, xsrfName.length + 1) == (xsrfName + '='))
{
xsrfCookieValue = cookie.substring(xsrfName.length + 1);
xsrfExists = true;
//console.log("found xsrf token: "+xsrfCookieValue);
//document.cookie = "X-XSRF-TOKEN= ; expires = Thu, 01 Jan 1970 00:00:00 GMT;path=/";

}
else if (cookie.substring(0, csrfName.length + 1) == (csrfName + '='))
{
csrfCookieValue = cookie.substring(csrfName.length + 1);
newCookies.push(cookie);
//console.log("found csrf token: "+csrfCookieValue);
csrfExists = true;
//document.cookie = "CSRF-TOKEN= ; expires = Thu, 01 Jan 1970 00:00:00 GMT;path=/";
}

}
if(!xsrfExists || xsrfCookieValue != csrfCookieValue)
{
newCookies.push(xsrfName + '=' + csrfCookieValue);
//console.log("xsrf doesn't exist or the values are diff.");
//console.log("adding the xsrf cookie: "+xsrfName + '=' + csrfCookieValue);
}
//console.log("is empty ? decoded document.cookie: "+decodeURI(document.cookie));
document.cookie=xsrfName + '=' + csrfCookieValue;
document.cookie=csrfName + '=' + csrfCookieValue;
newCookies.push(xsrfName + '=' + csrfCookieValue);
//console.log("decoded document.cookie: "+decodeURI(document.cookie));
//document.cookie=newCookies.join(';');
//console.log("newcookies.join: "+newCookies.join(';'));
//console.log("after seting to newCookies. document.cookie: "+document.cookie);


}
//console.log("after document.cookie: "+document.cookie);

return document.cookie;
}

function postFeedbacktext(selrowcount,feedbacktext,qanda,resturl,actionType) {

payload = {
feedbacktext:feedbacktext,
certId: jsonconfigdata.CERT_ID,
feedbackcode: selrowcount,
qanda: qanda,
actionType: actionType
};

Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){

if (transport.responseText.indexOf("Success") ==-1)
SailPoint.EXCEPTION_ALERT("Thanks for the Feedback, but error while updating");
else
showMessagebox("Thank you for the feedback");


},
failure:function(transport){
//alert("Error: " - transport.responseText);
SailPoint.EXCEPTION_ALERT("Thanks for the Feedback, but error while updating");
}
});


}

//Reset the text inside the div or hide it here
function loadFeedBackSubmitted(selectedStars) {
jQuery(".swecertsfeedbackdivLayoverUniversal").each(function() {
var optionshtml="";
optionshtml='<span class="swecertsfeedbackdivLayoverUniversal" >';
//unchecked stars
for(var starsUnchecked = selectedStars; starsUnchecked < 5; starsUnchecked++)
{
optionshtml=optionshtml+'<span class="sweStarSizeNoHover fa fa-star"></span>';
}
//checked stars
for(var stars = 0; stars < selectedStars; stars++)
{
optionshtml=optionshtml+'<span class="sweStarSizeSelected fa fa-star"></span>';
}
optionshtml=optionshtml+'</span>';
//optionshtml=optionshtml+'<span class="Universalfeedbacklabel"> !Thank you</span></div>';
$(this).replaceWith(optionshtml);

});
}

function getStartRatingHTML() {
var optionshtml = '<span class="swecertsfeedbackdivLayoverUniversal" >';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star"  onclick="feedbackOnlclickActions(5)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star"  onclick="feedbackOnlclickActions(4)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star" onclick="feedbackOnlclickActions(3)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star"  onclick="feedbackOnlclickActions(2)"></span>';
optionshtml=optionshtml+'<span class="sweStarSize fa fa-star"  onclick="feedbackOnlclickActions(1)"></span>';
optionshtml=optionshtml+'<span class="Universalfeedbacklabel"></span></span>';

return optionshtml;
}

function resetStars() {

jQuery(".swecertsfeedbackdivLayoverUniversal").each(function() {
var optionshtml = getStartRatingHTML();

$(this).replaceWith(optionshtml);

});
starratingcount=0;
}

var certificationURL="";
function initPluginOptions() {
if (jQuery( "#csvDownloadLink" ).length)
{
certificationURL=jQuery( "#csvDownloadLink" ).attr("href");
certsPluginCheckConfig();
}
else
//if page not loaded then wait
setTimeout(initPluginOptions,1000);
}

function certsPluginCheckConfig() {
formatHTMLTags();

payload = {
certUrl: certificationURL
};

var resturl = PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/certsPluginCheckConfig');
Ext.Ajax.request({
url: resturl,
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success: function(transport){
// do something
//alert("success: "+transport.responseText);
jsonconfigdata = Ext.JSON.decode(transport.responseText);
// alert("jsonconfigdata: "+jsonconfigdata);
//  alert("ENABLE_TEXT_MESSAGE: "+jsonarrayData.ENABLE_TEXT_MESSAGE);

loadPluginOptions();
},
failure:function(transport){
//alert("Error: " - transport.responseText);
//SailPoint.EXCEPTION_ALERT("Error= "+transport.responseText);
loadBasicOptionsonPluginError();
}
});

}

var getAccessDescFeedbackResults=[];
var getAccessDescFeedbackProcessing=[];
function getExistingAccessDescFeedbackRating(thisObj,certItemId,accessId) {

var resultIndex = getAccessDescFeedbackResults.findIndex(x => x.certItemId==certItemId);
if(resultIndex == -1 && getAccessDescFeedbackProcessing.indexOf(certItemId) == -1)
{
getAccessDescFeedbackProcessing.push(certItemId);

payload = {
certItemId: certItemId,
accessId: accessId
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getExistingAccessDescFeedbackRating'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
var rating = Ext.JSON.decode(transport.responseText);

if(undefined != rating && rating != '')
{
getAccessDescFeedbackResults.push({certItemId : certItemId, rating : rating, result : true});
setUIAccessDescFeedbackRating(thisObj,rating,certItemId);
}
else
{
getAccessDescFeedbackResults.push({certItemId : certItemId, rating : rating, result : false});
}

//console.log("getExistingAccessDescFeedbackRating accessId: "+ accessId+" Result : "+ Ext.JSON.decode(transport.responseText));
getAccessDescFeedbackProcessing.splice(getAccessDescFeedbackProcessing.indexOf(certItemId),1);

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("error in getting Existing Access Desc Feedback Rating");
}
});

}
else
{
var oldResultMap=getAccessDescFeedbackResults[resultIndex];
//console.log("else oldResultMap: "+ oldResultMap);
if(undefined != oldResultMap && oldResultMap.result)
{
setUIAccessDescFeedbackRating(thisObj,oldResultMap.rating,certItemId);
}
}

}

function setUIAccessDescFeedbackRating(thisObj,rating,certItemId) {
var feedbacksection=thisObj.find('.custDescFeedbackYesNoSection');
if(rating == 'Yes' && feedbacksection.length)
{
var yesbutton=feedbacksection.find(".swecustDescFeedbackYes");
var nobuttonselected=feedbacksection.find(".swecustDescFeedbackNoSelected");
var clearbutton=feedbacksection.find(".swedescfeedbackafterclearlinknot-active");

if(yesbutton.length)
yesbutton.removeClass().addClass("swecustDescFeedbackYesSelected");
if(nobuttonselected.length)
nobuttonselected.removeClass().addClass("swecustDescFeedbackNo");
if(clearbutton.length)
clearbutton.removeClass().addClass("swedescfeedbackafterclearlink");
swecustDescFeedbackClearRatingClickEvent(thisObj);
}
else if(rating == 'No' && feedbacksection.length)
{
var yesbuttonselected=feedbacksection.find(".swecustDescFeedbackYesSelected");
var nobutton=feedbacksection.find(".swecustDescFeedbackNo");
var clearbutton=feedbacksection.find(".swedescfeedbackafterclearlinknot-active");

if(yesbuttonselected.length)
yesbuttonselected.removeClass().addClass("swecustDescFeedbackYes");
if(nobutton.length)
{
nobutton.removeClass().addClass("swecustDescFeedbackNoSelected");
swecustDescFeedbackNoSelectedClickEvent(thisObj);
nobutton.attr("id","item_"+certItemId);
nobutton.attr("data-toggle","dropdown");
nobutton.attr("aria-haspopup","true");
nobutton.attr("aria-expanded","true");
}
if(clearbutton.length)
clearbutton.removeClass().addClass("swedescfeedbackafterclearlink");
swecustDescFeedbackClearRatingClickEvent(thisObj);
}
}

var OtherDataResults=[];
var OtherDataProcessing=[];
function getOtherData(thisObj,certItemId,accessId) {
var resultIndex = OtherDataResults.findIndex(x => x.certItemId==certItemId);
if(resultIndex == -1 && OtherDataProcessing.indexOf(certItemId) == -1)
{

/*if(OtherDataProcessing.indexOf(certItemId) == -1)
{
*/	  OtherDataProcessing.push(certItemId);
payload = {
certItemId: certItemId,
accessId: accessId,
sysIdCert: jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS,
entOwnerCert: jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS,
displayPrevScores: jsonconfigdata.DISPLAY_PREVALENCE_SCORES,
bearerToken: bearerToken,
certId: jsonconfigdata.CERT_ID
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getOtherData'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
var resultData = Ext.JSON.decode(transport.responseText);
//console.log("resultData"+ resultData)
if(undefined != resultData && resultData.result)
{
OtherDataResults.push(resultData);
updateOtherData(thisObj,resultData);

}
else
{
OtherDataResults.push({certItemId : certItemId, result : false});
thisObj.find(".dummy-otherdata-flag").removeClass().addClass("access-otherdata-flag");
thisObj.find(".dummy-otherdata-info").removeClass().addClass("access-otherdata-info");
}


// console.log("getOtherData certItemId: "+ certItemId+" Result : "+ Ext.JSON.decode(transport.responseText));
OtherDataProcessing.splice(OtherDataProcessing.indexOf(certItemId),1);

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("error in getOtherData: " + transport.responseText);
}
});

}
else
{

var oldResultMap=OtherDataResults[resultIndex];
//console.log("else getOtherData oldResultMap: "+ oldResultMap);
if(undefined != oldResultMap && oldResultMap.result && OtherDataProcessing.indexOf(certItemId) == -1)
{
OtherDataProcessing.push(certItemId);
updateOtherData(thisObj,oldResultMap);
OtherDataProcessing.splice(OtherDataProcessing.indexOf(certItemId),1);
}

}

}

function updateOtherData(thisObj,resultData) {
var accessHelpInfo='';
var accessHelpFlags='';






if(resultData.HighRisk)
{
var title='Any SOX or Privileged access will be tagged as High Risk';
if(resultData.isPriv && resultData.isSox)
title='SOX and Privileged Entitlement';
else if(resultData.isPriv)
title='Privileged Entitlement';
else if(resultData.isSox)
title='SOX Entitlement';

accessHelpFlags=accessHelpFlags+"<span  class=\"access-priv\"><span class=\"fa fa-warning flagaccessinfoicon\"></span>High Risk<span class=\"access-priv-helptip\">"+title+"</span></span>";

}



// Ent Owner Cert
if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true && undefined != resultData.grantedByRoleName)
{
accessHelpFlags=accessHelpFlags+"<span  class=\"access-priv\"><span class=\"fa fa-warning flagaccessinfoicon\"></span>Access Granted By Role<span class=\"access-priv-helptip\">This access is granted via "+resultData.grantedByRoleName+" role</span></span>";
}

//last login
if(resultData.lastlogin6monthsbefore && undefined != resultData.lastlogindiffStr && undefined != resultData.lastLoginDateStr)
{
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3\"> Last Login: </span><span title=\"Last Login Date is more than 60 days\" class=\"sweentvalred3\"> "+resultData.lastlogindiffStr.toLowerCase()+" ("+resultData.lastLoginDateStr +")</span>";

accessHelpFlags=accessHelpFlags+"<span  class=\"stale-account\"><span class=\"fa fa-clock-o flagaccessinfoicon\"></span> Last Login: "+resultData.lastlogindiffStr+"<span class=\"stale-account-helptip\">Last Login Date was "+resultData.lastLoginDateStr+" which is more than 60 days. Please review carefully as this user may no longer need this access.</span></span>";

}
else if(resultData.lastlogin3monthsbefore && undefined != resultData.lastlogindiffStr && undefined != resultData.lastLoginDateStr)
{
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3\"> Last Login: </span><span title=\"Last Login Date is more than 30 days\" class=\"sweentvalred3\"> "+resultData.lastlogindiffStr.toLowerCase()+" ("+resultData.lastLoginDateStr +")</span>";
accessHelpFlags=accessHelpFlags+"<span  class=\"less-stale-account\"><span class=\"fa fa-clock-o flagaccessinfoicon\"></span> Last Login: "+resultData.lastlogindiffStr+"<span class=\"stale-account-helptip\">Last Login Date was "+resultData.lastLoginDateStr+" which is more than 30 days. Please review carefully as this user may no longer need this access.</span></span>";

}
else if(resultData.lastloginrecent && undefined != resultData.lastlogindiffStr && undefined != resultData.lastLoginDateStr)
{
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3\"> Last Login: </span><span class=\"sweentval3\"> "+resultData.lastlogindiffStr.toLowerCase()+" ("+resultData.lastLoginDateStr +")</span>";
}
else if(resultData.staleaccount)
{
accessHelpFlags=accessHelpFlags+"<span  class=\"stale-account\"><span class=\"fa fa-clock-o flagaccessinfoicon\"></span> Last Login: Never<span class=\"stale-account-helptip\">This access has not been used since the access was granted, which is more than 30 days. Please review carefully as this user may no longer need this access.</span></span>";
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3\">Last Login: </span><span class=\"sweentval3\">Not Accessed in Tracking Period</span>";
}
else if(resultData.recentaccount)
{
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3\">Last Login: </span><span class=\"sweentval3\">Not Accessed in Tracking Period</span>";
}

// access info
if(resultData.foundgranted && undefined != resultData.granteddiffStr && undefined != resultData.accessGrantedOnStr)
{
accessHelpInfo = accessHelpInfo+"<span class=\"fa fa-gift accessinfoicon\"></span><span class=\"sweentlbl3\" title=\"Date may not be accurate\">Granted: </span><span title=\"Date may not be accurate\" class=\"sweentval3\"> "+resultData.granteddiffStr.toLowerCase()+" ("+resultData.accessGrantedOnStr +")</span>"
}
if(resultData.disabled)
{
accessHelpInfo = "<span class=\"fa fa-lock accessinfoicon\"></span><span class=\"sweentlbl4\"> Status: </span><span class=\"sweentvalred3\">Disabled</span>"
}
if(resultData.locked)
{
accessHelpInfo = "<span class=\"fa fa-lock accessinfoicon\"></span><span class=\"sweentlbl4\"> Status: </span><span class=\"sweentvalred3\">Locked</span>"
}

// previous jobchange
if(resultData.moverflag && undefined != resultData.moverDatedispStr && undefined != resultData.accessGrantedOnStr)
{
accessHelpFlags=accessHelpFlags+"<span  class=\"access-jobchange-violation\"><span class=\"fa fa-clock-o flagaccessinfoicon\"></span> Granted in Prior Job<span class=\"access-jobchange-helptip\">This User recently transferred roles on "+resultData.moverDatedispStr+" but was granted this access prior to that on "+resultData.accessGrantedOnStr+". Please review carefully to ensure they still require this access</span></span>";
}

// shared mail box
if(jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS == true && undefined != resultData.sharedmailbox && undefined != resultData.mail)
{
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-envelope accessinfoicon\"></span><span class=\"sweentlbl3\">Mail Box Name: </span><span class=\"sweentval3\">"+resultData.mail+"</span>";
accessHelpFlags=accessHelpFlags+"<span  class=\"access-SharedMailBox\"><span class=\"fa fa-envelope flagaccessinfoicon\"></span>Shared Mail Box<span class=\"access-SharedMailBox-helptip\">This is a Shared Mail Box with Name "+resultData.mail+"</span></span>";
}
if(undefined != resultData.daysToExpire && resultData.daysToExpire >= 0)
{
if(resultData.daysToExpire > 0)
accessHelpFlags=accessHelpFlags+"<span  class=\"role-sunset-expiring\">This role is from a previous Department ID and cannot be retained. Even if approved, access will be deleted in "+resultData.daysToExpire+" days.</span>";
else
accessHelpFlags=accessHelpFlags+"<span  class=\"role-sunset-expiring\">This role is from a previous Department ID and cannot be retained. Even if approved, access will be deleted by tomorrow.</span>";

accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-clock-o accessinfoicon\"></span><span class=\"sweentlbl3 role-sunset-expiring-Info\">Access expiration: </span><span class=\"sweentval3 role-sunset-expiring-Info\">"+resultData.endDate+"</span>";
}

if(resultData.isNPI)
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-warning accessinfoicon\" title=\"Non-Public Information\"></span><span class=\"sweentlbl3\" title=\"Non-Public Information\"> NPI: </span><span class=\"sweentval3\" title=\"Non-Public Information\">Yes</span>";
else
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-warning accessinfoicon\" title=\"Non-Public Information\"></span><span class=\"sweentlbl3\" title=\"Non-Public Information\"> NPI: </span><span class=\"sweentval3\" title=\"Non-Public Information\">No</span>";

/*
//prevalence Score
if(jsonconfigdata.DISPLAY_PREVALENCE_SCORES == true && undefined != resultData.prevalenceScore && undefined != resultData.communityType && undefined != resultData.prevalenceCount)
{

console.log("resultData.prevResultStatus: "+resultData.prevResultStatus);
var prevDispLabel='Unique';
var communityType='for this manager'
if(resultData.communityType.indexOf('job code') != -1)
communityType='for this manager in this job code';

var prevDispText="No one else who works "+communityType+" has this access.";

if(resultData.prevalenceCount > 0)
{
var percentile=((resultData.prevalenceCount/resultData.communitySize)*100).toFixed();

if(undefined != resultData.communitySize && percentile < 60)
{
prevDispText="Used by only "+percentile+"% of people who work "+communityType+".";
prevDispLabel='Uncommon';
}
else if(undefined != resultData.communitySize && percentile >= 60)
{
prevDispText="Used by "+percentile+"% of people who work "+communityType+".";
prevDispLabel='Common';
}
//else
//prevDispText="Used some of people who work "+communityType+".";
}
if(percentile >= 60)
{}
else
{
accessHelpFlags=accessHelpFlags+"<span  class=\"less-stale-account\"><span class=\"fa fa-asterisk flagaccessinfoicon\"></span>"+prevDispLabel+"<span class=\"stale-account-helptip\">"+prevDispText+"</span></span>";
}
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-asterisk accessinfoicon\"></span><span class=\"sweentlbl3\">"+prevDispLabel+": </span><span class=\"sweentvalred3\"> "+prevDispText+"</span>";
}
*/
if(accessHelpFlags != '' && thisObj.find(".swecertactualdescwithoutflags").length)
{
thisObj.find(".swecertactualdescwithoutflags").removeClass().addClass("swecertactualdescwithflags");
}

if(jsonconfigdata.SHOW_SYSTEM_ID_CERT_OPTIONS == true && undefined != resultData.sharedmailbox && undefined != resultData.descupdate && undefined != thisObj.find(".swecertactualdescwithflags"))
{
var desc="<span class=\"swecertactualdescwithflags\">"+resultData.descupdate+"</span>";
thisObj.find(".swecertactualdescwithflags").replaceWith(desc);
}

if(accessHelpFlags != '' && thisObj.find(".dummy-otherdata-flag").length)
thisObj.find(".dummy-otherdata-flag").replaceWith('<span class="access-otherdata-flag">'+accessHelpFlags+'</span>');

if(accessHelpInfo != '' && thisObj.find(".dummy-otherdata-info").length)
thisObj.find(".dummy-otherdata-info").replaceWith('<span class="access-otherdata-info">'+accessHelpInfo+'</span>');


///////////////Ent owner cert self certify use case to hide check box////////////////
if(jsonconfigdata.SHOW_ENT_OWNER_CERT_OPTIONS == true)
{
if(resultData.selfReviewItem)
{
//console.log("resultData.selfReviewItem: "+resultData.selfReviewItem);
var trNode=thisObj.closest('tr');
//console.log("trNode: "+trNode);
if(undefined != trNode)
{
// checkbox disable code
var checkBoxNode= trNode.find("td > sp-checkbox > button");
//console.log("disbling self review item checkBoxNode: "+checkBoxNode);
checkBoxNode.attr('disabled','true');
//checkBoxNode.hide();

if(!checkBoxNode.hasClass("selfReviewItem"))
checkBoxNode.addClass("selfReviewItem");

// checkbox uncheck code
if(checkBoxNode.hasClass("fa-check-square-o"))
checkBoxNode.removeClass("fa-check-square-o").addClass("fa-square-o");

// action button icon add code
/*
var actionColumnNode= trNode.find("sp-certification-action-column");
console.log("adding icon to actionColumnNode: "+actionColumnNode);

if(!actionColumnNode.hasClass("entOwnerCertInfoIconSelfReviewItem"))
actionColumnNode.addClass("entOwnerCertInfoIconSelfReviewItem");
*/
//var actionColumnNode= trNode.find("td.normal-col.ng-scope.col-fixed-right.no-row-click");
var actionColumnNode= trNode.find(".cert-action-column > div > div");
if(!actionColumnNode.hasClass("entOwnerCertInfoIconSelfReviewItem"))
{
actionColumnNode.addClass("entOwnerCertInfoIconSelfReviewItem");
if(!actionColumnNode.find(".entOwnerCertSelfReviewSection").length)
{
actionColumnNode.prepend("<span  class=\"entOwnerCertSelfReviewSection\"><span class=\"fa fa-exclamation-circle entOwnerCertSelfReviewItem\"></span><span class=\"entOwnerCertSelfReviewItem-helptip\"><br/><b>Self Approval Not Allowed</b> <br/><br/>A different Workgroup member will need to <br/>decision item in order to complete the certification.<br/><br/></span></span>");
}
}
}
}
else
{
var trNode=thisObj.closest('tr');
if(trNode.length && jQuery(".selfReviewItem").length)
{
// checkbox enable code
var checkBoxNode= trNode.find("td > sp-checkbox > button");
//console.log("enabling non self review item checkBoxNode: "+checkBoxNode);
checkBoxNode.removeAttr("disabled");
//checkBoxNode.show();
checkBoxNode.removeClass("selfReviewItem");

// checkbox check code
var tableNode = thisObj.closest("table");
if(tableNode.length)
{
var eachCheckBoxNode = tableNode.find("thead > tr:first th > span > sp-checkbox > button");
if(eachCheckBoxNode.length && eachCheckBoxNode.hasClass("fa-check-square-o"))
{
//console.log("checking non self item CheckBoxNode.length: "+eachCheckBoxNode.length);
var checkBoxNode= trNode.find("td > sp-checkbox > button");
checkBoxNode.removeClass("fa-square-o").addClass("fa-check-square-o");
}
}

// action button icon remove code
/* var actionColumnNode= trNode.find("sp-certification-action-column");
console.log("adding icon to actionColumnNode: "+actionColumnNode);

if(actionColumnNode.hasClass("entOwnerCertInfoIconSelfReviewItem"))
actionColumnNode.removeClass("entOwnerCertInfoIconSelfReviewItem");
*/
//var actionColumnNode= trNode.find("td.normal-col.ng-scope.col-fixed-right.no-row-click");
var actionColumnNode= trNode.find(".cert-action-column > div > div");
if(actionColumnNode.hasClass("entOwnerCertInfoIconSelfReviewItem"))
{
actionColumnNode.removeClass("entOwnerCertInfoIconSelfReviewItem");
if(actionColumnNode.find(".entOwnerCertSelfReviewSection").length)
actionColumnNode.find(".entOwnerCertSelfReviewSection").remove();
}
}
}
}


}

var prevalenceScoresDataResults=[];
var prevalenceScoresDataProcessing=[];
function getprevalenceScoresData(thisObj,certItemId,accessId) {
var resultIndex = prevalenceScoresDataResults.findIndex(x => x.certItemId==certItemId);
if(resultIndex == -1 && prevalenceScoresDataProcessing.indexOf(certItemId) == -1)
{

/*if(prevalenceScoresDataProcessing.indexOf(certItemId) == -1)
{
*/	  prevalenceScoresDataProcessing.push(certItemId);
payload = {
certItemId: certItemId,
accessId: accessId,
certId: jsonconfigdata.CERT_ID,
bearerToken: bearerToken
};

Ext.Ajax.request({
url: PluginHelper.getPluginRestUrl('sweCertificationsPluginAddins/getprevalenceScoresData'),
method: 'POST',
//contentType: "text/plain",
headers: {'X-XSRF-TOKEN': getcustomCsrfToken (),'Content-Type': 'application/json','Accept': 'application/json'},
params : Ext.JSON.encode(payload),
success:function(transport){
var resultData = Ext.JSON.decode(transport.responseText);
//console.log("resultData"+ resultData)
if(undefined != resultData && resultData.result)
{
prevalenceScoresDataResults.push(resultData);
updateprevalenceScoresData(thisObj,resultData);

}
else
{
prevalenceScoresDataResults.push({certItemId : certItemId, result : false});
thisObj.find(".dummy-prev-flag").removeClass().addClass("access-prev-flag");
thisObj.find(".dummy-prev-info").removeClass().addClass("access-prev-info");
}

console.log("getprevalenceScoresData certItemId: "+ certItemId+" Result : "+ Ext.JSON.decode(transport.responseText));
//console.log("getprevalenceScoresData certItemId: "+ certItemId+" Result : "+ Ext.JSON.decode(transport.responseText));
prevalenceScoresDataProcessing.splice(prevalenceScoresDataProcessing.indexOf(certItemId),1);

},
failure:function(transport){
//alert("Error: " - transport.responseText);
console.log("error in getprevalenceScoresData: " + transport.responseText);
}
});

}
else
{

var oldResultMap=prevalenceScoresDataResults[resultIndex];
//console.log("else getprevalenceScoresData oldResultMap: "+ oldResultMap);
if(undefined != oldResultMap && oldResultMap.result && prevalenceScoresDataProcessing.indexOf(certItemId) == -1)
{
prevalenceScoresDataProcessing.push(certItemId);
updateprevalenceScoresData(thisObj,oldResultMap);
prevalenceScoresDataProcessing.splice(prevalenceScoresDataProcessing.indexOf(certItemId),1);
}

}

}

function updateprevalenceScoresData(thisObj,resultData) {
var accessHelpInfo='';
var accessHelpFlags='';


//prevalence Score
if(jsonconfigdata.DISPLAY_PREVALENCE_SCORES == true && undefined != resultData.prevalenceScore && undefined != resultData.communityType && undefined != resultData.prevalenceCount)
{

console.log("resultData.prevResultStatus: "+resultData.prevResultStatus);
var prevDispLabel='Unique';
var communityType='for this manager'
if(resultData.communityType.indexOf('job code') != -1)
communityType='for this manager in this job code';

var prevDispText="No one else who works "+communityType+" has this access.";

if(resultData.prevalenceCount > 0)
{
var percentile=((resultData.prevalenceCount/resultData.communitySize)*100).toFixed();

if(undefined != resultData.communitySize && percentile < 60)
{
prevDispText="Used by only "+percentile+"% of people who work "+communityType+".";
prevDispLabel='Uncommon';
}
else if(undefined != resultData.communitySize && percentile >= 60)
{
prevDispText="Used by "+percentile+"% of people who work "+communityType+".";
prevDispLabel='Common';
}
//else
//prevDispText="Used some of people who work "+communityType+".";
}
if(percentile >= 60)
{}
else
{
accessHelpFlags=accessHelpFlags+"<span  class=\"less-stale-account\"><span class=\"fa fa-asterisk flagaccessinfoicon\"></span>"+prevDispLabel+"<span class=\"stale-account-helptip\">"+prevDispText+"</span></span>";
}
accessHelpInfo=accessHelpInfo+"<span class=\"fa fa-asterisk accessinfoicon\"></span><span class=\"sweentlbl3\">"+prevDispLabel+": </span><span class=\"sweentvalred3\"> "+prevDispText+"</span>";
}

if(accessHelpFlags != '' && thisObj.find(".swecertactualdescwithoutflags").length)
{
thisObj.find(".swecertactualdescwithoutflags").removeClass().addClass("swecertactualdescwithflags");
}



if(accessHelpFlags != '' && thisObj.find(".dummy-prev-flag").length)
thisObj.find(".dummy-prev-flag").replaceWith('<span class="access-prev-flag">'+accessHelpFlags+'</span>');

if(accessHelpInfo != '' && thisObj.find(".dummy-prev-info").length)
thisObj.find(".dummy-prev-info").replaceWith('<span class="access-prev-info">'+accessHelpInfo+'</span>');

}

