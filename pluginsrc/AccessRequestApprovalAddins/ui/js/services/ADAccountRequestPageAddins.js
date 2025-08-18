/* will execute the function on script load */
jQuery(document).ready(function(){
	showADprovisioningFormAccountRequestPage();
});

// handles the path change event and invokes the function
window.addEventListener('popstate', showADprovisioningFormAccountRequestPage);


//==================================================
/* function to display secret management link in the Identity account request AD provisioning form page */ 
function showADprovisioningFormAccountRequestPage(){
	
	if(window.location.href.indexOf('/identityiq/identities/identities.jsf#/identities/') !== -1 && (window.location.href.indexOf('/accounts/confirm') !== -1))  {
		var foundAccountSetting = false;
		var foundDomain = false;
		var foundServiceIdentity = false;

	//setting interval of 100ms 
		var pid = setInterval(function(){
			
			/* checking for the Account Setting, Target Identity and Domain name label */
					$('sp-form-item').each(function(i,spItem){
						$(spItem).find('.form-section-title strong').each(function(i,e){
					//	console.log(i,e.textContent)
						if(e.textContent === 'Account Settings'){
							foundAccountSetting = true;
						}

						if(e.textContent === 'Target Identity'){
							//console.log('Target Identity', e);
						const innerLabel = $($(e?.parentElement?.parentElement?.parentElement?.parentElement).find('div[ng-repeat]')[0]).find('span')[0].innerText;
							if(innerLabel === 'Service Identity'){
								foundServiceIdentity = true;
							}
						}

						})
					});
					
					$('sp-object-suggest').each(function(i,spItem){
						$(spItem).find('label').each(function(i,e){
						//console.log(i,e)
							if(e.getAttribute('id') === 'Domain Name-label'){
								foundDomain = true;
							}
						})
					});

				//will check if the custom div is already inserted and returns false
					if(document.getElementById('secretManagement-notification-IdentityPage')){
						foundAccountSetting = false;
						foundDomain = false;
						foundServiceIdentity = false;
						//console.log("print this when identityAccountsMessageBanner is already printed");
					}

			//checks for the above mentioned structure and inserts the message banner
			if(foundDomain && foundAccountSetting && foundServiceIdentity){
				clearInterval(pid);
				var finalPath = window.location.origin+"/identityiq/plugins/pluginPage.jsf?pn=SecretManagement#/form/profile";
				//console.log("printing the final plugin path: " +finalPath);
				var htmlContent = `<div id="secretManagement-notification-IdentityPage" class="ADaccessRequestPageMessageBanner"><i class="fa fa-info-circle" role="presentation"></i>&nbsp; To create a new AD Service Account, please utilize our new Secrets Management intake form. By registering your secret as managed, password rotation for your account will be performed automatically. <a target="_blank" href=${finalPath} id="finalPath" rel="noopener noreferrer"><span class="showADRequestProvFormAddin-HeaderLinkText">Click here</span></a> </div>`;
				//$(`${htmlContent}`).insertBefore('.form-work-item-row .panel');
				$(`${htmlContent}`).insertBefore('.modal-content');
			}

		},200);
	}
}
