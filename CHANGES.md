# TebakSiapakahAku Changelog

## v1.2 (Current) - Free Movement & PvP Prevention

### What Was Fixed

**🐛 Issue #3: Movement Freeze "Teleport Hopping" Uncomfortable**
- Old behavior: `/startgame` froze player movement completely (WASD blocked)
- Problem: Every time player gets teleported to next pijakan, POV jumps suddenly
- Solution: ✅ Removed movement freeze completely
  - Player now free to walk, run, jump around board
  - Use `/cekposisi` command to return to current pijakan if lost

**🐛 Issue #4: Players Punching Each Other**
- Old behavior: PvP enabled - players could punch each other
- Problem: During gameplay, players would gang up and punch one another
- Solution: ✅ Added `EntityDamageListener.java`
  - Automatically cancel player-vs-player damage
  - Players still take fall/environment damage
  - Focused on solving the quiz, not fighting

**🎮 Feature #1: Starting Position 0**
- Old: Positions 1-10 per lane (10 pijakan total)
- New: Positions 0-10 per lane (11 pijakan total)
  - Position 0 = START (all players begin here with white concrete)
  - Position 1-9 = progression (purple/blue concrete alternating)
  - Position 10 = FINISH (lime concrete - winning condition)
- Board now has 55 total pijakan (5 lanes × 11 positions)

### New Features Added

#### Commands
- **`/cekposisi`** - Player command to return to their current pijakan
  - No cooldown - use anytime
  - Shows current lane and position

#### Listeners
- **`EntityDamageListener.java`** - Prevents PvP between registered players
  - Blocks player-vs-player punch/melee damage
  - Still allows fall damage and environment damage
  - Message feedback: "✗ PvP dinonaktifkan! Fokus menjawab soal saja."

### Modified Files

| File | Change Type | Details |
|------|-------------|---------|
| `GamePlayer.java` | 🔄 UPDATED | Start from position 0 (was 1), clamp 0-10 (was 1-10) |
| `BoardBuilder.java` | 🔄 UPDATED | Generate 11 positions per lane (was 10), material logic for pos 0 |
| `StartBoardCommand.java` | 🔄 UPDATED | Teleport to "posisi start" explicitly, comment update |
| `PlayerMoveListener.java` | 🔄 UPDATED | Disabled - now allows all movement |
| `EntityDamageListener.java` | ✨ NEW | Prevent player-vs-player damage |
| `CekposisiCommand.java` | ✨ NEW | Command for players to return to pijakan |
| `plugin.yml` | 🔄 UPDATED | Added `/cekposisi` command + permission `siapakahaku.player` |
| `SiapakahAku.java` | 🔄 UPDATED | Register CekposisiCommand and EntityDamageListener |
| Documentation | 🔄 UPDATED | README, QUICKSTART, this file |

### API Changes

#### GamePlayer
```java
// Changed from 1-10 to 0-10 range
getCurrentPosition() : int      // now 0-10
setCurrentPosition(int) : void  // now clamps 0-10
hasWon() : boolean              // still returns >= 10 (now position 10 = win)
```

#### GameManager  
No new methods, but `registerCorrectAnswer()` and `getCorrectAnswerCountThisRound()` work with resets per `/setsoal`

### Game Flow Changes

**Before (v1.1)**:
1. `/startgame` → Players frozen (can't move)
2. Clue displayed → Player answers
3. Correct answer → Auto-teleport → Camera jarring experience

**After (v1.2)**:
1. `/startgame` → Players free to move
2. Clue displayed → Player walks around, answers
3. Correct answer → Auto-teleport (smooth movement in background)
4. Fall off? → Use `/cekposisi` (any player can use anytime)

### Configuration

#### plugin.yml Changes
```yaml
cekposisi:
  description: Return player to their current pijakan
  usage: /cekposisi
  permission: siapakahaku.player

permissions:
  siapakahaku.player:
    description: Allow player commands (cekposisi)
    default: true  # All players can use
```

### Known Limitations

- `/cekposisi` only works if board was spawned with `/startboard`
- PvP prevention only works for registered players (non-registered players CAN still PvP each other)
- No animation for movement - just teleport (smoothly though)

### Migration Notes

**If upgrading from v1.1 to v1.2**:
1. Rebuild JAR: `mvn clean package`
2. Back up old JAR if needed
3. Replace plugin file
4. **No data migration needed** - all changes are code/config only
5. Existing questions.yml and game saves (if any) remain compatible

### Testing Checklist

- [x] Build passes: `mvn clean package`
- [x] 5 players register to different lanes
- [x] `/startboard` spawns 55 pijakan (should see WHITE at start, LIME at finish per lane)
- [x] `/startgame` does NOT freeze movement
- [x] Players can walk/run/jump freely around board
- [x] `/cekposisi` teleports player back to their current pijakan
- [x] Players cannot punch each other (PvP blocked)
- [x] Answers still work: teleport to next position, counter resets with `/setsoal`
- [x] Winning condition: position 10 → announce winner

---

## Previous Versions

### v1.1 - Board System & Scoring Fixes
- ✅ Added BoardBuilder for 5x10 pijakan spawn
- ✅ Added `/startboard` command
- ✅ Fixed scoring counter reset on `/setsoal`

### v1.0 - Initial Release
- ✅ Core gameplay: regis/unregis, clues, silent answers
- ✅ Command system: /clue1, /clue2, /clue3, /jawaban
- ✅ Scoring system: 1-10 progression
- ✅ Sound effects & title broadcasts

---

**Version**: 1.2  
**Date**: 2026-04-27  
**Breaking Changes**: None (fully backward compatible)

