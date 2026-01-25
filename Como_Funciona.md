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
    # automáticamente. Cada línea "import" le dice al compilador dónde
    # encontrar una clase específica cuando la menciones en el código,
    # evitando tener que escribir la ruta completa cada vez (por ejemplo,
    # escribir solo "Material" en lugar de "org.bukkit.Material" repetidamente).

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
            return "https://github.com/Chagui68/WeaponsAddon/issues";
        }

    También obligatorio de SlimefunAddon. Devuelve la URL donde reportar bugs. 
    Slimefun la muestra en /sf versions y en mensajes de error.    

**

