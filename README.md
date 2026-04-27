# TebakSiapakahAku Plugin

## Deskripsi
Plugin Minecraft Paper untuk minigame seru "Tebak Siapakah Aku" - Reality Show berbasis pergerakan pijakan dengan sistem kuis item/mob Minecraft.

### Fitur Utama
- ✅ Registrasi hingga 5 player per sesi  
- ✅ Silent answer system (jawaban via chat, tidak terlihat publik)
- ✅ **Free movement** (player bisa jalan, gunakan `/cekposisi` untuk kembali ke pijakan)
- ✅ 3 Clue system dengan broadcast title
- ✅ Scoring system (0-10 pijakan, 11 total positions)
- ✅ Winner announcement dengan title dan sound effects
- ✅ Anti-PvP system (player tidak bisa pukul-pukul)
- ✅ Moderator-only commands

---

## Setup & Build

### Requirements
- **Java 18+** (minimal)
- **Maven 3.6+** atau **Gradle 7+**
- **Paper API 1.21.1** (atau compatible version)

### Build Plugin JAR

#### Option 1: Menggunakan Maven (Recommended)
```bash
cd H:\Project\ Minecraft\ Plugin\siapakahAku
mvn clean package
```
JAR file akan tersimpan di `target/siapakahAku-1.0-SNAPSHOT.jar`

#### Option 2: Menggunakan Maven Wrapper (Jika ada di project)
```bash
./mvnw clean package
```

#### Option 3: Manual Compile dengan Javac
```bash
javac -d classes src/main/java/yt/corazonid/siapakahAku/**/*.java
jar cvf siapakahAku.jar -C classes .
```

---

## Instalasi ke Server Paper

1. Copy JAR file ke folder `plugins/` di Paper server
2. Restart server
3. Plugin akan auto-enable dengan config di `plugins/siapakahAku/`

---

## Game Flow

### Moderator Commands

#### Player Management
```
/regis <playername>        → Register player ke lane kosong (1-5)
/unregis <playername>      → Unregister player dari game
/listplayer                → Lihat daftar semua player + score
```

#### Board Setup
```
/startboard                → Spawn 5x10 pijakan board & teleport player ke start
```

#### Game Control
```
/startgame                 → Unlock all player movement & aktifkan game session
/setsoal <question_id>     → Load soal (obsidian, creeper, diamond, etc.)
/clue1                     → Broadcast clue pertama
/clue2                     → Broadcast clue kedua
/clue3                     → Broadcast clue ketiga
/jawaban                   → Reveal jawaban yang benar
```

#### Player Commands
```
/cekposisi                 → Return ke pijakan saat ini (jika jatuh atau tersesat)
```

### Complete Player Flow

1. **Setup Phase**
   - Moderator `/regis Alice` → Alice assigned ke Lane 1
   - Moderator `/regis Bob` → Bob assigned ke Lane 2
   - ... (repeat untuk 5 player)
   - Moderator `/listplayer` → Verify semua player registered

2. **Board Spawn Phase**
   - Moderator stand di start location
   - Moderator `/startboard` → Build 5 lanes x 10 positions board
   - Semua player auto-teleport ke pijakan start mereka (Lane X, Position 1)

3. **Game Start Phase**
   - Moderator `/startgame` → Game active, all player **free to move around**
   - Pijakan sudah terlihat, player siap menjawab
   - Jika player jatuh: player bisa `/cekposisi` untuk kembali ke pijakan mereka

4. **Question Phase** (repeat per soal)
   - Moderator `/setsoal obsidian` → Load soal, reset player answer counter
   - Moderator `/clue1` → Title broadcast clue 1 ke semua player
   - Player menunggu...
   - Moderator `/clue2` → Title broadcast clue 2
   - Player mulai think/chat...
   - Moderator `/clue3` → Title broadcast clue 3
   - **Player answer via chat** (silent) → Auto-detect benar/salah
     - ✓ Benar → Player teleport maju 1 posisi + sound + broadcast success
     - ✗ Salah → Private message ke player + wrong sound
   - Moderator `/jawaban` → Reveal jawaban ke public

5. **Win Condition**
   - Player mencapai posisi 10 → Winner!
   - Auto-announce winner + fireworks + title global
   - Game bisa continue ke soal berikutnya atau end
   - Player yang sudah menang di luar dan bisa lihat yang lain bermain

---

## Board Structure

### Board Structure

### Layout
- **5 Parallel Lanes** (Lane 1 - Lane 5)
- **11 Positions per Lane** (Position 0 - Position 10)
- **Total: 55 Pijakan** (2x2 platform blocks)

### Material Colors
- **WHITE_CONCRETE** → Position 0 (START - all players begin here)
- **LIME_CONCRETE** → Position 10 (FINISH - winning condition)
- **BLUE_CONCRETE** → Even positions (2, 4, 6, 8)
- **PURPLE_CONCRETE** → Odd positions (1, 3, 5, 7, 9)

### Spacing
- **4 blocks** jarak antar posisi dalam satu lane (Z-axis/forward)
- **5 blocks** jarak antar lane (X-axis/left-right)
- Platform blocks spawned di Y-1, teleport di Y+0.5 (atas blok)

