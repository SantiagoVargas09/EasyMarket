# EasyMarket 🛒

Sistema de comercio electrónico desarrollado en Java con Swing como interfaz gráfica.  
Proyecto final — Programación Orientada a Objetos.

---

## Integrantes

| Nombre | GitHub |
|--------|--------|
| (Tu nombre aquí) | @usuario |
| (Integrante 2) | @usuario |
| (Integrante 3) | @usuario |
| (Integrante 4) | @usuario |

---

## Tecnologías usadas

- **Lenguaje:** Java 21+
- **Interfaz:** Swing
- **Persistencia:** Archivos `.txt` (en la carpeta `data/`)
- **Build:** Maven
- **IDE:** NetBeans

---

## Cómo ejecutar

### Opción 1 — Desde NetBeans
1. Clonar o descargar el repositorio.
2. Abrir NetBeans → `File > Open Project` → seleccionar la carpeta del proyecto.
3. Clic derecho en el proyecto → `Run`.

### Opción 2 — Desde la terminal
```bash
# Compilar y empaquetar
mvn package

# Ejecutar
java -jar target/EasyMarket-1.0-SNAPSHOT.jar
```

> **Importante:** ejecutar desde la raíz del proyecto para que la carpeta `data/` se cree correctamente.

---

## Credenciales de prueba

| Rol | Email | Contraseña |
|-----|-------|------------|
| Administrador | admin@easymarket.com | admin123 |

El administrador se crea automáticamente la primera vez que se ejecuta la app.  
Los clientes pueden registrarse desde la pantalla de login.

---

## Funcionalidades

### Cliente
- Registro e inicio de sesión
- Ver catálogo de productos con stock en tiempo real
- Agregar productos al carrito con cantidad personalizada
- Ver y modificar el carrito
- Confirmar compra con pago en efectivo o tarjeta
- Ver historial de pedidos

### Administrador
- Agregar, editar y eliminar productos
- Ver todos los pedidos del sistema

---

## Estructura del proyecto

```
EasyMarket/
├── data/                        ← Archivos de persistencia (se generan solos)
│   ├── usuarios.txt
│   ├── productos.txt
│   └── pedidos.txt
├── docs/
│   └── uml/
│       ├── diagrama-clases.png
│       └── diagrama-casos-uso.png
└── src/main/java/com/easymarket/
    ├── model/                   ← Clases del dominio
    ├── repository/              ← Acceso a los archivos
    ├── service/                 ← Lógica de negocio
    ├── ui/                      ← Interfaz gráfica (Swing)
    └── utils/                   ← Utilidades (FileManager, IdGenerator)
```

---

## Conceptos de POO aplicados

- **Herencia:** `Usuario` → `Cliente` / `Administrador`; `Pago` → `PagoEfectivo` / `PagoTarjeta`
- **Encapsulación:** todos los atributos son privados con getters/setters
- **Polimorfismo:** `getTipo()` y `procesarPago()` con `@Override` en cada subclase
- **Abstracción:** clases abstractas `Usuario` y `Pago`
- **Colecciones:** `ArrayList` para productos, usuarios, pedidos e items del carrito
- **Manejo de excepciones:** `try/catch` en lectura de archivos, validaciones de stock y formularios
