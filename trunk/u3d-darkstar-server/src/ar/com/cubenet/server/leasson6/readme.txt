Descripción de la funcionalidad implementada en leasson6
--------------------------------------------------------

El AppListener de este ejemplo crea un canal y agrega cada cliente
a ese canal. Cada vez que un cliente envía un mensaje por ese canal,
el canal recibe el mensaje y crea una tarea ComplexTask.

Cuando la tarea ComplexTask se ejecuta (método run) se crean
tres instancias de SimpleTask y se enconlan. También se crea
una instancia de RecursiveTask y se encola. Luego ComplexTask
reenvía el mensaje por el canal.

SimpleTask cuando se ejecuta envía el mensaje por el canal.

RecursiveTask cuando se ejecuta crea otra tarea RecursiveTask,
la encola y luego reenvía el mensaje.

El cliente recibe primero el mensaje que reenvía ComplexTask,
luego los mensajes que envían las instancias de SimpleTask
y RecursiveTask. Las RecursiveTask llegan en orden.

Darkstar Project dice que asegura que las tareas padres se ejecuten
antes que las hijas. No encontré ninguna documentación que diga cómo 
crear tareas hijas así que las cree en el run de cada tarea que,
por esto, se convertirá en padre. Pero obviamente, las tareas creadas
en el método run se ejecutarán luego que la tarea que las crea dentro
de ese método.

En conclusión, no estoy muy seguro que leasson6 utilice parent task y
child task.