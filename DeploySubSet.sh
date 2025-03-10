#!/bin/bash

#########################################
###         Script Variables          ###
#########################################

# Network logon id (uid to authenitcate to svn and iiq)
unfiUID=""

# Subversion password
svnPwd=""

# SailPoint IdentityIQ password
iiqPwd=""

# Build tag
iiqTag="trunk"

# Identify tomcat root 
if [ "$HOSTNAME" = eplnx361 ] ||
   [ "$HOSTNAME" = wplnx623 ] ; then
    #echo "IIQ Development Server"
    TOMCAT_ROOT='/opt/tomcat/8.5.14'
elif [ "$HOSTNAME" = eplnx362 ] ||
     [ "$HOSTNAME" = eplnx363 ] ||
     [ "$HOSTNAME" = wplnx624 ] ||
     [ "$HOSTNAME" = wplnx625 ] ; then
    #echo "IIQ QA Server"
    TOMCAT_ROOT='/opt/tomcat/8.5.14'
elif [ "$HOSTNAME" = eplnx406 ] ||
     [ "$HOSTNAME" = eplnx407 ] ||
     [ "$HOSTNAME" = eplnx417 ] ||
     [ "$HOSTNAME" = eplnx420 ] ||
     [ "$HOSTNAME" = wplnx650 ] ||
     [ "$HOSTNAME" = wplnx651 ] ||
     [ "$HOSTNAME" = wplnx654 ] ||
     [ "$HOSTNAME" = wplnx657 ] ; then
    #echo "IIQ UAT Server"
    TOMCAT_ROOT='/opt/tomcat/7.0.61'
elif [ "$HOSTNAME" = eplnx408 ] ||
     [ "$HOSTNAME" = eplnx409 ] ||
     [ "$HOSTNAME" = eplnx418 ] ||
     [ "$HOSTNAME" = eplnx419 ] ||
     [ "$HOSTNAME" = wplnx652 ] ||
     [ "$HOSTNAME" = wplnx653 ] ||
     [ "$HOSTNAME" = wplnx655 ] ||
     [ "$HOSTNAME" = wplnx656 ] ; then
    #echo "IIQ Prodution Server"
    TOMCAT_ROOT='/opt/tomcat/7.0.61'
else
    #echo "Unknown server"
    printf "Script running on unknown server '$HOSTNAME', exiting...\n"
    exit
fi

#########################################
###          Start of Script          ###
#########################################

# Prompt user for values if any variables are empty
if [ -z "$unfiUID" ]; then
    read -p "Enter UID: " unfiUID
fi

if [ -z "$svnPwd" ]; then
    read -sp "Enter SVN Password: " svnPwd
    printf "\n"
fi

if [ -z "$iiqPwd" ]; then
    read -sp "Enger IIQ Password: " iiqPwd
    printf "\n"
fi

if [ -z "$iiqTag" ]; then
    read -p "Enter TAG:" iiqTag
fi

if [ $iiqTag == "trunk" ]; then
    url="http://sdedaps0286.devsvuent.supervalu.com/svn/SailpointCodeRepo/trunk"
else
    url="http://sdedaps0286.devsvuent.supervalu.com/svn/SailpointCodeRepo/tag/$iiqTag"
fi

# set java variables
source $TOMCAT_ROOT/tomcatprofile

# checkout tagged build to tmp directory
cd /tmp
svn checkout --username $unfiUID --password $svnPwd --non-interactive $url

# give execute writes to build script and ant binary
chmod ug+x /tmp/$iiqTag/build.sh
chmod ug+x /tmp/$iiqTag/lib/ant/bin/ant

# execute build script
cd /tmp/$iiqTag
/tmp/$iiqTag/build.sh clean war

# check before deploying build
while true; do
    read -p "Finished building war, proceed with deployment? " yn
    case $yn in
        [Yy]* ) buildCheck=true; break;;
        [Nn]* ) buildCheck=false; break;;
        * ) echo "Please answer yes or no.";;
    esac
done

if [ $buildCheck == true ]; then 
    
    # remove WEB-INF/config folder from deployed application
    echo "Overwriting WEB-INF/config..."
    rsync -a --delete /tmp/$iiqTag/build/subset/WEB-INF/config $TOMCAT_ROOT/webapps/identityiq/WEB-INF/ 
    
    # import subset configuration from application console
    echo "Importing subset configuration at application console..."
    chmod a+x $TOMCAT_ROOT/webapps/identityiq/WEB-INF/bin/iiq
    
    echo 'import sp.init-custom.xml' > /tmp/commands.txt
    $TOMCAT_ROOT/webapps/identityiq/WEB-INF/bin/iiq console iiqBeans -u $unfiUID -p $iiqPwd < /tmp/commands.txt
    
    # cleanup
    echo "Removing temporary files needed for build process..."
    rm -f /tmp/commands.txt
else
    printf "Skipping build...\n"
fi

# check for cleanup
while true; do
    read -p "Finished deploying new configuration, cleanup files at /tmp/$iiqTag? " yn
    case $yn in
        [Yy]* ) echo "Removing /tmp/$iiqTag..."; rm -rf /tmp/$iiqTag; break;;
        [Nn]* ) break;;
        * ) echo "Please answer yes or no.";;
    esac
done

echo -e "\033[32mBuild complete.\033[0;39m\n"
