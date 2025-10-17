# Active Directory

Active Directory (AD) is a directory service developed by Microsoft for managing and organizing network resources, such as users, computers, and other devices in a network.  

The history of Active Directory can be traced back to the early days of Microsoft's networking efforts. In the late 1980s and early 1990s, Microsoft began developing its own networking protocols, such as NetBIOS and SMB (Server Message Block). These protocols allowed Windows computers to communicate with each other over a network.  

With the release of Windows NT in 1993, Microsoft introduced its first directory service called NT Domain Services. This service provided basic user authentication and resource management capabilities but was limited in scalability and functionality.
In response to this limitation, Microsoft developed Active Directory for use with Windows 2000. Active Directory introduced many new features such as hierarchical organization of resources, support for multiple domains, and improved security mechanisms.  

Today, Active Directory is widely used by organizations of all sizes to manage their networks. Its purpose is to provide a single point of control for managing users, computers, groups, and other resources on a network. With Active Directory, administrators can easily create and manage user accounts, assign permissions to resources, configure group policies, and much more.  

## Components of Active Directory  
Active Directory is a directory service developed by Microsoft that provides centralized authentication and authorization services for Windows-based computers. It consists of several components that work together to provide a comprehensive directory service for managing users, computers, and other resources in an organization.
  
### Domain Services
The core service of Active Directory that stores directory data (like user accounts, groups, and resources) and makes it accessible to users and devices within the domain. It uses a hierarchical structure that includes domains, trees, and forests.  

It is responsible for managing the authentication and authorization of users and computers in a network.  

### Domain Controllers
A Domain Controller is a server that runs the Active Directory Domain Services role. It stores user account information, manages authentication and authorization requests, and replicates changes to other domain controllers in the same domain. A domain can have multiple domain controllers to provide redundancy and fault tolerance.

#### Primary Domain Controller  and Backup Domain Controller are types of Domain Controllers:
**Primary Domain Controller (PDC):** Manages most authentication requests.
**Backup Domain Controller (BDC):** Provides redundancy and backup in case of failure.


### Domains
A domain is a core component that acts as a logical grouping of network resources (such as users, computers, printers, and other devices). Domains provide a framework for organizing and managing resources, security policies, and authentication within an Active Directory environment.  

A domain is a boundary within which certain Active Directory resources are managed and accessed. It is defined by a unique Domain Name System (DNS) name, such as example.com or corp.example.com.  

### Trusts
Trusts are relationships between domains that allow users in one domain to access resources in another domain. They enable cross-domain authentication and resource sharing while maintaining security boundaries between domains. 

There are different types of trusts, including one-way trusts and two-way trusts.

### Identity Services
Identity Services are responsible for managing user identities within Active Directory. This includes Users and Groups, Authentication and Authorization, Certificates.

### Users and Groups
Users are Individuals or machines that have access to resources in the domain.  
User objects store information like passwords, group memberships, and security permissions.  

Groups are Collections of users for simplified management.  
Security groups (for access control) and Distribution groups (for email distribution) are types of groups.  

They simplify administration by allowing administrators to manage permissions at the group level rather than at the individual user level.  

### Authentication and Authorization
Authentication is the process of verifying the identity of a user or computer attempting to access network resources. Authorization is the process of determining whether a user or computer has permission to access specific resources based on their authenticated identity.   

AD helps authenticate users, ensuring that only authorized individuals can access network resources. It also manages the permissions and access control for different users and groups.  

Active Directory supports various authentication protocols such as  
- Kerberos Authentication:
	- The default authentication protocol in AD, allowing secure login and communication.
- NTLM (NT LAN Manager):
	- A legacy authentication protocol used in some older systems.
- Multi-factor Authentication (MFA):
	- Enhances security by requiring multiple forms of identity verification.

### Certificates
Certificates are used to secure communication between servers or clients using encryption techniques like SSL/TLS (Secure Sockets Layer/Transport Layer Security).   
Active Directory Certificate Services (AD CS) provides certificate management services for issuing certificates within an organization's public key infrastructure (PKI).  

#### Directory Services
Directory Services provide information about network resources stored in Active Directory through various protocols like LDAP (Lightweight Directory Access Protocol).   

The main components include Schema, Global Catalog, LDAP.

### Schema
The schema defines the structure of objects stored in Active Directory such as User objects or Computer objects. It specifies what attributes an object can have along with their data types which help maintain consistency across all objects stored in the active directory.

### Global Catalog
The Global Catalog contains information about all objects from every domain within a forest making it easier to locate objects from any part of the forest without needing specific knowledge about where they reside.

### LDAP
LDAP (Lightweight Directory Access Protocol) is used by applications such as email clients or web browsers to search for information stored in Active Directory through queries sent over the network.

### Group Policy
This feature enables administrators to manage settings and configurations for all computers and users in an AD domain. 

For example, 
- To set security policies (password complexity, user permissions).
- Control desktop environments (start menu settings, control panel visibility across the network.).
- Deploy software remotely.

## AD Structure
### Domain
The primary unit within AD, containing user accounts, computers, and other resources.

### Trees and Forests:
A tree is a collection of domains sharing a common schema and namespace.

A forest is a collection of trees, providing a broader, more comprehensive directory structure.

### Organizational Units (OUs)
Containers for grouping objects, which help in delegation of administrative tasks.

### Global Catalog
A searchable index of all objects in the directory, enabling fast lookups across the forest.

## Active Directory Replication
Replication is the process through which domain controllers share and synchronize changes to the AD database.

Types of Replication:
- Intra-Site Replication: Occurs within a single site for faster synchronization.
- Inter-Site Replication: Occurs between different sites, typically over slower connections

### Replication Topology
Active Directory uses a multi-master replication model, where any domain controller can make changes to the AD database.
