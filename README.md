# Sistema de Comunicación Interna

Este es un proyecto de Java que implementa un sistema de chat cliente-servidor con interfaz gráfica de usuario (GUI). Permite a múltiples usuarios conectarse a un servidor central, enviar mensajes en tiempo real y ver el log de actividades. Incluye funcionalidades básicas de registro de usuarios y visualización de conexiones activas.

## Descripción

El sistema consta de:
- **Servidor**: Maneja conexiones de múltiples clientes, difunde mensajes y mantiene un log de actividades.
- **Cliente**: Permite a los usuarios registrarse, iniciar sesión, enviar mensajes y desconectarse.
- **Interfaz Gráfica**: Usando Swing para una experiencia visual amigable.
- **Almacenamiento**: Usuarios se guardan en un archivo de texto simple (`usuarios.txt`).

El chat incluye timestamps en los mensajes y notificaciones de conexión/desconexión.

## Características

- Registro e inicio de sesión de usuarios.
- Chat en tiempo real con múltiples usuarios.
- Visualización de usuarios conectados en el servidor.
- Log de actividades en el servidor.
- Pantalla de inicio con barra de progreso.
- Soporte para comandos especiales (ej. listar usuarios conectados).
- Hilos daemon para un cierre limpio de la aplicación.

## Tecnologías Utilizadas

- **Lenguaje**: Java (JDK 8 o superior).
- **Bibliotecas**: Swing para GUI, sockets para comunicación de red.
- **Entorno de Desarrollo**: NetBeans (basado en los archivos `.form` proporcionados).
- **Almacenamiento**: Archivo de texto plano para credenciales de usuarios.

## Requisitos

- Java Runtime Environment (JRE) 8 o superior.
- Compilador Java (para construir el proyecto).
- Opcional: IDE como NetBeans o IntelliJ para editar y ejecutar.

## Instalación

1. Clona o descarga el repositorio:
   ```
   git clone https://github.com/tu-usuario/tu-repositorio.git
   ```

2. Abre el proyecto en tu IDE (ej. NetBeans).
   - Asegúrate de que los archivos `.java` y `.form` estén en sus paquetes correspondientes:
     - `comunicacion.cliente`: Cliente.java
     - `comunicacion.servidor`: Servidor.java, ManejadorCliente.java
     - `gui`: ClienteGUI.java, ServidorGUI.java, MenuPrincipal.java, PantallaInicio.java

3. Compila el proyecto.

4. Crea el archivo `usuarios.txt` en el directorio raíz del proyecto si no existe (se generará automáticamente al registrar usuarios).

## Uso

### Iniciar el Servidor
1. Ejecuta `ServidorGUI.java` (o desde el IDE).
2. Haz clic en "Iniciar Servidor".
   - El servidor escuchará en el puerto 5000 (configurable en el código).
3. Monitorea el log y la lista de usuarios conectados.
4. Para detener, haz clic en "Detener Servidor".

### Iniciar un Cliente
1. Ejecuta `PantallaInicio.java` (o directamente `MenuPrincipal.java`).
2. En la pantalla de inicio de sesión:
   - Ingresa usuario y contraseña.
   - Si no existe, se registrará automáticamente.
3. Una vez conectado, envía mensajes en el chat.
4. Para ver conexiones activas, usa el botón "Ver Conexiones Activas" en el menú principal.
5. Desconéctate con el botón "Desconectar".

### Notas
- El servidor debe estar corriendo antes de conectar clientes.
- Host por defecto: `localhost`. Puerto: `5000`. Modifícalo en `MenuPrincipal.java` si es necesario.
- Mensajes se difunden a todos los clientes conectados.

## Estructura del Proyecto

```
src/
├── comunicacion/
│   ├── cliente/
│   │   └── Cliente.java
│   └── servidor/
│       ├── Servidor.java
│       └── ManejadorCliente.java
└── gui/
    ├── ClienteGUI.java
    ├── ClienteGUI.form
    ├── MenuPrincipal.java
    ├── MenuPrincipal.form
    ├── PantallaInicio.java
    ├── PantallaInicio.form
    ├── ServidorGUI.java
    └── ServidorGUI.form
usuarios.txt  (generado en runtime)
```
## Contribuciones

Si deseas contribuir:
1. Haz un fork del repositorio.
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`).
3. Commitea tus cambios (`git commit -m 'Agrega nueva funcionalidad'`).
4. Pushea la rama (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.

## Licencia

Este proyecto está bajo la licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

## Autor

- Diego Resendiz
- Contacto: dirm_1104@hotmail.com

Si tienes problemas o sugerencias, abre un issue en el repositorio. ¡Gracias por usar este proyecto!
