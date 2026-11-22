# Wildbound v2 — Roadmap y directrices

> Este archivo es la fuente de verdad del proyecto. Si se agota la cuota o se
> pierde el contexto, retoma desde aquí: lee la sección **Estado actual**, toma
> la primera tarea sin marcar y continúa.

## Reglas de trabajo (siempre)

1. Antes de programar algo nuevo, actualiza **Estado actual** y el checklist.
2. Cada tarea termina con un commit pequeño y descriptivo.
3. Si una decisión cambia el diseño, anótala en **Decisiones** con fecha.
4. No empieces una fase nueva sin que la anterior compile y se pueda ejecutar.
5. Lo que no se pueda verificar en este entorno (ver *Limitaciones*) se marca como
   `[PENDIENTE DE PRUEBA]` en lugar de darse por bueno.

## Objetivo

Mod de Minecraft tipo RPG para **Fabric 26.3**:

- Sistema de niveles y experiencia.
- HUD ultra detallada y animada, activada con la tecla **H**.
- **16–18 elementos reales**, cada uno con **10 habilidades** (≈160–180 en total).
- Animaciones de habilidad cinematográficas, en tercera persona, con un **cetro 3D**
  de alta calidad como arma/foco visual.
- Ejemplo de referencia — **Water Bolt**: tres estalagmitas de hielo que se forman
  lentamente, cada una más grande que la anterior en orden de cámara. Al pulsar
  **click derecho** con el slot de habilidad seleccionado, se lanzan hacia donde
  mira el jugador.

## Estado actual

- Fase actual: **0 — Cimientos**
- Última tarea completada: `0.2`/`0.3` — plantilla Gradle + código fuente mínimo para Fabric 26.3.
- Próxima tarea: `0.4` — compilar y arrancar el mod en un cliente real (requiere hacerlo en tu equipo, ver más abajo).

## Checklist

### Fase 0 — Cimientos
- [x] 0.1 Repositorio local con roadmap y README
- [x] 0.2 Plantilla Fabric 26.3 (`fabric.mod.json`, `build.gradle`, `gradle.properties`, mixins, entrypoints main/client)
- [x] 0.3 Versiones investigadas en fuentes oficiales de Fabric (ver **Decisiones**) — **falta compilar de verdad** `[PENDIENTE DE PRUEBA]`
- [ ] 0.4 Mod arranca en cliente y servidor sin crash (hazlo en tu equipo con IntelliJ/Gradle, ver `docs/COMPILAR.md`)

### Fase 1 — Núcleo RPG
- [ ] 1.1 Capability de datos del jugador (nivel, XP, elemento activo, slots de habilidad)
- [ ] 1.2 Fórmula de XP y subida de nivel, con evento al subir
- [ ] 1.3 Persistencia (NBT / Codec) al desconectar y reconectar
- [ ] 1.4 Fuente de XP (matar mobs, minar, etc.) configurable en JSON
- [ ] 1.5 Comando de depuración `/wildbound level set|xp add`

### Fase 2 — Sistema de elementos y habilidades
- [ ] 2.1 Registro de elementos (datos en JSON: nombre, color, icono, habilidades)
- [ ] 2.2 Registro de habilidades: id, coste de maná/cooldown, nivel requerido, animación
- [ ] 2.3 Desbloqueo de habilidades por nivel
- [ ] 2.4 Selección de elemento y de slot de habilidad (rueda del ratón / teclas 1–9)
- [ ] 2.5 Lanzamiento con click derecho: validación de cooldown, maná y objetivo

### Fase 3 — HUD (tecla H)
- [ ] 3.1 Pantalla de HUD con pestañas por elemento
- [ ] 3.2 Barras animadas de XP, maná y cooldowns
- [ ] 3.3 Iconos de habilidad con estado (bloqueada / disponible / en cooldown)
- [ ] 3.4 Transiciones y animaciones de entrada/salida
- [ ] 3.5 Escalado y ajuste de opacidad en configuración

### Fase 4 — Motor de animaciones
- [ ] 4.1 Sistema de secuencias temporales (keyframes por tick + interpolación)
- [ ] 4.2 Cámara en tercera persona durante la habilidad (sin romper la jugabilidad)
- [ ] 4.3 Partículas y sonidos sincronizados con la secuencia
- [ ] 4.4 Proyectiles con trayectoria y colisión hacia el punto de mira
- [ ] 4.5 Prototipo completo de **Water Bolt** (3 estalagmitas de hielo)

