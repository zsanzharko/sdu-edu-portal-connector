# SDU Portal Connector
[![](https://jitpack.io/v/zsanzharko/sdu-edu-portal-connector.svg)](https://jitpack.io/#zsanzharko/sdu-edu-portal-connector)
## Introduction
I wrote a connector to be able to get data programmatically. This way it 
will be possible to write various microservices to automate student life.
The connector provides data that is also used by the main student portal site.

## Getting Started

### Gradle use
```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    
    dependencies {
        implementation 'com.github.zsanzharko:sdu-edu-portal-connector:1.2'
    }
```

### HTTPS connection PXI problem

After importing dependency, you need to import certificate from main website to Java Compiler.
Step-by-step: 
1. Go to the [SDU website](https://my.sdu.edu.kz/), and [download certificate from browser](https://superuser.com/questions/1291416/download-pem-via-browser-only).
2. Open Command prompt and check your java compiler.
3. Add certificate to cacerts in JDK compiler and JDK.
   - Example `keytool -import -trustcacerts -alias sdu-portal -file C:\<PATH>\_.sdu.edu.kz.cer -cacerts`
   - See more [here](https://www.ibm.com/docs/en/tnpm/1.4.2?topic=security-import-certificate-jre-keystore)

`PortalRequestAPIConnector.class` is the main class for connect to portal with user credentials.

### Authentication
The `AuthorizeStudentCredential.class` is used to connect a student to the portal.
``` java
    public void auth() throws IOException {
        PortalRequestAPIConnector service = new PortalRequestAPIConnector();
        AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
                "student_id", new char[] {'p','a','s','s','w','o','r','d'});
        boolean clientAuthorized = service.authorize(authorizeStudentCredential);
        service.close();
    }
```

### Get Sample Map Data (Academic) from portal

``` java
  void pingMyAcademic() throws IOException, PortalBadAuthorizationException {
    PortalRequestAPIConnector service = new PortalRequestAPIConnector();
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
                "student_id", new char[] {'p','a','s','s','w','o','r','d'});
    if (service.authorize(authorizeStudentCredential)) {
        PortalResponse response = service.getMyAcademicResponse();
        System.out.println("pingMyAcademic");
        System.out.println(service.getMapper().writeValueAsString(response.getData()));
        service.close();
    }
  }
```

### Get DTO class (Academic) from portal

``` java
  void getDtoMyAcademic() throws IOException, PortalException {
    PortalRequestAPIConnector service = new PortalRequestAPIConnector();
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
                "student_id", new char[] {'p','a','s','s','w','o','r','d'});

    StudentAcademic studentAcademic = PortalParserService.parseResponse(
            studentId, response.getData(), new AcademicParserServiceImpl()
    );
    service.close();
    
    System.out.println("getDtoMyAcademic");
    System.out.println(studentAcademic);
  }
```
#### P.S. when you will want to get DTO class, watch what u write in method `portalParserService.parseResponse`
Actually now I realized one to one parser. When I want to get StudentAcademic DTO I need 
to use new AcademicParserServiceImpl()

For other DTO classes have other **parser services**. Note it.
