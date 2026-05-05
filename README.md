# 🎬 MovieLibrary – Taller Práctico 4
- Juan Sebastian Ropero Amado
- E-191
- 05-05-2026
- Aplicaciones Moviles - Unidades Tecnologicas de Santander
- Tecnologia en desarrollo de sistemas informaticos

Aplicación Android desarrollada como parte del Taller 4, implementando arquitectura MVVM con Navigation
Component, Safe Args, LiveData y Room.

---

## 📱 Funcionalidades

- Ver lista completa de películas guardadas localmente.
- Agregar nuevas películas mediante formulario.
- Ver el detalle de cada película.
- Marcar películas como vistas o pendientes.
- Editar los datos de una película existente.
- Eliminar películas del catálogo.

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM** (Model–View–ViewModel) con las
siguientes capas:

model/       → Clases de dominio (Movie)
db/          → Room: Entity, DAO, AppDatabase, Mapper
repository/  → MovieRepository (abstracción del acceso a datos)
viewmodel/   → MovieViewModel (lógica de presentación)
ui/          → Fragments (Lista, Detalle, Edición)

---

## 💡 Documento de Reflexión

### ¿Qué es el ViewModel y por qué es útil?

El `ViewModel` es un componente de la arquitectura Android cuya
responsabilidad es contener y gestionar la lógica de presentación de la
interfaz de usuario. Lo que lo hace especialmente valioso es que **sobrevive
a los cambios de configuración** como la rotación de pantalla: mientras que
un Fragment se destruye y se vuelve a crear, el ViewModel permanece en
memoria, conservando el estado de la UI sin necesidad de guardar y restaurar
datos manualmente.

En MovieLibrary, el `MovieViewModel` centraliza todas las operaciones sobre
películas (insertar, actualizar, eliminar, seleccionar) y expone la información
a los Fragments a través de `LiveData`, de modo que la lógica de negocio
queda completamente separada de la capa visual.

---

### ¿Qué es LiveData y cómo funciona?

`LiveData` es un contenedor de datos observable que **respeta el ciclo de
vida** de los componentes Android (Activities y Fragments). Esto significa
que solo notifica a los observadores activos (en estado STARTED o RESUMED),
evitando crashes por actualizaciones cuando la UI ya no está visible.

En la práctica, cuando Room actualiza la lista de películas en la base de datos,
el `LiveData` emite automáticamente el nuevo valor, y los Fragments que lo
observan se actualizan en la pantalla sin necesidad de recargar manualmente.
Este flujo reactivo elimina mucho código de sincronización manual.

---

### ¿Qué es el Repository y por qué separarlo?

El `Repository` actúa como una **capa de abstracción** entre el ViewModel
y las fuentes de datos (en este caso, Room). Su función principal es que el
ViewModel no necesite saber *de dónde* vienen los datos: solo le pide la
información al Repository y este se encarga de ir a buscarla.

Esta separación tiene varias ventajas: facilita las pruebas unitarias (se puede
reemplazar el repositorio por un mock), permite cambiar la fuente de datos
(por ejemplo, agregar una API remota) sin modificar el ViewModel, y mantiene
el código organizado siguiendo el principio de responsabilidad única.

---

### ¿Qué es Navigation Component, NavController y Safe Args?

**Navigation Component** es una biblioteca de Jetpack que gestiona toda la
navegación de la aplicación de forma centralizada mediante un archivo de
grafo (`nav_graph.xml`). Permite visualizar y definir todas las pantallas y
las conexiones entre ellas en un solo lugar.

**NavController** es el motor que ejecuta la navegación. Cuando se llama a
`findNavController().navigate(...)`, el NavController consulta el grafo y
realiza la transición al destino indicado, manejando automáticamente la pila
de retroceso (back stack).

**Safe Args** es un plugin que genera clases Kotlin tipadas a partir de los
argumentos definidos en el nav_graph. Antes de Safe Args, pasar datos entre
fragments usando `Bundle` era propenso a errores: podías escribir mal el nombre
de la clave o poner el tipo de dato incorrecto y el error solo aparecía en
tiempo de ejecución. Con Safe Args, esos errores se detectan en tiempo de
compilación, haciendo el código más seguro y legible.

---

### ¿Qué es Room y cuáles son sus componentes?

**Room** es la biblioteca de persistencia de Android que proporciona una capa
de abstracción sobre SQLite. Sus tres componentes principales son:

- **Entity**: una clase de datos anotada con `@Entity` que representa una
  tabla en la base de datos. Cada instancia es una fila de esa tabla.

- **DAO** (Data Access Object): una interfaz anotada con `@Dao` que define
  los métodos para interactuar con la base de datos (consultar, insertar,
  actualizar, eliminar). Room genera automáticamente la implementación de
  estos métodos.

- **Database**: la clase abstracta anotada con `@Database` que extiende
  `RoomDatabase`. Es el punto de entrada principal para acceder a la base de
  datos y expone los DAOs. Se implementa como Singleton para evitar múltiples
  instancias abiertas.

En MovieLibrary, Room garantiza que los datos persistan entre sesiones de la
app y entre rotaciones de pantalla, trabajando en conjunto con LiveData para
notificar automáticamente los cambios a la interfaz.

---

## 🚀 Tecnologías utilizadas

| Tecnología | Versión |
|---|---|
| Kotlin | 1.9.22 |
| Android minSdk | 24 |
| Room | 2.6.1 |
| Navigation Component | 2.7.7 |
| Lifecycle (ViewModel/LiveData) | 2.7.0 |
| Material Components | 1.11.0 |

---

