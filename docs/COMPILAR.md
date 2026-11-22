# Cómo compilar y probar Wildbound

Este entorno de desarrollo (donde trabaja Claude) **no tiene acceso de red para
descargar Gradle, Loom ni las dependencias de Minecraft**, así que el código se
escribe aquí pero **debes compilarlo y probarlo tú en tu propio ordenador**.
Estos son los pasos:

## Requisitos

- **Java 25** (JDK). Minecraft 26.x lo exige como mínimo.
- **IntelliJ IDEA 2025.3 o superior** (recomendado; versiones anteriores pueden
  no soportar bien los mixins de esta plantilla).
- Conexión a internet (Gradle descargará Loom, Fabric API, etc. la primera vez).

## Pasos

1. Clona el repositorio:
   ```
   git clone https://github.com/angeld03/wildboundv2.git
   cd wildboundv2
   ```
2. Antes de compilar, revisa **gradle.properties** y confirma en
   https://fabricmc.net/develop que `loader_version`, `loom_version` y
   `fabric_version` siguen siendo los más recientes para Minecraft 26.3.
   Esta plantilla se escribió a partir de los anuncios oficiales de Fabric,
   pero no se ha compilado nunca en el entorno de Claude.
3. Abre la carpeta en IntelliJ IDEA como proyecto Gradle, o desde terminal:
   ```
   ./gradlew build
   ```
   (La primera vez Gradle generará el wrapper si no está presente; si falta
   `gradlew`, ejecuta `gradle wrapper --gradle-version 9.6.0` con un Gradle
   instalado localmente.)
4. Para probar en un cliente de Minecraft:
   ```
   ./gradlew runClient
   ```
5. Pulsa **H** en el juego: debería aparecer en el log del cliente la línea
   `[Wildbound] Tecla H pulsada...`. Eso confirma que el mod carga y la tecla
   está registrada (la HUD real llega en la Fase 3 del roadmap).

## Si algo no compila

Lo más probable, dado que Fabric cambió mucho a partir de la versión 26.1
(ver `ROADMAP.md` → Decisiones), es que:

- Algún nombre de clase de Fabric API haya cambiado de nombre oficial
  (antes usaban nombres ofuscados mapeados por Yarn).
- Las versiones exactas en `gradle.properties` ya no sean las últimas.

En ambos casos, dime el error de compilación exacto (o pégalo en una nueva
conversación con este repositorio) y lo corrijo desde ahí.