### Fase 5 — Cetro 3D
- [ ] 5.1 Modelo 3D del cetro (formato Blockbench → JSON de Minecraft)
- [ ] 5.2 Texturas en alta resolución (64×64 o 128×128 con paleta coherente)
- [ ] 5.3 Animaciones del cetro: reposo, carga, disparo
- [ ] 5.4 Variante de cetro por elemento (color y forma)

### Fase 6 — Contenido
- [ ] 6.1 Elemento 1: Agua (10 habilidades)
- [ ] 6.2 Elemento 2: Fuego (10 habilidades)
- [ ] 6.3 … hasta 16–18 elementos, uno a la vez, cada uno con su lista de habilidades
- [ ] 6.4 Lista de elementos y habilidades acordada con el usuario antes de implementar

### Fase 7 — Pulido y publicación
- [ ] 7.1 Rendimiento: medir tick time con muchas partículas
- [ ] 7.2 Pruebas en multijugador dedicado
- [ ] 7.3 README con capturas, guía de instalación y lista de elementos
- [ ] 7.4 Release v0.1.0 en GitHub

## Decisiones

- 2026-11-22: Stack objetivo Fabric 26.3, Java. Datos de elementos y habilidades en JSON para poder añadir contenido sin tocar código.
- 2026-11-22: Animaciones basadas en secuencias de keyframes por tick, no en animaciones de cliente sueltas, para que sean reproducibles en servidor.
- 2026-11-22: **Cambio grande del ecosistema Fabric a partir de 26.1** (confirmado en los anuncios oficiales de fabricmc.net): Minecraft se distribuye sin ofuscar desde esa versión, así que **ya no se usan mapeos Yarn**; se usan directamente los nombres de Mojang. Esto afecta a toda la plantilla:
  - Java mínimo: **25** (antes 21).
  - Loom: plugin id `net.fabricmc.fabric-loom` (ya no `fabric-loom` / `fabric-loom-remap`), versión `1.17` para 26.3.
  - Sin bloque `mappings` en `build.gradle`.
  - `modImplementation`/`modCompileOnly` reemplazan a lo que antes era `remapJar`+mapeos; la tarea de jar final ya no remapea.
  - El id de mod de Fabric API pasó de `fabric` a `fabric-api`.
  - Algunas clases de Fabric API se renombraron a los nombres oficiales de Mojang (p. ej. `ItemGroupEvents` → `CreativeModeTabEvents`); hay que comprobar nombres de clase al usar la API, no dar por bueno lo que yo recuerde de versiones anteriores.
  - Fuentes: [fabricmc.net/2026/03/14/261.html](https://fabricmc.net/2026/03/14/261.html), [fabricmc.net/2026/09/15/263.html](https://fabricmc.net/2026/09/15/263.html), [docs.fabricmc.net — Migrating Mappings](https://docs.fabricmc.net/26.2/develop/porting/mappings/), [docs.fabricmc.net — Porting to 26.1](https://docs.fabricmc.net/26.1.2/develop/porting/), [Fabric API 0.155.0+26.3 en Modrinth](https://modrinth.com/mod/fabric-api/version/0.155.0+26.3).
  - **Importante:** no he podido compilar esta plantilla en este entorno (sin acceso de red para Gradle/Maven). Los valores de `gradle.properties` (`loader_version=0.19.5`, `loom_version=1.17`, `fabric_version=0.155.0+26.3`) están tomados de esas fuentes oficiales pero **debes confirmarlos en https://fabricmc.net/develop antes de compilar**, porque pueden publicarse versiones de parche más nuevas entre esta sesión y cuando tú compiles.

## Pendiente de decidir con el usuario

- Lista definitiva de los 16–18 elementos (propuesta: agua, fuego, tierra, aire, rayo, hielo, veneno, sombra, luz, sangre, arena, metal, naturaleza, vacío, tiempo, psíquico).
- Mecánica de subida de nivel: ¿XP por mobs, por bloques, o ambas?
- Licencia del repositorio.

## Limitaciones conocidas

- El entorno de trabajo no puede ejecutar Minecraft ni ver el juego renderizado; la
  verificación visual la hace el usuario en su equipo.
- Las versiones exactas de Fabric/Loom para 26.3 deben confirmarse con la documentación
  oficial antes de fijarlas en `gradle.properties`.
- Los modelos 3D se generan como JSON de Minecraft y/o desde Blockbench; la calidad
  final depende de la revisión visual.
