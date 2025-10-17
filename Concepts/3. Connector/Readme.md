# Connectors
There are several different types of connectors. Connectors are commonly grouped by the ways in which they can communicate
There are: 
- read-only connectors that can only communicate data from an external application (Governance)
- read-write connectors that can read data from external applications and write data out to them (Gateway and Direct)  

Connectors may be added, removed, or modified in any release, including patch releases. Existing defined applications will continue to use the connector specified during their initial creation, and changes to the connector will not affect existing applications unless those changes are manually applied to the application definition 

However, the **ConnectorRegistry** entry for the connectors does change with new releases. The list of available connectors, with their current set of available features, can be retrieved from the Connector Registry within the Debug Pages.

## Important Terminologies
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

## Preliminaries
**Decide scope & goals:** business owner, application owner, what access must be governed (users/accounts/roles/entitlements), compliance requirements (SOX, ISO, etc.).  
**Identify app type:** SaaS vs on-prem vs database vs directory vs custom (API/REST) — this determines connector choice.  
**Collect application details (minimum):** base URLs / endpoints, protocol (LDAP/AD/SCIM/SAML/REST/JDBC), admin/service account credentials (with required privileges), sample account(s), list of entitlements/roles, account/schema attributes.   

## Configuration
```
Setup → Applications / Application Definition →
Add New Application → provide name, owner, connector type →
select authoritative/Native Change (if required). 
```

## Correlation
Configure correlation rules that determine how an application account/manager links to an identity in SailPoint (primary key mapping: email, employeeID, uid). Test correlation with sample accounts to ensure correct linking and avoid duplicate identity creation. 

## Schema
After configuring the connector, add the necessary **Account** and **Group** attributes. Most connectors provide a **Discover Schema** option  - ensure you select the correct attribute types.

Attributes without the managed or entitlement mark are just (accounts) attributes. Actually, if you mark it as "Managed", even if you didn't mark it as "Entitlement", it will appear in the Entitlement Catalog.    
**Entitlement:** adds to identity cube as an entitlement that can be certified; also makes it available for use in role mining activities. Attributes marked as "Entitlement" can be certified in a certification. They could be used in IT roles, but not so easily.  
**Managed:** adds to entitlement catalog where it can be assigned an owner, a display name, and a description, and where it can be marked as "requestable" for LCM and can be used in policy definitions

##  Provisioning Policy
> Account - Create\Update\Delete  
> Group - Create\Update

### Why Provisioning Policy?
When a user requests entitlement or group access, and no account exists on the target system, IdentityIQ creates a new account. During this process, IIQ fills in the required account attributes based on the provisioning policies defined for that application.
```
User requests entitlement/group access → IIQ checks if account exists on target 
→ If account does NOT exist
→ Create Account →  IIQ uses Provisioning Policy → Populate required account attributes
→ Submit account creation request to target
```

## Application related Rules 
- Connector Rules
- Aggregation Rules
- Provisioning Rules
- Connector Specific Rules

### Application Rule execution

     Connector Rule executes 
        ↓
     Customization Rule modify attributes if required 
        ↓
     Correlation Rule tries to match account → Identity
        ↓
     Match Found → Link account to Identity
        ↓
     No Match Found → Run Creation Rule → Create new Identity  

## Aggregation
Run account/group aggregation (full import) to pull accounts/groups and attributes into SailPoint

### Other Concepts

#### FeatureString

The feature string determines which features of the application are enabled or disabled. For example if you remove "AUTHENTICATE" in the LDAP connector, you can no longer use it for pass-through authentication. If you remove PROVISION from the feature string of e.g. Active Directory, it will no longer provision accounts using the connector's features.
If you do not define the feature string, or leave it empty, it could be an issue. Important features will not be available.
The features that can be enabled or disabled depend on the connector.

Typically you would not modify the feature string of an application definition.  When an application is created, the featureString is copied from the prototype application in the connector registry.  You might want to remove features from the string if you wish to limit the capabilities of the connector.  This might be useful if you want to disable provisioning to make sure that provisioning is fulfilled by another path such as IdentityIQ work items.

The feature string in the connector registry should be the list of features that the connector can support, so you should never add to the list for an application instance.  Doing so will likely cause unexpected failures.

FeatureString entirely depends on the type of application object. If you look for the Application.Feature enum within /doc/javadoc, you can see the available featureString.

#### Attributes

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

## Delimited File

The SailPoint Delimited File Connector is a read only and rule driven connector. This connector has rules that can be customized to handle the complexity of the data that is being extracted.
This connector can be configured to enable the automatic discovery of schema attributes.


### JDBC

The SailPoint JDBC Connector is used for Read/Write operations on the data of JDBC- enabled database engines.
This connector supports flat table data. To handle complex, multi-table data, you need to define a rule and a more complex SQL statement.


### Create DB, User and Table
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

## LDAP
The LDAP connector was developed using the LDAP RFC. The LDAP Connector must plug into almost any LDAP server with no customization. The LDAP Connector now supports provisioning of users and entitlements along with the retrieval of LDAP account and group object classes.

## Active Directory

## Webservice