---

Edit `questions.yml` untuk menambahkan soal baru:

```yaml
questions:
  obsidian:
    clue1: "Aku bisa ditaruh berbagai jenis World."
    clue2: "Aku membutuhkan percikan api agar aku berguna."
    clue3: "Aku bisa memindahkan kalian ke dunia lain."
    answer: "obsidian"
  
  crystal:
    clue1: "..."
    clue2: "..."
    clue3: "..."
    answer: "crystal"
```

---

## Permissions

```yaml
siapakahaku.moderator  → Access ke semua moderator commands (default: OP)
```

---

## Sound Effects

- **✓ Jawaban Benar** : ENTITY_PLAYER_LEVELUP
- **✗ Jawaban Salah** : ENTITY_VILLAGER_NO  
- **⚡ Game Start** : ENTITY_EXPERIENCE_ORB_PICKUP

---

## Architecture

```
src/main/java/yt/corazonid/siapakahAku/
├── SiapakahAku.java           # Main plugin class
├── manager/
│   └── GameManager.java       # Core game logic
├── model/
│   ├── GamePlayer.java        # Player data model
│   └── Question.java          # Question data model
├── commands/
│   ├── RegisCommand.java
│   ├── UnregisCommand.java
│   ├── ListPlayerCommand.java
│   ├── StartGameCommand.java
│   ├── SetSoalCommand.java
│   ├── ClueCommand.java
│   └── JawabanCommand.java
├── listener/
│   ├── ChatListener.java      # Silent answer logic
│   └── PlayerMoveListener.java # Movement freeze logic
└── util/
    ├── TitleUtil.java         # Broadcast title
    └── SoundUtil.java         # Sound effects

resources/
├── plugin.yml                 # Plugin metadata & commands
└── questions.yml              # Questions database
```

---

## Bug Fixes & Improvements (v1.1 Update)

### Fixed Issues
❌ **Problem**: Pijakan tidak ter-spawn, player tidak punya lokasi untuk maju
✅ **Solution**: 
- Buat `BoardBuilder.java` untuk spawn 5x11 board auto-generate
- Add `/startboard` command untuk spawn dan teleport player ke start (posisi 0)
- Track board locations di `GameManager` dengan key format "lane_position"

❌ **Problem**: Player jawab benar tapi notif mengatakan "jawaban ke-2" padahal soal ke-1
✅ **Solution**: 
- Track `correctAnswerCountThisRound` variable untuk setiap soal
- Reset counter ke 0 saat `/setsoal` dipanggil
- Display counter yang akurat dalam broadcast message

### New Features (v1.2 Update)
❌ **Problem**: Movement freeze membuat teleport antar pijakan tidak nyaman
✅ **Solution**: 
- Remove movement freeze - player bisa jalan bebas ke manapun
- Add `/cekposisi` command - player bisa kembali ke pijakan mereka dengan command
- Add starting position 0 - jadi 11 total positions (0-10) per lane, 55 total pijakan

❌ **Problem**: Player saling pukul dan gang up satu sama lain
✅ **Solution**: 
- Create `EntityDamageListener.java` untuk disable PvP antar player terdaftar
- Prevent player-vs-player damage dengan automatic cancel event

### Optimizations
- Player auto-teleport ke pijakan berikutnya saat jawaban benar
- Silent answer system fully integrated (no chat spam)
- Game state properly reset per soal
- Free movement untuk better game experience
- Anti-PvP untuk menjaga fokus pada soal

## Troubleshooting

### Plugin tidak load
- Check `plugins/siapakahAku/` folder exist
- Verify `plugin.yml` syntax (especially indentation)
- Check `latest.log` di server untuk errors

### Board tidak ter-spawn
- Pastikan ada enough space di lokasi moderator (minimal 30 blocks ke forward & 25 blocks ke side)
- Pastikan player terdaftar sebelum `/startboard`
- Check console untuk error messages

### Commands tidak recognize
- Pastikan plugin enabled: `/plugins` (lihat TebakSiapakahAku di list)
- Cek permission moderator punya: `/perm group <group> siapakahaku.moderator true`
- Atau set moderator OP: `/op <playername>`
- Reload plugin jika perlu: `/reload confirm` (atau restart)

### Chat answer tidak terdeteksi  
- Pastikan game active: run `/startgame` (status di broadcast message)
- Pastikan soal loaded: run `/setsoal <valid_id>`
- Verify soal ID dari questions.yml (case-insensitive)
- Player harus type **persis** jawaban (case-insensitive tapi harus tepat spelling)

### Player tidak maju saat jawab benar
- Check apakah board sudah ter-spawn: `/startboard`
- Verify player di lane 1-5 di questions config
- Check console untuk teleport errors
- Pastikan player belum mencapai posisi 10 (already won)

### Score ditampilkan salah
- Jawab dengan `/setsoal <id>` baru, counter akan reset
- Score = current position (1-10)
- Counter correct answers per soal independent dari score

---

## Author
**Corazon (Irgi Dwiputra)**  
Created: 2026-04-27  
Version: 1.0.0  
Platform: Paper API 1.21+

