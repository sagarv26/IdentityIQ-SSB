/* will execute the function on script load */
	jQuery(document).ready(function(){
		showIdentityAccountsAddin();
	});

// handles the path change event and invokes the function
window.addEventListener('popstate', showIdentityAccountsAddin);


//==================================================
/* function to display secret management plugin link in the manage accounts page */ 

	function showIdentityAccountsAddin(){

		if(window.location.href.indexOf('/identityiq/identities/identities.jsf#/identities/') !== -1 && (window.location.href.indexOf('/accounts') !== -1) && !document.getElementById('secretManagement-notification')) {
			var IdentityId = window.location.hash.split('/')[2];

		// api call to get the serviceIdentity flag
			$.ajax({
				url: `/identityiq/plugin/rest/AccessRequestApprovalAddinsUI/getIdentityById?searchKey=${IdentityId}`,
				headers: {
					'X-XSRF-TOKEN' : getnewCsrfToken()	
				},
				success: function(data) {
				
				// to validate if the user is service identity					
					if(data.serviceIdentity){
							//console.log("hey this is service account");

						//setting interval of 100ms 
							var foundAccounts = false;
							var pid = setInterval(function(){
											
							if(document.getElementById('panelHeadingLeftmanageAccountLinkColConfig')){
								foundAccounts = true;
							}
				
						//will check if the custom div is already inserted and returns false
							if(document.getElementById('secretManagement-notification')){
								foundAccounts = false;
								//console.log("show this when identityAccountsMessageBanner is already printed");
							}
				
						//checks for the above mentioned structure 
							if(foundAccounts){
								clearInterval(pid);
								var finalPath = window.location.origin+"/identityiq/plugins/pluginPage.jsf?pn=SecretManagement#/form/profile";
								//console.log("printing the final plugin path: " +finalPath);
								var htmlContent = `<div id="secretManagement-notification" class="identityAccountsMessageBanner"><i class="fa fa-info-circle" role="presentation"></i>&nbsp; To create a new AD Service Account, please utilize our new Secrets Management intake form. By registering your secret as managed, password rotation for your account will be performed automatically. <a target="_blank" href=${finalPath} id="finalPath" rel="noopener noreferrer"><span>&nbsp;<button type="button" class="btn btn-info btn-xs"> Click here</button></span></a> </div>`;
								$(`${htmlContent}`).insertBefore('.data-table-heading');
									
							}

						},200);

					}
						
				}
			});

		}

	}


	



