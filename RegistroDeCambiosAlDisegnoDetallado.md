# Proposito #

Aquí quedarán registrados los cambios que vamos haciendo al diseño detallado.

# Recomendaciones #

  1. Checkstyle propone cambios, los cuales deberán ser analizados y justificados debidamente, no es necesario hacer feliz a checkstyle, lo importante es hacer las cosas lo mejor posible.
  1. Cambios en las interfaces de las clases necesito que sean aprobados por todos lo que usan tales clases. Un ejemplo es lo que pasa entre Roberto y Sebastian que comparten un método y que el cambio que propone Roberto está aprobado por Sebastian.

# Cambios Propuestos #

Roberto Kopp

Hola a todos!! tengo algunas propuestas a cambios, debido al uso de checkstyle y otras consideraciones...
Saludos Roberto.

Clase abstracta: Entity

Atributo: protected String idEntity;
Cambio: private String idEntity;
Motivo: El checkstyle me pide que el atributo sea protegido y ademas esta clase es la unica que modifica dicho atributo.

En estos dos metodos el checkstyle exige que sean declarados como fina, pero hay que tener en cuenta que las clases que extienda de Entity no podran sobrescribir estos metodos.

public String getIdEntity()
public void setIdEntity(String entity)

No se si esta bien, pero esta clase es abstracta y no contiene ningun metodo abstracto.

Clase abstracta DynamicEntity:

Todos los atributos deben ser private segun checkstyle.
Me pide que todos los metodos y parametros sean final.
No contiene metodos abtractos.

Clase: Player

Por defecto me pide el atributo private static final long serialVersionUID = 1L porque la clase es serializable.
Todos los atributos deben ser private segun checkstyle.

Atributo: ManagedReference

&lt;ClientSession&gt;

 session;
Cambio: ManagedReference

&lt;ClientSession&gt;

 refSession;
Motivo: para que sea claro que es una referencia a una sesion y no un objeto ClientSession

Metodo: public final ManagedReference

&lt;ClientSession&gt;

 getSession()
Cambio: public final ClientSession getSession()
Motivo: El player tendria que devolver directamente la session y no la referencia.

Metodo: public final void setSession(final ManagedReference

&lt;ClientSession&gt;

 session)
Cambio: public final void setSession(final ClientSession session)
Motivo: el player deberia tener en este metodo la funcionalidad para crear una referencia a la session de propio jugador.

Sebastian Perruolo

Facu (y a algunos también le puede interesar), estuve mirando/haciendo mis tareas y hay algunos temas que dan dudas. Quedamos en que para realizar cambios al código había que mandar mails pidiendo los cambios y por otro lado tengo el checkstyle que me llena el código de amarillitos.

Checkstyle me pide que los parámetros tengan el atributo final. ¿Agregar este atributo es considerado un cambio? Checkstyle me pide que haga esto en casi todos los métodos que están definidos, sin embargo, poner final prohibirá en un futuro extender la clase. Además, el atributo final debería (según checkstyle) ser agregado a métodos, argumentos y declaraciones de clases.

Checkstyle se queja diciendo "<parámetro> oculta un campo" cuando el parámetro tiene
el mismo nombre que un atributo de la clase.

Hay otros cambios que vimos con Pablo y Victor donde a veces se retorna o se pide un ManagedReference cuando en realidad creemos que debería utilizarce el objeto en lugar que su ManagedReference. Listo algunos ejemplos a continuación:

Clase: GridManager
Método: public ManagedReference getStructure(Object id)
Cambio: public IGridStructure getStructure(Object id)
Motivo: Conceptual. En GridManager la nomenclatura del método (add) addStructure es: public void addStructure(final IGridStructure structure). En cambio el método (get) getStructure es public ManagedReference getStructure(Object id). En otras palabras tengo: addStructure(IGridStructure) y getStructure():ManagedReference.

Clase: Cell
Método: public ManagedReference getChannel();
Cambio: public Channel getChannel()
Motivo: Idem anterior.

Clase: Cell
Método: public Cell(Object id, Object bounds, ManagedReference parent); //Creador
Cambio: public Cell(Object id, Rectangle bounds, IGridStructure parent);
Motivo: Los parámetros están definidos como tipos más genéricos: Object y ManagedReference cuando podrian ser más específicos.

Clase: Cell
Método: public ManagedReference getStructure();
Cambio: public IGridStructure getStructure()
Motivo: La estructura es un IGridStructure, pero se está retornando un ManagedReference.

Clase: Cell
Atributo: protected ManagedReference structure;
Cambio: private ManagedReference

&lt;IGridStructure&gt;

 refStructure;
Motivo: Estándar. El atributo al ser de tipo ManagedReference debería estar paramétrizando el tipo. En los ejemplos de Darkstar se agrega "ref" a los atributos de tipo ManagedReference. Además checkstyle se queja si los atributos están declarados como protected, sugiere usar private.

Clase: Cell
Atributo: protected ManagedReference channel;
Cambio: private ManagedReference

&lt;Channel&gt;

 channel;
Motivo: Idem anterior.

Clase: Cell
Método: public Object isInside(Vector3f position);
Cambio: public boolean isInside(Vector3f position);
Motivo: Determina si la posicion dada esta dentro de la celda y no entiendo porqué retorna un Object.


## Cambio ##
  * **Clase:** ServerMsgProcessor
  * **Atributo:** private ManagedReference`<`Cell`>` cellAsociete
  * **Cambio:** private ManagedReference`<`Cell`>` refCellAssocieted
  * **Motivo:** el primer motivo es que es un atributo ManagedReference, lo cual facilita el entendimiento que su nombre comience con _ref_. Segundo, no sé si _asociete_ es francés, pero definitivamente no es inglés ni español.
  * **Autor:** Sebastián Perruolo.
  * **Estado:** pedido.

## Cambio ##
  * **Clase:** ServerMsgProcessor
  * **Atributo:** private ManagedReference`<`Cell`>` playerAsociete
  * **Cambio:** private ManagedReference`<`Cell`>` refPlayerAssocieted
  * **Motivo:** el primer motivo es que es un atributo ManagedReference, lo cual facilita el entendimiento que su nombre comience con _ref_. Segundo, no sé si _asociete_ es francés, pero definitivamente no es inglés ni español.
  * **Autor:** Sebastián Perruolo.
  * **Estado:** pedido.