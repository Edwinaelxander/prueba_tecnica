# Proyecto de Prueba Técnica

Este proyecto es una prueba técnica que integra un **frontend** y un **backend**. Se ha desarrollado un sistema CRUD para gestionar libros utilizando tecnologías como **JSP**, **Java**, **Servlets**, **HTML**, **JavaScript** y **JDK 22**. Finalmente, se implementará en producción utilizando **Render**.

## Tecnologías Utilizadas
- **Frontend**: JSP, HTML, JavaScript
- **Backend**: Java, Servlets
- **Base de Datos**: MySQL (dentro de un contenedor Docker)
- **JDK**: 22
- **Docker**: Para contenerizar la aplicación

## Proceso de Instalación

1. **Descargar el Código**:
   - Descarga el proyecto desde GitHub como un archivo `.zip`.

2. **Configurar el Proyecto en NetBeans**:
   - Abre NetBeans.
   - Ve a **Configuración** (Configuration) en el menú.
   - Selecciona la opción **Exportar en ZIP** (Export as ZIP).
   - En el menú de archivo, selecciona **Abrir**.
   - Selecciona el archivo `.zip` que descargaste previamente y haz clic en **Abrir**.

3. **Construir y Ejecutar**:
   - Asegúrate de que tu entorno de desarrollo tenga configurado el JDK 22.
   - Compila y ejecuta el proyecto para verificar que todo funcione correctamente.

## Docker

El proyecto ha sido configurado para ejecutarse en contenedores Docker. Asegúrate de tener Docker instalado y ejecutando en tu sistema.

### Construir la Imagen de Docker

Ejecuta el siguiente comando en la raíz del proyecto para construir la imagen de Docker:

```bash
docker build -t nombre_de_tu_imagen .

