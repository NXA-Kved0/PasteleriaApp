# Mil Sabores - App Android (Kotlin + Jetpack Compose)

Integrantes:
-Joshua Martinez
-Ignacia Cavedo

Aplicación móvil para una pastelería llamada **Mil Sabores**, desarrollada en Kotlin con **Jetpack Compose**, **Room**, **Retrofit** y arquitectura basada en ViewModel/Repository. La app consume un backend REST desplegado en AWS (Spring Boot + Swagger) para obtener el catálogo de productos.

## Funcionalidades principales

- **Splash Screen animado** con logo de la pastelería.
- **Autenticación local**:
  - Registro de usuario (Room).
  - Login con validación visual de campos (errores en rojo, checks verdes).
- **Catálogo de productos**:
  - Obtiene los productos desde una **API REST en AWS** y los guarda en Room.
  - Búsqueda por texto (nombre y descripción).
  - Filtros por categoría mediante chips.
  - Modo claro/oscuro con interruptor en el `TopAppBar`.
  - Botón flotante para acceder a la cámara y tomar una foto de referencia para pedidos personalizados.
- **Carrito de compras**:
  - Agregar productos desde el catálogo.
  - Modificar cantidades o eliminar ítems.
  - Visualizar total a pagar.
  - **Badge** en el icono del carrito con la cantidad total de productos.
- **Finalizar compra**:
  - Genera una **boleta** con detalle de productos, subtotal, IVA y total.
  - Permite **compartir** la boleta (WhatsApp, correo, etc.).
- **Integración con cámara**:
  - Toma una foto y la muestra en el catálogo como referencia de pedido personalizado.
- **Tema visual personalizado**:
  - Colores pastel para la marca.
  - Soporte de Modo Oscuro.

## Arquitectura

- **Presentación**: Jetpack Compose (`SplashScreen`, `LoginScreen`, `RegisterScreen`, `CatalogScreen`, `CartScreen`, `CameraScreen`).
- **Navegación**: `NavHost` + `AppNavigation` con rutas (`Splash`, `Login`, `Register`, `Catalog`, `Cart`, `Camera`).
- **Capa de datos local**:
  - `Room`:
    - Entidades: `User`, `Product`, `CartItem`.
    - DAOs: `UserDao`, `ProductDao`, `CartDao`.
    - Base de datos: `AppDatabase`.
- **Capa de datos remota**:
  - `Retrofit`:
    - `ApiService` con endpoint `GET /products`.
    - `RemoteProduct` para mapear el JSON del backend.
    - `RetrofitInstance` con `BASE_URL` apuntando a la IP pública de AWS (`http://100.28.220.201:9090/`).
- **Repositorios**:
  - `UserRepository`, `ProductRepository`, `CartRepository`.
  - `ProductRepository` implementa `syncProductsFromApi()`:
    - Llama al backend con Retrofit.
    - Mapea `RemoteProduct` → `Product`.
    - Limpia e inserta en Room (Single Source of Truth).
- **ViewModels**:
  - `LoginViewModel`, `RegisterViewModel`, `CatalogViewModel`, `CartViewModel`.
  - Cada uno tiene su `ViewModelFactory` para inyectar repositorios.
  - `CatalogViewModel`:
    - Sincroniza con la API en `init`.
    - Expone `products`, filtros, estado de agregar al carrito y `cartItemCount`.
    - Ofrece `refresh()` para volver a sincronizar manualmente.
- **Utils / Componentes**:
  - `ShareUtils` para compartir carrito y boleta.
  - `CameraUtils` para manejar la cámara.
  - Componentes reutilizables: `ProductCard`, `CartItemCard`, `CustomTextField`, `CustomButton`, etc.
  - Tema: `Theme.kt`, `Color.kt`, `ThemeManager` para togglear modo oscuro.

## Integración con la API REST (AWS)

- La app se conecta a un backend Spring Boot desplegado en una instancia EC2.
- Se usa una **Network Security Config** para permitir tráfico HTTP hacia la IP pública de la API:
- <!-- res/xml/network_security_config.xml --> <network-security-config> <domain-config cleartextTrafficPermitted="true"> <domain includeSubdomains="false">100.28.220.201</domain> </domain-config> </network-security-config> ```
Y en el AndroidManifest.xml:
-<application
    ...
    android:networkSecurityConfig="@xml/network_security_config">
-De esta forma, la app puede consumir http://100.28.220.201:9090/products y reflejar los cambios que se hagan desde Swagger.

Requisitos
Android Studio Flamingo o superior.

Kotlin 1.9+.

Mínimo SDK: 24–28 (según configuración del proyecto).

Acceso a Internet y a la IP pública del backend.

Ejecución
Clonar el repositorio.

Abrir el proyecto en Android Studio.

Ajustar BASE_URL en RetrofitInstance si la IP de AWS cambia.

Ejecutar la app en un emulador o dispositivo físico.

Verificar que el backend esté corriendo en AWS en el puerto 9090.

## 📚 Licencia

Proyecto académico desarrollado como parte de la asignatura **“Desarrollo de Aplicaciones Móviles / Consumo de API REST y Microservicios”**.  
Uso educativo y demostrativo.
