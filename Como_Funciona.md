# Explicación del código por Chagui68

### Cosas a tener en cuenta:

Mientras se lee el texto se van a encontrar con simbolos o caracteres especiales, los caracteres tienen una funcionalidad a continuación voy a explicar para que sirve cada uno.

    #: Si al princio del texto contiene este caracter
    significa que es algo que se debe tener en cuenta y que 
    resulta en gran importancia lo que se va a explicar en ese momento

    **: Significa separación de contexto o explicación
    
    /: Significa separación de una explación de un mismo código pero con la
    misma idea y diferente contexto o pertenencia al mismo archivo
    

    /*: Significa separación de parrafo pero con la misma idea y/o contexto

**

## La base de lo importante:

### WeaponsAddon.java

    Empecemos con algo simple de momento con la primera clase 
    de todo el archivo: WeaponsAddon

    La primera linea de codigo que podemos ver es la siguiente y la base de todo

    package com.Chagui68.weaponsaddon;

    # La línea package com.Chagui68.weaponsaddon; 
    # es la declaración del paquete, y debe ser siempre
    # la primera línea de código en cualquier archivo Java
    # (exceptuando comentarios). Esta declaración define el
    # "namespace" o espacio de nombres único donde vive la clase, 
    # funcionando como una dirección postal que indica exactamente 
    # dónde encontrar este archivo dentro del proyecto.
/

    # El propósito principal de los packages es evitar conflictos de
    # nombres y organizar el código de manera lógica. Si dos
    # desarrolladores crean una clase llamada WeaponsAddon, Java las 
    # puede diferenciar porque una se llama com.Chagui68.weaponsaddon.WeaponsAddon 
    # y la otra podría ser com.otrousuario.addon.WeaponsAddon. 
    # Sin esta declaración, todas las clases estarían en el "default package" 
    # (sin organización), lo cual está desaconsejado en proyectos reales porque 
    # genera caos y hace imposible la modularización.

/

## Imports en el archivo base

    # Este bloque de imports declara todas las clases externas
    # que "WeaponsAddon.java" necesita utilizar directamente en
    # su código. Java requiere que declares explícitamente qué clases
    # vas a usar, excepto las del paquete "java.lang" que se importan
    # automáticamente.    # Cada línea "import" le dice al compilador dónde
    # encontrar una clase específica cuando la menciones en el código,
    # evitando tener que escribir la ruta completa cada vez (por ejemplo,
    # escribir solo "MilitaryMobHandler" en lugar de "com.Chagui68.handlers.MilitaryMobHandler" repetidamente).
    # Esto hace que el código sea mucho más corto y fácil de leer.

/

    import com.Chagui68.weaponsaddon.handlers.MachineGunHandler;
    import com.Chagui68.weaponsaddon.items.BombardmentTerminal;
    import com.Chagui68.weaponsaddon.items.MachineGun;
    import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
    import com.Chagui68.weaponsaddon.items.MilitaryComponents;
    import com.Chagui68.weaponsaddon.items.machines.TerminalClickHandler;
    import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
    import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
    import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
    import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
    import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
    import org.bukkit.Material;
    import org.bukkit.NamespacedKey;
    import org.bukkit.plugin.java.JavaPlugin;

/


    # El bloque está organizado en tres grupos lógicos para facilitar la lectura.
    # El primer grupo importa las clases propias del addon ubicadas en subpaquetes:
    # "MachineGunHandler" maneja los eventos de la ametralladora, "BombardmentTerminal",
    # "MachineGun", "MachineGunAmmo" y "MilitaryComponents" contienen las definiciones
    # de items, y "TerminalClickHandler" gestiona la interfaz gráfica del terminal. 
    # Estas clases existen en carpetas como "handlers/" e "items/" dentro del paquete
    # principal, y aunque son parte del mismo proyecto, deben importarse porque están en paquetes diferentes.

    import com.Chagui68.weaponsaddon.handlers.MachineGunHandler;
    import com.Chagui68.weaponsaddon.items.BombardmentTerminal;
    import com.Chagui68.weaponsaddon.items.MachineGun;
    import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
    import com.Chagui68.weaponsaddon.items.MilitaryComponents;
    import com.Chagui68.weaponsaddon.items.machines.TerminalClickHandler;


/

    # El segundo grupo importa clases de la API de Slimefun4, que es la dependencia
    # principal del addon. "SlimefunAddon" es la interfaz obligatoria que identifica
    # este plugin como un addon de Slimefun, "NestedItemGroup" y "SubItemGroup" permiten
    # crear la estructura de categorías anidadas que aparece en la guia del Slimefun,
    # "CustomItemStack" facilita crear items con nombres y descripciones personalizadas
    # con códigos de color, y "Config" proporciona funcionalidad de configuración
    # (aunque en este código no se usa activamente, está preparado para futuras implementaciones).

    import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
    import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
    import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
    import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
    import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

/


    # El tercer grupo importa clases fundamentales de la API de Bukkit/Spigot,
    # que es la base de todos los plugins de Minecraft. "JavaPlugin" es la clase
    # padre obligatoria que todos los plugins deben extender para que el servidor
    # los reconozca, "Material" es el enum que contiene todos los materiales de 
    # Minecraft (como "NETHERITE_SWORD", "DIAMOND", etc.) necesarios para definir
    # íconos de categorías, y "NamespacedKey" crea identificadores únicos en formato
    # "plugin:id" que previenen conflictos con otros plugins. Sin estos imports, el 
    # código no compilaría porque Java no sabría dónde encontrar estas clases cuando se
    # mencionan en métodos como "onEnable()".

    import org.bukkit.Material;
    import org.bukkit.NamespacedKey; 
    import org.bukkit.plugin.java.JavaPlugin;

/

## Aclaración importante:

    # Es importante entender que estos imports son exclusivos de este archivo y reflejan
    # únicamente lo que "WeaponsAddon.java" usa directamente. Otros archivos como "MachineGun.java"
    # o "MachineGunHandler.java" tienen sus propios bloques de imports completamente independientes 
    # con clases como "SlimefunItem", "PlayerInteractEvent" o "ItemStack" que esta clase principal 
    # no necesita porque no maneja esos detalles de implementación, solamente coordina el registro 
    # inicial llamando a los métodos ".register()" de cada componente.

/

        public class WeaponsAddon extends JavaPlugin implements SlimefunAddon {

    Esta línea define la clase principal del plugin con dos componentes críticos.
    extends JavaPlugin hace que esta clase herede de la clase base de Bukkit, 
    lo cual es obligatorio para que el servidor reconozca el archivo como un plugin
    válido y le proporciona acceso a métodos esenciales como onEnable(), getLogger() y 
    getServer().

/*

    "implements SlimefunAddon" implementa la interfaz requerida por Slimefun4 
    que identifica este plugin como un addon oficial, obligando a implementar los métodos 
    getJavaPlugin() y getBugTrackerURL(), y permitiendo que Slimefun gestione el addon 
    automáticamente, mostrándolo en comandos como /sf versions.

/

        private static WeaponsAddon instance;

    Esta línea de código se usa para asegurar que exista una sola instancia del plugin
    y que esa instancia pueda ser utilizada desde cualquier parte del proyecto. 
    En lugar de crear múltiples objetos del plugin, todos usan la misma referencia.

/*


    La palabra static indica que la variable pertenece a la clase y no a un objeto
    específico. Gracias a esto, otras clases pueden acceder a la instancia del plugin
    sin necesidad de crear nuevos objetos.

/*

    El modificador private evita que la variable sea modificada directamente desde
    fuera de la clase. Esto ayuda a mantener el control y evita errores que podrían
    afectar el funcionamiento del plugin.

/*

    La variable **instance** se inicializa cuando el plugin se activa, normalmente
    dentro del método "onEnable()", asignándole el valor "this". De esta forma se
    guarda la referencia al plugin principal.

/*

    Luego, mediante un método como "getInstance()", otras clases pueden obtener esa 
    referencia para crear "NamespacedKey", registrar ítems o eventos, acceder al logger
    o leer la configuración del servidor. Esto hace que el código sea más ordenado y fácil de mantener.

/

        @Override
        public void onEnable() {
            instance = this;

    La anotación @Override indica que estamos sobrescribiendo un método heredado de "JavaPlugin",
    ayudando a prevenir errores de escritura porque el compilador verifica que el método realmente
    exista en la clase padre. El método "onEnable()" es el punto de entrada automático que Bukkit
    ejecuta cuando el servidor inicia o carga el plugin, funcionando como un constructor especializado
    donde debes inicializar todos los componentes del addon. La línea "instance = this" guarda la
    referencia de la instancia actual en la variable estática, donde "this" representa el objeto
    "WeaponsAddon" que Bukkit acaba de crear, permitiendo que otras clases accedan a este plugin
    mediante "WeaponsAddon.getInstance()" sin necesidad de pasar referencias manualmente.

/

## Importante

            Config config = new Config(this);

    # Esta línea crea un objeto de configuración usando la clase "Config" de Slimefun, pasando "this"
    # (el plugin actual) como parámetro para que pueda acceder a archivos de configuración en la carpeta
    # del plugin. Sin embargo, esta variable no se utiliza en ningún lugar del código posterior, lo que 
    # significa que está preparada para futuras implementaciones donde podrías leer opciones de un archivo
    # "config.yml" (como activar/desactivar items, ajustar valores de daño, configurar consumos de energía), 
    # pero actualmente no tiene funcionalidad y puede ser eliminada sin afectar el funcionamiento del addon.
    # Si quisieras usarla, podrías hacer cosas como "config.getBoolean("enable-machine-gun")" para permitir que
    # los administradores personalicen el addon.

/

            NamespacedKey mainKey = new NamespacedKey(this, "military_arsenal");

    # Esta línea crea un identificador único para la categoría principal del addon usando el sistema
    # de "NamespacedKey" de Minecraft/Bukkit. El primer parámetro "this" representa el plugin actual 
    # (WeaponsAddon), y el segundo parámetro "military_arsenal" es el ID específico de la categoría, 
    # resultando en el identificador completo "weaponsaddon:military_arsenal" que previene conflictos 
    # con otros plugins que pudieran tener categorías con nombres similares. Este key es fundamental
    # porque Slimefun usa estos identificadores internamente para guardar datos, rastrear progreso de 
    # jugadores, y referenciar items en la base de datos, garantizando que aunque otro plugin tenga una
    # categoría llamada "arsenal", no habrá conflictos porque los namespaces son diferentes 
    # (otroplugin:arsenal | weaponsaddon:military_arsenal).

/*

            CustomItemStack mainItem = new CustomItemStack(
                    Material.NETHERITE_SWORD,
                    "&4⚔ &c&lMILITARY ARSENAL",
                    "",
                    "&7Advanced military equipment",
                    "&7and tactical systems",
                    "",
                    "&e▶ Click to open categories",
                    "&8⇨ Main Category"
            );

    Esta sección crea el item visual que los jugadores verán en el menú de la guia del Slimefun usando
    la clase CustomItemStack de Slimefun. El primer parámetro Material.NETHERITE_SWORD define el material
    base del item (En este caso una espada de Netherite), mientras que el segundo parámetro es el nombre con códigos de
    color de Minecraft donde :

    - &4 produce rojo oscuro 
    - &c produce rojo brillante 
    - &l aplica negrita

    Los parámetros siguientes son líneas de "lore" (descripción del item):
    las líneas vacías "" crean espacios visuales para separar secciones, 
    &7 produce texto gris claro para las descripciones,
    &e produce amarillo para instrucciones importantes,
    &8 produce gris oscuro para metadata.

    # Este item no tiene funcionalidad de juego real, es puramente cosmético para la interfaz de Slimefun Guide.

---
### 🎨 Referencia de Colores (&)
    
| Código | Resultado 

| `&0`   | Negro 

| `&1`   | Azul Oscuro 

| `&2`   | Verde Oscuro 

| `&3`   | Cian Oscuro 

| `&4`   | Rojo Oscuro 

| `&5`   | Púrpura 

| `&6`   | Dorado 

| `&7`   | Gris Claro 

| `&8`   | Gris Oscuro 

| `&9`   | Azul Brillante 

| `&a`   | Verde Lima 

| `&b`   | Celeste 

| `&c`   | Rojo Brillante 

| `&d`   | Rosa / Magenta

| `&e`   | Amarillo 

| `&f`   | Blanco 

| `&l`   | **Negrita** 

| `&m`   | ~~Tachado~~ 

| `&n`   | <u>Subrayado</u> 

| `&o`   | *Cursiva* 

| `&k`   | Texto Mágico 

| `&r`   | **Reset** (Quita colores) 

    **Nota:** Siempre coloca el color **antes** que el formato (Ejemplo: `&c&l` para Rojo Negrita).

    ---
    ### ⚔️ Lógica de Daño y Atributos

    Cuando creamos armas personalizadas para jefes, usamos un sistema de **Daño Combinado** para que el jugador vea el total real de daño.

    1. **HIDE_ATTRIBUTES**: Usamos esta "bandera" (`ItemFlag`) para ocultar el texto azul de Minecraft (`+15 Attack Damage`). Esto nos permite limpiar la interfaz y evitar confusiones.
    
    2. **Cálculo de Daño Real**:
       Para mostrar el daño total en el Lore (la descripción), sumamos tres valores:
       - **Daño Base**: El daño natural del material (Madera: 4, Oro: 4, Hierro: 6, Diamante: 7, Netherite: 8).
       - **Daño Extra**: El valor que añadimos mediante `AttributeModifier`.
       - **Bono de Filo (Sharpness)**: Los encantamientos de Filo suman daño extra según la fórmula: `(0.5 * Nivel) + 0.5`.
    
    3. **Resultado Final**:
       De esta manera, si una espada de oro (4) tiene un modificador de +15 y Filo III (+2), el código mostrará automáticamente **"Daño Total: 21.0"**, que es exactamente lo que el arma quitará de vida.
           
/*

        NestedItemGroup mainGroup = new NestedItemGroup(mainKey, mainItem, 2);

    # Esta línea instancia la categoría principal usando NestedItemGroup, que es un tipo especial de categoría 
    # de Slimefun diseñada para contener subcategorías dentro de ella. El primer parámetro "mainKey" es el identificador
    # único creado anteriormente, el segundo "mainItem" es el ícono visual que acabamos de definir, y el tercer parámetro "2"
    # es el tier o nivel de la categoría que controla en qué "página" del Slimefun Guide aparece (tier 1 para categorías
    # básicas de inicio, tier 2 para contenido intermedio, tier 3 para avanzado/endgame). Al usar "NestedItemGroup" en 
    # lugar de "ItemGroup" normal, esta categoría puede actuar como un "folder" que cuando un jugador hace clic en ella,
    # muestra las tres subcategorías (Components, Weapons, Machines) en lugar de mostrar items directamente, creando una
    # navegación organizada y jerárquica.

/*

            NamespacedKey componentsKey = new NamespacedKey(this, "military_components");
            CustomItemStack componentsItem = new CustomItemStack(
                    Material.REDSTONE_BLOCK,
                    "&6⚙ &eMilitary Components",
                    "",
                    "&7Basic materials for crafting",
                    "&7military equipment",
                    "",
                    "&8⇨ Level 1 Components"
            );
            SubItemGroup componentsGroup = new SubItemGroup(componentsKey, mainGroup, componentsItem);


    Este bloque crea la primera subcategoría para componentes militares. El "NamespacedKey" con ID "military_components"
    la identifica de forma única. El "CustomItemStack" define el ícono que aparecerá en el menú. La diferencia crucial
    es usar "SubItemGroup" en lugar de "NestedItemGroup", indicando que esta categoría contiene items directamente, 
    no más subcategorías. El segundo parámetro "mainGroup" establece que esta subcategoría pertenece a "MILITARY ARSENAL".


/*

            NamespacedKey weaponsKey = new NamespacedKey(this, "military_weapons");
            CustomItemStack weaponsItem = new CustomItemStack(
                    Material.DIAMOND_SWORD,
                    "&c⚔ &4Military Weapons",
                    "",
                    "&7Advanced combat equipment",
                    "&7and ammunition",
                    "",
                    "&8⇨ Tier 2 Weapons"
            );
            SubItemGroup weaponsGroup = new SubItemGroup(weaponsKey, mainGroup, weaponsItem);

    Segunda subcategoría que agrupa el equipamiento de combate. Sigue el mismo patrón: crea un identificador único,
    define el ícono con nombre y descripción, y la vincula a mainGroup. Aquí se registrarán posteriormente 
    la ametralladora y su munición.

/*

            NamespacedKey machinesKey = new NamespacedKey(this, "military_machines");
            CustomItemStack machinesItem = new CustomItemStack(
                    Material.OBSERVER,
                    "&4💣 &cMilitary Machines",
                    "",
                    "&7Automated warfare systems",
                    "&7and tactical devices",
                    "",
                    "&8⇨ Tier 2 Machines"
            );
            SubItemGroup machinesGroup = new SubItemGroup(machinesKey, mainGroup, machinesItem);

    Tercera subcategoría para máquinas automatizadas. Mismo patrón de creación que las anteriores.
    Aquí se registrará el Terminal de Bombardeo.
    
/*


            mainGroup.register(this);

    Registra la categoría principal en Slimefun, haciéndola visible en /sf guide. 
    Al registrar un "NestedItemGroup", automáticamente incluye todas sus subcategorías vinculadas, 
    por lo que no necesitas registrar cada SubItemGroup por separado.

/*


            getLogger().info("Registering Military Components...");
            MilitaryComponents.register(this, componentsGroup);

    El mensaje de logging rastrea el progreso de carga en consola. La segunda línea llama al método
    estático register() de MilitaryComponents, pasándole el plugin y la subcategoría. Esto delega la
    creación de los 6 componentes a esa clase especializada.

/*


            getLogger().info("Registering Military Weapons...");
            MachineGunAmmo.register(this, weaponsGroup);
            MachineGun.register(this, weaponsGroup);

    Registra primero la munición y después el arma. 
    El orden permite que dependencias se registren antes que los items que las usan.
    Ambos se asignan a weaponsGroup.

/*

            getLogger().info("Registering Military Machines...");
            BombardmentTerminal.register(this, machinesGroup);

    Registra el Terminal de Bombardeo delegando toda la lógica compleja a 
    BombardmentTerminal.java. Esta separación mantiene el archivo principal limpio.

/*

            getServer().getPluginManager().registerEvents(new MachineGunHandler(), this);

    Registra el "listener" de eventos que maneja la funcionalidad de la ametralladora. 
    "MachineGunHandler" contiene métodos "@EventHandler" que detectan cuando los jugadores 
    usan el arma. Sin este registro, el item existiría pero no tendría funcionalidad.

/*


            TerminalClickHandler.setPlugin(this);
            getServer().getPluginManager().registerEvents(new TerminalClickHandler(), this);

    "setPlugin()" pasa la instancia del plugin al handler porque necesita ejecutar tareas 
    asíncronas para los delays de bombardeo. La segunda línea registra el "listener" que detecta 
    clics en la GUI del terminal.

/


            getLogger().info("========================================");
            getLogger().info("WeaponsAddon enabled successfully!");
            getLogger().info("Main Category: 1 | Subcategories: 3");
            getLogger().info("Total Items: 9");
            getLogger().info("========================================");

    Banner de confirmación en consola indicando carga exitosa con estadísticas: 1 categoría principal, 
    3 subcategorías, 9 items totales. Facilita verificar que todo se cargó correctamente.

/

        @Override
        public void onDisable() {
            getLogger().info("WeaponsAddon disabled!");
        }

    Se ejecuta cuando el servidor se detiene o el plugin se desinstala. 
    Solo imprime confirmación porque Bukkit y Slimefun manejan automáticamente 
    la limpieza de items y "listeners".

/

        public static WeaponsAddon getInstance() {
            return instance;
        }

    Proporciona acceso global al plugin. Es "static" para llamarlo directamente sin crear objetos.
    Otras clases lo usan para crear "NamespacedKey", registrar items, o acceder al logger.

/

        @Override
        public JavaPlugin getJavaPlugin() {
            return this;
        }

    Método obligatorio de SlimefunAddon. Slimefun lo usa internamente para acceder a funcionalidades
    de Bukkit. Devuelve "this" porque esta clase ya extiende "JavaPlugin".

/

        @Override
        public String getBugTrackerURL() {
            return "https://github.com/Chagui68/Military-Arsenal-addon-for-Slimefun4/issues";
        }

    También obligatorio de SlimefunAddon. Devuelve la URL donde reportar bugs. 
    Slimefun la muestra en /sf versions y en mensajes de error.    

**

## Añadir Efectos de Poción a Entidades

    Para añadir efectos de poción a una entidad (como un Jefe o un Mob personalizado), 
    se utiliza el método **addPotionEffect()**. Este método se aplica directamente 
    sobre el objeto de la entidad (por ejemplo, un Skeleton, Zombie, etc.).

/

    # Código de ejemplo para aplicar un efecto:
    
    boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 1));

/

    # Explicación de los parámetros de PotionEffect:

    1. **PotionEffectType**: Es el tipo de efecto que quieres aplicar 
       (SPEED, INCREASE_DAMAGE, INVISIBILITY, etc.).
    2. **Duration**: La duración en "ticks" (20 ticks = 1 segundo). 
       Usar un número muy grande como 999999 hace que el efecto sea prácticamente infinito.
    3. **Amplifier**: El nivel del efecto (0 es nivel I, 1 es nivel II, etc.).

/

    # Ejemplo avanzado con partículas ocultas:
    
    boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, false, false));

    # El cuarto parámetro (false) indica si es un efecto de ambiente.
    # El quinto parámetro (false) indica si se deben mostrar partículas. 
    # Al ponerlo en false, el mob tendrá el efecto pero NO soltará burbujitas de colores.

**

## MilitaryMobHandler: Gestión de Mobs Militares

    Esta clase es el "armero" y "reclutador" de tu plugin. Se encarga de dos cosas:
    detectar cuando aparece un mob en el mundo y ponerle el equipo militar.

/

    # 1. El Evento de Spawn (onSpawn)
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) { ... }
    
    # Aquí es donde ocurre la magia del "Spawn Natural". 
    # El código revisa si el mob es un Esqueleto y si apareció de forma natural.
    # Si quieres añadir una NUEVA entidad natural, este es el lugar.

/

    # 2. Cómo añadir una nueva entidad con Spawn Natural:
    
    # Dentro del método onSpawn, verás un "roll" (un dado virtual):
    double roll = random.nextDouble();
    
    # Para añadir tu entidad, simplemente añade una probabilidad:
    if (roll < 0.10) { // 10% de probabilidad
        equipTuNuevaEntidad(skeleton);
    } else if (roll < 0.50) { // 50% de probabilidad
        equipEliteRanger(skeleton);
    }

/

    # 3. Los Métodos de Equipamiento (equipHeavyGunner, etc.)
    
    # Estos métodos sirven para "transformar" un mob normal en uno militar:
    - boss.setCustomName(...): Cambia el nombre visual.
    - boss.getAttribute(...).setBaseValue(...): Cambia vida, daño o velocidad.
    - boss.addScoreboardTag(...): Le pone una "etiqueta" invisible para que 
      la IA (BossAIHandler) sepa qué disparos o habilidades usar.
    - equip.setHelmet/Chestplate(...): Le pone la armadura.

/

    # 4. Probabilidades y Dificultad
    
    # El MilitaryMobHandler también ajusta la fuerza según la dificultad 
    # del servidor (EASY, NORMAL, HARD) para que los enemigos no sean 
    # imposibles para jugadores nuevos pero sí un reto para veteranos.

/

    # 5. Dónde se definen los Nombres

    # El NOMBRE DE LA CLASE se define al principio del archivo:
    public class MilitaryMobHandler { ... }
    # Recuerda: En Java, el nombre de la clase DEBE ser idéntico al 
    # nombre del archivo (MilitaryMobHandler.java).

    # El NOMBRE DE LA ENTIDAD (el que ven los jugadores) se define 
    # dentro de los métodos de equipamiento usando:
    boss.setCustomName(ChatColor.RED + "Nombre del Mob");
    boss.setCustomNameVisible(true); // Hace que el nombre se vea siempre

/

    # 5.1 Caso Práctico: Elite Killer
    # Para el "Elite Killer", usamos un Zombie. El código se divide en:
    # 1. El import de Zombie al principio del archivo.
    # 2. La lógica en onSpawn para detectar EntityType.ZOMBIE.
    # 3. El método equipEliteKiller que define su armadura blanca y 
    #    daño extremo (instakill).

**

    # 6. Control de Velocidad (Atributos vs Pociones)

    # Tienes dos formas de hacer que un mob sea extremadamente lento:

    # A) Por ATRIBUTOS (Cambio real del mob):
    # La velocidad normal es 0.25. Para hacerlo muy lento, usa valores bajos:
    boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.05);

    # B) Por EFECTO DE POCIÓN (Como guía o penalización):
    # Cada nivel de SLOWNESS (Lentitud) reduce la velocidad un 15%.
    # - Nivel 1 (amplificador 0): -15% de velocidad.
    # - Nivel 6 (amplificador 5): -90% de velocidad (Casi estático).
    # - Nivel 255: ¡CONGELADO TOTAL! No se puede mover.

/

    # 7. Equivalencia Matemática (Convertir Pociones a Atributos)

    # Si quieres que un mob tenga la velocidad de "Lentitud 3" de forma permanente:
    # 1. Lentitud 3 reduce un 45% (15% x 3).
    # 2. Solo queda el 55% de la velocidad original.
    # 3. Base (0.25) x 0.55 = 0.1375.

    # Código equivalente a Lentitud 3:
    boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1375);

    # Guía rápida de conversión (Base 0.25):
    # - Lentitud 1 -> 0.2125
    # - Lentitud 2 -> 0.1750
    # - Lentitud 3 -> 0.1375
    # - Lentitud 4 -> 0.1000

/

    # 7.1 Velocidad (Speed) comparado con Atributos
    
    # Cada nivel de Speed (Velocidad) AUMENTA un 20% la base:
    # - Velocidad 1 -> 0.30  (+20%)
    # - Velocidad 2 -> 0.35  (+40%)
    # - Velocidad 3 -> 0.40  (+60%)
    # - Velocidad 4 -> 0.45  (+80%)

    # Consejo: Un valor de 0.35 (Velocidad 2) ya es bastante rápido 
    # para un mob y lo hace difícil de esquivar.

/

    # Consejo: Si quieres que sea un poco más rápido que un caracol 
    # pero más lento que un humano, 0.13 es el valor perfecto.

/

    # 8. Límites de Daño (GENERIC_ATTACK_DAMAGE)

    # El valor máximo técnico en Minecraft moderno es 2048.0.
    # Pero cuidado: ¡Eso mataría a cualquier jugador de un solo golpe!

    # Guía de Daño (En puntos de daño, 2 puntos = 1 corazón):
    # - 2.0  -> 1 Corazón (Como un golpe de mano)
    # - 10.0 -> 5 Corazones (Como una espada de diamante)
    # - 20.0 -> 10 Corazones (Mata a un jugador sin armadura)
    # - 40.0 -> 20 Corazones (Mata a un jugador con armadura decente)

    # El límite que recomendamos NO pasar es 100.0, a menos que sea 
    # un jefe final extremadamente difícil (como el Wither o el Warden).

    # Código de ejemplo para un daño letal:
    boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(18.0);

/

    # 8.2 Atributos de Empuje (Knockback)

    # Existen dos atributos principales para controlar el empuje:

    # A) GENERIC_KNOCKBACK_RESISTANCE (Resistencia al recibir golpes)
    # Define qué tanto se mueve el mob cuando tú le pegas.
    # - 0.0 -> Empuje normal (vuela como un mob común).
    # - 1.0 -> 100% de resistencia (es una "pared", no se mueve nada).
    boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);

    # B) GENERIC_ATTACK_KNOCKBACK (Fuerza de empuje al atacar)
    # Define qué tan lejos lanza el mob al jugador cuando le pega.
    # - 0.0 -> Empuje normal.
    # - 1.0 -> Empuje fuerte (como el encantamiento Knockback I).
    # - 5.0 -> ¡Lanza al jugador por los aires!
    boss.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(1.5);

/

    # 8.3 Refuerzos de Zombie (ZOMBIE_SPAWN_REINFORCEMENTS)

    # Este es un atributo exclusivo de los Zombies. 
    # Controla la probabilidad de que el zombie "pida ayuda" 
    # y aparezcan otros zombies cerca cuando recibe daño.

    # - 0.0 -> Nunca aparecen refuerzos.
    # - 0.5 -> 50% de probabilidad de generar un refuerzo al ser golpeado.
    # - 1.0 -> ¡Casi siempre aparecerán refuerzos!

    # Código de ejemplo:
    zombie.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.1);

    # Nota: Es una buena forma de crear una "horda" sin necesidad 
    # de programar una IA compleja de invocación.

/

    # 8.3.1 Personalizar Refuerzos

    # Por defecto, los refuerzos son zombies normales. 
    # Para que sean "Custom", debemos permitir el motivo 
    # de spawn REINFORCEMENTS en nuestro Handler.

    # 1. En onSpawn, añadimos el filtro:
    if (e.getSpawnReason() == SpawnReason.REINFORCEMENTS) {
        equipPusher((Zombie) e.getEntity()); // Todos los refuerzos serán Pushers
    }

/

    # 8.3.2 ¿Vanilla Reinforcements o IA Custom? (IMPORTANTE)

    # El atributo vanilla (8.3) es "ciego": si un zombie normal 
    # llama a refuerzos, nuestro plugin los convertirá en custom 
    # también, porque no sabe quién los llamó.

    # Recomendación:
    # 1. Si quieres CONTROL TOTAL (ej: que solo el Elite Killer invoque), 
    #    NO uses el atributo vanilla. Usa la IA Custom (Sección 10).
    # 2. Si quieres CAOS total (que cualquier zombie pueda llamar a 
    #    tus entidades custom), usa el atributo vanilla.

/

    # 8.1 Daño Cero (Entidades Pacíficas)
    
    # Si pones el valor en 0.0, la entidad NO hará daño con sus golpes básicos.
    boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.0);

    # ¡CUIDADO!: Si le das una ESPADA o cualquier ARMA a la entidad, 
    # el daño del arma se SUMARÁ al valor base. Si quieres que no 
    # haga daño de ninguna forma, asegúrate de que no tenga armas 
    # o de que sus armas sean ítems decorativos sin daño.

/

    # 9. Forzar Entidades Bebé (Mini Mobs)

    # Si quieres que un Zombie, Piglin o entidad similar sea 
    # forzadamente un "Mini" (bebé), usa este método:
    zombie.setBaby(true);

    # Nota: Las entidades bebé son naturalmente más rápidas y 
    # tienen una "hitbox" más pequeña, lo que las hace mucho 
    # más difíciles de golpear para los jugadores.

/

    # 10. Invocación detrás del Jugador (Vectores)
 
     # Para que un mini-jefe invoque a un grupo de aliados (como 3 "Pushers") 
     # justo detrás del jugador, usamos matemáticas de vectores en un bucle:
 
     # 1. Obtenemos la ubicación y dirección del jugador.
     # 2. Multiplicamos la dirección por un valor negativo (atrás).
     # 3. Sumamos ese vector a la ubicación original.
 
     # Código conceptual (3 invocaciones):
     for (int i = 0; i < 3; i++) {
        Location playerLoc = player.getLocation();
        Vector detras = playerLoc.getDirection().multiply(-2); // 2 bloques atrás
        Location spawnLoc = playerLoc.clone().add(detras);
        player.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
     }

/

    # 11. Tiempos de Espera (Cooldowns) con Metadata

    # usamos "Metadata" para guardar el tiempo del próximo uso:

    # 1. Guardar el tiempo:
    mob.setMetadata("cooldown", new FixedMetadataValue(plugin, System.currentTimeMillis() + 30000));

    # 2. Comprobar el tiempo:
    if (mob.hasMetadata("cooldown")) {
        long fin = mob.getMetadata("cooldown").get(0).asLong();
        if (System.currentTimeMillis() < fin) return; // Aún esperando
    }

**
 
 ## 12. Drops Personalizados (Botín al morir)
 
     # Tienes dos formas de hacer que una entidad suelte un ítem específico:
 
 /
 
     # A) Método Automático (Probabilidad del Equipo)
     
     # Si el mob ya TIENE el ítem en la mano o armadura, puedes decidir 
     # si lo suelta al morir con una probabilidad (0.0 a 1.0):
     
     EntityEquipment equip = boss.getEquipment();
     equip.setItemInMainHandDropChance(0.05f); // 5% de probabilidad de soltar su arma
     equip.setHelmetDropChance(1.0f);          // 100% de probabilidad (Siempre lo suelta)
     equip.setBootsDropChance(0.0f);           // 0% de probabilidad (NUNCA lo suelta)

/

     # 12.1 Errores Comunes en Drop Chances:
     
     # 1. Confundir SET con GET:
     # - INCORRECTO: equip.getBootsDropChance(0.0f); (Intentar usar el "obtenedor" para "poner")
     # - CORRECTO:   equip.setBootsDropChance(0.0f); (Usar el "ponedor")
     
     # 2. Usar Booleans (true/false) en lugar de Floats (números):
     # - INCORRECTO: equip.setBootsDropChance(false);
     # - CORRECTO:   equip.setBootsDropChance(0.0f); // 0.0 es como "falso" (no cae)
 
 /
 
      # 12.2 ¿Antes o después de definir la armadura?
      
      # En Java, puedes poner la probabilidad antes o después, pero es 
      # mucho mejor ponerla DESPUÉS por orden lógico:
      
      # 1. Pones la bota (Define el ítem)
      equip.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
      
      # 2. Defines su probabilidad (Define qué pasa con ese ítem)
      equip.setBootsDropChance(0.0f);
 
 /
 
     # B) Método Manual (EntityDeathEvent)
     
     # Si quieres que suelte un ítem que NO tiene puesto (como un diamante 
     # o un componente), debes usar el evento de muerte:
     
     @EventHandler
     public void onDeath(EntityDeathEvent e) {
         // 1. Identificar a nuestra entidad por su TAG
         if (e.getEntity().getScoreboardTags().contains("EliteKiller")) {
             
             // 2. Limpiar los drops normales si quieres (Opcional)
             e.getDrops().clear(); 
             
             // 3. Añadir el ítem específico al botín
             ItemStack recompensa = new ItemStack(Material.NETHERITE_INGOT);
             e.getDrops().add(recompensa);
             
             // 4. (Opcional) Soltar un ítem de Slimefun
             // e.getDrops().add(MilitaryComponents.STEEL_PLATE.clone());
         }
     }
 
 ## 13. Lógica de Probabilidades y Exclusividad
 
     # En el código de spawn, usamos un "dado" virtual (`roll`) y una cadena 
     # de decisiones (`if / else if`). Es vital entender el orden de prioridad:
 
 /
 
     # 1. El Dado Único:
     double roll = random.nextDouble(); 
     # Se genera UN solo número por cada mob que aparece. Si sale 0.05, 
     # ese número se usará para todas las comparaciones de ese mob.
 
 /
 
     # 2. La Exclusividad (Prioridad):
     # Si usas `else if` para el mismo tipo de entidad, el primero que se 
     # cumpla "anula" a los demás.
     
     if (roll < 0.01) { // 1%
         equipEliteKiller(zombie);  // Gana el más raro
     } 
     else if (roll < 0.10) { // 10%
         equipPusher(zombie);       // Solo ocurre si el primero falló
     }
 
     # ¿Qué pasa aquí?
     # - Si el dado sale 0.005: Se convierte en Elite Killer y se detiene (pasa del Pusher).
     # - Si el dado sale 0.05: NO es Elite Killer, pero SÍ es Pusher.
     # - Si el dado sale 0.20: No es ninguno, se queda como zombie normal.
 
 /
 
     # 3. Importancia del Orden:
     # Siempre pon las probabilidades MÁS PEQUEÑAS (los mobs más raros) 
     # al principio de la cadena. Si pusieras el 50% primero y el 1% después, 
     # el del 1% casi nunca aparecería porque el del 50% "absorbería" su rango.
 
 /
 
     # 4. El error de "Doble Entidad":
     # Si usas `if` seguidos (sin el `else`), el código intentaría ponerle 
     # DOS equipaciones al mismo mob si el dado es bajo, causando errores visuales 
     # o reemplazando el nombre del anterior. Por eso usamos `else if`.
 
 ## 14. Bloques, Sangría y Anidación (Estructura)
 
     # La "sangría" (esos espacios a la izquierda) no son solo por estética; 
     # le dicen a Java (y a ti) qué código pertenece a qué "habitación".
 
 /
 
     # 1. Las Llaves `{ }` son Habitaciones:
     # Todo lo que esté dentro de `{` y `}` pertenece a la condición de arriba.
     
     if (e.getEntityType() == EntityType.ZOMBIE) {
         // --- Estás en la habitación "ZOMBIES" ---
         // Todo lo que escribas aquí SOLO afecta a zombies.
         
         if (roll < 0.1) { 
             // --- Estás en una sub-habitación "ELITE" ---
             // Solo entras aquí si eres Zombie Y el dado es < 0.1
         }
     }
 
 /
 
     # 2. El Error de la "Habitación Cerrada":
     # Si cierras la llave `}` de los Zombies y luego intentas preguntar 
     # otra cosa sobre Zombies con un `else if`, Java ya "salió" de esa lógica.
     
     if (esZombie) { ... } 
     else if (esZombie) { ... } // ¡ERROR LÓGICO! 
     
     # El segundo `else if` nunca se ejecutará porque el primero ya 
     # "atrapó" a todos los zombies y cerró la puerta.
 
 /
 
     # 3. La Sangría Correcta:
     # Cada vez que abras una llave `{`, el siguiente código debe llevar 
     # 4 espacios extra hacia la derecha. Esto ayuda a ver visualmente 
     # dónde termina una decisión y dónde empieza otra.
 
 /
 
     # 4. Anidación vs Cadenas:
     # - CADENA (if / else if): Eliges UNA de varias opciones diferentes (Zombie O Esqueleto).
     # - ANIDACIÓN (if dentro de if): Filtras más a fondo (Es Zombie -> Y es un Zombie Raro).
     # Para tus mobs, lo correcto es: 
     # 1. Preguntar qué bicho es (Cadena).
     # 2. Dentro de ese bicho, preguntar qué variante es (Anidación).
 
 ## 15. Ejemplo: Dos variantes del mismo bicho (El "Combo")
 
     # Si quieres que un Zombie pueda ser o bien "Elite" o bien "Pusher", 
     # la forma final y correcta de escribirlo para que Java no se confunda es esta:
 
 /
 
     # Código Final Consolidado:
     
     if (e.getEntityType() == EntityType.ZOMBIE) {
         Zombie zombie = (Zombie) e.getEntity();
         double roll = random.nextDouble(); // El dado se tira AQUÍ
 
         if (roll < 0.05) { 
             // 1. ¿Es el 5% más raro? -> Se vuelve ELITE KILLER
             equipEliteKiller(zombie);
         } 
         else if (roll < 0.15) { 
             // 2. ¿No fue Elite pero es el siguiente 10%? -> Se vuelve PUSHER
             // (Este rango va de 0.05 a 0.15)
             equipPusher(zombie);
         }
         
         // Si el dado es 0.16 o más, no entra en ningún 'if' y se queda normal.
     }
 
 /
 
     # ¿Por qué esta es la "Opción Ganadora"?
     
     # 1. CPU Eficiente: Solo preguntas una vez si es un Zombie.
     # 2. Sin Conflictos: Un Zombie nunca intentará tener dos nombres a la vez.
     # 3. Rareza Real: El Elite Killer tiene prioridad absoluta por estar arriba.
     # 4. Limpieza: Todo lo relacionado con Zombies vive en el mismo bloque `{ }`.

/

    # 16. Bloques como Cascos (Cabezas Personalizadas)

    # En Minecraft, puedes poner casi cualquier bloque o ítem en el 
    # espacio del casco (`setHelmet`) de un mob. Esto es ideal para 
    # crear mobs con apariencias únicas:

    # 1. Bloques de Cristal (Cápsulas):
    equip.setHelmet(new ItemStack(Material.YELLOW_STAINED_GLASS));

    # 2. Bloques de Construcción:
    equip.setHelmet(new ItemStack(Material.TNT)); // ¡Un mob con cabeza de TNT!

    # 3. Cabezas de Jugadores (Skins):
    # Se usa para poner caras humanas o decoraciones detalladas.

    # Nota: Los bloques en la cabeza no proporcionan puntos de 
    # armadura (protección) por sí solos, son puramente estéticos.

/

    # 16.1 Protección Solar Automática

    # ¡Buenas noticias!: En Minecraft, CUALQUIER ítem que pongas 
    # en el espacio del casco protege a los Zombies y Esqueletos 
    # de quemarse con el sol.
    
    # - Si usas cristal, bloque de oro o incluso un palo, el mob 
    #   NO se quemará durante el día.
    # - A diferencia de los cascos normales de armadura, los bloques 
    #   no tienen durabilidad, por lo que nunca se romperán por el sol.

/

    # 17. Saqueo (Looting) vs Drop Chance

    # El encantamiento Saqueo (Looting) SÍ afecta al equipamiento:
    
    # 1. El Incremento:
    # Por defecto, cada nivel de Looting añade un 1% (0.01) a la 
    # probabilidad de que un mob suelte su armadura o arma.
    
    # 2. El Riesgo (0.0f no siempre es 0%):
    # Si pones `setBootsDropChance(0.0f)`, un jugador con Looting III 
    # tendría un 3% de probabilidad de conseguir las botas.
    
    # 3. Solución (Garantizar 0%):
    # Si quieres que sea IMPOSIBLE que el ítem caiga (incluso con Looting), 
    # la mejor forma es usar el `EntityDeathEvent` y limpiar los drops

/

    # 18. Propiedades Avanzadas de Ítems (ItemMeta)

    # Para cambiar cosas como el nombre, el lore (descripción) o hacer que 
    # un ítem sea irrompible, usamos el `ItemMeta`:

    # 1. Obtener el Meta del ítem:
    ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta meta = item.getItemMeta();

    if (meta != null) {
        # 2. Hacerlo IRROMPIBLE:
        # Evita que el arma se gaste o se rompa (útil para jugadores).
        meta.setUnbreakable(true);

        # 3. Cambiar NOMBRE y LORE:
        meta.setDisplayName(ChatColor.GOLD + "Espada del Rey");
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Una reliquia antigua...");
        meta.setLore(lore);

        # 4. OCULTAR Atributos (Hide Flags):
        # Sirve para que no se vea el texto de "+7 Daño" o "Irrompible".
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        # 5. Guardar los cambios de vuelta en el ítem:
        item.setItemMeta(meta);
    }

/

# 19. Orden Lógico de Equipamiento

    # 1. Creamos el ítem básico
    ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
    
    # 2. Modificamos sus propiedades avanzadas (Nombre, Irrompible)
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.GOLD + "Nombre");
        item.setItemMeta(meta);
    }
    
    # 3. Se lo entregamos al mob
    equip.setItemInMainHand(item);

/*

# 20. Ejemplo Arma

    public static void equipWardenGeneral(Skeleton boss) {
        boss.setCustomName(ChatColor.DARK_PURPLE + "☣ Warden General ☣");
        boss.setCustomNameVisible(true);
        
        boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500.0);
        boss.setHealth(500.0);
        boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.20);
        boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25.0);
        boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        
        EntityEquipment equip = boss.getEquipment();
        if (equip != null) {
            ItemStack arma = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta metaArma = arma.getItemMeta();
            if (metaArma != null) {
                metaArma.setDisplayName(ChatColor.RED + "Espada del Vacío");
                metaArma.setUnbreakable(true);
                arma.setItemMeta(metaArma);
            }
            equip.setItemInMainHand(arma);
            equip.setItemInMainHandDropChance(0.0f);

            ItemStack casco = new ItemStack(Material.PURPLE_STAINED_GLASS);
            equip.setHelmet(casco);
            equip.setHelmetDropChance(0.0f);

            equip.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
            equip.setChestplateDropChance(0.0f);
        }
    }

/*

# 21. Ejemplo Armadura

    ItemStack pechera = new ItemStack(Material.NETHERITE_CHESTPLATE);
    ItemMeta meta = pechera.getItemMeta();
    if (meta != null) {
        meta.setDisplayName(ChatColor.AQUA + "Coraza de Escamas");
        meta.setUnbreakable(true);
        pechera.setItemMeta(meta);
    }
    equip.setChestplate(pechera);
    equip.setChestplateDropChance(0.01f);

/

# 22. Combinación de Colores y Estilos

    # Solo Color:
    meta.setDisplayName(ChatColor.RED + "Nombre");

    # Color + Negrita (BOLD): Usamos "" para unir correctamente
    meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Nombre");

    # Color + Itálica (ITALIC):
    meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.ITALIC + "Nombre");

    # Múltiples estilos juntos:
    meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Nombre");

/

# 23. Estilos de Texto (ITALIC y más)

    # Además de colores, existen "modificadores" visuales:
    
    # - ITALIC: Hace que la letra esté inclinada (cursiva). 
    #   Ej: meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.ITALIC + "Susurro...");
    
    # Otros estilos útiles:
    # - BOLD: Negrita (Letra más gruesa).
    # - UNDERLINE: Subrayado.
    # - STRIKETHROUGH: Tachado.
    # - MAGIC: Texto "Matrix" que se mueve y no se puede leer.

/

# 24. Colores Hexadecimales (RGB - Personalizados)

    # Si los 16 colores básicos de Minecraft no te bastan, puedes 
    # usar CUALQUIER color del mundo usando códigos Hex (#RRGGBB).
    
    # - IMPORTANTE: Requiere Minecraft 1.16 o superior.
    # - Formato: net.md_5.bungee.api.ChatColor.of("#C0A026")
    
    # Ejemplo para un color "Oro Rosado":
    meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#FFB6C1") + "Nombre Especial");

/

# 25. Propiedades de ItemMeta (Lista Completa)

    # Estas propiedades se aplican a través del objeto 'meta' 
    # de casi cualquier ítem o armadura:

    # 1. Básicas:
    meta.setDisplayName("Nombre"); # Cambia el nombre visible
    meta.setLore(listaDeStrings); # Añade descripción (varias líneas)
    meta.setUnbreakable(true);    # El ítem nunca se rompe

    # 2. Visibilidad (Flags):
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);   # Oculta "+7 Daño"
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);     # Oculta lista de encantamientos
    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);  # Oculta el texto "Irrompible"
    meta.addItemFlags(ItemFlag.HIDE_DESTROYS);     # Oculta qué bloques puede romper
    meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);    # Oculta dónde se puede poner

    # 3. Datos Técnicos:
    meta.setCustomModelData(123); # Útil para texturas de Resource Packs
    meta.setRepairCost(999);      # Coste de reparación en yunque

    # 4. Exclusivas de Armadura de Cuero (LeatherArmorMeta):
    # Requiere: LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
    # meta.setColor(Color.fromRGB(R, G, B)); # Color exacto del cuero

/

# 26. Atributos Directos (Daño, Armadura, Resistencia)
 
     # Si quieres que un ítem dé estadísticas extra MIENTRAS el mob 
     # lo tiene puesto (como +20 de vida), usamos AttributeModifiers.
 
 /
 
     # Ejemplo Completo: Coraza de Titán (Da 20 de vida extra y 5 de armadura)
     
     public static ItemStack createTitanChestplate() {
         ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
         ItemMeta meta = item.getItemMeta();
         
         if (meta != null) {
             meta.setDisplayName(ChatColor.DARK_AQUA + "Coraza de Titán");
             
             // 1. Crear el modificador de VIDA (+20 puntos / 10 corazones)
             AttributeModifier modifierVida = new AttributeModifier(
                 UUID.randomUUID(), 
                 "titán_salud", 
                 20.0, 
                 AttributeModifier.Operation.ADD_NUMBER, 
                 EquipmentSlot.CHEST
             );
             
             // 2. Crear el modificador de ARMADURA (+5 puntos extra)
             AttributeModifier modifierArmor = new AttributeModifier(
                 UUID.randomUUID(), 
                 "titán_defensa", 
                 5.0, 
                 AttributeModifier.Operation.ADD_NUMBER, 
                 EquipmentSlot.CHEST
             );
             
             // 3. Aplicar los modificadores al Meta
             meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifierVida);
             meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifierArmor);
             
             item.setItemMeta(meta);
         }
         return item;
     }
 
     # Al usar `addAttributeModifier`, borras los
     # atributos base del ítem (el Netherite normal). Por eso, si quieres 
     # mantener la defensa original, debes añadirla tú manualmente.

/

     # Ejemplo
 
     
     public static void addTitanStats(ItemStack item) {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) return;
 
         UUID hpID = UUID.randomUUID();
         UUID defID = UUID.randomUUID();
 
         AttributeModifier hpMod = new AttributeModifier(hpID, "titán_salud", 20.0, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
         AttributeModifier defMod = new AttributeModifier(defID, "titán_defensa", 5.0, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
 
         meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, hpMod);
         meta.addAttributeModifier(Attribute.GENERIC_ARMOR, defMod);
         
         item.setItemMeta(meta);
     }
    
 
 /
 
# 27. Encantamientos (Poder Adicional)

    # Puedes añadir cualquier encantamiento de Minecraft:
    
    # 1. Añadir encantamiento normal (respeta límites):
    # item.addEnchantment(Enchantment.SHARPNESS, 5); 

    # 2. Añadir encantamiento inseguro (rompe límites, ej: Filo 10):
    # item.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);

    # Lista de nombres comunes (Versiones Modernas 1.20+):
    # - SHARPNESS: Filo (Antes DAMAGE_ALL)
    # - PROTECTION: Protección (Antes PROTECTION_ENVIRONMENTAL)
    # - KNOCKBACK: Empuje
    # - POWER: Poder (Antes ARROW_DAMAGE)
    # - FIRE_ASPECT: Aspecto Ígneo
    # - BLAST_PROTECTION: Protecc. Explosiones (Antes PROTECTION_EXPLOSIONS)

/

# 28. Protecciones por Caso Específico

    # Si quieres que una armadura sea "especialista" en proteger 
    # contra un tipo de daño, usa estos encantamientos:
    
    # 1. Protección contra Proyectiles (Flechas, Balas):
    # item.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 4);

    # 2. Protección contra Fuego (Lava, Llamas):
    # item.addEnchantment(Enchantment.FIRE_PROTECTION, 4);

    # 3. Protección contra Explosiones (TNT, Creepers):
    # item.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

    # 4. Protección contra Caída (Botas):
    # item.addEnchantment(Enchantment.FEATHER_FALLING, 4);

    # 5. Protección General:
    # item.addEnchantment(Enchantment.PROTECTION, 4);

    # Nota: Los niveles pueden superar el límite vanilla (IV o V) 
    # si usas 'addUnsafeEnchantment' como vimos en la Sección 27.

/

# 29. Drops Personalizados con Encantamientos

    # Si quieres que al morir un mob suelte un ítem con 
    # propiedades específicas (como Filo 3), se hace así:

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getScoreboardTags().contains("TheKing")) {
            
            # 1. Crear el ítem que queremos dropear
            ItemStack recompensa = new ItemStack(Material.GOLDEN_SWORD);
            
            # 2. Añadir el encantamiento (Filo 3)
            # SHARPNESS es el nombre moderno para el daño físico
            recompensa.addEnchantment(Enchantment.SHARPNESS, 3);
            
            # 3. Añadir el ítem a la lista de drops del evento
            e.getDrops().add(recompensa);
        }
    }

/

# 30. Formas de llamar Encantamientos (Estático vs Dinámico)

    # Existen dos formas de pedirle a Bukkit un encantamiento. 
    # Es importante saber cuándo usar cada una:

    # 1. El Método Estático (Recomendado y Moderno)
    # Usas directamente el nombre del objeto. Es el más rápido y seguro.
    # - Si usas 1.20+: Enchantment.SHARPNESS
    # - Si usas 1.12 a 1.19: Enchantment.DAMAGE_ALL

    # 2. El Método Dinámico (getByName)
    # Buscas el encantamiento usando un texto (String).
    # - Ejemplo: Enchantment.getByName("DAMAGE_ALL");
    
    # ¿Por qué usar getByName?
    # - Compatibilidad: Si tu código debe funcionar en muchas versiones 
    #   distintas del juego a la vez.
    # - Configuración: Si quieres que el usuario escriba el nombre del 
    #   encantamiento en un archivo config.yml.

    # Ejemplo de uso seguro con getByName (para evitar errores):
    Enchantment e = Enchantment.getByName("SHARPNESS");
    if (e == null) {
        e = Enchantment.getByName("DAMAGE_ALL"); // Plan B por si es versión vieja
    }
    if (e != null) {
        item.addEnchantment(e, 3);
    }

**

## 31. Cambiar el Tamaño de una Entidad (Scale)

    # Hacer que un mob sea gigante o diminuto es posible de varias formas, 
    # dependiendo de la versión de Minecraft y el tipo de bicho:

/

    # 1. El Método Moderno (Atributo SCALE - Recomendado 1.20.5+)
    # Minecraft añadió recientemente un atributo universal para cambiar 
    # el tamaño de casi cualquier entidad (Zombies, Esqueletos, etc.)
    
    # Multiplicar tamaño por 2.0 (Gigante):
    boss.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(2.0);
    
    # Reducir tamaño a 0.5 (Miniatura):
    boss.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.5);

/

    # 2. Entidades con Tamaño Especial (Slimes y Phantoms)
    # Algunos bichos tienen un método propio para el tamaño:
    
    Slime slime = (Slime) entidad;
    slime.setSize(10); // Un slime enorme (Suele subir vida y daño también)
    
    Phantom phantom = (Phantom) entidad;
    phantom.setSize(5); // Un phantom mucho más grande de lo normal

/

    # 3. Forzar Estado Bebé (Mini Mobs)
    # Como vimos antes, esto cambia el tamaño visual y la hitbox:
    
    zombie.setBaby(true);

/

    # 4. Aspectos a tener en cuenta:
    # - Hitbox: Si usas el atributo SCALE, la caja de colisión (donde 
    #   le pegas) suele ajustarse automáticamente al nuevo tamaño.
    # - Daño y Vida: Cambiar el tamaño NO cambia automáticamente la 
    #   fuerza del mob. Debes ajustar la Vida y el Daño por separado 
    #   si quieres un "Giga-Zombie" real.
 
 /
 
     # 5. Guía de Tamaños Proporcionales (Basado en bloques)
     # Para entidades de 2 bloques de alto (Zombie/Skeleton):
 
     # | Valor (Scale) | Altura Aprox. | Sentido Visual |
     # | :------------ | :------------ | :------------- |
     # | 0.25          | 0.5 Bloques   | Como un loro   |
     # | 0.50          | 1.0 Bloques   | Como un bicho  |
     # | 1.0           | 2.0 Bloques   | Normal         |
     # | 1.5           | 3.0 Bloques   | Mini-Boss      |
     # | 2.0           | 4.0 Bloques   | Gigante        |
     # | 5.0           | 10.0 Bloques  | ¡GIGANTESCO!   |
 
 /
 
     # 5.1 Guía de Referencia: La Araña
     # La araña es un caso especial porque es más ancha que alta:
     
     # - Escala por Defecto: 1.0
     # - Altura Real: 0.9 Bloques (Casi un bloque completo)
     # - Ancho Real: 1.4 Bloques (Casi un bloque y medio)
     
     # Si quieres una Araña "Pequeña" (tipo Araña de cueva):
     # - Usa Escala: 0.50 (Mediría 0.45 de alto y 0.7 de ancho)

**

 ## 32. Ejemplo: Entidad con Atributos en el Equipo
 
     # En este ejemplo creamos un "Caballero de Élite". 
     # Sus estadísticas no vienen del mob, sino de la espada y la pechera.
 
 /
 
     # 1. El Código:
 
     
     public static void equipEliteKnight(Zombie knight) {
         knight.setCustomName(ChatColor.GOLD + "Elite Knight");
         
         EntityEquipment equip = knight.getEquipment();
         if (equip == null) return;
 
         ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
         ItemMeta swordMeta = sword.getItemMeta();
         if (swordMeta != null) {
             AttributeModifier dMod = new AttributeModifier(UUID.randomUUID(), "att", 15.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
             swordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, dMod);
             sword.setItemMeta(swordMeta);
         }
         equip.setItemInMainHand(sword);
 
         ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
         ItemMeta chestMeta = chest.getItemMeta();
         if (chestMeta != null) {
             AttributeModifier hMod = new AttributeModifier(UUID.randomUUID(), "hp", 40.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
             chestMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, hMod);
             chest.setItemMeta(chestMeta);
         }
         equip.setChestplate(chest);
     }

 
 /
 
     # 2. Explicación por pedazos:
 
     # EL ARMA:
     # - Definimos el daño (+15 puntos) y lo inyectamos en el Meta:
     AttributeModifier dMod = new AttributeModifier(UUID.randomUUID(), "att", 15.0, Operation.ADD_NUMBER, EquipmentSlot.HAND);
     swordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, dMod);
     
     # - Guardamos los cambios y entregamos:
     sword.setItemMeta(swordMeta);
     equip.setItemInMainHand(sword);
 
     # LA ARMADURA:
     # - Creamos vida extra (+40 o 20 corazones) vinculada al pecho:
     AttributeModifier hMod = new AttributeModifier(UUID.randomUUID(), "hp", 40.0, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
     chestMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, hMod);
     
     # - Guardamos y equipamos para que el Zombie suba su vida:
     chest.setItemMeta(chestMeta);
     equip.setChestplate(chest);
 
     # LA ESTRATEGIA:
     # - Los modificadores se deben aplicar sobre el ItemMeta.
     # - Sin 'setItemMeta(meta)', el ítem ignorará los cambios y será normal.
     # - 'UUID.randomUUID()' evita que el juego confunda las estadísticas.
 
 **
 
 ## 33. Velocidad de Ataque y Persistencia de Stats
 
     # Si quieres modificar qué tan rápido puede golpear un jugador con el 
     # arma personalizada, usamos el atributo de velocidad de ataque:
 
 /
 
     # 1. Atributo de Velocidad (GENERIC_ATTACK_SPEED)
     
     AttributeModifier speedMod = new AttributeModifier(
         UUID.randomUUID(), 
         "velocidad_arma", 
         4.0, // Un valor de 4.0 es muy rápido (estilo versiones antiguas)
         AttributeModifier.Operation.ADD_NUMBER, 
         EquipmentSlot.HAND
     );
     meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedMod);
 
 /
 
     # 2. ¿Los stats se mantienen al dropear el ítem?
     # SÍ. Absolutamente todo lo que guardes en el 'ItemMeta' (Encantamientos, 
     # Atributos, Nombres, Lore) se queda grabado en el objeto.
 
     # - Si el mob muere y suelta la espada: El jugador la recogerá con 
     #   exactamente los mismos stats que tenía el mob.
     # - Funciona como un ítem de RPG: Una espada que da +40 de vida al mob, 
     #   le dará +40 de vida al jugador si se la pone en la mano correcta.
 
 **
 
 ## 34. Interacción entre Lore y Atributos
 
     # Es importante entender que el Lore (el texto descriptivo) y los 
     # Atributos (el daño/vida real) son cosas totalmente separadas.
 
 /
 
     # 1. ¿El Lore se borra al cambiar Atributos?
     # NO. Modificar los atributos con 'addAttributeModifier' no toca la lista 
     # de Lore del ítem. El texto que ya tenía el arma se mantendrá igual.
 
 /
 
     # 2. ¿El Lore se actualiza solo si cambio el daño?
     # NO. El Lore es solo "texto decorativo". Si usas un modificador para 
     # que una espada haga 50 de daño, pero el Lore escrito dice "Daño: 10", 
     # el texto seguirá diciendo "Daño: 10".
     
     # Si quieres que el texto coincida con el nuevo daño, debes actualizar 
     # la lista de Lore:
     
     public static void setCustomStats(ItemStack item, double damage) {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) return;
 
         AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "att", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
         meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, mod);
 
         List<String> lore = new ArrayList<>();
         lore.add(ChatColor.GRAY + "Poder: " + ChatColor.RED + damage);
         meta.setLore(lore);
 
         item.setItemMeta(meta);
     }
 
 /
 
     # 4. Explicación por pedazos:
 
     # EL ATRIBUTO:
     # AttributeModifier mod = new AttributeModifier(...);
     # meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, mod);
     # - Define la fuerza real del arma para el motor de Minecraft.
 
     # EL LORE:
     # List<String> lore = new ArrayList<>();
     # lore.add(ChatColor.GRAY + "Poder: " + ChatColor.RED + damage);
     # meta.setLore(lore);
     # - Crea la información visual que el jugador verá al pasar el ratón.
     # - Al usar la variable 'damage', ambos valores siempre serán iguales.
 
     # LA SINCRONIZACIÓN:
     # - Al hacer ambos pasos dentro del mismo método, aseguras que el 
     #   arma nunca mienta sobre su daño real.
 
 /
 
     # 3. Resumen visual en el juego:
     # - Lo que ves en AZUL/GRIS oscuro abajo del ítem son los ATRIBUTOS reales.
     # - Lo que escribes tú con 'setLore' es solo información visual.
     # - Minecraft suele ocultar los atributos base si añades uno personalizado, 
     #   pero el Lore que tú escribiste nunca desaparece solo.
 
 **
 
 ## 35. Despliegue Completo: Clase de Creación de Super Jefe
 
     # Este es un ejemplo de una clase terminada que podrías usar para 
     # invocar un jefe con estadísticas, tamaño y equipo personalizados.
 
 /
 
     # 1. El Código:
 
     package com.Chagui68.weaponsaddon.handlers;
 
     import org.bukkit.*;
     import org.bukkit.attribute.*;
     import org.bukkit.entity.*;
     import org.bukkit.inventory.*;
     import org.bukkit.inventory.meta.*;
     import java.util.*;
 
     public class SuperBossHandler {
 
         public static void spawnSuperBoss(Location loc) {
             Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
             
             boss.setCustomName(ChatColor.DARK_PURPLE + "TITAN OVERLORD");
             boss.setCustomNameVisible(true);
             boss.setRemoveWhenFarAway(false);
             
             boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000.0);
             boss.setHealth(1000.0);
             boss.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(3.0);
             boss.addScoreboardTag("SuperBoss");
             
             equipBoss(boss);
         }
 
         private static void equipBoss(Zombie boss) {
             EntityEquipment equip = boss.getEquipment();
             if (equip == null) return;
 
             ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
             ItemMeta swordMeta = sword.getItemMeta();
             if (swordMeta != null) {
                 AttributeModifier dmg = new AttributeModifier(UUID.randomUUID(), "atk", 50.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                 swordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, dmg);
                 
                 List<String> lore = new ArrayList<>();
                 lore.add(ChatColor.RED + "Daño Destructor: 50.0");
                 swordMeta.setLore(lore);
                 
                 sword.setItemMeta(swordMeta);
             }
             equip.setItemInMainHand(sword);
 
             ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
             ItemMeta chestMeta = chest.getItemMeta();
             if (chestMeta != null) {
                 AttributeModifier hp = new AttributeModifier(UUID.randomUUID(), "hp", 200.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                 chestMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, hp);
                 chest.setItemMeta(chestMeta);
             }
             equip.setChestplate(chest);
         }
     }
 
 /
 
     # 2. Explicación por pedazos:
 
     # EL SPAWN:
     # - Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
     # - Crea la entidad físicamente en el mundo en la ubicación 'loc'.
 
     # ATRIBUTOS DEL MOB:
     # - boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000.0);
     # - Define la vida base del bicho (500 corazones).
     # - boss.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(3.0);
     # - Lo hace 3 veces más grande que un zombie normal (aprox 6 bloques).
 
     # EQUIPO PERSONALIZADO:
     # - El método 'equipBoss' se encarga de fabricar los ítems.
     # - La espada tiene un modificador de daño de 50.0.
     # - La pechera tiene un modificador de vida extra de 200.0.
 
     # SINCRONIZACIÓN SÍ/NO:
     # - En la pechera se añaden los HP extra pero no se puso lore (para comparar).
 
 **
 
 ## 36. Gestión de Lore: Reemplazar vs Añadir
 
     # Es fundamental saber que el método 'setLore' reemplaza toda la lista 
     # de texto del ítem. Si no tienes cuidado, borrarás el lore original.
 
 /
 
     # 1. Reemplazar Lore (Borra lo anterior):
     
     List<String> nuevoLore = new ArrayList<>();
     nuevoLore.add("Solo queda este texto");
     meta.setLore(nuevoLore);
 
 /
 
     # 2. Añadir al Lore (Mantener lo anterior):
     
     List<String> loreActual = meta.getLore();
     if (loreActual == null) {
         loreActual = new ArrayList<>();
     }
     loreActual.add("Nueva línea sin borrar las otras");
     meta.setLore(loreActual);
 
 /
 
     # 3. Ejemplo Reutilizable (Método para añadir):
 
     public static void addLoreLine(ItemStack item, String line) {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) return;
 
         List<String> lore = meta.getLore();
         if (lore == null) lore = new ArrayList<>();
         
         lore.add(ChatColor.translateAlternateColorCodes('&', line));
         meta.setLore(lore);
         
         item.setItemMeta(meta);
     }
 
 /
 
     # Resumen:
     # - meta.getLore(): Te da lo que ya está escrito (o null si está vacío).
     # - meta.setLore(): Guarda la lista completa. Si la lista es nueva, 
     #   lo viejo desaparece.
     # - Siempre pide la lista actual si quieres conservar el texto previo 
     #   (como las descripciones de Slimefun).
 
 **
 
 ## 37. Rendimiento y Spark (Evitar Lag)
 
     # Si Spark detecta que tu addon causa lag, lo más probable es que sea 
     # por el uso excesivo del Scheduler en el hilo principal.
 
 /
 
     # 1. El Problema: runTaskTimer (Síncrono)
     # Cuando ejecutas algo cada 1, 2 o 5 ticks síncronamente, Minecraft 
     # tiene que esperar a que tu código termine antes de seguir con el 
     # siguiente tick del servidor. Si tienes muchos jefes disparando 
     # a la vez, el TPS bajará.
 
 /
 
     # 2. La Solución: runTaskTimerAsynchronously
     # Si tu código solo hace cálculos matemáticos, efectos de partículas 
     # o sonidos, puedes enviarlo al hilo asíncrono. Esto libera al 
     # hilo principal de carga.
     
     new BukkitRunnable() {
         public void run() {
             // Lógica pesada aquí (Cálculos de vectores, partículas)
         }
     }.runTaskTimerAsynchronously(plugin, 0L, 2L);
 
 /
 
     # 3. Reglas de Oro para evitar Lag:
     
     # - NO toques la API de Bukkit en hilos asíncronos: No puedes usar 
     #   'setHealth', 'teleport' o 'damage' dentro de un hilo ASYNC.
     
     # - Estrategia Híbrida:
     #   1. El Scheduler ASYNC calcula la trayectoria y pone las partículas.
     #   2. Cuando detectas un impacto, usas 'runTask' para volver al 
     #      hilo principal solo para aplicar el daño.
     
     #   cada 2 ticks? A veces, subirlo a 5 o 10 ticks (0.5s) visualmente 
     #   es casi igual pero reduce el uso de CPU a la mitad.
 
 **


 ## 38. Boss Reinforcements: Damage Detection (The King)
 
     # Los jefes pueden invocar ayuda cuando son atacados. Para evitar 
     # que spawneen infinitos minions, usamos un cooldown con Metadatos.
 
 /
 
     # 1. Detectar daño al Jefe:
     if (entity.getScoreboardTags().contains("TheKing")) {
         // Revisar cooldown de 25 segundos
         if (entity.hasMetadata("king_summon_cd")) {
             long cd = entity.getMetadata("king_summon_cd").get(0).asLong();
             if (System.currentTimeMillis() < cd) return;
         }
 
         // Invocar minions a los lados
         spawnWarriors(entity.getLocation());
 
         // Establecer nuevo cooldown
         entity.setMetadata("king_summon_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + 25000));
     }
 
 **
 
 ## 39. Charging Dash AI: 2-Phase Movement
 
     # Esta IA hace que la entidad se quede quieta (cargando) antes 
     # de salir disparada hacia el jugador.
 
 /
 
     # Fase 1: Carga (Inmovilizar)
     # Aplicamos Slowness 255 por 1 segundo y efectos visuales.
     entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 255));
     entity.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 0.5f);
 
 /
 
     # Fase 2: Impulso (Dash)
     # Usamos un scheduler para que el dash ocurra después del segundo de carga.
     new BukkitRunnable() {
         public void run() {
             Vector dash = target.getLocation().subtract(loc).toVector().normalize();
             entity.setVelocity(dash.multiply(1.5).setY(0.2));
         }
     }.runTaskLater(plugin, 20L);
 
 **

---

## 40. Heavy Gunner Boss: Sistema Completo

    # El Heavy Gunner es un jefe basado en Skeleton con múltiples sistemas
    # interconectados: fases, arena, refuerzos y tracking de daño.

/

### 40.1 Estructura del BossAIHandler

    # Este archivo contiene TODA la lógica del jefe:
    
    BossAIHandler.java
    ├── Constantes de configuración (daño, cooldowns, tiempos)
    ├── Variables estáticas (arena, boss bar, tracking)
    ├── onProjectileHit() → Detecta daño al jefe
    ├── scanAndShoot() → Loop principal de IA
    ├── handleShooting() → Sistema de disparo
    ├── executeReinforcementCall() → Llamada de refuerzos
    ├── handleBattleWitchAI() → IA de la bruja
    ├── buildArena() / destroyArena() → Gestión de arena
    ├── setupBossBar() → Barra de jefe
    └── onBossDeath() → Limpieza y leaderboard

/

### 40.2 Sistema de Fases Dinámico

    # Las fases se calculan automáticamente basándose en el HP:
    
    private int calculatePhase(double healthPercent) {
        if (healthPercent > 0.857) return 1;
        if (healthPercent > 0.714) return 2;
        if (healthPercent > 0.571) return 3;
        if (healthPercent > 0.428) return 4;
        if (healthPercent > 0.285) return 5;
        if (healthPercent > 0.142) return 6;
        return 7;
    }
    
    # Cada fase cambia:
    # - Color de la Boss Bar
    # - Título de la fase
    # - Cooldown de habilidades
    # - Daño de las balas

/

### 40.3 Daño Progresivo de Balas

    # Las balas del jefe hacen más daño según la fase actual:
    
    double phaseDamage = baseDamage + (currentBossPhase * 3.0);
    
    # Ejemplo:
    # Fase 1: 5 + 3 = 8 de daño
    # Fase 4: 5 + 12 = 17 de daño
    # Fase 7: 5 + 21 = 26 de daño

/

### 40.4 Cap de Daño (Damage Cap)

    # Para evitar que armas muy poderosas maten al jefe de un golpe:
    
    if (e.getDamage() > 1000.0) {
        e.setDamage(1000.0);
    }
    
    # Esto asegura que el jefe SIEMPRE sobreviva al menos 1 golpe.

**

## 41. Arena de Combate

    # La arena es un cubo de cristal rojo que encierra al jefe y jugadores.

/

### 41.1 Construcción de Arena

    # Se guarda el bloque ORIGINAL antes de reemplazarlo:
    
    for (x, y, z en el área) {
        Location blockLoc = new Location(world, x, y, z);
        Block block = blockLoc.getBlock();
        
        // IMPORTANTE: Guardar el bloque original
        originalBlocks.put(blockLoc, block.getType());
        arenaBlocks.add(blockLoc);
        
        // Colocar cristal rojo
        block.setType(Material.RED_STAINED_GLASS);
    }

/

### 41.2 Destrucción de Arena

    # Al destruir, se restauran los bloques originales:
    
    public static void destroyArena() {
        for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
        arenaBlocks.clear();
        originalBlocks.clear();
    }
    
    # ¡El mapa DEBE ser Map<Location, Material> para recordar qué había!

**

## 42. Reinforcement Call (Llamada de Refuerzos)

    # Sistema de dados que determina qué entidad aparece.

/

### 42.1 Animación de Dados

    # Se muestra un dado animado en títulos:
    
    for (int tick = 0; tick < 10; tick++) {
        int randomNumber = random.nextInt(6) + 1;
        showTitle("🎲 " + randomNumber + " 🎲");
        wait(4 ticks);
    }
    // Resultado final
    int finalRoll = random.nextInt(6) + 1;

/

### 42.2 Spawns por Dado

    switch (diceRoll) {
        case 1: spawnWarrior();     break;
        case 2: spawnPusher();      break;
        case 3: spawnTheKing();     break;
        case 4: spawnEliteKiller(); break;
        case 5: spawnEliteRanger(); break;
        case 6: spawnBattleWitch(); break;
    }

**

## 43. Battle Witch: IA de Pociones

    # La bruja tiene su propia IA que lanza pociones malditas.

/

### 43.1 Sistema de Cooldown

    # Cada bruja tiene su propio cooldown individual:
    
    if (witch.hasMetadata("witch_potion_cd")) {
        long cd = witch.getMetadata("witch_potion_cd").get(0).asLong();
        if (System.currentTimeMillis() < cd) return; // Aún en cooldown
    }
    
    // Ejecutar habilidad...
    
    // Establecer cooldown de 8 segundos
    witch.setMetadata("witch_potion_cd", 
        new FixedMetadataValue(plugin, System.currentTimeMillis() + 8000));

/

### 43.2 Pociones Personalizadas

    # Las pociones tienen MÚLTIPLES efectos combinados:
    
    switch (diceRoll) {
        case 1: // Starvation Brew
            addEffect(HUNGER, 10s, nivel 5);
            addEffect(CONFUSION, 10s, nivel 2);
            break;
        case 4: // Inferno Draught
            addEffect(SLOWNESS, 12s, nivel 3);
            target.setFireTicks(240); // Fuego manual
            break;
    }
    
    # El fuego se aplica MANUALMENTE porque no existe poción de fuego.

**

## 44. Sistema de Recompensas (Boss Rewards)

    # Al morir el jefe, se elige a un jugador para recibir un premio aleatorio.

/

### 44.1 Selección de Jugador (Radio 10)

    # Se prioriza a los jugadores que estuvieron cerca en el combate:
    
    List<Player> nearby = deathLoc.getPlayersInRange(10);
    if (!nearby.isEmpty()) {
        luckyPlayer = nearby.get(random);
    } else {
        luckyPlayer = getClosestPlayer();
    }

/

### 44.2 Entrega de Recompensa (32 items)

    # El sistema elige uno de los 32 objetos disponibles:
    
    ItemStack reward = rewardsList.get(random.nextInt(32));
    player.getInventory().addItem(reward);
    
    # Los premios incluyen Componentes de Élite, Armas, Munición y Vales Especiales.
    # Se han separado las categorías en el Libro de Slimefun:
    # - Military Workbenches: Estaciones de trabajo.
    # - War Machines: Máquinas de combate avanzadas (Bombardment Terminal).
    # - Military Vouchers: Vales de recompensa registrados oficialmente.
    
    # Si el inventario está lleno, se suelta en el suelo automáticamente.

**

## 45. Despawn por Inactividad

    # Si nadie ataca al jefe por 60 segundos, desaparece.

/

### 45.1 Tracking de Último Daño

    # Se guarda el timestamp del último daño recibido:
    
    entity.setMetadata("last_damage_taken", 
        new FixedMetadataValue(plugin, System.currentTimeMillis()));

/

### 45.2 Verificación en Loop de IA

    # Cada tick del loop de IA se verifica:
    
    long lastDamage = entity.getMetadata("last_damage_taken").get(0).asLong();
    
    if (System.currentTimeMillis() - lastDamage > 60000) { // 60 segundos
        destroyArena();
        cleanupBossBar();
        entity.remove();
        broadcast("The Heavy Gunner has retreated...");
    }

**

## 46. Comando /resetarena

    # Comando de emergencia para resetear arenas bugeadas.

/

### 46.1 Implementación

    public class ResetArenaCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, ...) {
            if (!sender.hasPermission("militaryarsenal.admin")) {
                sender.sendMessage("No tienes permiso!");
                return true;
            }
            
            BossAIHandler.destroyArena();
            sender.sendMessage("Arena reseteada!");
            return true;
        }
    }

/

### 46.2 Registro en Plugin

    # En WeaponsAddon.java:
    getCommand("resetarena").setExecutor(new ResetArenaCommand());
    
    # En plugin.yml:
    commands:
      resetarena:
        permission: militaryarsenal.admin

**

