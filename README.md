# SDU Portal Connector
[![](https://jitpack.io/v/zsanzharko/sdu-edu-portal-connector.svg)](https://jitpack.io/#zsanzharko/sdu-edu-portal-connector)
## Introduction
I wrote a connector to be able to get data programmatically. This way it 
will be possible to write various microservices to automate student life.
The connector provides data that is also used by the main student portal site.

## Getting Started

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
    }
  }
```

### Get DTO class (Academic) from portal

``` java
  void getDtoMyAcademic() throws IOException, PortalException {
    PortalRequestAPIConnector service = new PortalRequestAPIConnector();
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
                "student_id", new char[] {'p','a','s','s','w','o','r','d'});

    StudentAcademic studentAcademic = portalParserService.parseResponse(
            studentId, response.getData(), new AcademicParserServiceImpl()
    );
    
    System.out.println("getDtoMyAcademic");
    System.out.println(studentAcademic);
  }
```
#### P.S. when you will want to get DTO class, watch what u write in method `portalParserService.parseResponse`
Actually now I realized one to one parser. When I want to get StudentAcademic DTO I need 
to use new AcademicParserServiceImpl()

For other DTO classes have other **parser services**. Note it.