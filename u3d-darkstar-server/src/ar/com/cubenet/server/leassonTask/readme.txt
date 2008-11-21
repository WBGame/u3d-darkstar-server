Descripción de la funcionalidad implementada en leassonTask
--------------------------------------------------------

Este ejemplo funcionalmente es equivalente a leasson3, con la diferencia
que los comandos que se agregaron en leasson3 ahora son implementados
utilizando Task y TaskManager. 

Se agrega la clase ar.com.cubenet.server.leassonTask.command.CommandManager
que es donde se encuentra la funcionalidad que crea tareas (clases que
implementan com.sun.sgs.app.Task) dependiendo del comando que envió el 
cliente.

Se agregan las clases:
	ar.com.cubenet.server.leassonTask.tasks.BroadcastLoginCommandTask
	ar.com.cubenet.server.leassonTask.tasks.LeaveChannelCommandTask
	ar.com.cubenet.server.leassonTask.tasks.UnknowCommandTask
	ar.com.cubenet.server.leassonTask.tasks.WhoAmICommandTask
Estas clases son tareas. Para que una clase sea una tarea que pueda
ser manejada por TaskManager debe cumplir:
	- implementar com.sun.sgs.app.Task
	- implementar java.io.Serializable
	- si guarda instancias de ManagedObjects deberá hacerlo a través
	  de ManagedReferences.