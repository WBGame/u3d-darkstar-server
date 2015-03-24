# Introducción #

Un archivo de configuración es un archivo de texto plano que principalmente detalla
  * con qué clase instanciar _com.sun.sgs.app.AppContext_.
  * ubicación de la base de datos Berkeley.


# Ejemplo #

Un archivo de configuración de ejemplo podría ser el siguiente:
```
# This is the properties file for running the HelloChannels
# example from the Project Darkstar Server Application Tutorial

com.sun.sgs.app.name=ServerChannels
com.sun.sgs.app.root=data/ServerChannels
com.sun.sgs.app.port=1139
com.sun.sgs.app.listener=ar.com.cubenet.server.leasson3.ServerChannels
```