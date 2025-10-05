# 🌿 Proyecto Here4U – Configuración y Ejecución

Este repositorio contiene el código fuente del proyecto **Here4U**, una aplicación móvil desarrollada en **Kotlin** bajo arquitectura **MVVM**, integrada con **Firebase** y servicios externos como **OpenAI API**.

A continuación encontrarás todos los pasos necesarios para preparar el entorno, configurar las claves y ejecutar correctamente el proyecto desde la rama de desarrollo (`develop`).

---

## 🚀 Configuración previa al correr el proyecto

Antes de ejecutar la aplicación, es necesario realizar algunos pasos de configuración para asegurar que las claves y variables de entorno estén correctamente definidas.

---

### ⚙️ 1. Configurar la API Key

1. Dirígete al archivo **`local.properties`** ubicado en la raíz del proyecto (donde se encuentra el archivo `gradle.properties`).
2. Agrega tu **API Key** con el siguiente formato:

   ```properties
   OPENAI_API_KEY=tu_api_key_aquí
3. Subir el archivo .env a la carpeta del proyecto
