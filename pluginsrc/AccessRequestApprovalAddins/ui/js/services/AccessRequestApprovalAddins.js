/* will execute the function on script load */
	jQuery(document).ready(function(){
		showWorkItemAddIn();
	});

	
//==================================================
/* function to display the custom structure in the workitem page */ 
	function showWorkItemAddIn(){
		if(window.location.pathname === "/identityiq/workitem/commonWorkItem.jsf"){
			//setting interval of 100ms 
			var pid = setInterval(function(){
				var sectionElements = $('.approval.panel .panel-body.in.collapse').find('div[ng-repeat="approvalItem in approval.approvalItems"]');
				//checks for the above mentioned structure
				if(sectionElements.length){
					clearInterval(pid);
					// gets the workItemId from the url
					var workItemId = window.location.hash.split('/')[2];
					// makes the api call
					$.ajax({
						url: `/identityiq/plugin/rest/AccessRequestApprovalAddinsUI/getWorkitemDetails?searchKey=${workItemId}`,
						headers: {
							'X-XSRF-TOKEN' : getnewCsrfToken()	
						},
						success: function(data) {
							//console.log(data);
							//console.log(getnewCsrfToken());
							$('.approval.panel .panel-body.in.collapse').find('div[ng-repeat="approvalItem in approval.approvalItems"]').each(function(index, element){
								if(data[index] && data[index].pairedAccountName){
									// var selector = "<div class='workItemMessageBanner'>&#9432; &nbsp; The below approval item includes "+data[index].pairAccount+" account approval for paired account. "+data[index].nativeIdentity+"</div>";
									var selector = `<div class='workItemMessageBanner'> <i class="fa fa-info-circle" role="presentation"></i>&nbsp; This approval is for paired account: ${data[index].pairedAccountName} and ${data[index].nativeIdentity}</div>`;
									//console.log(selector);
									jQuery(selector)
									.insertBefore(element);
								}
							})
						
						}
					});
				}
			},100);
		}
	}






