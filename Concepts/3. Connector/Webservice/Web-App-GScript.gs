var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/*********/edit#gid=0");
var sheet = ss.getSheetByName("users");

function doGet(e) {
    var op = e.parameter.action;

    if (op == "getUsers"){
      return getUsers(e);
    }

    if (op == "getLastUser"){
      return getLastUser(e);
    }
     
}

function getUsers(e){
  var records = {}

  var rows = sheet.getRange(2, 1, sheet.getLastRow() - 1, sheet.getLastColumn()).getValues();
  data = [];
  
  for (var r = 0, l = rows.length; r < l; r++) {
    var row = rows[r],
    record = {};

    record['id'] = row[0];
    record['givenName'] = row[1];
    record['familyName'] = row[2];
    record['group'] = row[3];
    record['email'] = row[4];
    record['employeeId'] = row[5];
    record['active'] = row[6];
    record['fullName'] = row[7];

    data.push(record);		

  }

  records.user = data;

  var result = JSON.stringify(records);
  
  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
  
}

function getLastUser(e){
  var lr = sheet.getLastRow();
  Logger.log(lr);
  
  var id = 7.0;
  var flag = 0;
  var group = 'test';
  for (var i = 1; i <= lr; i++) {
  var rid = sheet.getRange(i, 1).getValue();
  Logger.log(rid);
  if (rid == id) {
      Logger.log('Id Matched');
      if (group) {
         sheet.getRange(i, 4).setValue(group);
      }

    var result = "updated group successfully group : "+group;
    flag = 1;
  }
}


if (flag == 0)
  var result = "id not found";

result = JSON.stringify({
  "result": result
});

Logger.log(result);
return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.TEXT);

}

function doPost(e){
 var action = e.parameter.action;


 if(action == 'addUser'){
   return addUser(e);
 }
  if(action == 'updateUser'){
   return updateUser(e);
 }
 if(action == 'updateStatus'){
   return updateStatus(e);
 }
 
}


function addUser(e){

  var id = e.parameter.id;
  var fullName = e.parameter.fullName;
  var email = e.parameter.email;
  var givenName = e.parameter.givenName;
  var familyName = e.parameter.familyName;
  var active = e.parameter.active;
  var employeeId = e.parameter.employeeId;
  var group = e.parameter.group;

  //var lr = sheet.getLastRow();
  //var id = lr+1;

  sheet.appendRow([id,givenName,familyName,group,email,employeeId,active,fullName]);

  return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);
}


function updateUser(e){
var id = e.parameter.id;
var flag = 0;
var group = e.parameter.group;

var lr = sheet.getLastRow();


for (var i = 1; i <= lr; i++) {
  var rid = sheet.getRange(i, 1).getValue();
  if (rid == id) {
      if (group) {
         sheet.getRange(i, 4).setValue(group);
      }

    var result = "updated group successfully";
    flag = 1;
  }
}

if (flag == 0)
  var result = "id not found";

result = JSON.stringify({
  "result": result
});


return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.TEXT);
}


function updateStatus(e){
var id = e.parameter.id;
var flag = 0;
var active = e.parameter.active;

var lr = sheet.getLastRow();


for (var i = 1; i <= lr; i++) {
  var rid = sheet.getRange(i, 1).getValue();
  if (rid == id) {
      if (active) {
         sheet.getRange(i, 7).setValue(active);
      }

    var result = "updated status successfully";
    flag = 1;
  }
}

if (flag == 0)
  var result = "id not found";

result = JSON.stringify({
  "result": result
});


return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.TEXT);
}
