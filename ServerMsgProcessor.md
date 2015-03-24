# Documentación #
La clase representa la base de la jerarquia de procesadores de mensajes en el servidor Darkstar.

Abstrae el comportamiento de cualquier procesador utilizado en el servidor, al definir una {@link ManagedReference} (a un {@link Player}) y una {@link ManagedReference} (a una {@link Cell}) que representan las relacionadas a la recepsion y/o envio de los mensajes a procesar.