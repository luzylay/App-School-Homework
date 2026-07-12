# 🚀 Guía de Actualización del Proyecto (Buenas Prácticas)

¡Hola! Esta guía te servirá para mantener tu repositorio de GitHub de forma profesional, siguiendo los estándares que acabamos de configurar.

---

## 🛠 Paso 1: Realiza tus cambios en Android Studio
Trabaja en tus pantallas, lógica o diseño como lo haces normalmente. Asegúrate de que tu código funcione y el proyecto **compile sin errores** (pulsa el martillo de "Build").

---

## 📝 Paso 2: Guardar tus cambios (Git local)
Cuando estés listo para guardar un avance, sigue estos tres comandos en la terminal:

1.  **Preparar los archivos:**
    ```bash
    git add .
    ```
    *(Esto dice: "Git, fíjate en todos los cambios que hice").*

2.  **Crear el "punto de guardado" (Commit):**
    En lugar de poner mensajes genéricos como "actualización", usa estos prefijos (se llaman *Conventional Commits*):
    - `feat:` si es una nueva funcionalidad.
    - `fix:` si arreglaste un error.
    - `docs:` si solo cambiaste comentarios o documentos.
    - `style:` si solo cambiaste diseño (colores, espaciados).

    **Ejemplo:**
    ```bash
    git commit -m "feat: añadida lógica de búsqueda en pantalla de docentes"
    ```

---

## ⬆️ Paso 3: Subir a la nube (Push)
Ahora envía tus cambios a GitHub:
```bash
git push origin main
```
*Nota: Ya **NO** necesitas usar `--force`. Eso solo se usa en emergencias o cuando reiniciamos el proyecto como hicimos hoy.*

---

## 🔍 Paso 4: Verifica en GitHub (El "Check" verde)
Como configuramos **GitHub Actions**, cada vez que hagas un `push`, GitHub intentará compilar tu proyecto automáticamente:

1.  Entra a tu repo: `https://github.com/luzylay/App-School-Homework`
2.  Verás un punto amarillo (procesando) o un check verde (éxito) junto al nombre de tu último commit.
3.  Si sale una **X roja**, significa que tu código tiene algún error que impide que compile. ¡Revisalo en Android Studio!

---

## 💡 Consejos de Oro (Buenas Prácticas)

1.  **Haz commits frecuentes:** No esperes a terminar todo el proyecto para subir cambios. Es mejor hacer 5 commits pequeños que uno gigante.
2.  **No subas basura:** El archivo `.gitignore` ya está configurado para ignorar archivos pesados (`.hprof`, carpetas de build). No lo borres.
3.  **Usa Issues:** Si encuentras un error pero no tienes tiempo de arreglarlo, ve a la pestaña "Issues" en GitHub y crea uno usando la plantilla que te dejé. Así no se te olvidará.
4.  **Credenciales:** **NUNCA** pongas contraseñas reales o llaves secretas de APIs directamente en el código. Si necesitas cambiarlas, hazlo en `local.properties` o asegúrate de que el archivo esté ignorado por Git.

---

¡Felicidades por llevar tu proyecto al siguiente nivel! 🎓✨
