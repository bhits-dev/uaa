# Register clients
you can register clients into two ways
1) adding it in the uaa.yml file

		oauth:  
	  	 clients:  
		  patient-portal-ui:  
		      secret: secret  
		      authorized-grant-types: authorization_code  
		      scope: openid,phr.hie.readDocument  
		      authorities: uaa.resource  
		      redirect-uri: http://localhost:8081/pp-ui/fe/login   
	
2) using uaac command.

you need to set your target UAA first  

`-uaac target http://localhost:8080/uaa (Local UAA)`
or 

`-uaac target http://bhitsqaapp02:80/uaa (for QA UAA)`

then run 

`-uaac client add customer-portal --authorized_grant_types authorization_code  --scope openid,phr.hie.readDocument --authorities uaa.resource --secret secret --redirect-uri http://localhost:8081/pp-ui/fe/login`