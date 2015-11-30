# Register users
you can register users into two ways
1) adding it in the uaa.yml file

scim:  
  users: 
	-phr|phr|bob@phr.com|Bob|Lastname|phr.hie.ReadDocument

2) using uaac command.

you need to set your target UAA first  
-uaac target http://localhost:8080/uaa (Local UAA)
or 
-uaac target http://bhitsqaapp02:80/uaa (for QA UAA)

   1)	Then log in as admin  client  
-uaac token client get admin -s adminsecret –t

   2)	Create a user

	-uaac user add bob -p bobpw --emails bob@bob.com

if you want to add a user you just created to a specific group, continue with step 3 and 4
   3)	Create a group (example : phr.hie.readDocument) (if it exists already you will get an error, then go to step 5) 
 	
	-uaac group add phr.hie.readDocument
   4)	Add the user you created to this group (meaning giving a role/scope)  

	-uaac member add phr.hie.readDocument bob
