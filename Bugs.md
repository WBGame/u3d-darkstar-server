# Introduction #

Esta página registra los bugs que envió facu por mail. Cada integrante puede hacer los cambios necesarios.


# Details #

## AppListener ##
### Question: ###
¿Que pasa si se cae el server con ServerMsgProcessor porque no es managed object?

Ver que esto se inicializa en el metodo initialize y listo. Con lo cual esto se pierde tras una caida del servidor.
### Answer: ###
El problema que ocurre es que los tipos de mensajes correspondiente al servidor, se agregan en el metodo Inicialize y si se cae el mismo estos tipo de de mensajes no se vuelven a cargar

### Solution proposed: ###
DESCRIPTION: Lo mas probable es que vamos a necesitar que sea managed object.

Si lo que vos decis facu es verdad, pero esta clase no almacena los tipos de mensajes es solo un clase que abstrae todo lo correspondiente a los procesadores del servidor. Lo que tendria que ser managed object es MessageFactory debido a que en esta clase si es la que almacena los tipos de mensajes por lo tanto guardar el estado de dicha clase sea una solucion. Lo que habria que hacer es algo parecido a lo que hice con GridManager el cual almacena los mundos y ante una caida se recuperan los mismos. Esta solucion no se si es la apropiada, porque la clase MessageFactory la utiliza el cliente tambien y si almacenamos esta clase en la DB, el cliente haria lo mismo lo que se generará un error porque el cliente no tiene conocimiento de la DB.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo Inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo Inchausti:     [Una posible solucion es charlar con la gente del common acerca de este problema y ver de alguna manera como ellos pueden cargar los tipos de mensajes del servidor (por ejemplo cuando hacemos MessageFactory.getInstance() si no existe dicha instancia que se cargen los tipos de mensajes del servidor. Creo que esto seria lo mas correcto pero esta desicion queda en ustedes. ](.md)
  * Roberto Kopp:        [comment ](.md)

## ChannelMessageListener ##
### Question: ###
Si observan detenidamente el receivedMessage es casi igual que el programado para UserSessionListener.

Lo unico que cambia es que en este caso usamos el ClientSession para recuperar el player.
### Answer: ###
(completar por el que realizo el cambio o implementación del metodo)

### Solution proposed: ###
Abstraer este codigo a un solo lugar y extender o reutilizar el mismo porque esta mal que este 2 veces.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo Inchausti:     [Lo que se podria hacer que la clase UserSessionListener extienda ademas de ChannelListener y que la funcionalidad implementada para recibir los mensajes de los canales este en esta clase y asi se prodria crear un metodo que abstraiga el compotamiento comun para la recepcion de mensajes directos y a travez de canales en una sola clase (UserSessionListener) ](.md)
  * Roberto Kopp:        [comment ](.md)

## GridManager ##

Cuestion de implementacion

### Question: ###
¿Porque el hashtable no servia como deposito de los mundos?
### Answer: ###
El problema no es el hashtable sino que al tener un ManagedReference a esta estructura, se generaban excepciones ya que para acceder a un objeto que es referenciado, el código de acceso debe pertenecer o estar dentro del contexto en donde fue creada la referencia, osea, que se produce un error cuando recuperamos un mundo que fue previamente almacenado.
Se podria haber almacenado el hashtable con setBinding y haberlo recuperado con setBinding, pero esto no sería muy eficiente ya que cada vez que se desea acceder a un mundo hay que recuperar todo el hashtable con los demas.

### Solution proposed: ###
DESCRIPTION Se reemplaza el hash por un id long con el cual se van a ir identificando los
mundos.
TODO
Corregir los comentarios de toda la clase porque no representan lo que hace el codigo internamente.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo: Si eliminamos el hashtable será necesario abstraer la logica de generación de ids porque supongo que la podemos reutilizar en varios lugares.
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti: Se puede abstraer para la generacion de ids, se puede reutilizar la clase CellIDGenerator, habria que hacer algunas modificaciones a la clase GridManager y al metodo que genera los ids.
  * Roberto Kopp:        [comment ](.md)

## GridManager ##
### Question: ###
¿Porque en el metodo removeStructure se utiliza "dataManager.markForUpdate(this)" si en realidad no cambia el estado de la clase GridManager?
### Answer: ###
Creo que esta demás, no cambia el estado de la clase porque los mundos no se almacenan en una estructura interna a discha clase, osea cuando un mundo se elimina, se hace directamente desde la DB la cual si cambia y el estado del objeto no.

### Solution proposed: ###
DESCRIPTION Remover esta linea porque no hace falta ya que no se actualiza el estado del
objeto.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti:     [comment ](.md)
  * Roberto Kopp:        [comment ](.md)

## GridManager ##
### Question: ###
-
### Answer: ###
-
### Solution proposed: ###
DESCRIPTION -
TODO Uniformizar el codigo para el tratamiento de las excepciones porque en todos los casos es distinto.
Al mismo tiempo definir si se va a retornar null en caso de que los objetos no se puedan recuperar del object store, o si se va a hacer un rethrow de las excepiones que genera el framework.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti:     [comment ](.md)
  * Roberto Kopp:        [comment ](.md)

## MatrixStructure ##
### Question: ###
¿Porque el set del id queda oculto en la clase GridManager cuando se agrega el mundo al manager?
### Answer: ###
Es responsabilidad del manager generar un id nuevo para cada mundo, un manager tendria que hacer esto, sino será solo un repositorio de mundos.

### Solution proposed: ###
DESCRIPTION El metodo addStructure de la clase GridManager hace el set del id del mundo.

### Approved: ###
  * Cabrera Facundo:     not approved
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     Desacoplar esto sacando la generacion de ids a un mecanismo externo porque queda oculto entre el funcionamiento. (Ver comentario en la sección GridManager).
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti: Podemos sacar la generacion de ids y usar la implemetada en la clase CellIDGenerator y desacoplar esta funcionalidad para que el GridManger la utilice.
  * Roberto Kopp:        [comment ](.md)

## MatrixStructure ##
### Question: ###
Metodo getCell necesita ser optimizado porque está programado de la forma mas lenta me parece.
### Answer: ###
(completar por el que realizo el cambio o implementación del metodo)
### Solution proposed: ###
DESCRIPTION El método fue implementado antes de que las celdas se instancien dentro de MatrixGridStructure, por lo que ahora que se instancian dentro, sabemos que todas las celdas tienen el mismo ancho y alto. Se puede hacer una cuenta matemática para obtener un rango mas chico de búsqueda (así no recorremos toda la matriz).

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  También se podría implementar una búsqueda binaria.
  * Pablo inchausti: Se me ocurrió alguna busqueda tipo binaria o algo asi para que sea mas eficiente el acceso.
  * Roberto Kopp:        [comment ](.md)


## Player ##
### Question: ###
Según pablo hay problemas con la serializacion de un hashtable, por lo tanto las properties de un player no se podrían persistir correctamente.
Por otra parte no veo conexión con la creación del player y la inicialización de este atributo en el codígo.

### Answer: ###
(completar por el que realizo el cambio o implementación del metodo)
### Solution proposed: ###
DESCRIPTION -

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti: El problema no es el hashtable (en la clase GridManager) sino que los errores de debian al ManagedReference a esta estructura. No se preocupen que el hash va a funcionar. Por lo que vi en el código, falta setearle al player el estado inicial que tendria que ser “QUIET” y las propiedades como vos decis. Con respecto a esto ultimo no se si habria que acceder al la base de datos (ModelAccess) para recuperar las propiedades del mismo.
  * Roberto Kopp:        [comment ](.md)


## PTaskDelegator ##
### Question: ###
Dentro del metodo process(...) es necesario setear tanto el player asociado a la task como tambien la celda.
En este caso me encuentro con un comentario que dice que hay problemas con las celdas porque no son Managed Object.

Ver la sección UserSessionListener punto 1.

### Answer: ###
Unos de los problemas que me surgieron a la hora de implementar la inicialización del Server fueron las excepciones provocadas por esta línea de código. Lo analice varias veces y me di cuenta que se producía cuando el DataManger creaba una referencia al objeto Cell y requería que dicho objeto sea ManagedObject. Si descomentas esta linea se generará una excepción salvo que la clase Cell implemente ManagedObject, pero no te lo recomiendo porque habría que cambiar la estructura de la matriz y se va a complicar demasiado. === Solution proposed: ===
Descomentar la linea porque esta mal que esté comentada me parece.

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti:     Que la clase Cell implemente ManagedObject o optar por recuperar la celda con getCell en la implementación de las tareas.

  * Roberto Kopp:        [comment ](.md)


## UserSessionListener ##
### Question: ###
Dentro del metodo receivedMessage(...) es necesario setear tanto el player como la celda donde esta el mismo al objeto processor porque necesita las 2 cosas para funcionar. Es parte del estado de un procesador, y si no son seteadas falla la linea que fue comentada en la clase PTaskDelegator.

### Answer: ###
En este caso no le definí la celda a los procesadores porque nunca se ejecutará el procesador PTaskDelegator ya que corresponde al procesamiento de los mensajes (tareas) provenientes de los canales. Aparte, los demás procesadores no hacen uso de la instancia de Cell para su implementación, salvo la clase PTaskDelegator.
Solo es necesario definir la celda en el método receivedMessage() de la clase ChannelMessageListener en el cual si define la celda.
Igualmente se puede setear la celda en el método receivedMessage() de la clase UserSessionListener pero tendríamos que llamar al método getCell() para obtenerla lo cual cargaríamos con mas procesamiento al servidor.

### Solution proposed: ###
-

### Approved: ###
  * Sebastian Perruolo:  [completely | not completety | not approved ](.md)
  * Pablo inchausti:     [completely | not completety | not approved ](.md)
  * Roberto Kopp:        [completely | not completety | not approved ](.md)

### Alternative solution: ###
  * Cabrera Facundo:     [comment ](.md)
  * Sebastian Perruolo:  [comment ](.md)
  * Pablo inchausti:     [comment ](.md)
  * Roberto Kopp:        [comment ](.md)