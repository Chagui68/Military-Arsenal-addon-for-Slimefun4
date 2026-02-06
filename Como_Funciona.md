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
 
 **
