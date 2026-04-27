# Quick Start Guide - TebakSiapakahAku

## Pre-Game Setup (5 minutes)

```bash
# 1. Register all 5 players
/regis Alice
/regis Bob
/regis Charlie
/regis David
/regis Eve

# 2. Verify registration
/listplayer

# Output should show:
# ━━━━━━━━━━━━━━━━━━━━━━━━━━
#      DAFTAR PLAYER
# ━━━━━━━━━━━━━━━━━━━━━━━━━━
#   • Alice | Lintasan 1 | Score: 0/10
#   • Bob | Lintasan 2 | Score: 0/10
#   • Charlie | Lintasan 3 | Score: 0/10
#   • David | Lintasan 4 | Score: 0/10
#   • Eve | Lintasan 5 | Score: 0/10
# ━━━━━━━━━━━━━━━━━━━━━━━━━━
```

## Game Start (1 minute)

```bash
# 1. Stand at board spawn location (pick a good spot with space)
# Run command from your location - board will spawn relative to you

/startboard

# Output:
# ✓ Board berhasil di-spawn! (55 pijakan)
# • Alice di teleport ke lane 1, posisi start
# • Bob di teleport ke lane 2, posisi start
# ... (all 5 players teleported to position 0 of their lanes)

# 2. Start game (unlock movement - players free to walk around)
/startgame

# Output:
# ═══════════════════
# ✦ GAME DIMULAI! ✦
# ═══════════════════

# NOTE: Player bisa jalan bebas. Jika jatuh, mereka bisa /cekposisi untuk kembali ke pijakan.
```

## Question Round (per soal - 2-3 menit)

```bash
# 1. Load question (reset scoring counter)
/setsoal obsidian

# Output:
# ━━━━━━━━━━━━━━━━━
# ✓ Soal Baru Dimulai: OBSIDIAN
# ━━━━━━━━━━━━━━━━━

# 2. Show clues one by one (wait 10-15 seconds between each)
/clue1
# Players see Title: "CLUE 1" subtitle "Aku bisa ditaruh berbagai jenis World."

(wait 15 seconds)
/clue2
# Players see Title: "CLUE 2" subtitle "Aku membutuhkan percikan api agar aku berguna."

(wait 15 seconds)
/clue3
# Players see Title: "CLUE 3" subtitle "Aku bisa memindahkan kalian ke dunia lain."

# 3. Players start answering via chat (SILENT - you won't see in chat)
# Player types: "obsidian"
# ✓ Player auto-advances to next position
# ✓ Sound effect plays (level up sound)
# ✓ Broadcast: "✓ [PlayerName] menjawab dengan benar! (Jawaban ke-1) (Progress: 1/10)"

# If player falls: they can use
/cekposisi
# to return to their current pijakan

# 4. After everyone answers or ready to reveal
/jawaban

# Output:
# ━━━━━━━━━━━━━━━━━
# JAWABAN: OBSIDIAN
# ━━━━━━━━━━━━━━━━━

# (Players can see chat again now if they want to know how many got it right)
```

## Game End

```bash
# When a player reaches position 10:
# ✓ They automatically WIN
# 
# Broadcast:
# ╔═══════════════════╗
# ║     PEMENANG!     ║
# ║  [PlayerName]     ║
# ╚═══════════════════╝
#
# Game continues for other players or you can:

# For next round of questions:
/setsoal creeper  # Load new question

# To completely end game:
# (Just don't run /setsoal, or unregister players)
/unregis Alice
/unregis Bob
...
```

## Game Commands Quick Reference

| Command | What It Does | Who | When To Use |
|---------|------------|-----|-----------|
| `/regis <name>` | Add player to game | Moderator | Before board spawn |
| `/unregis <name>` | Remove player | Moderator | If DC or early end |
| `/listplayer` | Show all players + score | Moderator | Anytime to check |
| `/startboard` | Spawn board & teleport to pos 0 | Moderator | After regis, before /startgame |
| `/startgame` | Unlock movement & start game | Moderator | Before first question |
| `/setsoal <id>` | Load new question | Moderator | Before showing clues |
| `/clue1` | Show first clue | Moderator | After /setsoal |
| `/clue2` | Show second clue | Moderator | 10-15 sec after /clue1 |
| `/clue3` | Show third clue | Moderator | 10-15 sec after /clue2 |
| `/jawaban` | Reveal answer | Moderator | After everyone answered |
| `/cekposisi` | Return to pijakan | Player | If fell off or lost |

## Tips & Tricks

✓ **Free Movement**: Player bisa jalan, berlari, melompat di sekitar board
✓ **Return to Pijakan**: Gunakan `/cekposisi` untuk kembali jika tersesat atau jatuh
✓ **No PvP**: Player TIDAK bisa menyakiti satu sama lain - fokus pada soal!
✓ **Spacing Between Clues**: Wait 15 seconds between each clue untuk waktu think
✓ **Answer Timing**: Jangan reveal answer terlalu cepat - give players 30-45 seconds
✓ **Multiple Rounds**: Run `/setsoal <new_id>` untuk start another question
✓ **Player Disconnected**: Use `/unregis <name>` untuk free up lane
✓ **Wrong Answer**: Player bisa retry infinite times per question
✓ **Board Size**: 55 total pijakan (5 lanes x 11 positions: 0-10)
✓ **See Correct Answers**: Check your questions.yml file untuk spelling reference

## Example Full Game (3 players, 2 rounds)

```
/regis Alice
/regis Bob
/regis Charlie
/listplayer
/startboard      ← Players teleport to start positions (position 0)
/startgame       ← Movement unlocked, game active

/setsoal obsidian
/clue1 (wait 15s)
/clue2 (wait 15s)
/clue3 (wait 45s for answers)
/jawaban

/setsoal creeper
/clue1 (wait 15s)
/clue2 (wait 15s)
/clue3 (wait 45s)
/jawaban

# Alice reaches position 10 first
# AUTO-ANNOUNCE: "ALICE WINS!"
# Game continues for Bob & Charlie...
```

---

**Good luck! Have fun! 🎮**

