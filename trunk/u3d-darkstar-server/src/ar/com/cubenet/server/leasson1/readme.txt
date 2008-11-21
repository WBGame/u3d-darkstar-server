Descripción de la funcionalidad implementada en leasson1
--------------------------------------------------------

Basicamente este ejemplo muestra como los usuarios se conectan al servidor via 
un metodo cliente/servidor.

Por el momento el login siempre es true por lo tanto cualquier usuario puede 
acceder al sistema, pero posteriormente implementaremos un ejemplo en donde el
usuario tenga una contraseña para restringuir el acceso.

El unico Managed Object que se maneja es el del cliente y no hay un ambiente 
donde se ubica ni tampoco se hace uso del Object Store para recuperar datos 
existentes de logins anteriores del cliente.

Principalmente debe entenderse de este ejemplo como es que funciona el login del
usuario y a partir de ahi como es que el cliente se comunica con el servidor.

Como cliente utilizen el proyecto HelloUserClient que existe en los tutoriales 
del cliente que para estoy sirve.
 