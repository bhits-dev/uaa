# Defining a Scope


-Scope values in OAuth2 are arbitrary strings, but in the UAA they normally have a resource id and some level of access (e.g., “read”, “write” and “admin”) separated by a period.

- The uaa auto populates the "audience" field of the token with the scope names ( sub-string until last index of (dot) ) that you provided in the client registration of the UAA.yml class,  and with client Id name.

- If the token "audience" field doesn't contain the resource id , the resource server will deny access to a resource.


