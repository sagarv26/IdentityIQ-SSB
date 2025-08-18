
//==================================================
/* handles setting the token value for the functions */
    function getnewCsrfToken ()
    {
        var csrftoken = scrubTokens();
        csrftoken=PluginHelper.getCsrfToken();
        return csrftoken;
    }


    function scrubTokens()
    {
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
