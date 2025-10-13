# IdentityIQ
- [What is Sailpoint?](https://youtu.be/LeUCZyBg0ME?si=NEULrvckt-85LLHa)

# Sailpoint IIQ Learnings
- [Sailpoint Complete Beginners Guide](https://youtu.be/vcfgLPXeB7c?si=hXVYBrqmeejH2CoE) 

# Setup

### High Level Installation Steps
```
Download & Expand Installation Files
        ↓
Configure Extended & Searchable Attributes
        ↓
Create Database & Tables
        ↓
Configure IIQ Database Connection
        ↓
Access IIQ & Continue Enterprise Configuration
```

### 🚀 SailPoint IIQ Supported Platforms Cheatsheet  

| Category            | Supported Platforms                                                                 |
|---------------------|--------------------------------------------------------------------------------------|
| **Operating Systems** | - AIX  <br> - Red Hat Enterprise Linux (RHEL) <br> - SuSE Linux Enterprise Server <br> - Solaris <br> - Windows Server |
| **Databases**        | - IBM DB2 *(requires correct JDBC driver from IBM)* <br> - MySQL <br> - Microsoft SQL Server 2012 <br> - Oracle <br> **Note:** IIQ includes Oracle & SQL Server JDBC drivers at release. Update to newer JDBC drivers if available for better performance. |
| **Application Servers** | *(JDK 1.6 / 1.7 supported depending on server)* <br> - Apache Tomcat <br> - Oracle WebLogic <br> - IBM WebSphere <br> - JBoss Application Server |
| **Java Platforms**   | - Sun / Oracle / IBM JDK <br> - Oracle JRockit JDK <br> - OpenJDK |

## SSD
A build process is critical for ensuring smooth deployment of a configured IdentityIQ environment. It streamlines the promotion of configuration objects across development, test, and production so that all environments remain consistent.  
Key benefits of a build process:
- Ensures all environments contain the same custom objects (applications, rules, task definitions, identity mappings, etc.).
- Simplifies integration of new custom objects and Java code.
- Provides a manageable set of commands that can be easily automated.


### SSB (Services Standard Build) Tools
- The SSB tools should be configured immediately after installing IdentityIQ in the first development environment.
- SSB is a subset of the Services Standard Deployment (SSD).
- You can download SSB as a standalone build tool, but downloading SSD provides additional components:
	- SSF (Services Standard Frameworks)
	- SST (Services Standard Test)
	- SSP (Services Standard Performance)

> [!NOTE]
> Configuration and use of SSD, SSF, SST, and SSP are documented on Compass and are outside the scope of this guide.

**Getting Started with SSB**  
**1. Prerequisites**  
- Installed and configured IdentityIQ development environment.  
- Access to SSB download package (from Compass or internal repository).  
- Java and Ant (for build automation).

**2. Setup**  
1. Download the SSB tools package.
2. Unzip it into your IdentityIQ project directory.
3. Add exported objects into config folder (config\Application, config\Bundle, config\Rule, config\Workflow)
4. Update the build.properties, env.target.properties, env.iiq.properties, env.ignorefiles.properties files with your environment details (database, application server, etc.).

**3. Common Commands**  

**build:** Runs the entire build process, placing a fully expanded war file in the \build\extract folder, and all compiled, custom .class files in \build\classes. 

**main:** Default Ant target - runs this target when no target is specified. 
Example: build without a target is essentially build main. 

**clean:**  Deletes everything in the \build directory. It is recommended to run the clean target before most deployments, as this ensures a clean working directory. 
Examples: build clean main or build clean deploy or build clean dist 

**dist:** Copies the entire expanded war content to your application server webapps directory (wherever the IIQHome property points to). 

**deploy:** Runs entire build process and deploys the expanded war content to your application server webapps directory

## Git

**Clone an existing repository**

Open a terminal and run:
```
#Replace URL with the repo you want to clone
git clone https://github.com/sagarv26/IdentityIQ-SSB.git
cd IdentityIQ-SSB
```
Now you have the repo locally.
 
Create your own new local repo
If you want to turn this into your own independent project:
``` 
# Remove the old remote (original repo link)
git remote remove origin
 
# Initialize a fresh repo (optional if .git already exists)
git init
```

**Create a new repo on GitHub**  
Go to GitHub → click New Repository.
Give it a name (say iiq).
Don’t initialize with README or license (since you already have files locally).
 
You’ll get a new repo URL like:
https://github.com/sagarv26/iiq.git
 
**Connect local repo to your new GitHub repo**
```
#Add your new GitHub repo as remote 
git remote add origin https://github.com/sagarv26/IIQ.git
 
#Make sure the remote is set correctly
git remote -v
 
Push your code to your new repo
git remote set-url origin https://token@github.com/sagarv26/IIQ.git
git branch -M main        # Rename current branch to main
git add .
git commit -m "Initial commit from cloned repo"
git push -u origin main
```

## IDE Configuration
The IdentityIQ Deployment Accelerator (IIQDA) is a plugin for the popular and free IDE Eclipse that provides several features designed to make configuring and managing IdentityIQ easier. 

1. Download Deployment Accelerator
2. Eclipse configuration
	- In your Eclipse workbench, navigate to Help -> Install New Software.
	- click the 'Archive' button and point to the zip you just downloaded.
3. Import the "Workflow-Importer.xml" workflow object into your IdentityIQ instances. (Available in my Repo)
4. Configuring a new IdentityIQ project
	- File → New → Project → Sailpoint → Identity IQ Project
	- Provide project name, local IIQ address and spadmin credentials

Reference: https://community.sailpoint.com/t5/Deployment-Accelerator-Knowledge/IIQDA-IdentityIQ-Deployment-Accelerator-User-s-guide/ta-p/76698


# IIQ Configuration

## Global Setting
### IdentityIQ Settings

**Notification Setting**

**Email Setting**
- Notification Type 
	- HTTP/OAuth
	- SMTP/Basic
	- Redirect to Email
	- Redirect to File
- Email Template - Reminder, Escalation,Workitem Forwarding

**Workitems**  
- Certification Related Workitem policy
- Archives
- Workitem Rule - Global Workitem forwarding rule, Inactive user workitems escalation rule.

**Roles**  
- Sunrise/Sunset configuration
- Allow Propagation

**Miscellaneous**  
- Object Expiration - snapshot, task result, certification archive, Certification archive deletion
- Syslog
- Provisioning Transaction
- Business Processes - Entitlement Update, Password Intercept.

### Audit Configuration

- General Actions
	- Login, Email, Events, Provisioning, Custom
- Identity Attribute Changes
	- Role assigned, password etc
- Class Actions
	- Application, Role, Task, Workflow (Create/Update/Delete)
- SCIM Actions

> [!NOTE]
> All configuration will reflect in System Configuration File

## Rules

## Application Onboarding
Application Onboarding
There are several different types of connectors. Connectors are commonly grouped by the ways in which they can communicate
There are: 
- read-only connectors that can only communicate data from an external application (Governance)
- read-write connectors that can read data from external applications and write data out to them (Gateway and Direct)  

Connectors may be added, removed, or modified in any release, including patch releases. Existing defined applications will continue to use the connector specified during their initial creation, and changes to the connector will not affect existing applications unless those changes are manually applied to the application definition 

However, the **ConnectorRegistry** entry for the connectors does change with new releases. The list of available connectors, with their current set of available features, can be retrieved from the Connector Registry within the Debug Pages.

### Important Terminologies
**Authoritative Application**
An authoritative application is a trusted source of identity data (users, attributes, statuses).
It is usually a system of record (e.g., HR system, Student Information System, Contractor DB).
IdentityIQ uses it to create and update identities in the Identity Warehouse.

**Authoritative App:** Workday (HR system)
**Flow:**
A new hire added in Workday → aggregated into IIQ → IIQ creates a new identity.
Based on policies, IIQ provisions accounts in AD, Email, SAP, etc.

**Native Change Detection**
Native Change Detection (NCD) is a mechanism in SailPoint IdentityIQ that detects real-time changes directly from authoritative or managed applications.

**Use Case:**
- Enable NCD on Active Directory in SailPoint IIQ.
- Change occurs → e.g., a user is added to Domain Admins in the target system.
- AD Aggregation runs → IIQ captures the change and records the NCD attribute in the trigger snapshot.
- Identity Refresh with Process Events enabled:
  - IIQ evaluates NCD events.
  - Triggers custom workflow (e.g., audit logging, send notification email to owner).

### Preliminaries
**Decide scope & goals:** business owner, application owner, what access must be governed (users/accounts/roles/entitlements), compliance requirements (SOX, ISO, etc.).  
**Identify app type:** SaaS vs on-prem vs database vs directory vs custom (API/REST) — this determines connector choice.  
**Collect application details (minimum):** base URLs / endpoints, protocol (LDAP/AD/SCIM/SAML/REST/JDBC), admin/service account credentials (with required privileges), sample account(s), list of entitlements/roles, account/schema attributes.   

### Configuration
```
Setup → Applications / Application Definition →
Add New Application → provide name, owner, connector type →
select authoritative/Native Change (if required). 
```

### Correlation
Configure correlation rules that determine how an application account/manager links to an identity in SailPoint (primary key mapping: email, employeeID, uid). Test correlation with sample accounts to ensure correct linking and avoid duplicate identity creation. 

### Schema
After configuring the connector, add the necessary **Account** and **Group** attributes. Most connectors provide a **Discover Schema** option  - ensure you select the correct attribute types.

Attributes without the managed or entitlement mark are just (accounts) attributes. Actually, if you mark it as "Managed", even if you didn't mark it as "Entitlement", it will appear in the Entitlement Catalog.    
**Entitlement:** adds to identity cube as an entitlement that can be certified; also makes it available for use in role mining activities. Attributes marked as "Entitlement" can be certified in a certification. They could be used in IT roles, but not so easily.  
**Managed:** adds to entitlement catalog where it can be assigned an owner, a display name, and a description, and where it can be marked as "requestable" for LCM and can be used in policy definitions

###  Provisioning Policy
> Account - Create\Update\Delete  
> Group - Create\Update

#### Why Provisioning Policy?
When a user requests entitlement or group access, and no account exists on the target system, IdentityIQ creates a new account. During this process, IIQ fills in the required account attributes based on the provisioning policies defined for that application.
```
User requests entitlement/group access → IIQ checks if account exists on target 
→ If account does NOT exist
→ Create Account →  IIQ uses Provisioning Policy → Populate required account attributes
→ Submit account creation request to target
```

### Rules
- Connector Rules
- Aggregation Rules
- Provisioning Rules
- Connector Specific Rules

#### Application Rule execution

     Connector Rule executes 
        ↓
     Customization Rule modify attributes if required 
        ↓
     Correlation Rule tries to match account → Identity
        ↓
     Match Found → Link account to Identity
        ↓
     No Match Found → Run Creation Rule → Create new Identity  

### Aggregation
Run account/group aggregation (full import) to pull accounts/groups and attributes into SailPoint

#### Other Concepts

##### FeatureString

The feature string determines which features of the application are enabled or disabled. For example if you remove "AUTHENTICATE" in the LDAP connector, you can no longer use it for pass-through authentication. If you remove PROVISION from the feature string of e.g. Active Directory, it will no longer provision accounts using the connector's features.
If you do not define the feature string, or leave it empty, it could be an issue. Important features will not be available.
The features that can be enabled or disabled depend on the connector.

Typically you would not modify the feature string of an application definition.  When an application is created, the featureString is copied from the prototype application in the connector registry.  You might want to remove features from the string if you wish to limit the capabilities of the connector.  This might be useful if you want to disable provisioning to make sure that provisioning is fulfilled by another path such as IdentityIQ work items.

The feature string in the connector registry should be the list of features that the connector can support, so you should never add to the list for an application instance.  Doing so will likely cause unexpected failures.

FeatureString entirely depends on the type of application object. If you look for the Application.Feature enum within /doc/javadoc, you can see the available featureString.

##### Attributes

**createAccountTimelag**  
Time in seconds to wait after creating an account and before calling get account. Default: 20 seconds,
<entry key=”createAccountTimelag” value=”120”/>

**maxReadTimeout** 
Time in seconds to wait for getting response from the REST call, in the read operation, before the operation gets timed out. Default: 180 seconds. 
```<entry key=”maxReadTimeout” value=”200”/>```

**maxRetryCount** 
Indicates the number of time read op
```<entry key=”maxRetryCount” value=”5”/>```

**retryableErrors** 
The retryableErrors entry is a list of strings through which the connector searches when it receives a message from the managed application. If one of the strings in the entry exists in the error, the connector attempts to retry the connection.
```
<entry key=”retryableErrors”>
    <value>
        <List>
            <String>Server is not operational</String>
            <String>Object Not Found</String>
        </List>
     </value>
</entry>
```

### Delimited File

The SailPoint Delimited File Connector is a read only and rule driven connector. This connector has rules that can be customized to handle the complexity of the data that is being extracted.
This connector can be configured to enable the automatic discovery of schema attributes.


#### JDBC

The SailPoint JDBC Connector is used for Read/Write operations on the data of JDBC- enabled database engines.
This connector supports flat table data. To handle complex, multi-table data, you need to define a rule and a more complex SQL statement.


#### Create DB, User and Table
```
-- Create Database
CREATE DATABASE sweDB;

-- Create Admin User with Full Privileges
-- Create admin user with password
CREATE USER 'adminUser'@'localhost' IDENTIFIED BY '@dminUser!123';

-- Grant read, write, delete (all privileges) on the new database
GRANT ALL PRIVILEGES ON sweDB.* TO 'adminUser'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

-- Notes:
-- Replace 'StrongPassword123!' with a secure password.
-- If you want the admin to connect from anywhere, replace 'localhost' with '%'.


-- Switch to the New Database
USE sweDB;

-- Create User Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- unique row identifier
    username VARCHAR(50) NOT NULL UNIQUE, -- login name
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    employeenumber VARCHAR(20) UNIQUE NOT NULL,
    manager VARCHAR(50), -- could also be a foreign key to another user
    status VARCHAR(50) NOT NULL,
    usertype VARCHAR(50) NOT NULL,
    access VARCHAR(50) NOT NULL
);

INSERT INTO `users` (`username`, `firstname`, `lastname`, `manager`, `employeenumber`, `status`, `usertype`, `access`) VALUES
('testUser10', 'Test', 'User10', '20250001', '20250010', 'ACTIVE', 'User', 'Admin'),
('testUser11', 'Test', 'User11', '20250001', '20250011', 'ACTIVE', 'User', 'Admin');
COMMIT;

```

### LDAP
The LDAP connector was developed using the LDAP RFC. The LDAP Connector must plug into almost any LDAP server with no customization. The LDAP Connector now supports provisioning of users and entitlements along with the retrieval of LDAP account and group object classes.


### Active Directory

### Webservice

# Provisioning
The IdentityIQ provisioning capabilities help companies manage system access for their personnel. Provisioning requests can be created and processed in several ways in IdentityIQ, based on the needs and configuration of the installation. In many cases, modifications to access or entitlements you request in IdentityIQ can be automatically reflected in the associated native applications.  

At a high level, provisioning requests are processed as follows  
**Plan Creation → Broker → Expansion → Role Resolution → Account Creation → Filtering → Pathway → Execution**
```
Provisioning request initiated  
(via user request, role assignment, policy, or lifecycle event)
        ↓
Request created as a Provisioning Plan  
 (contains accounts, entitlements, operations)
        ↓
Provisioning Broker processes the plan  
   - Evaluates requirements  
   - Splits into Partitioned Plans (per application)
        ↓
Expansion phase  
   - Expand plan details  
   - Resolve roles → entitlements  
   - Identify account creation needs
        ↓
Filtering phase  
   - Check existing access  
   - Remove redundant requests
        ↓
Determine provisioning pathway  
   - Direct connector provisioning  
   - Manual fulfillment / workflow  
   - Ticketing system (e.g., ServiceNow)
        ↓
Final provisioning execution  
   - Create/modify accounts  
   - Assign entitlements
```

**ProvisioningPlan**  
ProvisioningPlan is an object which represents the provisioning request. It contains a list of requested changes to an identity    
ProvisioningPlan is one object which contains information about what to be provisioned on the target,on which application.    
Plan contains for which identity and it can hold multiple Account requests. Each account request may contain multiple Attribute requests.  

Basically this defines which Identity the request is created for .. what application account needs to be provisioned in and what all attributes needs to be provisioned.

```
Identity identity = context.getObjectByName(Identity.class, identityName);

ProvisioningPlan plan = new ProvisioningPlan();
// Set identity to the plan
plan.setIdentity(identity);

AccountRequest acctReq = new AccountRequest();
acctReq.setOperation(AccountRequest.Operation.Modify);
acctReq.setNativeIdentity("nativeIdentity");
acctReq.setApplication("Application");
acctReq.add(new AttributeRequest("name", ProvisioningPlan.Operation.Set, "value"));
plan.add(acctReq);
```

Recording Provisioning Requests You can create provisioning requests in IdentityIQ using any of the following actions or activities:  
- Certifications  
- Policy Violations  
- Identity-Refresh-Driven Assignments    
- Lifecycle Manager Requests
- Lifecycle Event-Driven Provisioning 

Provisioning requests create a provisioning plan that the Provision Broker can analyze and process. In all cases, except certification and policy violation-generated requests, provisioning requests create a Workflow case. The Workflow case manages the processing of the provisioning request based on a defined Workflow

## Roles
A role is a collection of entitlement or other roles that enables an identity to access the resources and to perform certain operations within an organization.  

**Type**  
- Organizational Role  
- Business Role  
- IT Role  
- Entitlement Role  

> Custom Role can be created

**Organizational Role**
Designed for organizing the role hierarchy in IIQ UI. Do not provide any function other than creating nesting structures in the role modeler.

**Business Role**
Identifying job functions or titles or other attributes by which users can be grouped together into a Business role
Tagged as Assigned Role

**IT Role**
IT Roles allow multiple entitlements from one or more applications to be grouped together into a single role. IT Roles should encapsulate groups of related entitlements that are shared by one or more business roles

**Level 1: Organization Roles**
- IGA Team
	- maps to Business Roles:
		- IGA Engineer Team
		- IGA Testing Team


- IGA Team IT
	- maps to IT Roles:
		- IGA Engineer Team IT
		- IGA Testing Team IT


**Level 2: Business Roles**
- IGA Engineer Team
	- maps to IT Role:
		- IGA Engineer Team IT


- IGA Testing Team
	- maps to IT Role:
		- IGA Testing Team IT

```
Organization Role: IGA Team
   ├── Business Role: IGA Engineer Team
   │       └── IT Role: IGA Engineer Team IT
   └── Business Role: IGA Testing Team
           └── IT Role: IGA Testing Team IT

Organization Role: IGA Team IT
   ├── IT Role: IGA Engineer Team IT
   └── IT Role: IGA Testing Team IT
```

**Birthright Role**

Business Roles can be leveraged to define birthright access by using the selector option, enabling automated assignment of entitlements based on role-based (RBAC) or attribute-based (ABAC) conditions
```
Organization Role: Birthright Role
   └── Business Role: Employee Birthright
          	   └── IT Role: Employee Birthright IT
```

**Case 1: Using Rule**
```
<Selector>
    <IdentitySelector>
      <RuleRef>
        <Reference class="sailpoint.object.Rule" name="Employee Assignment Rule"/>
      </RuleRef>
    </IdentitySelector>
  </Selector>
```
**Case 2: Using Attribute**
```
<Selector>
    <IdentitySelector>
      <MatchExpression>
        <matchTerm name="employeeType" value=”Employee”/>
      </MatchExpression>
    </IdentitySelector>
  </Selector>
```

# Essentials
## Task
In IdentityIQ, a TaskDefinition represents a background job that runs operations such as importing data, recalculating identities, or executing rules.  

Tasks can be:
- Scheduled (via Task Scheduler)
- Run on demand (via Tasks tab)
- Partitioned across servers for scalability

### Aggregation
Pulls account/entitlement data from connected applications into IIQ.
Updates the Identity Warehouse so IIQ knows what access each user has.

**Attributes**
**Enable Delta Aggregation** - Enable the connector to aggregate only those accounts that have changed since the last aggregation. This requires support by the connector.   
**Detect deleted accounts** - Compare current aggregated accounts with the accounts previously aggregated and report any deleted accounts. Maximum deleted accounts: This is the maximum number of accounts that can be flagged for deletion after an account aggregation. If this number is passed, no accounts are deleted from the application.  
**Promote managed attributes** - When enabled, any values for entitlement or permissions encountered while running  
**Enable Partitioning** - Enable partitioning of this task across multiple hosts.  

### Refresh
Recalculates identity attributes, entitlements, policies, and role assignments.
Often needed after aggregations, role updates, or policy changes.

**Attribute**
**Optional filter** – Limit identities refreshed (e.g., name == "12345").  
**Refresh identity attributes** – Update Identity Cubes with attribute changes.  
**Refresh entitlements** – Refresh all entitlement values for all links (resource-intensive).  
**Refresh manager status** – Update Identity Cubes when manager status changes.  
**Refresh roles & promote** entitlements – Update changed role assignments and promote any new entitlements.  
**Provision assignments** – Provision newly assigned roles and entitlements.  
**Synchronize attributes** – Update identity mapping targets if values have changed.  
**Maintain identity histories** – Snapshot identities with changes since last refresh.  
**Process events** – Trigger lifecycle events.

### Perform Maintenance
Handles housekeeping operations to keep IIQ clean and performant. Examples include:
- Purging old task results
- Cleaning completed/expired work items
- Trimming logs and audit data
- Clearing temporary objects

### Rule Execution
Executes a specific SailPoint Rule as a background task.  
Useful for running custom logic (data fixes, bulk updates, reporting).

## Debug Page
The Debug Page is an internal IdentityIQ tool used by administrators and developers to view, test, and troubleshoot configuration objects and system behavior.

Purpose of the Debug Page  
- Inspect and manage IdentityIQ objects (e.g., Identities, Roles, Tasks, Applications).
- Run and test rules, workflows, and forms.
- View log messages and troubleshoot provisioning or aggregation issues.
- Perform quick searches and queries against the IIQ database.

Accessing the Debug Page
Typically accessed via URL:
```
http://<IIQ-Server>/identityiq/debug/debug.jsp
(Requires admin or sufficient capability permissions.)
```

**Common Uses**
- Search Objects → Quickly look up an identity, role, entitlement, or task.
- Run Rules → Test rule logic directly from the UI.
- About Page → Get application details.
- Reload Logger → Reload log files forcefully.


> [!NOTE]
> The debug page is a powerful admin tool and should be restricted to trusted users. Changes made through debug may directly impact system behavior.
> Never ever delete objects through Debug page

## Console
The IIQ Console is a command-line interface (CLI) provided with SailPoint IdentityIQ that allows administrators to interact directly with the application. It’s mainly used for administration, troubleshooting, and executing tasks that are not always possible (or convenient) through the web UI.
```
<Installation Path>\WEB-INF\bin > .\iiq console -j
```
**Common Commands** 
 
**export** - Export objects to a file  
**rule** - Run a rule  
**notify** - Send an email  
**connectorDebug** - Call one of the exposed connector methods using the specified application 
**encrypt** - Encrypt a string.  
**source** - runs commands from a script file.  
**delete** - Delete an object  

```
Redirecting
get identity Rahul.Dravid > c:\output\rahulDravid.xml
```

## Group
Sets of identities created automatically based on the values of a single identity attribute
Used to filter identities included in a task, certification, or report

## Workgroup
A Workgroup is a collection of IIQ identities (users) grouped for workflow or governance purposes.

Commonly used in approval processes, certifications, and policy violations.

Example: An "Access Reviewers Workgroup" where all managers responsible for access certifications are included.

👉 Think: Who should approve or review something? → Put them in a Workgroup.

## Population
Populations are sets of Identities generated from queries on the Advanced Analytics page and can be based on multiple criteria, such as North America, non-manager, accounting department employees. 

Any Identity Attribute marked as Searchable can be used as a Population criterion. The result set for the query (the Population) is a single set of Identities who share a common set of properties.

## Capabilities
A Capabilities is an administrative privilege inside IIQ that controls what a user can do within the IIQ application.

Defines system-level access rights (not target system access).

## Access Request
Access Request is the process by which a user (or their manager, or an admin) requests additional access — such as roles, entitlements, or applications — for themselves or for another user. It’s a core part of Identity Governance and Administration (IGA), enabling secure provisioning with proper approvals and policy enforcement.
Here’s a breakdown of how Access Requests work in IIQ:

Ways to Request Access
```
IdentityIQ Home Page → Request Access
Users can request access for themselves or others (if they have the delegation/manager rights).

Identity Search → Request Access
Search for an identity and request access for that identity.

Access Request Module (Shopping Cart Style)
Add roles, or entitlements to a cart-like interface and submit the request.
```
Manage User Access can be restricted to allow only specific items to be requested(Add/Remove).

Access Request Workflow

**User Submits Request**
Selects items (role/entitlement/application) → Adds justification → Submits.

**Policy & SOD (Separation of Duties) Check**
IIQ checks for policy violations (e.g., conflicts with existing access).
If conflicts exist, the request may be blocked or routed for exception approval.

**Approval Workflow**
Routed to managers, role/application owners, or custom-defined approvers.
Multi-level approvals can be configured.

**Provisioning**
If approved, IIQ sends provisioning instructions to target applications via connectors (direct or through an external ticketing system like ServiceNow).

**Completion & Notification**
Users and approvers are notified when access is granted or denied.

## Workitem
To assign tasks and approvals generated by workflows (e.g., approving access requests, certifying access, or resolving policy violations).

Whenever IdentityIQ generates an approval, remediation, or manual action item. Workitems appear in a user’s “My Work” inbox, where they can review, approve/reject, or complete the task.

### WorkitemArchieve 
To keep track of completed or expired workitems for audit and compliance.
After a workitem is finished (approved/rejected/closed), it is moved into the archive.
Users and auditors can review archived workitems for evidence of decision-making and compliance reporting.

General Flow
```
Manage User Access (user submits Role/Entitlement request) →
Access Request (IIQ creates AR) → 
Workitem (approval/review task generated) → 
Manage User Access (grants/revokes applied to identity) → 
Workitem Archive (completed record kept for audit)
```

## Email Template
The Email Template XML consists of an element with a set of attributes and nested elements that specify the basic components of an email message, such as sender, subject, message body, etc.

**Apache Velocity**  
IdentityIQ email templates are processed through an open-source engine called Apache Velocity. Velocity is a Java-based template engine that allows web page designers to reference methods defined in Java code. IdentityIQ email templates make use of the Velocity Template Language to dynamically specify the email messages' contents and generate custom email messages specific to the recipient, work item, and action involved. 

## Identity Warehouse

## Advance Analytics

# Policy
A Policy Violation occurs in IIQ when a user’s access breaks a defined compliance or security rule.
Policies in IIQ are created to enforce business rules, security standards, and regulatory requirements (e.g., SOX, GDPR).

# Report
Reports in IdentityIQ provide visibility into identities, accounts, entitlements, policy violations, certifications, and system activities.   

They are essential for audit, compliance, and operational monitoring.
- Customizable: Can be filtered by identity attributes, entitlements, or dates.
- Exportable: Results can be exported in formats like CSV, PDF, or Excel.
- Schedulable: Reports can run on-demand or be scheduled at regular intervals.
- Secure: Access to reports is controlled via Capabilities (only authorized users can run sensitive reports).

Typical Use Cases  
- Audit Evidence: Prove compliance with SOX, GDPR, HIPAA, etc.
- Access Monitoring: Track privileged accounts or orphan accounts.
- Policy Enforcement: Monitor violations and remediation progress.
- Operationl Insights: Analyze system performance and provisioning outcomes.

TaskDefinition  
- Attribute
	- Live Report
		- DataSource
			- Filter
				- QueryParameters
					- Report Argument
					- Default Value
					- Hard-coded value
					- Different Operations
					- ValueScript/ValuerRule
					- QueryScrip/QueryRule
				- Join
				- Query
				- OptionsRule/OptionsScript
			- Java
				- Datasource
				- objectType
				- Type
				- defaultSort
			- HQL
		- Columns
		- Report Form
			- Custom
			- Standard Properties
			- Report Layout
			- ValidationScript/ValidationRule
		- Report Summary
			- DatasourceScript/DatasourceRule
		- Chart

# Certification
Certification in IdentityIQ is a governance process that ensures users have only the access they need. It provides a structured way to review and validate entitlements, roles, and account access for compliance and security.

**Phase**
```
Initiation (Generate Certification) → 
Staging (Optional, if selected administrator can examine certification before notification is triggered and activated) → 
Notify (Notify certifiers) → 
Active (certifiers need to take action) → 
Challenge (dispute the revocation) → 
Remediation (Revocation of items) → 
SignOff
```

# Quicklink
Quicklinks in IdentityIQ are shortcut links displayed on the IdentityIQ dashboard (home page).
They provide easy access to common actions and requests, improving user experience.

**Purpose of Quicklinks**
- Simplify navigation for end users.
- Allow one-click access to frequently used features.

# Form
A Form in IdentityIQ is a configurable user interface component that collects input from users during workflows, tasks, requests, or approvals.
Forms make interactions in IIQ dynamic, user-friendly, and customizable.  

**Purpose of Forms**  
- Capture user input during Access Requests, Certifications, Approvals, or Workflows.
- Provide customized UI fields instead of hard-coded values.
- Ensure business processes collect the right data for provisioning or governance.

**Where Forms Are Used**  
- Access Requests → To request roles, entitlements, or applications.
- Approvals → Manager or application owner approvals with comments.
- Identity Management → Collect information during onboarding or updates.
- Custom Workflows → Gather parameters before task execution.

**Key Features**  
- Types of Fields: Text boxes, dropdowns, checkboxes, date pickers, etc.
- Dynamic Behavior: Fields can be shown/hidden based on conditions.
- Validation: Input can be validated before submission.
- Reusability: Forms can be reused across workflows.

```
Form
 ├── Attributes
 │     (Map of name/value pairs that influence the form renderer)
 │
 ├── Button
 │     (Defines form processing actions)
 │
 └── Section
       (Subdivision of the form; can contain nested Sections and Fields)
        └── Field
             (Can include:)
               ├── Attributes map
               ├── Script to set value
               ├── Allowed Values Definition script
               └── Validation Script
```

# Workflow
In IIQ, a workflow (a “Business Process”) is the engine that orchestrates approvals, provisioning, notifications, branching logic, and custom scripts for things like access requests, joiner/mover/leaver events, identity updates, policy violations, etc. Every workflow has a type (e.g., LCM Provisioning, Identity Update, Identity Lifecycle, Role Modeler) which controls where and how it can be triggered in the product. Choosing the correct type is crucial because IIQ only offers workflows of the matching type in the configuration screens that wire them to system events. 

Typical places you’ll see workflows fire:  
**Lifecycle Manager (LCM):** create/update, registration, manage passwords, provisioning.   
**Lifecycle Events:** Joiner, Leaver, Manager Change, Reinstate.  
Identity Update / Identity Refresh, Role actions, Policy violations, Scheduled role activation. 

**Ways to create (or modify) workflows**  
- Business Process Editor (UI) – the built-in designer to create/edit workflows, define steps and transitions, design forms, and set inputs/variables. You’ll usually sketch the flow here, then fine-tune in XML for advanced logic.  
- Copy an existing/default workflow – clone an out-of-the-box workflow in the editor and tweak it (helpful for LCM or Lifecycle variants).  
- Author/edit the XML directly – open a workflow in Debug pages to view/edit XML or export it to version control, then re-import. Forms can be embedded in a workflow or created as separate Form objects.    
- Import via UI or IIQ Console – use Global Settings → Import from File or the iiq console import command to bring in workflow XML (also use console to validate/run tests).  

**Important Workflow Objects**

The IdentityIQ Object Model uses four key objects in workflows. To work with workflows, you need a basic understanding of these objects.  
**Workflow** - Defines the workflow structure and steps involved in the workflow processing  
**WorkflowCase** - Represents a workflow in progress. Contains a workflow element in which the process is outlined and current state data is tracked. Contains identifying information about the workflow target object.  
**WorkflowContext** - Tracks launchtime information the Workflower maintains as it advances through a workflow case. Passed into rules and scripts and to the registered WorkflowHandler. Contains all workflow variables, step arguments, current step or approval, workflow definition, libraries, and WorkflowCase.   
**TaskResult** - Records the completion status of a task, or in this case, the workflow. Contained within the WorkflowCase. 

**Key concepts you’ll use a lot**
- Workflow Type – determines where/how a workflow can be attached and what libraries/features it sees. Pick correctly or it won’t appear in config dropdowns. 
- Libraries – expose helper Java methods inside a workflow (e.g., Identity, LCM). If omitted, core libraries are available by default. 
- Steps – the nodes (Approval, Provisioning, Script/Action, Notification, Subprocess, Fork/Join, Loop…). Not all specialized options are available in the GUI; sometimes you must edit XML.  
- Transitions – edges with conditions (string expressions or rules). Handle success, reject, and exception paths. 
- Forms – UI screens embedded in the workflow XML or defined as independent Form objects for reuse.  
- Subprocesses – split large workflows into reusable sub-workflows (commonly marked type Subprocess).  
- Transient workflows – special mode (no DB persistence until there’s an approval or wait/background step), useful for quick, form-only flows (e.g., self-registration).  
- Testing & validation – validate, workflow, wftest in the IIQ Console. 

## Workflow Variables
Workflow variables control approvals, provisioning, notifications, and policy handling in request workflows.

**Flow Control Variables**  
- approvalSplitPoint → Splits approvals/provisioning so each entitlement is processed independently.
- optimisticProvisioning → Treats queued provisioning as successful (used in demos/ticket-based systems).
- foregroundProvisioning → Runs provisioning in foreground; used mainly for dev/test/demo.
- doRefresh → Forces identity refresh after request completion.
- identityName → Stores the target identity’s name for the request.


**Policy Checking Variables**  
- policyScheme → Defines action if violations are found (none, continue, interactive, fail).
- allowRequestsWithViolations → Lets requesters proceed without fixing violations (only in interactive mode).
- violationReviewDecision → Captures requester’s decision (ignore, remediate, cancel).
- policyViolations → List of violations found, passed into workitems for visibility.

**Approval Control Variables**
- approvalMode → Defines approval flow (serial, parallel, poll, any).
- approvalScheme → Defines who approves (manager, owner, security officer, identity, none).
- fallbackApprover → Backup approver if no valid approver is found.
- approvalSet → Groups approval items for processing.

**Provisioning Control Variables**  
- Plan → Holds provisioning plan generated for the request.
- Flow → holds execution flow name.

**Notification Variables**  
- notificationScheme → Who gets notified after request completes (user, requester, manager, security officer, none).

**Other Workflow Variables**  
- Trace → Enables workflow trace logging (useful in debugging).
- Source → Identifies origin of request (UI, API, etc.).
- Project → Stores project metadata linked to the request.
- identityRequestId → Unique ID of the identity request object.
- EmailTemplates → Defines email templates used for notifications.

## Lifecycle Event
Lifecycle Events (LCEs) in IdentityIQ represent key identity-related activities that occur during an employee’s journey in an organization.
They help automate provisioning, de-provisioning, and access adjustments based on employment changes.

**Common Use Cases**  
**Joiner (New Hire)**  
- Provision accounts and assign birthright access automatically.
- Trigger onboarding workflows (e.g., request manager approvals, send welcome notifications).

**Mover (Change in Role/Department/Manager)**  
- Recalculate business roles and entitlements.
- Revoke access is no longer required in the new role.
- Grant new access based on updated department, title, or manager.

**Leaver (Termination/Exit)**  
- Automatically disable or delete user accounts across systems.
- Revoke all entitlements and roles.
- Trigger offboarding workflows (e.g., notify HR, recover assets).

# Multithreading and Partitioning
When IdentityIQ is installed, certain objects are defined that control how partitioned tasks are processed. These include:
- Server
- ServiceDefinition
- RequestDefinition


These objects define things like:
- How many threads each server can run
- Limits on threads per host for specific tasks
- Which servers handle partitioned requests

## Server
IdentityIQ automatically creates a Server object for each host connected to the database.
You can set **maxRequestThreads** in the Server object’s attributes map.
This controls the maximum number of request-processing threads allowed on that host.
It overrides any maxThreads values set in the RequestDefinition.

## ServiceDefinition
By default, every IdentityIQ host runs in UI, task, and request roles at the same time.  
**Task** and **Request** ServiceDefinition objects let you control which servers are used for tasks and requests.  
Partitioned processes are handled by creating Request objects, which run on request servers.
The hosts attribute in the Request ServiceDefinition specifies which servers should handle partitioned activities.

## RequestDefinition
These objects define how requests in the queue are processed.  
Not all RequestDefinitions affect partitioning — only a few key ones:
- Aggregate Partition
- Identity Refresh Partition
- Manager Certification Generation Partition
- Certification Builder
- Role Propagation Partition

Each RequestDefinition sets a **maxThreads** attribute, which specifies how many threads per server can be used for that request type.

# Password Interception
Password Interception allows IdentityIQ to capture and synchronize password changes across connected systems without directly storing passwords in IIQ.  

Key Points:
- Intercepts password changes from users or applications.
- Ensures password updates are propagated to target systems securely.
- Commonly used with Active Directory, LDAP, or other target systems where direct provisioning may not capture password changes.

# IQ Service
The IQService is a native Windows service that enables IdentityIQ to participate in a Windows environment and access information only available through Windows APIs.

Connectors
Active Directory
Azure Active Directory
HCL  Domino
Microsoft SharePoint Server
Microsoft Windows Local - Direct

# Connector Gateway
Connector Gateway communicates between IdentityIQ/IdentityNow Provisioning Engine and the Mainframe Connector on z/OS.

The Provisioning Engine sends multiple requests (T1, T2, …) over dedicated TCP/IP channels to the Connector Gateway.
Connector Gateway forwards requests sequentially to the Mainframe Connector, maintaining persistent socket channels secured via DES, 3DES, or TLS.
Mainframe Connector executes requests (aggregation, provisioning, role/entitlement updates, password management) and sends responses back to update IdentityIQ/IdentityNow; intercept functionality is supported only in IdentityIQ.


## Business Processes (Workflows)

## IIQ Console

## Database

## Groups

## Roles

## [Reports](https://youtu.be/CJQJPFwoRHU?si=MsCXfO_eqbeF9RFi)

## Policy

## [Workitem](https://youtu.be/q2U2b0o0b1E?si=NPX91elAzhBjJlWA)

## [Certifications](https://youtu.be/WUZTVFwDA_k?si=I_2T9Oi2Xfqk5Uk2)

## [QuickLink](https://youtu.be/98jTMXuHlu0?si=mufI3zmCauRq1e3U)

## Services Standard Deployment

## [IdentityIQ REST API Integration](https://youtu.be/aJwBlzkN-Sw?si=7Cg4XAQyhyMTtW5f)

## Plugins

## [Account Correlation](https://youtu.be/p4HzMUgPRcs?si=aIC-ax9q2XNRGZQk)

## BeanShell

